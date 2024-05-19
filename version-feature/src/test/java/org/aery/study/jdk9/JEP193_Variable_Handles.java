package org.aery.study.jdk9;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * <pre>
 * - acquire : 用在讀取上, 保證內存訪問, 也就是之後的指令不會被優化重排到acquire之前, 確保之後的操作對其他thread是可見的
 * - release : 用在寫入上, 保證內存訪問, 也就是之前的指令不會被優化重排到release之後, 確保之前的操作對其他thread是可見的
 * - opaque : 用於性能優化狀況, 是最弱的內存保證, 只保證讀寫操作本身的原子性, 該操作前後的指令有可能因為指令優化被重排到操作之前或之後,
 * - volatile : 如同 volatile 修飾詞, 其實也就是acquire/release/opaque內存保證的組合
 * - bitwise : 位元原子性操作, 很適合拿來做 bit flag
 * </pre>
 */
public class JEP193_Variable_Handles {

    private static class ArrayQuiz {
        private static int[] array1 = {1, 2};
        private static int[][] array2 = {{1}, {2}};
    }

    private static class Initial {
        private static int STATIC_INT = 5566;
        private static String STATIC_STRING = "5566";
        private static byte STATIC_BYTE = 0b1010;
        private static int INSTANCE_INT = 9527;
        private static String INSTANCE_STRING = "9527";
        private static byte INSTANCE_BYTE = 0b0110;
    }

    private static final VarHandle STATIC_INT_VH;

    private static final VarHandle STATIC_STRING_VH;

    private static final VarHandle STATIC_BYTE_VH;

    private static final VarHandle INSTANCE_INT_VH;

    private static final VarHandle INSTANCE_STRING_VH;

    private static final VarHandle INSTANCE_BYTE_VH;

    private static int STATIC_INT;

    private static String STATIC_STRING;

    private static byte STATIC_BYTE;

