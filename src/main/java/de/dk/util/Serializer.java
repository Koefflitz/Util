package de.dk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {
   private final ByteArrayOutputStream serializationTarget = new ByteArrayOutputStream();

   public Serializer() {

   }

   public synchronized byte[] serialize(Serializable o) throws IOException, InvalidClassException {
      ObjectOutputStream serializer = new ObjectOutputStream(serializationTarget);
      serializer.writeObject(o);
      byte[] result = serializationTarget.toByteArray();
      serializationTarget.reset();
      return result;
   }

   public synchronized Object deserialize(byte[] bytes) throws ClassNotFoundException, IOException {
      ByteArrayInputStream deserializationTarget = new ByteArrayInputStream(bytes);
      ObjectInputStream deserializer;
      deserializer = new ObjectInputStream(deserializationTarget);
      return deserializer.readObject();
   }

}