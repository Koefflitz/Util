package de.dk.util.text;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import de.dk.util.FileUtils;

/**
 * A CharIterator which is internally printing the read chars when reading to a <code>PrintStream</code>.
 * Prints a char whenever a read operation, which moves this iterator forward is called.
 *
 * @author David Koettlitz
 * <br>Erstellt am 27.03.2017
 */
public class RedirectingCharIterator extends CharIterator {
   private PrintStream out;
   private boolean redirecting = true;

   /**
    * Creates a new RedirectingCharIterator that iterates over the given <code>content</code>.
    * To specify a printstream as target of the redirection
    * use another constructor of the {@link #setOut(PrintStream)} method.
    *
    * @param content the content to iterate over
    */
   public RedirectingCharIterator(String content) {
      super(content);
   }

   /**
    * Creates a new RedirectingCharIterator that iterates over the given <code>content</code>.
    * To specify a printstream as target of the redirection
    * use another constructor of the {@link #setOut(PrintStream)} method.
    *
    * @param content the content to iterate over
    */
   public RedirectingCharIterator(char[] content) {
      super(content);
   }

   /**
    * Creates a new RedirectingCharIterator that iterates over the given <code>content</code>.
    * To specify a printstream as target of the redirection
    * use another constructor of the {@link #setOut(PrintStream)} method.
    *
    * @param content the content to iterate over
    */
   public RedirectingCharIterator(byte[] content) {
      super(content);
   }

   /**
    * Creates a new RedirectingCharIterator that iterates over the given <code>content</code>
    * and redirects the read chars to <code>out</code>.
    *
    * @param content the content to iterate over
    * @param out the target to redirect to
    */
   public RedirectingCharIterator(String content, PrintStream out) {
      super(content);
      this.out = out;
   }

   /**
    * Creates a new RedirectingCharIterator that iterates over the given <code>content</code>
    * and redirects the read chars to <code>out</code>.
    *
    * @param content the content to iterate over
    * @param out the target to redirect to
    */
   public RedirectingCharIterator(char[] content, PrintStream out) {
      super(content);
      this.out = out;
   }

   /**
    * Creates a new RedirectingCharIterator that iterates over the given <code>content</code>
    * and redirects the read chars to <code>out</code>.
    *
    * @param content the content to iterate over
    * @param out the target to redirect to
    */
   public RedirectingCharIterator(byte[] content, PrintStream out) {
      super(content);
      this.out = out;
   }

   /**
    * Creates a RedirectingCharIterator that iterates over the given <code>file</code>.
    * The content of the file is read once, then the file is closed and given to the iterator.
    * So while iterating over this iterators content the file is not touched
    * and the iterator doesn't know anything about that file.
    *
    * @param file the file whichs content is contained by the iterator
    *
    * @return An iterator containing the <code>file</code>s content
    *
    * @throws IOException if an IOException is thrown while reading the file
    * @throws NullPointerException if the given <code>file</code> is <code>null</code>.
    */
   public static RedirectingCharIterator from(File file) throws IOException {
      return new RedirectingCharIterator(FileUtils.getContentOf(Objects.requireNonNull(file)));
   }

   /**
    * Reads the next char of this iterator and moves forward.
    * Prints the read char to the specified printstream.
    */
   @Override
   public Character next() throws NoSuchElementException {
      char c = super.next();
      if (out != null && redirecting)
         out.print(c);

      return c;
   }

   @Override
   public RedirectingCharIterator moveTo(CharIterator iterator) {
      int before = index;
      super.moveTo(iterator);
      if (out != null && redirecting)
         out.print(Arrays.copyOfRange(content, before, index));

      return this;
   }

   /**
    * Get the printstream to write on.
    *
    * @return the printstream to write on.
    */
   public PrintStream getOut() {
      return out;
   }

   /**
    * Set the printstream to write on.
    *
    * @param out the printstream to write on
    */
   public void setOut(PrintStream out) {
      this.out = out;
   }

   /**
    * Get the flag that switches on or off the redirecting.
    *
    * @return <code>true</code> if redirecting is enabled so this iterator prints to the specified printstream
    * <code>false</code> if it is disabled so this iterator will not print.
    */
   public boolean isRedirecting() {
      return redirecting;
   }

   /**
    * Set the flag that enables/disables the redirection to the specified printstream.
    *
    * @param redirecting Set this to <code>true</code> if all read chars should be redirected
    * to the specified printstream. <code>false</code> to disable the printing.
    *
    * @return this iterator to go on.
    */
   public RedirectingCharIterator setRedirecting(boolean redirecting) {
      this.redirecting = redirecting;
      return this;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (this.redirecting ? 1231 : 1237);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      RedirectingCharIterator other = (RedirectingCharIterator) obj;
      if (this.redirecting != other.redirecting)
         return false;
      return true;
   }

}