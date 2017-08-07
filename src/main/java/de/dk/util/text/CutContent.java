package de.dk.util.text;

import java.util.Arrays;
import java.util.List;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CutContent {
   private final String value;
   private List<Substring> outtakes;

   public CutContent(String value, Substring... outtakes) {
      this.value = value;
      this.outtakes = Arrays.asList(outtakes);
   }

   public String reconstructFullContent() {
      String fullContent = value;
      for (Substring outtake : outtakes)
         fullContent = outtake.insertInto(fullContent);

      return fullContent;
   }

   public List<Substring> getOuttakes() {
      return outtakes;
   }

   public String getValue() {
      return value;
   }

}
