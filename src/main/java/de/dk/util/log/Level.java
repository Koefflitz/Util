package de.dk.util.log;

import java.util.function.BiConsumer;

import org.slf4j.Logger;

public enum Level {
   INFO(Logger::info),
   ERROR(Logger::error),
   WARN(Logger::warn),
   TRACE(Logger::trace),
   DEBUG(Logger::debug);

   private final BiConsumer<Logger, String> methodMapper;

   private Level(BiConsumer<Logger, String> methodMapper) {
      this.methodMapper = methodMapper;
   }

   public void logWith(Logger logger, String msg) {
      methodMapper.accept(logger, msg);
   }
}