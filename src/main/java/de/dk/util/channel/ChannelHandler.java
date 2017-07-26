package de.dk.util.channel;

/**
 * An instance for handling requests for new channels and closes of channels of a specific type.
 * A <code>ChannelHandler</code> can be registered at a {@link ChannelManager}
 * and will be informed whenever a request to open a channel of the type that matches this <code>ChannelHandler</code>
 * is received or when such a <code>Channel</code> is closed.
 *
 * @author David Koettlitz
 * <br>Erstellt am 14.07.2017
 *
 * @see ChannelManager
 */
public interface ChannelHandler<T> {
   /**
    * This method is called whenever a request to open a new channel of the matching type is requested.
    * To decline the request a <code>ChannelDeclinedException</code> is thrown.
    * Otherwise the channel will be opened to be ready for communication.
    *
    * @param channel The channel that is requested to be opened
    * @param initialMessage An initial message that came with the request. (May be <code>null</code>)
    *
    * @throws ChannelDeclinedException If the channel should not be opened
    */
   public void newChannelRequested(Channel<T> channel, T initialMessage) throws ChannelDeclinedException;

   /**
    * This method is called when a channel has been closed.
    *
    * @param channel The closed channel
    */
   public void channelClosed(Channel<T> channel);

   /**
    * Get the type this channel handler can handle.
    *
    * @return The type this channel handler can handle
    */
   public Class<T> getType();
}