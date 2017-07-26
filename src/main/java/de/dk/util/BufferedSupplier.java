package de.dk.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author David Koettlitz
 * <br/>Erstellt am 20.10.2016
 */
public class BufferedSupplier<T> implements Supplier<T> {
   private T buffer;
   private Supplier<? extends T> supplier;

   public BufferedSupplier(Supplier<? extends T> supplier) {
      Objects.requireNonNull(supplier);
      this.supplier = supplier;
   }

   @Override
   public T get() {
      return buffer != null ? buffer : update();
   }

   public T update() {
      return (this.buffer = supplier.get());
   }
}