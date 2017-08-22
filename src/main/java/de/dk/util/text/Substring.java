package de.dk.util.text;

/**
 * Model of a substring of a larger string.
 * Contains a string and the begin index of the string inside the parent string.
 *
 * @author David Koettlitz
 * <br>Erstellt am 23.03.2017
 */
public class Substring {
   private String value;
   private final int beginIndex;

   /**
    * Creates a new <code>Substring</code> with a <code>value</code> and the <code>beginIndex</code>
    * of this string in the parent string.
    *
    * @param value the value of the substring
    * @param beginIndex the begin index of the substring in the parent string
    */
   public Substring(String value, int beginIndex) {
      if (beginIndex < 0)
         throw new StringIndexOutOfBoundsException(beginIndex);

      this.value = value;
      this.beginIndex = beginIndex;
   }

   /**
    * Creates a new <code>Substring</code>.
    *
    * @param parent the parent string that the new string is a substring of.
    * @param beginIndex the begin index of the substring in the parent string
    *
    * @return the new <code>Substring</code>
    */
   public static Substring of(String parent, int beginIndex) {
      return new Substring(parent.substring(beginIndex), beginIndex);
   }

   /**
    * Creates a new <code>Substring</code>.
    *
    * @param parent the parent string that the new string is a substring of.
    * @param beginIndex the begin index of the substring in the parent string
    * @param endIndex the end index of the substring in the parent string
    *
    * @return the new <code>Substring</code>
    */
   public static Substring of(String parent, int beginIndex, int endIndex) {
      return new Substring(parent.substring(beginIndex, endIndex), beginIndex);
   }

   /**
    * Inserts this substring at the <code>beginIndex</code> into the given <code>string</code>.
    * All characters with an index that is &gt;= the <code>beginIndex</code>
    * of this substring will be shifted to the right.
    *
    * @param string the string to insert this substring into
    *
    * @return the <code>string</code> with this substring in it
    */
   public String insertInto(String string) {
      if (string.length() < beginIndex) {
         String msg = String.format("The string \"%s\" was too short to conatin this substring \"%s\"", string, value);
         throw new IllegalArgumentException(msg);
      }

      String result = string.substring(0, beginIndex) + value;
      return string.length() == beginIndex ? result : result + string.substring(beginIndex);
   }

   /**
    * Inserts this substring at the <code>beginIndex</code> into the given <code>string</code>.
    * Overwrites the content of the <code>string</code> that is in place if this substring.
    *
    * @param string the string to insert this substring into
    *
    * @return the <code>string</code> with this substring in it
    *
    * @throws StringIndexOutOfBoundsException if the substrings <code>beginIndex</code> is &gt; the strings length
    */
   public String overwriteTo(String string) {
      if (string.length() < beginIndex)
         throw new StringIndexOutOfBoundsException(beginIndex);

      String result = string.substring(0, beginIndex) + value;
      return string.length() <= getEndIndex() ? result : result + string.substring(getEndIndex());
   }

   /**
    * Get the actual string value of this substring
    *
    * @return the actual string value of this substring
    */
   public String getValue() {
      return value;
   }

   /**
    * Set the actual string value of this substring.
    *
    * @param value this actual string value of this substring
    */
   public void setValue(String value) {
      this.value = value;
   }

   /**
    * Append a string to the end of this substring
    *
    * @param string the string to be appended
    */
   public void append(String string) {
      value += string;
   }

   /**
    * Append a char to the end of this substring
    *
    * @param c the char to be appended
    */
   public void append(char c) {
      value += c;
   }

   /**
    * Get the index of the first char of this substring in the parent string
    *
    * @return the begin index of this substring
    */
   public int getBeginIndex() {
      return beginIndex;
   }

   /**
    * Get the index of after the last char of this substring in the parent string.
    *
    * @return the end index
    */
   public int getEndIndex() {
      return beginIndex + value.length();
   }

   /**
    * Get the length of this substring
    *
    * @return the length of this substring
    */
   public int length() {
      return value.length();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.beginIndex;
      result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
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
      Substring other = (Substring) obj;
      if (this.beginIndex != other.beginIndex)
         return false;
      if (this.value == null) {
         if (other.value != null)
            return false;
      } else if (!this.value.equals(other.value))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "Substring {\n"
             + "beginIndex = " + beginIndex + "\n"
             + "content = {\n"
             + value + "\n"
             + "}\n"
             + "}";
   }
}