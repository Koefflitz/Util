package de.dk.util.timing;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import de.dk.util.Vector;

/**
 * The default implementation for {@link PulseController}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 11.11.2016
 *
 * @see PulseController
 * @see AsyncPulseController
 */
public class SimplePulseController implements PulseController, Pulse {
   private float fps;
   private long interval;
   private boolean catchUpForSkippedFrames = true;

   private final List<PulseController> joinedPulses = new LinkedList<>();
   private Consumer<Pulse> handler;
   private FrameCounter frameCounter;

   private long lastTime;
   private long frameTime;
   private long now;
   private boolean running = false;

   /**
    * Creates a new <code>SimplePulseController</code> that calls the <code>handler</code>
    * every frame in the specified framerate.
    *
    * @param handler the handler to be called every frame.
    * @param fps the framerate in frames per second
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public SimplePulseController(Consumer<Pulse> handler, float fps) throws NullPointerException {
      this.handler = Objects.requireNonNull(handler);
      setFramerate(fps);
   }

   /**
    * Creates a new <code>SimplePulseController</code> that calls the <code>handler</code>
    * every frame in the default framerate.
    *
    * @param handler the handler to be called every frame.
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public SimplePulseController(Consumer<Pulse> handler) throws NullPointerException {
      this(handler, DEFAULT_FPS);
   }

   /**
    * Create a new <code>SimplePulseController</code> that is its own handler.
    * For calling this constructor, the {@link #execute(Pulse)} method has to be overridden.
    * Otherwise the {@link #execute(Pulse)} method will throw an exception.
    *
    * @param fps The framerate in frames per second
    */
   protected SimplePulseController(float fps) {
      this.handler = this::execute;
      setFramerate(fps);
   }

   /**
    * Create a new <code>SimplePulseController</code> that is its own handler.
    * For calling this constructor, the {@link #execute(Pulse)} method has to be overridden.
    * Otherwise the {@link #execute(Pulse)} method will throw an exception.
    */
   protected SimplePulseController() {
      this(DEFAULT_FPS);
   }

   /**
    * Starts this pulse controller.
    * This method blocks until the this pulse is stopped.
    *
    * @throws IllegalStateException If this pulse contoller is already running.
    */
   @Override
   public void start() {
      if (running)
         throw new IllegalStateException("Already running.");

      running = true;
      while (running)
         update();

      reset();
   }

   @Override
   public void update() {
      this.now = System.nanoTime();
      update(this);
   }

   @Override
   public void update(Pulse pulse) {
      if (lastTime == 0)
         lastTime = pulse.now();

      while ((frameTime = pulse.now() - lastTime) > interval) {
         handler.accept(pulse);
         joinedPulses.forEach(p -> p.update(pulse));

         if (frameCounter != null)
            frameCounter.update();

         if (interval == 0 || !catchUpForSkippedFrames && frameTime >= interval * 2)
            lastTime = pulse.now();
         else
            lastTime += interval;
      }
   }

   protected void execute(Pulse pulse) {
      throw new IllegalStateException("If this method is called, it has to be overridden.");
   }

   @Override
   public void stop() {
      running = false;
   }

   @Override
   public float interpolate(float value) {
      return value * (frameTime / DEFAULT_INTERVALL);
   }

   @Override
   public Vector interpolate(Vector v) {
      return new Vector(interpolate(v.x()), interpolate(v.y()));
   }

   @Override
   public long now() {
      return now;
   }

   @Override
   public long frameTime() {
      return frameTime;
   }

   @Override
   public void join(PulseController pulse) {
      joinedPulses.add(pulse);
   }

   @Override
   public void remove(PulseController pulse) {
      joinedPulses.remove(pulse);
   }

   public void removeChildPulses() {
      joinedPulses.clear();
   }

   @Override
   public void reset() {
      lastTime = 0;
      if (frameCounter != null)
         frameCounter.reset();

      joinedPulses.forEach(PulseController::reset);
   }

   @Override
   public FrameCounter getFrameCounter() {
      return frameCounter;
   }

   @Override
   public void setFrameCounter(FrameCounter frameCounter) {
      this.frameCounter = frameCounter;
   }

   @Override
   public Consumer<Pulse> getHandler() {
      return handler;
   }

   @Override
   public void setHandler(Consumer<Pulse> handler) throws NullPointerException {
      this.handler = Objects.requireNonNull(handler);
   }

   @Override
   public void setInterval(long interval) {
      this.interval = interval;
      if (interval == 0)
         this.fps = Float.MAX_VALUE;
      else
         this.fps = (float) SECOND_IN_NANOS / interval;
   }

   @Override
   public long getInterval() {
      return interval;
   }

   @Override
   public void setFramerate(float fps) {
      this.fps = fps;
      this.interval = (long) (SECOND_IN_NANOS / fps);
   }

   @Override
   public float getFramerate() {
      return fps;
   }

   @Override
   public boolean isCatchUpForSkippedFrames() {
      return catchUpForSkippedFrames;
   }

   @Override
   public void setCatchUpForSkippedFrames(boolean catchUpForSkippedFrames) {
      this.catchUpForSkippedFrames = catchUpForSkippedFrames;
   }

   @Override
   public boolean isRunning() {
      return running;
   }
}