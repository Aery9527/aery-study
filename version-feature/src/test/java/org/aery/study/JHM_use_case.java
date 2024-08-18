package org.aery.study;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * JMH (Java Microbenchmark Harness) 是一個用來進行 Java 程式效能測試的工具,
 * 此 sample code 用來 "String相加" 與 "StringBuilder操作" 的效能比較
 * (需加入 JMH 的 lib)
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1) // 預熱的目的是讓 JVM 對被測試的code進行足夠多的優化, 因此在預熱後，被測試的code應該得到了充分的 JIT 編譯和優化
@Measurement(iterations = 3, time = 5) // 進行測試的次數, 也就是最終統計結果
@Threads(4)
@Fork(1) // process
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JHM_use_case {

    @Param(value = {"10", "50", "100"})
    private int length;

    @Benchmark
    public void testStringAdd(Blackhole blackhole) {
        String a = "";
        for (int i = 0; i < length; i++) {
            a += i;
        }
        blackhole.consume(a);
    }

    @Benchmark
    public void testStringBuilderAdd(Blackhole blackhole) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(i);
        }
        blackhole.consume(sb.toString());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JHM_use_case.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

}
