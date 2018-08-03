package de.dk.util.timing;

import de.dk.util.Vector;

/**
 * Represents the current frame of a pulse and provides information about the pulse.
 * A pulsehandler attached to a {@link PulseController} will receive a <code>Pulse</code> object in each frame.
 *
 * @author David Koettlitz
 * <br>Erstellt am 14.11.2016
 *
 * @see AsyncPulseController
 */
public interface Pulse {
   static final long SECOND_IN_NANOS = 1000000000L;
   static final float DEFAULT_FPS = 60;
   static final float DEFAULT_INTERVALL = SECOND_IN_NANOS / DEFAULT_FPS;

   /**
    * Interpolates the value in order to fit the time that has passed since the last frame.
    *
    * @param value A value that is dependend to the time to be interpolated.
    *
    * @return The interpolated value
    */
   public float interpolate(float value);

   /**
    * Interpolates the vector in order to fit the time that has passed since the last frame.
    *
    * @param v A vector that is dependend to the time to be interpolated.
    *
    * @return The interpolated vector
    */
   public Vector interpolate(Vector v);

   /**
    * Provides the timestamp of this current pulse frame.
    *
    * @return The timestamp in nanoseconds
    */
   public long now();

   /**
    * Provides the time that has passed since the last pulse frame.
    *
    * @return The passed time in nanoseconds
    */
   public long frameTime();

   /**
    * Joins a pulse to this current running pulse.
    * The pulse will be invoked on its own pulserate.
    * But it is recommended to define a pulse with a higher or equal
    * pulse rate as parent than the child pulses.
    *
    * @param pulse The pulse to join this current running pulse
    */
   public void join(PulseController pulse);

   /**
    * Removes a joint pulse from this current running pulse.
    *
    * @param pulse The pulse to be removed from this current running pulse
    */
   public void remove(PulseController pulse);

   /**
    * Get the target interval time between two frames.
    *
    * @return The target interval time
    */
   long getInterval();

   /**
    * Get the number of frames that should be executed every second.
    *
    * @return the target framerate
    */
   float getFramerate();
}