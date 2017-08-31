package de.dk.util;

import java.util.function.IntSupplier;

/**
 * A random float value generator,
 * that produces random int values between boundaries.
 * The {@link #getAsInt()} method produces the random int values inside the boundaries
 * and returns a new randomly generated value every time it is called.
 *
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class RandomIntValue implements IntSupplier {
   protected int minValue;
   protected int maxValue;

   /**
    * Creates a new random int value generator with the specified boundaries.
    *
    * @param minValue The minimum value to be generated
    * @param maxValue The maximum value to be generated
    */
   public RandomIntValue(int minValue, int maxValue) {
      this.minValue = minValue;
      this.maxValue = maxValue;
   }

   /**
    * Creates a new random int value generator
    * with minimum value <code>0</code> and maximum value {@link Integer#MAX_VALUE}.
    */
   public RandomIntValue() {
      this(0, Integer.MAX_VALUE);
   }

   /**
    * Get a random int value between the max- and the minValue.
    * This method returns a new generated value every time it is called.
    *
    * @return A randomly generated int value between the boundaries.
    */
   @Override
   public int getAsInt() {
      return (int) (minValue + Math.random() * (maxValue - minValue));
   }

   /**
    * Get the minimum value to be returned by the {@link #getAsInt()} method.
    *
    * @return the minimum value
    */
   public float getMinValue() {
      return minValue;
   }

   /**
    * Set the minimum value to be returned by the {@link #getAsInt()} method.
    *
    * @param minValue The minimum value to be returned
    * by the {@link #getAsInt()} method.
    */
   public void setMinValue(int minValue) {
      this.minValue = minValue;
   }

   /**
    * Get the maximum value to be returned by the {@link #getAsInt()} method.
    *
    * @return the maximum value
    */
   public float getMaxValue() {
      return maxValue;
   }

   /**
    * Set the maximum value to be returned by the {@link #getAsInt()} method.
    *
    * @param maxValue The maximum value to be returned
    * by the {@link #getAsInt()} method.
    */
   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.maxValue;
      result = prime * result + this.minValue;
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
      RandomIntValue other = (RandomIntValue) obj;
      if (this.maxValue != other.maxValue)
         return false;
      if (this.minValue != other.minValue)
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "RandomIntValue { "
             + "minValue=" + minValue
             + ", maxValue=" + maxValue
             + " }";
   }

}
