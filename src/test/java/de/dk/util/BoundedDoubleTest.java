package de.dk.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BoundedDoubleTest {

   public BoundedDoubleTest() {

   }

   @Test
   public void maxCannotBeLowerThanMin() {
      assertThrows(IllegalArgumentException.class, () -> new BoundedDouble(2, 1));
      BoundedDouble value = new BoundedDouble(1, 2);
      assertThrows(IllegalArgumentException.class, () -> value.setMax(0));
      assertThrows(IllegalArgumentException.class, () -> value.decreaseMax(2));
      assertThrows(IllegalArgumentException.class, () -> value.increaseMax(-2));
      assertThrows(IllegalArgumentException.class, () -> value.manipulateMax(max -> max - 2));
   }

   @Test
   public void minCannotBeGreaterThanMax() {
      BoundedDouble value = new BoundedDouble(0, 1);
      assertThrows(IllegalArgumentException.class, () -> value.setMin(2));
      assertThrows(IllegalArgumentException.class, () -> value.increaseMin(2));
      assertThrows(IllegalArgumentException.class, () -> value.decreaseMin(-2));
      assertThrows(IllegalArgumentException.class, () -> value.manipulateMin(min -> min + 2));
   }

   @Test
   public void valueCannotBeLessThanMin() {
      BoundedDouble value = new BoundedDouble(0, 1, -1);
      assertEquals(0, value.getAsDouble());
      value.set(-1);
      assertEquals(0, value.getAsDouble());
      value.sub(1);
      assertEquals(0, value.getAsDouble());
      value.add(-1);
      assertEquals(0, value.getAsDouble());
   }

   @Test
   public void valueCannotBeGreaterThanMax() {
      BoundedDouble value = new BoundedDouble(0, 1, 2);
      assertEquals(1, value.getAsDouble());
      value.set(2);
      assertEquals(1, value.getAsDouble());
      value.add(1);
      assertEquals(1, value.getAsDouble());
      value.sub(-1);
      assertEquals(1, value.getAsDouble());
   }

   @Test
   public void valuesAreAsSetIfInsideBounds() {
      BoundedDouble value = new BoundedDouble(0, 10, 1);
      assertEquals(0, value.getMin());
      assertEquals(10, value.getMax());
      assertEquals(1, value.getAsDouble());

      value.set(5);
      assertEquals(5, value.getAsDouble());
      value.add(2);
      assertEquals(7, value.getAsDouble());
      value.sub(-2);
      assertEquals(9, value.getAsDouble());
   }

   @Test
   public void boundsAreChangable() {
      BoundedDouble value = new BoundedDouble(0, 10, 1);
      assertEquals(0, value.getMin());
      assertEquals(10, value.getMax());
      assertEquals(1, value.getAsDouble());

      value.setMax(12);
      assertEquals(12, value.getMax());
      value.set(12);
      assertEquals(12, value.getAsDouble());
   }

}
