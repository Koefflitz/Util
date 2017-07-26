package de.dk.util.net;

import java.io.Serializable;

public class TestObject implements Serializable {
   private static final long serialVersionUID = -123094996089802269L;

   private final String msg;

   public TestObject(String msg) {
      this.msg = msg;
   }

   public String getMsg() {
      return msg;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.msg == null) ? 0 : this.msg.hashCode());
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
      TestObject other = (TestObject) obj;
      if (this.msg == null) {
         if (other.msg != null)
            return false;
      } else if (!this.msg.equals(other.msg))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "TestObject{\"" + msg + "\"}";
   }
}