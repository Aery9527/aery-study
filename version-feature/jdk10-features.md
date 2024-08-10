## [form OpenJDK - JDK10](https://openjdk.org/projects/jdk/10)

here is [[study code](./src/test/java/org/aery/study/jdk10)]

---

### 不知道會唾棄的版本特性

- [286 : Local-Variable Type Inference](https://openjdk.org/jeps/286)
    - 引用 `var` 關鍵字, 用於宣告變數時, 讓編譯器自動推斷型別
    - 只能用在區域變數宣告, 不能用在方法參數, 回傳型別, 全域變數宣告

### 不知道沒差但知道了會變強的版本特性

- [307 : Parallel Full GC for G1](https://openjdk.org/jeps/307)
    - 改進 G1 將 Full GC 並行化, 並通過 -XX:ParallelGCThreads 選項控制線程數
    - G1 在 JDK 9 中成為默認垃圾收集器, 但其現有的單線程標記-清除-壓縮算法影響性能
- [310 : Application Class-Data Sharing](https://openjdk.org/jeps/310)
    - 擴展現有的 Class-Data Sharing (CDS) 功能, 之前 CDS 僅支持 JDK 的基本類別, 現在允許應用程序類別被放置在共享的存檔中
    - 也就是不同 jvm process 之間可以共享 class metadata, 這樣可以減少啟動時間和內存佔用
- [312 : Thread-Local Handshakes](https://openjdk.org/jeps/312)
    - 引入了一種不需全域虛擬機（VM）安全點的回調執行方式, 可以單獨停下個別執行緒
    - 這提升了偏向鎖撤銷/降低服務查詢延遲/以及更安全的堆疊追蹤等功能
    - 最初實作將支援 x64 和 SPARC 平台, 並設有 -XX:ThreadLocalHandshakes 選項來選擇使用傳統安全點, 這能減少全域安全點數量, 從而降低整體 VM 延遲
    - *安全點(safepoint)*: 是指當前執行緒能夠安全地暫停執行的時刻, 以便進行 GC 等操作, 傳統上安全點需要所有執行緒都停下來, 這會導致整體延遲
- [314 : Additional Unicode Language-Tag Extensions](https://openjdk.org/jeps/314)
    - 增強了 `java.util.Locale` 和相關 API 以支持 **BCP 47 語言標籤** 的額外 Unicode 擴展
    - 新增的擴展包括
        - cu (貨幣類型)
        - fw (每週的第一天)
        - rg (地區覆蓋)
        - tz (時區)
    - 這些變更旨在改進國際化和本地化的支持, 提供更靈活和精確的語言設置
    - *BCP 47 語言標籤*: 是一種標準化格式, 用來標識人類語言, 由 IETF 維護, 它的結構包含語言代碼/地區代碼/書寫系統和可選的擴展, 用於表示語言的具體變體,
      例如 en-US 表示美式英語.
- [316 : Heap Allocation on Alternative Memory Devices](https://openjdk.org/jeps/316)
    - 允許在替代記憶裝置（例如 NV-DIMM）上分配 Java 堆記憶體, 這有助於利用便宜的非揮發性記憶體設備,\
      為低優先級進程提供更多 DRAM 記憶體空間或滿足大數據和內存資料庫應用程式對大容量記憶體的需求
    - 透過 `-XX:AllocateHeapAt=<path>` 來設定

### 不知道沒差但知道了也沒差的版本特性

- [296 : Consolidate the JDK Forest into a Single Repository](https://openjdk.org/jeps/296)
    - 將JDK的多個 *Mercurial* 版本庫合併成一個單一版本庫, 以簡化和精簡開發過程
    - 旨在解決跨版本庫進行原子提交的困難, 並減少對新開發者的進入障礙
- [304 : Garbage-Collector Interface](https://openjdk.org/jeps/304)
    - 引入了垃圾收集器的介面, 使其邁向模組化
    - 這項變更主要是代碼重構, 無意添加或移除任何垃圾收集器, 並確保性能不會因重構而退化
- [313 : Remove the Native-Header Generation Tool (javah)](https://openjdk.org/jeps/313)
    - 從 JDK 中移除 `javah` 工具, 功能已被 JDK 8 中的 `javac` 所取代
    - 自 JDK 9 起，每次調用 `javah` 都會生成警告, 提醒該工具的即將移除
- [317 : Experimental Java-Based JIT Compiler](https://openjdk.org/jeps/317)
    - 提出了一個實驗性的 Java 即時編譯器 (JIT Compiler) 名為 Graal, 並計劃在 Linux/x64 平台上使用
    - 此 JIT 編譯器基於 JDK 9 中引入的 JVM 編譯器介面 (JVMCI), 旨在測試和調試 Graal 作為 JIT 編譯器的可行性
    - 此計畫不以超越現有 JIT 編譯器的性能為目標, 而是作為實驗性的功能提供給開發者進行測試和性能基準測試
- [319 : Root Certificates](https://openjdk.org/jeps/319)
    - 提供了在 JDK 中預設的一組根證書, 旨在將 Oracle 的 Java SE Root CA 計劃中的根證書開源, 以吸引更多開發者使用 OpenJDK 並減少其與 Oracle JDK 之間的差異
    - 此變更解決了 OpenJDK 默認 keystore cacerts 為空的問題, 確保像 TLS 這樣的安全組件能夠即時運行, 而無需用戶進行額外配置
- [322 : Time-Based Release Versioning](https://openjdk.org/jeps/322)
    - 提出了基於時間的版本控制模式, 重新定義了 Java SE 平台和 JDK 的版本號方案, 以適應未來基於時間的發行模式
    - 新的版本控制模式還包括對 `Runtime.Version API` 和系統屬性的修改
    - 新版本號格式為 $FEATURE.$INTERIM.$UPDATE.$PATCH
        - $FEATURE : 功能發布計數器, 無論發佈內容為何都會針對每個功能發布而增加(以前 $MAJOR)
        - $INTERIM : 臨時版本計數器, 對於包含相容的錯誤修復和增強功能但沒有不相容的變更、沒有功能刪除以及沒有對標準 API 進行更改的非功能版本遞增(以前$
          MINOR)
        - $UPDATE : 更新版本計數器, 針對修復新功能中的安全性問題、迴歸和錯誤的兼容更新版本而遞增(以前是$SECURITY, 但具有重要的增量規則)
        - $PATCH : 緊急修補程式發布計數器, 僅在需要產生緊急版本來解決關鍵問題時才會增加(
          為此目的使用附加元素可以最大限度地減少對正在進行的更新版本的開發人員和用戶的干擾)
