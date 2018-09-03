package de.dk.util.function;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Represents an operation on a single operand that produces a result of the
 * same type as its operand.
 *
 * @author David Koettlitz
 * <br>Erstellt am 31.08.2018
 *
 * @see UnaryOperator
 */
public interface UnaryOperatorFloat {

   /**
    * Applies this function to the given argument.
    *
    * @param t the function argument
    *
    * @return the function result
    */
   float apply(float t);

   /**
    * Returns a composed function that first applies the {@code before}
    * function to its input, and then applies this function to the result.
    * If evaluation of either function throws an exception, it is relayed to
    * the caller of the composed function.
    *
    * @param before the function to apply before this function is applied
    * @return a composed function that first applies the {@code before}
    * function and then applies this function
    * @throws NullPointerException if before is null
    *
    * @see #andThen(UnaryOperatorFloat)
    */
   default UnaryOperatorFloat compose(UnaryOperatorFloat before) {
       Objects.requireNonNull(before);
       return (float v) -> apply(before.apply(v));
   }

   /**
    * Returns a composed function that first applies this function to
    * its input, and then applies the {@code after} function to the result.
    * If evaluation of either function throws an exception, it is relayed to
    * the caller of the composed function.
    *
    * @param after the function to apply after this function is applied
    * @return a composed function that first applies this function and then
    * applies the {@code after} function
    * @throws NullPointerException if after is null
    *
    * @see #compose(UnaryOperatorFloat)
    */
   default UnaryOperatorFloat andThen(UnaryOperatorFloat after) {
       Objects.requireNonNull(after);
       return (float t) -> after.apply(apply(t));
   }

   /**
    * Returns a unary operator that always returns its input argument.
    *
    * @return a unary operator that always returns its input argument
    */
   static UnaryOperatorFloat identity() {
       return t -> t;
   }
}
