package org.aery.study.java8.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.function.Consumer;

public class ClockStudy {

    /**
     * study {@link Clock.SystemClock}
     */
    @Test
    void SystemClock() {
        String subClass = "java.time.Clock$SystemClock";

        Clock clock1 = Clock.systemUTC();
        Clock clock2 = Clock.systemDefaultZone();
        Clock clock3 = Clock.system(ZoneId.of("Asia/Taipei"));
        Clock clock4 = Clock.system(ZoneId.of("Asia/Tokyo"));

        Consumer<Clock> show = clock -> {
            System.out.println(clock + " | " + clock.getZone().getRules());
            Assertions.assertThat(clock.getClass().getName()).isEqualTo(subClass);
        };
        show.accept(clock1);
        show.accept(clock2);
        show.accept(clock3);
        show.accept(clock4);
    }

    /**
     * study {@link Clock.FixedClock}
     */
    @Test
    void FixedClock() {
        String subClass = "java.time.Clock$FixedClock";

        Clock clock1 = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        Consumer<Clock> show = clock -> {
            System.out.println(clock + " | " + clock.getZone().getRules());
            Assertions.assertThat(clock.getClass().getName()).isEqualTo(subClass);
        };
        show.accept(clock1);
    }

    /**
     * study {@link Clock.OffsetClock}
     */
    @Test
    void Clock$OffsetClock() {
        String subClass = "java.time.Clock$OffsetClock";

        Clock clock1 = Clock.offset(Clock.systemDefaultZone(), Duration.ofHours(8));

        Consumer<Clock> show = clock -> {
            System.out.println(clock + " | " + clock.getZone().getRules());
            Assertions.assertThat(clock.getClass().getName()).isEqualTo(subClass);
        };
        show.accept(clock1);
    }

    @Test
    void TickClock() {
        String subClass = "java.time.Clock$TickClock";

        Clock clock1 = Clock.tick(Clock.systemDefaultZone(), Duration.ofSeconds(1));
        Clock clock2 = Clock.tickMinutes(ZoneId.systemDefault());
        Clock clock3 = Clock.tickSeconds(ZoneId.systemDefault());

        Consumer<Clock> show = clock -> {
            System.out.println(clock + " | " + clock.getZone().getRules());
            Assertions.assertThat(clock.getClass().getName()).isEqualTo(subClass);
        };
        show.accept(clock1);
        show.accept(clock2);
        show.accept(clock3);
    }

}
