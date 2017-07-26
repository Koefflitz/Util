package de.dk.util.text;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

import de.dk.util.FileUtils;
import de.dk.util.StringUtils;

/**
 * A class to iterate over each <code>char</code> of a Sequence of chars, e.g. a <code>String</code>.
 *
 * @author David Koettlitz
 * <br>Erstellt am 16.12.2016
 */
public class CharIterator implements Iterator<Character> {
   protected char[] content;
   protected int index = 0;

   protected int lineNumber;
   protected int columnNumber;

   protected boolean closed = false;

   /**
    * Creates a new CharIterator, that contains the given <code>content</code>.
    *
    * @param content The content of the created iterator.
    *
    * @throws NullPointerException if <code>content</code> is <code>null</code>.
    */
   public CharIterator(String content) {
      this(Objects.requireNonNull(content).toCharArray());
   }

   /**
    * Creates a new CharIterator, that can iterate over the given char-array.
    * Remember, that the char-array bound to this iterator will be final.
    *
    * @param content The content of the created iterator.
    *
    * @throws NullPointerException if <code>content</code> is <code>null</code>
    */
   public CharIterator(char[] content) {
      this.content = Objects.requireNonNull(content);
   }

   /**
    * Creates a new CharIterator, that contains the given bytes
    * decoded by the <code>String</code> constructor.
    *
    * @param content The content of the created iterator.
    *
    * @throws NullPointerException if <code>content</code> is <code>null</code>
    */
   public CharIterator(byte[] content) {
      this.content = new String(Objects.requireNonNull(content)).toCharArray();
   }

   /**
    * Creates a CharIterator that iterates over the given <code>file</code>.
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
   public static CharIterator from(File file) throws IOException {
      return new CharIterator(FileUtils.getContentOf(Objects.requireNonNull(file)));
   }

   /**
    * Creates a CharIterator that starts at the <code>from</code> iterator until the <code>to</code> operator.
    *
    * @param from the iterator from whichs position to start
    * @param to the iterator to end at
    *
    * @return an iterator that starts at the position of <code>from</code> and iterates until the position of <code>to</code>.
    *
    * @throws NullPointerException if either <code>from</code> or <code>to</code> is <code>null</code>.
    * @throws IllegalArgumentException if one of the iterators were already closed
    * @throws IllegalStateException if the <code>from</code> iterator was further than the <code>to</code> iterator<br>
    *                                  or the two iterators have different contents
    */
   public static CharIterator getIterator(CharIterator from, CharIterator to) {
      if (Objects.requireNonNull(from).closed || Objects.requireNonNull(to).closed)
         throw new IllegalArgumentException("At least one of the iterators had been closed already.");

      if (from.index > to.index)
         throw new IllegalStateException("The \"from\" iterator was already further than the \"to\" iterator");

      String fromContent = new String(from.content);
      String toContent = new String(to.content);

      if (!fromContent.equals(toContent))
         throw new IllegalArgumentException("The iterators had different contents.");

      CharIterator result = new CharIterator(fromContent.substring(from.index, to.index));
      result.lineNumber = from.lineNumber;
      result.columnNumber = from.columnNumber;
      return result;
   }

   /**
    * Moves forward until the next character, that is not a whitespace character.
    */
   public String skipNextWhiteSpaces() {
      String result = "";
      while (hasNext()) {
         if (StringUtils.isBlank(peek()))
            result += next();
         else
            return result;
      }
      return result;
   }

   /**
    * Moves forward until the next whitespace character and returns the string
    * exclusive the whitespace character.
    *
    * @return The string from the current position to exclusive the next whitespace character.
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readUntilNextWhiteSpace() {
      return readUntil(StringUtils::isBlank);
   }

   /**
    * Reads the rest of the current line.
    *
    * @return the string from the current position till the end of the current line exclusive the linebreak char(s).
    * If the iterator is at the end of the string <code>null</code> will be returned.
    * If the iterator is at the end of the line but not at the end of the whole string
    * an empty string will be returned.
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readLine() {
      if (!hasNext())
         throw new IllegalStateException("This iterator was already closed.");

      String line = "";
      for (char c = next(); hasNext(); c = next()) {
         if (c == '\n')
            return line;

         if (c == '\r') {
            if (peek() == '\n')
               next();

            return line;
         }

         line += c;
      }
      return line;
   }

   /**
    * Get the next line without moving the iterator further.
    *
    * @return the rest of the current line
    * if the iterator is at the end of the line, so that the next returned char would be a line break char
    * an empty string is returned
    */
   public String peekLine() {
      if (!hasNext())
         throw new IllegalStateException("This iterator was already closed.");

      String line = "";
      CharIterator tmp = branch();
      for (char c = tmp.next(); tmp.hasNext(); c = tmp.next()) {
         if (c == '\n' || c == '\r')
            return line;

         line += c;
      }
      return line;
   }

