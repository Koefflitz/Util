package de.dk.util;

@FunctionalInterface
public interface UnsafeBiFunction<T, U, R, E extends Exception> {
   public R apply(T t, U u) throws E;
}