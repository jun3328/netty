package ch02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class NonBlockingServer {
    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

    private void startEchoServer() {
        try (
                // 자바 1.7 이후부터 소괄호 안에서 선언된 자원은 try 블록이 끝날 때 자동으로 해제

                // 자바 NIO 컴포넌트 Selector,
                // 자신에게 등록된 채널에 변경사항이 발생했는지 검사하고, 변경사항이 발생한 채널에 대한 접근을 가능하게 함.
                Selector selector = Selector.open();
                // 블로킹 서버소켓(ServerSocket)에 대응되는 논블로킹 서버소켓
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
        ) {
            // 생성한 Selector 와 ServerSocketChannel 객체가 정상적으로 생성되었는지 확인
            if (serverSocketChannel.isOpen() && selector.isOpen()) {
                serverSocketChannel.configureBlocking(false); // 논블로킹 모드로 설정, 기본값은 true(블로킹 모드)
                serverSocketChannel.bind(new InetSocketAddress(8888)); // 연결 포트 지정

                // serverSocketChannel 객체를 Selector 객체에 등록, Selector 가 감지할 이벤트는 연결요청
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("접속 대기중");

                while (true) {
                    // Selector 에 등록된 채널에서 변경사항이 발생했는지 검사
                    // Selector 에 아무런 I/O 이벤트도 발생하지 않으면 스레드는 이부분에서 블로킹된다.
                    // 블로킹을 피하고 싶을 경우 selectNow 메서드를 사용
                    selector.select();

                    // Selector 에 등록된 채널 중에서 I/O 이벤트가 발생한 채널들의 목록을 조회
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove(); // I/O 이벤트가 발생한 채널에서 동일 이벤트 감지를 방지하기 위해 조회된 목록에서 제거

                        if (!key.isValid()) {
                            continue;
                        }

                        if (key.isAcceptable()) { // 조회된 I/O 이벤트 종류가 연결 요청인지 확인.
                            this.acceptOP(key, selector); // 연결처리 메서드로 이동

                        } else if (key.isReadable()) { // 조회된 I/O 이벤트 종류가 데이터 수신인지 확인.
                            this.readOP(key); // 데이터 읽기 처리 메서드로 이동

                        } else if (key.isWritable()) { // 조회된 I/O 이벤트 종류가 데이터 쓰기 가능인지 확인.
                            this.writeOP(key); // 데이터 쓰기 처리 메서드로 이동
                        }
                    }
                }

           } else {
                System.out.println("서버 소켓을 생성하지 못했습니다.");
           }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void acceptOP(SelectionKey key, Selector selector) throws IOException {
        // 연결 요청 이벤트가 발생한 채널은 항상 ServerSocketChannel 이므로, 채널을 캐스팅(형변환) 처리
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

        SocketChannel socketChannel = serverChannel.accept(); // 클라이언트 연결을 수락하고 연결된 소켓 채널을 가져옴
        socketChannel.configureBlocking(false); // 연결된 클라이언트 소켓 채널을 논블로킹 모드로 설정

        System.out.println("클라이언트 연결됨 : " + socketChannel.getRemoteAddress());

        keepDataTrack.put(socketChannel, new ArrayList<>());
        socketChannel.register(selector, SelectionKey.OP_READ); // 클라이언트 소켓 채널을 Selector 에 등록하여 I/O 이벤트 감시
    }

    private void readOP(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            buffer.clear();
            int numRead = -1;

            try {
                numRead = socketChannel.read(buffer);
            } catch (IOException e) {
                System.err.println("데이터 읽기 에러");
            }

            if (numRead == -1) {
                this.keepDataTrack.remove(socketChannel);
                System.out.println("클라이언트 연결 종료 : " + socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }

            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println(new String(data, "UTF-8") + "from " + socketChannel.getRemoteAddress());

            doEchoJob(key, data);

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void writeOP(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        Iterator<byte[]> its = channelData.iterator();

        while (its.hasNext()) {
            byte[] it = its.next();
            its.remove();
            socketChannel.write(ByteBuffer.wrap(it));
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    private void doEchoJob(SelectionKey key, byte[] data) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        channelData.add(data);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) {
        NonBlockingServer server = new NonBlockingServer();
        server.startEchoServer();
    }
}
