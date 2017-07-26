package de.dk.util.opt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import de.dk.util.ArrayIterator;
import de.dk.util.opt.ex.MissingArgumentException;
import de.dk.util.opt.ex.MissingOptionValueException;

/**
 * A class to parse command line arguments and options.
 * Supports plain arguments, options (a dash followed by 0 or more characters)
 * and long options (two dashes followed by a string)
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 *
 * @see ArgumentParserBuilder
 */
public class ArgumentParser {
   private static final String HELP = "help";
   private static final String SHORT_HELP = "h";
   private final List<ExpectedPlainArgument> arguments;
   private final Map<Character, ExpectedOption> options;
   private final Map<String, ExpectedOption> longOptions;

   private ExpectedArgument[] allArguments;

   /**
    * Creates a new argumentparser expecting the given arguments and options.
    * Use a {@link ArgumentParserBuilder} to comfortably build an argumentparser.
    *
    * @param arguments The expected plain arguments in the order of the list
    * @param options The expected options mapped by their key
    * @param longOptions The expected options mapped by their long key
    */
   public ArgumentParser(List<ExpectedPlainArgument> arguments,
                         Map<Character, ExpectedOption> options,
                         Map<String, ExpectedOption> longOptions) {
      this.arguments = Objects.requireNonNull(arguments);
      this.options = Objects.requireNonNull(options);
      this.longOptions = Objects.requireNonNull(longOptions);

      this.allArguments = joinArguments(arguments, options.values());
   }

   private static ExpectedArgument[] joinArguments(Collection<ExpectedPlainArgument> plainArgs,
                                                   Collection<ExpectedOption> options) {
      ExpectedArgument[] result = new ExpectedArgument[plainArgs.size() + options.size()];
      for (ExpectedPlainArgument arg : plainArgs)
         result[arg.getIndex()] = arg;

      for (ExpectedOption opt : options)
         result[opt.getIndex()] = opt;

      return result;
   }

   /**
    * Parses the given <code>args</code> and returns the results as an argument model.
    *
    * @param args The arguments to parse
    *
    * @return An argument model containing the results.
    *
    * @throws MissingArgumentException If a mandatory argument (or mandatory option) is missing
    * @throws MissingOptionValueException If an option that has to go with a following value was no value given
    */
   public ArgumentModel parseArguments(String... args) throws MissingArgumentException,
                                                              MissingOptionValueException {
      List<ExpectedPlainArgument> arguments = this.arguments
                                                  .stream()
                                                  .map(ExpectedPlainArgument::clone)
                                                  .collect(ArrayList::new,
                                                           Collection::add,
                                                           Collection::addAll);

      Map<Character, ExpectedOption> options = this.options
                                                   .entrySet()
                                                   .stream()
                                                   .collect(Collectors.toMap(Entry::getKey,
                                                                             e -> e.getValue().clone()));

      Map<String, ExpectedOption> longOptions = this.longOptions
                                                    .entrySet()
                                                    .stream()
                                                    .collect(Collectors.toMap(Entry::getKey,
                                                                              e -> e.getValue().clone()));

      ArgumentModelBuilder builder = new ArgumentModelBuilder(arguments, options, longOptions);
      return parseArguments(ArrayIterator.of(args), builder);
   }

   private ArgumentModel parseArguments(Iterator<String> iterator,
                                        ArgumentModelBuilder builder) throws MissingArgumentException,
                                                                             MissingOptionValueException {
      try {
         while (iterator.hasNext()) {
            String arg = iterator.next();
            if (!arg.startsWith("-")) {
               handlePlainArgument(arg, builder);
               continue;
            }

            ExpectedOption option = handleOption(arg, builder);
            if (option.expectsValue() && option.getValue() == null) {
               if (!iterator.hasNext())
                  throw new MissingOptionValueException(option);

               option.setValue(iterator.next());
            }
         }
      } catch (NoSuchElementException e) {

      }

      return builder.build();
   }

   private void handlePlainArgument(String arg, ArgumentModelBuilder builder) throws NoSuchElementException {
      ExpectedPlainArgument expected;
      expected = builder.nextArg();
      expected.setValue(arg);
   }

   private ExpectedOption handleOption(String arg, ArgumentModelBuilder builder) throws MissingOptionValueException {
      ExpectedOption result = null;

      if (arg.length() > 2 && arg.charAt(1) == '-') {
         int equalsIndex = arg.indexOf('=');
         if (equalsIndex == -1) {
            result = builder.getOption(arg.substring(2))
                            .orElseThrow(() -> new NoSuchElementException(arg));

            if (result.expectsValue())
               throw new MissingOptionValueException(result);
         } else {
            result = builder.getOption(arg.substring(2, equalsIndex))
                            .orElseThrow(() -> new NoSuchElementException(arg));

            if (arg.length() > equalsIndex + 1)
               result.setValue(arg.substring(equalsIndex + 1));
         }
      } else if (arg.length() >= 2) {
         for (int i = 1; i < arg.length(); i++) {
            char key = arg.charAt(i);
            result = builder.getOption(key)
                            .orElseThrow(() -> new NoSuchElementException(arg));

            result.setPresent(true);
            if (result.expectsValue() && i + 1 < arg.length())
               throw new MissingOptionValueException(result);
         }
      } else {
         result = builder.getOption('\0')
                         .orElseThrow(() -> new NoSuchElementException(arg));
         result.setPresent(true);
      }

      result.setPresent(true);
      return result;
   }

   /**
    * Prints the syntax of the arguments with descriptions if the <code>args</code> match "help" or "h".
    *
    * @param out The printstream to print the help message on
    * @param args The given arguments
    *
    * @return <code>true</code> if the arguments match "help" or "h", <code>false</code> otherwise.
    *
    * @throws NullPointerException If <code>out</code> is <code>null</code>
    */
   public boolean printUsageIfHelp(PrintStream out, String... args) throws NullPointerException {
      if (!isHelp(args))
         return false;

      printUsage(out);
      return true;
   }

   /**
    * Finds out if the given <code>args</code> match "help" or "h".
    *
    * @param args The given arguments
    *
    * @return <code>true</code> if the arguments match "help" or "h", <code>false</code> otherwise.
    */
   public boolean isHelp(String... args) {
      if (args == null || args.length < 1)
         return false;

      return args[0].equals(HELP) || args[0].equals(SHORT_HELP);
   }

   /**
    * Prints the syntax of the expected commandline arguments with names and descriptions.
    *
    * @param out The printstream to print on
    *
    * @throws NullPointerException If <code>out</code> is <code>null</code>
    */
   public void printUsage(PrintStream out) throws NullPointerException {
      Objects.requireNonNull(out);
      out.println("Syntax:");
      out.println(syntax());
      for (ExpectedArgument arg : allArguments) {
         out.println();
         out.println(arg.fullName());
         out.println(arg.getDescription());
      }
   }

   /**
    * Get the string that describes the syntax of the expected arguments.
    *
    * @return The syntax string
    */
   public String syntax() {
      String synopsis = "";
      for (ExpectedArgument arg : allArguments) {
         if (arg.isMandatory())
            synopsis += arg.fullName() + " ";
         else
            synopsis += "[" + arg.fullName() + "] ";
      }

      return synopsis.trim();
   }

}