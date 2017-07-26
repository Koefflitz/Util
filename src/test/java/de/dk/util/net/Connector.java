package de.dk.util.net;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;
import java.util.function.Supplier;

public class Connector<C extends Connection> {
   public static final int PORT = 10000;
   public static final long TIMEOUT = 4096;

   private final Supplier<C> clientConnectionSupplier;
   private final Function<Socket, C> serverConnectionSupplier;

   private C clientConnection;
   private C serverConnection;

   public Connector(Supplier<C> clientConnectionSupplier,
                    Function<Socket, C> serverConnectionSupplier) {
      this.clientConnectionSupplier = clientConnectionSupplier;
      this.serverConnectionSupplier = serverConnectionSupplier;
   }

   public static Connector<SimpleConnection> simpleConnector() {
      return new Connector<>(Connector::createClientConnection, Connector::createServerConnection);
   }

   private static SimpleConnection createClientConnection() {
      try {
         return new SimpleConnection("localhost", PORT);
      } catch (IOException e) {
         fail(e.getMessage());
      }
      return null;
   }

   private static SimpleConnection createServerConnection(Socket socket) {
      try {
         return new SimpleConnection(socket);
      } catch (IOException e) {
         fail(e.getMessage());
      }
      return null;
   }

   public void connect() {
      Thread serverThread = null;
      try {
         ServerSocket serverSocket = new ServerSocket(PORT);
         serverThread = new Thread(() -> runServer(serverSocket));
      } catch (IOException e) {
         fail(e.getMessage());
      }

      serverThread.start();

      this.clientConnection = clientConnectionSupplier.get();
      clientConnection.start();

      try {
         if (serverThread != null && serverThread != Thread.currentThread())
            serverThread.join(TIMEOUT);
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
      if (serverConnection == null)
         fail("Server connection null");

      try {
         serverConnection.waitForRunning(TIMEOUT);
         clientConnection.waitForRunning(TIMEOUT);
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
   }

   private void runServer(ServerSocket serverSocket) {
      try {
         Socket socket = serverSocket.accept();
         if (socket == null)
            fail("socket was null");

         this.serverConnection = serverConnectionSupplier.apply(socket);
         serverConnection.start();
      } catch (IOException e) {

      }
      try {
         serverSocket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public C getClientConnection() {
      return clientConnection;
   }

   public C getServerConnection() {
      return serverConnection;
   }
}