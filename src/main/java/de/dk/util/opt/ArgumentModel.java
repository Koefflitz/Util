package de.dk.util.opt;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents parsed arguments. This class is implementing the <code>Iterable</code> interface
 * to iterate over the argumentvalues.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 *
 * @see ArgumentParser
 * @see ArgumentParserBuilder
 */
public class ArgumentModel implements Iterable<String> {
   private LinkedHashMap<String, ExpectedPlainArgument> arguments;
   private Map<Character, ExpectedOption> options;

   /**
    * Creates a new argument model with argument and options.
    *
    * @param arguments The arguments mapped by their names
    * @param options The options mapped by their key characters
    */
   public ArgumentModel(LinkedHashMap<String, ExpectedPlainArgument> arguments, Map<Character, ExpectedOption> options) {
      this.arguments = arguments;
      this.options = options;
   }

   /**
    * Get the value of the argument with the given <code>name</code>.
    *
    * @param name The name of the argument
    *
    * @return The value of the argument of <code>null</code> if the argument
    * <code>name</code> was not specified.
    */
   public String getArgumentValue(String name) {
      return arguments.get(name) == null ? null : arguments.get(name).getValue();
   }

   /**
    * Get the value of the argument with the given <code>name</code>.
    *
    * @param name The name of the argument
    *
    * @return The value of the argument of <code>null</code> if the argument
    * <code>name</code> was not specified.
    */
   public Optional<String> getOptionalArgumentValue(String name) {
      return Optional.ofNullable(arguments.get(name))
                     .map(ExpectedArgument::getValue);
   }

   /**
    * Get the value of the option of the given <code>key</code>.
    *
    * @param key The key of the option
    *
    * @return The value of the option
    */
   public String getOptionValue(char key) {
      return options.get(key) == null ? null : options.get(key).getValue();
   }

   /**
    * Get the value of the option of the given <code>key</code>.
    *
    * @param key The key of the option
    *
    * @return The value of the option
    */
   public Optional<String> getOptionalValue(char key) {
      return Optional.ofNullable(options.get(key))
                     .map(ExpectedArgument::getValue);
   }

   /**
    * Find out whether an option was set or not.
    *
    * @param key The key of the option
    *
    * @return <code>true</code> if the option was set. <code>false</code> otherwise
    */
   public boolean isOptionPresent(char key) {
      return options.containsKey(key);
   }

   @Override
   public Iterator<String> iterator() {
      return arguments.values()
                      .stream()
                      .map(ExpectedPlainArgument::getValue)
                      .iterator();
   }
}