package de.dk.util.timing;

import de.dk.util.game.Vector;

/**
 * Represents the current cycle of a pulse and provides information about the cycle.
 * A pulsehandler attached to a {@link PulseController} will receive a Pulse object in each pulsecylce.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 14.11.2016
 *
 * @see PulseController
 */
public interface Pulse {

   /**
    * Interpolates the value in order to fit the time that has passed since the last cycle.
    *
    * @param value A value that is dependend to the time to be interpolated.
    *
    * @return The interpolated value
    */
   public float interpolate(float value);

   /**
    * Interpolates the vector in order to fit the time that has passed since the last cycle.
    *
    * @param v A vector that is dependend to the time to be interpolated.
    *
    * @return The interpolated vector
    */
   public Vector interpolate(Vector v);

   /**
    * Provides the timestamp of this current pulse cycle.
    *
    * @return The timestamp in nanoseconds
    */
   public long now();

   /**
    * Provides the time that has passed since the last pulse cycle.
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
    * Get the calls per second
    *
    * @return the rate in which this pulse is running.
    * It provides information about how many times per second it is called.
    */
   public float getCps();

   /**
    * Get the interval time between calls.
    *
    * @return The interval time that has to pass between the calls.
    */
   public long getInterval();
}