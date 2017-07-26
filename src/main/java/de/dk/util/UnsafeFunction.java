package de.dk.util;

@FunctionalInterface
public interface UnsafeFunction<T, R, E extends Exception> {
   public R apply(T t) throws E;
}