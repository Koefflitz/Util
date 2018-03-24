package de.dk.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CollectionUtilsTest {

   public CollectionUtilsTest() {

   }

   @Test
   public void testFindRecursively() {
      final Random rnd = new Random();
      final byte[] content = new byte[rnd.nextInt(128) + 4];
      final String element = "This is the relevant element!";
      final Set<String> expectation = new HashSet<>();
      expectation.add(element);
      final List<Set<String>> nested = new ArrayList<>();

      for (int i = 0; i < 8; i++) {
         Set<String> list = new HashSet<>();
         rnd.nextBytes(content);
         list.add(new String(content));
         nested.add(list);
      }

      nested.add(rnd.nextInt(nested.size()), expectation);

      assertSame(expectation, CollectionUtils.findRecursively(nested, element));
   }

   @Test
   public void testCombine() {
      Map<Integer, String> forenames = new HashMap<>();
      Map<Integer, String> surnames = new HashMap<>();

      // 1
      forenames.put(0, "Gerhardt");
      surnames.put(0, "Müller");

      // 2
      forenames.put(1, "Klaus");
      surnames.put(1, "Maier");

      // 3
      forenames.put(2, "Dieter");

      // 4
      forenames.put(3, "Gundula");

      // 5
      surnames.put(17, "Schmidt");

      Map<Integer, String> result = CollectionUtils.combine(forenames, surnames, String::concat);
      assertEquals(result.size(), 5);
      assertEquals(forenames.get(0) + surnames.get(0), result.get(0));
      assertEquals(forenames.get(1) + surnames.get(1), result.get(1));
      assertEquals(forenames.get(2), result.get(2));
      assertEquals(forenames.get(3), result.get(3));
      assertEquals(surnames.get(17), result.get(17));
   }
}
