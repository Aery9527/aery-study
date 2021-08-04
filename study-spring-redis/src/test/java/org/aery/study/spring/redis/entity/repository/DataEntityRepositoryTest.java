package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.entity.DataEntity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class DataEntityRepositoryTest {

    @Autowired
    private DataRepository dataRepository;

    @Test
    public void saveAndFindById() {
        String id = "Aery";

        DataEntity data1 = new DataEntity();
        data1.setId(id);
        data1.setIndex1("Alicia");
        data1.setIndex2("Rion");
        data1.setParameter1("");
        data1.setDate(new Date());
        data1.setMap(new HashMap<>());
        data1.setList(new ArrayList<>());
        data1.setSet(new HashSet<>());

        Map<String, DataEntity.Detail> map = data1.getMap();
        map.put("a", new DataEntity.Detail("A", 1));
        map.put("b", new DataEntity.Detail("B", 2));

        List<String> list = data1.getList();
        list.add("abc");
        list.add("def");
        list.add("abc");

        Set<String> set = data1.getSet();
        set.add("x");
        set.add("y");

        this.dataRepository.save(data1);

        Optional<DataEntity> optional = this.dataRepository.findById(id);

        Assertions.assertThat(optional).isNotEmpty();
        DataEntity data2 = optional.get();

        Assertions.assertThat(data2.getId()).isEqualTo(data1.getId());
        Assertions.assertThat(data2.getIndex1()).isEqualTo(data1.getIndex1());
        Assertions.assertThat(data2.getIndex2()).isEqualTo(data1.getIndex2());
        Assertions.assertThat(data2.getParameter1()).isEmpty();
        Assertions.assertThat(data2.getParameter2()).isNull();
        Assertions.assertThat(data2.getDate()).isEqualTo(data1.getDate());
        Assertions.assertThat(data2.getMap()).containsAllEntriesOf(data1.getMap());
        Assertions.assertThat(data2.getList()).containsExactly(data1.getList().toArray(new String[0]));
        Assertions.assertThat(data2.getSet()).containsExactly(data1.getSet().toArray(new String[0]));
    }

    @Test
    public void updateAndDelete() {
        String id = "Aery";

        DataEntity data1 = new DataEntity();
        data1.setId(id);
        this.dataRepository.save(data1);

        DataEntity data2 = this.dataRepository.findById(id).get();
        Assertions.assertThat(data2.getIndex1()).isNull(); // index允許null
        data2.setIndex1("9527"); // 沒有持久層, 所以要save回去
        this.dataRepository.save(data2);

        DataEntity data3 = this.dataRepository.findById(id).get();
        Assertions.assertThat(data3.getIndex1()).isEqualTo(data2.getIndex1());
        data3.setIndex1(null);
        this.dataRepository.save(data3); // 主資料會移除index1, 但metadata會留著

        DataEntity data4 = this.dataRepository.findById(id).get();
        Assertions.assertThat(data4.getIndex1()).isNull();

        this.dataRepository.deleteById(id); // 所有資料包含metadata都會移除
        Optional<DataEntity> optional = this.dataRepository.findById(id);
        Assertions.assertThat(optional).isEmpty();
    }

    @Test
    public void findByIndex() {

    }

}
