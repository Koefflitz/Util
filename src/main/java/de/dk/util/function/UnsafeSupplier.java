package de.dk.util.function;

import java.util.function.Supplier;

/**
 * A <code>Supplier</code> that throws an exception.
 * This interface is almost equivalent to {@link Supplier},
 * except it throws an exception.
 *
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
@FunctionalInterface
public interface UnsafeSupplier<T, E extends Exception> {
   /**
    * Gets a result.
    *
    * @return a result
    *
    * @throws E
    */
   public T get() throws E;
}
