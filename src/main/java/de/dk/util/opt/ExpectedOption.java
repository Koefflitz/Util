package de.dk.util.opt;

import de.dk.util.StringUtils;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ExpectedOption implements ExpectedArgument {
   public final static char NO_KEY = '\0';

   private final int index;
   private char key;
   private final String name;
   private String longKey;
   private boolean mandatory;
   private boolean expectsValue;
   private String description;

   private String value;
   private boolean present;

   private ExpectedOption(int index,
                          String name,
                          char key,
                          String longKey,
                          boolean mandatory,
                          boolean expectsValue,
                          String description,
                          String value,
                          boolean present) {
      this.index = index;
      this.name = name;
      this.key = key;
      this.longKey = longKey;
      this.mandatory = mandatory;
      this.expectsValue = expectsValue;
      this.description = description;
      this.value = value;
      this.present = present;
   }

   public ExpectedOption(int index,
                         String name,
                         char key,
                         String longKey,
                         boolean mandatory,
                         String description) {
      this.index = index;
      this.name = name;
      this.key = key;
      this.longKey = longKey;
      this.mandatory = mandatory;
      this.expectsValue = mandatory;
      this.description = description;
   }

   public ExpectedOption(int index, char key, String name, String longKey, String description) {
      this(index, name, key, longKey, false, description);
   }

   public ExpectedOption(int index, char key, String name, boolean mandatory, String description) {
      this(index, name, key, null, mandatory, description);
   }

   public ExpectedOption(int index, char key, String name, String description) {
      this(index, name, key, null, false, description);
   }

   public ExpectedOption(int index, char key, String name) {
      this(index, key, name, null);
   }

   public ExpectedOption(int index, String longKey, String name) {
      this(index, NO_KEY, name, longKey, null);
   }

   public void setValue(String value) throws UnsupportedOperationException {
         if (!expectsValue)
            throw new UnsupportedOperationException("No value for this option expected.");

      this.value = value;
   }

   public String getValue() {
      return value;
   }

   public void setMandatory(boolean mandatory) {
      this.mandatory = mandatory;
      this.expectsValue |= mandatory;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public boolean isPresent() {
      return present;
   }

   public boolean expectsValue() {
      return expectsValue;
   }

   public void setExpectsValue(boolean expectsValue) {
      this.expectsValue = expectsValue;
      this.mandatory &= expectsValue;
   }

   public void setPresent(boolean present) {
      this.present = present;
   }

   public char getKey() {
      return key;
   }

   public void setKey(char key) {
      this.key = key;
   }

   public String getLongKey() {
      return longKey;
   }

   public void setLongKey(String longKey) {
      this.longKey = StringUtils.isBlank(longKey) ? null : longKey;
   }

   @Override
   public int getIndex() {
      return index;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public boolean isMandatory() {
      return mandatory;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public boolean isOption() {
      return true;
   }

   @Override
   public String fullName() {
      String fullName;
      if (key != NO_KEY)
         fullName = "-" + key + (expectsValue ? " <" + getName() + ">" : "");
      else if (longKey != null)
         fullName = "--" + longKey + (expectsValue ? "=<" + getName() + ">" : "");
      else
         fullName = "-" + (expectsValue ? " <" + getName() + ">" : "");

      return fullName;
   }

   @Override
   public ExpectedOption clone() {
      return new ExpectedOption(index, name, key, longKey, mandatory, expectsValue, description, value, present);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.key;
      result = prime * result + ((this.longKey == null) ? 0 : this.longKey.hashCode());
      result = prime * result + (this.mandatory ? 1231 : 1237);
      result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
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
      ExpectedOption other = (ExpectedOption) obj;
      if (this.key != other.key)
         return false;
      if (this.longKey == null) {
         if (other.longKey != null)
            return false;
      } else if (!this.longKey.equals(other.longKey))
         return false;
      if (this.mandatory != other.mandatory)
         return false;
      if (this.value == null) {
         if (other.value != null)
            return false;
      } else if (!this.value.equals(other.value))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "ExpectedOption { name=" + name
                               + ", index=" + index
                               + (value != null ? (", value=" + value) : "")
              + " }";
   }
}
