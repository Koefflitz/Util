package de.dk.util.function;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * A <code>BiConsumer</code> that throws an Exception.
 * This is an almost-equivalent to {@link BiConsumer}, but the method throws an exception.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <E> the type of the Exception that might be thrown
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.10.2017
 *
 * @see BiConsumer
 * @since 1.8
 */
@FunctionalInterface
public interface UnsafeBiConsumer<T, U, E extends Exception> {
   /**
    * Performs this operation on the given arguments.
    *
    * @param t the first input argument
    * @param u the second input argument
    *
    * @throws E The exception that might be thrown
    */
   void accept(T t, U u) throws E;

   /**
    * Returns a composed {@code BiConsumer} that performs, in sequence, this
    * operation followed by the {@code after} operation. If performing either
    * operation throws an exception, it is relayed to the caller of the
    * composed operation.  If performing this operation throws an exception,
    * the {@code after} operation will not be performed.
    *
    * @param after the operation to perform after this operation
    * @return a composed {@code UnsafeBiConsumer} that performs in sequence this
    * operation followed by the {@code after} operation.
    * If the first operation throws an exception the second will not be executed.
    *
    * @throws NullPointerException if {@code after} is <code>null</code>
    */
   default UnsafeBiConsumer<T, U, E> andThen(UnsafeBiConsumer<? super T, ? super U, ? extends E> after) {
       Objects.requireNonNull(after);

       return (l, r) -> {
           accept(l, r);
           after.accept(l, r);
       };
   }
}
