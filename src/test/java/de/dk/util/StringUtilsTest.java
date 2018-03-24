package de.dk.util;

import static de.dk.util.StringUtils.filter;
import static de.dk.util.StringUtils.getCommonPrefixesOf;
import static de.dk.util.StringUtils.getCommonPrefixesOfParallel;
import static de.dk.util.StringUtils.getIndicesAfter;
import static de.dk.util.StringUtils.getIndicesOf;
import static de.dk.util.StringUtils.getLastLineOf;
import static de.dk.util.StringUtils.getLineUntil;
import static de.dk.util.StringUtils.indent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.dk.util.StringUtils.SimpleStringIterator;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class StringUtilsTest {

   public StringUtilsTest() {

   }

   @Test
   public void testFilter() {
      String origin = "\\abc\\def\\gh\\\\ij\\k";
      String expected = "bcefhij";
      assertEquals(expected, filter('\\', origin));
   }

   @Test
   public void testPrefixes() {
      String prefix = "The ";
      String[] strings = new String[] {
         prefix + "small man",
         prefix + "small woman",
         prefix + "small dog",
         prefix + "bird"
      };
      assertEquals(prefix, getCommonPrefixesOf(strings));
      assertEquals(prefix, getCommonPrefixesOfParallel(strings));

      strings = new String[] {
         "one",
         "two",
         "athree",
         "four"
      };
      assertNull(getCommonPrefixesOf(strings));
      assertNull(getCommonPrefixesOfParallel(strings));

      String[] nullString = null;
      assertThrows(NullPointerException.class, () -> getCommonPrefixesOf(nullString));
      assertThrows(NullPointerException.class, () -> getCommonPrefixesOfParallel(nullString));
   }

   @Test
   public void testGetLastLineOf() {
      String lastLine = "last line";
      String string = "first line\n"
                      + "second line\n"
                      + "third line\n"
                      + lastLine;

      assertEquals(lastLine, getLastLineOf(string));

      string = "kjafhbäga#egjrefbgadmfsönkv ds fv pjqwfe#+e";
      assertEquals(string, getLastLineOf(string));

      assertThrows(NullPointerException.class, () -> getLastLineOf(null));
   }

   @Test
   public void testGetLineUntil() {
      String theWord = "THEWORD";
      String a = "a_Blabla bla";
      String b = "b_Testtestteßttestitest";
      String c = "c_bliblu blü blubberblaß.";
      String d = "d_kaljdsfhesböjdcassä jwqpj ";

      String text = theWord + a + '\n' + b + '\n' + c + '\n' + d;
      assertEquals(theWord, getLineUntil(text, theWord.length()));

      text = a + theWord + '\n' + b + '\n' + c + '\n' + d;
      assertEquals(a + theWord, getLineUntil(text, a.length() + theWord.length()));

      text = a + '\n' + theWord + b + '\n' + c + '\n' + d;
      assertEquals(theWord, getLineUntil(text, a.length() + 1 + theWord.length()));

      text = a + '\n' + b + '\n' + c + '\n' + theWord + d;
      int index = text.length() - d.length();
      assertEquals(theWord, getLineUntil(text, index));

      text = a + '\n' + b + '\n' + c + '\n' + d + theWord;
      assertEquals(d + theWord, getLineUntil(text, text.length()));

      text = a + theWord + b;
      assertEquals(a + theWord, getLineUntil(text, a.length() + theWord.length()));

      assertThrows(StringIndexOutOfBoundsException.class, () -> getLineUntil(a, -1));
   }

   @Test
   public void testGetIndicesOf() {
      String pattern = "Pfirsichsuppe";
      String a = "Blabla bla";
      String b = "Testtesttesttestitest";
      String c = "bliblu blü blubberbla.";
      String d = "kaljdsfhesböjdcassä jwqpj ";

      String string = pattern + a + pattern + b + pattern + c + pattern + d + pattern;
      List<Integer> expected = Arrays.asList(0,
                                             pattern.length() + a.length(),
                                             2 * pattern.length() + a.length() + b.length(),
                                             3 * pattern.length() + a.length() + b.length() + c.length(),
                                             string.length() - pattern.length());

      List<Integer> expectedEnd = Arrays.asList(pattern.length(),
                                                2 * pattern.length() + a.length(),
                                                3 * pattern.length() + a.length() + b.length(),
                                                4 * pattern.length() + a.length() + b.length() + c.length(),
                                                string.length());

      assertEquals(expected, getIndicesOf(pattern, string));
      assertEquals(0, getIndicesOf(pattern, a).size());

      assertEquals(expectedEnd, getIndicesAfter(pattern, string));
      assertEquals(0, getIndicesAfter(pattern, a).size());
   }

   @Test
   public void testIndent() {
      String a = "abc";
      assertEquals('\t' + a, indent(a, 1));
      String b = a + '\n' + a;
      assertEquals("\t\t" + a + "\n\t\t" + a, indent(b, 2));
      String c = b + '\n' + a + '\n';
      assertEquals("\t\t\t" + a + "\n\t\t\t" + a + "\n\t\t\t" + a + "\n", indent(c, 3));

      assertEquals(' ' + a, indent(a, 1, 1));
      assertEquals("    " + a + "\n    " + a, indent(b, 2, 2));
      assertEquals("   " + a + "\n   " + a + "\n   " + a + "\n", indent(c, 1, 3));
   }

   @Test
   public void testSimpleStringIterator() {
      String string = "Einfach ein String zum Testen.\nöäüß";
      Iterable<Character> iterable = () -> new SimpleStringIterator(string);
      int index = 0;
      for (char c : iterable)
         assertEquals(string.charAt(index++), c);
   }

}
