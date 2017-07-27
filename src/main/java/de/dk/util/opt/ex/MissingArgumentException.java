package de.dk.util.opt.ex;

import java.util.List;

import de.dk.util.opt.ExpectedArgument;

/**
 * Thrown to indicate that a mandatory argument was absent.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 */
public class MissingArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = 1L;

   private final List<? extends ExpectedArgument> missingArguments;

   public MissingArgumentException(List<? extends ExpectedArgument> missingArguments) {
      super(createMessage(missingArguments));
      this.missingArguments = missingArguments;
   }

   private static String createMessage(List<? extends ExpectedArgument> missingArguments) {
      String msg = "Missing Arguments:";
      for (ExpectedArgument arg : missingArguments)
         msg += " <" + arg.getName() + ">";

      return msg;
   }

   public List<? extends ExpectedArgument> getMissingArguments() {
      return missingArguments;
   }

}