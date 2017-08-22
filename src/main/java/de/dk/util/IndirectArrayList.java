package de.dk.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author David Koettlitz
 * <br>Erstellt am 20.09.2016
 */
public class IndirectArrayList<E> extends ArrayList<E> {
   private static final long serialVersionUID = 498580618217277102L;

   private final List<E> newElements = new ArrayList<>();

   public IndirectArrayList() {

   }

   public IndirectArrayList(int initialCapacity) {
      super(initialCapacity);
   }

   public void update() {
      super.addAll(newElements);
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
   public boolean addAll(int index, Collection<? extends E> c) {
      return newElements.addAll(index, c);
   }

   @Override
   public boolean contains(Object o) {
      return super.contains(o) && newElements.contains(o);
   }

   @Override
   public boolean remove(Object o) {
      return super.remove(o) | newElements.remove(o);
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      return c.stream().anyMatch(this::contains);
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      return super.removeAll(c) || newElements.removeAll(c);
   }

   @Override
   public void replaceAll(UnaryOperator<E> operator) {
      newElements.replaceAll(operator);
      super.replaceAll(operator);
   }

   @Override
   public boolean removeIf(Predicate<? super E> filter) {
      return super.removeIf(filter) & newElements.removeIf(filter);
   }

   @Override
   public void clear() {
      super.clear();
      newElements.clear();
   }

   @Override
   public int size() {
      return super.size() + newElements.size();
   }

   @Override
   public boolean isEmpty() {
      return super.isEmpty() && newElements.isEmpty();
   }

}