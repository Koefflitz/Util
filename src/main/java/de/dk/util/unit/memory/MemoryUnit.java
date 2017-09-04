package de.dk.util.unit.memory;

import de.dk.util.DMath;

/**
 * Represents the common memory units from bit to terra byte.
 *
 * @author David Koettlitz
 * <br>Erstellt am 15.06.2017
 */
public enum MemoryUnit {
   BIT("bit", 1d / 8d),
   BYTE("byte", 1),
   KILO_BYTE("kb", 1000),
   MEGA_BYTE("MB", 1000000),
   GIGA_BYTE("GB", 1000000000),
   TERRA_BYTE("TB", 1000000000000d);

   private final String name;
   private final double factor;

   private MemoryUnit(String name, double factor) {
      this.name = name;
      this.factor = factor;
   }

   /**
    * Calculates the best MemoryUnit to measure the given number of bytes with.
    * This algorithm takes the MemoryUnit, where the value is the lowest possible integer value.
    *
    * @param bytes The number of bytes to find a good MemoryUnit for.
    *
    * @return The best MemoryUnit to measure the given <code>bytes</code> with.
    *
    * @throws IllegalArgumentException If a value, that cannot be expressed in MemoryUnits is passed,
    * e.g. 0.00001.
    */
   public static MemoryUnit getBestUnitFor(double bytes) throws IllegalArgumentException {
      if (bytes == 0)
         return BYTE;

      MemoryUnit[] units = values();
      for (int i = units.length - 1; i >= 0; i--) {
         if (bytes >= units[i].factor)
            return units[i];
      }
      throw new IllegalArgumentException("No MemoryUnit available for " + bytes + "bytes");
   }

   /**
    * Checks if the given number of bytes can be expressed in MemoryUnits.
    *
    * @param bytes The number of bytes to be expressed in MemoryUnits
    *
    * @return <code>true</code> if the given number of bytes can be expressed in MemoryUnits.
    * <code>false</code> otherwise, e.g. -1 or 0.000001
    */
   public static boolean isValidMemoryUnitValue(double bytes) {
      if (bytes < 0)
         return false;

      return DMath.isInteger(DMath.getAfterPoint(bytes) * 8);
   }

   /**
    * Converts the value this memory unit into the given <code>targetUnit</code>.
    *
    * @param value The value to be converted
    * @param targetUnit The memory unit to convert the <code>value</code> to from this unit
    * @return The value of the given <code>targetUnit</code>
    * that is equivalent to the given <code>value</code> in this unit.
    */
   public double convertTo(double value, MemoryUnit targetUnit) {
      if (targetUnit == this)
         return value;

      return (double) (value * factor / targetUnit.factor);
   }

   @Override
   public String toString() {
      return name;
   }
}