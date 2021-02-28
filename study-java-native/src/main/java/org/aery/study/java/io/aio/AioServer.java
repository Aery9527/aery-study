package org.aery.study.java.io.aio;

import org.aery.study.java.io.tool.GeneralLogger;
import org.aery.study.java.io.tool.ServerLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 處理模型: 一個請求由一個{@link AioServerRequestReader}接收input, 接著同時處理output之後, 再繼續接收input
 */
public class AioServer implements CompletionHandler<AsynchronousSocketChannel, Integer> {

	public static void main(String[] args) throws Throwable {
		GeneralLogger.ShowThread = true;
		
		int listenPort = 9527;
		AioServer aioServer = new AioServer(listenPort);

		aioServer.process();
//		nioServer.close();
	}

	private final String server = "AioServer";

	private final int listenPort;

	private final AsynchronousServerSocketChannel channel;

	public AioServer(int listenPort) throws IOException {
		this.listenPort = listenPort;

		this.channel = AsynchronousServerSocketChannel.open();
		this.channel.bind(new InetSocketAddress(listenPort)); // 綁port

		ServerLogger.server_open(this.server, this.listenPort);
	}

	private void process() {
		continued();

		synchronized (this) {
			try {
				wait(); // 主線等待, 避免程式終止
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ServerLogger.server_close(this.server, this.listenPort);
	}

	public synchronized void close() {
		notifyAll(); // 喚醒主線, 終止程式
	}

	public void continued() {
		this.channel.accept(this.listenPort, this);
	}

	@Override
	public void completed(AsynchronousSocketChannel channel, Integer attachment) {
		try {
			AioServerRequestReader asyncRequestReader = new AioServerRequestReader(channel);
			String id = asyncRequestReader.getId();

			SocketAddress remoteAddress = channel.getRemoteAddress();
			ServerLogger.accept_connection(id, remoteAddress);

			asyncRequestReader.continued();
			continued();

		} catch (IOException e) {
			ServerLogger.error_accept_connection(e);

			try {
				channel.close();
			} catch (IOException e1) {
				ServerLogger.error_channel_close(null, e1);
			}
		}
	}

	@Override
	public void failed(Throwable exc, Integer attachment) {
		ServerLogger.error(exc, this.server + " error");

		try {
			this.channel.close();
		} catch (IOException e) {
			ServerLogger.error_channel_close(null, e);
		}
	}

}
