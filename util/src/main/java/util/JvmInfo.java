package util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JvmInfo {

    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getJvmArguments() {
        return getJvmArguments(arg -> true);
    }

    public static List<String> getJvmArguments(Predicate<String> filter) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        return Collections.unmodifiableList(arguments.stream().filter(filter).collect(Collectors.toList()));
    }

    public static Map<String, String> getEnvironment() {
        return getEnvironment((key, val) -> true);
    }

    public static Map<String, String> getEnvironment(BiPredicate<String, String> filter) {
        Map<String, String> env = System.getenv();
        Map<String, String> result = env.entrySet().stream()
                .filter(entry -> {
                    String key = entry.getKey();
                    String val = entry.getValue();
                    return filter.test(key, val);
                })
                .map(entry -> Collections.singletonMap(entry.getKey(), entry.getValue()))
                .collect(TreeMap::new, Map::putAll, Map::putAll);
        return Collections.unmodifiableMap(result);
    }

    public static Map<String, String> getProperties() {
        return getProperties((key, val) -> true);
    }

    public static Map<String, String> getProperties(BiPredicate<String, String> filter) {
        Properties properties = System.getProperties();

        Map<String, String> result = new TreeMap<>();

        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            String val = properties.getProperty(key);
            if (filter.test(key, val)) {
                result.put(key, val);
            }
        }

        return Collections.unmodifiableMap(result);
    }


}
