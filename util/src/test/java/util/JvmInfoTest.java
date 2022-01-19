package util;

import org.junit.Test;

import java.util.Map;

public class JvmInfoTest {

    @Test
    public void getJvmArguments() {
        JvmInfo.getJvmArguments().forEach(System.out::println);
    }

    @Test
    public void getEnvironment() {
        Map<String, String> environment = JvmInfo.getEnvironment();
        environment.entrySet().forEach(System.out::println);
    }

    @Test
    public void getProperties() {
        Map<String, String> properties = JvmInfo.getProperties();
        properties.entrySet().forEach(System.out::println);
    }

    @Test
    public void getHostname() {
        System.out.println(JvmInfo.getHostname());
    }
}
