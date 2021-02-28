package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.RandomID;
import org.aery.study.java.io.tool.ServerLogger;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public abstract class AioServerRequestProcessor implements CompletionHandler<Integer, ByteBuffer> {

    public enum CloseType {
        ServerClose, ClientClose, ConnectionBroken;
    }

    private final String id;

    public AioServerRequestProcessor() {
        this.id = RandomID.get();
    }

    public AioServerRequestProcessor(String id) {
        this.id = id;
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        ServerLogger.error(exc, getClass().getSimpleName());
        close(CloseType.ConnectionBroken);
    }

    public abstract void close(CloseType closeType);

    public abstract void continued(ByteBuffer buffer);

    public String getId() {
        return id;
    }

}
