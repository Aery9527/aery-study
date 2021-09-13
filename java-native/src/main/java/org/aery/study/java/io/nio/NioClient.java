package org.aery.study.java.io.nio;

import org.aery.study.java.io.tool.ClientLogger;
import org.aery.study.java.io.tool.GeneralLogger;
import util.MessageDisplayWindow;
import util.MessageReceiveWindow;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioClient {

    public static void main(String[] args) throws Throwable {
        NioClient nioClient = new NioClient("127.0.0.1", 9527);
        nioClient.go();

        ClientLogger.client_finish();
    }

    private final MessageDisplayWindow messageDisplayWindow;

    private final MessageReceiveWindow messageReceiveWindow;

    private final StringBuffer input = new StringBuffer();

    private final AtomicBoolean running = new AtomicBoolean(true);

    private final Selector selector;

    private final SocketChannel socketChannel;

    private boolean connectionBrokenFlag = false;

    public NioClient(String host, int port) throws IOException {
        this.messageDisplayWindow = new MessageDisplayWindow("[ServerResponse] " + host + ":" + port);
        this.messageReceiveWindow = new MessageReceiveWindow("[ClientRequest] " + host + ":" + port);
        this.selector = Selector.open(); // 打開選擇器

        this.socketChannel = SocketChannel.open(); // 開啟準備要連線的SocketChannel
        this.socketChannel.configureBlocking(false); // 設定為非阻塞模式, 若設為true就等同以前的BIO模式
        this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
        this.socketChannel.connect(new InetSocketAddress(host, port));

        this.messageReceiveWindow.setInputReceiver((msg) -> {
            this.input.delete(0, this.input.length());
            this.input.append(msg);
            synchronized (this.input) {
                this.input.notify();
            }
        });

        this.messageDisplayWindow.locationOffset(-300, -300);
        this.messageReceiveWindow.locationOffset(300, -300);

        this.messageDisplayWindow.show();
        this.messageReceiveWindow.show();

        Thread serverResponseReceiverThread = new Thread(this::readServerResponse, "ResponseReceiver");
        serverResponseReceiverThread.start();
    }

    public void go() throws IOException {
        try {
            ClientLogger.client_intro();

            while (true) {
                synchronized (this.input) {
                    if (this.running.get()) {
                        this.input.wait();
                    }
                }

                if (!this.selector.isOpen()) {
                    break;
                }

                String enter = this.input.toString();

                if ("exit".equals(enter)) {
                    break;
                }

                byte[] clientOutputBytes = enter.getBytes();

                ByteBuffer writeBuffer = ByteBuffer.allocate(clientOutputBytes.length);
                writeBuffer.put(clientOutputBytes);
                writeBuffer.flip();

                try {
                    socketChannel.write(writeBuffer);
                } catch (IOException e) {
                    GeneralLogger.connection_is_broken();
                    break;
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();

        } finally {
            this.selector.close();
            this.socketChannel.close();

            if (this.connectionBrokenFlag) {
                this.messageDisplayWindow.append("connection is broken.");
                this.messageReceiveWindow.append("connection is broken.");
            } else {
                this.messageDisplayWindow.append("connection is close.");
                this.messageReceiveWindow.append("connection is close.");
            }
        }
    }

    //

    private void readServerResponse() {
        try {
            while (true) {
//				this.selector.select(1000); // 無論註冊的channel是否有事件發生, 每隔1s就會被喚醒
                selector.select(); // 阻塞至最少一個事件發生才會被喚醒

                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> keysIterator = keys.iterator();
                while (keysIterator.hasNext()) {
                    SelectionKey key = keysIterator.next();
                    keysIterator.remove(); // 若不移除此key, 下一次仍會存在keys裡而被處理到...不懂為什麼要這麼麻煩...超不合理的...

                    try {
                        boolean finish = readServerResponse(key);
                        if (finish) {
                            return; // 結束
                        }

                    } catch (Throwable t) {
                        t.printStackTrace();

                        key.cancel();
                        key.channel().close();
                    }
                }
            }

        } catch (ClosedSelectorException e) {
            // this.selector已被關閉

        } catch (Throwable t) {
            t.printStackTrace();

        } finally {
            try {
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (this.input) {
                this.input.notify();
            }
        }
    }

    private boolean readServerResponse(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        if (key.isConnectable()) {
            while (!socketChannel.finishConnect()) {
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (socketChannel.isConnected()) {
                key.interestOps(SelectionKey.OP_READ); // 將此key多加read的操作
            }

            return false;
        }

        if (key.isReadable()) {
            return readServerResponse(socketChannel);
        }

        return false;
    }

    private boolean readServerResponse(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int readBytes;
        try {
            readBytes = socketChannel.read(buffer); // 讀取input至buffer並回傳已讀取的bytes數, 若內容超過buffer數量, 下一次select仍有該事件存在
        } catch (IOException e) {
            this.connectionBrokenFlag = true;
            GeneralLogger.connection_is_broken();
            return true;
        }

        if (readBytes < 0) {
            ClientLogger.server_close_the_connection();
            return true;
        } else if (readBytes == 0) { // 沒有input也有event?
            String receive = ClientLogger.receive_response("<not content>");
            this.messageDisplayWindow.append(receive);
            return false;
        }

        buffer.flip(); // 將此buffer刷新(指針歸0), 使後續可以完整讀取buffer內容
        byte[] serverResponseBytes = new byte[buffer.remaining()]; // 根據還未讀取的byte數量建立byte array
        buffer.get(serverResponseBytes); // 將buffer內容寫進byte array

        String serverResponse = new String(serverResponseBytes, StandardCharsets.UTF_8);
        serverResponse = serverResponse.trim();

        String receive = ClientLogger.receive_response("(" + readBytes + " bytes) " + serverResponse);
        this.messageDisplayWindow.append(receive);

        return false;
    }

}
