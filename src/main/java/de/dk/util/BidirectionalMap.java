package de.dk.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class BidirectionalMap<K, V> extends HashMap<K, V> {
   private static final long serialVersionUID = 7711766342642236547L;

   private Map<V, K> reverse = new HashMap<>();

   public BidirectionalMap() {

   }

   @Override
   public V put(K key, V value) {
      reverse.put(value, key);
      return super.put(key, value);
   }

   @Override
   public void putAll(Map<? extends K, ? extends V> m) {
      m.forEach((k, v) -> reverse.put(v, k));
      super.putAll(m);
   }

   public K getByValue(V value) {
      return reverse.get(value);
   }

}
