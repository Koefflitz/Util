package de.dk.util;

import static de.dk.util.DMath.delimit;
import static de.dk.util.DMath.isInteger;
import static de.dk.util.DMath.maxabs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class DMathTest {
   @Rule
   public final ExpectedException expectedException = ExpectedException.none();

   public DMathTest() {

   }

   @Test
   public void testMaxAbs() {
      assertEquals(5, maxabs(1, 5));
      assertEquals(5, maxabs(-1, 5));
      assertEquals(-7, maxabs(-7, 5));
      assertEquals(-5, maxabs(-1, -5));
   }

   @Test
   public void testDelimit() {
      assertEquals(5, delimit(5, 0, 10));
      assertEquals(5, delimit(4, 5, 10));
      assertEquals(5, delimit(6, 0, 5));
      assertEquals(-5, delimit(-5, -10, 0));
      assertEquals(-5, delimit(-4, -10, -5));
      assertEquals(-5, delimit(-6, -5, 0));

      expectedException.expect(IllegalArgumentException.class);
      delimit(1, 3, 2);
   }

   @Test
   public void testIsInteger() {
      assertTrue(isInteger(5));
      assertTrue(isInteger(1024));
      assertTrue(isInteger(32.00));
      assertTrue(isInteger(-8));
      assertTrue(isInteger(-2048.00000));
      assertTrue(isInteger(0));
      assertFalse(isInteger(2.8f));
      assertFalse(isInteger(-2.8f));
      assertFalse(isInteger(512.256));
      assertFalse(isInteger(-128.4));
   }

}
