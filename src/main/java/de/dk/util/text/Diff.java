package de.dk.util.text;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Class to compare two strings and extract their differences.
 *
 * It is possible to apply a matcher predicate.
 * The purpose of the matcher is to define a threshold when the two strings match again.
 * For example if you have two strings:<br>
 * string a: "<i>An example string. The programmer writes the program.</i>"<br>
 * string b: "<i>An example string. I need 2 grams, please.</i>"<br>
 * In this example the string "<i>gram</i>" is found in "<i>The pro<b>gram</b>mer...</i>"
 * and in "<i>I need 2 <b>gram</b>s, please.</i>" but it should not be marked as common.
 * To define a match the matcher is called whenever two strings are equal.
 * The default implementation marks a match of a common string if it has a length of at least 16.
 *
 * @author David Koettlitz
 * <br>Erstellt am 23.03.2017
 */
public class Diff {
   private Predicate<String> matcher;

   /**
    * Creates a new diff object with the specified matcher.
    *
    * @param matcher the matcher that defines when the two strings are coming together again.
    */
   public Diff(Predicate<String> matcher) {
      setMatcher(matcher);
   }

   /**
    * Creates a new diff object with a matcher that triggers a match,
    * if a common string has a length of at least 16.
    */
   public Diff() {
      this(minLengthMatcher(16));
   }

   /**
    * Get a matcher that marks a common string as match, if it has at least a minimum length.
    *
    * @param minLength The minimum length a common string has to have to be a match.
    *
    * @return the matcher predicate
    */
   public static Predicate<String> minLengthMatcher(int minLength) {
      return s -> s.length() >= minLength;
   }

   /**
    * Compares two strings and creates a diff model as result.
    *
    * @param a some string
    * @param b another string to be compared with <code>a</code>
    *
    * @return a diff model that contains the common string of both
    * and the specific differences of <code>a</code> and <code>b</code>.
    */
   public DiffModel diff(String a, String b) {
      return diff(new CharIterator(a), new CharIterator(b));
   }

   /**
    * Compares two strings of two <code>CharIterator</code>s and creates a diff model as result.
    *
    * @param a some char iterator
    * @param b another char iterator
    *
    * @return a diff model that contains the common string of both
    * and the specific differences of <code>a</code> and <code>b</code>.
    */
   public DiffModel diff(CharIterator a, CharIterator b) {
      DiffModel model = new DiffModel();
      SubstringBuilder diffA = null;
      SubstringBuilder diffB = null;

      while (a.hasNext() && b.hasNext()) {
         char ca = a.peek();
         if (ca == b.peek()) {
            model.append(ca);
            a.next();
            b.next();
            continue;
         }

         if (diffA == null)
            diffA = new SubstringBuilder(model.getCommon().length());
         if (diffB == null)
            diffB = new SubstringBuilder(model.getCommon().length());

         String match = findNextMatch(a, b, diffA, diffB);
         if (match != null)
            model.append(match);

         if (diffA != null)
            model.addDiffA(diffA.build());
         if (diffB != null)
            model.addDiffB(diffB.build());

         diffA = null;
         diffB = null;
      }

      if (a.hasNext())
         model.addDiffA(new Substring(a.getRemainingString(), model.getCommon().length()));
      else if (b.hasNext())
         model.addDiffB(new Substring(b.getRemainingString(), model.getCommon().length()));

      return model;
   }

   private String findNextMatch(CharIterator a, CharIterator b, SubstringBuilder diffA, SubstringBuilder diffB) {
      int lenBBefore = diffB.length();
      String match = "";

      while (a.hasNext()) {
         diffB.setLength(lenBBefore);

         char ca = a.peek();
         CharIterator bb = b.branch();

         while (bb.hasNext()) {
            char cb = bb.peek();

            if (ca != cb) {
               diffB.append(bb.next());
               match = "";
            } else {
               CharIterator ba = a.branch();
               match = match(ba, bb, diffB);
               if (match != null) {
                  a.moveTo(ba);
                  b.moveTo(bb);
                  return match;
               }
            }
         }

         diffA.append(a.next());
      }
      b.readToEnd();
      return null;
   }

   private String match(CharIterator a, CharIterator b, SubstringBuilder diff) {
      CharIterator ba = a.branch();
      String match = "";
      while (ba.hasNext() && b.hasNext()) {
         char ca = ba.peek();
         char cb = b.peek();
         if (ca != cb)
            break;

         match += ca;
         ba.next();
         b.next();
      }
      if (matcher.test(match)) {
         a.moveTo(ba);
         return match;
      }
      diff.append(match);
      return null;
   }

   /**
    * Get the matcher that this diff uses to find a match.
    *
    * @return the matcher
    */
   public Predicate<String> getMatcher() {
      return matcher;
   }

   /**
    * Set the matcher that defines when the two strings <code>a</code> and <code>b</code> continue together.
    *
    * @param matcher the matcher predicate
    */
   public void setMatcher(Predicate<String> matcher) {
      this.matcher = Objects.requireNonNull(matcher);
   }

}