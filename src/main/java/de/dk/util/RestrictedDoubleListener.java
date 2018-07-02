package de.dk.util;

import java.util.LinkedList;

public interface RestrictedDoubleListener {
   void onChange(double old, double newValue);

   public static class BoundedDoubleListenerChain extends LinkedList<RestrictedDoubleListener>
                                                  implements RestrictedDoubleListener {
      private static final long serialVersionUID = -713371338820244253L;

      @Override
      public void onChange(double old, double newValue) {
         for (RestrictedDoubleListener listener : toArray(new RestrictedDoubleListener[size()]))
            listener.onChange(old, newValue);
      }
   }
}
