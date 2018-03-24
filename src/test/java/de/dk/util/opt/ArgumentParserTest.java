package de.dk.util.opt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import de.dk.util.opt.ex.MissingArgumentException;
import de.dk.util.opt.ex.MissingOptionValueException;
import de.dk.util.opt.ex.UnexpectedOptionValueException;
import de.dk.util.opt.ex.UnknownArgumentException;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ArgumentParserTest {
   private static final String ARG_NAME0 = "testarg";
   private static final String ARG_VALUE0 = "argvalue";
   private static final String ARG_NAME1 = "Muttermilch";
   private static final String ARG_VALUE1 = "Butterknilch";
   private static final String ARG_NAME2 = "Faltenkrem";
   private static final String ARG_VALUE2 = "-128";

   private static final char OPT_KEY0 = 't';
   private static final String OPT_NAME0 = "testopt";
   private static final String OPT_VALUE0 = "optvalue";
   private static final char OPT_KEY1 = 'm';
   private static final String OPT_LONG_KEY1 = "almi";
   private static final String OPT_VALUE1 = "lecker!";
   private static final String OPT_NAME1 = "Alpenmilch";
   private static final char OPT_KEY2 = 'w';
   private static final String OPT_LONG_KEY2 = "wamasti";
   private static final String OPT_NAME2 = "Wachsmalstift";

   private static final String CMD_NAME0 = "run";

   public ArgumentParserTest() {

   }

   private static ArgumentModel parse(ArgumentParser parser, String... args) {
      try {
         return parser.parseArguments(args);
      } catch (MissingArgumentException
               | MissingOptionValueException
               | UnknownArgumentException
               | UnexpectedOptionValueException e) {
         fail(e.getMessage());
      }
      return null;
   }

   @Test
   public void testSingleArgument() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addArgument(ARG_NAME0)
                                                   .buildAndGet();
      ArgumentModel result = parse(parser, ARG_VALUE0);

      assertEquals(ARG_VALUE0, result.iterator().next());
      assertEquals(ARG_VALUE0, result.getArgumentValue(ARG_NAME0));
   }

   @Test
   public void testMultipleArguments() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addArgument(ARG_NAME0)
                                                   .addArgument(ARG_NAME1)
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, ARG_VALUE0, ARG_VALUE1);

      Iterator<String> iterator = result.iterator();
      assertEquals(ARG_VALUE0, iterator.next());
      assertEquals(ARG_VALUE0, result.getArgumentValue(ARG_NAME0));
      assertEquals(ARG_VALUE1, iterator.next());
      assertEquals(ARG_VALUE1, result.getArgumentValue(ARG_NAME1));
   }

   @Test
   public void testSingleOption() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .buildOption(OPT_KEY0, OPT_NAME0)
                                                      .setMandatory(true)
                                                      .build()
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, "-" + OPT_KEY0, OPT_VALUE0);
      assertTrue(result.isOptionPresent(OPT_KEY0));
      assertEquals(OPT_VALUE0, result.getOptionValue(OPT_KEY0));
   }

   @Test
   public void testSingleLongOption() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .buildOption(OPT_KEY1, OPT_NAME1)
                                                      .setLongKey(OPT_LONG_KEY1)
                                                      .build()
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, "--" + OPT_LONG_KEY1);
      assertTrue(result.isOptionPresent(OPT_KEY1));
   }

   @Test
   public void testMultipleOptions() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addOption(OPT_KEY0, OPT_NAME0)
                                                   .buildOption(OPT_KEY1, OPT_NAME1)
                                                      .setExpectsValue(true)
                                                      .setLongKey(OPT_LONG_KEY1)
                                                      .build()
                                                   .addOption(OPT_KEY2, OPT_NAME2)
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, "-" + OPT_KEY0, "--" + OPT_LONG_KEY1 + "=" +  OPT_VALUE1);
      assertTrue(result.isOptionPresent(OPT_KEY0));
      assertTrue(result.isOptionPresent(OPT_KEY1));
      assertEquals(OPT_VALUE1, result.getOptionValue(OPT_KEY1));
      assertFalse(result.isOptionPresent(OPT_KEY2));
   }

   @Test
   public void testMultipleOptionsAsOneArgument() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .buildOption(OPT_KEY0, OPT_NAME0)
                                                      .setMandatory(true)
                                                      .build()
                                                   .buildOption(OPT_KEY1, OPT_NAME1)
                                                      .build()
                                                   .buildOption(OPT_KEY2, OPT_NAME2)
                                                      .setExpectsValue(false)
                                                      .build()
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, "-" + OPT_KEY1 + OPT_KEY2 + OPT_KEY0, OPT_VALUE0);
      assertEquals(OPT_VALUE0, result.getOptionValue(OPT_KEY0));
      assertTrue(result.isOptionPresent(OPT_KEY1));
      assertTrue(result.isOptionPresent(OPT_KEY2));
   }

   @Test
   public void testSpecialOptions() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addOption(ExpectedOption.NO_KEY, "minus")
                                                   .addOption('-', "minusminus")
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, "-", "--");

      assertTrue(result.isOptionPresent(ExpectedOption.NO_KEY));
      assertTrue(result.isOptionPresent('-'));
   }

   @Test
   public void testMinusMinus() throws MissingArgumentException {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .ignoreUnknown()
                                                   .addArgument(ARG_NAME0)
                                                   .addArgument(ARG_NAME1)
                                                   .buildArgument(ARG_NAME2)
                                                      .setMandatory(true)
                                                      .build()
                                                   .addOption(OPT_KEY0, OPT_NAME0)
                                                   .buildAndGet();

      ArgumentModel result = parse(parser, ARG_VALUE0, "--", ARG_VALUE1, ARG_VALUE2, "-" + OPT_KEY0);
      assertEquals(ARG_VALUE0, result.getArgumentValue(ARG_NAME0));
      assertEquals(ARG_VALUE1, result.getArgumentValue(ARG_NAME1));
      assertEquals(ARG_VALUE2, result.getArgumentValue(ARG_NAME2));
      assertFalse(result.isOptionPresent(OPT_KEY0));

      assertThrows(MissingArgumentException.class, () -> {
            ArgumentModel tmpResult = parser.parseArguments(ARG_VALUE0, ARG_VALUE1, ARG_VALUE2, "-" + OPT_KEY0);
            assertTrue(tmpResult.isOptionPresent(OPT_KEY0));
      });

   }

   @Test
   public void testArgsAndOptions() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addArgument(ARG_NAME0)
                                                   .addArgument(ARG_NAME1)
                                                   .buildOption(OPT_KEY0, OPT_NAME1)
                                                      .setMandatory(true)
                                                      .build()
                                                   .addOption(OPT_KEY1, OPT_NAME1)
                                                   .addOption(OPT_KEY2, OPT_NAME2)
                                                   .buildAndGet();

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
   public void testSimpleCommand() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .buildCommand(CMD_NAME0)
                                                      .setMandatory(true)
                                                      .build()
                                                   .buildAndGet();
      ArgumentModel result = parse(parser, CMD_NAME0);
      assertTrue(result.isCommandPresent(CMD_NAME0));
   }

   @Test
   public void testComplexCommand() {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .buildCommand(CMD_NAME0)
                                                      .setMandatory(true)
                                                      .buildParser()
                                                         .addArgument(ARG_NAME0)
                                                         .addArgument(ARG_NAME1)
                                                         .buildOption(OPT_KEY0, OPT_NAME1)
                                                            .setMandatory(true)
                                                            .build()
                                                         .addOption(OPT_KEY1, OPT_NAME1)
                                                         .addOption(OPT_KEY2, OPT_NAME2)
                                                         .build()
                                                      .build()
                                                   .buildAndGet();

      ArgumentModel result = parse(parser,
                                   CMD_NAME0,
                                   "-" + OPT_KEY0,
                                   OPT_VALUE0,
                                   ARG_VALUE0,
                                   "-" + OPT_KEY1,
                                   ARG_VALUE1);

      assertTrue(result.isCommandPresent(CMD_NAME0));
      ArgumentModel cmdResult = result.getCommandValue(CMD_NAME0);
      Iterator<String> iterator = cmdResult.iterator();
      assertEquals(ARG_VALUE0, iterator.next());
      assertEquals(ARG_VALUE0, cmdResult.getArgumentValue(ARG_NAME0));
      assertEquals(ARG_VALUE1, iterator.next());
      assertEquals(ARG_VALUE1, cmdResult.getArgumentValue(ARG_NAME1));
      assertTrue(cmdResult.isOptionPresent(OPT_KEY0));
      assertEquals(OPT_VALUE0, cmdResult.getOptionValue(OPT_KEY0));
      assertTrue(cmdResult.isOptionPresent(OPT_KEY1));
      assertFalse(cmdResult.isOptionPresent(OPT_KEY2));
   }

   @Test
   public void testSyntax() {
      String expected = String.format("<%s> [<%s>] -%s <%s> [--%s] --%s=<%s>",
                                      ARG_NAME0,
                                      ARG_NAME1,
                                      OPT_KEY0,
                                      OPT_NAME0,
                                      OPT_LONG_KEY1,
                                      OPT_LONG_KEY2,
                                      OPT_NAME2);

      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addArgument(ARG_NAME0)
                                                   .buildArgument(ARG_NAME1)
                                                      .setMandatory(false)
                                                      .build()
                                                   .buildOption(OPT_KEY0, OPT_NAME0)
                                                      .setMandatory(true)
                                                      .setLongKey("shouldNeverShowUp")
                                                      .build()
                                                   .addOption(OPT_LONG_KEY1, OPT_NAME1)
                                                   .buildOption(OPT_LONG_KEY2, OPT_NAME2)
                                                      .setMandatory(true)
                                                      .build()
                                                   .buildAndGet();
      assertEquals(expected, parser.syntax());
   }

   @Test
   public void testMissingMandatoryArg() throws MissingArgumentException {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addArgument(ARG_NAME0)
                                                   .addArgument(ARG_NAME1)
                                                   .buildAndGet();

      assertThrows(MissingArgumentException.class, () -> parser.parseArguments(ARG_VALUE0));
   }

   @Test
   public void testMissingMandatoryOption() throws MissingArgumentException {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .buildOption(OPT_KEY0, OPT_NAME1)
                                                      .setMandatory(true)
                                                      .build()
                                                   .buildOption(OPT_KEY1, OPT_NAME1)
                                                      .setMandatory(true)
                                                      .build()
                                                   .buildAndGet();

      assertThrows(MissingArgumentException.class, () -> parser.parseArguments("-" + OPT_KEY0, OPT_VALUE0));
   }

   @Test
   public void testUnknownArgument() throws UnknownArgumentException {
      ArgumentParser parser = ArgumentParserBuilder.begin()
                                                   .addArgument(ARG_NAME0)
                                                   .buildAndGet();

      assertThrows(UnknownArgumentException.class, () -> parser.parseArguments(ARG_VALUE0, "InvalidOne"));
   }

}
