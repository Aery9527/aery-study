import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        printClassPath(Test.class.getClassLoader());
    }

    private static void printClassPath(ClassLoader classLoader) {
        ClassLoader parent = classLoader.getParent();
        if (parent != null) {
            printClassPath(parent);
        }

        URLClassLoader ucl = (URLClassLoader) classLoader;
        URL[] urls = ucl.getURLs();

        System.out.println();
        System.out.println("=================================================");
        System.out.println(classLoader + "(" + classLoader.getClass() + ")");
        System.out.println("=================================================");
        Arrays.asList(urls).forEach(System.out::println);
    }

}
