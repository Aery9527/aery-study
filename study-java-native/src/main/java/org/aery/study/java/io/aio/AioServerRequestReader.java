package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.GeneralLogger;
import org.aery.study.java.io.tool.ServerLogger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

public class AioServerRequestReader extends AioServerRequestProcessor {

    private final AsynchronousSocketChannel channel;

    private final StringBuilder inputTemp = new StringBuilder();

    private final AioServerRequestWriter writer;

    public AioServerRequestReader(AsynchronousSocketChannel channel) {
        this.channel = channel;
        this.writer = new AioServerRequestWriter(super.getId(), channel);
    }

    public void continued() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        continued(buffer);
    }

    @Override
    public void continued(ByteBuffer buffer) {
        // 也可以建立新的buffer給予下次讀取使用
        this.channel.read(buffer, buffer, this);
    }

    @Override
    public void completed(Integer readBytes, ByteBuffer readBuffer) {
        if (readBytes < 0) { // client關閉連線
            close(CloseType.ClientClose);
            return;
        }

        boolean finishedReading = readBytes < readBuffer.limit();

        String input = read(readBuffer);

        if ("close".equals(input)) {
            close(CloseType.ServerClose);
            return;
        }

        ServerLogger.receive_input(super.getId(), input);

        this.inputTemp.append(input);

        if (finishedReading) {
            write();
        } else {
            readBuffer.clear();
            continued(readBuffer);
        }
    }

    @Override
    public void close(CloseType closeType) {
        try {
            this.channel.close();
        } catch (IOException e) {
            ServerLogger.error_channel_close(super.getId(), e);
        }

        if (CloseType.ServerClose.equals(closeType)) {
            ServerLogger.client_notice_server_to_close_the_connection(super.getId());
        } else if (CloseType.ClientClose.equals(closeType)) {
            ServerLogger.client_close_the_connection(super.getId());
        } else {
            GeneralLogger.connection_is_broken(super.getId());
        }
    }

    //

    private String read(ByteBuffer readBuffer) {
        readBuffer.flip();
        byte[] input = new byte[readBuffer.remaining()];
        readBuffer.get(input);

        return new String(input, StandardCharsets.UTF_8);
    }

    private void write() {
        String totalInput = this.inputTemp.toString();
        this.inputTemp.delete(0, this.inputTemp.length());

        this.writer.write(totalInput, this::continued, this::close);
    }

}
