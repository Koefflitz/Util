package de.dk.util.net;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class TestReceiver implements Receiver {
   private Object object;

   public TestReceiver() {

   }

   @Override
   public synchronized void receive(Object object) {
      this.object = object;
      notify();
   }

   public Object nextPacket(long timeout) throws InterruptedException {
      if (object == null) {
         synchronized (this) {
            wait(timeout);
         }
      }

      Object object = this.object;
      this.object = null;
      return object;
   }
}
