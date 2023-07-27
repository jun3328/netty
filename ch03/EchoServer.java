package ch03;

import ch01.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        // 논블로킹 입출력 모드를 지원하는 ServerBootstrap 초기화

        // 클라이언트의 연결을 수락하는 부모 스레드 그룹(단일 스레드)
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 연결된 클라이언트의 소켓으로부터 데이터 입출력 및 이벤트 처리를 담당하는 자식 스레드 그룹
        // 인수 없는 생성자는 사용할 스레드 수를 CPU 코어 수의 2배를 사용
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 인수없는 생성자로 ServerBootstrap 생성 후 group(), channel() 과 같은 메서드로 객체를 초기화
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) // 서버 소켓이 사용할 네트워크 입출력 모드를 NIO 로 설정
                .childHandler(new ChannelInitializer<SocketChannel>() { // 자식 채널의 초기화 방법을 설저
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline(); // 채널 파이프라인 객체 생성
                        p.addLast(new EchoServerHandler()); // 클라이언트의 연결이 생성되었을때 데이터 처리담당 클래스 설정
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
