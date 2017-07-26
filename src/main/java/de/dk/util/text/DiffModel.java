package de.dk.util.text;

import java.util.LinkedList;
import java.util.List;

/**
 * Resultmodel of a comparison of two string by a {@link Diff}.
 * Contains the common parts of the two strings and the specific differences of each string.
 *
 * @author David Koettlitz
 * <br>Erstellt am 23.03.2017
 */
public class DiffModel {
   private String common = "";

   private List<Substring> diffA = new LinkedList<>();
   private List<Substring> diffB = new LinkedList<>();

   public DiffModel() {

   }

   /**
    * Appends a <code>char</code> to the common string.
    *
    * @param c a char that both strings have in common.
    */
   public void append(char c) {
      common += c;
   }

   /**
    * Appends a string to the common string.
    *
    * @param s a string that both strings have in common.
    */
   public void append(String s) {
      common += s;
   }

   /**
    * Adds a string that only the string a has but not b.
    *
    * @param diff a string that is in a but not in b.
    */
   public void addDiffA(Substring diff) {
      if (diff != null && !diff.getValue().isEmpty())
         diffA.add(diff);
   }

   /**
    * Adds a string that only the string b has but not a.
    *
    * @param diff a string that is in b but not in b.
    */
   public void addDiffB(Substring diff) {
      if (diff != null && !diff.getValue().isEmpty())
         diffB.add(diff);
   }

   /**
    * Get the string that both strings, a and b have in common.
    *
    * @return the string that both strings have in common
    */
   public String getCommon() {
      return common;
   }

   /**
    * Get the list of strings that <code>a</code> has but not <code>b</code>
    *
    * @return the list of strings that <code>a</code> has but not <code>b</code>
    */
   public List<Substring> getDiffA() {
      return diffA;
   }

   /**
    * Get the list of strings that <code>b</code> has but not <code>a</code>;
    *
    * @return the list of strings that <code>b</code> has but not <code>a</code>
    */
   public List<Substring> getDiffB() {
      return diffB;
   }

}