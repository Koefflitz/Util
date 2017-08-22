package de.dk.util.net;

import java.util.LinkedList;

import de.dk.util.channel.ChannelManager;

/**
 * This is the interface to receive the read messages from a {@link Connection}
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 *
 * @see Connection
 * @see ChannelManager
 */
public interface Receiver {
   /**
    * Handles a received message.
    *
    * @param msg The received message
    *
    * @throws IllegalArgumentException If the message could not be handled
    */
   public void receive(Object msg) throws IllegalArgumentException;

   public static class ReceiverChain extends LinkedList<Receiver> implements Receiver {
      private static final long serialVersionUID = 4570474656007106847L;

      @Override
      public void receive(Object msg) throws IllegalArgumentException {
         Receiver[] receivers = toArray(new Receiver[size()]);
         for (Receiver receiver : receivers) {
            receiver.receive(msg);
         }
      }

   }
}