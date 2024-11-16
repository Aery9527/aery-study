<a id="head"></a>

# [form OpenJDK - JDK11](https://openjdk.org/projects/jdk/11)

#### <a id="head1"></a> [不能不知道的版本特性](#不能不知道的版本特性)

1. [321 : HTTP Client (Standard)](#321--http-client--standard-)
1. [323 : Local-Variable Syntax for Lambda Parameters](#323--Local-Variable-Syntax-for-Lambda-Parameters)
1. [328 : Flight Recorder](#328--flight-recorder)

#### <a id="head2"></a> [不知道沒差但知道了會變厲害的版本特性](#不知道沒差但知道了會變厲害的版本特性)

1. [309 : Dynamic Class-File Constants](#309--dynamic-class-file-constants)
1. [318 : Epsilon: A No-Op Garbage Collector](#318--epsilon--a-no-op-garbage-collector)
1. [320 : Remove the Java EE and CORBA Modules](#320--remove-the-java-ee-and-corba-modules)
1. [327 : Unicode 10](#327--unicode-10)
1. [329 : ChaCha20 and Poly1305 Cryptographic Algorithms](#329--chacha20-and-poly1305-cryptographic-algorithms)
1. [330 : Launch Single-File Source-Code Programs](#330--launch-single-file-source-code-programs)
1. [331 : Low-Overhead Heap Profiling](#331--low-overhead-heap-profiling)

#### <a id="head3"></a> [不知道沒差但知道了也沒差的版本特性](#不知道沒差但知道了也沒差的版本特性)

1. [181 : Nest-Based Access Control](#181--nest-based-access-control)
1. [315 : Improve Aarch64 Intrinsics](#315--improve-aarch64-intrinsics)
1. [324 : Key Agreement with Curve25519 and Curve448](#324--key-agreement-with-curve25519-and-curve448)
1. [332 : Transport Layer Security (TLS) 1.3](#332--transport-layer-security--tls--13)
1. [333 : ZGC: A Scalable Low-Latency Garbage Collector (Experimental)](#333--zgc--a-scalable-low-latency-garbage-collector--experimental-)
1. [335 : Deprecate the Nashorn JavaScript Engine](#335--deprecate-the-nashorn-javascript-engine)
1. [336 : Deprecate the Pack200 Tools and API](#336--deprecate-the-pack200-tools-and-api)

---

#### [不能不知道的版本特性](#head1)

###### [321 : HTTP Client (Standard)](https://openjdk.org/jeps/321)

- [study-code](./src/test/java/org/aery/study/jdk11/JEP321_Http2.java)
- 定義新的http API, 並實作HTTP/2和websocket, 用以取代舊的`HttpURLConnection` API
- 支援HTTP/2, HTTPS/LTS, websocket
- 支援非同步操作
- 有標準和通用的身分驗證機制

###### [323 : Local-Variable Syntax for Lambda Parameters](https://openjdk.org/jeps/323)

- [study-code](./src/test/java/org/aery/study/jdk11/JEP323_Local_Variable_Syntax_for_Lambda_Parameters.java)
- 允許在隱式型別的 lambda 表達式中使用 var 關鍵字來宣告參數
- 這一變更旨在使 lambda 表達式的參數宣告語法與區域變數的宣告語法一致, 提升語法的統一性和簡潔性
- 使用 var 的 lambda 表達式必須對所有參數使用 var, 不能混合使用 var 和其他型別宣告方式

###### [328 : Flight Recorder](https://openjdk.org/jeps/328)

- [study-code](./src/test/java/org/aery/study/jdk11/JEP328_Flight_Recorder.java)
- 一個低負擔的數據收集框架, 用於故障排除、監控和分析 Java 應用程序和 HotSpot JVM
- 它提供 API 來生成和消費事件數據, 並允許配置和過濾事件
- Flight Recorder 記錄應用程序、JVM 和操作系統的事件, 這些事件存儲在單一文件中, 可供後續分析, 旨在在生產環境下提供詳盡的診斷數據
- 使用 Java Mission Control (JMC) 開啟 .jfr 文件進行分析
- 開啟參數:
    - -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr
    - -XX:FlightRecorderOptions=stackdepth=256
- Java Flight Recorder (JFR) 可以分析以下面向的資訊:
    - 應用程序性能 : 方法執行時間, 執行緒活動, 內存分配等
    - JVM 行為 : GC 活動, JIT 編譯事件, 堆內存使用情況等
    - 操作系統事件 : CPU 使用率, 磁碟 I/O, 網路流量等
    - 自定義事件 : 應用程式中特定業務邏輯的事件
- intellij 內建就有 jfr 的分析工具, 可以直接開啟 .jfr 檔案

---

#### [不知道沒差但知道了會變厲害的版本特性](#head2)

###### [309 : Dynamic Class-File Constants](https://openjdk.org/jeps/309)

- [study-code](./src/test/java/org/aery/study/jdk11/JEP309_Dynamic_Class_File_Constants.java)
- 引入了新的常量池形式CONSTANT_Dynamic, 允許在動態解析期間由啟動方法生成常量
- 可減少創建新類型可物化常量的成本, 為語言設計者和編譯器實現者提供更多選擇, 並改進invokedynamic的使用
- 根據 ChatGPT-4o 提供的 sample 來看, 跟一般寫死 `static final` 的 field 沒差別, 但有以下幾點特點
    - 動態生成 : 當常量的值需要在運行時計算或從外部資源獲取時 *(這裡我還是不是很明白, 就算需要計算或外部資源, 那就是寫在 `static block`
      裡面不就好了?)*
    - 減少類初始化成本 : 避免了在類初始化時直接計算複雜常量 *(這個可以理解, 但也可以做成 `lazy initialization` 的方式即可)*
    - 優化invokedynamic : 與 invokedynamic 結合使用時, 動態常量可顯著減少樣板代碼和提升性能 *(不理解)*

###### [318 : Epsilon: A No-Op Garbage Collector](https://openjdk.org/jeps/318)

- 介紹了 **Epsilon GC**, 一種無操作的垃圾收集器, 它處理記憶體分配但不進行回收, 一旦堆內存耗盡, JVM 會關閉
- Epsilon GC 的目標是提供最低延遲的內存分配, 主要用於性能測試、內存壓力測試和短期工作負載, 它適合對垃圾回收極度敏感的應用程序, 允許開發者精確控制應用內存使用量

###### [320 : Remove the Java EE and CORBA Modules](https://openjdk.org/jeps/320)

- 從 Java SE 平台和 JDK 中移除 Java EE 和 CORBA 模組
- 這些模組在 Java SE 9 中已被標記為將來移除, 主要原因是它們與 Java SE 平台的整合變得困難且不再必要
- CORBA : 分布式對象技術規範, 由 *OMG (Object Management Group)* 制定, 也就類似 java 的 RMI, 只是無視語言與平台皆通用. \
  它提供了一種標準的方法來呼叫遠程對象方法, 允許應用程序通過網絡進行通信, 實現了對象之間的透明通信

###### [327 : Unicode 10](https://openjdk.org/jeps/327)

- 將 Java 平台的 API 升級以支持 Unicode 10.0 標準, 這次升級將帶來新增的 16,018 個字符和 10 種新的書寫系統
- 這包括在 `java.lang、java.awt.font` 和 `java.text` 包中的相關類別

###### [329 : ChaCha20 and Poly1305 Cryptographic Algorithms](https://openjdk.org/jeps/329)

- 介紹了 ChaCha20 和 ChaCha20-Poly1305 密碼演算法, 這些演算法旨在替代不安全的 RC4
- 它們會在 SunJCE 提供者中實現, 並且提供相應的 Cipher 和 KeyGenerator 實作
- ChaCha20 是一種流密碼, 而 ChaCha20-Poly1305 是 AEAD 模式的密碼, 結合了 Poly1305 作為驗證器
- 此 JEP 的目標是提升 JDK 的加密工具包，使其符合現代加密標準

###### [330 : Launch Single-File Source-Code Programs](https://openjdk.org/jeps/330)

- 新增允許直接執行 .java 檔案而不需先編譯的功能, 例如: `java HelloWorld.java` 直接執行, 此功能主要是為了簡化教學和小型工具程式的開發。
- 此外, 還支援 Unix 系統的 shebang 機制, 讓腳本檔案可以以 Java 原始碼的形式存在並直接執行

```
#!/usr/bin/env java
 public class HelloWorld {
   public static void main(String[] args) {
       System.out.println("Hello, World!");
   }
 }
```

- Shebang : 是一種用於 Unix 系統的機制, 指的是腳本文件的第一行形如 `#!` 後面跟著解釋器的路徑, 例如 : `#!/usr/bin/env python3` 表示這個腳本應該用
  python3 來執行

###### [331 : Low-Overhead Heap Profiling](https://openjdk.org/jeps/331)

- 提供低開銷的堆積剖析方法, 主要目的是通過 JVMTI 提供一種低成本的方式來採樣 Java 堆積分配, 對性能影響小, 適合持續啟用
- 可以針對對象分配進行採樣, 而非全面的堆積快照, 減少數據量和性能開銷
- 此方法不依賴於特定的垃圾回收算法或 JVM 實現, 並且能夠為開發者提供分配點的調用站點資訊, 有助於調試記憶體問題
- 在 JVM 啟動時透過 java-agent 加載 JVMTI 代理庫 `java -agentpath:/path/to/heapSampler ...`
- 要實現一個基於 JVMTI 的 heapSampler 代理, 用於堆積剖析並進行採樣, 需要使用 C/C++ 編寫代理庫, 並通過 JVMTI 提供的 API 來實現採樣功能

---

#### [不知道沒差但知道了也沒差的版本特性](#head3)

###### [181 : Nest-Based Access Control](https://openjdk.org/jeps/181)

- 這主要是 JVM 和 JDK 的優化, 並不涉及Java語法的改變, 它改進了內部類和封閉類之間的訪問控制機制
- 引入了一個稱為「巢」（nest）的訪問控制概念, 使同一代碼實體的不同類別文件能夠互相訪問私有成員, 而不需編譯器插入橋接方法, 這簡化了編譯器的工作,
  增強了安全性和透明度
- 同時改進了 reflect API 和 MethodHandle 的行為, 更適用於 JVM 的訪問控制規則、核心反射、MethodHandle查詢等

###### [315 : Improve Aarch64 Intrinsics](https://openjdk.org/jeps/315)

- 旨在改善 AArch64 處理器上的內部函數 (intrinsics), 特別是 `java.lang.Math` 的 `sin`、`cos` 和 `log` 函數, 並優化現有的字串和陣列內部函數,
  這些內部函數利用特定 CPU 架構的組合語言來提升性能

###### [324 : Key Agreement with Curve25519 and Curve448](https://openjdk.org/jeps/324)

- 引入了使用 Curve25519 和 Curve448 進行金鑰協商的方法, 這兩種曲線被認為在安全性和效能方面優於傳統的橢圓曲線 Diffie-Hellman (ECDH) 方法
- 此 JEP 提供了一個基於 Java 平台的實現, 強調定時不依賴於秘密數據以防止側信道攻擊
- 這項變更將在 SunEC 提供者中實現, 並且僅適用於該提供者, 不會支持任意域參數

###### [332 : Transport Layer Security (TLS) 1.3](https://openjdk.org/jeps/332)

- 在 OpenJDK 中實作 TLS 1.3, TLS 1.3 提供了顯著的安全性和性能改進, 此計劃主要目標是實作最小可互操作且相容的 TLS 1.3 版本
- TLS 1.3 與之前版本不完全相容，但提供向後相容模式以減少風險

###### [333 : ZGC: A Scalable Low-Latency Garbage Collector (Experimental)](https://openjdk.org/jeps/333)

- 介紹了 ZGC (Z Garbage Collector), 這是一種可擴展的低延遲垃圾回收器, 目標是將 GC 暫停時間控制在 10 毫秒內, 並且能處理從 MB 到 TB 的堆內存大小
- 初期版本僅支持 Linux/x64 平台, 並且不支持類卸載和 JVMCI

###### [335 : Deprecate the Nashorn JavaScript Engine](https://openjdk.org/jeps/335)

- 標記棄用 Nashorn JavaScript 引擎及其相關 API 和 `jjs` 工具, 並計劃在未來版本中移除
- Nashorn 首次在 JDK 8 中引入, 符合 ECMAScript-262 5.1 標準, 但由於 ECMAScript 語言結構和 API 的快速變化, 維護變得困難
- 這次棄用不會影響 `javax.script` API

###### [336 : Deprecate the Pack200 Tools and API](https://openjdk.org/jeps/336)

- 標記棄用 *pack200* 和 *unpack200* 工具及 `java.util.jar.Pack200`, 開發者應考慮使用 `jlink` 或 `jpackage` 工具來代替
- 這些是用來壓縮 JAR 檔案的技術, 但由於下載速度的提升和新壓縮技術的引入, 且其技術複雜且維護成本高, 因此重要性已經減少
