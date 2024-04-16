package org.aery.study.vf._9;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class ProcessHandleTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessHandleTest.class);

    public static void print(String term, ProcessHandle processHandle) {
        String prefix = "[" + term + "] ";
        Consumer<String> printer = msg -> LOGGER.info("{}{}", prefix, msg);

        BiConsumer<String, Optional<?>> infoPrinter = (method, result) -> printer.accept("ProcessHandle$Info." + method + " = " + result);
        ProcessHandle.Info info = processHandle.info();
        infoPrinter.accept("command()", info.command());
        infoPrinter.accept("commandLine()", info.commandLine());
        infoPrinter.accept("arguments()", info.arguments());
        infoPrinter.accept("user()", info.user());
        infoPrinter.accept("startInstant()", info.startInstant());
        infoPrinter.accept("totalCpuDuration()", info.totalCpuDuration());

        BiConsumer<String, Object> handlerPrinter = (method, result) -> printer.accept("ProcessHandle." + method + " = " + result);
        List<ProcessHandle> childrens = processHandle.children().toList();

        handlerPrinter.accept("pid()", processHandle.pid());
        handlerPrinter.accept("isAlive()", processHandle.isAlive());
        handlerPrinter.accept("parent()", processHandle.parent());
        handlerPrinter.accept("children()", childrens);
        childrens.forEach(children -> handlerPrinter.accept("children()#" + children, children.info()));
    }

    @Test
    void allProcesses() {
        ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .filter(info -> info.command().isPresent())
                .forEachOrdered(System.out::println);
    }

    @Test
    void current() {
        ProcessHandle processHandle = ProcessHandle.current();
        print("current", processHandle);
    }

}
