package de.dk.util.unit.memory;

import java.text.DecimalFormat;
import java.util.function.BinaryOperator;

/**
 * Represents a memory value with a memory unit.
 *
 * @author David Koettlitz
 * <br>Erstellt am 15.06.2017
 */
public class MemoryValue {
   private double value;
   private MemoryUnit unit;

   /**
    * Creates a new MemoryValue with the specified <code>value</code> and the given <code>unit</code>.
    *
    * @param value The actual value
    * @param unit the MemoryUnit of this value
    */
   public MemoryValue(double value, MemoryUnit unit) {
      this.value = value;
      this.unit = unit;
   }

   /**
    * Creates a new MemoryValue with the specified <code>unit</code> and an actual value of 0.
    * @param unit The unit this MemoryValue should have
    */
   public MemoryValue(MemoryUnit unit) {
      this(0, unit);
   }

   /**
    * Creates a new MemoryValue with the specified value of bytes.
    * Converts the MemoryUnit to the best matching unit for the given number of <code>bytes</code>.
    *
    * @param bytes The number of bytes, that represent the value of this MemoryValue object
    *
    * @throws IllegalArgumentException If a value, that cannot be expressed in MemoryUnits is passed,
    * e.g. 0.00001 or <code>bytes < 0</code>.
    */
   public MemoryValue(double bytes) throws IllegalArgumentException {
      this(bytes, MemoryUnit.BYTE);
      setUnit(MemoryUnit.getBestUnitFor(bytes));
   }

   /**
    * Adds the given MemoryValues together and returns the result.
    *
    * @param v0 One of the MemoryValues to be added together
    * @param v1 The other MemoryValue to be added to <code>v0</code>
    *
    * @return The result of the addition.
    * The result MemoryValue object will have the MemoryUnit of <code>v0</code>.
    */
   public static MemoryValue addValues(MemoryValue v0, MemoryValue... v1) {
      return addValues(v0.getUnit(), v0, v1);
   }

   /**
    * Adds the given MemoryValues together and returns the result.
    *
    * @param targetUnit The MemoryUnit the result object should be of.
    * @param v0 One of the MemoryValues to be added together
    * @param v1 The other MemoryValues to be added to <code>v0</code>
    *
    * @return The result of the addition with the <code>targetUnit</code>.
    */
   public static MemoryValue addValues(MemoryUnit targetUnit, MemoryValue v0, MemoryValue... v1) {
      return compute(targetUnit, Double::sum, v0, v1);
   }

   /**
    * Subtracts <code>v1</code> from <code>v0</code>.
    *
    * @param v0 The MemoryValue to substract the others from.
    * @param v1 The MemoryValues to be subtracted from the first one.
    *
    * @return The result of the subtraction.
    * The result MemoryValue object will have the MemoryUnit of <code>v0</code>.
    */
   public static MemoryValue subtractValues(MemoryValue v0, MemoryValue... v1) {
      return subtractValues(v0.getUnit(), v0, v1);
   }

   /**
    * Subtracts <code>v1</code> from <code>v0</code>.
    *
    * @param targetUnit The MemoryUnit the result object should be of.
    * @param v0 The MemoryValue to substract the others from.
    * @param v1 The MemoryValues to be subtracted from the first one.
    *
    * @return The result of the subtraction with the <code>targetUnit</code>.
    */
   public static MemoryValue subtractValues(MemoryUnit targetUnit, MemoryValue v0, MemoryValue... v1) {
      return compute(targetUnit, (a, b) -> a - b, v0, v1);
   }

   /**
    * Computes <code>v0</code> with every value of <code>v0</code> using the given <code>op</code>.
    *
    * @param targetUnit The MemoryUnit the result should be of.
    * @param op The operation to be executed for the value of <code>v0</code> and every value of <code>v1</code>
    * @param v0 The MemoryValue to be computed with the others
    * @param v1 The MemoryValues to be computed with <code>v0</code>
    *
    * @return The result of the computation with the given <code>targetUnit</code>.
    */
   public static MemoryValue compute(MemoryUnit targetUnit,
                                     BinaryOperator<Double> op,
                                     MemoryValue v0,
                                     MemoryValue... v1) {

      double value = v0.getValueAs(targetUnit);
      for (MemoryValue mv : v1)
         value = op.apply(value, mv.getValueAs(targetUnit));

      return new MemoryValue(value, targetUnit);
   }

