package de.dk.util;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public interface UnsafeConsumer<T, E extends Exception> {
   public void accept(T value) throws E;
}
