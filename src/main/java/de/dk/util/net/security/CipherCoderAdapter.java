package de.dk.util.net.security;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

import de.dk.util.net.Coder;

public class CipherCoderAdapter implements Coder {
   private final Cipher encryptCipher;
   private final Cipher decryptCipher;

   public CipherCoderAdapter(Cipher cipher, SecretKey key) throws GeneralSecurityException {
      this.encryptCipher = Cipher.getInstance(cipher.getAlgorithm(), cipher.getProvider());
      encryptCipher.init(Cipher.ENCRYPT_MODE, key);
      this.decryptCipher = Cipher.getInstance(cipher.getAlgorithm(), cipher.getProvider());
      decryptCipher.init(Cipher.DECRYPT_MODE, key);
   }

   @Override
   public byte[] encode(byte[] data) throws IOException {
      try {
         return encryptCipher.doFinal(data);
      } catch (IllegalBlockSizeException | BadPaddingException e) {
         throw new IOException(e);
      }
   }

   @Override
   public byte[] decode(byte[] data) throws IOException {
      try {
         return decryptCipher.doFinal(data);
      } catch (IllegalBlockSizeException | BadPaddingException e) {
         throw new IOException(e);
      }
   }

}
