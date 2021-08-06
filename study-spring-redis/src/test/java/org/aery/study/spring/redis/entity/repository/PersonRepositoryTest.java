package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.entity.Man;
import org.aery.study.spring.redis.entity.Person;
import org.aery.study.spring.redis.entity.Woman;
import org.aery.study.spring.redis.service.api.RedisDataExplorer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WomanRepository womanRepository;

    @Autowired
    private ManRepository manRepository;

    @Autowired
    private RedisDataExplorer redisDataExplorer;

    @Test
    public void saveAndFind() {
        Person aery = new Person("Aery", 30);
        Man james = new Man("James", 60);
        Woman tina = new Woman("Tina", 60);
        Woman rion = new Woman("Rion", 18);
        Man andy = new Man("Andy ", 50);

        aery.setFather(james);
        aery.setMother(tina);
        aery.setFavoriteArtist(new ArrayList<>());
        aery.getFavoriteArtist().add(rion);
        aery.getFavoriteArtist().add(andy);

        this.personRepository.save(aery);



    }

}
