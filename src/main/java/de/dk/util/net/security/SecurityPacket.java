package de.dk.util.net.security;

import java.io.Serializable;

public interface SecurityPacket extends Serializable {
   public SecurityPacketType getSecurityType();

   public static enum SecurityPacketType {
      PUBLIC_KEY, SESSION_KEY;
   }
}
