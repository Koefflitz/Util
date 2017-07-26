package de.dk.util.opt.ex;

import de.dk.util.opt.ExpectedOption;

/**
 * Thrown to indicate that a mandatory option was absent.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 */
public class MissingOptionException extends ArgumentParserException {
   private static final long serialVersionUID = 1L;

   public MissingOptionException(ExpectedOption option) {
      super("Missing option " + option.fullName());
   }
}