package de.dk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliProcess<E extends Exception> extends Process {
   private static final Logger LOGGER = LoggerFactory.getLogger(CliProcess.class);

   private UnsafeBufferedSupplier<Process, ? extends E> process;

   private PrintStream responseStream;
   private PrintStream errStream;

   public CliProcess(UnsafeSupplier<Process, ? extends E> source,
                     PrintStream responseStream,
                     PrintStream errStream) throws E {

      this.process = new UnsafeBufferedSupplier<>(source);
      this.responseStream = responseStream;
      this.errStream = errStream;

      init();
   }

   public static void redirect(InputStream in, PrintStream out) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      try {
         Util.loop(reader::readLine, line -> line != null, out::println);
      } catch (IOException e) {
         LOGGER.trace("Exception reading the Inputstream", e);
      } finally {
         try {
            reader.close();
         } catch (IOException e) {
            LOGGER.trace("Could not close cli output inputstream", e);
         }
      }
   }

   private void init() throws E {
      Process process = this.process.update();
      if (!process.isAlive())
         throw new IllegalStateException("Process was already terminated.");

      new Thread(() -> redirect(process.getInputStream(), responseStream)).start();
      new Thread(() -> redirect(process.getErrorStream(), errStream)).start();
   }

   public void execute(String... commands) throws E {
      PrintStream cli = new PrintStream(process.get().getOutputStream());
      for (String cmd : commands)
         cli.println(cmd);
   }

   @Override
   public OutputStream getOutputStream() {
      try {
         return process.get().getOutputStream();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public InputStream getInputStream() {
      try {
         return process.get().getInputStream();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public boolean equals(Object obj) {
      try {
         return process.get().equals(obj);
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public InputStream getErrorStream() {
      try {
         return process.get().getErrorStream();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public int waitFor() throws InterruptedException {
      try {
         return process.get().waitFor();
      } catch (Exception e) {
         if (e instanceof InterruptedException)
            throw (InterruptedException) e;

         throw new IllegalStateException(e);
      }
   }

   @Override
   public boolean waitFor(long timeout, TimeUnit unit) throws InterruptedException {
      try {
         return process.get().waitFor(timeout, unit);
      } catch (Exception e) {
         if (e instanceof InterruptedException)
            throw (InterruptedException) e;

         throw new IllegalStateException(e);
      }
   }

   @Override
   public int exitValue() {
      try {
         return process.get().exitValue();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public void destroy() {
      try {
         process.get().destroy();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public Process destroyForcibly() {
      try {
         return process.get().destroyForcibly();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public boolean isAlive() {
      try {
         return process.get().isAlive();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public int hashCode() {
      try {
         return process.get().hashCode();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public String toString() {
      try {
         return process.get().toString();
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }
}