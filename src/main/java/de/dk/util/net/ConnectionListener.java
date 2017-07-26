package de.dk.util.net;

import java.util.LinkedList;
import java.util.List;

/**
 * A listener interface to listen to events of a {@link Connection}.
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 */
public interface ConnectionListener {
   /**
    * This method is called after the connection has been closed.
    */
   public void closed();

   /**
    * This method is called if an error occured while reading an incoming method of the connection.
    *
    * @param e The thrown reading exception
    */
   public void readingError(ReadingException e);

   /**
    * Sums up many listeners in a {@link List} of listeners.
    * This is just a wrapper class to sum up many listeners.
    *
    * @author David Koettlitz
    * <br>Erstellt am 13.07.2017
    */
   public static class ConnectionListenerChain extends LinkedList<ConnectionListener>
                                               implements ConnectionListener {

      private static final long serialVersionUID = 1L;

      @Override
      public void closed() {
         ConnectionListener[] listeners = toArray(new ConnectionListener[size()]);
         for (ConnectionListener l : listeners)
            l.closed();
      }

      @Override
      public void readingError(ReadingException e) {
         ConnectionListener[] listeners = toArray(new ConnectionListener[size()]);
         for (ConnectionListener l : listeners)
            l.readingError(e);
      }
   }
}