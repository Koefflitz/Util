package de.dk.util.unit;

import static de.dk.util.unit.memory.MemoryUnit.BIT;
import static de.dk.util.unit.memory.MemoryUnit.BYTE;
import static de.dk.util.unit.memory.MemoryUnit.GIGA_BYTE;
import static de.dk.util.unit.memory.MemoryUnit.KILO_BYTE;
import static de.dk.util.unit.memory.MemoryUnit.MEGA_BYTE;
import static de.dk.util.unit.memory.MemoryUnit.TERRA_BYTE;
import static de.dk.util.unit.memory.MemoryUnit.getBestUnitFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.dk.util.unit.memory.MemoryValue;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class MemoryUnitTest {
   private static final double DELTA = 0.000000000000006;

   public MemoryUnitTest() {

   }

   @Test
   public void testSetUnit() {
      double bit = 512000000l * 8l;
      double byt = 512000000;
      double kb = 512000;
      double mb = 512;
      double gb = 0.512;
      double tb = 0.000512;
      MemoryValue value = new MemoryValue(mb, MEGA_BYTE);
      value.setUnit(BIT);
      assertEquals(bit, value.getValue(), DELTA);
      value.setUnit(BYTE);
      assertEquals(byt, value.getValue(), DELTA);
      value.setUnit(KILO_BYTE);
      assertEquals(kb, value.getValue(), DELTA);
      value.setUnit(MEGA_BYTE);
      assertEquals(mb, value.getValue(), DELTA);
      value.setUnit(GIGA_BYTE);
      assertEquals(gb, value.getValue(), DELTA);
      value.setUnit(TERRA_BYTE);
      assertEquals(tb, value.getValue(), DELTA);
   }

   @Test
   public void testAdd() {
      MemoryValue v0 = new MemoryValue(512, KILO_BYTE);
      MemoryValue v1 = new MemoryValue(8, MEGA_BYTE);

      MemoryValue result = MemoryValue.addValues(v0, v1);
      assertEquals(8512, (int) result.getValue());

      result = MemoryValue.addValues(GIGA_BYTE, v0, v1);
      assertEquals(0.008512, result.getValue(), DELTA);

      v0.add(v1);
      assertEquals(8512, (int) v0.getValue());
   }

   @Test
   public void testSubtract() {
      MemoryValue v0 = new MemoryValue(8, MEGA_BYTE);
      MemoryValue v1 = new MemoryValue(512, KILO_BYTE);

      MemoryValue result = MemoryValue.subtractValues(v0, v1);
      assertEquals(7.488, result.getValue(), DELTA);

      result = MemoryValue.subtractValues(GIGA_BYTE, v0, v1);
      assertEquals(0.007488, result.getValue(), DELTA);

      v0.subtract(v1);
      assertEquals(7.488, v0.getValue(), DELTA);
   }

   @Test
   public void testBestUnit() {
      assertEquals(BIT, getBestUnitFor(5d / 8d));
      assertEquals(BYTE, getBestUnitFor(128));
      assertEquals(KILO_BYTE, getBestUnitFor(1024));
      assertEquals(MEGA_BYTE, getBestUnitFor(1000000));
      assertEquals(GIGA_BYTE, getBestUnitFor(999999999999l));
      assertEquals(TERRA_BYTE, getBestUnitFor(Long.MAX_VALUE));
   }

}
