package de.dk.util.net;

import java.io.IOException;

/**
 * This exception indicates that something unexpected was read from a {@link Connection}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 */
public class ReadingException extends IOException {
   private static final long serialVersionUID = 3342185559159912816L;

   public ReadingException() {

   }

   public ReadingException(String message) {
      super(message);
   }

   public ReadingException(Throwable cause) {
      super(cause);
   }

   public ReadingException(String message, Throwable cause) {
      super(message, cause);
   }
}