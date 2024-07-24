package org.aery.study.jdk9;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JEP269_ConvenienceFactoryMethodsforCollections {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void list_of() {
        List<String> list = List.of("b", "a", "d", "c");
        Assertions.assertThat(list).containsExactly("b", "a", "d", "c");
        Assertions.assertThatThrownBy(() -> list.add("d")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void set_of() {
        // order by hashcode, so in a way it's sorted randomly, see ImmutableCollections.SetN#probe
        Set<String> set = Set.of("a", "b", "c", "d");
        this.logger.info("{}", set);
        Assertions.assertThatThrownBy(() -> set.add("d")).isInstanceOf(UnsupportedOperationException.class);

        Assertions.assertThatThrownBy(() -> Set.of("a", "b", "a")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void map_of() {
        // max argument is 10, key order is same to Set.of()
        Map<String, Integer> mapIn10 = Map.of(
                "a", 1,
                "b", 2,
                "c", 3,
                "d", 4,
                "e", 5,
                "f", 6,
                "g", 7,
                "h", 8,
                "i", 9,
                "j", 10
        );
        Assertions.assertThatThrownBy(() -> mapIn10.put("k", 11)).isInstanceOf(UnsupportedOperationException.class);

        Map<String, Integer> mapOver10 = Map.ofEntries(
                Map.entry("a", 1),
                Map.entry("b", 2),
                Map.entry("c", 3)
        );
        Assertions.assertThatThrownBy(() -> mapOver10.put("k", 11)).isInstanceOf(UnsupportedOperationException.class);

        Assertions.assertThatThrownBy(() -> Map.of("a", 1, "a", 2)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> Map.of("a", null)).isInstanceOf(NullPointerException.class);
        Assertions.assertThatThrownBy(() -> Map.of(null, 1)).isInstanceOf(NullPointerException.class);
    }

}
