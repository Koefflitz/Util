package de.dk.util.opt.ex;

/**
 * Thrown to indicate that something went wrong while parsing arguments.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 */
public class ArgumentParserException extends Exception {
   private static final long serialVersionUID = 1L;

   public ArgumentParserException() {

   }

   public ArgumentParserException(String message) {
      super(message);
   }

   public ArgumentParserException(Throwable cause) {
      super(cause);
   }

   public ArgumentParserException(String message, Throwable cause) {
      super(message, cause);
   }

   public ArgumentParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}