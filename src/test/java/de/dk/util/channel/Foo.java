package de.dk.util.channel;

import de.dk.util.net.TestObject;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class Foo extends TestObject {
   private static final long serialVersionUID = 5555923218722447835L;

   public Foo() {
      super("foo");
   }

   protected Foo(String msg) {
      super(msg);
   }
}
