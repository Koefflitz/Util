package de.dk.util;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class UnsafeBufferedSupplier<T, E extends Exception> implements UnsafeSupplier<T, E> {
   private T buffered;
   private UnsafeSupplier<? extends T, ? extends E> supplier;

   public UnsafeBufferedSupplier(UnsafeSupplier<? extends T, ? extends E> supplier) {
      this.supplier = supplier;
   }

   @Override
   public T get() throws E {
      return buffered != null ? buffered : update();
   }

   public T update() throws E {
      return (this.buffered = supplier.get());
   }
}
