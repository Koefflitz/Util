package de.dk.util.log;

import java.util.Arrays;

public class DynamicByteArray {
   private static final int CAPACITY_EXTENSION = 64;

   private byte[] data;

   private int index = 0;

   public DynamicByteArray(int initialCapacity) {
      this.data = new byte[initialCapacity];
   }

   public DynamicByteArray() {
      this(CAPACITY_EXTENSION);
   }

   public void add(byte b) {
      if (index >= data.length)
         setCapacity(index + CAPACITY_EXTENSION);

      data[index++] = b;
   }

   public void clear() {
      index = 0;
      data = new byte[CAPACITY_EXTENSION];
   }

   private void setCapacity(int capacity) {
      this.data = Arrays.copyOf(data, capacity);
   }

   public int size() {
      return index;
   }

   public byte[] getData() {
      return data;
   }

   public boolean isEmpty() {
      return index == 0;
   }
}