package de.dk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IO {

   private IO() {

   }

   public static ByteArrayOutputStream read(InputStream src) throws IOException {
      ByteArrayOutputStream target = new ByteArrayOutputStream(src.available());
      write(src, target);
      return target;
   }

   public static byte[] readData(InputStream src) throws IOException {
      ByteArrayOutputStream target = new ByteArrayOutputStream(src.available()) {
         @Override
         public byte[] toByteArray() {
            return super.buf;
         }
      };
      write(src, target);
      return target.toByteArray();
   }

   public static void write(InputStream src, OutputStream target) throws IOException {
      byte[] buf = new byte[src.available()];
      for (int len = src.read(buf); len != -1; len = src.read(buf))
         target.write(buf, 0, len);
   }

}
