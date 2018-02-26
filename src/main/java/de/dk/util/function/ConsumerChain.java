package de.dk.util.function;

import java.util.LinkedList;
import java.util.function.Consumer;

public class ConsumerChain<T> extends LinkedList<Consumer<T>> implements Consumer<T> {
   private static final long serialVersionUID = -7960056568324246755L;

   public ConsumerChain() {

   }

   @SuppressWarnings("unchecked")
   @Override
   public void accept(T t) {
      Object[] consumers = toArray();
      for (Object consumer : consumers)
         ((Consumer<T>) consumer).accept(t);
   }

}
