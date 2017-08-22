package de.dk.util.net.security;

import java.security.PublicKey;

public class PublicKeyPacket implements SecurityPacket {
   private static final long serialVersionUID = 3733429242417434953L;

   private PublicKey publicKey;

   public PublicKeyPacket(PublicKey publicKey) {
      this.publicKey = publicKey;
   }

   public PublicKey getPublicKey() {
      return publicKey;
   }

   @Override
   public SecurityPacketType getSecurityType() {
      return SecurityPacketType.PUBLIC_KEY;
   }

   @Override
   public String toString() {
      return "PublicKeyPacket { algorithm=" + publicKey.getAlgorithm() + " }";
   }
}
