package de.dk.util.channel;

import static de.dk.util.net.Connector.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dk.util.net.Connection;
import de.dk.util.net.Connector;
import de.dk.util.net.SimpleConnection;
import de.dk.util.net.TestObject;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ChannelTest {
   private static final Logger LOGGER = LoggerFactory.getLogger(ChannelTest.class);

   private Connection serverConnection;
   private Connection clientConnection;

   private Multiplexer serverMultiplexer;
   private Multiplexer clientMultiplexer;

   private TestChannelHandler<Foo> serverFooHandler;
   private TestChannelHandler<Bar> serverBarHandler;

   private TestChannelHandler<Foo> clientFooHandler;
   private TestChannelHandler<Bar> clientBarHandler;

   public ChannelTest() {

   }

   private static void close(Channel<?> channel) {
      try {
         channel.close();
      } catch (IOException e) {
         fail(e.getMessage());
      }
   }

   private static <T> void testClose(Channel<T> active,
                                     Channel<T> passive,
                                     TestChannelHandler<T> passiveHandler,
                                     Multiplexer activeMultiplexer,
                                     Multiplexer passiveMultiplexer) {
      close(active);
      assertTrue(active.isClosed());
      assertNull(activeMultiplexer.getChannel(active.getId()), "Channel should be removed from handler");
      try {
         passiveHandler.waitForClose(passive, TIMEOUT);
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
      assertTrue(passive.isClosed());
      assertNull(passiveMultiplexer.getChannel(passive.getId()), "Channel should be removed from handler");
   }

   private static <T extends TestObject> void testMessage(T message,
                                                          Channel<T> sender,
                                                          TestChannelListener<T> receiver) {
      try {
         sender.send(message);
      } catch (IOException e) {
         fail(e.getMessage());
      }

      try {
         assertEquals(message, receiver.waitGetAndThrowAwayPacket(TIMEOUT));
      } catch (InterruptedException e) {
         fail(e.getMessage());
      }
   }

   @BeforeEach
   public void init() {
      Connector<SimpleConnection> connector = Connector.simpleConnector();
      connector.connect();
      this.clientConnection = connector.getClientConnection();
      this.serverConnection = connector.getServerConnection();
      try {
         this.clientMultiplexer = clientConnection.attachMultiplexer((this.clientFooHandler = new TestChannelHandler<>(Foo.class)),
                                                                     (this.clientBarHandler = new TestChannelHandler<>(Bar.class)));

         this.serverMultiplexer = serverConnection.attachMultiplexer((this.serverFooHandler = new TestChannelHandler<>(Foo.class)),
                                                                     (this.serverBarHandler = new TestChannelHandler<>(Bar.class)));
      } catch (UnknownHostException e) {
         fail(e);
      }
   }

   @Test
   public void testSingleChannelMessage() {
      TestChannelListener<Foo> clientListener = new TestChannelListener<>();
      Channel<Foo> clientChannel = establishChannel(Foo.class, clientListener, clientFooHandler);
      Channel<Foo> serverChannel = serverFooHandler.getChannel(clientChannel.getId());
      assertNotNull(serverChannel);

      TestChannelListener<Foo> serverListener = new TestChannelListener<>();
      serverChannel.addListener(serverListener);

      testMessage(new Foo(), clientChannel, serverListener);
      testMessage(new Foo(), serverChannel, clientListener);
      testMessage(new Foo(), clientChannel, serverListener);
      testMessage(new Foo(), clientChannel, serverListener);
      testMessage(new Foo(), serverChannel, clientListener);
   }

   @Test
   public void testMultipleChannelsMessage() {
      TestChannelListener<Foo> clientFooListener = new TestChannelListener<>();
      TestChannelListener<Bar> clientBarListener = new TestChannelListener<>();
      Channel<Foo> clientFooChannel = establishChannel(Foo.class, clientFooListener, clientFooHandler);
      Channel<Bar> clientBarChannel = establishChannel(Bar.class, clientBarListener, clientBarHandler);
      Channel<Foo> serverFooChannel = serverFooHandler.getChannel(clientFooChannel.getId());
      Channel<Bar> serverBarChannel = serverBarHandler.getChannel(clientBarChannel.getId());
      TestChannelListener<Foo> serverFooListener = new TestChannelListener<>();
      TestChannelListener<Bar> serverBarListener = new TestChannelListener<>();
      serverFooChannel.addListener(serverFooListener);
      serverBarChannel.addListener(serverBarListener);

      testMessage(new Foo(), clientFooChannel, serverFooListener);
      testMessage(new Foo(), serverFooChannel, clientFooListener);
      testMessage(new Bar(), clientBarChannel, serverBarListener);
      testMessage(new Bar(), serverBarChannel, clientBarListener);
   }

   @Test
   public void testHandlerHierarchy() {
      TestChannelListener<Foo> clientFooListener = new TestChannelListener<>();
      TestChannelListener<Bar> clientBarListener = new TestChannelListener<>();
      Channel<Foo> clientFooChannel = establishChannel(Foo.class, clientFooListener, clientFooHandler);
      Channel<Bar> clientBarChannel = establishChannel(Bar.class, clientBarListener, clientBarHandler);
      Channel<Foo> serverFooChannel = serverFooHandler.getChannel(clientFooChannel.getId());
      Channel<Bar> serverBarChannel = serverBarHandler.getChannel(clientBarChannel.getId());
      TestChannelListener<Foo> serverFooListener = new TestChannelListener<>();
      TestChannelListener<Bar> serverBarListener = new TestChannelListener<>();
      serverFooChannel.addListener(serverFooListener);
      serverBarChannel.addListener(serverBarListener);

      TestChannelHandler<SubFoo> clientSubFooHandler = new TestChannelHandler<>(SubFoo.class);
      TestChannelHandler<SubFoo> serverSubFooHandler = new TestChannelHandler<>(SubFoo.class);
      clientMultiplexer.addHandler(clientSubFooHandler);
      serverMultiplexer.addHandler(serverSubFooHandler);

      TestChannelListener<SubFoo> clientSubFooListener = new TestChannelListener<>();
      TestChannelListener<SubFoo> serverSubFooListener = new TestChannelListener<>();
      Channel<SubFoo> clientSubFooChannel = establishChannel(SubFoo.class, clientSubFooListener, clientSubFooHandler);
      Channel<SubFoo> serverSubFooChannel = serverSubFooHandler.getChannel(clientSubFooChannel.getId());
      serverSubFooChannel.addListener(serverSubFooListener);

      testMessage(new SubFoo(), clientSubFooChannel, serverSubFooListener);
      testMessage(new SubFoo(), serverSubFooChannel, clientSubFooListener);
   }

   @Test
   public void testClose() {
      Channel<Foo> clientFooChannel = establishChannel(Foo.class,
                                                       new TestChannelListener<>(),
                                                       clientFooHandler);
      Channel<Bar> clientBarChannel = establishChannel(Bar.class,
                                                       new TestChannelListener<>(),
                                                       clientBarHandler);
      Channel<Foo> serverFooChannel = serverFooHandler.getChannel(clientFooChannel.getId());
      Channel<Bar> serverBarChannel = serverBarHandler.getChannel(clientBarChannel.getId());

      testClose(clientFooChannel, serverFooChannel, serverFooHandler, clientMultiplexer, serverMultiplexer);
      testClose(serverBarChannel, clientBarChannel, clientBarHandler, serverMultiplexer, clientMultiplexer);
   }

   private <T extends TestObject> Channel<T> establishChannel(Class<T> type,
                                                              ChannelListener<T> listener,
                                                              TestChannelHandler<T> handler) {
      Channel<T> channel = null;
      try {
         channel = clientMultiplexer.establishNewChannel(type, TIMEOUT);
      } catch (IOException | ChannelDeclinedException | InterruptedException | TimeoutException e) {
         fail(e.getMessage());
      }

      assertNotNull(channel);
      channel.addListener(listener);
      handler.add(channel);
      return channel;
   }

   @AfterEach
   public void cleanUp() {
      try {
         serverConnection.close(TIMEOUT);
      } catch (IOException | InterruptedException e) {
         LOGGER.error("Error closing the server connection.");
      }

      try {
         clientConnection.close(TIMEOUT);
      } catch (IOException | InterruptedException e) {
         LOGGER.error("Error closing the client connection.");
      }
   }

}
