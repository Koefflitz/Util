package de.dk.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains utility methods for converting and validating Strings.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 30.08.2016
 */
public final class StringUtils {

   private StringUtils() {}

   /**
    * Ignores all chars of the string which have a specific filter-char as prefix
    * and returns the filtered string.
    *
    * @param filter The filtering char, which can be a "\"
    * @param string The string to be filtered
    *
    * @return The filtered string
    */
   public static String filter(char filter, String string) {
      if (string.length() <= 1)
         return string;

      String result = "";
      boolean ignore = false;
      for (int i = 0; i < string.length(); i++) {
         char c = string.charAt(i);
         if (ignore) {
            ignore = false;
            continue;
         }

         ignore = c == filter;
         result += ignore ? "" : c;
      }

      return result;
   }

   /**
    * Retrieves the prefix that all of the strings have in common.
    *
    * @param strings The strings to be inspected.
    *
    * @return The substring from the first char to the last common char of all the strings.
    *
    * @throws NullPointerException if the given strings are <code>null</code>.
    */
   public static String getCommonPrefixesOf(String... strings) {
      int smallest = Integer.MAX_VALUE;
      for (String string : Objects.requireNonNull(strings)) {
         if (string == null)
            return null;

         if (string.length() < smallest)
            smallest = string.length();
      }

      for (int i = 0; i < smallest; i++) {
         char c = strings[0].charAt(i);
         for (int j = 1; j < strings.length; j++) {
            if (strings[j].charAt(i) != c)
               return i == 0 ? null : strings[0].substring(0, i);
         }
      }
      return strings[0].substring(0, smallest);
   }

   /**
    * Retrieves the prefix that all of the strings have in common. Uses the {@link Stream}-API
    * to parallelize the algorithm. Only recommended for very big amounts of data.
    * Otherwise use the {@link #getCommonPrefixesOf(String...)} method.
    *
    * @param strings The stream of strings to be inspected.
    *
    * @return The substring from the first char to the last common char of all the strings.
    */
   public static String getCommonPrefixesOfParallel(String[] strings) {
      if (Arrays.stream(Objects.requireNonNull(strings))
                .parallel()
                .anyMatch(s -> s == null)) {
         return null;
      }

      int smallest = Arrays.stream(strings)
                           .parallel()
                           .mapToInt(String::length)
                           .min()
                           .getAsInt();

      int endIndex = 0;
      for (int i = 0; i < smallest; i++) {
         final int j = i;
         boolean equal = Arrays.stream(strings)
                               .parallel()
                               .map(s -> s.charAt(j))
                               .distinct()
                               .count() == 1;
         if (!equal) {
            endIndex = i;
            break;
         }
      }

      int finalEndIndex = endIndex;
      return Optional.ofNullable(strings[0])
                     .map(s -> s.substring(0, finalEndIndex))
                     .filter(s -> !s.isEmpty())
                     .orElse(null);
   }

   /**
    * Checks whether the string is <code>null</code>, empty or
    * contains only whitespaces.
    */
   public static boolean isBlank(String string) {
      return string == null || string.trim().isEmpty();
   }

   /**
    * Checks whether the char is whitespace.
    */
   public static boolean isBlank(char c) {
      return isBlank("" + c);
   }

   /**
    * Gets the first line of the <code>text</code>.
    *
    * @param text The text of which you want the first line
    *
    * @return the first line of the <code>text</code>
    */
   public static String getFirstLineOf(String text) {
      String line = "";
      for (char c : text.toCharArray()) {
         if (c == '\n' || c == '\r')
            return line;

         line += c;
      }
      return line;
   }

   /**
    * Retrieves the last line of the string.
    *
    * @param text The string to be inspected.
    *
    * @return The last line of the string or an empty string if the string is <code>null</code>.
    */
   public static String getLastLineOf(String text) {
      if (Objects.requireNonNull(text).equals(""))
         return "";

      if (!text.contains("\n") && !text.contains("\r"))
         return text;

      if (text.length() <= 1)
         return "";

      int lastN = text.lastIndexOf('\n');
      int lastR = text.lastIndexOf('\r');
      int lastReturnIndex = lastN == -1 ?
                            lastR :
                            lastR == -1 ? lastN : Math.min(lastN, lastR);

      return text.substring(lastReturnIndex + 1);
   }

