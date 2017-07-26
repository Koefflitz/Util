package de.dk.util.timing;

import java.util.Objects;
import java.util.function.IntConsumer;

/**
 * A tracker for a pulse to count the frames for each second.
 * The update method has to be called in each frame of the pulse.
 * You can either call the update method by yourself or use a {@link PulseController}
 * and attach the framecounter to it. After each second the attached handler will notified
 * of how many frames were executed in that second.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 11.11.2016
 *
 * @see PulseController
 */
public class FrameCounter {
   private long lastTime = 0;
   private int count = 0;

   private IntConsumer handler;

   public FrameCounter(IntConsumer handler) {
      this.handler = handler;
   }

   public void update() {
      if (lastTime == 0)
         lastTime = System.currentTimeMillis();

      if (System.currentTimeMillis() - lastTime >= 1000) {
         handler.accept(count);
         count = 0;
         lastTime += 1000;
      }
      count++;
   }

   public void reset() {
      lastTime = 0;
      count = 0;
   }

   public void setHandler(IntConsumer handler) {
      Objects.requireNonNull(handler);
      this.handler = handler;
   }

   public IntConsumer getHandler() {
      return handler;
   }

}