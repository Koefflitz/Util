package de.dk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Contains some static methods that are about files.
 *
 * @author David Koettlitz
 * <br>Erstellt am 30.08.2016
 */
public final class FileUtils {
   private static URI sourceLocation;

   private FileUtils() {}

   /**
    * Locates the codesource of the class.
    *
    * @param source The source to be located
    * @return The URI to the source
    *
    * @throws SecurityException If a security manager permits this action
    */
   public static URI locateSource(Class<?> source) throws SecurityException {
      if (sourceLocation != null)
         return sourceLocation;

      try {
         sourceLocation = source.getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI();
      } catch (URISyntaxException e) {
         throw new InternalError("Internal library provided invalid object.", e);
      }
      return sourceLocation;
   }

   /**
    * Locates the codesource of the calling class.
    *
    * @return The URI to the source
    *
    * @throws SecurityException if a security manager doesn't allow this action
    */
   public static URI locateSource() throws SecurityException {
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
         throw new InternalError("The method got called by a class, that cannot be found", e);
      }
   }

   /**
    * Reads the files content.
    *
    * @param file The file to read the content from
    *
    * @return The conent of the file
    *
    * @throws IOException If an I/O error occurs
    */
   public static byte[] getContentOf(File file) throws IOException {
      long length = file.length();
      if (length > Integer.MAX_VALUE)
         throw new IOException("File too large (" + length + "bytes).");

      ByteArrayOutputStream out = new ByteArrayOutputStream(DMath.cutToInt(file.length()));
      writeContentOf(file, out);
      return out.toByteArray();
   }

   /**
    * Reads the files content and passes it in pieces to the consumer.
    *
    * @param file the file to read
    * @param out The OutputStream to write the read data to
    *
    * @throws IOException if there are problems reading the file
    */
   public static void writeContentOf(File file, OutputStream out) throws IOException {
      InputStream input = new FileInputStream(file);
      byte[] piece = new byte[DMath.cutToInt(file.length())];

      try {
         for (int readLength = input.read(piece); readLength > 0; readLength = input.read(piece))
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
    * @throws IOException If an I/O error occures
    */
   public static void copy(File source, File target) throws IOException {
      if (source.isDirectory()) {
         File result = new File(target, source.getName());
         result.mkdirs();
         File[] children = source.listFiles();
         if (children == null) {
            String msg = "Could not access content of " + source.getAbsolutePath();
            throw new IOException(msg);
         }
         for (File f : children)
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
    * @throws IOException If an I/O error occurs
    */
   public static void delete(File file) throws IOException {
      if (file.isDirectory()) {
         File[] children = file.listFiles();
         if (children == null) {
            String msg = "Could not access content of " + file.getAbsolutePath();
            throw new IOException(msg);
         }
         for (File f : children)
            delete(f);
      }
      Files.delete(file.toPath());
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
    * Converts the given <code>file</code> into an URL.
    *
    * @param file The file to be converted to an URL.
    *
    * @return An URL that locates the given <code>file</code>
    *
    * @throws IllegalStateException If the URL could not be contructed
    */
   public static URL toURL(File file) throws IllegalStateException {
      try {
         return file.toURI()
                    .toURL();
      } catch (MalformedURLException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * Removes the file extension includung the '.'
    * and returns the filename without the extension.
    * In othern words returns a substring of <code>filename</code>
    * until excluding the last appearance of a '.'.
    * If the <code>filename</code> doesn't include a '.'
    * the argument is returned as is.
    *
    * @param filename the filename to remove the extension from
    *
    * @return The filename without the file extension
    *
    * @throws NullPointerException if <code>filename</code> is <code>null</code>
    */
   public static String removeExtension(String filename) throws NullPointerException {
      int dotIndex = Objects.requireNonNull(filename)
                            .lastIndexOf('.');
      if (dotIndex == -1)
         return filename;

      return filename.substring(0, dotIndex);
   }

   /**
    * Removes the file extension includung the '.'
    * and returns the filename without the extension.
    * In othern words returns a substring of the name of the <code>file</code>
    * until excluding the last appearance of a '.'.
    * If the name doesn't include a '.'
    * the name is returned as is.
    *
    * @param file the filename to remove the extension from
    *
    * @return The filename without the file extension
    *
    * @throws NullPointerException if <code>file</code> is <code>null</code>
    */
   public static String getNameWithoutExtension(File file) throws NullPointerException {
      return removeExtension(Objects.requireNonNull(file).getName());
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