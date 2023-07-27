package ch04;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpHelloWorldServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec()); // 네티가 제공해는 HTTP 서버코덱


        // HTTP 서버코덱 이 수신한 이벤트 데이터를 처리하여 HTTP 객체로 변환한 다음
        // channelRead 이벤트를 HttpHelloWorldServerHandler 로 전달
        p.addLast(new HttpHelloWorldServerHandler());
    }
}