    static {
        try {
            Class<?> clazz = JEP193_Variable_Handles.class;
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            STATIC_INT_VH = lookup.findStaticVarHandle(clazz, "STATIC_INT", int.class);
            STATIC_STRING_VH = lookup.findStaticVarHandle(clazz, "STATIC_STRING", String.class);
            STATIC_BYTE_VH = lookup.findStaticVarHandle(clazz, "STATIC_BYTE", byte.class);
            INSTANCE_INT_VH = lookup.findVarHandle(clazz, "instanceInt", int.class);
            INSTANCE_STRING_VH = lookup.findVarHandle(clazz, "instanceString", String.class);
            INSTANCE_BYTE_VH = lookup.findVarHandle(clazz, "instanceByte", byte.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int instanceInt;

    private String instanceString;

    private byte instanceByte;

    @BeforeEach
    void setUp() {
        STATIC_INT = Initial.STATIC_INT;
        STATIC_STRING = Initial.STATIC_STRING;
        STATIC_BYTE = Initial.STATIC_BYTE;
        this.instanceInt = Initial.INSTANCE_INT;
        this.instanceString = Initial.INSTANCE_STRING;
        this.instanceByte = Initial.INSTANCE_BYTE;
    }

    @Test
    void incorrect_invoke() {
        // 因為是 static filed, 不需要丟instance進去
        Assertions.assertThatThrownBy(() -> STATIC_INT_VH.get(this))
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> STATIC_STRING_VH.get(this))
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> STATIC_BYTE_VH.get(this))
                .isInstanceOf(WrongMethodTypeException.class);

        // 因為是 instance filed, 需要丟instance進去
        Assertions.assertThatThrownBy(() -> INSTANCE_INT_VH.get())
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> INSTANCE_STRING_VH.get())
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> INSTANCE_BYTE_VH.get())
                .isInstanceOf(WrongMethodTypeException.class);
    }

    /**
     * 取得該filed的值, 另外還有
     * {@link VarHandle#getVolatile(Object...)},
     * {@link VarHandle#getOpaque(Object...)},
     * {@link VarHandle#getAcquire(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void get() {
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo(Initial.INSTANCE_STRING);
    }

    /**
     * 先返回當前的值再更新該值. 另外還有
     * {@link VarHandle#getAndSetAcquire(Object...)},
     * {@link VarHandle#getAndSetRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void getAndSet() {
        int setInt = 1;
        String setString = "Aery";

        Assertions.assertThat(STATIC_INT_VH.getAndSet(setInt)).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(setInt);

        Assertions.assertThat(STATIC_STRING_VH.getAndSet(setString)).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo(setString);

        Assertions.assertThat(INSTANCE_INT_VH.getAndSet(this, setInt)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(setInt);

        Assertions.assertThat(INSTANCE_STRING_VH.getAndSet(this, setString)).isEqualTo(Initial.INSTANCE_STRING);
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo(setString);

        Assertions.assertThatThrownBy(() -> STATIC_INT_VH.getAndSet(setString))
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> STATIC_STRING_VH.getAndSet(setInt))
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> INSTANCE_INT_VH.getAndSet(this, setString))
                .isInstanceOf(WrongMethodTypeException.class);
        Assertions.assertThatThrownBy(() -> INSTANCE_STRING_VH.getAndSet(this, setInt))
                .isInstanceOf(WrongMethodTypeException.class);
    }

    /**
     * 先返回當前的值再加上某個值後更新. 另外還有
     * {@link VarHandle#getAndAddAcquire(Object...)},
     * {@link VarHandle#getAndAddRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void getAndAdd() {
        int add = 1;

        Assertions.assertThat(STATIC_INT_VH.getAndAdd(add)).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(Initial.STATIC_INT + add);

        Assertions.assertThatThrownBy(() -> STATIC_STRING_VH.getAndAdd(add))
                .isInstanceOf(UnsupportedOperationException.class);
        Assertions.assertThatThrownBy(() -> STATIC_STRING_VH.getAndAdd(""))
                .isInstanceOf(UnsupportedOperationException.class);

        Assertions.assertThat(INSTANCE_INT_VH.getAndAdd(this, add)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(Initial.INSTANCE_INT + add);

        Assertions.assertThatThrownBy(() -> INSTANCE_STRING_VH.getAndAdd(this, add))
                .isInstanceOf(UnsupportedOperationException.class);
        Assertions.assertThatThrownBy(() -> INSTANCE_STRING_VH.getAndAdd(this, ""))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    /**
     * 先返回當前的值再操作bit AND 後更新. 另外還有
     * {@link VarHandle#getAndBitwiseAndAcquire(Object...)},
     * {@link VarHandle#getAndBitwiseAndRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void getAndBitwiseAnd() {
        byte flag = 0b1001;

        Assertions.assertThat(STATIC_BYTE_VH.getAndBitwiseAnd(flag)).isEqualTo(Initial.STATIC_BYTE);
        Assertions.assertThat(STATIC_BYTE_VH.get()).isEqualTo((byte) (Initial.STATIC_BYTE & flag));

        Assertions.assertThat(INSTANCE_BYTE_VH.getAndBitwiseAnd(this, flag)).isEqualTo(Initial.INSTANCE_BYTE);
        Assertions.assertThat(INSTANCE_BYTE_VH.get(this)).isEqualTo((byte) (Initial.INSTANCE_BYTE & flag));
    }

    /**
     * 先返回當前的值再操作bit OR 後更新. 另外還有
     * {@link VarHandle#getAndBitwiseOrAcquire(Object...)},
     * {@link VarHandle#getAndBitwiseOrRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void getAndBitwiseOr() {
        byte flag = 0b1001;

        Assertions.assertThat(STATIC_BYTE_VH.getAndBitwiseOr(flag)).isEqualTo(Initial.STATIC_BYTE);
        Assertions.assertThat(STATIC_BYTE_VH.get()).isEqualTo((byte) (Initial.STATIC_BYTE | flag));

        Assertions.assertThat(INSTANCE_BYTE_VH.getAndBitwiseOr(this, flag)).isEqualTo(Initial.INSTANCE_BYTE);
        Assertions.assertThat(INSTANCE_BYTE_VH.get(this)).isEqualTo((byte) (Initial.INSTANCE_BYTE | flag));
    }

    /**
     * 先返回當前的值再操作bit XOR 後更新. 另外還有
     * {@link VarHandle#getAndBitwiseXorAcquire(Object...)},
     * {@link VarHandle#getAndBitwiseXorRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void getAndBitwiseXor() {
        byte flag = 0b1001;

        Assertions.assertThat(STATIC_BYTE_VH.getAndBitwiseXor(flag)).isEqualTo(Initial.STATIC_BYTE);
        Assertions.assertThat(STATIC_BYTE_VH.get()).isEqualTo((byte) (Initial.STATIC_BYTE ^ flag));

        Assertions.assertThat(INSTANCE_BYTE_VH.getAndBitwiseXor(this, flag)).isEqualTo(Initial.INSTANCE_BYTE);
        Assertions.assertThat(INSTANCE_BYTE_VH.get(this)).isEqualTo((byte) (Initial.INSTANCE_BYTE ^ flag));
    }

    /**
     * 更新file的值. 另外還有
     * {@link VarHandle#setVolatile(Object...)},
     * {@link VarHandle#setOpaque(Object...)},
     * {@link VarHandle#setRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void set() {
        STATIC_INT_VH.set(0);
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(0);

        STATIC_STRING_VH.set("0");
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo("0");

        INSTANCE_INT_VH.set(this, -0);
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(-0);

        INSTANCE_STRING_VH.set(this, "-0");
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo("-0");
    }

    /**
     * 比較相同則更新的原子操作. 另外還有 weak 系列,
     * {@link VarHandle#weakCompareAndSet(Object...)},
     * {@link VarHandle#weakCompareAndSetAcquire(Object...)},
     * {@link VarHandle#weakCompareAndSetRelease(Object...)},
     * {@link VarHandle#weakCompareAndSetPlain(Object...)} (可以對標 opaque 概念),
     * 是不保證成功的原子操作, 但是效能較好, 適合自旋鎖等高性能需求的場景.
     */
    @Test
    void compareAndSet() {
        int updateInt = 0;
        String updateString = "0";

        Assertions.assertThat(STATIC_INT_VH.compareAndSet(updateInt, Initial.STATIC_INT)).isFalse();
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_INT_VH.compareAndSet(Initial.STATIC_INT, updateInt)).isTrue();
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(updateInt);

        Assertions.assertThat(STATIC_STRING_VH.compareAndSet(updateString, Initial.STATIC_STRING)).isFalse();
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(STATIC_STRING_VH.compareAndSet(Initial.STATIC_STRING, updateString)).isTrue();
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo(updateString);

        Assertions.assertThat(INSTANCE_INT_VH.compareAndSet(this, updateInt, Initial.INSTANCE_INT)).isFalse();
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_INT_VH.compareAndSet(this, Initial.INSTANCE_INT, updateInt)).isTrue();
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(updateInt);

        Assertions.assertThat(INSTANCE_STRING_VH.compareAndSet(this, updateString, Initial.INSTANCE_STRING)).isFalse();
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo(Initial.INSTANCE_STRING);
        Assertions.assertThat(INSTANCE_STRING_VH.compareAndSet(this, Initial.INSTANCE_STRING, updateString)).isTrue();
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo(updateString);
    }

    /**
     * 比較相同則交換的原子操作, 也就是回傳舊值. 另外還有
     * {@link VarHandle#compareAndExchangeAcquire(Object...)},
     * {@link VarHandle#compareAndExchangeRelease(Object...)}
     * 同樣功能, 但對於內存訪問的保證不同.
     */
    @Test
    void compareAndExchange() {
        int changeInt = 0;
        String changeString = "0";

        Assertions.assertThat(STATIC_INT_VH.compareAndExchange(changeInt, Initial.STATIC_INT)).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_INT_VH.compareAndExchange(Initial.STATIC_INT, changeInt)).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(STATIC_INT_VH.get()).isEqualTo(changeInt);

        Assertions.assertThat(STATIC_STRING_VH.compareAndExchange(changeString, Initial.STATIC_STRING)).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(STATIC_STRING_VH.compareAndExchange(Initial.STATIC_STRING, changeString)).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(STATIC_STRING_VH.get()).isEqualTo(changeString);

        Assertions.assertThat(INSTANCE_INT_VH.compareAndExchange(this, changeInt, Initial.INSTANCE_INT)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_INT_VH.compareAndExchange(this, Initial.INSTANCE_INT, changeInt)).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(INSTANCE_INT_VH.get(this)).isEqualTo(changeInt);

        Assertions.assertThat(INSTANCE_STRING_VH.compareAndExchange(this, changeString, Initial.INSTANCE_STRING)).isEqualTo(Initial.INSTANCE_STRING);
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo(Initial.INSTANCE_STRING);
        Assertions.assertThat(INSTANCE_STRING_VH.compareAndExchange(this, Initial.INSTANCE_STRING, changeString)).isEqualTo(Initial.INSTANCE_STRING);
        Assertions.assertThat(INSTANCE_STRING_VH.get(this)).isEqualTo(changeString);
    }

    @Test
    void handle_array() {
        VarHandle array1VarHandle = MethodHandles.arrayElementVarHandle(int[].class);
        VarHandle array2VarHandle = MethodHandles.arrayElementVarHandle(int[][].class);

        array1VarHandle.set(ArrayQuiz.array1, 0, 56); // array[0] = 56
        Assertions.assertThat(array1VarHandle.get(ArrayQuiz.array1, 0)).isEqualTo(56);

        array2VarHandle.set(ArrayQuiz.array2, 1, ArrayQuiz.array1);
        Assertions.assertThat(ArrayQuiz.array2[1] == ArrayQuiz.array1).isTrue();
        Assertions.assertThat(ArrayQuiz.array2[1] == array2VarHandle.get(ArrayQuiz.array2, 1)).isTrue();
    }

    @Test
    void accessModeType() {
        Map<String, VarHandle> targets = Map.of(
                "static   String", STATIC_STRING_VH,
                "instance int   ", INSTANCE_INT_VH
        );

        VarHandle.AccessMode[] accessModes = {
                VarHandle.AccessMode.GET,
                VarHandle.AccessMode.SET
        };

        for (VarHandle.AccessMode accessMode : accessModes) {
            targets.forEach((target, varHandle) -> {
                // MethodType 是描述關於指定訪問模式的內容物件
                MethodType methodType = varHandle.accessModeType(accessMode);
                this.logger.info("{} methodType({}) = {}", target, accessMode, methodType); // (args) return
            });
        }
    }

    /**
     * XXX 具體意義還不清楚
     */
    @Test
    void coordinateTypes() {
        String newLine = System.lineSeparator();

        BiConsumer<String, VarHandle> print = (target, varHandle) -> {
            List<Class<?>> coordinateTypes = varHandle.coordinateTypes();
            this.logger.info("{} coordinateTypes: {}", target, coordinateTypes.stream()
                    .map(Class::toString)
                    .collect(Collectors.joining(",", "[" + newLine, newLine + "]")));
        };
        print.accept("int[]               ", MethodHandles.arrayElementVarHandle(int[].class));
        print.accept("int[][]             ", MethodHandles.arrayElementVarHandle(int[][].class));
        print.accept("static int          ", STATIC_INT_VH);
        print.accept("static String       ", STATIC_STRING_VH);
        print.accept("static byte         ", STATIC_BYTE_VH);
        print.accept("instance int        ", INSTANCE_INT_VH);
        print.accept("instance String     ", INSTANCE_STRING_VH);
        print.accept("instance byte       ", INSTANCE_BYTE_VH);
    }

    /**
     * 取得 VarHandle 的描述
     */
    @Test
    void describeConstable() {
        BiConsumer<String, VarHandle> print = (target, VarHandle) -> {
            Optional<VarHandle.VarHandleDesc> optional = VarHandle.describeConstable();
            optional.ifPresentOrElse(
                    varHandleDesc -> this.logger.info("{} describeConstable: {}", target, varHandleDesc),
                    () -> this.logger.info("{} describeConstable: null", target)
            );
        };

        print.accept("static int", STATIC_INT_VH);
        print.accept("String[]", MethodHandles.arrayElementVarHandle(String[].class));
    }

    @Test
    void other() {
//        STATIC_INT_VH.hasInvokeExactBehavior();
//        STATIC_INT_VH.isAccessModeSupported();
//        STATIC_INT_VH.toMethodHandle();
//        STATIC_INT_VH.varType();
//        STATIC_INT_VH.withInvokeBehavior();
//        STATIC_INT_VH.withInvokeExactBehavior();
    }

}
