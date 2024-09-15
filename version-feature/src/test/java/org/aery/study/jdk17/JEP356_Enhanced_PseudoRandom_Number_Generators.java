package org.aery.study.jdk17;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class JEP356_Enhanced_PseudoRandom_Number_Generators {

    public static void main(String[] args) {
        RandomGenerator generator1 = RandomGeneratorFactory.getDefault().create();
        RandomGenerator generator2 = RandomGeneratorFactory.of("L32X64MixRandom").create();

        System.out.println(generator1);
        System.out.println(generator2);
    }

    @Test
    void RandomGenerator_nextInt() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        System.out.println(generator.nextInt());
        System.out.println(generator.nextInt(3)); // [0, 3)
        System.out.println(generator.nextInt(3, 5)); // [3, 5)
    }

    @Test
    void RandomGenerator_nextLong() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        System.out.println(generator.nextLong());
        System.out.println(generator.nextLong(3)); // [0, 3)
        System.out.println(generator.nextLong(3, 5)); // [3, 5)
    }

    @Test
    void RandomGenerator_nextDouble() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        System.out.println(generator.nextDouble()); // [0, 1)
        System.out.println(generator.nextDouble(3.5)); // [0, 3.5)
        System.out.println(generator.nextDouble(3.5, 5)); // [3.5, 5)
    }

    @Test
    void RandomGenerator_nextFloat() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        System.out.println(generator.nextFloat()); // [0, 1)
        System.out.println(generator.nextFloat(3.5f)); // [0, 3.5)
        System.out.println(generator.nextFloat(3.5f, 5)); // [3.5, 5)
    }

    @Test
    void RandomGenerator_nextBoolean() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        int trueCount = 0;
        int falseCount = 0;
        for (int i = 0; i < 100_000; i++) {
            if (generator.nextBoolean()) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        System.out.println("true : " + trueCount);
        System.out.println("false : " + falseCount);
    }

    @Test
    void RandomGenerator_nextExponential() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        Map<Integer, Integer> stageCounts = new TreeMap<>();

        for (int i = 0; i < 100_000; i++) {
            double random = generator.nextExponential(); // 0 ~ ∞ 的指數分布, 較小的數值會出現得更頻繁, 較大的數值則較為稀少
            int stage = (int) (random * 100);
            stageCounts.put(stage, stageCounts.getOrDefault(stage, 0) + 1);
        }

        stageCounts.forEach((stage, count) -> System.out.printf("%4d : %d%n", stage, count));
    }

    @Test
    void RandomGenerator_nextGaussian() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        Map<Integer, Integer> stageCounts = new TreeMap<>();

        for (int i = 0; i < 100_000; i++) {
            double random = generator.nextGaussian(); // -∞ ~ ∞ 的高斯分布, 越接近0出現的機率越高, 越遠離0出現的機率越低
            int stage = (int) (random * 100);
            stageCounts.put(stage, stageCounts.getOrDefault(stage, 0) + 1);
        }

        stageCounts.forEach((stage, count) -> System.out.printf("%4d : %d%n", stage, count));
    }

    @Test
    void RandomGenerator_nextBytes() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        byte[] randomBytes = new byte[10];
        generator.nextBytes(randomBytes); // 常用於需要隨機生成數據的情境，例如密碼學應用或隨機填充數據
        System.out.print("Random bytes:");
        for (byte b : randomBytes) {
            System.out.print(b + " ");
        }
    }

    @Test
    void RandomGenerator_ints() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        generator.ints().limit(5).forEach(System.out::println);  // 生成 5 個 Integer.MIN_VALUE 到 Integer.MAX_VALUE 之間的隨機數
        System.out.println();

        generator.ints(5).forEach(System.out::println);  // 生成 5 個 Integer.MIN_VALUE 到 Integer.MAX_VALUE 之間的隨機數
        System.out.println();

        generator.ints(5, 10, 20).forEach(System.out::println); // 生成 5 個 [10, 20) 之間的隨機數
        System.out.println();

        generator.ints(10, 20)  // 生成無限個在 [10, 20) 之間的隨機數
                .limit(5) // 沒有這個就真的是無限生成
                .forEach(System.out::println);
    }

    @Test
    void RandomGenerator_longs() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        generator.longs().limit(5).forEach(System.out::println);  // 生成 5 個 Integer.MIN_VALUE 到 Integer.MAX_VALUE 之間的隨機數
        System.out.println();

        generator.longs(5).forEach(System.out::println);  // 生成 5 個 Integer.MIN_VALUE 到 Integer.MAX_VALUE 之間的隨機數
        System.out.println();

        generator.longs(5, 10, 20).forEach(System.out::println); // 生成 5 個 [10, 20) 之間的隨機數
        System.out.println();

        generator.longs(10, 20)  // 生成無限個在 [10, 20) 之間的隨機數
                .limit(5) // 沒有這個就真的是無限生成
                .forEach(System.out::println);
    }

    @Test
    void RandomGenerator_doubles() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        generator.doubles().limit(5).forEach(System.out::println);  // 生成 5 個 0.0 到 1.0 之間的隨機數
        System.out.println();

        generator.doubles(5).forEach(System.out::println);  // 生成 5 個 0.0 到 1.0 之間的隨機數
        System.out.println();

        generator.doubles(5, 10.0, 20.0).forEach(System.out::println); // 生成 5 個 [10.0, 20.0) 之間的隨機數
        System.out.println();

        generator.doubles(10.0, 20.0)  // 生成無限個在 10.0 到 20.0 之間的隨機數
                .limit(5) // 沒有這個就真的是無限生成
                .forEach(System.out::println);
    }

    @Test
    void RandomGenerator_isDeprecated() {
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create();
        System.out.println(generator.isDeprecated()); // 是否為已棄用的演算法
    }

}
