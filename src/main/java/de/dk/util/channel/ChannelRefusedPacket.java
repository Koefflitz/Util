package de.dk.util.channel;

import static de.dk.util.channel.ChannelPacket.ChannelPacketType.REFUSED;

public class ChannelRefusedPacket extends ChannelPacket {
   private static final long serialVersionUID = -204989785585197191L;

   private final String msg;
   private final Exception exception;

   public ChannelRefusedPacket(long channelId, String msg, Exception e) {
      super(channelId, REFUSED);
      this.msg = msg;
      this.exception = e;
   }

   public ChannelRefusedPacket(long channelId, String msg) {
      this(channelId, msg, null);
   }

   public ChannelRefusedPacket(long channelId, Exception e) {
      this(channelId, null, e);
   }

   public String getMsg() {
      return msg;
   }

   public Exception getException() {
      return exception;
   }

   @Override
   public String toString() {
      return "ChannelRefusedPacket {channelID=" + channelId + ", msg=\"" + msg + "\"}";
   }
}