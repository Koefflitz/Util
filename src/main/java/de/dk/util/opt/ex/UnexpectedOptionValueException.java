package de.dk.util.opt.ex;

import de.dk.util.opt.ExpectedOption;

public class UnexpectedOptionValueException extends ArgumentParseException {
   private static final long serialVersionUID = -7461242904311426155L;

   public UnexpectedOptionValueException(ExpectedOption opt, String arg) {
      super("No value for option " + opt.fullName() + " expected: " + arg);
   }
}
