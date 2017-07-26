package de.dk.util.channel;

import java.util.HashMap;
import java.util.Map;

public class TestChannelHandler<P> implements ChannelHandler<P> {
   private final Class<P> packetType;
   private final Map<Long, Channel<P>> channels = new HashMap<>();

   private Map<Long, Channel<P>> closingChannels = new HashMap<>();

   private boolean accept = true;

   public TestChannelHandler(Class<P> packetType) {
      this.packetType = packetType;
   }

   @Override
   public void newChannelRequested(Channel<P> channel, P initialMessage) throws ChannelDeclinedException {
      if (!accept)
         throw new ChannelDeclinedException("Not accepting channels right now");

      channels.put(channel.getId(), channel);
   }

   @Override
   public void channelClosed(Channel<P> channel) {
      channels.remove(channel.getId());
      Channel<P> waitingChannel;
      synchronized (closingChannels) {
         waitingChannel = closingChannels.remove(channel.getId());
      }
      if (waitingChannel != null) {
         synchronized (waitingChannel) {
            waitingChannel.notify();
         }
      }
   }

   public void waitForClose(Channel<P> channel, long timeout) throws InterruptedException {
      synchronized (closingChannels) {
         if (channel.isClosed())
            return;

         closingChannels.put(channel.getId(), channel);
      }
      synchronized (channel) {
         channel.wait(timeout);
      }
   }

   @Override
   public Class<P> getType() {
      return packetType;
   }

   public Channel<P> getChannel(long id) {
      return channels.get(id);
   }

   public boolean isAcceptingChannelRequests() {
      return accept;
   }

   public void setAcceptChannelRequests(boolean accept) {
      this.accept = accept;
   }

   public void add(Channel<P> channel) {
      channels.put(channel.getId(), channel);
   }
}