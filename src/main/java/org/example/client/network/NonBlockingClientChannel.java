package org.example.client.network;

import org.example.common.command.CommandRequest;
import org.example.common.command.CommandResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

public class NonBlockingClientChannel {
    private final String host;
    private final int port;

    public NonBlockingClientChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public CommandResponse sendCommand(CommandRequest request) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));

            while (!channel.finishConnect()) {
                Thread.sleep(10);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(request);
            oos.flush();
            byte[] data = bos.toByteArray();

            ByteBuffer buffer = ByteBuffer.wrap(data);
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.shutdownOutput();

            ByteBuffer readBuffer = ByteBuffer.allocate(8192);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = channel.read(readBuffer)) != -1) {
                readBuffer.flip();
                baos.write(readBuffer.array(), 0, bytesRead);
                readBuffer.clear();
            }

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            CommandResponse response = (CommandResponse) ois.readObject();

            oos.close();
            ois.close();
            channel.close();
            return response;

        } catch (Exception e) {
            System.out.println("Error: Server is temporarily unavailable");
            return null;
        }
    }
}