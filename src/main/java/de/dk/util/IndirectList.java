package de.dk.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author David Koettlitz
 * <br>Erstellt am 20.09.2016
 */
public class IndirectList<E> implements List<E> {
   private final LinkedList<E> newElements = new LinkedList<>();
   private List<E> impl;

   public IndirectList(List<E> impl) {
      this.impl = impl;
   }

   public IndirectList() {
      this(new LinkedList<>());
   }

   public void update() {
      impl.addAll(newElements);
      newElements.clear();
   }

   @Override
   public boolean add(E e) {
      return newElements.add(e);
   }

   @Override
   public void add(int index, E element) {
      newElements.add(index, element);
   }

   @Override
   public boolean addAll(Collection<? extends E> c) {
      return newElements.addAll(c);
   }

   @Override
   public boolean contains(Object o) {
      return impl.contains(o) || newElements.contains(o);
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      return c.stream().anyMatch(this::contains);
   }

   @Override
   public boolean remove(Object o) {
      return impl.remove(o) | newElements.remove(o);
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      return impl.removeAll(c) | newElements.removeAll(c);
   }

   @Override
   public boolean removeIf(Predicate<? super E> filter) {
      return impl.removeIf(filter) | newElements.removeIf(filter);
   }

   @Override
   public void replaceAll(UnaryOperator<E> operator) {
      newElements.replaceAll(operator);
      impl.replaceAll(operator);
   }

   @Override
   public void clear() {
      impl.clear();
      newElements.clear();
   }

   @Override
   public int size() {
      return impl.size() + newElements.size();
   }

   @Override
   public boolean isEmpty() {
      return impl.isEmpty() && newElements.isEmpty();
   }

   @Override
   public Iterator<E> iterator() {
      return impl.iterator();
   }

   @Override
   public Object[] toArray() {
      return impl.toArray();
   }

   @Override
   public <T> T[] toArray(T[] a) {
      return impl.toArray(a);
   }

   @Override
   public boolean addAll(int index, Collection<? extends E> c) {
      return impl.addAll(index, c);
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      return impl.retainAll(c);
   }

   @Override
   public boolean equals(Object o) {
      return impl.equals(o);
   }

   @Override
   public int hashCode() {
      return impl.hashCode();
   }

   @Override
   public E get(int index) {
      return impl.get(index);
   }

   @Override
   public E set(int index, E element) {
      return impl.set(index, element);
   }

   @Override
   public E remove(int index) {
      return impl.remove(index);
   }

   @Override
   public int indexOf(Object o) {
      return impl.indexOf(o);
   }

   @Override
   public int lastIndexOf(Object o) {
      return impl.lastIndexOf(o);
   }

   @Override
   public ListIterator<E> listIterator() {
      return impl.listIterator();
   }

   @Override
   public ListIterator<E> listIterator(int index) {
      return impl.listIterator(index);
   }

   @Override
   public List<E> subList(int fromIndex, int toIndex) {
      return impl.subList(fromIndex, toIndex);
   }

}