package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDateTimeStudy {

    @Test
    void create() {
        System.out.println(LocalDateTime.MIN);
        System.out.println(LocalDateTime.MAX);
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        System.out.println(LocalDateTime.of(1, Month.JANUARY, 1, 1, 1));
        System.out.println(LocalDateTime.of(1, Month.JANUARY, 1, 1, 1, 1));
        System.out.println(LocalDateTime.of(1, Month.JANUARY, 1, 1, 1, 1, 1));
        System.out.println(LocalDateTime.of(1, 1, 1, 1, 1));
        System.out.println(LocalDateTime.of(1, 1, 1, 1, 1, 1));
        System.out.println(LocalDateTime.of(1, 1, 1, 1, 1, 1, 1));
        System.out.println(LocalDateTime.of(1, 1, 1, 1, 1, 1, 1));
        System.out.println(LocalDateTime.ofEpochSecond(1, 1, ZoneOffset.UTC));
        System.out.println(LocalDateTime.ofEpochSecond(1, 1, ZoneOffset.MIN)); // -18hr
        System.out.println(LocalDateTime.ofEpochSecond(1, 1, ZoneOffset.MAX)); // +18hr
        System.out.println(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        System.out.println(LocalDateTime.from(LocalDateTime.now()));
        System.out.println(LocalDateTime.parse("2021-01-01T01:01:01"));
    }

    @Test
    void plus() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(localDateTime.plus(Duration.of(10, ChronoUnit.DAYS)));
        System.out.println(localDateTime.plus(Period.ofDays(3)));
        System.out.println(localDateTime.plus(3, ChronoUnit.DAYS));
        System.out.println(localDateTime.plusNanos(1));
        System.out.println(localDateTime.plusSeconds(1));
        System.out.println(localDateTime.plusMinutes(1));
        System.out.println(localDateTime.plusHours(1));
        System.out.println(localDateTime.plusDays(1));
        System.out.println(localDateTime.plusWeeks(1));
        System.out.println(localDateTime.plusMonths(1));
        System.out.println(localDateTime.plusYears(1));
    }

    @Test
    void minus() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(localDateTime.minus(Duration.of(10, ChronoUnit.DAYS)));
        System.out.println(localDateTime.minus(Period.ofDays(3)));
        System.out.println(localDateTime.minus(3, ChronoUnit.DAYS));
        System.out.println(localDateTime.minusNanos(1));
        System.out.println(localDateTime.minusSeconds(1));
        System.out.println(localDateTime.minusMinutes(1));
        System.out.println(localDateTime.minusHours(1));
        System.out.println(localDateTime.minusDays(1));
        System.out.println(localDateTime.minusWeeks(1));
        System.out.println(localDateTime.minusMonths(1));
        System.out.println(localDateTime.minusYears(1));
    }

    @Test
    void is_series() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2000, 2, 29, 1, 1); // 閏年
        LocalDateTime localDateTime2 = localDateTime1.plus(1, ChronoUnit.YEARS);

        System.out.println(localDateTime1);
        System.out.println(localDateTime2);

        Assertions.assertThat(localDateTime1.isBefore(localDateTime2)).isTrue();
        Assertions.assertThat(localDateTime2.isBefore(localDateTime1)).isFalse();
        Assertions.assertThat(localDateTime1.isAfter(localDateTime2)).isFalse();
        Assertions.assertThat(localDateTime2.isAfter(localDateTime1)).isTrue();

        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.NANOS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.MILLIS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.SECONDS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.MINUTES)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.HOURS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.HALF_DAYS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.DAYS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.WEEKS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.MONTHS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.YEARS)).isTrue();
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.DECADES)).isTrue(); // 10 years
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.CENTURIES)).isTrue(); // 100 years
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.MILLENNIA)).isTrue(); // 1000 years
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.ERAS)).isTrue(); // 1,000,000,000 Years.
        Assertions.assertThat(localDateTime2.isSupported(ChronoUnit.FOREVER)).isFalse();
    }

    @Test
    void compareTo() {
        LocalDateTime localDate1 = LocalDateTime.now();
        LocalDateTime localDate2 = localDate1.plus(1, ChronoUnit.DAYS);
        Assertions.assertThat(localDate1.compareTo(localDate1)).isEqualTo(0);
        Assertions.assertThat(localDate1.compareTo(localDate2)).isEqualTo(-1);
        Assertions.assertThat(localDate2.compareTo(localDate1)).isEqualTo(1);
    }

    @Test
    void adjustInto() {
        LocalDateTime currentDateTime = LocalDateTime.of(5566, 4, 1, 1, 1, 1, 1);
        LocalDateTime anotherDateTime = LocalDateTime.of(2024, 5, 6, 2, 2, 2, 2);
        Temporal adjustedDateTime0 = currentDateTime.adjustInto(anotherDateTime); // What is the use??
//        Temporal adjustedDateTime1 = currentDateTime.adjustInto(LocalDate.now()); // unsupported
//        Temporal adjustedDateTime2 = currentDateTime.adjustInto(LocalTime.now()); // unsupported
        System.out.println("Current DateTime   : " + currentDateTime);
        System.out.println("Another DateTime   : " + anotherDateTime);
        System.out.println("Adjusted DateTime0 : " + adjustedDateTime0 + " (" + adjustedDateTime0.getClass() + ")");
//        System.out.println("Adjusted DateTime1 : " + adjustedDateTime1 + " (" + adjustedDateTime1.getClass() + ")");
//        System.out.println("Adjusted DateTime2 : " + adjustedDateTime2 + " (" + adjustedDateTime2.getClass() + ")");
    }

    @Test
    void format() {
        LocalDateTime localDateTime = LocalDateTime.now();

        List<String> unsupport = new ArrayList<>();
        DateTimeFormatterStudy.ALL_STATIC_FORMATTER.forEach((name, formatter) -> {
            String prefix = "format(" + name + ") : ";
            try {
                String formated = formatter.format(localDateTime);
                System.out.println(prefix + formated);
            } catch (UnsupportedTemporalTypeException e) {
                unsupport.add(prefix + e.getMessage());
            }
        });
        unsupport.forEach(System.err::println);
    }

    @Test
    void at_series() {
        LocalDateTime localDateTime = LocalDateTime.now();

        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);

        System.out.println(zonedDateTime);
        System.out.println(offsetDateTime);
    }

    @Test
    void get_series() {
        LocalDateTime localDateTime = LocalDateTime.of(1, 4, 25, 3, 4, 5);

        Assertions.assertThat(localDateTime.get(ChronoField.SECOND_OF_DAY)).isEqualTo(11045);
        Assertions.assertThat(localDateTime.getLong(ChronoField.SECOND_OF_MINUTE)).isEqualTo(5);
        Assertions.assertThat(localDateTime.getNano()).isEqualTo(0);
        Assertions.assertThat(localDateTime.getSecond()).isEqualTo(5);
        Assertions.assertThat(localDateTime.getMinute()).isEqualTo(4);
        Assertions.assertThat(localDateTime.getHour()).isEqualTo(3);
        Assertions.assertThat(localDateTime.getDayOfWeek()).isEqualTo(DayOfWeek.WEDNESDAY);
        Assertions.assertThat(localDateTime.getDayOfMonth()).isEqualTo(25);
        Assertions.assertThat(localDateTime.getDayOfYear()).isEqualTo(115);
        Assertions.assertThat(localDateTime.getMonth()).isEqualTo(Month.APRIL);
        Assertions.assertThat(localDateTime.getMonthValue()).isEqualTo(4);
        Assertions.assertThat(localDateTime.getYear()).isEqualTo(1);
        Assertions.assertThat(localDateTime.getChronology()).isEqualTo(IsoChronology.INSTANCE);
    }

    @Test
    void range() {
        LocalDateTime localDateTime = LocalDateTime.now();
        for (ChronoField field : ChronoField.values()) {
            try {
                ValueRange valueRange = localDateTime.range(field);
                System.out.println(field + " : " + valueRange);
            } catch (UnsupportedTemporalTypeException e) {
                System.err.println(field + " : Unsupport");
            }
        }
    }

    @Test
    void with() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime + "                                    " + localDateTime);
        System.out.println(localDateTime + " with(DayOfWeek.SUNDAY)           = " + localDateTime.with(DayOfWeek.SUNDAY));
        System.out.println(localDateTime + " with(ChronoField.DAY_OF_WEEK, 5) = " + localDateTime.with(ChronoField.DAY_OF_WEEK, 5));
        System.out.println(localDateTime + " withNano(5566)                   = " + localDateTime.withNano(5566));
        System.out.println(localDateTime + " withSecond(43)                   = " + localDateTime.withSecond(43));
        System.out.println(localDateTime + " withMinute(33)                   = " + localDateTime.withMinute(33));
        System.out.println(localDateTime + " withHour(9)                      = " + localDateTime.withHour(9));
        System.out.println(localDateTime + " withDayOfMonth(5)                = " + localDateTime.withDayOfMonth(5));
        System.out.println(localDateTime + " withDayOfYear(12)                = " + localDateTime.withDayOfYear(12));
        System.out.println(localDateTime + " withMonth(1)                     = " + localDateTime.withMonth(1));
        System.out.println(localDateTime + " withYear(12)                     = " + localDateTime.withYear(12));
    }

    @Test
    void until() {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        LocalDateTime localDateTime2 = localDateTime1.plus(3, ChronoUnit.DAYS);
        Assertions.assertThat(localDateTime1.until(localDateTime2, ChronoUnit.DAYS)).isEqualTo(3);
    }

    @Test
    void query() {
        LocalDateTime now = LocalDateTime.now();
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
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.toLocalDate());
        System.out.println(now.toLocalTime());
        System.out.println(now.toInstant(ZoneOffset.UTC));
        System.out.println(now.toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    public void truncatedTo() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        for (ChronoUnit unit : ChronoUnit.values()) {
            String prefix = "truncatedTo(" + unit + ") = ";
            try {
                LocalDateTime localDateTime = now.truncatedTo(unit);
                System.out.println(prefix + localDateTime);
            } catch (UnsupportedTemporalTypeException e) {
                System.err.println(prefix + "Unsupport");
            }
        }
    }

}
