package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class InstantStudy {

    @Test
    void creat() {
        long testEpochSecond = 1609459200L; // 2021-01-01T00:00:00Z
        Instant instant1 = Instant.now(); // use timestamp
        Instant instant2 = Instant.parse("2021-01-01T00:00:00.001Z");
        Instant instant3 = Instant.ofEpochSecond(testEpochSecond);
        Instant instant4 = Instant.ofEpochMilli(testEpochSecond * 1000L); // 2021-01-01T00:00:00Z
        Instant instant5 = Instant.ofEpochSecond(testEpochSecond, (long) Math.pow(10, 6));
        Instant instant6 = Instant.ofEpochSecond(testEpochSecond, 1);

        System.out.println(instant1);
        System.out.println(instant2);
        System.out.println(instant3);
        System.out.println(instant4);
        System.out.println(instant5);
        System.out.println(instant6);
        System.out.println(Instant.EPOCH);
        System.out.println(Instant.MIN);
        System.out.println(Instant.MAX);
    }

    @Test
    void plus() {
        Instant offset = Instant.EPOCH;
        System.out.println(offset.plus(Duration.of(10, ChronoUnit.MINUTES)));
        System.out.println(offset.plus(10, ChronoUnit.DAYS));
        System.out.println(offset.plus(-10, ChronoUnit.DAYS));
        System.out.println(offset.plusSeconds(10));
        System.out.println(offset.plusMillis(10));
        System.out.println(offset.plusNanos(10));
    }

    @Test
    void minus() {
        Instant offset = Instant.EPOCH;
        System.out.println(offset.minus(Duration.of(10, ChronoUnit.MINUTES)));
        System.out.println(offset.minus(10, ChronoUnit.DAYS));
        System.out.println(offset.minus(-10, ChronoUnit.DAYS));
        System.out.println(offset.minusSeconds(10));
        System.out.println(offset.minusMillis(10));
        System.out.println(offset.minusNanos(10));
    }

    @Test
    void isAfter() {
        Instant instant_00 = Instant.EPOCH;
        Instant instant_10 = instant_00.plus(10, ChronoUnit.SECONDS);
        Assertions.assertThat(instant_00.isAfter(instant_10)).isFalse();
    }

    @Test
    void isBefore() {
        Instant instant_00 = Instant.EPOCH;
        Instant instant_10 = instant_00.plus(10, ChronoUnit.SECONDS);
        Assertions.assertThat(instant_00.isBefore(instant_10)).isTrue();
    }

    @Test
    void compareTo() {
        Instant instant_00 = Instant.EPOCH;
        Instant instant_10 = instant_00.plus(10, ChronoUnit.SECONDS);
        Assertions.assertThat(instant_00.compareTo(instant_10)).isEqualTo(-1);
        Assertions.assertThat(instant_00.compareTo(Instant.EPOCH)).isEqualTo(0);
        Assertions.assertThat(instant_10.compareTo(instant_00)).isEqualTo(1);
    }

}
