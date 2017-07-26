package de.dk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Contains some static methods that are about files.
 *
 * @author David Koettlitz
 * <br/>Erstellt am 30.08.2016
 */
public final class FileUtils {
   private static URI jarDir;

   private FileUtils() {}

   /**
    * Locates the codesource of the class.
    *
    * @param source The source to be located
    * @return The URI to the source
    * @throws IllegalStateException if the URI cannot be parsed (this should not happen
    */
   public static URI locateSource(Class<?> source) throws IllegalStateException {
      if (jarDir != null)
         return jarDir;

      try {
         jarDir = source.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI();
      } catch (URISyntaxException e) {
         throw new IllegalStateException(e);
      }
      return jarDir;
   }

   /**
    * Locates the codesource of the calling class.
    *
    * @return The URI to the source
    *
    * @throws IllegalStateException if the caller class cannot be found.
    */
   public static URI locateSource() throws IllegalStateException {
      String callingClassName = null;

      boolean foundThisMethod = false;
      for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
         String className = se.getClassName();
         String methodName = se.getMethodName();
         if (foundThisMethod) {
             callingClassName = className;
             break;
         } else if (FileUtils.class.getName().equals(className)
                 && "locateSource".equals(methodName)) {

             foundThisMethod = true;
         }
      }

      if (callingClassName == null)
         throw new IllegalStateException("Couldn't find the caller class.");

      try {
         return locateSource(Class.forName(callingClassName));
      } catch (ClassNotFoundException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * Reads the files content.
    *
    * @return The conent of the file
    *
    * @throws IOException
    */
   public static String getContentOf(File file) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream(DMath.cutToInt(file.length()));
      writeContentOf(file, out);
      return out.toString();
   }

   /**
    * Reads the files content and passes it in pieces to the consumer.
    *
    * @param file the file to read
    * @param out The OutputStream to write the read data to
    * @throws IOException if there are problems reading the file
    */
   public static void writeContentOf(File file, OutputStream out) throws IOException {
      InputStream input = new FileInputStream(file);
      byte[] piece = new byte[DMath.cutToInt(file.length())];

      try {
         for (int readLength = input.read(piece); readLength > -1; readLength = input.read(piece))
            out.write(piece, 0, readLength);
      } finally {
         input.close();
      }
   }

   /**
    * Copies the source file to the target file.
    *
    * @param source The source file to be copied.
    * @param target The target file of the copying process. If it does not exist it is created.
    *
    * @throws IOException
    */
   public static void copy(File source, File target) throws IOException {
      if (source.isDirectory()) {
         File result = new File(target, source.getName());
         result.mkdirs();
         for (File f : source.listFiles())
            copy(f, result);
      } else {
         FileOutputStream out;
         if (target.isDirectory()) {
            target.mkdirs();
            out = new FileOutputStream(new File(target, source.getName()));
         } else {
            out = new FileOutputStream(target);
         }
         try {
            writeContentOf(source, out);
         } finally {
            out.close();
         }
      }
   }

   /**
    * Deletes a file recursively. If <code>file</code> is a non empty directory
    * the directorys content will be deleted recursively.
    *
    * @param file The file to be deleted.
    */
   public static void delete(File file) {
      if (file.isDirectory()) {
         for (File f : file.listFiles())
            delete(f);
      }
      file.delete();
   }

   /**
    * Creates a temporary directory.
    *
    * @return the created temporary directory
    *
    * @throws IllegalStateException if there is no tempdir available
    */
   public static File createTempDir() throws IllegalStateException {
      String tmpPath = System.getProperty("java.io.tmpdir");
      if (tmpPath == null)
         throw new IllegalStateException("No tempdir accessible.");

      File result = new File(tmpPath, "" + System.nanoTime());
      if (result.mkdir())
         return result;

      throw new IllegalStateException("Could not create dir at " + tmpPath);
   }

   /**
    * Checks whether the given <code>file</code> is <code>null</code> or has an emtpy path.
    *
    * @param file the file to be checked
    * @return <code>true</code> if the file is <code>null</code> or has an empty path.
    *         <code>false</code> otherwise
    */
   public static boolean isBlank(File file) {
      return file == null ? true : StringUtils.isBlank(file.getAbsolutePath());
   }

}