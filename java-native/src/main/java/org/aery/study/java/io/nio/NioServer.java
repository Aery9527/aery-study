package org.aery.study.java.io.nio;


import org.aery.study.java.io.tool.GeneralLogger;
import org.aery.study.java.io.tool.RandomID;
import org.aery.study.java.io.tool.ServerLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NioServer {

    public static void main(String[] args) throws Throwable {
        GeneralLogger.ShowThread = true;

        int listenPort = 9527;
        NioServer nioServer = new NioServer(listenPort);

        nioServer.process();
//		nioServer.close();
    }

    private final String server = "NioServer";

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Map<SocketChannel, String> selectionKeyAndIdMap = new WeakHashMap<>();

    private final int listenPort;

    private final Selector selector;

    public NioServer(int listenPort) throws IOException {
        this.listenPort = listenPort;

        this.selector = Selector.open(); // 打開選擇器

        ServerSocketChannel serverChannel = ServerSocketChannel.open(); // 打開監聽通道
        serverChannel.configureBlocking(false); // 設定為非阻塞模式, 若設為true就等同以前的BIO模式
        serverChannel.bind(new InetSocketAddress(listenPort), 1024); // 開始listen, 1024為queue數量
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT); // 將此channel註冊至selector

        ServerLogger.server_open(this.server, this.listenPort);
    }

    public void process() {
        try {
            while (this.isRunning.get()) {
                this.selector.select(1000); // 無論註冊的channel是否有事件發生, 每隔1s就會被喚醒
//				selector.select(); // 阻塞至最少一個事件發生才會被喚醒

                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> keysIterator = keys.iterator();
                while (keysIterator.hasNext()) {
                    SelectionKey key = keysIterator.next();
                    keysIterator.remove(); // 若不移除此event依然會存在, 下一次仍會存在keys裡而被處理到...不懂為什麼要這麼麻煩??

                    try {
                        process(key);

                    } catch (Throwable t) {
                        t.printStackTrace();

                        key.cancel();
                        key.channel().close();
                    }
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();

        } finally {
            if (this.isRunning.get()) {
                close();
            }
        }
    }

    public void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) { // 為server listen port收到訊息觸發的事件
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = ssc.accept(); // 此操作完成TCP三次交握
            socketChannel.configureBlocking(false); // 設為非阻塞
            socketChannel.register(this.selector, SelectionKey.OP_READ); // 將此channel註冊回selector, 設為可讀

            getId(socketChannel, (rid) -> ServerLogger.print(rid, "[Acceptable] from " + socketChannel));
        }

        if (key.isReadable()) { // 為上述SocketChannel被觸發的事件
            SocketChannel socketChannel = (SocketChannel) key.channel();
            String id = getId(socketChannel);

            boolean needClose = process(id, socketChannel);
            if (needClose) {
                key.cancel(); // 會將此對應的channel從selector移除
                socketChannel.close();
            }
        }
    }

    public void close() {
        boolean lastStatus = this.isRunning.getAndSet(false);
        if (!lastStatus) {
            throw new RuntimeException("ServerSocket:" + this.listenPort + " has been close!");
        }

        try {
            this.selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //

    private boolean process(String id, SocketChannel socketChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int readBytes;
            try {
                readBytes = socketChannel.read(buffer); // 讀取input至buffer並回傳已讀取的bytes數, 若內容超過buffer數量, 下一次select仍有該事件存在
            } catch (IOException e) {
                GeneralLogger.connection_is_broken(id);
                return true;
            }

            if (readBytes < 0) {
                ServerLogger.client_close_the_connection(id);
                return true;
            } else if (readBytes == 0) { // 沒有input也有event?
                ServerLogger.print(id, "not content");
                return false;
            }

            buffer.flip(); // 將此buffer刷新(指針歸0), 使後續可以完整讀取buffer內容
            byte[] clientInputBytes = new byte[buffer.remaining()]; // 根據還未讀取的byte數量建立byte array
            buffer.get(clientInputBytes); // 將buffer內容寫進byte array

            String clientInput = new String(clientInputBytes, StandardCharsets.UTF_8);
            clientInput = clientInput.trim();

            ServerLogger.receive_input(id, "(" + readBytes + " bytes) " + clientInput);

            if ("close".equals(clientInput)) {
                ServerLogger.client_notice_server_to_close_the_connection(id);
                return true;
            }

            String serverOutput = clientInput + "\n"; // 因為client是讀取到換行為一次處理
            byte[] serverOutputBytes = serverOutput.getBytes();

            ByteBuffer writeBuffer = ByteBuffer.allocate(serverOutputBytes.length);
            writeBuffer.put(serverOutputBytes);
            writeBuffer.flip();

            try {
                socketChannel.write(writeBuffer); // 不阻塞
            } catch (IOException e) {
                GeneralLogger.connection_is_broken(id);
                return true;
            }

            return false;

        } catch (Throwable t) {
            t.printStackTrace();

            return true;
        }
    }

    private String getId(SocketChannel channel) {
        return getId(channel, (id) -> {
        });
    }

    private String getId(SocketChannel channel, Consumer<String> firstAction) {
        String id = this.selectionKeyAndIdMap.get(channel);
        if (id != null) {
            return id;
        }

        synchronized (this.selectionKeyAndIdMap) {
            id = this.selectionKeyAndIdMap.get(channel);
            if (id != null) {
                return id;
            }

            id = RandomID.get();
            this.selectionKeyAndIdMap.put(channel, id);
        }

        firstAction.accept(id);

        return id;
    }

    private void printKeyInfo(SelectionKey key, Consumer<String> println) {
        boolean isValid = key.isValid(); // 當key操作了cancel()之後會為false, 但同時也會將此key移出, 所以基本上都不會是false

        // 以下四個分別對應SelectionKey的四種操作, 當註冊channel的時候給予什麼就會是什麼, 可以複選
        boolean isAcceptable = key.isAcceptable(); // OP_ACCEPT
        boolean isConnectable = key.isConnectable(); // OP_CONNECT
        boolean isReadable = key.isReadable(); // OP_READ
        boolean isWritable = key.isWritable(); // OP_WRITE

        String booleanFormat = "%-5s";
        String format = "isValid(" + booleanFormat + ")";
        format += " isAcceptable(" + booleanFormat + ")";
        format += " isConnectable(" + booleanFormat + ")";
        format += " isReadable(" + booleanFormat + ")";
        format += " isWritable(" + booleanFormat + ")";

        println.accept("-------------------------------------------------------------------------------------------");
        println.accept(String.format(format, isValid, isAcceptable, isConnectable, isReadable, isWritable));
        println.accept("-------------------------------------------------------------------------------------------");
        println.accept("key     : " + key);
        println.accept("channel : " + key.channel());
    }

}
