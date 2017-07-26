package de.dk.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 10.11.2016
 */
public final class CollectionUtils {

   private CollectionUtils() {}

   /**
    * Finds a the collection, that contains a specific element
    * recursively in any nested collection.
    *
    * @param <E> The type of the element, the method is looking up
    * (can be inferred automatically)
    * @param <R> The type of the collection, that contains the specified element.
    * (Cannot be inferred automatically)
    * Should be specified when calling this method,
    * if a more specific type than collection is desired.
    *
    * @param collection The nested collection,
    * that contains the desired collection (doesn't have to be nested).
    * @param element The element the desired collection contains.
    *
    * @return The collection, that contains the specific element.
    *
    * @throws ClassCastException If the collection, that contains the element doesn't match
    * the specified collection type <code>R</code>.
    */
   @SuppressWarnings("unchecked")
   public static <E, R extends Collection<? extends E>> R findRecursively(Collection<?> collection,
                                                                          E element) throws ClassCastException {
      if (collection.isEmpty())
         return null;

      boolean nested = collection.stream()
                                 .allMatch(e -> ReflectionUtils.isSubclass(e.getClass(),
                                                                           Collection.class));

      for (Object c : collection) {
         R result;

         if (nested)
            result = findRecursively((Collection<?>) c, element);
         else
            result = c.equals(element) ? (R) collection : null;

         if (result != null)
            return result;
      }
      return null;
   }

   /**
    * Converts an {@link Enumeration} to an {@link Iterable}.
    * @param enumeration the enumeration to be converted
    * @return the iterable equivalent to the enumeration
    */
   public static <T> Iterable<T> toIterable(Enumeration<T> enumeration) {
      return () -> new EnumerationIterator<>(enumeration);
   }

   /**
    * Combines 2 maps by just adding the values of the same key together.
    *
    * @param a A map
    * @param b Another map
    *
    * @return a map that contains all the key of map a and b and the combined values.
    */
   public static <K, V> Map<K, V> combine(Map<? extends K, ? extends V> a,
                                          Map<? extends K, ? extends V> b,
                                          BinaryOperator<V> combiner) {
      Map<K, V> result = new HashMap<>();

      for (Entry<? extends K, ? extends V> entry : a.entrySet()) {
         if (b.containsKey(entry.getKey()))
            result.put(entry.getKey(), combiner.apply(entry.getValue(), b.get(entry.getKey())));
         else
            result.put(entry.getKey(), entry.getValue());
      }

      b.entrySet()
       .stream()
       .filter(e -> !result.containsKey(e.getKey()))
       .forEach(e -> result.put(e.getKey(), e.getValue()));

      return result;
   }

   /**
    * Combines 2 maps by just adding the values of the same key together.
    *
    * @param a A map
    * @param b Another map
    *
    * @return a map that contains all the key of map a and b and the combined values.
    */
   public static <K> Map<K, Integer> combine(Map<? extends K, Integer> a, Map<? extends K, Integer> b) {
      return combine(a, b, Integer::sum);
   }

   /**
    * Joins 2 collections into one list.
    *
    * @param a Some collection
    * @param b Some other collection
    * @return A List that contains all the elements of both collections.
    */
   public static <T> List<T> join(Collection<? extends T> a, Collection<? extends T> b) {
      return Stream.concat(a.stream(), b.stream())
                   .collect(Collectors.toList());
   }

   @SuppressWarnings("rawtypes")
   public static Class<? extends Collection> getDefaultImplementationOf(Class<? extends Collection> type)
   throws UnsupportedCollectionTypeException {

      if (type.equals(Collection.class))
         return LinkedList.class;
      else if (ReflectionUtils.isSubclass(type, List.class) && ReflectionUtils.isSubclass(LinkedList.class, type))
         return LinkedList.class;
      else if (ReflectionUtils.isSubclass(type, Set.class) && ReflectionUtils.isSubclass(HashSet.class, type))
         return HashSet.class;

      throw new UnsupportedCollectionTypeException(type);
   }

   public static class EnumerationIterator<E> implements Iterator<E> {
      private Enumeration<E> enumeration;

      public EnumerationIterator(Enumeration<E> enumeration) {
         this.enumeration = enumeration;
      }

      @Override
      public boolean hasNext() {
         return enumeration.hasMoreElements();
      }

      @Override
      public E next() {
         return enumeration.nextElement();
      }

   }

   public static class UnsupportedCollectionTypeException extends Exception {
      private static final long serialVersionUID = -957121365276576493L;

      @SuppressWarnings("rawtypes")
      public UnsupportedCollectionTypeException(Class<? extends Collection> type) {
         super("Type " + type.getName() + " was not supported.");
      }

      public UnsupportedCollectionTypeException(String msg) {
         super(msg);
      }

      public UnsupportedCollectionTypeException(String msg, Exception cause) {
         super(msg, cause);
      }

      public UnsupportedCollectionTypeException(Exception cause) {
         super(cause);
      }
   }

}