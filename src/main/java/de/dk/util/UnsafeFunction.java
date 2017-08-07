package de.dk.util;

@FunctionalInterface
/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public interface UnsafeFunction<T, R, E extends Exception> {
   public R apply(T t) throws E;
}
