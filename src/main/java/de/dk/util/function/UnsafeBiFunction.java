package de.dk.util.function;

import java.util.function.BiFunction;

/**
 * A BiFunction that throws an Exception.
 * This is an almost-equivalent to {@link BiFunction}, but the method throws an exception.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception that is thrown
 *
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 *
 * @see BiFunction
 */
@FunctionalInterface
public interface UnsafeBiFunction<T, U, R, E extends Exception> {
   /**
    * Applies this function to the given arguments.
    *
    * @param t the first function argument
    * @param u the second function argument
    *
    * @return the function result
    *
    * @throws E
    */
   public R apply(T t, U u) throws E;

   /**
    * Returns a composed function that first applies this function to
    * its input, and then applies the {@code after} function to the result.
    * If evaluation of either function throws an exception, it is relayed to
    * the caller of the composed function.
    *
    * @param <V> the type of output of the {@code after} function, and of the
    *           composed function
    * @param after the function to apply after this function is applied
    *
    * @return a composed function that first applies this function and then
    * applies the {@code after} function
    *
    * @throws NullPointerException if after is <code>null</code>
    */
   default <V> UnsafeBiFunction<T, U, V, E> andThen(UnsafeFunction<? super R, V, ? extends E> after) {
      return (t, u) -> after.apply(apply(t, u));
   }
}
