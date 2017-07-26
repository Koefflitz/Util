package de.dk.util;

import static de.dk.util.StringUtils.filter;
import static de.dk.util.StringUtils.getCommonPrefixesOf;
import static de.dk.util.StringUtils.getCommonPrefixesOfParallel;
import static de.dk.util.StringUtils.getIndicesOf;
import static de.dk.util.StringUtils.getIndicesAfter;
import static de.dk.util.StringUtils.getLastLineOf;
import static de.dk.util.StringUtils.getLineUntil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.dk.util.StringUtils.SimpleStringIterator;

public class StringUtilsTest {
   @Rule
   public ExpectedException expectedException = ExpectedException.none();

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

      strings = null;
      expectedException.expect(NullPointerException.class);
      getCommonPrefixesOf(strings);

      expectedException.expect(NullPointerException.class);
      getCommonPrefixesOfParallel(strings);
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

      expectedException.expect(NullPointerException.class);
      getLastLineOf(null);
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

      expectedException.expect(StringIndexOutOfBoundsException.class);
      getLineUntil(a, -1);
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
   public void testSimpleStringIterator() {
      String string = "Einfach ein String zum Testen.\nöäüß";
      Iterable<Character> iterable = () -> new SimpleStringIterator(string);
      int index = 0;
      for (char c : iterable)
         assertEquals(string.charAt(index++), c);
   }

}