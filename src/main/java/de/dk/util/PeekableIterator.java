package de.dk.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface PeekableIterator<E> extends Iterator<E> {
   public E peek() throws NoSuchElementException;
}
