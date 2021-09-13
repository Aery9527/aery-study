package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.ServerLogger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Consumer;

public class AioServerRequestWriter extends AioServerRequestProcessor {

	private final AsynchronousSocketChannel channel;

	private Runnable writeFinishAction;

	private Consumer<CloseType> closeAction;

	public AioServerRequestWriter(String id, AsynchronousSocketChannel channel) {
		super(id);
		this.channel = channel;
	}

	public void write(String output, Runnable writeFinishAction, Consumer<CloseType> closeAction) {
		this.writeFinishAction = writeFinishAction;
		this.closeAction = closeAction;

		byte[] bytes = output.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();

		continued(writeBuffer);
	}

	@Override
	public void completed(Integer writeBytes, ByteBuffer writeBuffer) {
		System.out.println(ServerLogger.output(super.getId(), writeBytes.toString() + " Bytes"));

		if (writeBuffer.hasRemaining()) {
			continued(writeBuffer);
		} else {
			this.writeFinishAction.run();
			this.writeFinishAction = null;
			this.closeAction = null;
		}
	}

	@Override
	public void close(CloseType closeType) {
		this.closeAction.accept(closeType);
	}

	@Override
	public void continued(ByteBuffer buffer) {
		this.channel.write(buffer, buffer, this);
	}

}
