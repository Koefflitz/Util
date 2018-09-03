package de.dk.util.function;

import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * Represents an operation upon two float operands, producing a float result.
 *
 * @see BinaryOperator
 * @since 1.8
 */
@FunctionalInterface
public interface BinaryOperatorFloat {

   /**
    * Applies this function to the given arguments.
    *
    * @param t the first function argument
    * @param u the second function argument
    * @return the function result
    */
   float apply(float t, float u);

   /**
    * Returns a composed function that first applies this function to its input, and then applies the {@code after} function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the composed function.
    *
    * @param after the function to apply after this function is applied
    * @return a composed function that first applies this function and then applies the {@code after} function
    * @throws NullPointerException if after is null
    */
   default BinaryOperatorFloat andThen(UnaryOperatorFloat after) {
      Objects.requireNonNull(after);
      return (t, u) -> after.apply(apply(t, u));
   }

   /**
    * Returns a {@link BinaryOperatorFloat} which returns the lesser of two elements according to the specified {@code Comparator}.
    *
    * @return a {@code BinaryOperatorFloat} which returns the lesser of its operands
    */
   public static BinaryOperatorFloat min() {
      return (a, b) -> a <= b ? a : b;
   }

   /**
    * Returns a {@link BinaryOperatorFloat} which returns the greater of two elements according to the specified {@code Comparator}.
    *
    * @return a {@code BinaryOperatorFloat} which returns the greater of its operands
    */
   public static BinaryOperatorFloat max() {
      return (a, b) -> a >= b ? a : b;
   }
}