   /**
    * Moves forward until a character matches the given predicate and returns the read string.
    * Stops right after the matched character.
    *
    * @param limit A predicate to limit the read-process.
    *
    * @return The read String until exclusive the character, that matches the predicate.
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readUntil(Predicate<Character> limit) {
      if (!hasNext())
         throw new IllegalStateException("This iterator was already closed.");

      String read = "";
      boolean quote = false;
      for (char c = next(); hasNext(); c = next()) {
         if (!quote && limit.test(c))
            return read;

         read += c;
         quote = c == '"' ^ quote;
      }
      return read;
   }

   /**
    * Moves forward until a character matches the given predicate and returns the read string.
    * Stops right before the matched character.
    *
    * @param limit A predicate to limit the read-process.
    *
    * @return The read String until exclusive the character, that matches the predicate.
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readUntilBefore(Predicate<Character> limit) {
      if (!hasNext())
         throw new IllegalStateException("This iterator was already closed.");

      String read = "";
      boolean quote = false;
      for (char c = peek(); hasNext(); c = peek()) {
         if (!quote && limit.test(c))
            return read;

         read += c;
         next();
         quote = c == '"' ^ quote;
      }
      return read;
   }

   /**
    * Moves forward until the given character and returns the read string.
    * Stops right after the matched character.
    *
    * @param character The character after which the iterator should stop.
    *
    * @return The read String until exclusive the character.
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readUntil(char character) {
      return readUntil(c -> c == character);
   }

   /**
    * Moves forward until the given character and returns the read string.
    * Stops right before the matched character.
    *
    * @param character The character after which the iterator should stop.
    *
    * @return The read String until exclusive the character.
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readUntilBefore(char character) {
      return readUntilBefore(c -> c == character);
   }

   /**
    * Moves forward until the given pattern is reached and returns the read String.
    *
    * @param pattern The pattern to stop after.
    *
    * @return The read String until exclusive the pattern.
    *
    * @throws NoSuchElementException if the remaining string of this iterator doesn't contain the given <code>pattern</code>.
    */
   public String readUntil(String pattern) throws NoSuchElementException {
      if (!hasNext())
         throw new NoSuchElementException("The iterator was already at the end");

      String read = "";
      String readPattern = "";
      boolean quote = false;

      for (char c = next(); hasNext(); c = next()) {
         if (!quote && (c == pattern.charAt(readPattern.length()))) {
            readPattern += c;
            if (readPattern.length() == pattern.length())
               return read;

         } else {
            read += readPattern + c;
            readPattern = "";
         }
         quote = c == '"' ^ quote;
      }
      throw new NoSuchElementException("The string does not contain the pattern \"" + pattern + "\"");
   }

   /**
    * Reads until the end of this iterator.
    * If you want to get the remaining string,
    * but dont want the iterator to get to the end use the {@link #getRemainingString()} method.
    *
    * @return the string from the current position to the end, that is equal to {@link #getRemainingString()}
    */
   public String readToEnd() {
      String result = getRemainingString();
      while (hasNext())
         next();

      return result;
   }

   /**
    * Get the remaining string of this iterator and closes this iterator.
    * This operation is a little faster than {@link #readToEnd()}, because it only gets the remaining string
    * without gathering any meta information. That's why this iterator is closed afterwards.
    * If you don't want this iterator to move to the end, but you need the remaining string
    * use the {@link #getRemainingString()} method.
    *
    * @return the remaining string of this iterator
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public String readFastToEnd() {
      String result = getRemainingString();
      close();
      return result;
   }

   /**
    * Allows access to the next character of this iterator without moving the iterator forward.
    *
    * @return The next character of this iteratior.
    *
    * @throws NoSuchElementException If the iterator has no more characters.
    */
   public char peek() throws NoSuchElementException {
      if (!hasNext())
         throw new NoSuchElementException("Iterator already at the end.");

      return content[index];
   }

   @Override
   public Character next() throws NoSuchElementException {
      if (!hasNext())
         throw new NoSuchElementException("Iterator already at the end.");

      char c = content[index++];
      if (c == '\n' || c == '\r') {
         lineNumber++;
         columnNumber = 0;
      } else {
         columnNumber++;
      }
      return c;
   }

   @Override
   public boolean hasNext() {
      return !closed && index < content.length;
   }

   /**
    * Creates an iterator as a branch of this iterator at the exact same current position.
    * The branch and this iterator are fully independent from each other.
    * Touching any of the branches will not influence any other branch.
    *
    * @return A {@link CharIterator}, that starts at the same position of this iterator.
    *
    * @throws IllegalStateException if the iterator has been closed
    */
   public CharIterator branch() {
      if (closed)
         throw new IllegalStateException("This iterator was already closed.");

      CharIterator branch = new CharIterator(content);
      branch.index = index;
      branch.columnNumber = columnNumber;
      branch.lineNumber = lineNumber;
      return branch;
   }

