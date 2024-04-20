package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.Chronology;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.List;

public class LocalTimeStudy {

    @Test
    public void create() {
        System.out.println(LocalTime.MIN);
        System.out.println(LocalTime.MAX);
        System.out.println(LocalTime.now());
        System.out.println(LocalTime.of(1, 1));
        System.out.println(LocalTime.of(1, 1, 31));
        System.out.println(LocalTime.of(1, 1, 31, 22));
        System.out.println(LocalTime.ofNanoOfDay(9527));
        System.out.println(LocalTime.ofSecondOfDay(9527));
        System.out.println(LocalTime.from(LocalDateTime.now()));
        System.out.println(LocalTime.parse("03:02:01"));
    }

    @Test
    public void plus() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        System.out.println(localTime.plus(Duration.of(10, ChronoUnit.MILLIS)));
//        System.out.println(localTime.plus(Period.ofDays(3))); // unsupported Period
        System.out.println(localTime.plus(3, ChronoUnit.MILLIS));
        System.out.println(localTime.plusNanos(10));
        System.out.println(localTime.plusSeconds(10));
        System.out.println(localTime.plusMinutes(10));
        System.out.println(localTime.plusHours(10));
    }

    @Test
    public void minus() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        System.out.println(localTime.minus(Duration.of(10, ChronoUnit.MILLIS)));
