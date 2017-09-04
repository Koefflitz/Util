package de.dk.util.timing;

import java.util.concurrent.TimeoutException;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class TimedExecutor {
   private int index = 0;
   private final TimedRunnable[] tasks;
   private final Thread[] threads;

   private long startTime = -1;
   private long timeout;

   public TimedExecutor(long timeout, int threadCount, TimedRunnable[] tasks) {
      this.timeout = timeout;
      this.tasks = tasks;
      this.threads = new Thread[threadCount];
      for (int i = 0; i < threads.length; i++)
         threads[i] = new Thread(this::run);
   }

   public long start() throws InterruptedException, TimeoutException {
      if (startTime == -1)
         this.startTime = System.currentTimeMillis();

      for (Thread thread : threads)
         thread.start();

      try {
         for (Thread thread : threads) {
            thread.join(timeout);
            long delta = System.currentTimeMillis() - startTime;
            if (delta >= timeout)
               throw new TimeoutException();
         }
      } catch (TimeoutException | InterruptedException e) {
         for (Thread thread : threads)
            thread.interrupt();

         throw e;
      }
      return timeout - (System.currentTimeMillis() - startTime);
   }

   public void run() {
      TimedRunnable task;
      while ((task = next()) != null) {
         synchronized (tasks) {
            if (index >= tasks.length)
               return;

            task = tasks[index++];
         }

         try {
            task.run(0);
         } catch (InterruptedException e) {
            return;
         }
      }
   }

   private synchronized TimedRunnable next() {
      if (index >= tasks.length)
         return null;

      return tasks[index++];
   }

   public long getTimeout() {
      return timeout;
   }

   public void setTimeout(long timeout) {
      this.timeout = timeout;
   }

   public void setStartTime(long startTime) {
      this.startTime = startTime;
   }

}
