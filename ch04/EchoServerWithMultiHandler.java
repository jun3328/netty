package ch04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServerWithMultiHandler {
    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new EchoServerFirstHandler()); // channelRead 이벤트만 구현
                        p.addLast(new EchoServerSecondHandler()); // channelRead, channelReadComplete, exceptionCaught 이벤트를 구현

                        /**
                         *   SecondHandler 의 channelRead() 는 수행되지 않음
                         *   FirstHandler 에서 channelRead() 가 수행되면서 해당 이벤트가 사라졌기 때문
                         */
                    }
                });

        try {
            ChannelFuture f = b.bind(8888).sync();
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }
    }
}
