package de.dk.util.timing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import de.dk.util.game.Vector;

/**
 * Creates a pulse in the application with a specific pulse rate.
 * In each pulse cycle a handler is notified and receives a {@link Pulse} object
 * to interpolate speed values to match the pulse speed.
 * The base for the interpolation is a pulse of 60 executions per second.
 * That means that the {@link Pulse#interpolate(float)} method of a pulse with a pulse rate of 60 executions per second
 * won't change the value.
 * <br><br>
 * A Pulse controller can be used in different ways:
 * <br>
 * 1. You can start a new pulse by using this implementation of the {@link Runnable} interface.
 * By calling the run method a new pulse is initiated.
 * The run method will block until the pulsecontroller is stopped.
 * A stopped pulse can always be started again by calling the <code>run()</code> method.
 * <br><br>
 * 2. You can join the pulse controller to an existing pulse
 * by using the {@link Pulse#join(PulseController)} method.
 * The pulserate of the joint controller will stay the same so that it is independent from the parent pulse.<br>
 * It is recommended that the pulse rate of the parent pulse &gt;= the pulse rate of its children.
 * <br><br>
 * 3. You can execute this pulse by manually calling the {@link PulseController#update(Pulse)} method every pulse cycle
 * passing the current timestamp.
 *
 * @author David Koettlitz
 * <br>Erstellt am 11.11.2016
 *
 * @see Pulse
 * @see FrameCounter
 */
public class PulseController implements Runnable, Pulse {
   public static final long SECOND_IN_NANOS = 1000000000L;
   public static final float DEFAULT_CPS = 60;
   public static final float DEFAULT_INTERVALL = SECOND_IN_NANOS / 60f;

   private float cps;
   private long interval;

   private final List<PulseController> joinedPulses = Collections.synchronizedList(new LinkedList<>());
   private Consumer<Pulse> handler;
   private FrameCounter frameCounter;

   private Thread thread;
   private long lastTime;
   private long frameTime;
   private long now;
   private boolean running = false;

   /**
    * Creates a new pulsecontroller that calls a handler every pulse cycle with a specified cps rate.
    *
    * @param handler The handler to be notified each pulse cycle.
    * @param cps The number of calls to be initiated per second.
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public PulseController(Consumer<Pulse> handler, float cps) throws NullPointerException {
      this.handler = Objects.requireNonNull(handler);
      setCps(cps);
   }

   /**
    * Creates a new pulsecontroller that calls a handler every pulse cycle with a default cps rate of 60.
    *
    * @param handler The handler to be called every pulse cycle.
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public PulseController(Consumer<Pulse> handler) throws NullPointerException {
      this(handler, DEFAULT_CPS);
   }

   /**
    * Create a new PulseController that is its own handler.
    * For calling this constructor, the {@link #execute(Pulse)} method has to be overridden.
    * Otherwise the {@link #execute(Pulse)} method will throw an exception.
    *
    * @param cps The calls of the handler (in this case this) per second
    */
   protected PulseController(float cps) {
      this.handler = this::execute;
      setCps(cps);
   }

   /**
    * Create a new PulseController that is its own handler.
    * For calling this constructor, the {@link #execute(Pulse)} method has to be overridden.
    * Otherwise the {@link #execute(Pulse)} method will throw an exception.
    */
   protected PulseController() {
      this(DEFAULT_CPS);
   }

   /**
    * Starts this pulse controller on a new thread.
    * This method returns when the thread is started.
    *
    * @throws IllegalStateException If this pulse contoller is already running.
    */
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

   /**
    * Update the pulse this calls the handler if the specified interval time has passed.
    * Also all the PulseControllers that are joined to this are updated.
    */
   public void update() {
      this.now = System.nanoTime();
      update(this);
   }

   /**
    * Update from the given <code>pulse</code>.
    * If enough time has passed from the last call until {@link Pulse#now()} the handler is called.
    * Also all the PulseControllers that are joined to this are updated.
    *
    * @param pulse The pulse that represents the current time.
    */
   public synchronized void update(Pulse pulse) {
      if (lastTime == 0)
         lastTime = pulse.now();

      while ((frameTime = pulse.now() - lastTime) > interval) {
         handler.accept(pulse);
         synchronized (joinedPulses) {
            joinedPulses.forEach(p -> p.update(pulse));
         }

         if (frameCounter != null)
            frameCounter.update();

         lastTime = interval == 0 ? pulse.now() : lastTime + interval;
      }
   }

   protected void execute(Pulse pulse) {
      throw new IllegalStateException("If this method is called, it has to be overridden.");
   }

   /**
    * Stops the pulse run by this PulseController.
    * If it is not running, this call has no effect.
    * A stopped pulse can always be started again by calling the {@link #run()} method.
    *
    * @throws InterruptedException If this thread is interrupted
    * while waiting for the running thread to terminate.
    */
   public void stop() throws InterruptedException {
      running = false;
      if (thread != null && thread != Thread.currentThread())
         thread.join();
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

   public synchronized void reset() {
      lastTime = 0;
      if (frameCounter != null)
         frameCounter.reset();

      synchronized (joinedPulses) {
         joinedPulses.forEach(PulseController::reset);
      }
   }

   public FrameCounter getFrameCounter() {
      return frameCounter;
   }

   /**
    * Attaches a framecounter to this pulse. The framecounter will count the number of pulse cycles
    * for each second and notify the handler.
    *
    * @param frameCounter A framecounter to count the actual executed pulse cycles per second.
    */
   public synchronized void setFrameCounter(FrameCounter frameCounter) {
      this.frameCounter = frameCounter;
   }

   public Consumer<Pulse> getHandler() {
      return handler;
   }

   /**
    * Set the handler that is called every pulse cycle.
    *
    * @param handler The handler to be called frequently in the rate of this pulse.
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   public void setHandler(Consumer<Pulse> handler) throws NullPointerException {
      this.handler = Objects.requireNonNull(handler);
   }

   /**
    * Set the interval time that should pass between calls.
    * This method has the same effect as the {@link #setCps(float)} method.
    *
    * @param interval The time in nanoseconds that has to pass between the calls.
    */
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

   /**
    * Set the times per second the handlers are called.
    * This method has the same effect as the {@link #setInterval(long)} method.
    *
    * @param cps The calls per second
    */
   public synchronized void setCps(float cps) {
      this.cps = cps;
      this.interval = (long) (SECOND_IN_NANOS / cps);
   }

   @Override
   public synchronized float getCps() {
      return cps;
   }

   public boolean isRunning() {
      return running;
   }
}