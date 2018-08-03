package de.dk.util.timing;

import java.util.function.Consumer;

/**
 * Creates a pulse with a specific framerate.
 * In each frame a handler is notified and receives a {@link Pulse} object
 * to interpolate values to match the framerate.
 * The base for the interpolation is a pulse of 60 executions per second.
 * That means that the {@link Pulse#interpolate(float)} method of a pulse with a framerate of 60 executions per second
 * won't change the value. A higher pulse rate would decrease the value and a lower one would increase it.
 * <br><br>
 * A Pulse controller can be used in different ways:
 * <br>
 * 1. You can start a new pulse by calling the {@link #start()} method.
 * The pulse will then be executed until the pulsecontroller is stopped.
 * A stopped pulse can always be started again by calling the <code>start()</code> method.
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
 * @see SimplePulseController
 * @see AsyncPulseController
 */
public interface PulseController extends Pulse {
   /**
    * Starts this pulse controller.
    *
    * @throws IllegalStateException If this pulse contoller is already running.
    */
   void start() throws IllegalStateException;

   /**
    * Update from the given <code>pulse</code>.
    * If enough time has passed from the last call until {@link Pulse#now()} the handler is called.
    * Also all the <code>PulseControllers</code> that are joined to this are updated.
    *
    * @param pulse The pulse that represents the current frame.
    */
   void update(Pulse pulse);

   /**
    * Update the pulse. This instantiates a new frame and calls the handler
    * if the specified interval time has passed since the last frame.
    * Also all the PulseControllers that are joined to this are updated.
    */
   void update();

   /**
    * Stops and resets the pulse executed by this PulseController.
    * If it is not running, this call has no effect.
    * A stopped pulse can always be started again by calling the {@link #start()} method.
    */
   void stop();

   /**
    * Resets the pulse to a fresh new state.
    * The memorized timestamp of the last frame will be erased,
    * to put the pulse in a state, that is equal to the state
    * before it started.
    * After calling this method the next update call will definetely
    * instantiate a new frame and call the registered handler.
    */
   void reset();

   /**
    * Attaches a framecounter to this pulse. The framecounter will count the number of pulse cycles
    * for each second to determine the actual framerate of this pulse.
    *
    * @param frameCounter A framecounter to count the actual executed frames per second.
    */
   void setFrameCounter(FrameCounter frameCounter);

   /**
    * Get the attached framecounter to this pulse. The framecounter will count the number of pulse cycles
    * for each second to determine the actual framerate of this pulse.
    *
    * @return the attached framecounter that counts the actual executed frames per second.
    */
   FrameCounter getFrameCounter();

   /**
    * Get the handler that is called every frame.
    *
    * @return the handler that is called frequently in the rate of this pulse.
    */
   Consumer<Pulse> getHandler();

   /**
    * Set the handler that is called every frame.
    *
    * @param handler the handler to be called frequently in the rate of this pulse.
    *
    * @throws NullPointerException if the given <code>handler</code> is <code>null</code>.
    */
   void setHandler(Consumer<Pulse> handler) throws NullPointerException;

   /**
    * Set the target interval time between two frames.
    *
    * @param interval The time in nanoseconds that has to pass between two frames.
    */
   void setInterval(long interval);

   /**
    * Set the target framerate.
    * Determines the times per second the handlers are called.
    *
    * @param fps The target frames per second
    */
   void setFramerate(float fps);

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
   boolean isCatchUpForSkippedFrames();

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
   void setCatchUpForSkippedFrames(boolean catchUpForSkippedFrames);

   /**
    * Get wether this pulse is running.
    *
    * @return <code>true</code> if the pulse is running
    * <code>false</code> otherwise
    */
   boolean isRunning();
}
