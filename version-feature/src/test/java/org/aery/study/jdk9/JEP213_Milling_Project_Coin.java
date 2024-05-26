package org.aery.study.jdk9;

import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;

public class JEP213_Milling_Project_Coin {

//    static int _ = 9527; // can't use _ as variable name from java9

    interface MyInterface<Type> {

        private static void newFeature1() {
        }

        private void newFeature2() {
        }

    }

    class Quiz<Type> implements MyInterface<Type>, Closeable {

//        @Override // can't override private method
//        private void newFeature() {
//        }

        @Override
        public void close() throws IOException {
            MyInterface.newFeature1(); // can invoke private static method
            MyInterface.super.newFeature2(); // can invoke private method
        }

        @SafeVarargs // can be used in private method
        private void safeVarargs(Type... types) {
            MyInterface.newFeature1(); // can invoke private static method
            MyInterface.super.newFeature2(); // can invoke private method
        }
    }

    @Test
    void try_with_resources() {
        // from java7
        try (Quiz quiz = new Quiz()) { // Closeable object must be declared in try-with-resources scope
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // from java9
        Quiz quiz = new Quiz();
        try (quiz) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void anonymousClass_with_Generics() {
        // before java9
        MyInterface<String> myInterface1 = new MyInterface<String>() {
        };

        // from java9
        MyInterface<String> myInterface2 = new MyInterface<>() {
        };
    }

}