   /**
    * Concats the <code>strings</code> to one <code>String</code> object
    * inserting the <code>separator</code> between them.
    *
    * @param separator The separator to be inserted between the <code>strings</code>
    * If <code>null</code> is given, no separator will be inserted.
    * @param strings The strings to be concatenated to one string object
    *
    * @return A <code>String</code> that contains the given <code>strings</code>
    * in a sequence separated by <code>separator</code>
    * or <code>null</code> if no <code>strings</code> are specified
    */
   public static String concat(String separator, String... strings) {
      if (strings == null)
         return null;

      if (separator == null)
         separator = "";

      StringBuilder result = new StringBuilder();
      for (int i = 0; i < strings.length; i++) {
         result.append(strings[i]);
         if (i < strings.length - 1)
            result.append(separator);
      }
      return result.toString();
   }

   /**
    * Retrieves the substring of the line of the index until the index exclusive.
    *
    * @param text The string to be inspected.
    * @param index The index after the last char of the result string.
    *
    * @return The substring of the line of the index until the index exclusive.
    *
    * @throws StringIndexOutOfBoundsException if the index is less than zero or greater than the strings length.
    */
   public static String getLineUntil(String text, int index) throws StringIndexOutOfBoundsException {
      if (Objects.requireNonNull(text).isEmpty())
         return "";
      if (index < 0 || index > text.length())
         throw new StringIndexOutOfBoundsException(index);

      boolean entireLineWanted = index == text.length() || (text.charAt(index) == '\n' || text.charAt(index) == '\r');
      String left = text.substring(0, index + (entireLineWanted ? 0 : 1));

      int beginIndex = Math.max(Math.max(left.lastIndexOf('\n') + 1, left.lastIndexOf('\r') + 1), 0);

      return text.substring(beginIndex, index);
   }

   /**
    * Determines the indices of the first char of all occurences
    * of the pattern in the string.
    *
    * @param pattern The pattern to look for.
    * @param string The string in which the pattern is looked for.
    *
    * @return The list of the indices of all the occurences of the pattern.
    *         If no matches are found an empty list is returned.
    */
   public static List<Integer> getIndicesOf(String pattern, String string) {
      List<Integer> indices = new ArrayList<>();

      int counter = 0;
      char[] chars = string.toCharArray();

      for (int i = 0; i < chars.length; i++) {
         if (chars[i] != pattern.charAt(counter++)) {
            counter = 0;
         } else if (counter == pattern.length()) {
            indices.add(i - (counter - 1));
            counter = 0;
         }
      }
      return indices;
   }

   /**
    * Determines the indices after char of all occurences
    * of the <code>pattern</code> in the <code>string</code>.
    *
    * @param pattern The pattern to look for.
    * @param string The string in which the pattern is looked for.
    *
    * @return The list of the indices after all the occurences of the pattern.
    *         If no matches are found an empty list is returned.
    */
   public static List<Integer> getIndicesAfter(String pattern, String string) {
      List<Integer> indices = new ArrayList<>();

      int counter = 0;
      char[] chars = string.toCharArray();

      for (int i = 0; i < chars.length; i++) {
         if (chars[i] != pattern.charAt(counter++)) {
            counter = 0;
         } else if (counter == pattern.length()) {
            indices.add(i + 1);
            counter = 0;
         }
      }
      return indices;
   }

   public static class SimpleStringIterator implements Iterator<Character> {
      private char[] value;
      private int index = 0;

      public SimpleStringIterator(String value) {
         this.value = value.toCharArray();
      }

      @Override
      public boolean hasNext() {
         return index < value.length;
      }

      @Override
      public Character next() {
         return value[index++];
      }
   }
}