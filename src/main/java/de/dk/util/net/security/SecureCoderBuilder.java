package de.dk.util.net.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import de.dk.util.channel.Sender;
import de.dk.util.net.Coder;

public class SecureCoderBuilder {
   private static final String DEFAULT_SYMMETRIC_ALGORITHM = "AES";
   private static final String DEFAULT_ASYMMETRIC_ALGORITHM = "RSA";

   private Cipher symCipher;
   private Cipher asymCipher;
   private PrivateKey privateKey;
   private PublicKey ownPublicKey;
   private PublicKey otherPublicKey;
   private boolean genSessionKey;
   private KeyGenerator sessionKeyGen;;

   private SecretKey sessionKey;

   private ObjectInputStream in;
   private Sender sender;

   public SecureCoderBuilder(Sender sender,
                             ObjectInputStream in) throws NullPointerException {
      this.sender = sender;
      this.in = in;
   }

   private static PublicKey readOtherPublicKey(ObjectInputStream in) throws IOException {
      Object object;
      try {
         object = in.readObject();
      } catch (ClassNotFoundException e) {
         throw new IOException("Unknown object received while expecting the public key.", e);
      }
      PublicKeyPacket packet;
      try {
         packet = (PublicKeyPacket) object;
      } catch (ClassCastException e) {
         throw new IOException("Expected the public key, but received something else: " + object, e);
      }

      return packet.getPublicKey();
   }

   public Future<Coder> asyncBuild() {
      FutureTask<Coder> task = new FutureTask<>(() -> build());
      Thread thread = new Thread(task);
      thread.start();
      return task;
   }

   public Coder build() throws IOException, IllegalStateException {
      if (ownPublicKey == null)
         throw new IllegalStateException("Public key was not set.");

      try {
         if (symCipher == null)
            symCipher = Cipher.getInstance(sessionKey.getAlgorithm());
         if (asymCipher == null)
            asymCipher = Cipher.getInstance(DEFAULT_ASYMMETRIC_ALGORITHM);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
         throw new IOException("Could not initialize cipher.", e);
      }

      try {
         sender.send(new PublicKeyPacket(ownPublicKey));
      } catch (IOException e) {
         throw new IOException("Could not send public key", e);
      }

      this.otherPublicKey = readOtherPublicKey(in);

      SecretKey sessionKey;

      if (genSessionKey) {
         try {
            if (sessionKeyGen == null)
               sessionKeyGen = KeyGenerator.getInstance(DEFAULT_SYMMETRIC_ALGORITHM);

            sessionKey = sessionKeyGen.generateKey();
         } catch (NoSuchAlgorithmException e) {
            throw new IOException("Could not generate the session key", e);
         }
         sendSessionKey(sessionKey, sender);
      } else {
         sessionKey = readSessionKey(in);
      }

      try {
         return new CipherCoderAdapter(symCipher, sessionKey);
      } catch (GeneralSecurityException e) {
         throw new IOException(e);
      }
   }

   private SecretKey readSessionKey(ObjectInputStream in) throws IOException {
      Object object;
      try {
         object = in.readObject();
      } catch (ClassNotFoundException e) {
         throw new IOException("Received a strange message while expecting the session key.", e);
      }

      SessionKeyPacket packet;
      try {
         packet = (SessionKeyPacket) object;
      } catch (ClassCastException e) {
         throw new IOException("Expecting the session key, but received something else: " + object, e);
      }

      byte[] cryptedSessionKey = packet.getSessionKey();
      try {
         asymCipher.init(Cipher.UNWRAP_MODE, privateKey);
      } catch (InvalidKeyException e) {
         throw new IOException("Could not decrypt the session key with the others public key", e);
      }

      try {
         return (SecretKey) asymCipher.unwrap(cryptedSessionKey,
                                              packet.getAlgorithm(),
                                              Cipher.SECRET_KEY);
      } catch (InvalidKeyException | NoSuchAlgorithmException e) {
         throw new IOException("Could not decrypt the session key.", e);
      }
   }

   private void sendSessionKey(SecretKey sessionKey, Sender sender) throws IOException {
      byte[] encryptedSessionKey;
      try {
         asymCipher.init(Cipher.WRAP_MODE, otherPublicKey);
         encryptedSessionKey = asymCipher.wrap(sessionKey);
      } catch (InvalidKeyException | IllegalBlockSizeException e) {
         throw new IOException("Could not encrypt the session key with the others public key", e);
      }
      try {
         sender.send(new SessionKeyPacket(encryptedSessionKey, sessionKey.getAlgorithm()));
      } catch (IOException e) {
         throw new IOException("Could not send the session key", e);
      }
   }

   public SecureCoderBuilder setSymmetricCipher(Cipher symCipher) {
      this.symCipher = symCipher;
      return this;
   }

   public SecureCoderBuilder setAsymmetricCipher(Cipher asymCipher) {
      this.asymCipher = asymCipher;
      return this;
   }

   public SecureCoderBuilder setPublicKey(PublicKey publicKey) {
      this.ownPublicKey = publicKey;
      return this;
   }

   public SecureCoderBuilder setPrivateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
   }

   public SecureCoderBuilder setSessionKey(SecretKey sessionKey) {
      this.sessionKey = sessionKey;
      return this;
   }

   public SecureCoderBuilder setSessionKeyGen(KeyGenerator sessionKeyGen) {
      this.sessionKeyGen = sessionKeyGen;
      this.genSessionKey |= sessionKeyGen != null;
      return this;
   }

   public SecureCoderBuilder setGenerateSessionKey(boolean generateSessionKey) {
      this.genSessionKey = generateSessionKey;
      return this;
   }

}
