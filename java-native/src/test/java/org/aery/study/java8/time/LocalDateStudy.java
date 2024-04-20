package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoChronology;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDateStudy {

    @Test
    void create() {
        System.out.println(LocalDate.MIN);
        System.out.println(LocalDate.MAX);
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.of(1, 1, 1));
        System.out.println(LocalDate.of(1, Month.JANUARY, 31));
        System.out.println(LocalDate.from(LocalDateTime.now()));
        System.out.println(LocalDate.ofEpochDay(1)); // offset from 1970-01-01T00:00:00Z
        System.out.println(LocalDate.ofYearDay(1, 150));
        System.out.println(LocalDate.ofYearDay(4, 150)); // because 4 is leap year, there has 2/29
        System.out.println(LocalDate.parse("2021-01-01"));
    }

    @Test
    void plus() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
//        System.out.println(localDate.plus(Duration.of(10, java.time.temporal.ChronoUnit.DAYS))); // unsupported Duration
        System.out.println(localDate.plus(Period.ofDays(3)));
        System.out.println(localDate.plus(3, ChronoUnit.DAYS));
        System.out.println(localDate.plusDays(3));
        System.out.println(localDate.plusWeeks(1));
        System.out.println(localDate.plusMonths(1));
        System.out.println(localDate.plusYears(1));
    }

    @Test
    void minus() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
//        System.out.println(localDate.minus(Duration.of(10, java.time.temporal.ChronoUnit.DAYS))); // unsupported Duration
        System.out.println(localDate.minus(Period.ofDays(3)));
        System.out.println(localDate.minus(3, ChronoUnit.DAYS));
        System.out.println(localDate.minusDays(3));
        System.out.println(localDate.minusWeeks(1));
        System.out.println(localDate.minusMonths(1));
        System.out.println(localDate.minusYears(1));
    }

    @Test
    void is_series() {
        LocalDate localDate1 = LocalDate.ofYearDay(2000, 256); // 閏年
        LocalDate localDate2 = localDate1.plus(1, ChronoUnit.YEARS);

        System.out.println(localDate1);
        System.out.println(localDate2);

        Assertions.assertThat(localDate1.isBefore(localDate2)).isTrue();
        Assertions.assertThat(localDate2.isBefore(localDate1)).isFalse();
        Assertions.assertThat(localDate1.isAfter(localDate2)).isFalse();
        Assertions.assertThat(localDate2.isAfter(localDate1)).isTrue();

        Assertions.assertThat(localDate1.isLeapYear()).isTrue();
        Assertions.assertThat(localDate2.isLeapYear()).isFalse();

        Assertions.assertThat(localDate2.isSupported(ChronoUnit.NANOS)).isFalse();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.MILLIS)).isFalse();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.SECONDS)).isFalse();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.MINUTES)).isFalse();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.HOURS)).isFalse();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.HALF_DAYS)).isFalse();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.DAYS)).isTrue();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.WEEKS)).isTrue();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.MONTHS)).isTrue();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.YEARS)).isTrue();
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.DECADES)).isTrue(); // 10 years
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.CENTURIES)).isTrue(); // 100 years
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.MILLENNIA)).isTrue(); // 1000 years
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.ERAS)).isTrue(); // 1,000,000,000 Years.
        Assertions.assertThat(localDate2.isSupported(ChronoUnit.FOREVER)).isFalse();

