package wg.im.common;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        NioSocketChannel nioSocketChannel = new NioSocketChannel();
        ByteBuffer.allocate(1024);
        ByteBuffer.allocateDirect(1024);
    }
}
