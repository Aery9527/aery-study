package org.aery.study.java8.time;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class DateTimeFormatterStudy {

    public static final Map<String, DateTimeFormatter> ALL_STATIC_FORMATTER;

    static {
        ALL_STATIC_FORMATTER = Collections.unmodifiableMap(
                Stream.of(DateTimeFormatter.class.getDeclaredFields())
                        .filter(field -> Modifier.isPublic(field.getModifiers()))
                        .filter(field -> Modifier.isStatic(field.getModifiers()))
                        .filter(field -> Modifier.isFinal(field.getModifiers()))
                        .filter(field -> field.getType().equals(DateTimeFormatter.class))
                        .collect(TreeMap::new, (map, field) -> {
                            try {
                                map.put(field.getName(), (DateTimeFormatter) field.get(null));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }, Map::putAll)
        );
    }

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

        List<String> unsupported = new ArrayList<>();

        for (FormatStyle formatStyle : FormatStyle.values()) {
            DateTimeFormatter formatter = formatterFactory.apply(formatStyle);
            String title = "DateTimeFormatter." + method + "(" + formatStyle + ").format";

            for (TemporalAccessor temporal : temporals) {
                String prefix = title + "(" + temporal.getClass().getSimpleName() + ":" + temporal + ") = ";
                try {
                    System.out.println(prefix + formatter.format(temporal));
                } catch (DateTimeException e) {
                    unsupported.add(prefix + e.getMessage());
                }
            }
        }

        unsupported.forEach(System.err::println);
    }

}
