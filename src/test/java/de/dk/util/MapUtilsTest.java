package de.dk.util;

import static de.dk.util.MapUtils.toMap;
import static de.dk.util.MapUtils.transfer;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class MapUtilsTest {
   private static final String KEY0 = "Testschlüssel";
   private static final String KEY1 = "Pfirsichsuppe";
   private static final String KEY2 = "Berlin";
   private static final String KEY3 = "foo";

   private static final String VALUE0 = "Testwert";
   private static final String VALUE1 = "Buttermilch";
   private static final String VALUE2 = "Wustermark";
   private static final String VALUE3 = "bar";

   private static final Map<String, String> SOURCE_MAP;

   private Map<String, String> source;
   private Map<String, String> target;

   static {
      Map<String, String> map = new HashMap<>();
      map.put(KEY0, VALUE0);
      map.put(KEY1, VALUE1);
      map.put(KEY2, VALUE2);
      map.put(KEY3, VALUE3);
      SOURCE_MAP = Collections.unmodifiableMap(map);
   }

   public MapUtilsTest() {

   }

   @Before
   public void init() {
      this.source = new HashMap<>();
      this.target = new HashMap<>();

      for (Entry<String, String> e : SOURCE_MAP.entrySet())
         source.put(e.getKey(), e.getValue());
   }

   @Test
   public void testTransferToEmptyMap() {
      transfer(source, target);

      for (Entry<String, String> e : SOURCE_MAP.entrySet())
         assertEquals(e.getValue(), target.get(e.getKey()));
   }

   @Test
   public void testTransfer() {
      target.put("ülülü", "ldöfjkslhnb");
      target.put("sdakjjc", "sadsfcv");

      transfer(source, target);

      for (Entry<String, String> e : SOURCE_MAP.entrySet())
         assertEquals(e.getValue(), target.get(e.getKey()));
   }

   @Test
   public void testTransferOverwrite() {
      target.put(KEY1, VALUE0);
      target.put(KEY3, VALUE1);
      target.put("ülülü", "läakfa+üvn");

      transfer(source, target, true);

      for (Entry<String, String> e : SOURCE_MAP.entrySet())
         assertEquals(e.getValue(), target.get(e.getKey()));
   }

   @Test
   public void testToMap() {
      Collection<TestModel> values = new HashSet<>();
      for (String key : SOURCE_MAP.keySet())
         values.add(new TestModel(key));

      Map<String, ? extends TestModel> result = toMap(values, TestModel::getKey);
      assertEquals(values.size(), result.size());
      for (TestModel tm : values)
         assertEquals(tm, result.get(tm.key));
   }

   private static class TestModel {
      private String key;

      public TestModel(String key) {
         this.key = key;
      }

      public String getKey() {
         return key;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
         return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         TestModel other = (TestModel) obj;
         if (this.key == null) {
            if (other.key != null)
               return false;
         } else if (!this.key.equals(other.key))
            return false;
         return true;
      }
   }

}
