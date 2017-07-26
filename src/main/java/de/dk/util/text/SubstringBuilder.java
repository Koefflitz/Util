package de.dk.util.text;

import java.util.stream.IntStream;

public class SubstringBuilder implements CharSequence {
   private StringBuilder builder;
   private int beginIndex;

   public SubstringBuilder(int beginIndex) {
      this("", beginIndex);
   }

   public SubstringBuilder(String value, int beginIndex) {
      if (beginIndex < 0)
         throw new StringIndexOutOfBoundsException(beginIndex);

      this.builder = new StringBuilder(value);
      this.beginIndex = beginIndex;
   }

   public Substring build() {
      return new Substring(builder.toString(), beginIndex);
   }

   public void ensureCapacity(int minimumCapacity) {
      builder.ensureCapacity(minimumCapacity);
   }

   public void trimToSize() {
      builder.trimToSize();
   }

   @Override
   public IntStream chars() {
      return builder.chars();
   }

   public SubstringBuilder append(Object obj) {
      builder.append(obj);
      return this;
   }

   public void setLength(int newLength) {
      builder.setLength(newLength);
   }

   public SubstringBuilder append(String str) {
      builder.append(str);
      return this;
   }

   public SubstringBuilder append(StringBuffer sb) {
      builder.append(sb);
      return this;
   }

   @Override
   public IntStream codePoints() {
      return builder.codePoints();
   }

   public SubstringBuilder append(CharSequence s) {
      builder.append(s);
      return this;
   }

   public SubstringBuilder append(CharSequence s, int start, int end) {
      builder.append(s, start, end);
      return this;
   }

   @Override
   public char charAt(int index) {
      return builder.charAt(index);
   }

   public SubstringBuilder append(char[] str) {
      builder.append(str);
      return this;
   }

   public SubstringBuilder append(char[] str, int offset, int len) {
      builder.append(str, offset, len);
      return this;
   }

   public SubstringBuilder append(boolean b) {
      builder.append(b);
      return this;
   }

   public SubstringBuilder append(char c) {
      builder.append(c);
      return this;
   }

   public SubstringBuilder append(int i) {
      builder.append(i);
      return this;
   }

   public SubstringBuilder append(long lng) {
      builder.append(lng);
      return this;
   }

   public SubstringBuilder append(float f) {
      builder.append(f);
      return this;
   }

   public SubstringBuilder append(double d) {
      builder.append(d);
      return this;
   }

   public int codePointAt(int index) {
      return builder.codePointAt(index);
   }

   public SubstringBuilder appendCodePoint(int codePoint) {
      builder.appendCodePoint(codePoint);
      return this;
   }

   public SubstringBuilder delete(int start, int end) {
      builder.delete(start, end);
      return this;
   }

   public SubstringBuilder deleteCharAt(int index) {
      builder.deleteCharAt(index);
      return this;
   }

   public SubstringBuilder replace(int start, int end, String str) {
      builder.replace(start, end, str);
      return this;
   }

   public SubstringBuilder insert(int index, char[] str, int offset, int len) {
      builder.insert(index, str, offset, len);
      return this;
   }

   public SubstringBuilder insert(int offset, Object obj) {
      builder.insert(offset, obj);
      return this;
   }

   public int codePointBefore(int index) {
      return builder.codePointBefore(index);
   }

   public SubstringBuilder insert(int offset, String str) {
      builder.insert(offset, str);
      return this;
   }

   public SubstringBuilder insert(int offset, char[] str) {
      builder.insert(offset, str);
      return this;
   }

   public SubstringBuilder insert(int dstOffset, CharSequence s) {
      builder.insert(dstOffset, s);
      return this;
   }

   public SubstringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
      builder.insert(dstOffset, s, start, end);
      return this;
   }

   public SubstringBuilder insert(int offset, boolean b) {
      builder.insert(offset, b);
      return this;
   }

   public int codePointCount(int beginIndex, int endIndex) {
      return builder.codePointCount(beginIndex, endIndex);
   }

   public SubstringBuilder insert(int offset, char c) {
      builder.insert(offset, c);
      return this;
   }

   public SubstringBuilder insert(int offset, int i) {
      builder.insert(offset, i);
      return this;
   }

   public SubstringBuilder insert(int offset, long l) {
      builder.insert(offset, l);
      return this;
   }

   public SubstringBuilder insert(int offset, float f) {
      builder.insert(offset, f);
      return this;
   }

   public SubstringBuilder insert(int offset, double d) {
      builder.insert(offset, d);
      return this;
   }

   public int indexOf(String str) {
      return builder.indexOf(str);
   }

   public int indexOf(String str, int fromIndex) {
      return builder.indexOf(str, fromIndex);
   }

   public int offsetByCodePoints(int index, int codePointOffset) {
      return builder.offsetByCodePoints(index, codePointOffset);
   }

   public int lastIndexOf(String str) {
      return builder.lastIndexOf(str);
   }

   public int lastIndexOf(String str, int fromIndex) {
      return builder.lastIndexOf(str, fromIndex);
   }

   public SubstringBuilder reverse() {
      builder.reverse();
      return this;
   }

   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
      builder.getChars(srcBegin, srcEnd, dst, dstBegin);
   }

   public void setCharAt(int index, char ch) {
      builder.setCharAt(index, ch);
   }

   public String substring(int start) {
      return builder.substring(start);
   }

   @Override
   public CharSequence subSequence(int start, int end) {
      return builder.subSequence(start, end);
   }

   public String substring(int start, int end) {
      return builder.substring(start, end);
   }

   @Override
   public int length() {
      return builder.length();
   }

   @Override
   public boolean equals(Object obj) {
      return builder.equals(obj);
   }

   @Override
   public int hashCode() {
      return builder.hashCode();
   }

   public int capacity() {
      return builder.capacity();
   }

   @Override
   public String toString() {
      return builder.toString();
   }
}