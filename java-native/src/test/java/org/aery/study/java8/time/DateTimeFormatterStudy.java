package org.aery.study.java8.time;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class DateTimeFormatterStudy {

    @Test
    void ofPattern() {
        LocalDateTime localDateTime = LocalDateTime.now();

        List<String> formats = Arrays.asList(
                "yyyy-MM-dd HH:mm:ss.SSS",
                "uuu-MMM-dd HH:mm:ss.SSS"
        );

        List<Locale> Locales = Arrays.asList(
                Locale.TAIWAN,
                Locale.US
        );

        formats.forEach(format -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format); // with default Locale
            String formated = formatter.format(localDateTime);
            System.out.println("ofPattern(" + format + ")        : " + formated);
        });

        formats.forEach(format -> Locales.forEach(locale -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);
            String formated = formatter.format(localDateTime);
            System.out.println("ofPattern(" + format + ", " + locale + ") : " + formated);
        }));
    }

    @Test
    void ofLocalizedDate() {
        test_ofLocalizedSeries("ofLocalizedTime", DateTimeFormatter::ofLocalizedDate);
    }

    @Test
    void ofLocalizedTime() {
        test_ofLocalizedSeries("ofLocalizedTime", DateTimeFormatter::ofLocalizedTime);
    }

    @Test
    void ofLocalizedDateTime() {
        test_ofLocalizedSeries("ofLocalizedDateTime", DateTimeFormatter::ofLocalizedDateTime);
    }

    private void test_ofLocalizedSeries(String method, Function<FormatStyle, DateTimeFormatter> formatterFactory) {
        List<TemporalAccessor> temporals = new ArrayList<>();
        temporals.add(Instant.now());
        temporals.add(LocalDate.now());
        temporals.add(LocalTime.now());
        temporals.add(LocalDateTime.now());
//        temporals.add(MonthDay.now());
//        temporals.add(Year.now());
//        temporals.add(YearMonth.now());
//        temporals.addAll(Arrays.asList(DayOfWeek.values()));
//        temporals.addAll(Arrays.asList(Month.values()));

        for (FormatStyle formatStyle : FormatStyle.values()) {
            DateTimeFormatter formatter = formatterFactory.apply(formatStyle);
            String title = "DateTimeFormatter." + method + "(" + formatStyle + ").format";

            for (TemporalAccessor temporal : temporals) {
                String prefix = title + "(" + temporal.getClass().getSimpleName() + ":" + temporal + ") = ";
                try {
                    System.out.println(prefix + formatter.format(temporal));
                } catch (DateTimeException e) {
                    System.err.println(prefix + e.getMessage());
                }
            }
        }
    }

}
