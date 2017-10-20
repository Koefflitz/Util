package de.dk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ToStringTest {

   @Test
   public void testPrimititves() {
      test("true", true);
      test("5", (byte) 5);
      test("5", (short) 5);
      test("5", 5);
      test("5", (long) 5);
      test("\"abc\"", "abc");
   }

   @Test
   public void testSimpleObject() {
      String expected = "Simple {\n"
                        + "  string=\"simple string\"\n"
                        + "}";

      test(expected, new Simple());
   }

   @Test
   public void testComplex() {
      String expected = "Complex {\n"
                        + "  bool=true,\n"
                        + "  beit=2,\n"
                        + "  kurz=4,\n"
                        + "  integer=8,\n"
                        + "  lang=16,\n"
                        + "  string=\"String\",\n"
                        + "  array=[1, 2, 3, 4],\n"
                        + "  object=Simple {\n"
                        + "    string=\"simple string\"\n"
                        + "  },\n"
                        + "  empty=null,\n"
                        + "  e=FIRST_ENUM_CONSTANT\n"
                        + "}";

      test(expected, new Complex());
   }

   @Test
   public void testInheritance() {
      String expected = "Extended {\n"
                        + "  simple=Simple {\n"
                        + "    string=\"simple string\"\n"
                        + "  },\n"
                        + "  bool=true,\n"
                        + "  beit=2,\n"
                        + "  kurz=4,\n"
                        + "  integer=8,\n"
                        + "  lang=16,\n"
                        + "  string=\"String\",\n"
                        + "  array=[1, 2, 3, 4],\n"
                        + "  object=Simple {\n"
                        + "    string=\"simple string\"\n"
                        + "  },\n"
                        + "  empty=null,\n"
                        + "  e=FIRST_ENUM_CONSTANT\n"
                        + "}";

      test(expected, new Extended());
   }

   private static void test(String expected, Object object) {
      try {
         assertEquals(expected, ReflectionUtils.toString(object));
      } catch (IllegalAccessException e) {
         fail(e.getMessage());
      }
   }

   private static class Simple {
      @SuppressWarnings("unused")
      private String string = "simple string";
   }

   private static class Complex {
      @SuppressWarnings("unused")
      private boolean bool = true;
      @SuppressWarnings("unused")
      private byte beit = 2;
      @SuppressWarnings("unused")
      private short kurz = 4;
      @SuppressWarnings("unused")
      private int integer = 8;
      @SuppressWarnings("unused")
      private long lang = 16;
      @SuppressWarnings("unused")
      private String string = "String";
      @SuppressWarnings("unused")
      private int[] array = new int[] {1, 2, 3, 4};
      @SuppressWarnings("unused")
      private Simple object = new Simple();
      @SuppressWarnings("unused")
      private Complex empty = null;
      @SuppressWarnings("unused")
      private Enum e = Enum.FIRST_ENUM_CONSTANT;
   }

   private static class Extended extends Complex {
      @SuppressWarnings("unused")
      private Simple simple = new Simple();
   }

   private static enum Enum {
      FIRST_ENUM_CONSTANT,
      SECOND_ENUM_CONSTANT;
   }

}
