package de.dk.util.channel;

/**
 * Indicates that a channel has been closed.
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 *
 * @see ChannelManager
 * @see Channel
 */
public class ChannelClosedException extends RuntimeException {
   private static final long serialVersionUID = 4162162428990881318L;

   public ChannelClosedException(String message) {
      super(message);
   }

   public ChannelClosedException(Throwable cause) {
      super(cause);
   }

   public ChannelClosedException(String message, Throwable cause) {
      super(message, cause);
   }
}