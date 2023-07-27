package ch05;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class EchoServerHandlerWithFuture extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 수신된 데이터를 클라이언트 소켓 버퍼에 기록하고
        // 버퍼의 데이터를 채널로 전송하는 비동기 메서드인 writeAndFlush 를 호출하고
        // ChannelFuture 객체를 돌려 받는다.
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);

        // ChannelFuture 객체에 채널을 종료하는 리스너를 등록한다.
        // ChannelFuture 객체가 완료 이벤트를 수신할 때 수행
//        channelFuture.addListener(ChannelFutureListener.CLOSE);

        final int writeMessageSize = ((ByteBuf) msg).readableBytes();
        // 사용자 정의 리스너 설정
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                System.out.println("전송한 Bytes : " + writeMessageSize);
                channelFuture.channel().close(); // ChannelFuture 객체에 포함된 채널을 가져와 채널 닫기 이벤트 발생
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}