package de.dk.util;

import java.util.LinkedList;

public interface BoundedDoubleListener {
   void onChange(double old, double newValue);

   public static class BoundedDoubleListenerChain extends LinkedList<BoundedDoubleListener>
                                                  implements BoundedDoubleListener {
      private static final long serialVersionUID = -713371338820244253L;

      @Override
      public void onChange(double old, double newValue) {
         for (BoundedDoubleListener listener : toArray(new BoundedDoubleListener[size()]))
            listener.onChange(old, newValue);
      }
   }
}
