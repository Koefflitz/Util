package de.dk.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dk.util.channel.ChannelHandler;
import de.dk.util.channel.ChannelManager;
import de.dk.util.channel.Sender;
import de.dk.util.net.ConnectionListener.ConnectionListenerChain;
import de.dk.util.net.Receiver.ReceiverChain;

/**
 * A class to represent a TCP connection based on a {@link Socket}.
 * The ingoing messages of this connection can be taken by an attached receiver.
 * The connection continuosly reads messages when started. The message reading task can be started
 * by calling the {@link Connection#start()} method.
 * Messages can be sent by calling the {@link Connection#send(Serializable)} method.
 * A basic implementation of this class is provided by the {@link SimpleConnection} class.
 * The messages can be channeled using the Channel API.
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 *
 * @see ChannelManager
 * @see ConnectionListener
 * @see SimpleConnection
 */
public abstract class Connection implements Runnable, Sender {
   private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

   protected final Socket socket;
   protected final InputStream in;
   protected final OutputStream out;

   private final ReceiverChain receivers = new ReceiverChain();
   private final ConnectionListenerChain listeners = new ConnectionListenerChain();

   protected final AtomicBoolean running = new AtomicBoolean(false);

   private Thread thread;

   /**
    * Creates a connection based on the given <code>socket</code>.
    *
    * @param socket The socket this connection will be based on
    * @param receiver The receiver, that handles the ingoing messages
    *
    * @throws IOException If the communication streams of the socket are unavailable
    */
   public Connection(Socket socket, Receiver receiver) throws IOException {
      this.socket = Objects.requireNonNull(socket);
      this.out = socket.getOutputStream();
      this.in = socket.getInputStream();
      addReceiver(receiver);
   }

   /**
    * Creates a new connection to the given <code>host</code> at the given <code>port</code>.
    * Internally creates a new socket on which this connection will be based.
    *
    * @param host The host to connect to
    * @param port The port at which to connect
    * @param receiver The receiver, that handles the ingoing messages
    *
    * @throws UnknownHostException If the IP address of the host could not be determined
    * @throws IOException If an I/O error occurs when creating the socket.
    *                     Or if the communication streams of the socket are unavailable
    */
   public Connection(String host, int port, Receiver receiver) throws UnknownHostException, IOException {
      this(new Socket(host, port), receiver);
   }

   /**
    * Creates a new Connection based on the given <code>socket</code>.
    *
    * @param socket The socket this connection will be based on
    *
    * @throws IOException If the communication streams of the socket are unavailable
    */
   public Connection(Socket socket) throws IOException {
      this(socket, null);
   }

   /**
    * Creates a new connection to the given <code>host</code> at the given <code>port</code>.
    * Internally creates a new socket on which this connection will be based.
    *
    * @param host The host to connect to
    * @param port The port at which to connect
    *
    * @throws UnknownHostException If the IP address of the host could not be determined
    * @throws IOException If an I/O error occurs when creating the socket.
    *                     Or if the communication streams of the socket are unavailable
    */
   public Connection(String host, int port) throws UnknownHostException, IOException {
      this(host, port, null);
   }

   /**
    * Starts reading of messages in a new thread.
    * It is recommended to use this start method over creating an own thread
    * using this connection as a <code>runnable</code> for the thread.
    */
   public void start() {
      if (isRunning())
         return;

      this.thread = new Thread(this);
      thread.start();
   }

   @Override
   public synchronized void run() {
      this.thread = Thread.currentThread();
      synchronized (running) {
         running.set(true);
         running.notify();
      }

      while (!socket.isClosed()) {
         Object object;
         try {
            object = readObject();
         } catch (ReadingException e) {
            LOGGER.error("Error reading an incoming message from " + socket.getInetAddress(), e);
            continue;
         } catch (IOException e) {
            break;
         }

         if (object == null)
            break;

         try {
            for (Receiver receiver : receivers)
               receiver.receive(object);
         } catch (IllegalArgumentException e) {
            LOGGER.warn("The object " + object + " could not be received.", e);
         }
      }

      if (!socket.isClosed()) {
         try {
            socket.close();
         } catch (IOException e) {
            LOGGER.warn("Error closing the socket " + socket.getInetAddress(), e);
         }
      }
      running.set(false);
      listeners.closed();
   }

