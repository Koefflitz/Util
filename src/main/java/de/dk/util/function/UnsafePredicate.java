package de.dk.util.function;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A <code>Predicate</code> that throws an exception.
 * This interface is almost equivalent to {@link Predicate},
 * except it throws an exception.
 *
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 *
 * @see Predicate
 */
public interface UnsafePredicate<T, E extends Exception> {

   /**
    * Evaluates this predicate on the given argument.
    *
    * @param t the input argument
    *
    * @return {@code true} if the input argument matches the predicate,
    * otherwise {@code false}
    *
    * @throws E if something went wrong
    */
   boolean test(T t) throws E;

   /**
    * Returns a composed unsafe predicate that represents a short-circuiting logical
    * AND of this predicate and another.  When evaluating the composed
    * predicate, if this predicate is {@code false}, then the {@code other}
    * predicate is not evaluated.
    *
    * <p>Any exceptions thrown during evaluation of either predicate are relayed
    * to the caller; if evaluation of this predicate throws an exception, the
    * {@code other} predicate will not be evaluated.
    *
    * @param other a predicate that will be logically-ANDed with this
    *              predicate
    * @return a composed predicate that represents the short-circuiting logical
    * AND of this predicate and the {@code other} predicate
    * @throws NullPointerException if other is null
    */
   default UnsafePredicate<T, E> and(UnsafePredicate<? super T, ? extends E> other) {
       Objects.requireNonNull(other);
       return t -> test(t) && other.test(t);
   }

   /**
    * Returns an unsafe predicate that represents the logical negation of this
    * predicate.
    *
    * @return a predicate that represents the logical negation of this
    * predicate
    */
   default UnsafePredicate<T, E> negate() {
       return (t) -> !test(t);
   }

   /**
    * Returns a composed unsafe predicate that represents a short-circuiting logical
    * OR of this predicate and another.  When evaluating the composed
    * predicate, if this predicate is {@code true}, then the {@code other}
    * predicate is not evaluated.
    *
    * <p>Any exceptions thrown during evaluation of either predicate are relayed
    * to the caller; if evaluation of this predicate throws an exception, the
    * {@code other} predicate will not be evaluated.
    *
    * @param other a predicate that will be logically-ORed with this
    *              predicate
    * @return a composed predicate that represents the short-circuiting logical
    * OR of this predicate and the {@code other} predicate
    * @throws NullPointerException if other is null
    */
   default UnsafePredicate<T, E> or(UnsafePredicate<? super T, ? extends E> other) {
       Objects.requireNonNull(other);
       return (t) -> test(t) || other.test(t);
   }
}
