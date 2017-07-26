package de.dk.util.timing;

import java.util.concurrent.TimeoutException;

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
            if ((timeout -= delta) <= 0)
               throw new TimeoutException();

            startTime += delta;
         }
      } catch (TimeoutException | InterruptedException e) {
         for (Thread thread : threads)
            thread.interrupt();

         throw e;
      }
      return timeout;
   }

   public void run() {
      TimedRunnable next;
      synchronized (tasks) {
         if (index >= tasks.length)
            return;

         next = tasks[index++];
      }

      try {
         next.run(0);
      } catch (InterruptedException e) {
         return;
      }
   }

   public void setStartTime(long startTime) {
      this.startTime = startTime;
   }

}