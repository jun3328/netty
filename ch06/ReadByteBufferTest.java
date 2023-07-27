package ch06;

import java.nio.ByteBuffer;

public class ReadByteBufferTest {
    public static void main(String[] args) {
        byte[] tempArr = {1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
        ByteBuffer buffer = ByteBuffer.wrap(tempArr);

        System.out.println("position : " + buffer.position());
        System.out.println("limit : " + buffer.limit());

        System.out.println("buffer.get() : " + buffer.get());
        System.out.println("buffer.get() : " + buffer.get());
        System.out.println("buffer.get() : " + buffer.get());
        System.out.println("buffer.get() : " + buffer.get());

        System.out.println("position : " + buffer.position());
        System.out.println("limit : " + buffer.limit());

        buffer.flip();
        System.out.println("buffer.flip()");
        System.out.println("position : " + buffer.position());
        System.out.println("limit : " + buffer.limit());

        System.out.println("buffer.get(3) : " + buffer.get(3));
        System.out.println("position : " + buffer.position());

    }
}
