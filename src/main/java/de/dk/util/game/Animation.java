package de.dk.util.game;

import java.util.Objects;

import de.dk.util.timing.Pulse;
import de.dk.util.timing.PulseController;
import de.dk.util.timing.SimplePulseController;

public class Animation<I> {
   private int index;
   private final I[] images;
   private PulseController pulse;

   public Animation(I[] images, float fps) {
      this.images = Objects.requireNonNull(images);
      this.pulse = new SimplePulseController(this::nextImage, fps);
   }

   public I getImage() {
      pulse.update();
      return images[index];
   }

   protected void nextImage(Pulse pulse) {
      if (++index >= images.length)
         index = 0;
   }

   public void reset() {
      index = 0;
      pulse.reset();
   }

   public void setFramerate(float fps) {
      pulse.setFramerate(fps);
   }

   public float getFramerate() {
      return pulse.getFramerate();
   }
}
