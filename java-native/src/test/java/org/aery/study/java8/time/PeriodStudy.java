package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * {@link Period} smallest unit is "day", calculated using the year and month calendar.
 */
public class PeriodStudy {

    @Test
    void of() {
        System.out.println(Period.of(1, 2, 3));
        System.out.println(Period.ofDays(1));
        System.out.println(Period.ofWeeks(1));
        System.out.println(Period.ofMonths(1));
        System.out.println(Period.ofYears(1));
    }

    @Test
    void parse() {
        Consumer<String> show = text -> System.out.println(text + " : " + Period.parse(text));
        show.accept("P2Y");
        show.accept("P3M");
        show.accept("P4W");
        show.accept("P5D");
        show.accept("P1Y2M3D");
        show.accept("P1Y2M3W4D");
        show.accept("P-1Y2M");
        show.accept("-P1Y2M");
    }

    @Test
    void from() {
        TemporalAmount amountOfPeriod = Period.ofDays(1);
        TemporalAmount amountOfDuration = Duration.of(10, ChronoUnit.MINUTES);
        System.out.println(Period.from(amountOfPeriod));
        Assertions.assertThatThrownBy(() -> Period.from(amountOfDuration)).isInstanceOf(DateTimeException.class);
    }

    @Test
    void with() {
        Period period = Period.of(1, 2, 3);
        System.out.println(period);
        Assertions.assertThat(period.getDays()).isEqualTo(3);
        Assertions.assertThat(period.getMonths()).isEqualTo(2);
        Assertions.assertThat(period.getYears()).isEqualTo(1);

        Period period1 = period.withDays(40);
        System.out.println(period1);
        Assertions.assertThat(period1.getDays()).isEqualTo(40);
        Assertions.assertThat(period1.getMonths()).isEqualTo(2);
        Assertions.assertThat(period1.getYears()).isEqualTo(1);

        Period period2 = period.withMonths(-1);
        System.out.println(period2);
        Assertions.assertThat(period2.getDays()).isEqualTo(3);
        Assertions.assertThat(period2.getMonths()).isEqualTo(-1);
        Assertions.assertThat(period2.getYears()).isEqualTo(1);

        Period period3 = period.withYears(0);
        System.out.println(period3);
        Assertions.assertThat(period3.getDays()).isEqualTo(3);
        Assertions.assertThat(period3.getMonths()).isEqualTo(2);
        Assertions.assertThat(period3.getYears()).isEqualTo(0);
    }

    @Test
    void to() {
        Period period = Period.of(1, 2, 3);
        Assertions.assertThat(period.toTotalMonths()).isEqualTo(12 + 2);
    }

    @Test
    void between() {
        Period period = Period.ofDays(10);
        LocalDate localDate1 = LocalDate.now();
        LocalDate localDate2 = localDate1.plus(period);
        Assertions.assertThat(Period.between(localDate1, localDate2)).isEqualTo(period);
        Assertions.assertThat(Period.between(localDate2, localDate1)).isEqualTo(period.negated());
    }

    @Test
    void plus() {
        Period period = Period.of(1, 2, 3);
        BiConsumer<String, Period> show = (title, plus) -> System.out.println(title + " : " + plus);
        show.accept("period", period);
        show.accept("plus(period)", period.plus(period));
        show.accept("plus(Period.ofDays(1))", period.plus(Period.ofDays(1)));
        show.accept("plusDays(1)", period.plusDays(1));
        show.accept("plusMonths(1)", period.plusMonths(1));
        show.accept("plusYears(1)", period.plusYears(1));
        Assertions.assertThatThrownBy(() -> period.plus(Duration.ofDays(1))).isInstanceOf(DateTimeException.class);
    }

