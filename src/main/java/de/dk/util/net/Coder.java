package de.dk.util.net;

import java.io.IOException;

public interface Coder {
   public byte[] encode(byte[] data) throws IOException;
   public byte[] decode(byte[] data) throws IOException;
}
