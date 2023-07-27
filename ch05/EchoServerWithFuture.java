package ch05;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServerWithFuture {
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
                        p.addLast(new EchoServerHandlerWithFuture()); // ChannelFuture 리스너가 설정된 핸들러
                    }
                });

        try {
            // 에코 서버가 8888번 포트를 사용하도록 바인드하는 비동기 bind 메서드 호출
            // 부트스트랩 클래스의 bind 메서드는 포트 바인딩이 완료되기 전에 ChannelFuture 객체 반환
            ChannelFuture bindFuture = b.bind(8888);

            // ChannelFuture 인터페이스의 sync 메서드는 주어진 ChannelFuture 객체의 작업이 완료될 때까지
            // 블로킹하는 메서드, bind 메서드의 처리가 완료될 때 sync 메서드도 같이 완료
            bindFuture.sync();

            // bindFuture 객체를 통해서 8888번 포트에 바인딩된 서버 채널을 얻어온다.
            Channel serverChannel = bindFuture.channel();

            // 바인드가 완료된 서버채널의 CloseFuture 객체 반환
            // 네티 내부에서는 채널이 생성될 때 CloseFuture 객체도 같이 생성되므로
            // closeFuture() 메서드가 돌려주는 객체는 항상 동일
            ChannelFuture closeFuture = serverChannel.closeFuture();

            // closeFuture 객체는 채널의 연결이 종료될 때 연결 종료 이벤트를 받는다.
            closeFuture.sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }
    }
}
