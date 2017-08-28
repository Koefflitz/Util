package de.dk.util.net.security;

import java.io.IOException;
import java.io.ObjectInputStream;
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

public class SessionKeyArrangement {
   public static final String DEFAULT_SYMMETRIC_ALGORITHM = "AES";
   public static final String DEFAULT_ASYMMETRIC_ALGORITHM = "RSA";

   private Cipher asymCipher;
   private PrivateKey privateKey;
   private PublicKey ownPublicKey;
   private PublicKey otherPublicKey;
   private boolean genSessionKey;
   private KeyGenerator sessionKeyGen;

   private ObjectInputStream in;
   private Sender sender;

   public SessionKeyArrangement(Sender sender,
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

   public Future<SecretKey> arrangeAsync() {
      FutureTask<SecretKey> task = new FutureTask<>(() -> arrange());
      Thread thread = new Thread(task);
      thread.start();
      return task;
   }

   public SecretKey arrange() throws IOException, IllegalStateException {
      if (ownPublicKey == null)
         throw new IllegalStateException("Public key was not set.");

      try {
         sender.send(new PublicKeyPacket(ownPublicKey));
      } catch (IOException e) {
         throw new IOException("Could not send public key", e);
      }

      this.otherPublicKey = readOtherPublicKey(in);

      try {
         if (asymCipher == null)
            asymCipher = Cipher.getInstance(DEFAULT_ASYMMETRIC_ALGORITHM);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
         throw new IOException("Could not initialize cipher.", e);
      }

      if (!genSessionKey)
         return readSessionKey(in);

      SecretKey result;
      try {
         if (sessionKeyGen == null)
            sessionKeyGen = KeyGenerator.getInstance(DEFAULT_SYMMETRIC_ALGORITHM);

         result = sessionKeyGen.generateKey();

      } catch (NoSuchAlgorithmException e) {
         throw new IOException("Could not generate the session key", e);
      }
      sendSessionKey(result, sender);

      return result;
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

   public SessionKeyArrangement setAsymmetricCipher(Cipher asymCipher) {
      this.asymCipher = asymCipher;
      return this;
   }

   public SessionKeyArrangement setPublicKey(PublicKey publicKey) {
      this.ownPublicKey = publicKey;
      return this;
   }

   public SessionKeyArrangement setPrivateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
   }

   public SessionKeyArrangement setSessionKeyGen(KeyGenerator sessionKeyGen) {
      this.sessionKeyGen = sessionKeyGen;
      this.genSessionKey |= sessionKeyGen != null;
      return this;
   }

   public SessionKeyArrangement setGenerateSessionKey(boolean generateSessionKey) {
      this.genSessionKey = generateSessionKey;
      return this;
   }

}
