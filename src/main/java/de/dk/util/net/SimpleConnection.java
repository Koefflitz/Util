package de.dk.util.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleConnection extends Connection {
   private ObjectInputStream in;
   private ObjectOutputStream out;

   {
      this.out = new ObjectOutputStream(super.out);
      this.in = new ObjectInputStream(super.in);
   }

   public SimpleConnection(Socket socket, Receiver receiver) throws IOException {
      super(socket, receiver);
   }

   public SimpleConnection(String host, int port, Receiver receiver) throws UnknownHostException, IOException {
      super(host, port, receiver);
   }

   public SimpleConnection(Socket socket) throws IOException {
      super(socket);
   }

   public SimpleConnection(String host, int port) throws UnknownHostException, IOException {
      super(host, port);
   }

   @Override
   public void send(Serializable msg) throws IOException {
      out.writeObject(msg);
   }

   @Override
   public synchronized Object readObjectImpl() throws IOException, ReadingException {
      try {
         return in.readObject();
      } catch (ClassNotFoundException e) {
         throw new ReadingException(e);
      }
   }

}
