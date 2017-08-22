package de.dk.util;

import java.util.Comparator;
import java.util.function.Predicate;

import de.dk.util.function.UnsafeConsumer;
import de.dk.util.function.UnsafeSupplier;

/**
 * Some static utility methods, that can sometimes be useful.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 30.08.2016
 */
public final class Util {

   private Util() {}

   /**
    * This method is equivalent to <code>flag ? 1 : -1</code>.
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
    * This method is equivalent to flag >= 0.
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

   /**
    * Compares 2 objects by their {@link #toString()} method.
    *
    * @see Comparator
    */
   public static int compareByToString(Object a, Object b) {
      return a.toString().compareTo(b.toString());
   }
}