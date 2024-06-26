package org.aery.study.jdk9;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

class JEP102_ProcessBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void test() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-version");
        Process subProcess = processBuilder.start();
        ProcessHandle subProcessHandle = subProcess.toHandle();

        Optional<ProcessHandle> parentBeforeSubProcessDead = subProcessHandle.parent();
        Assertions.assertThat(parentBeforeSubProcessDead).isNotEmpty();
        Assertions.assertThat(subProcess.isAlive()).isTrue();
        Assertions.assertThatThrownBy(subProcess::exitValue)
                .isInstanceOf(IllegalThreadStateException.class)
                .hasMessage("process has not exited");

        subProcess.onExit().thenAccept(process -> { // async
            this.logger.info("sub process exit");
            this.logger.info("sub process exitValue = " + process.exitValue());
        });

        JEP102_ProcessHandle.print("sub1", subProcessHandle);

        this.logger.info("before waiting...");
        Thread.sleep(100); // wait for subProcess to finish

        Optional<ProcessHandle> parentAfterSubProcessDead = subProcessHandle.parent();
        Assertions.assertThat(parentAfterSubProcessDead).isEmpty();
        Assertions.assertThat(subProcess.isAlive()).isFalse();
        Assertions.assertThat(subProcess.exitValue()).isEqualTo(0);

        Assertions.assertThat(parentBeforeSubProcessDead.get().compareTo(ProcessHandle.current())).isZero();

        subProcess.getInputStream().transferTo(System.out);
        subProcess.getErrorStream().transferTo(System.out);

        JEP102_ProcessHandle.print("this", parentBeforeSubProcessDead.get());
        JEP102_ProcessHandle.print("sub2", subProcessHandle);
    }


}
