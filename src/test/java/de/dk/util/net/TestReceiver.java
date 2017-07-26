package de.dk.util.net;

public class TestReceiver implements Receiver {
   private Object object;

   public TestReceiver() {

   }

   @Override
   public synchronized void receive(Object object) {
      this.object = object;
      notify();
   }

   public Object getAndThrowAwayPacket() {
      Object object = this.object;
      this.object = null;
      return object;
   }
}