    @Test
    void minus() {
        Period period = Period.of(1, 2, 3);
        BiConsumer<String, Period> show = (title, plus) -> System.out.println(title + " : " + plus);
        show.accept("period", period);
        show.accept("minus(period)", period.minus(period));
        show.accept("minus(Period.ofDays(1))", period.minus(Period.ofDays(1)));
        show.accept("minusDays(1)", period.minusDays(1));
        show.accept("minusMonths(1)", period.minusMonths(1));
        show.accept("minusYears(1)", period.minusYears(1));
        Assertions.assertThatThrownBy(() -> period.minus(Duration.ofDays(1))).isInstanceOf(DateTimeException.class);
    }

    @Test
    void get_getUnits() {
        Period period = Period.of(1, 2, 3);

        System.out.println("getDays() : " + period.getDays());
        System.out.println("getMonths() : " + period.getMonths());
        System.out.println("getYears() : " + period.getYears());
        System.out.println("getChronology() : " + period.getChronology());

        List<TemporalUnit> supportedUnits = period.getUnits();

        for (ChronoUnit unit : ChronoUnit.values()) {
            if (supportedUnits.contains(unit)) {
                System.out.println("get(" + unit + ") : " + period.get(unit));
            } else {
                Assertions.assertThatThrownBy(() -> period.get(unit)).isInstanceOf(UnsupportedTemporalTypeException.class);
            }
        }
    }

    @Test
    void addTo() {
        Period period = Period.of(1, 2, 3);
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();

        System.out.println(instant + " (" + instant.getClass() + ")");
        System.out.println(localDateTime + " (" + localDateTime.getClass() + ")");
        System.out.println(localDate + " (" + localDate.getClass() + ")");

        Assertions.assertThatThrownBy(() -> period.addTo(instant)).isInstanceOf(UnsupportedTemporalTypeException.class);

        Temporal addToLocalDateTime = period.addTo(localDateTime);
        System.out.println("addTo(LocalDateTime) : " + addToLocalDateTime + " (" + addToLocalDateTime.getClass() + ")");

        Temporal addToLocalDate = period.addTo(localDate);
        System.out.println("addTo(LocalDate)     : " + addToLocalDate + " (" + addToLocalDate.getClass() + ")");
    }

    @Test
    void subtractFrom() {
        Period period = Period.of(1, 2, 3);
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();

        System.out.println(instant + " (" + instant.getClass() + ")");
        System.out.println(localDateTime + " (" + localDateTime.getClass() + ")");
        System.out.println(localDate + " (" + localDate.getClass() + ")");

        Assertions.assertThatThrownBy(() -> period.subtractFrom(instant)).isInstanceOf(UnsupportedTemporalTypeException.class);

        Temporal addToLocalDateTime = period.subtractFrom(localDateTime);
        System.out.println("addTo(LocalDateTime) : " + addToLocalDateTime + " (" + addToLocalDateTime.getClass() + ")");

        Temporal addToLocalDate = period.subtractFrom(localDate);
        System.out.println("addTo(LocalDate)     : " + addToLocalDate + " (" + addToLocalDate.getClass() + ")");
    }

    @Test
    void multipliedBy() {
        Period period = Period.of(1, 2, 3);
        System.out.println(period);
        System.out.println(period.multipliedBy(2));
    }

    @Test
    void negated() {
        Period period = Period.of(1, 2, 3);
        System.out.println(period);
        System.out.println(period.negated());
    }

    @Test
    void isNegative() {
        Period period = Period.of(1, 2, 3);
        Assertions.assertThat(period.isNegative()).isFalse();
        Assertions.assertThat(period.negated().isNegative()).isTrue();
    }

    @Test
    void isZero() {
        Period period = Period.of(0, 0, 0);
        Assertions.assertThat(period.isZero()).isTrue();
        Assertions.assertThat(period.plusDays(1).isZero()).isFalse();
    }

    @Test
    void normalized() {
        Period period1 = Period.of(0, 0, 40);
        Period period2 = period1.plusMonths(20);
        System.out.println(period1);
        System.out.println(period1.normalized());
        System.out.println(period2);
        System.out.println(period2.normalized());
    }

}
