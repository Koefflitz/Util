package de.dk.util.function;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * An <code>UnaryOperator</code> that throws an exception.
 * This interface is almost equivalent to {@link UnaryOperator},
 * except it throws an exception.
 *
 * @author David Koettlitz
 * <br>Erstellt am 22.08.2017
 *
 * @see UnaryOperator
 */
@FunctionalInterface
public interface UnsafeUnaryOperator<T, E extends Exception> extends UnsafeFunction<T, T, E> {
   /**
    * Returns a unary operator that always returns its input argument.
    *
    * @param <T> the type of the input and output of the operator
    *
    * @return a unary operator that always returns its input argument
    */
   public static <T> UnsafeUnaryOperator<T, RuntimeException> identity() {
      return t -> t;
   }

   /**
    * Returns a composed <code>UnsafeUnaryOperator</code> that first applies
    * this function and then applies <code>after</code> to the result.
    *
    * @param after The function to apply with the result of this function
    *
    * @return A composed <code>UnsafeUnaryOperator</code> that first applies
    * this function and then applies <code>after</code> to the result.
    */
   default UnsafeUnaryOperator<T, E> andThen(UnsafeUnaryOperator<T, E> after) {
      return t -> after.apply(apply(t));
   }

   /**
    * Returns a composed <code>UnsafeUnaryOperator</code> that first applies this function to
    * its input, and then applies the {@code after} function to the result.
    * If evaluation of either function throws an exception, it is relayed to
    * the caller of the composed function.
    *
    * @param after the function to apply after this function is applied
    *
    * @return A composed <code>UnsafeUnaryOperator</code> that first applies this function to
    * its input, and then applies the {@code after} function to the result.
    *
    * @throws NullPointerException if after is null
    *
    * @see #andThen
    * @see #compose(UnsafeFunction)
    */
   default UnsafeUnaryOperator<T, E> compose(UnsafeUnaryOperator<T, E> before) {
      Objects.requireNonNull(before);
      return t -> apply(before.apply(t));
   }
}
