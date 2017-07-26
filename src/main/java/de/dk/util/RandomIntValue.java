package de.dk.util;

public class RandomIntValue {
   private int minValue;
   private int maxValue;

   public RandomIntValue(int minValue, int maxValue) {
      this.minValue = minValue;
      this.maxValue = maxValue;
   }

   public RandomIntValue() {
      this(0, Integer.MAX_VALUE);
   }

   public int get() {
      return (int) (minValue + Math.random() * (maxValue - minValue));
   }

   public int getMinValue() {
      return minValue;
   }

   public void setMinValue(int minValue) {
      this.minValue = minValue;
   }

   public int getMaxValue() {
      return maxValue;
   }

   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

}