   /**
    * Adds the <code>values</code> to this value.
    *
    * @param values The values to be added
    *
    * @return The result of the addition
    */
   public MemoryValue add(MemoryValue... values) {
      for (MemoryValue mv : values)
         this.value += mv.unit.convertTo(mv.getValue(), unit);

      return this;
   }

   /**
    * Subtracts the <code>values</code> from this value.
    *
    * @param values The values to be subtracted from
    *
    * @return The result of the subtraction
    */
   public MemoryValue subtract(MemoryValue... values) {
      for (MemoryValue mv : values)
         this.value -= mv.unit.convertTo(mv.getValue(), unit);

      return this;
   }

   /**
    * Adds the given <code>values</code> to this MemoryValue.
    * This method doesn't check for any MemoryUnits, it just plainly adds the given double <code>values</code>
    * to the actual double value of this object.
    *
    * @param values The values to be added to this MemoryValue.
    *
    * @return This object for calling more operations on it.
    */
   public MemoryValue add(double... values) {
      for (double v : values)
         this.value += v;

      return this;
   }

   /**
    * Subtracts the given <code>values</code> from this MemoryValue.
    * This method doesn't check for any MemoryUnits, it just plainly subtracts the given double <code>values</code>
    * from the actual double value of this object.
    *
    * @param values The values to be subtracted from this MemoryValue.
    *
    * @return This object for calling more operations on it.
    */
   public MemoryValue subtract(double... values) {
      for (double v : values)
         this.value -= v;

      return this;
   }

   /**
    * Multiplies the given <code>values</code> with this MemoryValue.
    * This method doesn't check for any MemoryUnits, it just plainly multiplies the given double <code>values</code>
    * with the actual double value of this object.
    *
    * @param values The values to be added to this MemoryValue.
    *
    * @return This object for calling more operations on it.
    */
   public MemoryValue multiply(double... values) {
      for (double v : values)
         this.value *= v;

      return this;
   }

   /**
    * Divides this MemoryValue through the given values.
    * This method doesn't check for any MemoryUnits, it just plainly divides the actual double value of this object
    * through the given double <code>values</code>.
    *
    * @param values The values to divide this MemoryValue through.
    *
    * @return This object for calling more operations on it.
    */
   public MemoryValue divide(double... values) throws ArithmeticException {
      for (double v : values)
         this.value /= v;

      return this;
   }

   /**
    * Provides the value of this MemoryValue in the specified <code>unit</code>.
    *
    * @param unit The unit of the value
    *
    * @return The value of this MemoryValue in the specified <code>unit</code>.
    */
   public double getValueAs(MemoryUnit unit) {
      return this.unit.convertTo(value, unit);
   }

   /**
    * Get the actual value of this object.
    *
    * @return The actual value of this object.
    */
   public double getValue() {
      return value;
   }

   /**
    * Set the actual value of this object
    *
    * @param value The actual value of this object
    *
    * @return This object for calling more operations on it.
    */
   public MemoryValue setValue(double value) {
      this.value = value;
      return this;
   }

   /**
    * Get the MemoryUnit of this object.
    *
    * @return The MemoryUnit of this object
    */
   public MemoryUnit getUnit() {
      return unit;
   }

   /**
    * Set the MemoryUnit of this object.
    * The value will be converted to stay equivalent with the new MemoryUnit.
    *
    * @param unit The new MemoryUnit of this object
    *
    * @return This object for calling more operations on it.
    */
   public MemoryValue setUnit(MemoryUnit unit) {
      this.value = this.unit.convertTo(value, unit);
      this.unit = unit;
      return this;
   }

   /**
    * Get the String where the value is formatted.
    *
    * @param format The format for the value of this object
    *
    * @return The formatted value + the short name of the MemoryUnit
    */
   public String toString(DecimalFormat format) {
      return format.format(value) + unit;
   }

   @Override
   public String toString() {
      return "" + value + unit;
   }
}