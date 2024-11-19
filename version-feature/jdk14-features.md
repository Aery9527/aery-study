<a id="head"></a>

# [form OpenJDK - JDK14](https://openjdk.org/projects/jdk/14)

#### <a id="head1"></a> [不能不知道的版本特性](#不能不知道的版本特性)

1. 349 : JFR Event Streaming [[intellij]](#349--jfr-event-streaming) [[github]](#349--jfr-event-streaming)
1. 361 : Switch Expressions (Standard) [[intellij]](#361--switch-expressions--standard-) [[github]](#361--switch-expressions-standard)
 
#### <a id="head2"></a> [不知道沒差但知道了會變厲害的版本特性](#不知道沒差但知道了會變厲害的版本特性)

1. 305 : Pattern Matching for instanceof (Preview) [[intellij]](#305--pattern-matching-for-instanceof--preview-) [[github]](#305--pattern-matching-for-instanceof-preview)
1. 343 : Packaging Tool (Incubator) [[intellij]](#343--packaging-tool--incubator-) [[github]](#343--packaging-tool-incubator)
1. 352 : Non-Volatile Mapped Byte Buffers [[intellij]](#352--non-volatile-mapped-byte-buffers) [[github]](#352--non-volatile-mapped-byte-buffers)
1. 358 : Helpful NullPointerExceptions [[intellij]](#358--helpful-nullpointerexceptions) [[github]](#358--helpful-nullpointerexceptions)
1. 359 : Records (Preview) [[intellij]](#359--records--preview-) [[github]](#359--records-preview)
1. 363 : Remove the Concurrent Mark Sweep (CMS) Garbage Collector [[intellij]](#363--remove-the-concurrent-mark-sweep--cms--garbage-collector) [[github]](#363--remove-the-concurrent-mark-sweep-cms-garbage-collector)
1. 364 : ZGC on macOS (Experimental) [[intellij]](#364--zgc-on-macos--Experimental-) [[github]](#364--zgc-on-macos-experimental)
1. 365 : ZGC on Windows (Experimental) [[intellij]](#365--zgc-on-windows--Experimental-) [[github]](#365--zgc-on-windows-experimental)
1. 366 : Deprecate the ParallelScavenge + SerialOld GC Combination [[intellij]](#366--deprecate-the-parallelscavenge--serialold-gc-combination) [[github]](#366--deprecate-the-parallelscavenge-serialold-gc-combination)
1. 368 : Text Blocks (Second Preview) [[intellij]](#368--text-blocks--second-preview-) [[github]](#368--text-blocks-second-preview)
1. 370 : Foreign-Memory Access API (Incubator) [[intellij]](#370--foreign-memory-access-api--incubator-) [[github]](#370--foreign-memory-access-api-incubator)

#### <a id="head3"></a> [不知道沒差但知道了也沒差的版本特性](#不知道沒差但知道了也沒差的版本特性)

1. 345 : NUMA-Aware Memory Allocation for G1 [[intellij]](#345--numa-aware-memory-allocation-for-g1) [[github]](#345--numa-aware-memory-allocation-for-g1)
1. 362 : Deprecate the Solaris and SPARC Ports [[intellij]](#362--deprecate-the-solaris-and-sparc-ports) [[github]](#362--deprecate-the-solaris-and-sparc-ports)
1. 367 : Remove the Pack200 Tools and API [[intellij]](#367--remove-the-pack200-tools-and-api) [[github]](#367--remove-the-pack200-tools-and-api)

---

#### [不能不知道的版本特性](#head1)

###### [349 : JFR Event Streaming](https://openjdk.org/jeps/349)

- [study-code](./src/test/java/org/aery/study/jdk14/JEP349_JFR_Event_Streaming.java)
- 提出 Java Flight Recorder (JFR) 事件流功能, 方便從 jfr 檔案讀取事件處理
- jfr 在 jdk11 的 jep328 有提到, 但是只能用 Java Mission Control (JMC) 來開啟 .jfr 檔案

###### [361 : Switch Expressions (Standard)](https://openjdk.org/jeps/361)

- [study-code](./src/test/java/org/aery/study/jdk14/JEP361_Switch_Expressions.java)
- 將 switch 語句轉變為一種語句和表達式, 引入了新的語法結構, 例如使用箭頭 (->) 符號來替代傳統的 case 和 break 語句, 從而減少樣板代碼和潛在的錯誤

---

#### [不知道沒差但知道了會變厲害的版本特性](#head2)

###### [305 : Pattern Matching for instanceof (Preview)](https://openjdk.org/jeps/305)

- 提出了 Java 語言中對 instanceof 操作符的模式匹配功能, 允許在檢查對象類型的同時直接將變量綁, 進而簡化代碼
```
  if (obj instanceof String str) {
    System.out.println(str.toUpperCase()); // 直接使用 str 變量，無需顯式轉型
  }
```

###### [343 : Packaging Tool (Incubator)](https://openjdk.org/jeps/343)

- 介紹了一個名為 jpackage 的工具, 旨在為Java應用程式創建自包含的本機安裝包
- 它支持不同操作系統的原生包格式，如 Windows 的 msi 和 exe, macOS 的 pkg 和 dmg, 以及 Linux 的 deb 和 rpm
- 該工具基於 JavaFX 的 javapackager, 提供簡單的命令行界面, 但不支持 Java Web Start 和 JavaFX 相關功能

###### [352 : Non-Volatile Mapped Byte Buffers](https://openjdk.org/jeps/352)

- 提出讓 `FileChannel` API 能夠創建指向非揮發性記憶體 (NVM) 的 MappedByteBuffer 實例, 主要目的是讓 Java 程序有效且一致地存取和更新 NVM
- 此外還增加了新的寫回記憶體方法, 並通過新模塊 `jdk.nio.mapmode` 引入新的 `MapMode` 枚舉值 (這功能限於支援 mmap 系統調用 MAP_SYNC 標誌的 Linux/x64
  和 Linux/AArch64 平台)

###### [358 : Helpful NullPointerExceptions](https://openjdk.org/jeps/358)

- 改進 `NullPointerException` 的訊息, 能精確地描述哪個變數為 null, 該功能可以通過命令行選項啟用或禁用

###### [359 : Records (Preview)](https://openjdk.org/jeps/359)

- 引入記錄類型 **Records** 這是一種特殊的class, 旨在簡化數據容器類別的建立過程
- 該class通過自動生成不可變性: `equals()` / `hashCode()` / `toString()` 等方法減少樣板代碼, 讓開發者能更專注於業務邏輯

###### [363 : Remove the Concurrent Mark Sweep (CMS) Garbage Collector](https://openjdk.org/jeps/363)

- 移除 Concurrent Mark Sweep (CMS) 垃圾回收器, 這是因為 CMS 缺乏維護者, 以及新的垃圾回收器如 ZGC 和 Shenandoah 的引入和 G1 的改進,
  這些替代方案已能夠提供相似或更好的性能

###### [364 : ZGC on macOS (Experimental)](https://openjdk.org/jeps/364)

- 引入了在 macOS 上實驗性地支持 ZGC 垃圾回收器
- 這一功能允許開發者在 macOS 平台上使用 ZGC 進行本地開發和測試, 並支持不連續的內存預留以克服 macOS 的地址空間隨機化限制

###### [365 : ZGC on Windows (Experimental)](https://openjdk.org/jeps/365)

- 在 Windows 平台上實驗性支持 ZGC 垃圾回收器, 這個改進允許在 Windows 上使用 ZGC 進行低延遲垃圾回收
- 但只適用於 Windows 10 和 Windows Server 1803 及以上版本, 這是因為需要使用新的內存管理 API 來支持 ZGC 的多映射內存和地址空間佔位符功能

###### [366 : Deprecate the ParallelScavenge + SerialOld GC Combination](https://openjdk.org/jeps/366)

- 棄用 **Parallel Scavenge** + **Serial Old** 垃圾回收器組合, 這種組合需要用戶使用特定選項啟用, 並且在實際中很少使用
- 此變更主要是因為維護成本高且用處有限, 建議用戶轉用其他垃圾回收器組合如 Parallel GC
- 此決策不會移除功能，只是會顯示棄用警告

###### [368 : Text Blocks (Second Preview)](https://openjdk.org/jeps/368)

- 重新實驗性地引入 **Text Blocks** 功能, 這項功能允許使用多行字符串來簡化書寫和閱讀, 尤其對於包含大量文本的代碼塊（如 JSON 或 SQL）非常有用
- **Text Blocks** 使用三重雙引號作為邊界, 並提供了一個更便捷的方式來處理多行字符串數據
```
String json = """
          {
              "name": "Alice",
              "age": 30,
              "city": "New York"
          }
          """;
```

###### [370 : Foreign-Memory Access API (Incubator)](https://openjdk.org/jeps/370)

- 提供了一種新的 API, 稱為外部記憶體訪問 API, 它允許 Java 程序高效且安全地訪問 Java 堆外的記憶體
- 這個 API 可以用來操作原生記憶體數據, 有助於提高性能, 尤其是對於需要與非 Java 計算進行高效數據交換的應用程序
- 與目前常見的堆外記憶體操作比較:

  | Unsafe                                    | Foreign-Memory Access API                 | ByteBuffer                      |
    |-------------------------------------------|-------------------------------------------|---------------------------------|
  | 提供直接訪問堆外記憶體和操作內存的能力                       | 提供受控、安全的堆外記憶體訪問                           | 提供對緩衝區的高效內存管理                   |
  | 沒有邊界檢查，容易導致錯誤和安全問題                        | 支援邊界檢查和自動資源管理，減少內存洩漏和錯誤的風險                | 支持直接緩衝區（堆外記憶體）和非直接緩衝區（堆內記憶體）    |
  | 適用於需要高性能且願意承擔風險的場景                        | 適用於需要高效、安全操作堆外記憶體的情況                      | 適用於需要讀寫內存數據但不需要操作底層內存的情況        |
  | 多用於內部性能優化和需要非常低級別內存操作的場景，但不推薦在公共 API 中使用  | 用於需要精細控制和安全操作堆外記憶體的應用，例如高性能網絡應用或數據庫引擎 | 適用於常規 I/O 操作和數據流處理，不需要直接操作內存地址 |

- 總結來說, 這次新的 API 提供了一個更安全和受控的方式來操作堆外記憶體, 相比之下 `Unsafe` 更靈活但危險, 而 `ByteBuffer` 則是較高層次的緩衝區操作工具

---

#### [不知道沒差但知道了也沒差的版本特性](#head3)

###### [345 : NUMA-Aware Memory Allocation for G1](https://openjdk.org/jeps/345)

- 提出了在 G1 垃圾收集器中實現 NUMA 感知的記憶體分配, 以改善大規模機器上的性能
- NUMA(非均勻記憶體存取) : 技術考慮了多插槽機器中不同插槽之間的記憶體訪問延遲, 此功能僅適用於 Linux 系統

###### [362 : Deprecate the Solaris and SPARC Ports](https://openjdk.org/jeps/362)

- 建議將 *Solaris* 和 *SPARC* 平台從 JDK 中廢除, 因為這些平台的使用率逐漸下降, 維護成本不再合理
- 移除對這些平台的支持, JDK 將能夠專注於更廣泛使用的操作系統和硬體平台, 以提升開發效率

###### [367 : Remove the Pack200 Tools and API](https://openjdk.org/jeps/367)

- 提議移除 *Pack200* 工具和 API, 包括 `pack200`、`unpack200` 和 `java.util.jar.Pack200`
- *Pack200* 最初用於減少 JAR 文件大小, 但隨著網絡速度的提升和其他更有效的壓縮方法的出現, *Pack200* 的使用已顯著減少
- 建議開發者應採用其他替代方案，如 `jlink` 或 `jpackage`
