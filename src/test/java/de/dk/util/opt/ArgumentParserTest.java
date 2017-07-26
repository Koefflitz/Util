package de.dk.util.opt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.dk.util.opt.ex.MissingArgumentException;
import de.dk.util.opt.ex.MissingOptionValueException;

public class ArgumentParserTest {
   private static final String ARG_NAME0 = "testarg";
   private static final String ARG_VALUE0 = "argvalue";
   private static final String ARG_NAME1 = "Muttermilch";
   private static final String ARG_VALUE1 = "Butterknilch";

   private static final char OPT_KEY0 = 't';
   private static final String OPT_NAME0 = "testopt";
   private static final String OPT_VALUE0 = "optvalue";
   private static final char OPT_KEY1 = 'm';
   private static final String OPT_LONG_KEY1 = "almi";
   private static final String OPT_NAME1 = "Alpenmilch";
   private static final char OPT_KEY2 = 'w';
   private static final String OPT_NAME2 = "Wachsmalstift";

   @Rule
   public ExpectedException expectedException = ExpectedException.none();

   public ArgumentParserTest() {

   }

   @Test
   public void testSingleArgument() {
      ArgumentParser parser = new ArgumentParserBuilder().addArgument(ARG_NAME0)
                                                         .build();
      ArgumentModel result = parse(parser, ARG_VALUE0);

      assertEquals(ARG_VALUE0, result.iterator().next());
      assertEquals(ARG_VALUE0, result.getArgumentValue(ARG_NAME0));
   }

   @Test
   public void testMultipleArguments() {
      ArgumentParser parser = new ArgumentParserBuilder().addArgument(ARG_NAME0)
                                                         .addArgument(ARG_NAME1)
                                                         .build();

      ArgumentModel result = parse(parser, ARG_VALUE0, ARG_VALUE1);

      Iterator<String> iterator = result.iterator();
      assertEquals(ARG_VALUE0, iterator.next());
      assertEquals(ARG_VALUE0, result.getArgumentValue(ARG_NAME0));
      assertEquals(ARG_VALUE1, iterator.next());
      assertEquals(ARG_VALUE1, result.getArgumentValue(ARG_NAME1));
   }

   @Test
   public void testSingleOption() {
      ArgumentParser parser = new ArgumentParserBuilder().buildOption(OPT_KEY0, OPT_NAME0)
                                                            .setExpectsValue(true)
                                                            .setMandatory(true)
                                                            .build()
                                                         .build();

      ArgumentModel result = parse(parser, "-" + OPT_KEY0, OPT_VALUE0);
      assertTrue(result.isOptionPresent(OPT_KEY0));
      assertEquals(OPT_VALUE0, result.getOptionValue(OPT_KEY0));
   }

   @Test
   public void testMultipleOptions() {
      ArgumentParser parser = new ArgumentParserBuilder().buildOption(OPT_KEY0, OPT_NAME0)
                                                            .setExpectsValue(true)
                                                            .build()
                                                         .buildOption(OPT_KEY1, OPT_NAME1)
                                                            .setLongKey(OPT_LONG_KEY1)
                                                            .build()
                                                         .addOption(OPT_KEY2, OPT_NAME2)
                                                         .build();

      ArgumentModel result = parse(parser, "-" + OPT_KEY0, OPT_VALUE0, "-" + OPT_KEY1);
      assertTrue(result.isOptionPresent(OPT_KEY0));
      assertEquals(OPT_VALUE0, result.getOptionValue(OPT_KEY0));
      assertTrue(result.isOptionPresent(OPT_KEY1));
      assertFalse(result.isOptionPresent(OPT_KEY2));
   }

