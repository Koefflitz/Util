package de.dk.util.channel;

import static de.dk.util.channel.ChannelPacket.ChannelPacketType.NEW;

public class NewChannelRequestPacket extends ChannelPacket {
   private static final long serialVersionUID = -1974870581458480869L;

   private final Class<?> type;
   private Object initialMessage;

   public NewChannelRequestPacket(long channelId, Class<?> type, Object initialMsg) {
      super(channelId, NEW);
      this.type = type;
      this.initialMessage = initialMsg;
   }

   public NewChannelRequestPacket(long channelId, Class<?> type) {
      this(channelId, type, null);
   }

   public boolean hasInitialMessage() {
      return initialMessage != null;
   }

   public Object getInitialMessage() {
      return initialMessage;
   }

   public Class<?> getType() {
      return type;
   }

   @Override
   public String toString() {
      return "NewChannelRequestPacket {channelID=" + channelId + ", type=" + type.getName() + "}";
   }
}