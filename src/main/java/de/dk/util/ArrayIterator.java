package de.dk.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayIterator<E> implements Iterator<E> {
   private final E[] array;
   private int index;

   protected ArrayIterator(E[] array) {
      this.array = Objects.requireNonNull(array);
   }

   public static <E> Iterator<E> of(E[] array) {
      if (array == null || array.length < 1)
         return new EmptyIterator<>();

      return new ArrayIterator<>(array);
   }

   @Override
   public boolean hasNext() {
      return index < array.length;
   }

   @Override
   public E next() {
      if (index >= array.length)
         throw new NoSuchElementException();

      return array[index++];
   }

   private static class EmptyIterator<E> implements Iterator<E> {
      @Override
      public boolean hasNext() {
         return false;
      }

      @Override
      public E next() {
         throw new NoSuchElementException();
      }
   }
}
