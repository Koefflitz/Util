package de.dk.util;

@FunctionalInterface
/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public interface UnsafeSupplier<T, E extends Exception> {
   public T get() throws E;
}
