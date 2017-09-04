package de.dk.util.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.dk.util.timing.TimeUtils;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CharIteratorTest {
   @Rule
   public ExpectedException expectedException = ExpectedException.none();

   protected final String line0 = "Test";
   protected final String line1 = "Testtest";
   protected final String content = line0 + '\n' + line1;
   protected CharIterator iterator;

   public CharIteratorTest() {

   }

   private static StringBuilder wrapToStringMeta(StringBuilder content) {
      return content.insert(0, "CharIterator {\n")
                    .append("\n}");
   }

   @Before
   public void init() {
      this.iterator = new CharIterator(content);
   }

   @Test
   public void testBasics() {
      assertTrue(iterator.hasNext());
      assertEquals(content, iterator.getContent());
      assertEquals("" + content.charAt(0), "" + iterator.peek());
      assertEquals("", iterator.getAlreadyReadString());
      assertEquals(content, iterator.getRemainingString());
      assertEquals("" + content.charAt(0), "" + iterator.next());
      assertNotEquals("" + content.charAt(0), "" + iterator.next());
   }

   @Test
   public void testReadLine() {
      assertEquals(line0, iterator.readLine());
      assertEquals(line0 + '\n', iterator.getAlreadyReadString());
      assertEquals(line1, iterator.getRemainingString());
      assertEquals(1, iterator.getLineNumber());
      assertEquals(0, iterator.getColumnNumber());
   }

   @Test
   public void testPeekLine() {
      assertEquals(line0, iterator.peekLine());
      assertEquals("", iterator.getAlreadyReadString());
      assertEquals(iterator.getContent(), iterator.getRemainingString());
      assertEquals(0, iterator.getLineNumber());
      assertEquals(0, iterator.getColumnNumber());
   }

   @Test
   public void testReadUntilChar() {
      assertEquals(line0, iterator.readUntil('\n'));
      assertEquals(line0 + '\n', iterator.getAlreadyReadString());
      assertEquals(line1, iterator.getRemainingString());
      assertEquals(1, iterator.getLineNumber());
      assertEquals(0, iterator.getColumnNumber());
   }

   @Test
   public void testReadUntilPredicate() {
      assertEquals(line0, iterator.readUntil(c -> c == '\n'));
      assertEquals(line0 + '\n', iterator.getAlreadyReadString());
      assertEquals(line1, iterator.getRemainingString());
      assertEquals(1, iterator.getLineNumber());
      assertEquals(0, iterator.getColumnNumber());
   }

   @Test
   public void testReadUntilBefore() {
      assertEquals(line0, iterator.readUntilBefore('\n'));
      assertEquals(line0, iterator.getAlreadyReadString());
      assertEquals('\n' + line1, iterator.getRemainingString());
      assertEquals(0, iterator.getLineNumber());
      assertEquals(line0.length(), iterator.getColumnNumber());
   }

   @Test
   public void testToString() {
      StringBuilder builder = new StringBuilder();
      builder.append(line0)
             .append("\n^\n")
             .append(line1);
      assertEquals(wrapToStringMeta(builder).toString(), iterator.toString());

      iterator.next();
      builder.setLength(0);
      builder.append(line0)
             .append("\n ^\n")
             .append(line1);

      iterator.readUntilBefore('\n');
      builder.setLength(0);
      builder.append(line0)
             .append("\n<\n")
             .append(line1);
      assertEquals(wrapToStringMeta(builder).toString(), iterator.toString());

      assertEquals("\n", "" + iterator.next());
      builder.setLength(0);
      builder.append(line0)
             .append('\n')
             .append(line1)
             .append("\n^");
      assertEquals(wrapToStringMeta(builder).toString(), iterator.toString());

      iterator.readToEnd();
      builder.setLength(0);
      builder.append(line0)
             .append('\n')
             .append(line1)
             .append('\n');
      for (int i = 0; i < line1.length(); i++)
         builder.append(' ');

      builder.append('>');
      assertEquals(wrapToStringMeta(builder).toString(), iterator.toString());
   }

   @Test
   public void testReadFastToEnd() {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < 1024; i++) {
         builder.append(content)
                .append('\n');
      }

      CharIterator tmp = new CharIterator(builder.toString());
      long slow = TimeUtils.time(tmp::readToEnd);
      tmp = new CharIterator(builder.toString());
      long fast = TimeUtils.time(tmp::readFastToEnd);
      assertTrue("readToEnd needed " + slow + "ns, while readFastToEnd needed " + fast + "ns",
                 fast < slow);

      assertEquals(content, iterator.readFastToEnd());
      assertTrue(iterator.isClosed());
      expectedException.expect(NoSuchElementException.class);
      iterator.next();
      expectedException.expect(IllegalStateException.class);
      iterator.getColumnNumber();
   }

   @Test
   public void testBranching() {
      CharIterator branch = iterator.branch();
      assertEquals(content, iterator.getContent());
      assertEquals(content, branch.getContent());
      assertEquals(content, branch.getRemainingString());
      assertEquals("", branch.getAlreadyReadString());
      assertTrue(branch.hasNext());
      assertEquals("" + content.charAt(0), "" + branch.next());
      assertNotEquals(iterator.peek(), branch.peek());
      iterator.moveTo(branch);
      assertEquals(branch.peek(), iterator.peek());
   }

   @After
   public void tearDown() {
      iterator = null;
   }
}
