package de.dk.util.opt;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.dk.util.opt.ex.MissingArgumentException;

/**
 * Builder class to build an argument model.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 */
public class ArgumentModelBuilder {
   private final List<ExpectedPlainArgument> arguments;
   private final Map<Character, ExpectedOption> options;
   private final Map<String, ExpectedOption> longOptions;

   private final List<ExpectedArgument> mandatories;

   private boolean allMandatoriesPresent;
   private int plainArgIndex;

   /**
    * Create a new argumentmodel builder with plain arguments, options and long options.
    *
    * @param arguments The expected arguments
    * @param options The expected options mapped by their key
    * @param longOptions The expected options mapped by their long key
    * @param plainArgIndex The index of the next plain argument that will be parsed
    */
   public ArgumentModelBuilder(List<ExpectedPlainArgument> arguments,
                               Map<Character, ExpectedOption> options,
                               Map<String, ExpectedOption> longOptions,
                               int plainArgIndex) {
      this.arguments = Objects.requireNonNull(arguments);
      this.options = Objects.requireNonNull(options);
      this.longOptions = Objects.requireNonNull(longOptions);
      this.plainArgIndex = plainArgIndex;
      this.mandatories = Stream.concat(arguments.stream(), options.values().stream())
                                      .filter(ExpectedArgument::isMandatory)
                                      .collect(Collectors.toList());
   }

   /**
    * Create a new argumentmodel builder with plain arguments, options and long options.
    *
    * @param arguments The expected arguments
    * @param options The expected options mapped by their key
    * @param longOptions The expected options mapped by their long key
    */
   public ArgumentModelBuilder(List<ExpectedPlainArgument> arguments,
                               Map<Character, ExpectedOption> options,
                               Map<String, ExpectedOption> longOptions) {
      this(arguments, options, longOptions, 0);
   }

   /**
    * Builds the argumentmodel with all the given arguments and options.
    * Fails if some mandatory arguments (including mandatory options) are missing.
    *
    * @return An argumentmodel with all the parsed results
    *
    * @throws MissingArgumentException If any of the mandatory arguments is missing
    */
   public ArgumentModel build() throws MissingArgumentException {
      if (!allMandatoriesPresent())
         throw new MissingArgumentException(mandatories);

      LinkedHashMap<String, ExpectedPlainArgument> arguments = new LinkedHashMap<>();
      for (ExpectedPlainArgument arg : this.arguments)
         arguments.put(arg.getName(), arg);

      return new ArgumentModel(arguments, options.entrySet()
                                                 .stream()
                                                 .filter(e -> e.getValue().isPresent())
                                                 .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
   }

   /**
    * Get the next argument and assume it as parsed.
    * If the argument is mandatory it is assumed to be present.
    * Only call this method if a next argument is present.
    *
    * @return The next plain argument
    *
    * @throws NoSuchElementException If no more arguments are expected
    */
   public ExpectedPlainArgument nextArg() throws NoSuchElementException {
      if (plainArgIndex >= arguments.size())
         throw new NoSuchElementException("No Argument left");

      ExpectedPlainArgument next = arguments.get(plainArgIndex++);
      mandatories.remove(next);
      return next;
   }

   /**
    * Determines whether all mandatory arguments (including mandatory options) are present.
    *
    * @return <code>true</code> if all mandatory arguments are present, <code>false</code> otherwise.
    */
   public boolean allMandatoriesPresent() {
      if (!allMandatoriesPresent)
         mandatories.removeIf(ExpectedArgument::isPresent);

      return (allMandatoriesPresent = mandatories.isEmpty());
   }

   /**
    * Get all the expected arguments in their current state.
    *
    * @return all expected args
    */
   public List<ExpectedPlainArgument> getArguments() {
      return arguments;
   }

   /**
    * Get all the expected options mapped by their key in their current state.
    *
    * @return all expected options
    */
   public Map<Character, ExpectedOption> getOptions() {
      return options;
   }

   /**
    * Get all the expected options mapped by their long key in their current state.
    *
    * @return all expected options
    */
   public Map<String, ExpectedOption> getLongOptions() {
      return longOptions;
   }

   /**
    * Get all the mandatory arguments (including mandatory options).
    *
    * @return all the mandatory args
    */
   public List<ExpectedArgument> getMandatories() {
      return mandatories;
   }

   /**
    * Get the index of the current parsed plain argument.
    *
    * @return The index of the current pain arg
    */
   public int getPlainArgIndex() {
      return plainArgIndex;
   }

   /**
    * Sets the index of the current parsed plain argument.
    * This method can be used to skip some arguments or to parse some arguments more than once.
    *
    * @param mandatoryArgIndex The index of the currently parsed plain argument
    */
   public void setPlainArgIndex(int mandatoryArgIndex) {
      this.plainArgIndex = mandatoryArgIndex;
   }

   /**
    * Increments the index of the currently parsed plain argument
    */
   public void incrementPlainArgIndex() {
      plainArgIndex++;
   }

   /**
    * Find out of an option of the given <code>key</code> is expected by this argumentmodel builder.
    *
    * @param key The key to be expected or not
    *
    * @return <code>true</code> if an option of the given <code>key</code> is expected, <code>false</code> otherwise.
    */
   public boolean isOptionExpected(char key) {
      return options.containsKey(key);
   }

   /**
    * Find out of an option of the given <code>longKey</code> is expected by this argumentmodel builder.
    *
    * @param longKey The key to be expected or not
    *
    * @return <code>true</code> if an option of the given <code>longKey</code> is expected, <code>false</code> otherwise.
    */
   public boolean isOptionExpected(String longKey) {
      return longOptions.containsKey(longKey);
   }

   /**
    * Get the option of the given <code>key</code>.
    *
    * @param key The key of the option
    *
    * @return An optional that contains the option if it is expected
    */
   public Optional<ExpectedOption> getOption(char key) {
      return Optional.ofNullable(options.get(key));
   }

   /**
    * Get the option of the given <code>longKey</code>.
    *
    * @param longKey The long key of the option
    *
    * @return An optional that contains the option if it is expected
    */
   public Optional<ExpectedOption> getOption(String longKey) {
      return Optional.ofNullable(longOptions.get(longKey));
   }

}