   /**
    * Moves this iterator to the position of the given <code>iterator</code>.
    *
    * @param iterator The iterator to move to
    *
    * @throws IllegalStateException if this iterator or the given <code>iterator</code> has already been closed
    * @throws IllegalArgumentException if the given <code>iterator</code> has another content than this
    * @throws NullPointerException if the given <code>iterator</code> is <code>null</code>.
    */
   public CharIterator moveTo(CharIterator iterator) {
      Objects.requireNonNull(iterator);
      if (closed)
         throw new IllegalStateException("This iterator was already closed.");
      if (iterator.closed)
         throw new IllegalStateException("The iterator was already closed.");

      if (!Arrays.equals(content, iterator.content))
         throw new IllegalArgumentException("Iterator had another content than this");

      this.index = iterator.index;
      this.lineNumber = iterator.lineNumber;
      this.columnNumber = iterator.columnNumber;
      return this;
   }

   /**
    * Gives information at which line the current position of this iterator stands.
    *
    * @return The current linenumber
    *
    * @throws IllegalStateException if the iterator has been closed
    */
   public int getLineNumber() {
      if (closed)
         throw new IllegalStateException("This iterator was already closed.");

      return lineNumber;
   }

   /**
    * Get the column number where this iterator is currently positioned.
    * In other words: The index of the next char of the current line.
    *
    * @return The current columnnumber
    *
    * @throws IllegalStateException if the iterator has been closed
    */
   public int getColumnNumber() {
      if (closed)
         throw new IllegalStateException("This iterator was already closed.");

      return columnNumber;
   }

   /**
    * Get the entire content of this iterator.
    * Includes also the already passed characters.
    *
    * @return The string this iterator iterates over.
    */
   public String getContent() {
      if (closed)
         throw new IllegalStateException("This iterator was already closed.");

      return new String(content);
   }

   /**
    * Get the remaining string, without moving the iterator.
    *
    * @return The entire remaining string of this iterator from the current position
    *
    * @throws IllegalStateException if this iterator has been closed
    */
   public String getRemainingString() {
      if (closed)
         throw new IllegalStateException("This iterator was already closed.");

      return new String(content, index, content.length - index);
   }

   /**
    * Get the remaining string as a <code>Substring</code>, without moving the iterator.
    *
    * @return The entire remaining string of this iterator from the current position
    *
    * @throws IllegalStateException if this iterator has been closed
    */
   public Substring getRemainingSubstring() {
      return new Substring(getRemainingString(), index);
   }

   /**
    * Get the substring that is already read by this CharIterator.
    *
    * @return the already read string
    */
   public String getAlreadyReadString() {
      return new String(content, 0, index);
   }

   /**
    * Starts a substringbuilder from the current position of this iterator.
    *
    * @return a substringbuilder from the current position
    */
   public SubstringBuilder startSubstring() {
      return new SubstringBuilder(index);
   }

   /**
    * Get the index of this iterator.
    *
    * @return the current position of this iterator
    *
    * @throws IllegalStateException if this iterator has already been closed
    */
   public int getIndex() {
      if (closed)
         throw new IllegalStateException("This iterator has already been closed.");
      return index;
   }

   /**
    * Informs wether this iterator has been closed. If a <code>CharIterator</code> is closed,
    * most of its methods will throw an <code>IllegalStateException</code>.
    *
    * @return wether this iterator has been closed
    */
   public boolean isClosed() {
      return closed;
   }

   public void close() {
      this.index = content.length;
      this.content = null;
      closed = true;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(content);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      CharIterator other = (CharIterator) obj;
      return Arrays.equals(content, other.content);
   }

   /**
    * Get the whole content with a pointer to the current char
    * @return the whole content with a pointer to the current char
    * or <code>null</code> if this iterator has already been closed
    */
   @Override
   public String toString() {
      if (closed)
         return null;

      String result = "CharIterator {\n";
      if (!hasNext()) {
         result += getContent() + "\n";
         String lastLine = StringUtils.getLastLineOf(getContent());
         for (int i = 0; i < lastLine.length(); i++)
            result += ' ';

         return result + ">\n}";
      }

      String before = getAlreadyReadString();
      String remaining = getRemainingString();
      int lineIndex = peek() == '\n' ? 0 : StringUtils.getLastLineOf(before).length();
      String lineend = StringUtils.getFirstLineOf(remaining);

      result += before + lineend + "\n";
      for (int i = 0; i < lineIndex; i++) {
         char c = before.charAt(before.length() - (lineIndex - i));
         result += c == '\t' ? '\t' : ' ';
      }

      result += peek() == '\n' ? '<' : '^';
      if (remaining.length() > lineend.length())
         result += remaining.substring(lineend.length());

      return result + "\n}";
   }
}