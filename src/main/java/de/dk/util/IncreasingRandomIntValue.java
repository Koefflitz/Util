package de.dk.util;

/**
 * A <code>RandomIntValue</code>, that generates random float values,
 * where the probabilities are different for each possible float value.
 * The probabilites rise/fall the closer the values come to the boundaries.
 * In other words: if the boundaries are set to
 * <code>minValue=0</code> and <code>maxValue=100</code>.
 * low values like 1, 2, 3, 4... are less likely generated
 * than high values (100, 99, 98...).
 * This behaviour can be reversed by calling the {@link #setDecreasing()} method.
 *
 * @author David Koettlitz
 * <br>Erstellt am 30.08.2017
 *
 * @see RandomIntValue
 */
public class IncreasingRandomIntValue extends RandomIntValue {
   private static final float DEFAULT_INCREASE = 1;

   private float increase;
   private boolean decreasing;

   /**
    * Creates a new random float value generator with the specified boundaries.
    *
    * @param minValue The minimum value to be generated
    * @param maxValue The maximum value to be generated
    * @param increase The parameter that affects the amount
    * of the in/decrease of the probability. Default is 2.
    * @param decreasing <code>true</code> if low values should have a higher
    * probability to be generated. <code>false</code> if high values
    * should have a higher probability to be generated
    */
   public IncreasingRandomIntValue(int minValue, int maxValue, float increase, boolean decreasing) {
      super(minValue, maxValue);
      this.decreasing = decreasing;
      setIncrease(increase);
   }

   /**
    * Creates a new random int value generator with the specified boundaries.
    *
    * @param minValue The minimum value to be generated
    * @param maxValue The maximum value to be generated
    * @param increase The parameter that affects the amount
    * of the in/decrease of the probability. Default is 2.
    */
   public IncreasingRandomIntValue(int minValue, int maxValue, float increase) {
      this(minValue, maxValue, increase, false);
   }

   /**
    * Creates a new random int value generator with the specified boundaries.
    *
    * @param minValue The minimum value to be generated
    * @param maxValue The maximum value to be generated
    * @param decreasing <code>true</code> if low values should have a higher
    * probability to be generated. <code>false</code> if high values
    * should have a higher probability to be generated
    */
   public IncreasingRandomIntValue(int minValue, int maxValue, boolean decreasing) {
      this(minValue, maxValue, DEFAULT_INCREASE, decreasing);
   }

   /**
    * Creates a new random int value generator with the specified boundaries.
    *
    * @param minValue The minimum value to be generated
    * @param maxValue The maximum value to be generated
    */
   public IncreasingRandomIntValue(int minValue, int maxValue) {
      this(minValue, maxValue, DEFAULT_INCREASE);
   }

   /**
    * Creates a new random int value generator
    * with minimum value <code>0</code> and maximum value {@link Float#MAX_VALUE}.
    */
   public IncreasingRandomIntValue() {

   }

   @Override
   public int getAsInt() {
      float power = decreasing ? increase : (1f / increase);
      int spectrum = maxValue - minValue;
      return Math.round(minValue + (float) Math.pow(Math.random(), power) * spectrum);
   }

   /**
    * Set the parameter that affects the amount of in/decrease
    * of the probability of the values generated the closer
    * they get to the boundaries
    * If this generator is in increasing mode (which is default),
    * values closer to the <code>maxValue</code> have a higher
    * probability to be generated.
    * If it is in decreasing mode the opposite applies.
    *
    * @param increase Affects the amount of in/decrease
    * of the probablitiy of the values the closer they get to
    * the set boundaries.
    *
    * @throws IllegalArgumentException If <code>increase &lt; 0</code>.
    */
   public void setIncrease(float increase) throws IllegalArgumentException {
      if (increase < 0)
         throw new IllegalArgumentException("Increase cannot be < 0.");

      this.increase = increase + 1;
   }

   public float getIncrease() {
      return increase - 1;
   }

   /**
    * Puts this generator into increasing mode.
    * If this generator is in increasing mode (which is default),
    * values closer to the <code>maxValue</code> have a higher
    * probability to be generated.
    */
   public void setIncreasing() {
      this.decreasing = false;
   }

   /**
    * Puts this generator into decreasing mode.
    * If this generator is in decreasing mode,
    * values closer to the <code>minValue</code> have a higher
    * probability to be generated.
    */
   public void setDecreasing() {
      this.decreasing = true;
   }

   /**
    * Get whether this generator is in increasing mode or not.
    * If this generator is in increasing mode (which is default),
    * values closer to the <code>maxValue</code> have a higher
    * probability to be generated.
    * If it is in decreasing mode the opposite applies.
    *
    * @return <code>true</code> if this generator is in increasing mode,
    * <code>false</code> otherwise.
    */
   public boolean isIncreasing() {
      return !decreasing;
   }

   /**
    * Get whether this generator is in decreasing mode or not.
    * If this generator is in decreasing mode,
    * values closer to the <code>minValue</code> have a higher
    * probability to be generated.
    * If it is in increasing mode the opposite applies.
    *
    * @return <code>true</code> if this generator is in decreasing mode,
    * <code>false</code> otherwise.
    */
   public boolean isDecreasing() {
      return decreasing;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (this.decreasing ? 1231 : 1237);
      result = prime * result + Float.floatToIntBits(this.increase);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      IncreasingRandomIntValue other = (IncreasingRandomIntValue) obj;
      if (this.decreasing != other.decreasing)
         return false;
      if (Float.floatToIntBits(this.increase) != Float.floatToIntBits(other.increase))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "IncreasingRandomIntValue { "
             + "minValue=" + minValue
             + ", maxValue=" + maxValue
             + ", in/decrease=" + increase
             + ", mode=" + (decreasing ? "decreasing" : "increasing")
             + " }";
   }
}
