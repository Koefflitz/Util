package de.dk.util.net;

import static de.dk.util.net.Connector.TIMEOUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

   @Before
   public void init() {
      this.serverReceiver = new TestReceiver();
      this.clientReceiver = new TestReceiver();

      Connector<SimpleConnection> connector = Connector.simpleConnector();

      connector.connect();
      this.clientConnection = connector.getClientConnection();
      this.serverConnection = connector.getServerConnection();
      clientConnection.setReceiver(clientReceiver);
      serverConnection.setReceiver(serverReceiver);
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
      assertTrue("server connection running", serverConnection.isRunning());
      assertFalse("server connection socket closed", serverConnection.getSocket().isClosed());
      assertTrue("client connection running", clientConnection.isRunning());
      assertFalse("client connection socket closed", clientConnection.getSocket().isClosed());
   }

   @Test
   public void testClose() {
      try {
         serverConnection.close(TIMEOUT);
      } catch (IOException | InterruptedException e) {
         fail(e.getMessage());
      }
      assertFalse("server connection running", serverConnection.isRunning());
      assertTrue("server connection socket closed", serverConnection.getSocket().isClosed());

      try {
         clientConnection.getThread().join(TIMEOUT);
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
      assertFalse("client connection running", clientConnection.isRunning());
      assertTrue("client connection socket closed", clientConnection.getSocket().isClosed());
   }

   public void testMessage(Sender sender, TestReceiver receiver) {
      String msg = "test message";
      TestObject packet = new TestObject(msg);
      try {
         sender.send(packet);
         try {
            synchronized (receiver) {
               receiver.wait(TIMEOUT);
            }
         } catch (InterruptedException e) {
            fail(e.getMessage());
         }
      } catch (IOException e) {
         fail(e.getMessage());
      }
      assertEquals(packet, receiver.getAndThrowAwayPacket());
   }

   @Test
   public void testAll() {
      testStates();
      testMessage();
      testStates();
      testClose();
   }

   @After
   public void cleanUp() {
      if (clientConnection != null)
         close(clientConnection);
      if (serverConnection != null)
         close(serverConnection);
   }

}
