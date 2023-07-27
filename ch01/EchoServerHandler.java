package ch01;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * 입력된 데이터를 처리하는 이벤트 핸들러, ChannelInboundHandlerAdapter 를 상속받음
 */

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 데이터 수신 이벤트 처리 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        /** 수신된 데이터를 가지고있는 네티의 바이트 버퍼 객체로부터 문자열 데이터를 읽어온다.
         *  네티의 바이트 버퍼(io.netty.buffer.ByteBuf)는
         *  자바의 바이트 버퍼(java.nio.ByteBuffer)와 유사한 클래스로서
         *  더 나은 성능과 편의성을 제공한다.
         */

        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

        System.out.println("수신한 문자열 [" + readMessage + "]"); // 수신된 문자열을 콘솔로 출력

        ctx.write(msg); // ctx 는 ChannelHandlerContext 인터페이스의 객체로서 채널 파이프라인에 대한 이벤트를 처리
    }


    // channelRead 이벤트 처리가 완료된 후 수행되는 이벤트 메서드
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush(); // 채널 파이프라인에 저장된 버퍼를 전송하는 flush 메서드 호출
    }
}