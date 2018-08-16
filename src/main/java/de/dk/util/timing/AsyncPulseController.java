package de.dk.util.timing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import de.dk.util.Vector;

/**
 * A thread safe implementation of {@link PulseController}
 *
 * @author David Koettlitz
 * <br>Erstellt am 11.11.2016
 *
 * @see PulseController
 * @see SimplePulseController
 */
public class AsyncPulseController implements PulseController, Pulse, Runnable {
   private float cps;
   private long interval;
   private boolean catchUpForSkippedFrames = true;

   private final List<PulseController> joinedPulses = Collections.synchronizedList(new LinkedList<>());
   private Consumer<Pulse> handler;
   private FrameCounter frameCounter;

   private Thread thread;
   private long lastTime;
   private long frameTime;
   private long now;
   private boolean running = false;

   /**
    * Creates a new <code>AsyncPulseController</code> that calls the <code>handler</code>
    * every frame in the specified framerate.
    *
    * @param handler the handler to be called every frame.
    * @param fps the framerate in frames per second
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public AsyncPulseController(Consumer<Pulse> handler, float fps) throws NullPointerException {
      this.handler = Objects.requireNonNull(handler);
      setFramerate(fps);
   }

   /**
    * Creates a new <code>AsyncPulseController</code> that calls the <code>handler</code>
    * every frame in the default framerate.
    *
    * @param handler the handler to be called every frame.
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public AsyncPulseController(Consumer<Pulse> handler) throws NullPointerException {
      this(handler, DEFAULT_FPS);
   }

   /**
    * Creates a new <code>AsyncPulseController</code> that is its own handler.
    * For calling this constructor, the {@link #execute(Pulse)} method has to be overridden.
    * Otherwise the {@link #execute(Pulse)} method will throw an exception.
    *
    * @param fps The framerate in frames per second
    */
   protected AsyncPulseController(float fps) {
      this.handler = this::execute;
      setFramerate(fps);
   }

   /**
    * Creates a new <code>AsyncPulseController</code> that is its own handler.
    * For calling this constructor, the {@link #execute(Pulse)} method has to be overridden.
    * Otherwise the {@link #execute(Pulse)} method will throw an exception.
    */
   protected AsyncPulseController() {
      this(DEFAULT_FPS);
   }

   /**
    * Starts this pulse controller on a new thread.
    * This method returns when the thread is started.
    *
    * @throws IllegalStateException If this pulse contoller is already running.
    */
   @Override
   public void start() throws IllegalStateException {
      if (running)
         throw new IllegalStateException("Already running.");

      new Thread(this).start();
   }

   @Override
   public void run() {
      if (running)
         throw new IllegalStateException("Already running.");

      running = true;
      this.thread = Thread.currentThread();
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
   public synchronized void update(Pulse pulse) {
      if (lastTime == 0) {
         lastTime = pulse.now();
         handler.accept(pulse);
         synchronized (joinedPulses) {
            joinedPulses.forEach(p -> p.update(pulse));
         }
         return;
      }

      while ((frameTime = pulse.now() - lastTime) > interval) {
         handler.accept(pulse);
         synchronized (joinedPulses) {
            joinedPulses.forEach(p -> p.update(pulse));
         }

         if (frameCounter != null)
            frameCounter.update();

         if (interval == 0 || !catchUpForSkippedFrames)
            lastTime = pulse.now();
         else
            lastTime += interval;
      }
   }

   protected void execute(Pulse pulse) {
      throw new IllegalStateException("If this method is called, it has to be overridden.");
   }

   /**
    * Stops the pulse run by this AsyncPulseController.
    * If it is not running, this call has no effect.
    * A stopped pulse can always be started again by calling the {@link #start()} method.
    *
    * @throws RuntimeException If this thread is interrupted
    * while waiting for the running thread to terminate.
    */
   @Override
   public void stop() throws RuntimeException {
      running = false;
      if (thread != null && thread != Thread.currentThread()) {
         try {
            thread.join();
         } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for the pulse to stop.", e);
         }
      }
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
      synchronized (joinedPulses) {
         joinedPulses.add(pulse);
      }
   }

   @Override
   public void remove(PulseController pulse) {
      synchronized (joinedPulses) {
         joinedPulses.remove(pulse);
      }
   }

   public void removeChildPulses() {
      synchronized (joinedPulses) {
         joinedPulses.clear();
      }
   }

   @Override
   public synchronized void reset() {
      lastTime = 0;
      if (frameCounter != null)
         frameCounter.reset();

      synchronized (joinedPulses) {
         joinedPulses.forEach(PulseController::reset);
      }
   }

   @Override
   public FrameCounter getFrameCounter() {
      return frameCounter;
   }

   /**
    * Attaches a framecounter to this pulse. The framecounter will count the number of pulse cycles
    * for each second and notify the handler.
    *
    * @param frameCounter A framecounter to count the actual executed pulse cycles per second.
    */
   @Override
   public synchronized void setFrameCounter(FrameCounter frameCounter) {
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
   public synchronized void setInterval(long interval) {
      this.interval = interval;
      if (interval == 0)
         this.cps = Float.MAX_VALUE;
      else
         this.cps = (float) SECOND_IN_NANOS / interval;
   }

   @Override
   public synchronized long getInterval() {
      return interval;
   }

   @Override
   public synchronized void setFramerate(float fps) {
      this.cps = fps;
      this.interval = (long) (SECOND_IN_NANOS / fps);
   }

   @Override
   public synchronized float getFramerate() {
      return cps;
   }

   /**
    * <code>catchUpForSkippedFrames</code> determines how to handle delays
    * between update calls that are longer than or equal to the time of 2
    * intervals.
    * <br><br>
    * Those delays can be caught up <br>
    * 1. by executing the skipped frames directly
    * one after another in the same update call or <br>
    * 2. just let them be skipped and keep going with the given framerate.
    * In this case the <code>interpolate</code> methods will still interpolate
    * the values correctly, so the length of the skipped frames will be
    * stretched into one delayed frame<br>
    *
    * If <code>catchUpForSkippedFrames</code> is set to <code>true</code>
    * skipped frames will be caught up retrospectively by executing those frames directly
    * one after another in the same update call.<br>
    * If <code>catchUpForSkippedFrames</code> is set to <code>false</code>
    * skipped frames will not be executed retrospectively.
    *
    * @return the value for <code>catchUpForSkippedFrames</code>
    */
   @Override
   public boolean isCatchUpForSkippedFrames() {
      return catchUpForSkippedFrames;
   }

   /**
    * <code>catchUpForSkippedFrames</code> determines how to handle delays
    * between update calls that are longer than or equal to the time of 2
    * intervals.
    * <br><br>
    * Those delays can be caught up <br>
    * 1. by executing the skipped frames directly
    * one after another in the same update call or <br>
    * 2. just let them be skipped and keep going with the given framerate.
    * In this case the <code>interpolate</code> methods will still interpolate
    * the values correctly, so the length of the skipped frames will be
    * stretched into one delayed frame<br>
    *
    * @param catchUpForSkippedFrames if set to <code>true</code>
    * skipped frames will be caught up retrospectively by executing those frames
    * directly one after another in the same update call.<br>
    * If set to <code>false</code> skipped frames will not be executed retrospectively.
    */
   @Override
   public void setCatchUpForSkippedFrames(boolean catchUpForSkippedFrames) {
      this.catchUpForSkippedFrames = catchUpForSkippedFrames;
   }

   @Override
   public boolean isRunning() {
      return running;
   }
}