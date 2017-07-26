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

public class CharIteratorTest {
   @Rule
   public ExpectedException expectedException = ExpectedException.none();

   protected final String line0 = "Test";
   protected final String line1 = "Testtest";
   protected final String content = line0 + '\n' + line1;
   protected CharIterator iterator;

   public CharIteratorTest() {

   }

   private static String wrapToStringMeta(String content) {
      return "CharIterator {\n" + content + "\n}";
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
      String expected = line0 + '\n'
                        + "^\n"
                        + line1;
      assertEquals(wrapToStringMeta(expected), iterator.toString());

      iterator.next();
      expected = line0 + '\n'
                 + " ^\n"
                 + line1;

      iterator.readUntilBefore('\n');
      expected = line0 + '\n'
                 + "<\n"
                 + line1;
      assertEquals(wrapToStringMeta(expected), iterator.toString());

      assertEquals("\n", "" + iterator.next());
      expected = line0 + '\n'
                 + line1 + '\n'
                 + "^";
      assertEquals(wrapToStringMeta(expected), iterator.toString());

      iterator.readToEnd();
      expected = line0 + '\n'
                 + line1 + '\n';
      for (int i = 0; i < line1.length(); i++)
         expected += ' ';

      expected += '>';
      assertEquals(wrapToStringMeta(expected), iterator.toString());
   }

   @Test
   public void testReadFastToEnd() {
      String bigContent = "";
      for (int i = 0; i < 1024; i++)
         bigContent += content + '\n';

      CharIterator tmp = new CharIterator(bigContent);
      long slow = TimeUtils.time(tmp::readToEnd);
      tmp = new CharIterator(bigContent);
      long fast = TimeUtils.time(tmp::readFastToEnd);
      assertTrue("readToEnd needed " + slow + "ns, while readFastToEnd needed " + fast + "ns", fast < slow);

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