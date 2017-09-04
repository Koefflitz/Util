package de.dk.util.net.security;

public class SessionKeyPacket implements SecurityPacket {
   private static final long serialVersionUID = -103001753017194083L;

   private final byte[] sessionKey;
   private final String algorithm;

   public SessionKeyPacket(byte[] sessionKey, String algorithm) {
      this.sessionKey = sessionKey;
      this.algorithm = algorithm;
   }

   public byte[] getSessionKey() {
      return sessionKey;
   }

   public String getAlgorithm() {
      return algorithm;
   }

   @Override
   public SecurityPacketType getSecurityType() {
      return SecurityPacketType.SESSION_KEY;
   }

   @Override
   public String toString() {
      return "SessionKeyPacket { algorithm=" + algorithm + " }";
   }

}
