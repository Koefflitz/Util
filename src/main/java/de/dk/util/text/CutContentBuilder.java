package de.dk.util.text;

import java.util.List;
import java.util.Objects;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CutContentBuilder {
   private String value;
   private List<Substring> outtakes;

   public CutContentBuilder(String value) {
      this.value = Objects.requireNonNull(value);
   }

   public CutContentBuilder() {
      this("");
   }

   public CutContentBuilder append(String s) {
      value += s;
      return this;
   }

   public void addOuttake(Substring outtake) {
      outtakes.add(outtake);
   }

   public CutContent build() {
      return new CutContent(value, outtakes.toArray(new Substring[outtakes.size()]));
   }

}
