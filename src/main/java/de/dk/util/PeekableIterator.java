package de.dk.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public interface PeekableIterator<E> extends Iterator<E> {
   public E peek() throws NoSuchElementException;
}
