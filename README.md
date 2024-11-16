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
  - [G1 設為預設垃圾回收器](./version-feature/jdk09-features.md#248--make-g1-the-default-garbage-collector) *[GC]*
- [java 10](./version-feature/jdk10-features.md)
  - [區域變數可使用`var`宣告變數, 降低複雜度與提高閱讀性, 關注在邏輯](./version-feature/jdk10-features.md#286--local-variable-type-inference)
  - [G1 將 Full GC 並行化, 並通過 `-XX:ParallelGCThreads` 選項控制線程數](./version-feature/jdk10-features.md#307--parallel-full-gc-for-g1) *[GC]*
- [java 11 (LTS)](./version-feature/jdk11-features.md)
  - [新的HTTP API, 支援 HTTP/2和websocket](./version-feature/jdk11-features.md#321--http-client--standard-)
  - [允許在 lambda 裡使用 `var`](./version-feature/jdk11-features.md#323--local-variable-syntax-for-lambda-parameters)
  - [新的數據收集框架 **JFR**, 用於故障排除、監控和分析 Java 應用程序和 HotSpot JVM](./version-feature/jdk11-features.md#328--flight-recorder)
  - [新增 **Epsilon GC**, 一種無操作的垃圾收集器, 它處理記憶體分配但不進行回收, 一旦堆內存耗盡, JVM 會關閉](./version-feature/jdk11-features.md#318--epsilon--a-no-op-garbage-collector) *[GC]*
  - [新增允許直接執行 `.java` 檔案而不需先編譯的功能, 例如: `java HelloWorld.java` 直接執行](./version-feature/jdk11-features.md#330--launch-single-file-source-code-programs)
  - [提供低開銷的堆積(stack)剖析方法, 對性能影響小, 適合持續啟用](./version-feature/jdk11-features.md#331--low-overhead-heap-profiling)
- [java 12](./version-feature/jdk12-features.md)
  - [新增基於 Java 微基準測試架構(JMH), JMH 專注於性能測試, 與 Junit 專注於業務邏輯的正確性測試不同](./version-feature/jdk12-features.md#230--microbenchmark-suite)
  - [改進 G1 垃圾收集器, 避免超過預設的暫停時間](./version-feature/jdk12-features.md#344--abortable-mixed-collections-for-g1) *[GC]*
  - [改進 G1 垃圾收集器, 使其能夠在應用程式閒置時, 自動將未使用的 Java 堆記憶體返還給作業系統](./version-feature/jdk12-features.md#346--promptly-return-unused-committed-memory-from-g1) *[GC]*
- [java 13](./version-feature/jdk13-features.md)
  - none
- [java 14](./version-feature/jdk14-features.md)
  - [增加(JFR) 事件流功能, 方便從 jfr 檔案讀取事件處理](./version-feature/jdk14-features.md#349--jfr-event-streaming)
  - [將 `switch` 語句轉變為一種語句和表達式, 引入了新的語法結構](./version-feature/jdk14-features.md#361--switch-expressions--standard-)
  - [改進 `NullPointerException` 的訊息, 能精確地描述哪個變數為 null](./version-feature/jdk14-features.md#358--helpful-nullpointerexceptions)
  - [移除 **CMS** 垃圾回收器](./version-feature/jdk14-features.md#363--remove-the-concurrent-mark-sweep--cms--garbage-collector) *[GC]*
  - [棄用 **Parallel Scavenge** + **Serial Old** 垃圾回收器組合](./version-feature/jdk14-features.md#366--deprecate-the-parallelscavenge--serialold-gc-combination) *[GC]*
- [java 15](./version-feature/jdk15-features.md)
  - [正式推出 **ZGC**, 這是一個可擴展的低延遲垃圾回收器, 適合大規模應用場景](./version-feature/jdk15-features.md#377--zgc--a-scalable-low-latency-garbage-collector) *[GC]*
  - [正式推出 **Shenandoah GC**, 是一個低延遲的垃圾回收器, 旨在最大限度地減少 GC 暫停時間](./version-feature/jdk15-features.md#377--zgc--a-scalable-low-latency-garbage-collector) *[GC]*
  - [引入了 **Text Blocks**, 允許多行字串文字表示法, 減少不必要的跳脫字元並改善程式碼的可讀性](./version-feature/jdk15-features.md#378--text-blocks)
  - [引入了隱藏類(Hidden Classes), 這些類僅能通過反射訪問而無法被其他類直接使用](./version-feature/jdk15-features.md#371--hidden-classes)
- [java 16](./version-feature/jdk16-features.md)
  - [針對 **值導向類別 (value-based classes)** 提供警告, 尤其是對原始類別包裝器(如 Integer、Double 等)進行調整](./version-feature/jdk16-features.md#390--warnings-for-value-based-classes)
  - [提供了 `jpackage` 工具, 用於將 Java 應用程式打包成自包含的可安裝包](./version-feature/jdk16-features.md#392--packaging-tool)
  - [提議為 `instanceof` 操作符引入模式匹配, 簡化類型檢查與轉型的代碼, EX: `if (obj instanceof String str) { System.out.println(str.toUpperCase()) }`](./version-feature/jdk16-features.md#394--pattern-matching-for-instanceof)
  - [引入 `Record` 類別, 用來簡單表示不可變數據的透明載體, 會自動生成如 equals/hashCode/toString 等基本方法](./version-feature/jdk16-features.md#395--records)
  - [改進 **ZGC** 的線程堆疊處理從 safepoint 遷移到並行階段的方案](./version-feature/jdk16-features.md#376--zgc--concurrent-thread-stack-processing) *[GC]*
- [java 17 (LTS)](./version-feature/jdk17-features.md)
  - [新增 `RandomGeneratorFactory`, 可用於取代 `Random`](./version-feature/jdk17-features.md#356--enhanced-pseudo-number-generators)
  - [新增 `sealed` 修飾符來指定類別為 **密封類(Sealed Classes)**, 允許類別或介面限制其可擴展或實作的子類或子介面](./version-feature/jdk17-features.md#409--sealed-classes)
  - [引入了針對 macOS 的新 Java 2D 渲染管道, 替代過時的 OpenGL API](./version-feature/jdk17-features.md#382--new-macos-rendering-pipeline)
  - [引入了上下文特定的反序列化過濾器, 允許根據應用程序的具體情況設置特定的過濾規則, 防止反序列化攻擊](./version-feature/jdk17-features.md#415--context-specific-deserialization-filters)
- [java 18](./version-feature/jdk18-features.md)
  - [新增一個簡單的 Web 伺服器工具, 僅支持基本的 HTTP(僅處理 HEAD 和 GET) 功能, 旨在快速啟動一個用於測試和開發的靜態文件伺服器](./version-feature/jdk18-features.md#408--simple-web-server)
  - [新增 `@snippet` javadoc 標籤, 簡化代碼片段的展示, 改善使用 `<pre>{@code ...}</pre>` 的方式](./version-feature/jdk18-features.md#413--code-snippets-in-java-api-documentation)
  - [將 **UTF-8** 設為預設字元集, 在這之前的版本中是基於作業系統的區域設置(Windows 上是 MS950, Linux 上是 UTF-8)](./version-feature/jdk18-features.md#400--utf-8-by-default)
  - [定義了一個用於網路位址解析的 SPI, 使得 `java.net.InetAddress` 可以使用平台內建解析器以外的其他解析器, 對虛擬線程的應用有幫助](./version-feature/jdk18-features.md#418--internet-address-resolution-spi)
  - [標記 **finalization** 機制即將移除, 建議使用 `try-with-resources` 或 Java 9 引入的 `Cleaner` 來管理資源](./version-feature/jdk18-features.md#421--deprecate-finalization-for-removal)
- [java 19](./version-feature/jdk19-features.md)
  - none
- [java 20](./version-feature/jdk20-features.md)
  - none
- [java 21 (LTS)](./version-feature/jdk21-features.md)
  - [引入了 **有序集合** 的新介面, 以支援具明確順序的集合(`SequencedCollection`, `SequencedSet`, `SequencedMap`)](./version-feature/jdk21-features.md#431--sequenced-collections)
  - [提案了 **Record Patterns** 功能, 使 `record` 結合 `instanceof` 跟 `switch` 操作時有更好的語法體驗](./version-feature/jdk21-features.md#440--record-patterns)
  - [提供 `switch` 表達式與語句的模式匹配功能, 能夠在 case 標籤中使用模式進行匹配, 讓程式更具表達力與安全性](./version-feature/jdk21-features.md#441--pattern-matching-for-switch)
  - [推出 **虛擬執行緒(Virtual Threads)**, 適用於大量 I/O 操作情境](./version-feature/jdk21-features.md#444--virtual-threads)
  - [推出 **Generational ZGC**, 是一種增強型 Z 垃圾收集器, 旨在減少記憶體分配阻塞/降低堆內記憶體使用率和 CPU 開銷](./version-feature/jdk21-features.md#439--generational-zgc) *[GC]*
  - [引入了 **Key Encapsulation Mechanism (KEM)** API, 這是一種加密技術, 為未來的量子抗攻擊標準提供基礎](./version-feature/jdk21-features.md#452--key-encapsulation-mechanism-api)

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

