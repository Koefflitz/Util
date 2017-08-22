package de.dk.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MapUtils {

   private MapUtils() {

   }

   /**
    * Puts the given <code>values</code> into a map.
    * Assigns each value with the key provided by the <code>keyProvider</code>.
    *
    * @param values The values to be put into a map
    * @param keyProvider The provider of the keys to each value
    * @param <K> The type of the keys
    * @param <V> The type of the values
    *
    * @return a map containing all the <code>values</code> mapped by the key given by the <code>keyProvider</code>
    */
   public static <K, V> Map<K, ? extends V> toMap(Collection<V> values, Function<? super V, ? extends K> keyProvider) {
      return toMap(values, keyProvider, new HashMap<>());
   }

   /**
    * Puts the given <code>values</code> into the <code>map</code>.
    * Assigns each value with the key provided by the <code>keyProvider</code>.
    *
    * @param values The values to be put into the <code>map</code>
    * @param keyProvider The provider of the keys to each value
    * @param map the concrete map to put the values into
    * @param <M> The type of the map
    * @param <K> The type of the keys
    * @param <V> The type of the values
    *
    * @return the <code>map</code> containing all the <code>values</code>
    * mapped by the key given by the <code>keyProvider</code>
    */
   public static <M extends Map<K, ? super V>, K, V> M toMap(Collection<V> values,
                                                             Function<? super V, ? extends K> keyProvider,
                                                             M map) {
      for (V e : values)
         map.put(keyProvider.apply(e), e);

      return map;
   }

   /**
    * Transfers the entries from the <code>source</code> map into the <code>target</code> map.
    * If a key of the <code>source</code> map already exists in the <code>target</code> map it will be overwritten.
    * The source map is empty after this operation.
    *
    * @param source The map from which the entries should be transferred
    * @param target The map to which the entries should be transferred
    * @param <K> The type of the keys
    * @param <V> The type of the values
    *
    * @return the <code>target</code> map
    */
   public static <K, V> Map<K, V> transfer(Map<? extends K, ? extends V> source, Map<K, V> target) {
      return transfer(source, target, true);
   }

   /**
    * Transfers the entries from the <code>source</code> map into the <code>target</code> map.
    * If a key of the <code>source</code> map already exists in the <code>target</code> map it will be overwritten.
    *
    * @param source The map from which the entries should be transferred
    * @param target The map to which the entries should be transferred
    * @param overwrite Whether the entries of the <code>target</code> map should be overwritten by the source entries.
    * @param <K> The type of the keys
    * @param <V> The type of the values
    *
    * @return the <code>target</code> map
    */
   public static <K, V> Map<K, V> transfer(Map<? extends K, ? extends V> source,
                                           Map<K, V> target,
                                           boolean overwrite) {
      for (Entry<? extends K, ? extends V> entry : source.entrySet()) {
         if (overwrite || !target.containsKey(entry.getKey()))
            target.put(entry.getKey(), entry.getValue());
      }

      source.clear();
      return target;
   }

   public static <K, V> Map<K, V> lazyHashMap(Function<K, V> valueSupplier) {
      return toLazyMap(HashMap::new, valueSupplier);
   }

   public static <K, V> Map<K, V> toLazyMap(Map<K, V> map, Function<K, V> valueSupplier) {
      return new LazyMap<>(() -> map, valueSupplier);
   }

   public static <K, V> Map<K, V> toLazyMap(Supplier<Map<K, V>> mapSupplier, Function<K, V> valueSupplier) {
      return new LazyMap<>(mapSupplier, valueSupplier);
   }

   private static class LazyMap<K, V> implements Map<K, V> {
      private final Supplier<Map<K, V>> targetSupplier;
      private final Function<? super K, ? extends V> valueSupplier;

      private Map<K, V> target;

      public LazyMap(Supplier<Map<K, V>> targetSupplier, Function<? super K, ? extends V> valueSupplier) {
         this.targetSupplier = Objects.requireNonNull(targetSupplier);
         this.valueSupplier = valueSupplier;
      }

      private Map<K, V> target() {
         return target == null ? (target = targetSupplier.get()) : target;
      }

      @SuppressWarnings("unchecked")
      @Override
      public V get(Object key) {
         V value = target().get(key);
         if (value != null)
            return value;

         K castedKey;
         try {
            castedKey = (K) key;
         } catch (ClassCastException e) {
            return null;
         }

         value = valueSupplier.apply(castedKey);
         target().put(castedKey, value);
         return value;
      }

      @Override
      public int size() {
         return target().size();
      }

      @Override
      public boolean isEmpty() {
         return target().isEmpty();
      }

      @Override
      public boolean containsKey(Object key) {
         return target().containsKey(key);
      }

      @Override
      public boolean containsValue(Object value) {
         return target().containsValue(value);
      }

      @Override
      public V put(K key, V value) {
         return target().put(key, value);
      }

      @Override
      public V remove(Object key) {
         return target().remove(key);
      }

      @Override
      public void putAll(Map<? extends K, ? extends V> m) {
         target().putAll(m);
      }

      @Override
      public void clear() {
         target().clear();
      }

      @Override
      public Set<K> keySet() {
         return target().keySet();
      }

      @Override
      public Collection<V> values() {
         return target().values();
      }

      @Override
      public Set<java.util.Map.Entry<K, V>> entrySet() {
         return target().entrySet();
      }

      @Override
      public boolean equals(Object o) {
         return target().equals(o);
      }

      @Override
      public int hashCode() {
         return target().hashCode();
      }

      @Override
      public V getOrDefault(Object key, V defaultValue) {
         return target().getOrDefault(key, defaultValue);
      }

      @Override
      public void forEach(BiConsumer<? super K, ? super V> action) {
         target().forEach(action);
      }

      @Override
      public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
         target().replaceAll(function);
      }

      @Override
      public V putIfAbsent(K key, V value) {
         return target().putIfAbsent(key, value);
      }

      @Override
      public boolean remove(Object key, Object value) {
         return target().remove(key, value);
      }

      @Override
      public boolean replace(K key, V oldValue, V newValue) {
         return target().replace(key, oldValue, newValue);
      }

      @Override
      public V replace(K key, V value) {
         return target().replace(key, value);
      }

      @Override
      public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
         return target().computeIfAbsent(key, mappingFunction);
      }

      @Override
      public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
         return target().computeIfPresent(key, remappingFunction);
      }

      @Override
      public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
         return target().compute(key, remappingFunction);
      }

      @Override
      public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         return target().merge(key, value, remappingFunction);
      }

   }
}