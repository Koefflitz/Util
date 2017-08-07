package de.dk.util;

import static de.dk.util.FileUtils.copy;
import static de.dk.util.FileUtils.createTempDir;
import static de.dk.util.FileUtils.delete;
import static de.dk.util.FileUtils.getContentOf;
import static de.dk.util.FileUtils.writeContentOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class FileUtilsTest {
   private static final Logger LOGGER = LoggerFactory.getLogger(FileUtilsTest.class);
   private static final String FILE_CONTENT = "This file is created by a unit test and should be deleted.";

   private File workingDir;

   public FileUtilsTest() {

   }

   private static void writeTo(File file) {
      file.getParentFile().mkdirs();
      try {
         file.createNewFile();
      } catch (IOException e) {
         fail(e.getMessage());
      }
      FileWriter writer = null;
      try {
         writer = new FileWriter(file);
         writer.write(FILE_CONTENT);
      } catch (IOException e) {
         fail(e.getMessage());
      } finally {
         try {
            if (writer != null)
               writer.close();
         } catch (IOException e) {
            LOGGER.warn("Filewriter could not be closed: " + e.getMessage());
         }
      }
   }

   @Before
   public void init() {
      this.workingDir = createTempDir();
      if (workingDir == null || !workingDir.exists() || !workingDir.isDirectory())
         throw new IllegalStateException("Could not create working dir");
   }

   @Test
   public void testTempDir() {
      try {
         File tempdir = createTempDir();
         assertTrue(tempdir.exists());
         assertTrue(tempdir.isDirectory());
      } catch (IllegalStateException e) {
         fail(e.getMessage());
      }
   }

   @Test
   public void testDelete() {
      File subDir = new File(workingDir, "subfolder");
      if (!subDir.mkdir())
         fail("Could not create directory");

      File file = new File(workingDir, "test.txt");
      File subFile = new File(subDir, "test.txt");
      writeTo(file);
      writeTo(subFile);
      delete(workingDir);
      assertFalse(file.exists());
      assertFalse(subFile.exists());
      assertFalse(subDir.exists());
      assertFalse(workingDir.exists());
   }

   @Test
   public void testGetContentOf() {
      File file = new File(workingDir, "testfile.txt");
      writeTo(file);
      try {
         assertEquals(FILE_CONTENT, getContentOf(file));
      } catch (IOException e) {
         fail(e.getMessage());
      }
   }

   @Test
   public void testWriteContentOf() {
      File file = new File(workingDir, "testFile.txt");
      writeTo(file);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
         writeContentOf(file, out);
      } catch (IOException e) {
         fail(e.getMessage());
      }
      assertEquals(FILE_CONTENT, out.toString());
   }

   @Test
   public void testCopySimple() {
      File source = new File(workingDir, "testFile.txt");
      writeTo(source);
      File target = new File(workingDir, "targetTestFile.txt");
      try {
         copy(source, target);
      } catch (IOException e) {
         fail(e.getMessage());
      }

      try {
         assertEquals(FILE_CONTENT, getContentOf(target));
      } catch (IOException e) {
         fail(e.getMessage());
      }
   }

   @Test
   public void testCopyComplex() {
      File source = new File(workingDir, "testDir");
      File sub = new File(source, "subTestDir");
      File fSub = new File(sub, "testFile.txt");
      File f = new File(source, "testFile.txt");

      writeTo(f);
      writeTo(fSub);

      File target = new File(workingDir, "targetTestDir");
      try {
         copy(source, target);
      } catch (IOException e) {
         fail(e.getMessage());
      }

      File resultRoot = new File(target, source.getName());
      File resultSub = new File(resultRoot, sub.getName());
      File resultF = new File(resultRoot, f.getName());
      File resultFSub = new File(resultSub, fSub.getName());

      assertTrue(resultRoot.exists());
      assertTrue(resultSub.exists());
      assertTrue(resultF.exists());
      assertTrue(resultFSub.exists());

      try {
         assertEquals(FILE_CONTENT, getContentOf(resultF));
         assertEquals(FILE_CONTENT, getContentOf(resultFSub));
      } catch (IOException e) {
         fail(e.getMessage());
      }
   }

   @After
   public void cleanUp() {
      if (workingDir == null) {
         LOGGER.warn("Working dir was null when intended to delete it.");
      } else {
         delete(workingDir);
         if (workingDir.exists())
            LOGGER.warn("Could not delete working dir: " + workingDir.getAbsolutePath());
      }
   }

}
