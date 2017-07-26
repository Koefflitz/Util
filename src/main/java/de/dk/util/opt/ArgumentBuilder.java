package de.dk.util.opt;

/**
 * A builder class to build an argument for an {@link ArgumentParser}.
 * An argument builder is a sub builder of an {@link ArgumentParserBuilder}.
 * The built argument is given to the parent argumentparser builder.
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 *
 * @see ArgumentParser
 * @see ArgumentParserBuilder
 */
public class ArgumentBuilder {
   private final ArgumentParserBuilder parentBuilder;
   private final ExpectedPlainArgument argument;

   /**
    * Creates a new argument builder that belongs to the given <code>parentBuilder</code>.
    * The argument that this argument builder is building is passed to the <code>parentBuilder</code>.
    *
    * @param parentBuilder The argumentparser builder this argument builder belongs to
    * @param index The index of the argument it has in the order
    * @param name The name of the argument
    */
   protected ArgumentBuilder(ArgumentParserBuilder parentBuilder, int index, String name) {
      this.parentBuilder = parentBuilder;
      this.argument = new ExpectedPlainArgument(index, name);
   }

   /**
    * Builds the argument and adds it to the argumentparser builder
    * by which this argument builder was created and returns the argumentparser builder.
    *
    * @return The argumentparser builder by which this argument builder was created.
    */
   public ArgumentParserBuilder build() {
      return parentBuilder.addArgument(argument);
   }

   /**
    * Builds the argument and passes it to the parent argumentparser builder.
    * It is recommended to use the {@link ArgumentBuilder#build()} method instead.
    *
    * @return The built argument.
    */
   public ExpectedPlainArgument buildAndGet() {
      parentBuilder.addArgument(argument);
      return argument;
   }

   /**
    * Makes the argument mandatory. By default arguments are mandatory.
    * If an argument is mandatory, the argumentparser is throwing a <code>MissingArgumentException</code>
    * if the argument is not present.
    *
    * @param mandatory Determines if the argument is mandatory or not.
    *
    * @return This argument builder to go on
    */
   public ArgumentBuilder setMandatory(boolean mandatory) {
      argument.setMandatory(mandatory);
      return this;
   }

   /**
    * Set a description of the argument. This description can be printed for the user to help him.
    *
    * @param description The description of the argument
    *
    * @return This argument builder to go on
    */
   public ArgumentBuilder setDescription(String description) {
      argument.setDescription(description);
      return this;
   }

}