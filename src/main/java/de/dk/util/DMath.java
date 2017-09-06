package de.dk.util;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.random;
import static java.lang.Math.round;

import java.util.Random;

/**
 * @author David Koettlitz
 * <br>Erstellt am 10.11.2016
 */
public final class DMath {
   private static Random rnd;

   private DMath() {}

   private static Random rnd() {
      return rnd == null ? (rnd = new Random()) : rnd;
   }

   /**
    * Divides the dividend by the divisor and even supports division by zero operations.
    *
    * @param dividend The dividend to divide through <code>divisor</code>
    * @param divisor The divisor to divide <code>dividend</code> through
    *
    * @return The result of the division. If the divisor is <code>0</code>
    * the highest possible value is returned.
    */
   public static int divideSafely(int dividend, int divisor) {
      return divisor == 0 ? Integer.MAX_VALUE : dividend / divisor;
   }

   /**
    * Checks if the given <code>value</code> is an integer number.
    * In other words: Checks if there are <code>digits != 0</code> behind the decimal point.
    *
    * @param value The value to be checked
    *
    * @return <code>true</code> if the <code>value</code> has only zeros behind the decimal point.
    * <code>false</code> if it cannot be converted into an <code>int</code> without loosing information.
    */
   public static boolean isInteger(double value) {
      return value - (int) value == 0;
   }

   /**
    * Get the number with only the digits after the decimal point.
    * The digits before the decimal point are cleared.
    *
    * @param value The <code>double</code> value to get the digits
    * after the decimal point from.
    *
    * @return The digits of <code>value</code> after the decimal point.
    * The returned value will always be <br> <code>&lt; 1 &amp;&amp; &gt; -1</code>.
    */
   public static double getAfterPoint(double value) {
      return value - (int) value;
   }

   /**
    * Divides the dividend by the divisor and even supports division by zero operations.
    *
    * @param dividend The dividend to divide through <code>divisor</code>
    * @param divisor The divisor to divide <code>dividend</code> through
    *
    * @return The result of the division. If the divisor is <code>0</code>
    * the highest possible value is returned.
    */
   public static long divideSafely(long dividend, long divisor) {
      return divisor == 0 ? Long.MAX_VALUE : dividend / divisor;
   }

   /**
    * Divides the dividend by the divisor and even supports division by zero operations.
    *
    * @param dividend The dividend to divide through <code>divisor</code>
    * @param divisor The divisor to divide <code>dividend</code> through
    *
    * @return The result of the division. If the divisor is <code>0</code>
    * {@link Float#MAX_VALUE} is returned.
    */
   public static float divideSafely(float dividend, float divisor) {
      return divisor == 0 ? Float.MAX_VALUE : dividend / divisor;
   }

   /**
    * Divides the dividend by the divisor and even supports division by zero operations.
    *
    * @param dividend The dividend to divide through <code>divisor</code>
    * @param divisor The divisor to divide <code>dividend</code> through
    *
    * @return The result of the division. If the divisor is <code>0</code>
    * the highest possible value is returned.
    */
   public static double divideSafely(double dividend, double divisor) {
      return divisor == 0 ? Double.MAX_VALUE : dividend / divisor;
   }

   /**
    * Finds out the value with the highest absolute value.
    *
    * @param a first int-value
    * @param b second int-value
    * @return Either a if its absolute value is greater than the absolute value of b
    * or in other case b.
    */
   public static int maxabs(int a, int b) {
      return max(abs(a), abs(b)) == abs(a) ? a : b;
   }

   /**
    * Finds out the value with the highest absolute value.
    *
    * @param a first float-value
    * @param b second float-value
    * @return Either a if its absolute value is greater than the absolute value of b
    * or in other case b.
    */
   public static float maxabs(float a, float b) {
      return max(abs(a), abs(b)) == abs(a) ? a : b;
   }

   /**
    * Finds out the value with the highest absolute value.
    *
    * @param a first double-value
    * @param b second double-value
    * @return Either a if its absolute value is greater than the absolute value of b
    * or in other case b.
    */
   public static double maxabs(double a, double b) {
      return max(abs(a), abs(b)) == abs(a) ? a : b;
   }

