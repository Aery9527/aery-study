package org.aery.study.jdk9;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class JEP259_StackWalker {

    @Test
    void stacktrace() {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace(); // 舊方法

        StackWalker walker = StackWalker.getInstance();

        Assertions.assertThatThrownBy(walker::getCallerClass)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("This stack walker does not have RETAIN_CLASS_REFERENCE access");

        walker.forEach(frame -> {
            Assertions.assertThatThrownBy(frame::getDeclaringClass)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("No access to RETAIN_CLASS_REFERENCE");
            Assertions.assertThatThrownBy(frame::getMethodType)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("No access to RETAIN_CLASS_REFERENCE");

            String className = frame.getClassName();
            String methodName = frame.getMethodName();
            String fileName = frame.getFileName();
            int lineNumber = frame.getLineNumber();
            String descriptor = frame.getDescriptor(); // 格式: (參數類型)返回類型
            int byteCodeIndex = frame.getByteCodeIndex(); // 字節碼索引, 在 @Test getByteCodeIndex() 另有說明
            boolean isNativeMethod = frame.isNativeMethod(); // 是否為 native 方法
            StackTraceElement stackTraceElement = frame.toStackTraceElement(); // 轉成舊方法的物件

            String frameToString = frame.toString();
            String showFormat = className + "." + methodName + "(" + fileName + ":" + lineNumber + ")";
            Assertions.assertThat(frameToString).endsWith(showFormat); // 判斷endsWith, 是因為如果 module 的話前面則會有 module名稱

            System.out.println(frame + " | " + descriptor);
        });
    }

    /**
     * <pre>
     * - 說明: 保留每個堆疊幀中 Class 物件的參考
     * - 用途: 允許使用 {@link StackWalker#getCallerClass} 和 {@link StackWalker.StackFrame#getDeclaringClass} 方法來獲取堆疊幀的類資訊
     * - 使用情境: 當需要訪問調用堆疊中的類資訊時, 必須使用這個選項
     * </pre>
     */
    @Test
    void stacktrace_RETAIN_CLASS_REFERENCE() {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

        Class<?> callerClass = walker.getCallerClass(); // 誰呼叫這個 method 的
        System.out.println(callerClass + " : " + callerClass);

        walker.forEach(frame -> {
            Class<?> declaringClass = frame.getDeclaringClass();
            System.out.println(frame + " | " + declaringClass);
        });
    }

    /**
     * <pre>
     * - 說明: 包含反射方法 {@link java.lang.reflect.Method#invoke} 和 {@link java.lang.reflect.Constructor#newInstance} 等的堆疊幀
     * - 用途: 顯示堆疊中包含反射調用的幀
     * - 使用情境: 當需要查看或處理通過反射機制調用的方法堆疊幀時, 可以使用這個選項
     * </pre>
     */
    @Test
    void stacktrace_SHOW_REFLECT_FRAMES() {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.SHOW_REFLECT_FRAMES);
        walker.forEach(System.out::println);
    }

    /**
     * <pre>
     * - 說明: 包含 JVM 隱藏的堆疊幀, 如一些內部方法的幀
     * - 用途: 顯示所有通常隱藏的堆疊幀
     * - 使用情境: 當需要完整的堆疊信息, 包括那些通常隱藏的 JVM 內部幀時
     * </pre>
     */
    @Test
    void stacktrace_SHOW_HIDDEN_FRAMES() {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.SHOW_HIDDEN_FRAMES);
        walker.forEach(System.out::println);
    }

    /**
     * 字節碼索引 (Bytecode Index, BCI), 這個索引表示當前執行位置在方法中的具體字節碼指令的偏移量(從方法的起始位置算起),
     * 它是 JVM 用來跟踪方法執行位置的內部機制
     */
    @Test
    void getByteCodeIndex() {
//        int a = 1; // 這行打開可以看到 ByteCodeIndex 的變化, 但我不曉得怎麼識別
        StackWalker walker = StackWalker.getInstance();
        walker.forEach(frame -> System.out.println(frame + " | " + frame.getByteCodeIndex()));
    }

}
