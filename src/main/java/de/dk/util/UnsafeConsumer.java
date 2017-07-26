package de.dk.util;

public interface UnsafeConsumer<T, E extends Exception> {
   public void accept(T value) throws E;
}