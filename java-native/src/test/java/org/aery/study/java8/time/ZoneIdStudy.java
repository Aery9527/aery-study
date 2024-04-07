package org.aery.study.java8.time;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ZoneIdStudy {

    @Test
    void getAvailableZoneIds() {
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        System.out.println("Available ZoneIds(" + availableZoneIds.size() + ") :");
        availableZoneIds.forEach(System.out::println);
    }

    @Test
    void systemDefault() {
        System.out.println("System Default ZoneId : " + ZoneId.systemDefault());
    }

    @Test
    void SHORT_IDS() {
        Map<String, String> shortIds = ZoneId.SHORT_IDS;
        System.out.println("SHORT_IDS(" + shortIds.size() + ") :");
        shortIds.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    @Test
    void of() {
        ZoneId zoneId1 = ZoneId.of("Asia/Tokyo");
        ZoneId zoneId2 = ZoneId.of("JST", ZoneId.SHORT_IDS); // JST is the short id of Asia/Tokyo
        ZoneId zoneId3 = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(9));
        ZoneId zoneId4 = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(9));
        ZoneId zoneId5 = ZoneId.ofOffset("UT", ZoneOffset.ofHours(9));
        ZoneId zoneId6 = ZoneId.ofOffset("", ZoneOffset.ofHours(9));

        Consumer<ZoneId> show = zoneId -> System.out.println(zoneId + " | " + zoneId.getRules());
        show.accept(zoneId1);
        show.accept(zoneId2);
        show.accept(zoneId3);
        show.accept(zoneId4);
        show.accept(zoneId5);
        show.accept(zoneId6);
    }

}
