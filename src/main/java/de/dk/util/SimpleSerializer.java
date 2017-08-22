package de.dk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class SimpleSerializer implements Serializer {
   private final ByteArrayOutputStream serializationTarget = new ByteArrayOutputStream();

   public SimpleSerializer() {

   }

   @Override
   public synchronized byte[] serialize(Serializable o) throws IOException {
      ObjectOutputStream serializer = new ObjectOutputStream(serializationTarget);
      serializer.writeObject(o);
      byte[] result = serializationTarget.toByteArray();
      serializationTarget.reset();
      return result;
   }

   @Override
   public Object deserialize(byte[] bytes) throws ClassNotFoundException, IOException {
      ByteArrayInputStream deserializationTarget = new ByteArrayInputStream(bytes);
      ObjectInputStream deserializer = new ObjectInputStream(deserializationTarget);
      return deserializer.readObject();
   }

}
