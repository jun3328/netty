package ch01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    /**
     *  소켓 채널이 최초 활성화 되었을 때 실행
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String sendMessage = "Hello, Netty!";

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        StringBuilder builder = new StringBuilder();
        builder.append("전송한 문자열 [");
        builder.append(sendMessage);
        builder.append("]");

        System.out.println(builder.toString());

        ctx.writeAndFlush(messageBuffer); // 내부적으로 데이터 기록과 전송의 두가지 메서드 호출
    }

    // 서버로부터 수신된 데이터가 있읗 때 호출
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 서버로부터 수신된 데이터가 저장된 msg 객체에서 문자열 데잍를 추출
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    // 수신된 데이터를 모두 읽었을 때 호출(channelRead 메서드의 수행 완료 뒤)
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.close(); // 수신된 데이터를 모두 읽은 후 서버와 연결된 채널을 닫음
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