   @Test
   public void testMultipleOptionsAsOneArgument() {
      ArgumentParser parser = new ArgumentParserBuilder().buildOption(OPT_KEY0, OPT_NAME0)
                                                            .setExpectsValue(true)
                                                            .setMandatory(true)
                                                            .build()
                                                         .buildOption(OPT_KEY1, OPT_NAME1)
                                                            .build()
                                                         .buildOption(OPT_KEY2, OPT_NAME2)
                                                            .setExpectsValue(false)
                                                            .setMandatory(false)
                                                            .build()
                                                         .build();

      ArgumentModel result = parse(parser, "-" + OPT_KEY1 + OPT_KEY2 + OPT_KEY0, OPT_VALUE0);
      assertEquals(OPT_VALUE0, result.getOptionValue(OPT_KEY0));
      assertTrue(result.isOptionPresent(OPT_KEY1));
      assertTrue(result.isOptionPresent(OPT_KEY2));
   }

   @Test
   public void testArgsAndOptions() {
      ArgumentParser parser = new ArgumentParserBuilder().addArgument(ARG_NAME0)
                                                         .addArgument(ARG_NAME1)
                                                         .buildOption(OPT_KEY0, OPT_NAME1)
                                                            .setMandatory(true)
                                                            .build()
                                                         .addOption(OPT_KEY1, OPT_NAME1)
                                                         .addOption(OPT_KEY2, OPT_NAME2)
                                                         .build();

      ArgumentModel result = parse(parser,
                                   "-" + OPT_KEY0,
                                   OPT_VALUE0,
                                   ARG_VALUE0,
                                   "-" + OPT_KEY1,
                                   ARG_VALUE1);

      Iterator<String> iterator = result.iterator();
      assertEquals(ARG_VALUE0, iterator.next());
      assertEquals(ARG_VALUE0, result.getArgumentValue(ARG_NAME0));
      assertEquals(ARG_VALUE1, iterator.next());
      assertEquals(ARG_VALUE1, result.getArgumentValue(ARG_NAME1));
      assertTrue(result.isOptionPresent(OPT_KEY0));
      assertEquals(OPT_VALUE0, result.getOptionValue(OPT_KEY0));
      assertTrue(result.isOptionPresent(OPT_KEY1));
      assertFalse(result.isOptionPresent(OPT_KEY2));
   }

   @Test
   public void testMissingMandatoryArg() throws MissingArgumentException {
      ArgumentParser parser = new ArgumentParserBuilder().addArgument(ARG_NAME0)
                                                         .addArgument(ARG_NAME1)
                                                         .build();
      expectedException.expect(MissingArgumentException.class);
      try {
         parser.parseArguments(ARG_VALUE0);
      } catch (MissingOptionValueException e) {
         fail(e.getMessage());
      }
   }

   @Test
   public void testMissingMandatoryOption() throws MissingArgumentException {
      ArgumentParser parser = new ArgumentParserBuilder().buildOption(OPT_KEY0, OPT_NAME1)
                                                            .setMandatory(true)
                                                            .setExpectsValue(true)
                                                            .build()
                                                         .buildOption(OPT_KEY1, OPT_NAME1)
                                                            .setMandatory(true)
                                                            .build()
                                                         .build();
      expectedException.expect(MissingArgumentException.class);
      try {
         parser.parseArguments("-" + OPT_KEY0, OPT_VALUE0);
      } catch (MissingOptionValueException e) {
         fail(e.getMessage());
      }
   }

   @Test
   public void testSyntax() {
      String expected = String.format("<%s> [<%s>] -%s <%s> [-%s]",
                                      ARG_NAME0,
                                      ARG_NAME1,
                                      OPT_KEY0,
                                      OPT_NAME0,
                                      OPT_KEY1,
                                      OPT_NAME1);

      ArgumentParser parser = new ArgumentParserBuilder().addArgument(ARG_NAME0)
                                                         .buildArgument(ARG_NAME1)
                                                            .setMandatory(false)
                                                            .build()
                                                         .buildOption(OPT_KEY0, OPT_NAME0)
                                                            .setMandatory(true)
                                                            .build()
                                                         .addOption(OPT_KEY1, OPT_NAME1)
                                                         .build();
      assertEquals(expected, parser.syntax());
   }

   private ArgumentModel parse(ArgumentParser parser, String... args) {
      try {
         return parser.parseArguments(args);
      } catch (MissingArgumentException | MissingOptionValueException e) {
         fail(e.getMessage());
      }
      return null;
   }

}