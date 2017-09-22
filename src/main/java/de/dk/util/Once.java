package de.dk.util;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Once {
   public static RunnableOnce of(Runnable action) {
      return new RunnableOnce(action);
   }

   public static <T> ConsumerOnce<T> of(Consumer<T> action) {
      return new ConsumerOnce<>(action);
   }

   public static <T, U> BiConsumerOnce<T, U> of(BiConsumer<T, U> action) {
      return new BiConsumerOnce<>(action);
   }

   public static class RunnableOnce implements Runnable {
      private final Runnable action;
      private boolean executed = false;

      public RunnableOnce(Runnable action) {
         this.action = Objects.requireNonNull(action);
      }

      @Override
      public void run() {
         if (executed)
            return;

         executed = true;
         action.run();
      }

      public boolean isExecuted() {
         return executed;
      }
   }

   public static class ConsumerOnce<T> implements Consumer<T> {
      private final Consumer<T> action;
      private boolean executed = false;

      public ConsumerOnce(Consumer<T> action) {
         this.action = Objects.requireNonNull(action);
      }

      @Override
      public void accept(T t) {
         if (executed)
            return;

         executed = true;
         action.accept(t);
      }

      public boolean isExecuted() {
         return executed;
      }
   }

   public static class BiConsumerOnce<T, U> implements BiConsumer<T, U> {
      private final BiConsumer<T, U> action;
      private boolean executed = false;

      public BiConsumerOnce(BiConsumer<T, U> action) {
         this.action = Objects.requireNonNull(action);
      }

      @Override
      public void accept(T t, U u) {
         if (executed)
            return;

         executed = true;
         action.accept(t, u);
      }

      public boolean isExecuted() {
         return executed;
      }
   }
}
