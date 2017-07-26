package de.dk.util.log;

import java.io.OutputStream;

import org.slf4j.Logger;

public class LogOutputStream extends OutputStream {
   private final Logger logger;
   private Level level;

   private final DynamicByteArray buffer = new DynamicByteArray();

   private boolean flushAtLineBreak = true;

   private boolean lineBreakCharDetected = false;

   public LogOutputStream(Logger logger, Level level) {
      this.logger = logger;
      this.level = level;
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

      level.logWith(logger, new String(buffer.getData()));
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

   public boolean isFlushAtLineBreak() {
      return flushAtLineBreak;
   }

   public void setFlushAtLineBreak(boolean flushAtLineBreak) {
      this.flushAtLineBreak = flushAtLineBreak;
   }

}