## [form OpenJDK - JDK9](https://openjdk.org/projects/jdk9/)

[[study code](./src/test/java/org/aery/study/jdk9)] and [[JPMS](./jdk9-features-JPMS.md)]

- [102 : Process API Updates](https://openjdk.org/jeps/102)
    - 增強`Process`的支援, 主要是透過`ProcessHandle`來操作新的功能, 而`Process`身上新增的一些方法, 主要也都是從`ProcessHandle`來的
    - `ProcessHandle`可透過`Process.toHandler()`取得
- [110 : HTTP 2 Client](https://openjdk.org/jeps/110)
    - 增加原生的HTTP/2 client API, 在 jdk11 正式推出
- [143 : Improve Contended Locking](https://openjdk.org/jeps/143)
    - 改善鎖的性能, 透過一系列測試基準確保效能的提升, 且無競爭的鎖不能有效能下降的狀況
- [158 : Unified JVM Logging](https://openjdk.org/jeps/158)
    - 為JVM所有元件引入一個通用的log系統
    - 可以方便觀測class loading, thread, GC, Module System, 等相關JVM原生的基礎資訊
    - 使用 `-Xlog` JVM參數來設置, 可以控制不同組件, 不同等級
    - 透過 JMX的MBean 或 `jcmd {PID} VM.log` 可以動態修改log設置
    - 例如jdk9之前要打開GC的log會使用 `-XX:+PrintGCDetails` `-XX:+UseGCLogFileRotation` `-XX:NumberOfGCLogFiles=5` 等參數,
      而jdk開始可以改用 `-Xlog:gc*=debug:file=/tmp/gc.log:uptime,tid:filecount=5,filesize=2m` 來設置
- [165 : Compiler Control](https://openjdk.org/jeps/165)
    - 提供一種可精細控制編譯器行為的方法, 因為有時候某些優化策略會意外導致性能下降, 因此透過此功能可以調整優化策略
    - 可以透過jvm參數, 環境變數, 或annotation進行設置, 從而為開發人員提供的更靈活的控制能力
    - 例如開發人員可以透過這些選向來調整方法內聯、循環展開、數組邊界檢查等優化行為, 還可以設置不同優化級別來平衡性能和代碼大小之間的權衡
- [193 : Variable Handles](https://openjdk.org/jeps/193)
    - 之前對於field的原子或有序操作都有部分問題存在, 在jdk9之前只有以下幾種方法:
        1. `Atomic*` : 這種方式導致額外的記憶體開銷且會有額外的併發問題 (AtomicInteger, AtomicLong, ...)
        2. `Atomic*FieldUpdater` : 通常會遇到操作本身更大的開銷 (AtomicIntegerFieldUpdater, AtomicLongFieldUpdater, ...)
        3. `Unsafe` : 這方式雖然快速但不安全且不易使用也有移植性問題 (可直接跟OS申請記憶體, 而繞過jvm記憶體管理模型)
    - 所以 `VarHandle` 基本上就是等效 `java.util.concurrent.atomic` 、 `sun.misc.Unsafe` 的功能, 提供更安全便捷的操作
    - `VarHandle` 與 reflection 機制不同在於, 前者只有創建時會做檢查, 而後者每次操作都會做檢查
- [197 : Segmented Code Cache](https://openjdk.org/jeps/197)
    - 通過引入分段代碼緩存結構, 顯著提升了Java虛擬機的性能和可維護性, 這一改進使得代碼緩存更加高效, 內存利用率更高, 且便於管理和診斷性能問題.
- [199 : Smart Java Compilation, Phase Two](https://openjdk.org/jeps/199)
    - 通過引入增量編譯和並行編譯技術, 顯著提升了Java編譯器的性能和效率.
    - 這一增強建議使得Java編譯過程更加高效, 特別是對於大型項目, 可以大幅減少編譯時間, 提升開發生產力.
- [200 : The Modular JDK](https://openjdk.org/jeps/200)
    - 透過 JEP201 將jdk模組化
- [201 : Modular Source Code](https://openjdk.org/jeps/201)
    - see [jdk9-features-JPMS.md](./jdk9-features-JPMS.md)
- [211 : Elide Deprecation Warnings on Import Statements](https://openjdk.org/jeps/211)
    - 降低 `@Deprecated` 的警告訊息, 僅在導入該 package 或 調用該 method 時才會出現警告, 減少干擾
- [212 : Resolve Lint and Doclint Warnings](https://openjdk.org/jeps/212)
    - 旨在解決 Lint 和 Doclint 的警告, 意味著在coding時會看到更少的警告信息. 這不僅減少大量警告信息帶來的的困惑, 更能幫助開發者更快地找到問題所在.
    - *Lint* : 用於分析源碼以找出潛在的錯誤、違反編碼規範的地方以及低效的代碼結構. 這名稱來源於一個早期的 UNIX 中用於檢查 C 語言代碼中問題的程序, 如今
      Lint 已廣泛應用於各種編程語言.
    - *Doclint* : 專門用於檢查 Javadoc 註釋的工具, 這個工具可以檢查 Javadoc 註釋是否符合規範, 並且可以檢查 Javadoc 註釋中的拼寫錯誤等問題.
- [213 : Milling Project Coin](https://openjdk.org/jeps/213)
    - 旨在對之前的 Project Coin 中的一些功能進行微調, 主要是一些語法糖
    - `@SafeVarargs` 用於指出帶有可變參數 (varargs) 方法或構造函數是安全的(不會有編譯警告), 不會引發潛在的 heap pollution 警告 (heap pollution
      是指在編譯期使用泛型導致運行時類型不匹配的問題)
    - `@SafeVarargs` 從 java7 中引入, 並且只能用於static/final/constructor method (因為這些方法不會被override, 可以確保不會改變)
    - `@SafeVarargs` 從 java9 可以用於 private method 上了
    - 禁止使用 `_` 一個底線當作變數名稱, 主要是預留 `_` 在未來版本中的特定用途
    - java7引入的 `try-with-resources` 語句不再限制只能在try()裡面實例化 `Closeable` 物件
    - interface 可以寫 private method
    - 泛型的型態推斷支援匿名類
    - 新增 `java.util.concurrent.Flow` 響應式流介面 (Reactive Streams API), 標準化生產者和消費者之間的異步流框架
        1. 這部分如同 jpa 僅是一個規範定義, 而目前主流的實作有 **Project Reactor**<sub>(by Spring)</sub>, **RxJava**<sub>(by ReactiveX)</sub>, **Akka
           Streams**<sub>(by Akka)</sub>
        1. 支援 **背壓 (back-pressure)** 機制, 也就是允許 **消費者** 控制 **生產者** 數據發送速率, 避免 **消費者** 消化不來
        1. `Flow` 僅作為命名空間, 定義了 `Publisher`, `Subscriber`, `Subscription`, `Processor`<sub>(Publisher + Subscriber)</sub> 四個介面
        1. `SubmissionPublisher`<sub>(Publisher)</sub> 是 jdk 內唯一的實作, 也是一個簡單的實作, 使用 `ForkJoinPool` 作為預設的非同步機制.
        1. `SubmissionPublisher` 基本上是作為基礎類別拿來繼承擴充使用, 當然也可以在簡單的場合直接拿來使用.
- [214 : Remove GC Combinations Deprecated in JDK 8](https://openjdk.org/jeps/214)
    - 移除在jdk8中被標記為deprecated的GC組合
    - 若啟用了相關的GC組合, jvm 將不會啟動
- [215 : Tiered Attribution for javac](https://openjdk.org/jeps/215)
    - 旨在改進 Java 編譑器（javac）中多態表達式（poly expressions）的型別檢查策略.
    - 引入了分層屬性（Tiered Attribution, TA）的概念, 旨在通過減少屬性化（attribution）過程中不必要的多次檢查, 從而提升性能.
    - 對於 lambda 表達式、條件多態表達式、泛型方法調用等均有應用.
    - 此外還改善型別推斷的機制, 特別是涉及泛型方法調用的重載檢查.
- [216 : Process Import Statements Correctly](https://openjdk.org/jeps/216)
    - 對 javac 編譯器進行修正, 以正確處理 import 語句的順序, 確保不會因為 import 語句的不同排列順序而導致程序被錯誤接受或拒絕.
- [217 : Annotations Pipeline 2.0](https://openjdk.org/jeps/217)
    - 提出了一個重構 javac 註解處理管線的計劃，目的是更好地處理各種註解和工具
    - 現在可以這樣寫 Function<String, String> fss = (@Anno @Anno String s) -> s;
- [219 : Datagram Transport Layer Security (DTLS)](https://openjdk.org/jeps/219)
    - 引入了 Datagram Transport Layer Security (DTLS) 到 Java 平台, 支持 1.0 和 1.2 版本
    - DTLS 允許在不可靠的數據報協議（如 UDP）上進行安全通信，這對於像 *會話啟動協定(SIP)* 和 *電子遊戲協定* 這樣的應用程序非常重要
- [220 : Modular Run-Time Images](https://openjdk.org/jeps/220)
    - 重新結構了 JDK 和 JRE 的運行時映像以支持模組化, 並提高性能、安全性和可維護性
- [221 : Simplified Doclet API](https://openjdk.org/jeps/221)
    - 提供了新的 Doclet API, 用於取代舊的 API
    - 旨在提高 javadoc 工具和新標準 Doclet 的性能, 同時提供更高效和準確的文檔注釋分析.
    - *Doclet API* : 用於創建自定義的文檔生成器. 它允許開發者擴展和自定義 javadoc 工具的輸出格式. 通過 Doclet API, 可以訪問和處理 Java 源碼中的注釋,
      從而生成特定格式的文檔, 例如 HTML、PDF 或其他格式.
- [222 : jshell: The Java Shell (Read-Eval-Print Loop)](https://openjdk.org/jeps/222)
    - 這是一個交互式的 Java shell 工具, 用於快速編寫和測試 Java 代碼, 並且可以在不需要編譯和打包的情況下直接運行 Java 代碼
    - 學習程式語言及其 API 時, 即時回饋非常重要, 學校放棄將 Java 作為教學語言的首要原因是其他語言有 *REPL* (讀取-評估-列印-循環)
- [223 : New Version-String Scheme](https://openjdk.org/jeps/223)
    - jdk 定義了新的 *版本號* 規則, 來解決究版本號難以理解和比較的問題
    - 新的版本號規則為 **{major}.{minor}.{security}[OTHER]**, 例如: 9.1.2
    - major : 重大新功能
    - minor : 兼容的改進或修復
    - security : 安全性修復
- [224 : HTML5 Javadoc](https://openjdk.org/jeps/224)
    - 支援將 javadoc 生成為 HTML5 格式
- [225 : Javadoc Search](https://openjdk.org/jeps/225)
    - 為 Javadoc 工具新增了搜尋功能, 這個功能在由標準 doclet 生成的 API 文檔頁面中加入了一個搜尋框
    - 搜尋功能完全在客戶端實現, 不依賴伺服器資源, 這使得導航和查找特定 API 更加方便和快捷
- [226 : UTF-8 Property Files](https://openjdk.org/jeps/226)
    - 允許應用程式指定以 UTF-8 編碼的屬性文件, 並擴展 ResourceBundle API 以載入這些文件 (ResourceBundle:用於國際化（i18n）應用程式的屬性管理)
    - 這一變更將 ResourceBundle 類的默認文件編碼從 ISO-8859-1 變為 UTF-8, 若在讀取 UTF-8 文件時發生異常, 系統將嘗試重新以 ISO-8859-1 編碼讀取文件
- [227 : Unicode 7.0](https://openjdk.org/jeps/227)
    - 支援 Unicode 7.0 標準, 新增了約三千個字符和二十多種書寫系統
    - 變動 `java.lang` 中的 `Character`, `String`
    - 變動 `java.text` 中的 `Bidi`, `BreakIterator`, `Normalizer`
      1. `Bidi` : 用於處理文字的雙向性, 例如阿拉伯文, 希伯來文等
      2. `BreakIterator` : 用於文本邊界分析, 確定單詞/句子/段落的邊界, 支持多語言, 對國際化文本處理尤為重要
      3. `Normalizer` : 將不同的 Unicode 字符表示形式轉換為標準形式, 支持四種正規化形式，確保文本的一致性和可比性
- [228 : Add More Diagnostic Commands](https://openjdk.org/jeps/228)
- [229 : Create PKCS12 Keystores by Default](https://openjdk.org/jeps/229)
- [231 : Remove Launch-Time JRE Version Selection](https://openjdk.org/jeps/231)
- [232 : Improve Secure Application Performance](https://openjdk.org/jeps/232)
- [233 : Generate Run-Time Compiler Tests Automatically](https://openjdk.org/jeps/233)
- [235 : Test Class-File Attributes Generated by javac](https://openjdk.org/jeps/235)
- [236 : Parser API for Nashorn](https://openjdk.org/jeps/236)
- [237 : Linux/AArch64 Port](https://openjdk.org/jeps/237)
- [238 : Multi-Release JAR Files](https://openjdk.org/jeps/238)
- [240 : Remove the JVM TI hprof Agent](https://openjdk.org/jeps/240)
- [241 : Remove the jhat Tool](https://openjdk.org/jeps/241)
- [243 : Java-Level JVM Compiler Interface](https://openjdk.org/jeps/243)
- [244 : TLS Application-Layer Protocol Negotiation Extension](https://openjdk.org/jeps/244)
- [245 : Validate JVM Command-Line Flag Arguments](https://openjdk.org/jeps/245)
- [246 : Leverage CPU Instructions for GHASH and RSA](https://openjdk.org/jeps/246)
- [247 : Compile for Older Platform Versions](https://openjdk.org/jeps/247)
- [248 : Make G1 the Default Garbage Collector](https://openjdk.org/jeps/248)
- [249 : OCSP Stapling for TLS](https://openjdk.org/jeps/249)
- [250 : Store Interned Strings in CDS Archives](https://openjdk.org/jeps/250)
- [251 : Multi-Resolution Images](https://openjdk.org/jeps/251)
- [252 : Use CLDR Locale Data by Default](https://openjdk.org/jeps/252)
- [253 : Prepare JavaFX UI Controls &amp; CSS APIs for Modularization](https://openjdk.org/jeps/253)
- [254 : Compact Strings](https://openjdk.org/jeps/254)
- [255 : Merge Selected Xerces 2.11.0 Updates into JAXP](https://openjdk.org/jeps/255)
- [256 : BeanInfo Annotations](https://openjdk.org/jeps/256)
- [257 : Update JavaFX/Media to Newer Version of GStreamer](https://openjdk.org/jeps/257)
- [258 : HarfBuzz Font-Layout Engine](https://openjdk.org/jeps/258)
- [259 : Stack-Walking API](https://openjdk.org/jeps/259)
- [260 : Encapsulate Most Internal APIs](https://openjdk.org/jeps/260)
- [261 : Module System](https://openjdk.org/jeps/261)
- [262 : TIFF Image I/O](https://openjdk.org/jeps/262)
- [263 : HiDPI Graphics on Windows and Linux](https://openjdk.org/jeps/263)
- [264 : Platform Logging API and Service](https://openjdk.org/jeps/264)
- [265 : Marlin Graphics Renderer](https://openjdk.org/jeps/265)
- [266 : More Concurrency Updates](https://openjdk.org/jeps/266)
- [267 : Unicode 8.0](https://openjdk.org/jeps/267)
- [268 : XML Catalogs](https://openjdk.org/jeps/268)
- [269 : Convenience Factory Methods for Collections](https://openjdk.org/jeps/269)
- [270 : Reserved Stack Areas for Critical Sections](https://openjdk.org/jeps/270)
- [271 : Unified GC Logging](https://openjdk.org/jeps/271)
- [272 : Platform-Specific Desktop Features](https://openjdk.org/jeps/272)
- [273 : DRBG-Based SecureRandom Implementations](https://openjdk.org/jeps/273)
- [274 : Enhanced Method Handles](https://openjdk.org/jeps/274)
- [275 : Modular Java Application Packaging](https://openjdk.org/jeps/275)
- [276 : Dynamic Linking of Language-Defined Object Models](https://openjdk.org/jeps/276)
- [277 : Enhanced Deprecation](https://openjdk.org/jeps/277)
- [278 : Additional Tests for Humongous Objects in G1](https://openjdk.org/jeps/278)
- [279 : Improve Test-Failure Troubleshooting](https://openjdk.org/jeps/279)
- [280 : Indify String Concatenation](https://openjdk.org/jeps/280)
- [281 : HotSpot C++ Unit-Test Framework](https://openjdk.org/jeps/281)
- [282 : jlink: The Java Linker](https://openjdk.org/jeps/282)
- [283 : Enable GTK 3 on Linux](https://openjdk.org/jeps/283)
- [284 : New HotSpot Build System](https://openjdk.org/jeps/284)
- [285 : Spin-Wait Hints](https://openjdk.org/jeps/285)
- [287 : SHA-3 Hash Algorithms](https://openjdk.org/jeps/287)
- [288 : Disable SHA-1 Certificates](https://openjdk.org/jeps/288)
- [289 : Deprecate the Applet API](https://openjdk.org/jeps/289)
- [290 : Filter Incoming Serialization Data](https://openjdk.org/jeps/290)
- [291 : Deprecate the Concurrent Mark Sweep (CMS) Garbage Collector](https://openjdk.org/jeps/291)
- [292 : Implement Selected ECMAScript 6 Features in Nashorn](https://openjdk.org/jeps/292)
- [294 : Linux/s390x Port](https://openjdk.org/jeps/294)
- [295 : Ahead-of-Time Compilation](https://openjdk.org/jeps/295)
- [297 : Unified arm32/arm64 Port](https://openjdk.org/jeps/297)
- [298 : Remove Demos and Samples](https://openjdk.org/jeps/298)
- [299 : Reorganize Documentation](https://openjdk.org/jeps/299)
