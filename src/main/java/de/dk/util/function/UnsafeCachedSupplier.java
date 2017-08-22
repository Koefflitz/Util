package de.dk.util.function;

import java.util.Objects;

/**
 * An <code>UnsafeSupplier</code> that caches the value supplied by the underlying supplier.
 *
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 *
 * @see CachedSupplier
 */
public class UnsafeCachedSupplier<T, E extends Exception> implements UnsafeSupplier<T, E> {
   private T buffered;
   private UnsafeSupplier<? extends T, ? extends E> supplier;

   /**
    * Create a new Cached supplier that caches the objects
    * from the underlying <code>supplier</code>.
    *
    * @param supplier The underlying supplier
    *
    * @throws NullPointerException if <code>supplier</code> is <code>null</code>.
    */
   public UnsafeCachedSupplier(UnsafeSupplier<? extends T, ? extends E> supplier) {
      this.supplier = Objects.requireNonNull(supplier);
   }

   /**
    * Get the value from the underlying supplier.
    * If no value is cached the underlying supplier will be called and the supplied value will be cached.
    * Otherwise the cached value is returned.
    *
    * @throws E if no value is cached, so the underlying supplier has to be called
    * and the underlying supplier throws an exception
    */
   @Override
   public T get() throws E {
      return buffered != null ? buffered : update();
   }

   /**
    * Updates the cached value by calling the underlying supplier.
    *
    * @return The freshly supplied and now cached value.
    *
    * @throws E if the underlying supplier throws an exception
    */
   public T update() throws E {
      return (this.buffered = supplier.get());
   }
}
