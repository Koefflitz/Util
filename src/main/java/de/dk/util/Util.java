package de.dk.util;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import de.dk.util.function.UnsafeConsumer;
import de.dk.util.function.UnsafeSupplier;

/**
 * Some static utility methods, that can sometimes be useful.
 *
 * @author David Koettlitz
 * <br>Erstellt am 30.08.2016
 */
public final class Util {

   private Util() {}

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    * @param <T> The type of the array
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static <T> T[] invert(T[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         T element = array[i];
         array[i] = array[j];
         array[j--] = element;
      }
      return array;
   }

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static byte[] invertByteArray(byte[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         byte b = array[i];
         array[i] = array[j];
         array[j--] = b;
      }
      return array;
   }

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static short[] invertShortArray(short[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         short value = array[i];
         array[i] = array[j];
         array[j--] = value;
      }
      return array;
   }

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static int[] invertIntArray(int[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         int value = array[i];
         array[i] = array[j];
         array[j--] = value;
      }
      return array;
   }

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static long[] invertLongArray(long[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         long value = array[i];
         array[i] = array[j];
         array[j--] = value;
      }
      return array;
   }

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static boolean[] invertBoolArray(boolean[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         boolean value = array[i];
         array[i] = array[j];
         array[j--] = value;
      }
      return array;
   }

   /**
    * Inverts the <code>array</code>.
    *
    * @param array The array to be inverted
    *
    * @return the inverted array which is the same object as the parameter array
    */
   public static char[] invertCharArray(char[] array) {
      if (array == null)
         return null;

      int j = array.length - 1;
      for (int i = 0; i < array.length / 2; i++) {
         char value = array[i];
         array[i] = array[j];
         array[j--] = value;
      }
      return array;
   }

   /**
    * Get the last item of the <code>array</code>
    *
    * @param array The array from which to get the last item
    * @param <T> The type of the array
    *
    * @return The last item of the given <code>array</code>
    */
   public static <T> T lastItemOf(T[] array) {
      return array[array.length - 1];
   }

   /**
    * This method is equivalent to <code>flag ? 1 : -1</code>.
    *
    * @param flag The <code>boolean</code> to be converted to an <code>int</code>
    *
    * @return the value 1 if <code>true</code> is passed, otherwise -1.
    */
   public static int toInt(boolean flag) {
      return flag ? 1 : -1;
   }

   public static <T, E extends Exception> void loop(UnsafeSupplier<T, E> varSupplier,
                                                    Predicate<? super T> condition,
                                                    UnsafeConsumer<? super T, E> action) throws E {
      for (T var = varSupplier.get(); condition.test(var); var = varSupplier.get())
         action.accept(var);
   }

   public static <T> Consumer<T> noOp() {
      return t -> {};
   }

   /**
    * Checks the equality of two objects considering <code>null</code> values.
    * Calls the <code>equals</code> method of object a if it is not <code>null</code>.
    *
    * @param a the object to check for equality to <code>b</code>
    * @param b the object to check for equality to <code>a</code>
    *
    * @return <code>true</code> if a AND b are <code>null</code> or if <code>a.equals(b)</code>.
    * <code>false</code> if only one of the objects is <code>null</code> or if <code>!a.equals(b)</code>.
    */
   public static boolean checkEquality(Object a, Object b) {
      if (a == null)
         return b == null;

      return a.equals(b);
   }

   /**
    * This method is equivalent to flag &gt;= 0.
    *
    * @param flag The integer value to be converted to <code>boolean</code>
    *
    * @return <code>true</code> if the value is greater than or equal 0.
    * Otherwise <code>false</code>.
    */
   public static boolean toFlag(int flag) {
      return flag >= 0;
   }

   public static boolean[] box(Boolean[] array) {
      boolean[] result = new boolean[array.length];
      for (int i = 0; i < result.length; i++)
         result[i] = array[i];

      return result;
   }

   public static char[] box(Character[] array) {
      char[] result = new char[array.length];
      for (int i = 0; i < result.length; i++)
         result[i] = array[i];

      return result;
   }

   public static Character[] box(char[] array) {
      Character[] result = new Character[array.length];
      for (int i = 0; i < result.length; i++)
         result[i] = array[i];

      return result;
   }

   public static Integer[] box(int[] array) {
      Integer[] result = new Integer[array.length];
      for (int i = 0; i < result.length; i++)
         result[i] = array[i];

      return result;
   }

   public static int[] box(Integer[] array) {
      int[] result = new int[array.length];
      for (int i = 0; i < result.length; i++)
         result[i] = array[i];

      return result;
   }

   /**
    * Compares 2 objects by their {@link #toString()} method.
    *
    * @param a The first object to be compared
    * @param b The second object to be compared
    *
    * @return a negative integer, zero, or a positive integer as the
    * first argument is less than, equal to, or greater than the
    * second.
    *
    * @see Comparator
    */
   public static int compareByToString(Object a, Object b) {
      return a.toString().compareTo(b.toString());
   }
}