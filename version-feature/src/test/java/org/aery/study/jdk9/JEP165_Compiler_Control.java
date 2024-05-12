package org.aery.study.jdk9;

/**
 * <pre>
 * -XX:-Inline (禁用方法內聯, 某些情況下方法內聯會導致代碼膨脹影響性能)
 * -Dcompiler.unroll.limit=16 (設置循環展開的上限, 可以將循環中的迭代次數轉化為一系列獨立的指令，以減少循環開銷)
 * </pre>
 */
public class JEP165_Compiler_Control {

    public static void main(String[] args) {
        System.out.println("Hello World");
    }

}
