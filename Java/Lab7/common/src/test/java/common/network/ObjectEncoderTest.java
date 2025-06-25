package common.network;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class ObjectEncoderTest {

  @Test
  void testEncodeObject() throws IOException, ClassNotFoundException {
    String testObj = "Hello, world!";

    ByteBuffer buffer = ObjectEncoder.encodeObject(testObj);

    buffer.flip();

    int size = buffer.getInt();
    assertTrue(size > 0, "Size should be positive");

    assertEquals(size, buffer.remaining());

    byte[] serializedData = new byte[size];
    buffer.get(serializedData);

    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData))) {
      Object deserialized = ois.readObject();
      assertEquals(testObj, deserialized);
    }
  }
}
