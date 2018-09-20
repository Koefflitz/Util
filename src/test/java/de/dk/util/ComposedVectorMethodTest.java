package de.dk.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComposedVectorMethodTest {
   private static final Vector PART0 = new Vector(1, 1);
   private static final Vector PART1 = new Vector(2, -4);
   private static final Vector PART2 = new Vector(-3, -1);

   private Vector part0;
   private Vector part1;
   private Vector part2;

   private ComposedVector c;

   @BeforeEach
   public void init() {
      this.part0 = PART0.clone();
      this.part1 = PART1.clone();
      this.part2 = PART2.clone();
      this.c = new ComposedVector(part0, part1, part2);
   }

   private void testEquality() {
      assertEquals(PART0, part0);
      assertEquals(PART1, part1);
      assertEquals(PART2, part2);
   }

   @Test
   public void testXY() {
      c.x(512);
      c.y(-1024);
      testEquality();
      assertEquals(new Vector(512, -1024), c);
   }

   @Test
   public void testSet() {
      c.set(-4, 4);
      testEquality();
      assertEquals(new Vector(-4, 4), c);
   }

   @Test
   public void testSetValue() {
      c.setValue(new Vector(0, 0));
      testEquality();
      assertEquals(new Vector(0, 0), c);
   }

   @Test
   public void testManipulate() {
      c.manipulate(xy -> 4f);
      testEquality();
      assertEquals(new Vector(4, 4), c);
   }

   @Test
   public void testManipulate2() {
      c.manipulate(x -> 1f, y -> -0f);
      testEquality();
      assertEquals(new Vector(1, 0), c);
   }

   @Test
   public void testManipulateXY() {
      c.manipulateX(x -> 2048.1024f);
      c.manipulateY(y -> 0f);
      testEquality();
      assertEquals(new Vector(2048.1024f, 0), c);
   }

   @Test
   public void testAdd() {
      c.add(new Vector(1, 1));
      testEquality();
      assertEquals(new Vector(1, -3), c);
   }

   @Test
   public void testSubtract() {
      c.subtract(new Vector(1, 1));
      testEquality();
      assertEquals(new Vector(-1, -5), c);
   }

   @Test
   public void testMultiply() {
      c.multiply(2);
      testEquality();
      assertEquals(new Vector(0, -8), c);
   }

   @Test
   public void testDivide() {
      c.divide(-4);
      testEquality();
      assertEquals(new Vector(0, 1), c);
   }

   @Test
   public void testSetMagnitude() {
      c.setMagnitude(9999.99f);
      testEquality();
      assertEquals(9999.99f, c.getMagnitude(), Vector.getEqualsDelta());
   }

   @Test
   public void testManipulateMagnitude() {
      c.manipulateMagnitude(m -> 92.3f);
      testEquality();
      assertEquals(92.3f, c.getMagnitude(), Vector.getEqualsDelta());
   }

   @Test
   public void testSetAngle() {
      c.setAngle(7.45f);
      testEquality();
      assertEquals(7.45f, c.getAngle(), Vector.getEqualsDelta());
   }

   @Test
   public void testManipulateAngle() {
      c.manipulateAngle(a -> 90f);
      testEquality();
      assertEquals(90f, c.getAngle(), Vector.getEqualsDelta());
   }

   @Test
   public void testReverse() {
      c.reverse();
      testEquality();
      assertEquals(new Vector(0, 4), c);
   }
}
