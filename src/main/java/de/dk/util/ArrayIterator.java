package de.dk.util;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ArrayIterator<E> implements PeekableIterator<E> {
   private final E[] array;
   private int index;
   private int length;

   protected ArrayIterator(int offset, int length, E[] array) {
      this.array = Objects.requireNonNull(array);
      if (offset < 0)
         throw new IllegalArgumentException("offset can not be less than 0. Was " + offset);
      if (length < 0 || offset + length > array.length)
         throw new IllegalArgumentException("Length cannot be less than 0 or greater than the length of the array. Was " + length);

      this.index = offset;
      this.length = offset + length;
   }

   public static <E> PeekableIterator<E> of(E[] array) {
      if (array == null || array.length < 1)
         return new EmptyIterator<>();

      return new ArrayIterator<>(0, array.length, array);
   }

   public static <E> PeekableIterator<E> of(int offset, int length, E[] array) {
      if (array == null || array.length < 1)
         return new EmptyIterator<>();

      return new ArrayIterator<>(offset, length, array);
   }

   @Override
   public E peek() {
      if (index >= length)
         throw new NoSuchElementException();

      return array[index];
   }

   @Override
   public E next() {
      if (index >= length)
         throw new NoSuchElementException();

      return array[index++];
   }

   @Override
   public boolean hasNext() {
      return index < length;
   }

   private static class EmptyIterator<E> implements PeekableIterator<E> {
      @Override
      public E peek() {
         throw new NoSuchElementException();
      }
      @Override
      public E next() {
         throw new NoSuchElementException();
      }
      @Override
      public boolean hasNext() {
         return false;
      }
   }
}
