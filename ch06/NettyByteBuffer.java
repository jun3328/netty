package ch06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuffer {
    public static void main(String[] args) {
        createUnpooledHeapBuffer();
//        createUnpooledDirectBuffer();
//        createPooledHeapBuffer();
//        createPooledDirectBuffer();

    }

    private static void test(ByteBuf buf) {
        System.out.println("capacity : " + buf.capacity());
        System.out.println("direct : " + buf.isDirect());
        System.out.println("readableBytes : " + buf.readableBytes());
        System.out.println("writableBytes : " + buf.writableBytes() + "\n");

        String sourceData = "Hello world";
        buf.writeBytes(sourceData.getBytes());

        System.out.println("writeBytes(\"Hello world\".getBytes())");
        System.out.println("readableBytes : " + buf.readableBytes());
        System.out.println("writableBytes : " + buf.writableBytes() + "\n");

        System.out.println("buf.toString() : " + buf.toString(Charset.defaultCharset()));

        buf.capacity(6);
        System.out.println("buf.capacity(6)");
        System.out.println("buf.toString() : " + buf.toString(Charset.defaultCharset()));

        buf.capacity(13);
        System.out.println("buf.capacity(13)");
        System.out.println("buf.toString() : " + buf.toString(Charset.defaultCharset()));

        buf.writeBytes("world".getBytes());
        System.out.println("buf.writeBytes(\"world\".getBytes())");
        System.out.println("buf.toString() : " + buf.toString(Charset.defaultCharset()));

        System.out.println("\ncapacity : " + buf.capacity());
        System.out.println("readableBytes : " + buf.readableBytes());
        System.out.println("writableBytes : " + buf.writableBytes());
        System.out.println("isReadable : " + buf.isReadable() + "\n");

        System.out.println("--------------------------------------");
    }

    private static void createUnpooledHeapBuffer() {
        System.out.println("UnpooledHeapBuffer");
        ByteBuf buf = Unpooled.buffer(11);
        test(buf);
    }

    private static void createUnpooledDirectBuffer() {
        System.out.println("UnpooledDirectBuffer");
        ByteBuf buf = Unpooled.directBuffer(11);
        test(buf);
    }

    private static void createPooledHeapBuffer() {
        System.out.println("PooledHeapBuffer");
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
        test(buf);
    }

    private static void createPooledDirectBuffer() {
        System.out.println("PooledDirectBuffer");
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
        test(buf);
    }
}
