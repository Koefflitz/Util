package de.dk.util;

/**
 * @author David Koettlitz
 * <br>Erstellt am 02.09.2016
 */
public interface UnsafeRunnable<T extends Throwable> {
   public void run() throws T;
}