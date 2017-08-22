package de.dk.util.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

import de.dk.util.channel.IDGenerator;

/**
 * An id generator that generates ids based on the local ip address.
 * The id generation is dependent on the {@link InetAddress#getLocalHost()} method.
 *
 * @author David Koettlitz
 * <br>Erstellt am 13.07.2017
 */
public class InetAddressIdGenerator implements IDGenerator {
   public static final long RIGHT = (long) (Math.pow(2, Integer.SIZE) - 1);
   public static final long LEFT = ~RIGHT;

   private static long idBase = -1;

   private final AtomicLong idCounter;

   /**
    * Creates a new id generator that is based on the local ip address.
    *
    * @throws UnknownHostException If the local host name could not be resolved into an address
    */
   public InetAddressIdGenerator() throws UnknownHostException {
      this.idCounter = new AtomicLong(getIdBase());
   }

   public static synchronized long getIdBase() throws UnknownHostException {
      if (idBase == -1) {
         long localhost = InetAddress.getLocalHost()
                                     .toString()
                                     .hashCode();

         long left = localhost << Integer.SIZE;
         long right = Integer.MIN_VALUE;
         right <<= Integer.SIZE;
         right >>>= Integer.SIZE;
         idBase = left | right;
      }
      return idBase;
   }

   @Override
   public long nextId() {
      synchronized (idCounter) {
         int id = (int) (idCounter.get() & RIGHT);
         id++;
         long right = id;
         right <<= Integer.SIZE;
         right >>>= Integer.SIZE;
         idCounter.set((idCounter.get() & LEFT) | right);
      }
      return idCounter.get();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (this.idCounter.get() ^ (this.idCounter.get() >>> 32));
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
      InetAddressIdGenerator other = (InetAddressIdGenerator) obj;
      if (this.idCounter.get() != other.idCounter.get())
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "InetAddressIdGenerator { idCounter=" + idCounter + " }";
   }
}