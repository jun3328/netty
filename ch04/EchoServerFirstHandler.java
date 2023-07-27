package ch04;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoServerFirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf readMsg = (ByteBuf) msg;
        System.out.println("FirstHandler channelRead : " + readMsg.toString(Charset.defaultCharset()));
        ctx.write(msg);

//        ctx.fireChannelRead(msg); // SecondHandler 의 channelRead 이벤트도 수행하고 싶을 경우
    }

}
