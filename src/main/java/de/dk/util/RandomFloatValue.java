package de.dk.util;

public class RandomFloatValue {
   private float minValue;
   private float maxValue;

   public RandomFloatValue(float minValue, float maxValue) {
      this.minValue = minValue;
      this.maxValue = maxValue;
   }

   public RandomFloatValue() {
      this(0, Integer.MAX_VALUE);
   }

   public float get() {
      return (float) (minValue + Math.random() * (maxValue - minValue));
   }

   public float getMinValue() {
      return minValue;
   }

   public void setMinValue(float minValue) {
      this.minValue = minValue;
   }

   public float getMaxValue() {
      return maxValue;
   }

   public void setMaxValue(float maxValue) {
      this.maxValue = maxValue;
   }

}