   /**
    * Reads the next object from the sockets inputstream.
    * This method block until an object is read or the connection has been closed.
    *
    * @return The read object
    *
    * @throws IOException If the socket is closed while reading
    * @throws ReadingException If no object can be read
    */
   protected abstract Object readObject() throws IOException, ReadingException;

   /**
    * Attaches a channel manager to this connection.
    * The previously set receiver will be removed and replaced by the channel manager.
    * It is now possible to communicate through channels provided by the channel manager.
    *
    * @param handlers The channel handlers for the channel manager
    *
    * @return The new created and attached channel manager
    *
    * @throws UnknownHostException If the idGenerator for the channel manager could not be created
    */
   public ChannelManager attachChannelManager(ChannelHandler<?>... handlers) throws UnknownHostException {
      ChannelManager manager = new ChannelManager(new InetAddressIdGenerator(), this, handlers);
      addReceiver(manager);
      return manager;
   }

   /**
    * Blocks until this connection is actually running or the given <code>timeout</code> is reached.
    * Running means this connection is looking for new messages to arrive.
    *
    * @param timeout The number of milliseconds to wait for this connection to actually run.
    *
    * @throws InterruptedException If this thread is interrupted while waiting
    */
   public void waitForRunning(long timeout) throws InterruptedException {
      synchronized (running) {
         if (running.get())
            return;

         running.wait(timeout);
      }
   }

   /**
    * Get the thread this connection is running in.
    * Might be <code>null</code> if either the connection is not running
    * or it has not been started using the {@link Connection#start()} method.
    *
    * @return The thread this connection is running in
    */
   public Thread getThread() {
      return thread;
   }

   /**
    * Closes this connection. A once closed connection object cannot be reopened again.
    * This method blocks until either the connection has been closed and is not running anymore
    * or the timeout was reached.
    * If this connection was not start by its {@link Connection#start()} method
    * this method is not able to wait for this connection to stop running and will return immediately.
    *
    * @param timeout The timeout in millis to maximum wait for this connection to stop running.
    *
    * @throws IOException If an I/O error occurs when closing this socket
    * @throws InterruptedException If this thread was interrupted while waiting to stop running
    */
   public void close(long timeout) throws IOException, InterruptedException {
      if (socket.isClosed())
         return;

      socket.close();
      if (thread != null && thread != Thread.currentThread())
         thread.join(timeout);
   }

   /**
    * Closes this connection. A once closed connection object cannot be reopened again.
    * This method returns immediately after the socket has been closed.
    * This connection could still be in running mode and stop after this method returns.
    * To be sure this connection is not runnning anymore after the call use the
    * {@link Connection#close(long)} method.
    *
    * @throws IOException If an I/O error occurs when closing this socket
    * @throws InterruptedException If the thread is interrupted while waiting
    * to close
    */
   public void close() throws IOException, InterruptedException {
      close(0);
   }

   public boolean isClosed() {
      return socket.isClosed();
   }

   /**
    * Adds a ConnectionListener to this connection.
    *
    * @param listener The listener to be added
    */
   public void addListener(ConnectionListener listener) {
      listeners.add(listener);
   }

   /**
    * Removes a ConnectionListener from this connection
    *
    * @param listener The listener to be removed
    */
   public void removeListener(ConnectionListener listener) {
      listeners.remove(listener);
   }

   /**
    * Set the receiver of the incoming messages.
    * This replaces the receiver, that was attached before.
    *
    * @param receiver The receiver of the incoming messages
    */
   public void addReceiver(Receiver receiver) {
      if (receiver != null)
         receivers.add(receiver);
   }

   public void removeReceiver(Receiver receiver) {
      receivers.remove(receiver);
   }

   /**
    * Get the receiver of the incoming messaged, that is currently attached to this connection.
    *
    * @return The currently attached receiver
    */
   public Receiver getReceiver() {
      return receivers;
   }

   /**
    * Get the running state of this connection.
    * A connection is running while it is in its <code>run()</code> method.
    * While running it looks for new incoming messages and redirects them to the attached receiver.
    *
    * @return The running state of this connection
    */
   public boolean isRunning() {
      return running.get();
   }

   /**
    * Get the InetAddress this connection is connected to.
    *
    * @return The connected InetAddress
    */
   public InetAddress getInetAddress() {
      return socket.getInetAddress();
   }

   /**
    * Get the socket this connection is based on.
    *
    * @return The socket of this connection
    */
   public Socket getSocket() {
      return socket;
   }
}