package de.dk.util.javafxUtils;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextInputControl;

/**
 * @author a0073646
 * <br/>Erstellt am 05.08.2016
 */
public class TextInputControlOutputstream extends OutputStream {
   private TextInputControl target;
   private String buffer = "";

   public TextInputControlOutputstream(TextInputControl target) {
      this.target = target;
   }

   @Override
   public void write(int b) throws IOException {
      buffer += (char) b;
   }

   @Override
   public void flush() throws IOException {
      if (buffer == null || buffer.isEmpty())
         return;

      target.appendText(buffer);
      buffer = "";
   }

   @Override
   public void close() throws IOException {
      flush();
      super.close();
   }

}