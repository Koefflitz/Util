package de.dk.util;

@FunctionalInterface
public interface UnsafeSupplier<T, E extends Exception> {
   public T get() throws E;
}