//        Assertions.assertThat(localDate2.isEqual(ChronoLocalDate)).isFalse(); // ???
    }

    @Test
    void compareTo() {
        LocalDate localDate1 = LocalDate.now();
        LocalDate localDate2 = localDate1.plus(1, ChronoUnit.DAYS);
        Assertions.assertThat(localDate1.compareTo(localDate1)).isEqualTo(0);
        Assertions.assertThat(localDate1.compareTo(localDate2)).isEqualTo(-1);
        Assertions.assertThat(localDate2.compareTo(localDate1)).isEqualTo(1);
    }

    @Test
    void adjustInto() {
        LocalDate currentDate = LocalDate.of(5566, 4, 1);
        LocalDate anotherDate = LocalDate.of(2024, 5, 6);
        Temporal adjustedDate = currentDate.adjustInto(anotherDate); // What is the use??
        System.out.println("Current Date  : " + currentDate);
        System.out.println("Another Date  : " + anotherDate);
        System.out.println("Adjusted Date : " + adjustedDate + " (" + adjustedDate.getClass() + ")");
    }

    @Test
    void format() {
        LocalDate localDate = LocalDate.now();

        List<String> unsupport = new ArrayList<>();
        DateTimeFormatterStudy.ALL_STATIC_FORMATTER.forEach((name, formatter) -> {
            String prefix = "format(" + name + ") : ";
            try {
                String formated = formatter.format(localDate);
                System.out.println(prefix + formated);
            } catch (UnsupportedTemporalTypeException e) {
                unsupport.add(prefix + e.getMessage());
            }
        });
        unsupport.forEach(System.err::println);
    }

    @Test
    void at_series() {
        LocalDate localDate = LocalDate.now();

        LocalDateTime localDateTime1 = localDate.atTime(LocalTime.now());
        LocalDateTime localDateTime2 = localDate.atTime(1, 30);
        LocalDateTime localDateTime3 = localDate.atTime(1, 30, 1);
        LocalDateTime localDateTime4 = localDate.atTime(1, 30, 1, 3);
        LocalDateTime startOfDay1 = localDate.atStartOfDay();
        ZonedDateTime startOfDay2 = localDate.atStartOfDay(ZoneId.systemDefault());

        System.out.println(localDateTime1);
        System.out.println(localDateTime2);
        System.out.println(localDateTime3);
        System.out.println(localDateTime4);
        System.out.println(startOfDay1);
        System.out.println(startOfDay2);
    }

    @Test
    void get_series() {
        LocalDate localDate = LocalDate.of(2021, 4, 25);

        Assertions.assertThat(localDate.get(ChronoField.DAY_OF_MONTH)).isEqualTo(25);
        Assertions.assertThat(localDate.getLong(ChronoField.DAY_OF_YEAR)).isEqualTo(115L);

        Assertions.assertThat(localDate.getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
        Assertions.assertThat(localDate.getDayOfMonth()).isEqualTo(25);
        Assertions.assertThat(localDate.getDayOfYear()).isEqualTo(115L);

        Assertions.assertThat(localDate.getMonth()).isEqualTo(Month.APRIL);
        Assertions.assertThat(localDate.getMonthValue()).isEqualTo(4);
        Assertions.assertThat(localDate.getYear()).isEqualTo(2021);

        IsoChronology chronology = localDate.getChronology();
        Era era = localDate.getEra();
        System.out.println(chronology);
        System.out.println(era);
    }

    @Test
    void length_series() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.lengthOfMonth()); // 這個月的天數(1月31, 2月28, ...)
        System.out.println(localDate.lengthOfYear()); // 這年的天數(365, 閏年366)
    }

    @Test
    void range() {
        LocalDate localDate = LocalDate.now();
        for (ChronoField field : ChronoField.values()) {
            try {
                ValueRange valueRange = localDate.range(field);
                System.out.println(field + " : " + valueRange);
            } catch (UnsupportedTemporalTypeException e) {
                System.err.println(field + " : Unsupport");
            }
        }
    }

    @Test
    void with() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate + "                                    " + localDate);
        System.out.println(localDate + " with(DayOfWeek.SUNDAY)           = " + localDate.with(DayOfWeek.SUNDAY));
        System.out.println(localDate + " with(ChronoField.DAY_OF_WEEK, 5) = " + localDate.with(ChronoField.DAY_OF_WEEK, 5));
        System.out.println(localDate + " withMonth(1)                     = " + localDate.withMonth(1));
        System.out.println(localDate + " withDayOfMonth(5)                = " + localDate.withDayOfMonth(5));
        System.out.println(localDate + " withDayOfYear(12)                = " + localDate.withDayOfYear(12));
        System.out.println(localDate + " withYear(12)                     = " + localDate.withYear(12));
    }

    @Test
    void until() {
        LocalDate localDate1 = LocalDate.now();
        LocalDate localDate2 = localDate1.plus(1, ChronoUnit.DAYS);
        System.out.println(localDate1.until(localDate2));
        System.out.println(localDate1.until(localDate2, ChronoUnit.DAYS));
    }

    @Test
    void query() {
        LocalDate now = LocalDate.now();
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
        LocalDate now = LocalDate.now();
        System.out.println(now);
        System.out.println(now.toEpochDay());
    }

}
