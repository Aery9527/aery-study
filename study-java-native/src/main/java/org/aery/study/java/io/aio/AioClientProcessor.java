package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.GeneralLogger;
import util.MessageDisplayWindow;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public abstract class AioClientProcessor<WindowType extends MessageDisplayWindow>
		implements CompletionHandler<Integer, ByteBuffer> {

	private final MessageDisplayWindow window;

	private final AsynchronousSocketChannel channel;

	private final Runnable closeAction;

	public AioClientProcessor(String windowTitle, AsynchronousSocketChannel channel, Runnable closeAction) {
		this.window = buildWindow(windowTitle);
		this.channel = channel;
		this.closeAction = closeAction;
	}

	public abstract WindowType buildWindow(String windowTitle);

	public abstract void continued(ByteBuffer buffer);

	public void openWindow() {
		this.window.show();
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		exc.printStackTrace();

		GeneralLogger.connection_is_broken();
		this.closeAction.run();
	}

	public AsynchronousSocketChannel getChannel() {
		return channel;
	}

	public Runnable getCloseAction() {
		return closeAction;
	}

	public MessageDisplayWindow getWindow() {
		return window;
	}

}
