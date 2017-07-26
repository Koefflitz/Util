package de.dk.util.opt;

import java.util.Objects;

public class ExpectedOption implements ExpectedArgument {
   private final int index;
   private final char key;
   private final String name;
   private String longKey;
   private boolean mandatory;
   private boolean expectsValue;
   private String description;

   private String value;
   private boolean present;

   protected ExpectedOption(int index,
                            String name,
                            char key,
                            String longKey,
                            boolean mandatory,
                            boolean expectsValue,
                            String description) {
      this.index = index;
      this.name = Objects.requireNonNull(name);
      this.key = key;
      this.longKey = longKey;
      this.mandatory = mandatory;
      this.expectsValue = expectsValue;
      this.description = description;
   }

   protected ExpectedOption(int index, char key, String name, String longKey, String description) throws NullPointerException {
      this(index, name, key, longKey, false, false, description);
   }

   protected ExpectedOption(int index, char key, String name, boolean mandatory, String description) throws NullPointerException {
      this(index, name, key, null, mandatory, false, description);
   }

   protected ExpectedOption(int index, char key, String name, String description) throws NullPointerException {
      this(index, name, key, null, false, false, description);
   }

   protected ExpectedOption(int index, char key, String name) {
      this(index, key, name, null);
   }

   public void setValue(String value) throws UnsupportedOperationException {
      if (!expectsValue)
         throw new UnsupportedOperationException("No value for this option expected.");

      this.value = value;
   }

   @Override
   public String getValue() {
      return value;
   }

   public void setLongKey(String longKey) {
      this.longKey = longKey;
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
      mandatory &= expectsValue;
   }

   public void setPresent(boolean present) {
      this.present = present;
   }

   public char getKey() {
      return key;
   }

   public String getLongKey() {
      return longKey;
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
      String fullName = "-" + key + (expectsValue ? " <" + getName() + ">" : "");
      if (longKey != null)
         fullName += "\n--" + longKey + (expectsValue ? " <" + getName() + ">" : "");

      return fullName;
   }

   @Override
   public ExpectedOption clone() {
      return new ExpectedOption(index, name, key, longKey, mandatory, expectsValue, description);
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
                               + value != null ? (", value=" + value) : ""
              + " }";
   }
}