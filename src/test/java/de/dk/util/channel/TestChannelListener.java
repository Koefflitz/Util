package de.dk.util.channel;

import de.dk.util.channel.ChannelListener;
import de.dk.util.net.TestObject;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class TestChannelListener<P extends TestObject> implements ChannelListener<P> {
   private P packet;

   public TestChannelListener() {

   }

   @Override
   public synchronized void received(P packet) {
      this.packet = packet;
      notify();
   }

   public synchronized P waitGetAndThrowAwayPacket(long timeout) throws InterruptedException {
      if (this.packet == null)
         wait(timeout);

      return getAndThrowAwayPacket();
   }

   public synchronized P getAndThrowAwayPacket() {
      P packet = this.packet;
      this.packet = null;
      return packet;
   }
}
