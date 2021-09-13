package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.entity.Man;
import org.aery.study.spring.redis.entity.Person;
import org.aery.study.spring.redis.entity.Woman;
import org.aery.study.spring.redis.service.api.RedisDataExplorer;
import org.assertj.core.api.Assertions;
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
    private ManRepository manRepository;

    @Autowired
    private WomanRepository womanRepository;

    @Autowired
    private RedisDataExplorer redisDataExplorer;

    @Test
    public void saveAndFind() {
        Person aery = new Person("Aery", 30);
        Man james = new Man("James", 60);
        Woman tina = new Woman("Tina", 60);
        Woman rion = new Woman("Rion", 18);
        Man andy = new Man("Andy", 50);

        aery.setFather(james);
        aery.setMother(tina);
        aery.setFavoriteArtists(new ArrayList<>());
        aery.getFavoriteArtists().add(andy);

        aery.getFavoriteArtists().add(rion);
        aery.setFavoriteMans(new ArrayList<>());
        aery.getFavoriteMans().add(andy);
        aery.setFavoriteWomans(new ArrayList<>());
        aery.getFavoriteWomans().add(rion);

        this.personRepository.save(aery);
        this.redisDataExplorer.printExplored();

        Person aery1FromRedis = this.personRepository.findById("Aery").get();
        Assertions.assertThat(aery1FromRedis.getFather()).isNull();
        Assertions.assertThat(aery1FromRedis.getMother()).isNull();
        Assertions.assertThat(aery1FromRedis.getFavoriteArtists()).isEmpty();
        Assertions.assertThat(aery1FromRedis.getFavoriteMans()).isEmpty();
        Assertions.assertThat(aery1FromRedis.getFavoriteWomans()).isEmpty();

        this.manRepository.save(james);
        this.manRepository.save(andy);
        this.womanRepository.save(tina);
        this.womanRepository.save(rion);
        this.redisDataExplorer.printExplored();

        Person aery2FromRedis = this.personRepository.findById("Aery").get();
        Assertions.assertThat(aery2FromRedis.getFather().getName()).isEqualTo(james.getName());
        Assertions.assertThat(aery2FromRedis.getFather().getAge()).isEqualTo(james.getAge());
        Assertions.assertThat(aery2FromRedis.getMother().getName()).isEqualTo(tina.getName());
        Assertions.assertThat(aery2FromRedis.getMother().getAge()).isEqualTo(tina.getAge());
        Assertions.assertThat(aery2FromRedis.getFavoriteArtists()).isEmpty();
        Assertions.assertThat(aery2FromRedis.getFavoriteMans()).hasSize(1).allMatch((person) -> {
            return person.getName().equals(andy.getName()) && person.getAge() == andy.getAge();
        });
        Assertions.assertThat(aery2FromRedis.getFavoriteWomans()).hasSize(1).allMatch((person) -> {
            return person.getName().equals(rion.getName()) && person.getAge() == rion.getAge();
        });
    }

}
