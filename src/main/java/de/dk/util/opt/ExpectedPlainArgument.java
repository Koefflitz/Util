package de.dk.util.opt;

import java.util.Objects;

public class ExpectedPlainArgument implements ExpectedArgument {
   private final int index;
   private final String name;
   private boolean mandatory;
   private String description;

   private String value;

   public ExpectedPlainArgument(int index, String name, boolean mandatory, String description) throws NullPointerException {
      this.index = index;
      this.name = Objects.requireNonNull(name);
      this.mandatory = mandatory;
      this.description = description;
   }

   protected ExpectedPlainArgument(int index, String name) throws NullPointerException {
      this(index, name, true, null);
   }

   public ExpectedPlainArgument(int index, String name, String description) throws NullPointerException {
      this(index, name, true, description);
   }

   @Override
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
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

   public void setMandatory(boolean mandatory) {
      this.mandatory = mandatory;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public String fullName() {
      return "<" + getName() + ">";
   }

   @Override
   public boolean isPresent() {
      return value != null;
   }

   @Override
   public boolean isOption() {
      return false;
   }

   @Override
   public ExpectedPlainArgument clone() {
      return new ExpectedPlainArgument(index, name, mandatory, description);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.index;
      result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
      ExpectedPlainArgument other = (ExpectedPlainArgument) obj;
      if (this.index != other.index)
         return false;
      if (this.name == null) {
         if (other.name != null)
            return false;
      } else if (!this.name.equals(other.name))
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
      return "ExpectedPlainArgument { name=" + name
                                      + ", index=" + index
                                      + value != null ? (", value=" + value) : ""
             + " }";
   }
}