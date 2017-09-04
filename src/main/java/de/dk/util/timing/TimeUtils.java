package de.dk.util.timing;

import java.util.concurrent.TimeoutException;

public final class TimeUtils {

   private TimeUtils() {}

   /**
    * Processes the time to run the {@link Runnable}.
    *
    * @param r The runnable to be timed.
    * @return The time in nanoseconds the runnable took to run.
    */
   public static long time(Runnable r) {
      long nanos = System.nanoTime();
      r.run();
      return System.nanoTime() - nanos;
   }

   /**
    * Runs all of the specified <code>runnables</code> sequentially
    * in maximal <code>timeout</code> milliseconds. The <code>timeout</code>
    * is the timeout for all of the <code>runnables</code> together, not each!
    * If all of the <code>runnables</code> could be run within the given <code>timeout</code>
    * the remaining time of the timeout is returned.
    * Othwerwise a <code>TimeoutException</code> is thrown
    *
    * @param timeout The time in which all of the <code>runnables</code> have to be finished running.
    * @param runnables The runnables to be run
    *
    * @return The remaining time in milliseconds of the <code>timeout</code>. In other words:
    * The <code>timeout</code> minus the time it took to execute all <code>runnables</code>.
    *
    * @throws TimeoutException If the <code>runnables</code> didn't finish running
    * within the specified <code>timeout</code>
    *
    * @throws InterruptedException If one of the <code>runnables</code> throws an <code>InterruptedException</code>
    */
   public static long run(long timeout, TimedRunnable... runnables) throws TimeoutException, InterruptedException {
      long start = System.currentTimeMillis();
      for (TimedRunnable runnable : runnables) {
         runnable.run(timeout);
         long delta = System.currentTimeMillis() - start;
         if (delta >= timeout)
            throw new TimeoutException();
      }
      return timeout;
   }

   /**
    * Runs all of the specified <code>runnables</code> parallel
    * in maximal <code>timeout</code> milliseconds using <code>threadCount</code> threads.
    * The <code>timeout</code> is the timeout for all of the <code>runnables</code> together,
    * not each! If all of the <code>runnables</code> could be run within the given
    * <code>timeout</code> the remaining time of the timeout is returned.
    * Othwerwise a <code>TimeoutException</code> is thrown
    *
    * @param timeout The time in which all of the <code>runnables</code> have to be finished running.
    * @param threadCount The number of threads to create for executing the <code>runnables</code>
    * @param runnables The runnables to be run
    *
    * @return The remaining time in milliseconds of the <code>timeout</code>. In other words:
    * The <code>timeout</code> minus the time it took to execute all <code>runnables</code>.
    *
    * @throws TimeoutException If the <code>runnables</code> didn't finish running
    * within the specified <code>timeout</code>
    * @throws InterruptedException If the invoking thread is interrupted while executing
    */
   public static long runParallel(long timeout,
                                  int threadCount,
                                  TimedRunnable... runnables) throws InterruptedException,
                                                                     TimeoutException {
      long start = System.currentTimeMillis();
      TimedExecutor executor = new TimedExecutor(timeout, threadCount, runnables);
      executor.setStartTime(start);
      return executor.start();
   }

}