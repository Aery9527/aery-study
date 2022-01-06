package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.entity.DataEntity;
import org.aery.study.spring.redis.service.api.RedisDataExplorer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
@DirtiesContext
public class DataEntityRepositoryTest {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private RedisDataExplorer redisDataExplorer;

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
        this.redisDataExplorer.printExplored();

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
    public void extendedMethod() {
        String id = "Aery";
        String index1 = "kerker";
        String index2 = "puipui";
        String parameter1 = "parameter1";
        String exceed = "Rion";

        DataEntity data1 = new DataEntity();
//        data1.setId(id); // id為空時, 預設會使用UUID
        data1.setIndex1(index1);
        data1.setIndex2(index2);
        data1.setParameter1(parameter1);
        this.dataRepository.save(data1);

        Assertions.assertThat(this.dataRepository.findByIndex1(index1)).isNotEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex2(index2)).isNotEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(index1, index2)).isNotEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(index1, index2)).isNotEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(exceed, index2)).isNotEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(index1, exceed)).isNotEmpty();

        Assertions.assertThat(this.dataRepository.findByIndex1(exceed)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex2(exceed)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(exceed, exceed)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(exceed, index2)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(index1, exceed)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(exceed, exceed)).isEmpty();

        // 雖然DataEntity有parameter1這個field, 但因為沒定義Indexed, 所以是查不出來的
        Assertions.assertThat(this.dataRepository.findByParameter1(parameter1)).isEmpty();

        Assertions.assertThatThrownBy(() -> this.dataRepository.findByKerker(index1))
                .isInstanceOf(PropertyReferenceException.class)
                .hasMessageContaining("No property " + index1 + " found for type DataEntity!");
    }

    @Test
    public void findByIndex() {
        String id1 = "Aery";
        String id2 = "Rion";
        String indexA = "A";
        String indexB = "B";
        String indexC = "C";
        String exceed = "Fuko";

        DataEntity data1 = new DataEntity(id1);
        data1.setIndex1(indexA);
        data1.setIndex2(indexB);
        this.dataRepository.save(data1);

        DataEntity data2 = new DataEntity(id2);
        data2.setIndex1(indexA);
        data2.setIndex2(indexC);
        this.dataRepository.save(data2);

        Assertions.assertThat(this.dataRepository.findByIndex1(indexA)).containsExactlyInAnyOrder(data1, data2);
        Assertions.assertThat(this.dataRepository.findByIndex1(indexB)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1(indexC)).isEmpty();

        Assertions.assertThat(this.dataRepository.findByIndex2(indexA)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex2(indexB)).containsExactlyInAnyOrder(data1);
        Assertions.assertThat(this.dataRepository.findByIndex2(indexC)).containsExactlyInAnyOrder(data2);

        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(indexA, indexB)).containsExactlyInAnyOrder(data1);
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(indexA, indexC)).containsExactlyInAnyOrder(data2);
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(indexA, exceed)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(indexA, exceed)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(exceed, indexB)).isEmpty();
        Assertions.assertThat(this.dataRepository.findByIndex1AndIndex2(exceed, indexC)).isEmpty();

        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(indexA, indexB)).containsExactlyInAnyOrder(data1, data2);
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(indexA, indexC)).containsExactlyInAnyOrder(data1, data2);
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(indexA, exceed)).containsExactlyInAnyOrder(data1, data2);
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(indexA, exceed)).containsExactlyInAnyOrder(data1, data2);
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(exceed, indexB)).containsExactlyInAnyOrder(data1);
        Assertions.assertThat(this.dataRepository.findByIndex1OrIndex2(exceed, indexC)).containsExactlyInAnyOrder(data2);
    }

    @Test
    public void ttl() throws InterruptedException {
        String id1 = "Aery";
        String id2 = "Rion";
        long ttlMs = DataEntity.TTL.SECOND * 1000;

        Consumer<Optional<DataEntity>> checkNotEmpty = (optional) -> Assertions.assertThat(optional).isNotEmpty();
        Consumer<Optional<DataEntity>> checkIsEmpty = (optional) -> Assertions.assertThat(optional).isEmpty();

        DataEntity data1 = new DataEntity(id1);
        this.dataRepository.save(data1);
        Thread.sleep(ttlMs + 100); // 等超過ttl, 測試redis資料是否移除
        checkIsEmpty.accept(this.dataRepository.findById(id1));

        DataEntity data2 = new DataEntity(id2);
        data2.setIndex1("aaa");
        data2.setIndex2("bbb");
        this.dataRepository.save(data2);
        long wait1Ms = ttlMs / 2; // 等一半的時間
        Thread.sleep(wait1Ms);
        this.dataRepository.save(data2); // 再更新一次, 測試ttl是否有重設
        Thread.sleep(wait1Ms + (ttlMs / 4));
        checkNotEmpty.accept(this.dataRepository.findById(id2));

        this.dataRepository.deleteById(id2);
        System.out.println();
    }

}
