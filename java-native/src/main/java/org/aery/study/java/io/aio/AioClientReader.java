package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.ClientLogger;
import util.MessageDisplayWindow;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

public class AioClientReader extends AioClientProcessor<MessageDisplayWindow> {

	private final StringBuilder inputTemp = new StringBuilder();

	public AioClientReader(String windowTitle, AsynchronousSocketChannel channel, Runnable closeAction) {
		super(windowTitle, channel, closeAction);
	}

	@Override
	public MessageDisplayWindow buildWindow(String windowTitle) {
		MessageDisplayWindow messageDisplayWindow = new MessageDisplayWindow(windowTitle);

		messageDisplayWindow.locationOffset(-300, -300);

		return messageDisplayWindow;
	}

	@Override
	public void completed(Integer readBytes, ByteBuffer readBuffer) {
		if (readBytes < 0) { // server關閉連線
			ClientLogger.server_close_the_connection();
			super.getCloseAction().run();
			return;
		}

		boolean finishedReading = readBytes < readBuffer.limit();

		String input = read(readBuffer);

		if ("exit".equals(input)) {
			super.getCloseAction().run();
			return;
		}

		this.inputTemp.append(input);

		ClientLogger.receive_response(input);

		if (finishedReading) {
			String totalInput = this.inputTemp.toString();
			super.getWindow().append(totalInput);
			this.inputTemp.delete(0, this.inputTemp.length());
		}

		readBuffer.clear();
		continued(readBuffer);
	}

	public void continued() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		continued(buffer);
	}

	@Override
	public void continued(ByteBuffer buffer) {
		super.getChannel().read(buffer, buffer, this);
	}

	private String read(ByteBuffer readBuffer) {
		readBuffer.flip();
		byte[] input = new byte[readBuffer.remaining()];
		readBuffer.get(input);

		return new String(input, StandardCharsets.UTF_8);
	}

}
