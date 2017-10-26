package de.dk.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * An object of this class represents a buffered value.
 *
 * @param <T> The type of the buffered value.
 *
 * @author David Koettlitz
 * <br>Erstellt am 23.10.2017
 */
public class Buffer<T> {
   private T value;

   /**
    * Create a new Buffer containing <code>value</code>.
    *
    * @param value The value contained by this buffer
    */
   public Buffer(T value) {
      this.value = value;
   }

   /**
    * Creates a new empty buffer.
    */
   public Buffer() {
      this(null);
   }

   /**
    * Set a new value to this buffer.
    *
    * @param value The value to be contained by this buffer
    *
    * @return An <code>Optional</code> containing the previous value
    * of this buffer or an empty <code>Optional</code> if the buffer was empty before
    */
   public synchronized Optional<T> set(T value) {
      T previous = this.value;
      this.value = value;
      return Optional.ofNullable(previous);
   }

   /**
    * Removes and returns this buffers value.
    *
    * @return An <code>Optional</code> conatining the value of this buffer
    * or an empty <code>Optional</code> if this buffer was empty.
    */
   public synchronized Optional<T> pop() {
      T value = this.value;
      this.value = null;
      return Optional.ofNullable(value);
   }

   /**
    * Get the value of this buffer without removing it.
    *
    * @return An <code>Optional</code> conatining the value of this buffer
    * or an empty <code>Optional</code> if this buffer was empty.
    */
   public Optional<T> peek() {
      return Optional.ofNullable(value);
   }

   /**
    * Removes the value of this buffer and passes it to the <code>consumer</code>
    * if this buffer currently contains a value.
    *
    * @param consumer The consumer to consume this buffer value
    *
    * @return <code>true</code> if the consumer was invoked, <code>false</code> otherwise
    */
   public boolean ifPresent(Consumer<T> consumer) {
      boolean result = isPresent();
      pop().ifPresent(consumer);
      return result;
   }

   /**
    * Get if this buffer is currently containing a value or is empty.
    *
    * @return <code>true</code> if this buffer currently contains a value,
    * <code>false</code> otherwise
    */
   public boolean isPresent() {
      return value != null;
   }
}
