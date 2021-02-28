package org.aery.study.java.io.tool;

import java.util.Scanner;

public class ClientLogger {

    public static final Scanner CONSOLE_SCANNER = new Scanner(System.in);

    public static String listen_console_input() {
        System.out.println("\n" + "--------------------------------------------");

        System.out.print("enter anything : ");
        String enter = CONSOLE_SCANNER.nextLine(); // 從console接受鍵盤輸入訊息

        return enter;
    }

    public static void client_finish() {
        String title = GeneralLogger.getTitle();
        System.out.println(title + "finish");
    }

    public static void server_close_the_connection() {
        String title = GeneralLogger.getTitle();
        System.out.println(title + "server close the connection");
    }

    public static String receive_response(String response) {
        String title = GeneralLogger.getTitle();
        return title + "[receive] " + response;
    }

    public static void client_intro() {
        System.out.println("enter \"exit\"  : client close the connect.");
        System.out.println("enter \"close\" : server close ths connect.");
    }

}
