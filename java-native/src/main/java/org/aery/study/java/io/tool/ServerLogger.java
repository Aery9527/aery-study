package org.aery.study.java.io.tool;

import java.net.InetAddress;
import java.net.SocketAddress;

public class ServerLogger {

    public static void print(String id, String msg) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + msg);
    }

    public static void server_open(String server, int listenPort) {
        String title = GeneralLogger.getTitle();
        System.out.println(title + server + "(" + listenPort + ") is open!");
    }

    public static void server_close(String server, int listenPort) {
        String title = GeneralLogger.getTitle();
        System.out.println(title + server + "(" + listenPort + ") is close!");
    }

    public static void error(Throwable t) {
        String title = GeneralLogger.getTitle();
        System.out.println(title + GeneralLogger.error(t, null));
    }

    public static void error(Throwable t, String msg) {
        String title = GeneralLogger.getTitle();
        System.out.println(title + GeneralLogger.error(t, msg));
    }

    public static void error(String id, Throwable t) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + GeneralLogger.error(t, null));
    }

    public static void error(String id, Throwable t, String msg) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + GeneralLogger.error(t, msg));
    }

    public static void error_accept_connection(Throwable t) {
        String title = GeneralLogger.getTitle();
        System.out.println(title + GeneralLogger.error(t, "accept connection error"));
    }

    public static void error_channel_close(String id, Throwable t) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + GeneralLogger.error(t, "channel colse error"));
    }

    public static void accept_connection(String id, InetAddress remoteAddress, int port) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + "accept connection from " + remoteAddress + ":" + port);
    }

    public static void accept_connection(String id, SocketAddress remoteAddress) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + "accept connection from " + remoteAddress);
    }

    public static void receive_input(String id, String input) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + "server receive : " + input);
    }

    public static void client_notice_server_to_close_the_connection(String id) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + "client notice server to close the connection");
    }

    public static void client_close_the_connection(String id) {
        String title = GeneralLogger.getTitle(id);
        System.out.println(title + "client close the connection");
    }

    public static String output(String id, String output) {
        String title = GeneralLogger.getTitle(id);
        return title + "[output] " + output;
    }

}
