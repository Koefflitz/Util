package de.dk.util.net;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This is the base implementation of the <code>Connection</code> class.
 * It reads and writes objects from/to the underlying socket using {@link ObjectInputStream} and {@link ObjectOutputStream}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 */
public class SimpleConnection extends Connection {
   private final ObjectInputStream in;
   private final ObjectOutputStream out;

   /**
    * Creates a connection based on the given <code>socket</code>.
    *
    * @param socket The socket this connection will be based on
    * @param receiver The receiver, that handles the ingoing messages
    *
    * @throws IOException If the communication streams of the socket are unavailable
    */
   public SimpleConnection(Socket socket, Receiver receiver) throws IOException {
      super(socket, receiver);
      this.out = new ObjectOutputStream(super.out);
      out.flush();
      this.in = new ObjectInputStream(super.in);
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
   public SimpleConnection(String host, int port, Receiver receiver) throws UnknownHostException, IOException {
      super(host, port, receiver);
      this.out = new ObjectOutputStream(super.out);
      out.flush();
      this.in = new ObjectInputStream(super.in);
   }

   /**
    * Creates a new Connection based on the given <code>socket</code>.
    *
    * @param socket The socket this connection will be based on
    *
    * @throws IOException If the communication streams of the socket are unavailable
    */
   public SimpleConnection(Socket socket) throws UnknownHostException, IOException {
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
   public SimpleConnection(String host, int port) throws UnknownHostException, IOException {
      this(host, port, null);
   }

   @Override
   protected Object[] readObject() throws IOException, ReadingException {
         try {
            return new Object[] {in.readObject()};
         } catch (ClassNotFoundException
                  | InvalidClassException
                  | StreamCorruptedException
                  | OptionalDataException e) {
            throw new ReadingException("Error reading from " + getInetAddress(), e);
         }
   }

   @Override
   public void send(Serializable packet) throws IOException {
      out.writeObject(packet);
   }
}