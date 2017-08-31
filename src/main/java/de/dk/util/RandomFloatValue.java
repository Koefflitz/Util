package de.dk.util;

import java.util.function.DoubleSupplier;

/**
 * A random float value generator,
 * that produces random float values between boundaries.
 * The {@link #get()} method produces the random float values inside the boundaries
 * and returns a new randomly generated value every time it is called.
 *
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 *
 * @see RandomIntValue
 * @see IncreasingRandomFloatValue
 */
public class RandomFloatValue implements DoubleSupplier {
   protected float minValue;
   protected float maxValue;

   /**
    * Creates a new random float value generator with the specified boundaries.
    *
    * @param minValue The minimum value to be generated
    * @param maxValue The maximum value to be generated
    */
   public RandomFloatValue(float minValue, float maxValue) {
      this.minValue = minValue;
      this.maxValue = maxValue;
   }

   /**
    * Creates a new random float value generator
    * with minimum value <code>0</code> and maximum value {@link Float#MAX_VALUE}.
    */
   public RandomFloatValue() {
      this(0, Float.MAX_VALUE);
   }

   /**
    * Get a random float value between the max- and the minValue.
    * This method returns a new generated value every time it is called.
    *
    * @return A randomly generated float value between the boundaries.
    */
   public float get() {
      return (float) (minValue + Math.random() * (maxValue - minValue));
   }

   /**
    * Creates a new random double value generator
    * with minimum value <code>0</code> and maximum value {@link Float#MAX_VALUE}.
    * This method just calls the {@link #get()} method.
    */
   @Override
   public double getAsDouble() {
      return get();
   }

   /**
    * Get the minimum value to be returned by the {@link #get()} method.
    *
    * @return the minimum value
    */
   public float getMinValue() {
      return minValue;
   }

   /**
    * Set the minimum value to be returned by the {@link #get()} method.
    *
    * @param minValue The minimum value to be returned
    * by the {@link #get()} method.
    */
   public void setMinValue(float minValue) {
      this.minValue = minValue;
   }

   /**
    * Get the maximum value to be returned by the {@link #get()} method.
    *
    * @return the maximum value
    */
   public float getMaxValue() {
      return maxValue;
   }

   /**
    * Set the maximum value to be returned by the {@link #get()} method.
    *
    * @param maxValue The maximum value to be returned
    * by the {@link #get()} method.
    */
   public void setMaxValue(float maxValue) {
      this.maxValue = maxValue;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Float.floatToIntBits(this.maxValue);
      result = prime * result + Float.floatToIntBits(this.minValue);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      RandomFloatValue other = (RandomFloatValue) obj;
      if (Float.floatToIntBits(this.maxValue) != Float.floatToIntBits(other.maxValue))
         return false;
      if (Float.floatToIntBits(this.minValue) != Float.floatToIntBits(other.minValue))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "RandomFloatValue { "
             + "minValue=" + minValue
             + ", maxValue=" + maxValue
             + " }";
   }

}
