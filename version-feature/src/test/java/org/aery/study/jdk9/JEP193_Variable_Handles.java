package org.aery.study.jdk9;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class JEP193_Variable_Handles {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static class Initial {
        private static int STATIC_INT = 5566;
        private static String STATIC_STRING = "kerker";
        private static int INSTANCE_INT = 9527;
        private static String INSTANCE_STRING = "haha";
    }

    private static final VarHandle STATIC_INT_VH;

    private static final VarHandle STATIC_STRING_VH;

    private static int STATIC_INT;

    private static String STATIC_STRING;

    static {
        try {
            Class<?> clazz = JEP193_Variable_Handles.class;
            STATIC_INT_VH = MethodHandles.lookup().findStaticVarHandle(clazz, "STATIC_INT", int.class);
            STATIC_STRING_VH = MethodHandles.lookup().findStaticVarHandle(clazz, "STATIC_STRING", String.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final VarHandle instanceIntVH;

    private final VarHandle instanceStringVH;

    private int instanceInt;

    private String instanceString;

    public JEP193_Variable_Handles() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = JEP193_Variable_Handles.class;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        this.instanceIntVH = lookup.findVarHandle(clazz, "instanceInt", int.class);
        this.instanceStringVH = lookup.findVarHandle(clazz, "instanceString", String.class);
    }

    @BeforeEach
    void setUp() {
        STATIC_INT = Initial.STATIC_INT;
        STATIC_STRING = Initial.STATIC_STRING;
        this.instanceInt = Initial.INSTANCE_INT;
        this.instanceString = Initial.INSTANCE_STRING;
    }

    /**
     * {@link VarHandle#getAcquire(Object...)}不保證內存訪問.
     */
    @Test
    void get() {
        int staticInt = (int) STATIC_INT_VH.get();
        String staticString = (String) STATIC_STRING_VH.get();
        int instanceInt = (int) this.instanceIntVH.get(this);
        String instanceString = (String) this.instanceStringVH.get(this);

        Assertions.assertThat(staticInt).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(staticString).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(instanceInt).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(instanceString).isEqualTo(Initial.INSTANCE_STRING);
    }

    /**
     * {@link VarHandle#getAcquire(Object...)}保證內存訪問,
     * 也就是所有後續操作都不會被指令優化重排序到當前線程的getAcquire()之前,
     * 可以確保這些操作的可見性跟順序性.
     */
    @Test
    void getAcquire() {
        int staticInt = (int) STATIC_INT_VH.getAcquire();
        String staticString = (String) STATIC_STRING_VH.getAcquire();
        int instanceInt = (int) this.instanceIntVH.getAcquire(this);
        String instanceString = (String) this.instanceStringVH.getAcquire(this);

        // 這裡的操作不會因為指令優化而被重排到getAcquire()之前

        Assertions.assertThat(staticInt).isEqualTo(Initial.STATIC_INT);
        Assertions.assertThat(staticString).isEqualTo(Initial.STATIC_STRING);
        Assertions.assertThat(instanceInt).isEqualTo(Initial.INSTANCE_INT);
        Assertions.assertThat(instanceString).isEqualTo(Initial.INSTANCE_STRING);
    }

}
