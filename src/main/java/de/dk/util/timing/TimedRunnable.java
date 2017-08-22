package de.dk.util.timing;

/**
 * A Runnable that runs not longer than a specified timeout
 *
 * @author David Koettlitz
 * <br>Erstellt am 06.07.2017
 */
@FunctionalInterface
public interface TimedRunnable {
   /**
    * Does stuff and definetely returns before the specified timeout is reached.
    *
    * @param timeout The timeout in millis
    *
    * @throws InterruptedException if the thread is interrupted.
    */
   public void run(long timeout) throws InterruptedException;
}