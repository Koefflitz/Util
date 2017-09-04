package de.dk.util.log;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class LogOutputStream extends OutputStream {
   private final Logger logger;
   private Level level;
   private Charset charset;

   private final DynamicByteArray buffer = new DynamicByteArray();

   private boolean flushAtLineBreak = true;

   private boolean lineBreakCharDetected = false;

   public LogOutputStream(Logger logger, Level level, Charset charset) {
      this.logger = logger;
      this.level = level;
      this.charset = charset == null ? defaultCharset() : charset;
   }


   public LogOutputStream(Logger logger, Level level) {
      this(logger, level, null);
   }

   private static Charset defaultCharset() {
      final String defaultCharset = "UTF-8";
      if (Charset.isSupported(defaultCharset))
         return Charset.forName(defaultCharset);
      else
         return Charset.defaultCharset();
   }

   @Override
   public void write(int b) {
      if (!flushAtLineBreak) {
         buffer.add((byte) b);
         return;
      }

      if (b == '\n') {
         lineBreakCharDetected = false;
         flush();
      } else if (b == '\r') {
         if (lineBreakCharDetected)
            flush();
         else
            lineBreakCharDetected = true;
      } else {
         if (lineBreakCharDetected)
            flush();

         buffer.add((byte) b);
      }
   }

   @Override
   public void flush() {
      if (buffer.isEmpty())
         return;

      level.logWith(logger, new String(buffer.getData(), charset));
      buffer.clear();
   }

   @Override
   public void close() {
      flush();
   }

   public Level getLevel() {
      return level;
   }

   public void setLevel(Level level) {
      this.level = level;
   }

   public Charset getCharset() {
      return charset;
   }


   public void setCharset(Charset charset) {
      this.charset = charset;
   }


   public boolean isFlushAtLineBreak() {
      return flushAtLineBreak;
   }

   public void setFlushAtLineBreak(boolean flushAtLineBreak) {
      this.flushAtLineBreak = flushAtLineBreak;
   }

}
