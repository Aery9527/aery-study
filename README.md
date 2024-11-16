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

- [java 9](./version-feature/jdk09-features.md)
  - [`List.of()` `Set.of()` `Map.of()` 新增方便建立 Collection 的 API](./version-feature/jdk09-features.md#269--convenience-factory-methods-for-collections)
  - [`StackWalker` 新的 Stack 遍歷 API ](./version-feature/jdk09-features.md#259--stack-walking-api)
  - [`Cleaner` 替代 `finalize` 的新方法](./version-feature/jdk09-features.md#cleaner)
  - [`VarHandle` 類似 reflection 的新機制](./version-feature/jdk09-features.md#193--variable-handles)
  - [`Process` 增強](./version-feature/jdk09-features.md#102--process-api-updates)
  - [java beans 規範優化, 開發者不再需要手動編寫 `BeanInfo` 類](./version-feature/jdk09-features.md#256--beaninfo-annotations)
  - [模組系統 (Module System)](./version-feature/jdk09-features.md#261--module-system)
  - [JVM通用log系統](./version-feature/jdk09-features.md#158--unified-jvm-logging)
  - [支援單個 JAR 檔案中包含針對不同 Java 平台版本的多個類文件版本](./version-feature/jdk09-features.md#238--multi-release-jar-files)
  - [G1 設為預設垃圾回收器](./version-feature/jdk09-features.md#248--make-g1-the-default-garbage-collector)
- [java 10](./version-feature/jdk10-features.md)
  - [可區域變數使用`var`宣告變數, 降低複雜度與提高閱讀性, 關注在邏輯](./version-feature/jdk10-features.md#286--local-variable-type-inference)
  - [G1 將 Full GC 並行化, 並通過 `-XX:ParallelGCThreads` 選項控制線程數](./version-feature/jdk10-features.md#307--parallel-full-gc-for-g1)
- [java 11](./version-feature/jdk11-features.md)
  - [新的HTTP API, 支援 HTTP/2和websocket](./version-feature/jdk11-features.md#321--http-client--standard-)
  - [允許在 lambda 裡使用 `var`](./version-feature/jdk11-features.md#323--local-variable-syntax-for-lambda-parameters)
  - [新的數據收集框架 **JFR**, 用於故障排除、監控和分析 Java 應用程序和 HotSpot JVM](./version-feature/jdk11-features.md#328--flight-recorder)
  - [新增 **Epsilon GC**, 一種無操作的垃圾收集器, 它處理記憶體分配但不進行回收, 一旦堆內存耗盡, JVM 會關閉](./version-feature/jdk11-features.md#318--epsilon--a-no-op-garbage-collector)
  - [新增允許直接執行 `.java` 檔案而不需先編譯的功能, 例如: `java HelloWorld.java` 直接執行](./version-feature/jdk11-features.md#330--launch-single-file-source-code-programs)
  - [提供低開銷的堆積(stack)剖析方法, 對性能影響小, 適合持續啟用](./version-feature/jdk11-features.md#331--low-overhead-heap-profiling)
- [java 12](./version-feature/jdk12-features.md)
  - [新增基於 Java 微基準測試架構(JMH), JMH 專注於性能測試, 與 Junit 專注於業務邏輯的正確性測試不同](./version-feature/jdk12-features.md#230--microbenchmark-suite)
  - [改進 G1 垃圾收集器, 避免超過預設的暫停時間](./version-feature/jdk12-features.md#344--abortable-mixed-collections-for-g1)
  - [改進 G1 垃圾收集器, 使其能夠在應用程式閒置時, 自動將未使用的 Java 堆記憶體返還給作業系統](./version-feature/jdk12-features.md#346--promptly-return-unused-committed-memory-from-g1)

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

