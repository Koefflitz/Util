package de.dk.util.opt.ex;

import de.dk.util.opt.ExpectedOption;

/**
 * Thrown to indicate that an option that has to go with a following value was no value given.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 */
public class MissingOptionValueException extends ArgumentParserException {
   private static final long serialVersionUID = 1L;

   public MissingOptionValueException(ExpectedOption option) {
      super("Missing value for option " + option.fullName());
   }
}