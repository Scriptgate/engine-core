package net.scriptgate.engine.util;

import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.BufferUtils.createByteBuffer;

public final class IOUtil {

    private IOUtil() {
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     * @return the resource data
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        File file = new File(resource);
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();

            buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);

            int remainder;
            do {
                remainder = fc.read(buffer);
            }
            while (remainder != -1);

            fis.close();
            fc.close();
        } else {
            buffer = createByteBuffer(bufferSize);

            try (InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
                 ReadableByteChannel rbc = Channels.newChannel(source)) {
                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }
        buffer.flip();
        return buffer;
    }

}