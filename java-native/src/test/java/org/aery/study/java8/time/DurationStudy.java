package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * {@link Duration} is "second + nanos" inside, over second is convert from second.
 */
public class DurationStudy {

    @Test
    void of() {
        BiConsumer<String, Duration> show = (title, duration) -> System.out.println(title + " : " + duration);
        show.accept("of(10, ChronoUnit.NANOS)", Duration.of(10, ChronoUnit.MINUTES));
        show.accept("ofNanos(10)", Duration.ofNanos(10));
        show.accept("ofMillis(10)", Duration.ofMillis(10));
        show.accept("ofSeconds(10)", Duration.ofSeconds(10));
        show.accept("ofMinutes(10)", Duration.ofMinutes(10));
        show.accept("ofHours(10)", Duration.ofHours(10));
        show.accept("ofDays(10)", Duration.ofDays(10));
    }

    @Test
    void parse() {
        Consumer<String> show = text -> System.out.println(text + " : " + Duration.parse(text));
        show.accept("PT20.345S");
        show.accept("PT-20.345S");
        show.accept("PT15M");
        show.accept("PT-15M");
        show.accept("PT10H");
        show.accept("PT-10H");
        show.accept("P2D");
        show.accept("P-2D");
        show.accept("P2DT3H4M");
        show.accept("P-2DT3H4M");
        show.accept("P-2DT-3H4M");
        show.accept("P-2DT-3H-4M");
//        show.accept("P-6H3M"); // ??
//        show.accept("-P6H3M"); // ??
//        show.accept("-P-6H+3M"); // ??
    }

    @Test
    void from() {
        TemporalAmount amountOfDuration = Duration.of(10, ChronoUnit.MINUTES);
        TemporalAmount amountOfPeriod = Period.ofDays(1);
        System.out.println(Duration.from(amountOfDuration));
        Assertions.assertThatThrownBy(() -> Duration.from(amountOfPeriod)).isInstanceOf(DateTimeException.class);
    }

