package org.aery.study.java.io.aio;

import util.MessageReceiveWindow;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClientWriter extends AioClientProcessor<MessageReceiveWindow> {

	public AioClientWriter(String windowTitle, AsynchronousSocketChannel channel, Runnable closeAction) {
		super(windowTitle, channel, closeAction);
	}

	@Override
	public MessageReceiveWindow buildWindow(String windowTitle) {
		MessageReceiveWindow messageReceiveWindow = new MessageReceiveWindow(windowTitle);
		messageReceiveWindow.locationOffset(300, -300);
		messageReceiveWindow.setInputReceiver(this::write);

		return messageReceiveWindow;
	}

	public void write(String input) {
		byte[] bytes = input.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();

		super.getChannel().write(writeBuffer, writeBuffer, this);
	}

	@Override
	public void completed(Integer result, ByteBuffer writeBuffer) {
		if (writeBuffer.hasRemaining()) {
			super.getChannel().write(writeBuffer, writeBuffer, this); // 沒寫完繼續寫
		}
	}

	@Override
	public void continued(ByteBuffer buffer) {
		// no need
	}

}
