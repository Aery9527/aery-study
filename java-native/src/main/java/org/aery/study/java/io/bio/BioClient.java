package org.aery.study.java.io.bio;

import org.aery.study.java.io.tool.ClientLogger;
import org.aery.study.java.io.tool.GeneralLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class BioClient {

    public static void main(String[] args) throws Throwable {
        BioClient bioClient = new BioClient("127.0.0.1", 9527);
        bioClient.go();

        ClientLogger.client_finish();
    }

    private final Socket socket;

    public BioClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public void go() {
        try ( // 透過包裝物件處理對server socket的IO
              BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //
              PrintWriter out = new PrintWriter(socket.getOutputStream(), true) //
        ) {
            ClientLogger.client_intro();

            while (true) {
                String enter = ClientLogger.listen_console_input(); // 從console接受鍵盤輸入訊息

                if ("exit".equals(enter)) {
                    break;
                }

                out.println(enter);

                if (out.checkError()) {
                    GeneralLogger.connection_is_broken();
                    break;
                }

                String serverResponse;
                try {
                    serverResponse = in.readLine(); // 等待server回應
                } catch (SocketException e) { // 與server斷線會拋出此錯誤 (網路中斷, server程式強制中斷, ...等)
                    GeneralLogger.connection_is_broken();
                    break;
                }

                if (serverResponse == null) { // server正常關閉連線時會回傳null
                    ClientLogger.server_close_the_connection();
                    break;
                } else {
                    ClientLogger.receive_response(serverResponse);
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
