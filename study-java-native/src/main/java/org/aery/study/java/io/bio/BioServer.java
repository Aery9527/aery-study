package org.aery.study.java.io.bio;


import org.aery.study.java.io.tool.GeneralLogger;
import org.aery.study.java.io.tool.RandomID;
import org.aery.study.java.io.tool.ServerLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BioServer {

	public static void main(String[] args) throws Throwable {
		GeneralLogger.ShowThread = true;
		
		int listenPort = 9527;
		BioServer bioServer = new BioServer(listenPort);

		bioServer.process();
//		bioServer.close();
	}

	private final String server = "BioServer";

	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	private final AtomicBoolean isRunning = new AtomicBoolean(true);

	private final int listenPort;

	private final ServerSocket serverSocket;

	public BioServer(int listenPort) throws IOException {
		this.listenPort = listenPort;
		this.serverSocket = new ServerSocket(this.listenPort);

		ServerLogger.server_open(this.server, this.listenPort);
	}

	public void process() {
		try {
			while (this.isRunning.get()) {
				Socket socket = this.serverSocket.accept(); // 阻塞, 會一直等到有請求進入該thread才會被喚醒, 因此一個請求會有一個對應的Socket

				this.threadPool.execute(() -> { // 透過thread pool, 委派某個thread執行此socket請求, 主線可以回去取得或等待下一個請求
					String id = RandomID.get();

					InetAddress inetAddress = socket.getInetAddress();
					int port = socket.getPort();

					ServerLogger.accept_connection(id, inetAddress, port);

					process(socket, id);
				});
			}

		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			if (this.isRunning.get()) {
				close();
			}
		}
	}

	public void process(Socket socket, String id) {
		try ( // 透過包裝的物件處理此socket的IO
			  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //
			  PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true) //
		) {
			while (true) {
				String clientInput;
				try {
					clientInput = bufferedReader.readLine(); // 阻塞至client輸入內容有換行出現
				} catch (SocketException e) { // 與client斷線會拋出此錯 (網路中斷, client程式強制中斷, ...等)
					GeneralLogger.connection_is_broken(id);
					break;
				}

				if (clientInput == null) { // client正常關閉連線時會回傳null
					ServerLogger.client_close_the_connection(id);
					break;
				}

				ServerLogger.receive_input(id, clientInput);

				if ("close".equals(clientInput)) {
					ServerLogger.client_notice_server_to_close_the_connection(id);
					break;
				}

				String serverOutput = ServerLogger.output(id, clientInput);
				printWriter.println(serverOutput);
			}

		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			try {
				socket.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}

			ServerLogger.server_close(id, this.listenPort);
		}
	}

	public void close() {
		boolean lastStatus = this.isRunning.getAndSet(false);
		if (!lastStatus) {
			throw new RuntimeException("BioServer:" + this.listenPort + " has been close!");
		}

		try {
			this.serverSocket.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		try {
			this.threadPool.shutdown();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
