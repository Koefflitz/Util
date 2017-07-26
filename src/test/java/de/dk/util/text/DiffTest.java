package de.dk.util.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DiffTest {
   private Diff diff;

   private String same;
   private String diffA;
   private String diffB;

   public DiffTest() {

   }

   public static <T> void assertValuesEqual(List<? extends T> expected, List<? extends T> actual) {
      assertEquals(expected.size(), actual.size());
      for (int i = 0; i < actual.size(); i++)
         assertEquals(expected.get(i), actual.get(i));
   }

   @Before
   public void init() {
      this.same = "Immer das Gleiche hier!";

      this.diffA = "Was Anderes!";
      this.diffB = "Nochwas Unterschiedliches";
      this.diff = new Diff();
   }

   @Test
   public void testSingleInsertBefore() {
      String a = same + '\n' + same;
      String b = diffB + same + '\n' + same;
      Substring expected = new Substring(diffB, 0);

      DiffModel result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertTrue(result.getDiffA().isEmpty());
      assertEquals(1, result.getDiffB().size());
      assertEquals(expected, result.getDiffB().get(0));

      a = diffA + same + '\n' + same;
      b = same + '\n' + same;
      expected = new Substring(diffA, 0);

      result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertEquals(1, result.getDiffA().size());
      assertTrue(result.getDiffB().isEmpty());
      assertEquals(expected, result.getDiffA().get(0));
   }

   @Test
   public void testSingleInsertBetween() {
      String a = same + '\n' + same;
      String b = same + '\n' + diffB + '\n' + same;
      Substring expected = new Substring(diffB + '\n', same.length() + 1);

      DiffModel result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertTrue(result.getDiffA().isEmpty());
      assertEquals(1, result.getDiffB().size());
      assertEquals(expected, result.getDiffB().get(0));

      a = same + '\n' + diffA + '\n' + same;
      b = same + '\n' + same;
      expected = new Substring(diffA + '\n', same.length() + 1);

      result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertEquals(1, result.getDiffA().size());
      assertTrue(result.getDiffB().isEmpty());
      assertEquals(expected, result.getDiffA().get(0));
   }

   @Test
   public void testSingleInsertAfter() {
      String a = same + '\n' + same;
      String b = same + '\n' + same + diffB;
      Substring expected = new Substring(diffB, a.length());

      DiffModel result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertTrue(result.getDiffA().isEmpty());
      assertEquals(1, result.getDiffB().size());
      assertEquals(expected, result.getDiffB().get(0));

      a = same + '\n' + same + diffA;
      b = same + '\n' + same;
      expected = new Substring(diffA, b.length());

      result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertEquals(1, result.getDiffA().size());
      assertTrue(result.getDiffB().isEmpty());
      assertEquals(expected, result.getDiffA().get(0));
   }

   @Test
   public void testInsert() {
      String a = same + '\n' + same;
      String b = diffB + same + '\n' + diffB + '\n' + same + diffB;
      List<Substring> expected = Arrays.asList(new Substring(diffB, 0),
                                               new Substring(diffB + '\n', (same + '\n').length()),
                                               new Substring(diffB, a.length()));

      DiffModel result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertTrue(result.getDiffA().isEmpty());
      assertEquals(3, result.getDiffB().size());
      assertValuesEqual(expected, result.getDiffB());

      a = diffA + same + '\n' + diffA + '\n' + same + diffA;
      b = same + '\n' + same;
      expected = Arrays.asList(new Substring(diffA, 0),
                               new Substring(diffA + '\n', (same + '\n').length()),
                               new Substring(diffA, b.length()));

      result = diff.diff(a, b);

      assertEquals(same + '\n' + same, result.getCommon());
      assertTrue(result.getDiffB().isEmpty());
      assertValuesEqual(expected, result.getDiffA());
   }

   @Test
   public void testDiffBothSides() {
      String a = diffA + '\n' + same + diffA + same + '\n' + diffA + same;
      String b = diffB + '\n' + same + diffB + same + '\n' + diffB;
      List<Substring> expectedA = Arrays.asList(new Substring(diffA, 0),
                                                new Substring(diffA, ('\n' + same).length()),
                                                new Substring(diffA + same, ('\n' + same + same + '\n').length()));

      List<Substring> expectedB = Arrays.asList(new Substring(diffB, 0),
                                                new Substring(diffB, ('\n' + same).length()),
                                                new Substring(diffB, ('\n' + same + same + '\n').length()));

      DiffModel result = diff.diff(a, b);

      assertEquals('\n' + same + same + '\n', result.getCommon());
      assertEquals(3, result.getDiffA().size());
      assertEquals(3, result.getDiffB().size());
      assertValuesEqual(expectedA, result.getDiffA());
      assertValuesEqual(expectedB, result.getDiffB());
   }

}