package de.dk.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ComposedVectorTest {

   @Test
   public void singlePartVectorEqualsThatPart() {
      Vector part = new Vector(2, -4);
      ComposedVector c = new ComposedVector(part);
      assertEquals(2, c.x());
      assertEquals(-4, c.y());
      assertTrue(c.equals(part), "The composed vector should equal its only part.");
      assertTrue(part.equals(c), "Its only part should equal a composed vector.");
   }

   @Test
   public void multiplePartVectorEqualsAdditionOfParts() {
      Vector part0 = new Vector(1, 1);
      Vector part1 = new Vector(2, -4);
      Vector part2 = new Vector(-3, -1);

      ComposedVector c = new ComposedVector(part0, part1, part2);
      assertEquals(0, c.x());
      assertEquals(-4, c.y());
      assertTrue(c.equals(new Vector(0, -4)), "A composed vector should equal the addition of its parts.");
      assertTrue(new Vector(0, -4).equals(c), "The addition of its parts should equal a composed vector.");
   }

   @Test
   public void changesOfPartAffectsSinglePartedComposedVector() {
      Vector part = new Vector(0, 0);
      ComposedVector c = new ComposedVector(part);
      assertEquals(0, c.x());
      assertEquals(0, c.y());

      part.set(3, -4);
      assertEquals(3, c.x());
      assertEquals(-4, c.y());
   }

   @Test
   public void changesOfPartsAffectsMultipartComposedVector() {
      Vector part0 = new Vector(1, 1);
      Vector part1 = new Vector(2, -4);
      Vector part2 = new Vector(-3, -1);
      ComposedVector c = new ComposedVector(part0, part1, part2);

      part0.x(2);
      assertEquals(1, c.x());
      part1.y(17.76f);
      assertEquals(17.76f, c.y());
      part2.set(-2, -12);
      assertEquals(2, c.x());
      assertEquals(6.76f, c.y());
   }
}
