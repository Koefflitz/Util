package de.dk.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class VectorTest {
   private static final float FLOAT_DELTA = 0.001f;

   public VectorTest() {

   }

   @Test
   public void testFactoryMethodOfMagnitudeAndAngle() {
      // Around the clock in steps of 90°
      assertEquals(new Vector(1, 0), Vector.of(1, 0));
      assertEquals(new Vector(0, 2), Vector.of(2, 90));
      assertEquals(new Vector(-17, 0), Vector.of(17, 180));
      assertEquals(new Vector(0, -1024), Vector.of(1024, 270));
   }

   @Test
   public void testFactoryMethodOfXAndAngle() {
      Vector v = Vector.ofX(1, 0);
      assertEquals(new Vector(1, 0), v);
   }

   @Test
   public void testFactoryMethodOfYAndAngle() {
      Vector v = Vector.ofY(1, 90);
      assertEquals(new Vector(0, 1), v);
   }

   @Test
   public void impossibleVectorThrowsException() {
      assertThrows(ArithmeticException.class, () -> Vector.ofX(1, 90));
      assertThrows(ArithmeticException.class, () -> Vector.ofY(1, 180));
   }

   @Test
   public void testEquals() {
      assertEquals(new Vector(8,-1024), new Vector(8, -1024));
      Vector a = new Vector(1, 1);
      a.add(new Vector(10, -10));
      assertEquals(new Vector(11, -9), a);
   }

   @Test
   public void testAdd() {
      Vector v = new Vector(1, 1);
      v.add(new Vector(1, 1));
      assertEquals(new Vector(2, 2), v);

      v = new Vector(-1, 1);
      v.add(new Vector(1, 1));
      assertEquals(new Vector(0, 2), v);

      v = new Vector(-2, -3);
      v.add(new Vector(1, 1));
      assertEquals(new Vector(-1, -2), v);

      v = new Vector(1, 1);
      v.add(new Vector(-1, -1024));
      assertEquals(new Vector(0, -1023), v);
   }

   @Test
   public void testSubtract() {
      Vector v = new Vector(1, 1);
      v.subtract(new Vector(1, 4));
      assertEquals(new Vector(0, -3), v);

      v = new Vector(-1, 1);
      v.subtract(new Vector(1, 1));
      assertEquals(new Vector(-2, 0), v);

      v = new Vector(-1, -1000);
      v.subtract(new Vector(1, 1));
      assertEquals(new Vector(-2, -1001), v);

      v = new Vector(-1, -1000);
      v.subtract(new Vector(-2, 24));
      assertEquals(new Vector(1, -1024), v);
   }

   @Test
   public void testMultiply() {
      Vector v = new Vector(2, 2);
      v.multiply(4);
      assertEquals(new Vector(8, 8), v);

      v = new Vector(-2, 2);
      v.multiply(4);
      assertEquals(new Vector(-8, 8), v);

      v = new Vector(2, -3);
      v.multiply(-3);
      assertEquals(new Vector(-6, 9), v);

      v = new Vector(-2, 4);
      v.multiply(-4);
      assertEquals(new Vector(8, -16), v);
   }

   @Test
   public void testDivide() {
      Vector v = new Vector(8, 8);
      v.divide(4);
      assertEquals(new Vector(2, 2), v);

      v = new Vector(-8, 8);
      v.divide(4);
      assertEquals(new Vector(-2, 2), v);

      v = new Vector(-6, 9);
      v.divide(-3);
      assertEquals(new Vector(2, -3), v);

      v = new Vector(8, -16);
      v.divide(-4);
      assertEquals(new Vector(-2, 4), v);
   }

   @Test
   public void divisionByZeroIsNoop() {
      Vector v = new Vector(7, -1024);
      v.divide(0);
      assertEquals(new Vector(7, -1024), v);
   }

   @Test
   public void angleIsSetCorrectly() {
      Vector v = new Vector(0, 1);
      v.setAngle(0);
      assertEquals(new Vector(1, 0), v);

      v = new Vector(4, 0);
      v.setAngle(270);
      assertEquals(new Vector(0, -4), v);

      v = new Vector(2, 2);
      v.setAngle(225);
      assertEquals(new Vector(-2, -2), v);
   }

   @Test
   public void angleGetterIsCorrect() {
      Vector v = new Vector(1, 0);
      assertEquals(0, v.getAngle(), FLOAT_DELTA);

      v = new Vector(1, 1);
      assertEquals(45, v.getAngle(), FLOAT_DELTA);

      v = new Vector(0, 1);
      assertEquals(90, v.getAngle(), FLOAT_DELTA);

      v = new Vector(-1, 1);
      assertEquals(135, v.getAngle(), FLOAT_DELTA);

      v = new Vector(-1, 0);
      assertEquals(180, v.getAngle(), FLOAT_DELTA);

      v = new Vector(-1, -1);
      assertEquals(225, v.getAngle(), FLOAT_DELTA);

      v = new Vector(0, -1);
      assertEquals(270, v.getAngle(), FLOAT_DELTA);

      v = new Vector(1, -1);
      assertEquals(315, v.getAngle(), FLOAT_DELTA);
   }

   @Test
   public void angleGreaterThan360IsHandledCorrectly() {
      Vector v = Vector.of(1, 400);
      assertEquals(40, v.getAngle(), FLOAT_DELTA);

      v.setAngle(1200);
      assertEquals(120, v.getAngle(), FLOAT_DELTA);

      v.setAngle(360);
      assertEquals(0, v.getAngle(), FLOAT_DELTA);
   }

   @Test
   public void angleLessThan0IsHandledCorrectly() {
      Vector v = Vector.of(3, -100);
      assertEquals(260, v.getAngle(), FLOAT_DELTA);

      v.setAngle(-780);
      assertEquals(300, v.getAngle(), FLOAT_DELTA);
   }

   public void negativeMagnitudeReverses() {
      Vector v = new Vector(1, 0);
      v.setMagnitude(-1);
      assertEquals(new Vector(-1, 0), v);
   }

}
