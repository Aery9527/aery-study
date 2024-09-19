package org.aery.study.jdk21;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JEP431_Sequenced_Collections {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEP431_Sequenced_Collections.class);

    @Test
    void SequencedCollection() {
        SequencedCollection<String> sc1 = new ArrayList<>();

        sc1.addFirst("A");
        sc1.addFirst("B");
        sc1.addLast("C");
        LOGGER.info("sc1               : {}", sc1);
        LOGGER.info("sc1.getFirst()    : {}", sc1.getFirst());
        LOGGER.info("sc1.getLast()     : {}", sc1.getLast());

        SequencedCollection<String> sc2 = sc1.reversed(); // same reference
        LOGGER.info("sc1 -> sc2        : {} -> {}", sc1, sc2);

        String first = sc1.removeFirst();
        LOGGER.info("sc1.removeFirst() : {} -> sc1 : {}, sc2: {}", first, sc1, sc2);
        String last = sc1.removeLast();
        LOGGER.info("sc1.removeLast()  : {} -> sc1 : {}, sc2: {}", last, sc1, sc2);
    }

    @Test
    void SequencedSet() {
        SequencedSet<String> sc1 = new LinkedHashSet<>();

        sc1.addFirst("A");
        sc1.addFirst("B");
        sc1.addLast("C");
        LOGGER.info("sc1               : {}", sc1);
        LOGGER.info("sc1.getFirst()    : {}", sc1.getFirst());
        LOGGER.info("sc1.getLast()     : {}", sc1.getLast());

        SequencedCollection<String> sc2 = sc1.reversed(); // same reference
        LOGGER.info("sc1 -> sc2        : {} -> {}", sc1, sc2);

        String first = sc1.removeFirst();
        LOGGER.info("sc1.removeFirst() : {} -> sc1 : {}, sc2: {}", first, sc1, sc2);
        String last = sc1.removeLast();
        LOGGER.info("sc1.removeLast()  : {} -> sc1 : {}, sc2: {}", last, sc1, sc2);
    }

    @Test
    void SequencedMap() {
        SequencedMap<String, String> sm1 = new LinkedHashMap<>();

        sm1.putFirst("A", "1");
        sm1.putFirst("B", "2");
        sm1.putLast("C", "3");
        LOGGER.info("sm1                     : {}", sm1);
        LOGGER.info("sm1.firstEntry()        : {}", sm1.firstEntry());
        LOGGER.info("sm1.lastEntry()         : {}", sm1.lastEntry());

        SequencedMap<String, String> sm2 = sm1.reversed(); // same reference
        LOGGER.info("sm1 -> sm2              : {} -> {}", sm1, sm2);

        LOGGER.info("sm1.sequencedKeySet()   : {}", sm1.sequencedKeySet());
        LOGGER.info("sm1.sequencedValues()   : {}", sm1.sequencedValues());
        LOGGER.info("sm1.sequencedEntrySet() : {}", sm1.sequencedEntrySet());

        Map.Entry<String, String> first = sm1.pollFirstEntry();
        LOGGER.info("sm1.pollFirstEntry()    : {} -> sm1 : {}, sm2: {}", first, sm1, sm2);
        Map.Entry<String, String> last = sm1.pollLastEntry();
        LOGGER.info("sm1.pollLastEntry()     : {} -> sm1 : {}, sm2: {}", last, sm1, sm2);
    }

}