    @Test
    void with() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES).plusNanos(10);
        Assertions.assertThat(duration.getNano()).isEqualTo(10);
        Assertions.assertThat(duration.getSeconds()).isEqualTo(600);

        Duration duration1 = duration.withNanos(20);
        Assertions.assertThat(duration1.getNano()).isEqualTo(20);
        Assertions.assertThat(duration1.getSeconds()).isEqualTo(600);

        Duration duration3 = duration.withSeconds(20);
        Assertions.assertThat(duration3.getNano()).isEqualTo(10);
        Assertions.assertThat(duration3.getSeconds()).isEqualTo(20);
    }

    @Test
    void to() {
        Duration duration = Duration.of(65, ChronoUnit.MINUTES).plusNanos(10);
        Assertions.assertThat(duration.toNanos()).isEqualTo((65L * 60 * 1000 * 1000 * 1000) + 10);
        Assertions.assertThat(duration.toMillis()).isEqualTo(65L * 60 * 1000);
        Assertions.assertThat(duration.toMinutes()).isEqualTo(65L);
        Assertions.assertThat(duration.toHours()).isEqualTo(1);
        Assertions.assertThat(duration.toDays()).isEqualTo(0);
    }

    @Test
    void between() {
        Instant instant1 = Instant.now();
        Instant instant2 = instant1.plus(Duration.ofSeconds(10));
        System.out.println(Duration.between(instant1, instant2));
        System.out.println(Duration.between(instant2, instant1));
        System.out.println(Duration.between(instant2, instant1).abs());
    }

    @Test
    void compareTo() {
        Duration duration1 = Duration.of(10, ChronoUnit.MINUTES);
        Assertions.assertThat(duration1.compareTo(duration1)).isEqualTo(0);

        Duration duration2 = duration1.plus(Duration.ofNanos(10));
        Assertions.assertThat(duration1.compareTo(duration2)).isEqualTo(-10); // return nanos difference when seconds same
        Assertions.assertThat(duration2.compareTo(duration1)).isEqualTo(10); // return nanos difference when seconds same

        Duration duration3 = duration1.plus(Duration.ofSeconds(5));
        Assertions.assertThat(duration1.compareTo(duration3)).isEqualTo(-1);
        Assertions.assertThat(duration3.compareTo(duration1)).isEqualTo(1);
    }

    @Test
    void plus() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES);
        BiConsumer<String, Duration> show = (title, plus) -> System.out.println(title + " : " + plus);
        show.accept("duration", duration);
        show.accept("plus(duration)", duration.plus(duration));
        show.accept("plus(10, ChronoUnit.SECONDS)", duration.plus(10, ChronoUnit.SECONDS));
        show.accept("plusNanos(10)", duration.plusNanos(10));
        show.accept("plusMillis(10)", duration.plusMillis(10));
        show.accept("plusSeconds(10)", duration.plusSeconds(10));
        show.accept("plusMinutes(10)", duration.plusMinutes(10));
        show.accept("plusHours(10)", duration.plusHours(10));
        show.accept("plusDays(10)", duration.plusDays(10));
    }

    @Test
    void minus() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES);
        BiConsumer<String, Duration> show = (title, minus) -> System.out.println(title + " : " + minus);
        show.accept("duration", duration);
        show.accept("minus(duration)", duration.minus(duration));
        show.accept("minus(10, ChronoUnit.SECONDS)", duration.minus(10, ChronoUnit.SECONDS));
        show.accept("minusNanos(10)", duration.minusNanos(10));
        show.accept("minusMillis(10)", duration.minusMillis(10));
        show.accept("minusSeconds(10)", duration.minusSeconds(10));
        show.accept("minusMinutes(10)", duration.minusMinutes(10));
        show.accept("minusHours(10)", duration.minusHours(10));
        show.accept("minusDays(10)", duration.minusDays(10));
    }

    @Test
    void get_getUnits() {
        Duration duration1 = Duration.of(10, ChronoUnit.MINUTES);
        Duration duration2 = duration1.plus(Duration.ofNanos(10));

        System.out.println("duration2.getNano() : " + duration2.getNano());
        System.out.println("duration2.getSeconds() : " + duration2.getSeconds());

        List<TemporalUnit> supportedUnits = duration2.getUnits();

        for (ChronoUnit unit : ChronoUnit.values()) {
            if (supportedUnits.contains(unit)) {
                System.out.println("get(" + unit + ") : " + duration2.get(unit));
            } else {
                Assertions.assertThatThrownBy(() -> duration2.get(unit)).isInstanceOf(UnsupportedTemporalTypeException.class);
            }
        }
    }

    @Test
    void addTo() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES);
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();

        System.out.println(instant + " (" + instant.getClass() + ")");
        System.out.println(localDateTime + " (" + localDateTime.getClass() + ")");
        System.out.println(localDate + " (" + localDate.getClass() + ")");

        Temporal addToInstant = duration.addTo(instant);
        System.out.println("addTo(Instant)       : " + addToInstant + " (" + addToInstant.getClass() + ")");

        Temporal addLocalDateTime = duration.addTo(localDateTime);
        System.out.println("addTo(LocalDateTime) : " + addLocalDateTime + " (" + addLocalDateTime.getClass() + ")");

        Assertions.assertThatThrownBy(() -> duration.addTo(localDate)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void subtractFrom() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES);
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();

        System.out.println(instant + " (" + instant.getClass() + ")");
        System.out.println(localDateTime + " (" + localDateTime.getClass() + ")");
        System.out.println(localDate + " (" + localDate.getClass() + ")");

        Temporal addToInstant = duration.subtractFrom(instant);
        System.out.println("addTo(Instant)       : " + addToInstant + " (" + addToInstant.getClass() + ")");

        Temporal addLocalDateTime = duration.subtractFrom(localDateTime);
        System.out.println("addTo(LocalDateTime) : " + addLocalDateTime + " (" + addLocalDateTime.getClass() + ")");

        Assertions.assertThatThrownBy(() -> duration.subtractFrom(localDate)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void dividedBy() {
        Duration duration = Duration.of(10, ChronoUnit.SECONDS);
        System.out.println(duration);
        System.out.println(duration.dividedBy(2));
        System.out.println(duration.dividedBy(3));
    }

    @Test
    void multipliedBy() {
        Duration duration = Duration.of(10, ChronoUnit.SECONDS);
        System.out.println(duration);
        System.out.println(duration.dividedBy(3).multipliedBy(3));
        System.out.println(duration.multipliedBy(3).dividedBy(3));
    }

    @Test
    void negated() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES);
        System.out.println(duration);
        System.out.println(duration.negated());
    }

    @Test
    void isNegative() {
        Duration duration = Duration.of(10, ChronoUnit.MINUTES);
        Assertions.assertThat(duration.isNegative()).isFalse();
        Assertions.assertThat(duration.negated().isNegative()).isTrue();
    }

    @Test
    void isZero() {
        Duration duration = Duration.of(0, ChronoUnit.MINUTES);
        Assertions.assertThat(duration.isZero()).isTrue();
        Assertions.assertThat(duration.plusNanos(1).isZero()).isFalse();
    }

}
