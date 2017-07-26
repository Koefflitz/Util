package de.dk.util.net;

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
}