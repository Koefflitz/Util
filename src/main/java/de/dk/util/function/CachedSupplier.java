package de.dk.util.function;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A <code>Supplier</code> that caches the value supplied by the underlying supplier.
 *
 * @author David Koettlitz
 * <br>Erstellt am 20.10.2016
 */
public class CachedSupplier<T> implements Supplier<T> {
   private T buffer;
   private Supplier<? extends T> supplier;

   /**
    * Create a new Cached supplier that caches the objects
    * from the underlying <code>supplier</code>.
    *
    * @param supplier The underlying supplier
    *
    * @throws NullPointerException if <code>supplier</code> is <code>null</code>.
    */
   public CachedSupplier(Supplier<? extends T> supplier) throws NullPointerException {
      Objects.requireNonNull(supplier);
      this.supplier = supplier;
   }

   /**
    * Get the value from the underlying supplier.
    * If no value is cached the underlying supplier will be called and the supplied value will be cached.
    * Otherwise the cached value is returned.
    */
   @Override
   public T get() {
      return buffer != null ? buffer : update();
   }

   /**
    * Updates the cached value by calling the underlying supplier.
    *
    * @return The freshly supplied and now cached value.
    */
   public T update() {
      return (this.buffer = supplier.get());
   }

   /**
    * Drops the cached value, if a cached value is present.
    */
   public void clearCache() {
      this.buffer = null;
   }
}
