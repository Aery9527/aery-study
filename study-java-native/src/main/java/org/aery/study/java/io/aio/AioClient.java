package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.ClientLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioClient implements CompletionHandler<Void, AsynchronousSocketChannel> {

	public static void main(String[] args) throws Throwable {
		AioClient aioClient = new AioClient("127.0.0.1", 9527);
		aioClient.go();

		ClientLogger.client_finish();
	}

	private final Object runnableControl = new Object();

	private final AsynchronousSocketChannel socketChannel;

	private final AioClientWriter writer;

	private final AioClientReader reader;

	public AioClient(String host, int port) throws IOException {
		this.socketChannel = AsynchronousSocketChannel.open();

		this.writer = new AioClientWriter("[ClientRequest] " + host + ":" + port, socketChannel, this::close);
		this.reader = new AioClientReader("[ServerResponse] " + host + ":" + port, socketChannel, this::close);

		this.socketChannel.connect(new InetSocketAddress(host, port), socketChannel, this);
	}

	public void go() {
		ClientLogger.client_intro();

		synchronized (this.runnableControl) {
			try {
				this.runnableControl.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void completed(Void result, AsynchronousSocketChannel socketChannel) {
		this.reader.continued();

		this.writer.openWindow();
		this.reader.openWindow();
	}

	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel socketChannel) {
		exc.printStackTrace();

		synchronized (this.runnableControl) {
			this.runnableControl.notifyAll();
		}
	}

	public void close() {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		synchronized (this.runnableControl) {
			this.runnableControl.notifyAll();
		}
	}

}
