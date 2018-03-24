package de.dk.util.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class RedirectingCharIteratorTest extends CharIteratorTest {
   private ByteArrayOutputStream target;

   public RedirectingCharIteratorTest() {

   }

   @BeforeEach
   @Override
   public void init() {
      this.target = new ByteArrayOutputStream();
      this.iterator = new RedirectingCharIterator(content, new PrintStream(target)).setRedirecting(true);
   }

   @Test
   public void testNext() {
      assertEquals("" + iterator.next(), target.toString());
      assertEquals("" + content.charAt(0) + iterator.next(), target.toString());
   }

   @Test
   public void testRead() {
      iterator.readLine();
      assertEquals(line0 + '\n', target.toString());

      iterator.readUntil(line1.charAt(2));
      assertEquals(line0 + '\n' + line1.substring(0, 3), target.toString());
   }

   @Test
   public void testBranchingRedirection() {
      CharIterator branch = iterator.branch();

      branch.readLine();
      assertEquals(0, target.size());

      iterator.moveTo(branch);
      assertEquals(line0 + '\n', target.toString());
   }

   @Test
   public void testPeek() {
      iterator.peek();
      assertEquals(0, target.size());
      iterator.peekLine();
      assertEquals(0, target.size());
   }

}
