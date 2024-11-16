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
- [java 11 (LTS)](./version-feature/jdk11-features.md)
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
- [java 13](./version-feature/jdk13-features.md)
  - none
- [java 14](./version-feature/jdk14-features.md)
  - [增加(JFR) 事件流功能, 方便從 jfr 檔案讀取事件處理](./version-feature/jdk14-features.md#349--jfr-event-streaming)
  - [將 `switch` 語句轉變為一種語句和表達式, 引入了新的語法結構](./version-feature/jdk14-features.md#361--switch-expressions--standard-)
  - [改進 `NullPointerException` 的訊息, 能精確地描述哪個變數為 null](./version-feature/jdk14-features.md#358--helpful-nullpointerexceptions)
  - [移除 **CMS** 垃圾回收器](./version-feature/jdk14-features.md#363--remove-the-concurrent-mark-sweep--cms--garbage-collector)
  - [棄用 **Parallel Scavenge** + **Serial Old** 垃圾回收器組合](./version-feature/jdk14-features.md#366--deprecate-the-parallelscavenge--serialold-gc-combination)
- [java 15](./version-feature/jdk15-features.md)
  - [正式推出 **ZGC**, 這是一個可擴展的低延遲垃圾回收器, 適合大規模應用場景](./version-feature/jdk15-features.md#377--zgc--a-scalable-low-latency-garbage-collector)
  - [正式推出 **Shenandoah GC**, 是一個低延遲的垃圾回收器, 旨在最大限度地減少 GC 暫停時間](./version-feature/jdk15-features.md#377--zgc--a-scalable-low-latency-garbage-collector)
  - [引入了 **Text Blocks**, 允許多行字串文字表示法, 減少不必要的跳脫字元並改善程式碼的可讀性](./version-feature/jdk15-features.md#378--text-blocks)
  - [引入了隱藏類(Hidden Classes), 這些類僅能通過反射訪問而無法被其他類直接使用](./version-feature/jdk15-features.md#371--hidden-classes)
- [java 16](./version-feature/jdk16-features.md)
  - [針對 **值導向類別 (value-based classes)** 提供警告, 尤其是對原始類別包裝器(如 Integer、Double 等)進行調整](./version-feature/jdk16-features.md#390--warnings-for-value-based-classes)
  - [提供了 `jpackage` 工具, 用於將 Java 應用程式打包成自包含的可安裝包](./version-feature/jdk16-features.md#392--packaging-tool)
  - [提議為 `instanceof` 操作符引入模式匹配, 簡化類型檢查與轉型的代碼, EX: `if (obj instanceof String str) { System.out.println(str.toUpperCase()) }`](./version-feature/jdk16-features.md#394--pattern-matching-for-instanceof)
  - [引入 `Record` 類別, 用來簡單表示不可變數據的透明載體, 會自動生成如 equals/hashCode/toString 等基本方法](./version-feature/jdk16-features.md#395--records)
  - [改進 **ZGC** 的線程堆疊處理從 safepoint 遷移到並行階段的方案](./version-feature/jdk16-features.md#376--zgc--concurrent-thread-stack-processing)

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