   /**
    * Converts the long value to an int value. If the long value is too big or too small to fit into an int -
    * in other words: If <code>l &lt; Integer.MIN_VALUE || l &gt; Integer.MAX_VALUE</code>
    * it will be converted to the next possible int value
    * (<code>Integer.MIN_VALUE</code> or <code>Integer.MAX_VALUE</code>.
    *
    * @param l the long value to be converted
    * @return an int that has the same or the closest possible value to the long value
    */
   public static int cutToInt(long l) {
      return (int) delimit(l, Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   /**
    * Keeps a value in a specific range.
    * Returns a value that is &gt;= the min-value and &lt;= the max-value
    *
    * @param value An int-value
    * @param min The minimum to be returned
    * @param max The maximum to be returned
    *
    * @return If the value is less than the min-value the min-value is returned.
    * Is the value greater than the max-value the max-value is returned.
    * In all other cases, the value itself is returned.
    */
   public static int delimit(int value, int min, int max) throws IllegalArgumentException {
      if (min > max)
         throw new IllegalArgumentException("Min value was greater than max value.");

      return min(max, max(min, value));
   }

   /**
    * Keeps a value in a specific range.
    * Returns a value that is &gt;= the min-value and &lt;= the max-value
    *
    * @param value An int-value
    * @param min The minimum to be returned
    * @param max The maximum to be returned
    *
    * @return If the value is less than the min-value the min-value is returned.
    * Is the value greater than the max-value the max-value is returned.
    * In all other cases, the value itself is returned.
    */
   public static long delimit(long value, long min, long max) {
      return min(max, max(min, value));
   }

   /**
    * Keeps a value in a specific range.
    * Returns a value that is &gt;= the min-value and &lt;= the max-value
    *
    * @param value A float-value
    * @param min The minimum to be returned
    * @param max The maximum to be returned
    *
    * @return If the value is less than the min-value the min-value is returned.
    * Is the value greater than the max-value the max-value is returned.
    * In all other cases, the value itself is returned.
    */
   public static float delimit(float value, float min, float max) {
      return min(max, max(min, value));
   }

   /**
    * Keeps a value in a specific range.
    * Returns a value that is &gt;= the min-value and &lt;= the max-value
    *
    * @param value A double-value
    * @param min The minimum to be returned
    * @param max The maximum to be returned
    *
    * @return If the value is less than the min-value the min-value is returned.
    * Is the value greater than the max-value the max-value is returned.
    * In all other cases, the value itself is returned.
    */
   public static double delimit(double value, double min, double max) {
      return min(max, max(min, value));
   }

   /**
    * Blurs an int-value randomly with a specific maximum deviation.
    * For examlpe:<br>
    * A value of 100 and a percentage of 10 passed to this method
    * would return any int-value between 90 and 110.
    *
    * @param value An int-value to be blurred
    * @param percent The percentage of how much the value should be blurred maximum.
    *
    * @return A blurred value that is not more less or greater
    * than the value than the specified percentage.
    */
   public static int blur(int value, float percent) {
      return round((float) blur((double) value, (double) percent));
   }

   /**
    * Blurs a float-value randomly with a specific maximum deviation.
    * For examlpe:<br>
    * A value of 100 and a percentage of 10 passed to this method
    * would return any float-value between 90 and 110.
    *
    * @param value A float-value to be blurred
    * @param percent The percentage of how much the value should be blurred maximum.
    *
    * @return A blurred value that is not more less or greater
    * than the value than the specified percentage.
    */
   public static float blur(float value, float percent) {
      return (float) blur((double) value, (double) percent);
   }

   /**
    * Blurs a double-value randomly with a specific maximum deviation.
    * For examlpe:<br>
    * A value of 100 and a percentage of 10 passed to this method
    * would return any double-value between 90 and 110.
    *
    * @param value A double-value to be blurred
    * @param percent The percentage of how much the value should be blurred maximum.
    *
    * @return A blurred value that is not more less or greater
    * than the value than the specified percentage.
    */
   public static double blur(double value, double percent) {
      double reversePercent = max(100d - percent, 0);
      double factor = (random() * percent * 2 + reversePercent) / 100d;
      return value * factor;
   }

   /**
    * Randomizes the sign of an int-value.
    *
    * @param num An int-value
    *
    * @return To 50% a negative and to 50% a positive value
    * with the absolute amount of parameter <b><code>num</code></b>.
    */
   public static int randomizeSign(int num) {
      return Util.toInt(rnd().nextBoolean()) * num;
   }

   /**
    * Randomizes the sign of an float-value.
    *
    * @param num A float-value
    *
    * @return To 50% a negative and to 50% a positive value
    * with the absolute amount of parameter <b><code>num</code></b>.
    */
   public static float randomizeSign(float num) {
      return Util.toInt(rnd().nextBoolean()) * num;
   }

   /**
    * Randomizes the sign of an double-value.
    *
    * @param num A double-value
    *
    * @return To 50% a negative and to 50% a positive value
    * with the absolute amount of parameter <b><code>num</code></b>.
    */
   public static double randomizeSign(double num) {
      return Util.toInt(rnd().nextBoolean()) * num;
   }

   public static long millisToNanos(long millis) {
      return millis * 1000000;
   }

   public static long nanosToMillis(long nanos) {
      return nanos / 1000000;
   }

}