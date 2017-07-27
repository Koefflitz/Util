package de.dk.util.opt.ex;

public class UnknownArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = -5582672005397832472L;

   private String arg;

   public UnknownArgumentException(String arg) {
      super("Unknown argument: " + arg);
   }

   public String getUnknownArg() {
      return this.arg;
   }
}
