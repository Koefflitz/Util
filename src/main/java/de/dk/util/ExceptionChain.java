package de.dk.util;
import java.util.function.Consumer;

/**
 *
 * @author David Koettlitz
 * <br/>Erstellt am 14.03.2017
 */
public class ExceptionChain<E extends Exception> {
   private E value;

   public ExceptionChain() {

   }

   public void add(E exception) {
      if (value != null) {
         for (Throwable t : value.getSuppressed())
            exception.addSuppressed(t);

         exception.addSuppressed(value);
      }
      value = exception;
   }

   public void ifPresent(Consumer<? super E> action) {
      if (isPresent())
         action.accept(value);
   }

   public void ifPresent(UnsafeConsumer<? super E, E> action) throws E {
      if (isPresent())
         action.accept(value);
   }

   public void throwIfPresent() throws E {
      if (isPresent())
         throw value;
   }

   public E getException() {
      return value;
   }

   public boolean isPresent() {
      return value != null;
   }

}