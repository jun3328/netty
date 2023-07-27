package ch02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {
    public static void main(String[] args) throws IOException {
        BlockingServer server = new BlockingServer();
        server.run();
    }

    private void run() throws IOException {
        // 블로킹 소켓과 논블로킹 소켓은 데이터 송수신을 위한 함수의 동작 방식에 따른 분류
        // 블로킹 소켓은 ServerSocket, Socket 클래스 사용
        // 논블로킹 소켓 ServerSocketChannel, SocketChannel 클래스 사용
        ServerSocket sever = new ServerSocket(8888);
        System.out.println("접속 대기중");

        while (true) {
            Socket socket = sever.accept(); // 연결된 클라이언트가 없으면 프로그램은 아무 동직도 하지 않음

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            while (true) {
                int request = in.read(); // 데이터 입력을 기다리며 스레드가 블로킹되어 대기
                out.write(request);
            }
        }
    }
}
