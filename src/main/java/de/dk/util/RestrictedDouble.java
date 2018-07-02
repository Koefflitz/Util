package de.dk.util;

import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

import de.dk.util.RestrictedDoubleListener.BoundedDoubleListenerChain;

/**
 * An double value wrapper with boundaries.
 * Contains a maximal boundary, a minimal boundary and an double value,
 * that is accessible through methods, which make sure the value is never
 * out of the set boundaries.
 *
 * @author David Koettlitz
 * <br>Erstellt am 21.06.2018
 */
public class RestrictedDouble implements Comparable<RestrictedDouble>, DoubleSupplier {
   private double value;
   private double min;
   private double max;

   private final BoundedDoubleListenerChain listeners = new BoundedDoubleListenerChain();

   /**
    * Create a new BoundedInt value with the specified bounds
    * and an initial value of 0.
    *
    * @param min The minimal value
    * @param max The maximal value
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public RestrictedDouble(double min, double max) throws IllegalArgumentException {
      this(min, max, 0);
   }

   /**
    * Create a new BoundedInt value with the specified bounds
    * and the given initial value.
    *
    * @param min The minimal value
    * @param max The maximal value
    * @param value The initial value for this BoundedInt object.
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public RestrictedDouble(double min, double max, double value) throws IllegalArgumentException {
      if (min > max)
         throwMinGreaterThanMin(min, max);

      this.min = min;
      this.max = max;
      set(value);
   }

   private static void throwMinGreaterThanMin(double min, double max) {
      throw new IllegalArgumentException("Given min value (" + min + ") was greater than max value (" + max + ")");
   }

   private static void throwMaxLessThanMin(double min, double max) {
      throw new IllegalArgumentException("Given max value (" + max + ") was less than min value (" + max + ")");
   }

   /**
    * Get the value.
    *
    * @return the actual double value
    */
   @Override
   public double getAsDouble() {
      return value;
   }

   public float getAsFloat() {
      return (float) value;
   }

   public int getAsInt() {
      return (int) value;
   }

   public short getAsShort() {
      return (short) value;
   }

   public byte getAsByte() {
      return (byte) value;
   }

   /**
    * Set the value. If the given <code>value</code> is greater than
    * the <code>max</code> bound, the value will become the <code>max</code> bound.
    *
    * If the <code>value</code> is less than the <code>min</code> bound,
    * the value will become the <code>min</code> bound.
    *
    * @param value The actual value of this BoundedInt object.
    *
    * @return The new value
    */
   public double set(double value) {
      double old = this.value;
      this.value = DMath.delimit(value, min, max);
      if (old != this.value)
         listeners.onChange(old, this.value);

      return this.value;
   }

   /**
    * Add the given <code>value</code> to this value.
    *
    * If the new value is greater than the <code>max</code> bound,
    * the value will become the <code>max</code> bound.
    *
    * If the new <code>value</code> is less than the <code>min</code> bound,
    * the value will become the <code>min</code> bound.
    *
    * @param value The value to add to this BoundedInt object.
    *
    * @return The new value
    */
   public double add(double value) {
      return set(this.value + value);
   }

   /**
    * Subtracts the given <code>value</code> from this value.
    *
    * If the new value is greater than the <code>max</code> bound,
    * the value will become the <code>max</code> bound.
    *
    * If the new <code>value</code> is less than the <code>min</code> bound,
    * the value will become the <code>min</code> bound.
    *
    * @param value The value to add to this BoundedInt object.
    *
    * @return The new value
    */
   public double sub(double value) {
      return set(this.value - value);
   }

   /**
    * Manipulate this value.
    *
    * If the new value is greater than the <code>max</code> bound,
    * the value will become the <code>max</code> bound.
    *
    * If the new <code>value</code> is less than the <code>min</code> bound,
    * the value will become the <code>min</code> bound.
    *
    * @param manipulator The function to manipulate this value.
    *
    * @return The new value
    */
   public double manipulate(DoubleUnaryOperator manipulator) {
      return set(manipulator.applyAsDouble(value));
   }

   /**
    * Get the minimum boundary of this value.
    *
    * @return The min bound
    */
   public double getMin() {
      return min;
   }

   /**
    * Set the minimum value.
    *
    * @param min the min bound
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void setMin(double min) throws IllegalArgumentException {
      if (min > max)
         throwMinGreaterThanMin(min, max);

      this.min = min;
   }

   /**
    * Increase the minimum value.
    *
    * @param increase the value to add to the min bound
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void increaseMin(double increase) throws IllegalArgumentException {
      setMin(min + increase);
   }

   /**
    * Decrease the minimum value.
    *
    * @param decrease the value to add to the min bound
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void decreaseMin(double decrease) throws IllegalArgumentException {
      setMin(min - decrease);
   }

   /**
    * Manipulate the minimum value.
    *
    * @param manipulator the function to manupulate the minimum value
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void manipulateMin(DoubleUnaryOperator manipulator) throws IllegalArgumentException {
      setMin(manipulator.applyAsDouble(min));
   }


   /**
    * Get the maximum boundary of this value.
    *
    * @return The max bound
    */
   public double getMax() {
      return max;
   }

   /**
    * Set the maximum value.
    *
    * @param max the min bound
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void setMax(double max) throws IllegalArgumentException {
      if (max < min)
         throwMaxLessThanMin(min, max);

      this.max = max;
   }

   /**
    * Increase the maximum value.
    *
    * @param increase the value to add to the max bound
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void increaseMax(double increase) throws IllegalArgumentException {
      setMax(max + increase);
   }

   /**
    * Decrease the maximum value.
    *
    * @param decrease the value to add to the max bound
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void decreaseMax(double decrease) throws IllegalArgumentException {
      setMax(max - decrease);
   }

   /**
    * Manipulate the maximum value.
    *
    * @param manipulator the function to manupulate the maximum value
    *
    * @throws IllegalArgumentException if <code>min</code> is greater than <code>max</code>.
    */
   public void manipulateMax(DoubleUnaryOperator manipulator) throws IllegalArgumentException {
      setMax(manipulator.applyAsDouble(max));
   }

   public void addListener(RestrictedDoubleListener listener) {
      listeners.add(listener);
   }

   public void removeListener(RestrictedDoubleListener listener) {
      listeners.remove(listener);
   }

   @Override
   public int compareTo(RestrictedDouble o) {
      return Double.compare(value, o.value);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits(this.value);
      result = prime * result + (int) (temp ^ (temp >>> 32));
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
      RestrictedDouble other = (RestrictedDouble) obj;
      if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "" + value;
   }

}
