package org.aery.study.jdk18;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.spi.InetAddressResolver;
import java.net.spi.InetAddressResolverProvider;
import java.util.Arrays;
import java.util.stream.Stream;

public class JEP418_InternetAddress_Resolution_SPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEP418_InternetAddress_Resolution_SPI.class);

    public static class Provider extends InetAddressResolverProvider {
        @Override
        public InetAddressResolver get(Configuration configuration) {
            return new Resolver();
        }

        @Override
        public String name() {
            return "JEP418_InternetAddress_Resolution_SPI#Resolver Provider";
        }
    }

    public static class Resolver implements InetAddressResolver {
        @Override
        public Stream<InetAddress> lookupByName(String host, LookupPolicy lookupPolicy) throws UnknownHostException {
            LOGGER.info("自訂解析邏輯: host={}, lookupPolicy={}", host, lookupPolicy.toString());
            return Stream.of(
                    InetAddress.getByName("127.0.0.1") // 原生解析邏輯
            ); // 自訂解析邏輯
        }

        @Override
        public String lookupByAddress(byte[] addr) {
            LOGGER.info("自訂反向解析邏輯: addr={}", Arrays.toString(addr));
            return "localhost";
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getByName("example.com");
        System.out.println("---");
        address.getHostName();
    }


}
