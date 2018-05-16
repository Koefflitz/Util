package de.dk.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;

public class SetList<E> implements List<E>, Set<E> {
   private List<E> list;

   public SetList(List<E> backList) {
      this.list = backList;
   }

   @Override
   public boolean add(E e) {
      if (list.contains(e))
         return false;

      return list.add(e);
   }

   @Override
   public void add(int index, E element) {
      if (!list.contains(element))
         list.add(index, element);
   }

   @Override
   public boolean addAll(Collection<? extends E> c) {
      boolean changed = false;
      for (E e : Objects.requireNonNull(c))
         changed |= add(e);

      return changed;
   }

   @Override
   public boolean addAll(int index, Collection<? extends E> c) throws NullPointerException, IndexOutOfBoundsException {
      Objects.requireNonNull(c);
      if (index < 0 || index > size())
         throw new IndexOutOfBoundsException("Size is " + size() + ", but index was " + index);

      int i = 0;
      boolean changed = false;
      for (E e : c) {
         if (!contains(e)) {
            list.add(i++ + index, e);
            changed = true;
         }
      }

      return changed;
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      return list.containsAll(c);
   }

   @Override
   public boolean remove(Object o) {
      return list.remove(o);
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      return list.removeAll(c);
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      return list.retainAll(c);
   }

   @Override
   public E get(int index) {
      return list.get(index);
   }

   @Override
   public E set(int index, E element) {
      if (!(contains(element) && list.indexOf(element) != index))
         return list.set(index, element);

      return element;
   }

   @Override
   public E remove(int index) {
      return list.remove(index);
   }

   @Override
   public int indexOf(Object o) {
      return list.indexOf(o);
   }

   @Override
   public int lastIndexOf(Object o) {
      return list.lastIndexOf(o);
   }

   @Override
   public void clear() {
      list.clear();
   }

   @Override
   public int size() {
      return list.size();
   }

   @Override
   public boolean isEmpty() {
      return list.isEmpty();
   }

   @Override
   public boolean contains(Object o) {
      return list.contains(o);
   }

   @Override
   public List<E> subList(int fromIndex, int toIndex) {
      return list.subList(fromIndex, toIndex);
   }

   @Override
   public Iterator<E> iterator() {
      return list.iterator();
   }

   @Override
   public ListIterator<E> listIterator() {
      return list.listIterator();
   }

   @Override
   public ListIterator<E> listIterator(int index) {
      return list.listIterator(index);
   }

   @Override
   public Spliterator<E> spliterator() {
      return list.spliterator();
   }

   @Override
   public Object[] toArray() {
      return list.toArray();
   }

   @Override
   public <T> T[] toArray(T[] a) {
      return list.toArray(a);
   }

   @Override
   public boolean equals(Object o) {
      return list.equals(o);
   }

   @Override
   public int hashCode() {
      return list.hashCode();
   }

}