//        System.out.println(localTime.minus(Period.ofDays(3))); // unsupported Period
        System.out.println(localTime.minus(3, ChronoUnit.MILLIS));
        System.out.println(localTime.minusNanos(10));
        System.out.println(localTime.minusSeconds(10));
        System.out.println(localTime.minusMinutes(10));
        System.out.println(localTime.minusHours(10));
    }

    @Test
    public void is_series() {
        LocalTime localTime1 = LocalTime.now();
        LocalTime localTime2 = localTime1.plus(1, ChronoUnit.HOURS);

        System.out.println(localTime1);
        System.out.println(localTime2);

        Assertions.assertThat(localTime1.isBefore(localTime2)).isTrue();
        Assertions.assertThat(localTime2.isBefore(localTime1)).isFalse();
        Assertions.assertThat(localTime1.isAfter(localTime2)).isFalse();
        Assertions.assertThat(localTime2.isAfter(localTime1)).isTrue();

        Assertions.assertThat(localTime2.isSupported(ChronoUnit.NANOS)).isTrue();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.MILLIS)).isTrue();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.SECONDS)).isTrue();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.MINUTES)).isTrue();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.HOURS)).isTrue();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.HALF_DAYS)).isTrue();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.DAYS)).isFalse();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.WEEKS)).isFalse();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.MONTHS)).isFalse();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.YEARS)).isFalse();
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.DECADES)).isFalse(); // 10 years
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.CENTURIES)).isFalse(); // 100 years
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.MILLENNIA)).isFalse(); // 1000 years
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.ERAS)).isFalse(); // 1,000,000,000 Years.
        Assertions.assertThat(localTime2.isSupported(ChronoUnit.FOREVER)).isFalse();
    }

    @Test
    public void compareTo() {
        LocalTime localTime1 = LocalTime.now();
        LocalTime localTime2 = localTime1.plus(1, ChronoUnit.MINUTES);
        Assertions.assertThat(localTime1.compareTo(localTime1)).isEqualTo(0);
        Assertions.assertThat(localTime1.compareTo(localTime2)).isEqualTo(-1);
        Assertions.assertThat(localTime2.compareTo(localTime1)).isEqualTo(1);
    }

    @Test
    public void adjustInto() {
        LocalTime currentTime = LocalTime.of(12, 4, 1);
        LocalTime anotherTime = LocalTime.of(2, 5, 6);
        Temporal adjustedDate = currentTime.adjustInto(anotherTime); // What is the use??
        System.out.println("Current Time  : " + currentTime);
        System.out.println("Another Time  : " + anotherTime);
        System.out.println("Adjusted Time : " + adjustedDate + " (" + adjustedDate.getClass() + ")");
    }

    @Test
    public void format() {
        LocalTime localTime = LocalTime.now();

        List<String> unsupport = new ArrayList<>();
        DateTimeFormatterStudy.ALL_STATIC_FORMATTER.forEach((name, formatter) -> {
            String prefix = "format(" + name + ") : ";
            try {
                String formated = formatter.format(localTime);
                System.out.println(prefix + formated);
            } catch (UnsupportedTemporalTypeException e) {
                unsupport.add(prefix + e.getMessage());
            }
        });
        unsupport.forEach(System.err::println);
    }

    @Test
    public void at_series() {
        LocalTime localTime = LocalTime.now();

        LocalDateTime localDateTime = localTime.atDate(LocalDate.now());
        OffsetTime offsetTime = localTime.atOffset(ZoneOffset.UTC);

        System.out.println(localDateTime);
        System.out.println(offsetTime);
    }

    @Test
    public void get_series() {
        LocalTime localTime = LocalTime.of(1, 4, 25);

        Assertions.assertThat(localTime.get(ChronoField.SECOND_OF_DAY)).isEqualTo(3865);
        Assertions.assertThat(localTime.getLong(ChronoField.SECOND_OF_MINUTE)).isEqualTo(25);
        Assertions.assertThat(localTime.getNano()).isEqualTo(0);
        Assertions.assertThat(localTime.getSecond()).isEqualTo(25);
        Assertions.assertThat(localTime.getMinute()).isEqualTo(4);
        Assertions.assertThat(localTime.getHour()).isEqualTo(1);
    }

    @Test
    public void range() {
        LocalTime localTime = LocalTime.now();
        for (ChronoField field : ChronoField.values()) {
            try {
                ValueRange valueRange = localTime.range(field);
                System.out.println(field + " : " + valueRange);
            } catch (UnsupportedTemporalTypeException e) {
                System.err.println(field + " : Unsupport");
            }
        }
    }

    @Test
    public void with() {
        LocalTime localTime = LocalTime.of(1, 4, 25, 9527);
        System.out.println(localTime + "                                         " + localTime);
        System.out.println(localTime + " with(LocalDateTime.now())             = " + localTime.with(LocalTime.now()));
        System.out.println(localTime + " with(ChronoField.SECOND_OF_MINUTE, 5) = " + localTime.with(ChronoField.SECOND_OF_MINUTE, 5));
        System.out.println(localTime + " withNano(5566)                        = " + localTime.withNano(5566));
        System.out.println(localTime + " withSecond(43)                        = " + localTime.withSecond(43));
        System.out.println(localTime + " withMinute(33)                        = " + localTime.withMinute(33));
        System.out.println(localTime + " withHour(9)                           = " + localTime.withHour(9));
    }

    @Test
    public void until() {
        LocalTime localTime1 = LocalTime.now();
        LocalTime localTime2 = localTime1.plus(77, ChronoUnit.MINUTES);
        Assertions.assertThat(localTime1.until(localTime2, ChronoUnit.MINUTES)).isEqualTo(77);
    }

    @Test
    public void query() {
        LocalTime now = LocalTime.now();
        TemporalUnit temporalUnit = now.query(TemporalQueries.precision());
        LocalDate localDate = now.query(TemporalQueries.localDate());
        LocalTime localTime = now.query(TemporalQueries.localTime());
        Chronology chronology = now.query(TemporalQueries.chronology());
        ZoneOffset zoneOffset = now.query(TemporalQueries.offset());
        ZoneId zoneId0 = now.query(TemporalQueries.zone());
        ZoneId zoneId1 = now.query(TemporalQueries.zoneId());

        System.out.println("temporalUnit = " + temporalUnit);
        System.out.println("localDate    = " + localDate);
        System.out.println("localTime    = " + localTime);
        System.out.println("chronology   = " + chronology);
        System.out.println("zoneOffset   = " + zoneOffset);
        System.out.println("zoneId0      = " + zoneId0);
        System.out.println("zoneId1      = " + zoneId1);
    }

    @Test
    public void to_series() {
        LocalTime now = LocalTime.now();
        System.out.println(now);
        System.out.println(now.toNanoOfDay());
        System.out.println(now.toSecondOfDay());
    }

    @Test
    public void truncatedTo() {
        LocalTime now = LocalTime.now();
        System.out.println(now);
        for (ChronoUnit unit : ChronoUnit.values()) {
            String prefix = "truncatedTo(" + unit + ") = ";
            try {
                LocalTime localTime = now.truncatedTo(unit);
                System.out.println(prefix + localTime);
            } catch (UnsupportedTemporalTypeException e) {
                System.err.println(prefix + "Unsupport");
            }
        }
    }

}
