package ch06;

import java.nio.ByteBuffer;

public class ByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer secondBuffer = ByteBuffer.allocate(11);
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        ByteBuffer thirdBuffer = ByteBuffer.allocateDirect(11);

        test1(firstBuffer);
        test2(secondBuffer);
        test3(thirdBuffer);

    }

    private static void test1(ByteBuffer firstBuffer) {
        System.out.println("firstBuffer - 초기값 : " + firstBuffer);

        byte[] source = "Hello world".getBytes();

        firstBuffer.put(source);
        System.out.println("firstBuffer - 11 바이트 기록 후 " + firstBuffer);
    }

    private static void test2(ByteBuffer secondBuffer) {
        System.out.println("secondBuffer - 초기값 : " + secondBuffer);

        secondBuffer.put((byte) 1);
        secondBuffer.put((byte) 2);
        System.out.println("secondBuffer - put() x2 후 position : " + secondBuffer.position());

        secondBuffer.rewind();
        System.out.println("secondBuffer - rewind() 후 position : " + secondBuffer.position());

        secondBuffer.get();
        secondBuffer.get();
        System.out.println("secondBuffer - get() x2 후 position : " + secondBuffer.position());
    }

    private static void test3(ByteBuffer thirdBuffer) {
        System.out.println("thirdBuffer - position : " + thirdBuffer.position());
        System.out.println("thirdBuffer - limit : " + thirdBuffer.limit());

        thirdBuffer.put((byte) 1);
        thirdBuffer.put((byte) 2);
        thirdBuffer.put((byte) 3);
        thirdBuffer.put((byte) 4);
        System.out.println("thirdBuffer - put() x4 후 position : " + thirdBuffer.position());
        System.out.println("thirdBuffer - put() x4 후 limit : " + thirdBuffer.limit());

        thirdBuffer.flip();
        System.out.println("thirdBuffer - flip() 후 position : " + thirdBuffer.position());
        System.out.println("thirdBuffer - flip() 후 limit : " + thirdBuffer.limit());

    }
}
