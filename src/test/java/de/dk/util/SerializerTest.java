package de.dk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class SerializerTest {
   private static final int NUMBER = 8;
   private static final String CONTENT = "Milkflip!";

   private SimpleSerializer serializer;

   public SerializerTest() {

   }

   @Before
   public void initSerialization() {
      this.serializer = new SimpleSerializer();
   }

   @Test
   public void testSerialization() {
      for (int i = 0; i < 3; i++) {

         TestObject object = new TestObject(NUMBER, CONTENT);
         byte[] bytes = null;
         try {
            bytes = serializer.serialize(object);
         } catch (IOException e) {
            fail(e.getMessage());
         }
         Object result = null;
         try {
            result = serializer.deserialize(bytes);
         } catch (ClassNotFoundException | IOException e) {
            fail(e.getMessage());
         }
         assertEquals(object, result);

      }
   }

   private static class TestObject implements Serializable {
      private static final long serialVersionUID = -914050437159650323L;

      private int number;
      private String content;

      public TestObject(int number, String content) {
         this.number = number;
         this.content = content;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
         result = prime * result + this.number;
         return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         TestObject other = (TestObject) obj;
         if (this.content == null) {
            if (other.content != null)
               return false;
         } else if (!this.content.equals(other.content))
            return false;
         if (this.number != other.number)
            return false;
         return true;
      }
   }
}
