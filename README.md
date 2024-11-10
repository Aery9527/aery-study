# version-feature

study all java version featiures.

- **incubator** : features are not mature yet but allow early adopters and enthusiasts to use them in experiments.
  You can expect them to be buggy or unstable as they need to further mature in the next releases.
  But they can even be removed again, based on the feedback and the evolution of their development.
- **preview** : features have been fully specified, developed, and implemented, 
  but are still being evaluated and can further change.
  They can be used in development to experiment with upcoming new features and can be expected to become available in a future version, 
  although this is still not guaranteed.

### important features and syntax changes/enhancements

- [java 9](./version-feature/jdk9-features.md)
  - [`List.of()` `Set.of()` `Map.of()` 新增方便建立 Collection 的 API](./version-feature/jdk9-features.md#269--convenience-factory-methods-for-collections)
  - [`StackWalker` 新的 Stack 遍歷 API ](./version-feature/jdk9-features.md#259--stack-walking-api)
  - [`Cleaner` 替代 `finalize` 的新方法](./version-feature/jdk9-features.md#cleaner)
  - [`VarHandle` 類似 reflection 的新機制](./version-feature/jdk9-features.md#193--variable-handles)
  - [`Process` 增強](./version-feature/jdk9-features.md#102--process-api-updates)
  - [java beans 規範優化, 開發者不再需要手動編寫 `BeanInfo` 類](./version-feature/jdk9-features.md#256--beaninfo-annotations)
  - [模組系統 (Module System)](./version-feature/jdk9-features.md#261--module-system)
  - [JVM通用log系統](./version-feature/jdk9-features.md#158--unified-jvm-logging)
  - [支援單個 JAR 檔案中包含針對不同 Java 平台版本的多個類文件版本](./version-feature/jdk9-features.md#238--multi-release-jar-files)
  - [G1 設為預設垃圾回收器](./version-feature/jdk9-features.md#248--make-g1-the-default-garbage-collector)

---

# java-native

about java native function.

---

# spring-*

about spring function.

[Spring Data Redis of Repositories](https://docs.spring.io/spring-data/redis/docs/2.5.3/reference/html/#redis.repositories)

---

# graphviz

about visualize, can generate .jpg/.png etc.

---

# squirrel-foundation

state machine lib, like spring-statemachine

---

# rocketmq

MQ lib, client of consumer and producer

