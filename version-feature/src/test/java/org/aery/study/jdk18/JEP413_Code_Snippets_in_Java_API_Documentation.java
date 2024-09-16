package org.aery.study.jdk18;

/**
 * {@snippet class = "JEP413_Code_Snippets_in_Java_API_Documentation" region = "region_target"}
 */
public class JEP413_Code_Snippets_in_Java_API_Documentation {

    /**
     * {@snippet :
     *   System.out.println("Hello, JEP 413!");
     *}
     * <br/>
     * <p>
     * {@snippet :
     *   System.out.println("Hello, JEP 413!"); // @highlight regex='".*"'
     *}
     * <br/>
     * <p>
     * {@snippet :
     *   System.out.println("Hello, JEP 413!"); // @link substring="println" target="PrintStream#println(String)"
     *}
     * <br/>
     * <p>
     * {@snippet :
     *    public static void main(String... args) {
     *        var text = "";                           // @replace substring='""' replacement="123"
     *        System.out.println(text);
     *    }
     *}
     * <br/>
     * <p>
     * {@snippet :
     *    public static void main(String... args) {    // @highlight region substring="text" type=highlighted
     *        var text = "";                           // @replace substring='""' replacement="123"
     *        System.out.println(text);
     *    }                                            // @end
     *}
     * <br/>
     * <p>
     * {@snippet :
     *    public static void main(String... args) {    // @highlight region=R1 substring="text" type=highlighted
     *        var text = "";                           // @replace substring='""' replacement="123"
     *        System.out.println(text);
     *    }                                            // @end region=R1
     *}
     */
    public static void main(String[] args) {
        // @start region="region_target"
        System.out.println("Hello, JEP 413!");
        // @end
    }

}
