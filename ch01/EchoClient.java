package ch01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new EchoClientHandler());
                    }
                });

        try {
            /**
             *  비동기 입출력 메서드 connect 를 호출 (ChannelFuture 객체 반환)
             *  ChannelFuture 객체를 통해 비동기 메서드의 처리결과 확인
             */
            ChannelFuture f = b.connect("localhost", 8888).sync();

            //
            /**
             *  ChannelFuture 객체의 sync 메서드는 ChannelFuture 객체의 요청이 완료될 때 까지 대기
             *  단, 요청이 실패하면 예외를 던짐.
             *  connect 메서드의 처리가 완료될 때 까지 다음 라인으로 진행하지 않음
             */
            f.channel().closeFuture().sync();

        } finally {

            group.shutdownGracefully();
        }
    }
}
