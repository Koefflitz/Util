package de.dk.util;

import java.io.IOException;
import java.io.Serializable;

public interface Serializer {
   public byte[] serialize(Serializable o) throws IOException;
   public Object deserialize(byte[] bytes) throws ClassNotFoundException, IOException;
}
