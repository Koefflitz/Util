package de.dk.util.net;

import static de.dk.util.net.Connector.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dk.util.channel.Sender;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ConnectionTest {
   private SimpleConnection clientConnection;
   private SimpleConnection serverConnection;

   private TestReceiver serverReceiver;
   private TestReceiver clientReceiver;

   public ConnectionTest() {

   }

   private static void close(Connection connection) {
      try {
         connection.close(TIMEOUT);
      } catch (IOException | InterruptedException e) {
         e.printStackTrace();
      }
   }

   @BeforeEach
   public void init() {
      this.serverReceiver = new TestReceiver();
      this.clientReceiver = new TestReceiver();

      Connector<SimpleConnection> connector = Connector.simpleConnector();

      connector.connect();
      this.clientConnection = connector.getClientConnection();
      this.serverConnection = connector.getServerConnection();
      clientConnection.addReceiver(clientReceiver);
      serverConnection.addReceiver(serverReceiver);
   }

   @Test
   public void testMessage() {
      testMessage(clientConnection, serverReceiver);
      testMessage(serverConnection, clientReceiver);
      testMessage(clientConnection, serverReceiver);
      testMessage(clientConnection, serverReceiver);
      testMessage(serverConnection, clientReceiver);
      testMessage(serverConnection, clientReceiver);
   }

   @Test
   public void testStates() {
      assertTrue(serverConnection.isRunning(), "server connection running");
      assertFalse(serverConnection.getSocket().isClosed(), "server connection socket closed");
      assertTrue(clientConnection.isRunning(), "client connection running");
      assertFalse(clientConnection.getSocket().isClosed(), "client connection socket closed");
   }

   @Test
   public void testClose() {
      try {
         serverConnection.close(TIMEOUT);
      } catch (IOException | InterruptedException e) {
         fail(e.getMessage());
      }
      assertFalse(serverConnection.isRunning(), "server connection running");
      assertTrue(serverConnection.getSocket().isClosed(), "server connection socket closed");

      try {
         clientConnection.getThread().join(TIMEOUT);
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
      assertFalse(clientConnection.isRunning(), "client connection running");
      assertTrue(clientConnection.getSocket().isClosed(), "client connection socket closed");
   }

   public void testMessage(Sender sender, TestReceiver receiver) {
      String msg = "test message";
      TestObject packet = new TestObject(msg);
      try {
         sender.send(packet);
      } catch (IOException e) {
         fail(e.getMessage());
      }
      try {
         assertEquals(packet, receiver.nextPacket(TIMEOUT));
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
   }

   @Test
   public void testAll() {
      testStates();
      testMessage();
      testStates();
      testClose();
   }

   @AfterEach
   public void cleanUp() {
      if (clientConnection != null)
         close(clientConnection);
      if (serverConnection != null)
         close(serverConnection);
   }

}
