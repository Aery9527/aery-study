<a id="head"></a>

# [form OpenJDK - JDK21](https://openjdk.org/projects/jdk/21)

#### <a id="head1"></a> [不能不知道的版本特性](#不能不知道的版本特性)

1. 431 : Sequenced Collections [[intellij]](#431--sequenced-collections) [[github]](#431--sequenced-collections)
1. 440 : Record Patterns [[intellij]](#440--record-patterns) [[github]](#440--record-patterns)
1. 441 : Pattern Matching for switch [[intellij]](#441--pattern-matching-for-switch) [[github]](#441--pattern-matching-for-switch)
1. 444 : Virtual Threads [[intellij]](#444--virtual-threads) [[github]](#444--virtual-threads)

#### <a id="head2"></a> [不知道沒差但知道了會變厲害的版本特性](#不知道沒差但知道了會變厲害的版本特性)

1. 430 : String Templates (Preview) [[intellij]](#430--string-templates--preview-) [[github]](#430--string-templates-preview)
1. 439 : Generational ZGC [[intellij]](#439--generational-zgc) [[github]](#439--generational-zgc)
1. 442 : Foreign Function & Memory API (Third Preview) [[intellij]](#442--foreign-function--memory-api--third-preview-) [[github]](#442--foreign-function--memory-api-third-preview)
1. 443 : Unnamed Patterns and Variables (Preview) [[intellij]](#443--unnamed-patterns-and-variables--preview-) [[github]](#443--unnamed-patterns-and-variables-preview)
1. 445 : Unnamed Classes and Instance Main Methods (Preview) [[intellij]](#445--unnamed-classes-and-instance-main-methods--preview-) [[github]](#445--unnamed-classes-and-instance-main-methods-preview)
1. 446 : Scoped Values (Preview) [[intellij]](#446--scoped-values--preview-) [[github]](#446--scoped-values-preview)
1. 448 : Vector API (Sixth Incubator) [[intellij]](#448--vector-api--sixth-incubator-) [[github]](#448--vector-api-sixth-incubator)
1. 451 : Prepare to Disallow the Dynamic Loading of Agents [[intellij]](#451--prepare-to-disallow-the-dynamic-loading-of-agents) [[github]](#451--prepare-to-disallow-the-dynamic-loading-of-agents)
1. 452 : Key Encapsulation Mechanism API [[intellij]](#452--key-encapsulation-mechanism-api) [[github]](#452--key-encapsulation-mechanism-api)
1. 453 : Structured Concurrency (Preview) [[intellij]](#453--structured-concurrency--preview-) [[github]](#453--structured-concurrency-preview)

#### <a id="head3"></a> [不知道沒差但知道了也沒差的版本特性](#不知道沒差但知道了也沒差的版本特性)

1. 449 : Deprecate the Applet API for Removal [[intellij]](#449--deprecate-the-windows-32-bit-x86-port-for-removal) [[github]](#449--deprecate-the-windows-32-bit-x86-port-for-removal)

---

#### [不能不知道的版本特性](#head1)

###### [431 : Sequenced Collections](https://openjdk.org/jeps/431)

- [study-code](./src/test/java/org/aery/study/jdk21/JEP431_Sequenced_Collections.java)
- 引入了 **有序集合** 的新介面, 以支援具明確順序的集合, 讓集合中的元素有明確的第一個和最後一個, 並能提供統一的 API 來訪問首尾元素及反向處理元素
- 這些新介面包括 **有序集合**/**有序集**/**有序映射**, 並將它們整合到現有的集合階層中, 解決目前集合框架中遇到的順序處理問題 ,提升一致性和操作方便性

###### [440 : Record Patterns](https://openjdk.org/jeps/440)

- [study-code](./src/test/java/org/aery/study/jdk21/JEP440_Record_Patterns.java)
- 提案了 **Record Patterns** 功能, 旨在增強 Java 語言的模式匹配, 允許開發者分解 `Record` 類型的數據, 它支持嵌套模式, 讓資料導航和處理更加宣告式與可組合
- 主要改進包括允許直接解構記錄類中的欄位資料，並透過嵌套模式處理更複雜的物件結構，進一步簡化了數據處理過程

###### [441 : Pattern Matching for switch](https://openjdk.org/jeps/441)

- [study-code](./src/test/java/org/aery/study/jdk21/JEP441_Pattern_Matching_for_switch.java)
- 提供 `switch` 表達式與語句的模式匹配功能, 這使得開發者能夠在 `switch` 的 case 標籤中使用模式進行匹配, 讓程式更具表達力與安全性
- 該功能能夠簡化針對多種模式的處理, 並確保所有輸入值都被覆蓋. 同時, 這個功能還支持 *null* 處理, 並允許在 case 中使用條件判斷 `when` 子句, 這項改進顯著提升了程式的可讀性與性能

###### [444 : Virtual Threads](https://openjdk.org/jeps/444)

- [study-code](./src/test/java/org/aery/study/jdk21/JEP444_Virtual_Threads.java)
- **虛擬執行緒** 也稱為 **用戶模式線程**(user-mode threads) 或 **纖程**(fibers), 指由應用程式自行管理的執行緒, 在 java 世界就是由 JVM 管理,
  概念就是將多個 **虛擬執行緒(後稱VT)** 對應到少數真實的 **平台執行緒(後稱PT)**
- 在實務上, 系統效能瓶頸通常在執行緒數量, 而非 CPU/網路連線等資源, 意味著系統的吞吐量被 PT 所限制, 因此 VT 的出現就是想要解決這個問題
- VT 工作原理是將其 *掛載* 到 PT 上執行, 當 VT 需要進行 blocking (I/O等待) 時, JVM 會將其 *卸載* 後 *掛載* 至另一個 VT 到 PT 上執行, 此時
  *掛載* VT 的 PT 會被稱為 **載體線程 (CarrierThread)**
- 從工作原理來看會發現 PT 基本上永遠處於 running 狀態, 這樣有機會減少 context switch 的成本, 也盡量降低 PT 沒有工作或是被 blocking 的情況發生,
  畢竟 PT 存在就是成本
- 因此 VT 適用於大量 I/O 操作情境, 而大量 CPU 計算情境使用 VT 反而會適得其反, 因此兩者應是相輔相成關係而非取代關係
- 在 VT 裡拋錯只會顯示 VT 的 **StackTrace**, 因為對 VT 來說 PT 是不可見的
- JVM 的 VT 調度器, 預設是以 **FIFO** 運行的 `ForkJoinPool` (參考 `VirtualThread#createDefaultScheduler()`)
- 使用 **JFR (Java Flight Recorder)** 將可以監控 VT 的狀態, 例如啟動、結束、因某些原因無法啟動或是因釘選被阻塞時發出事件

---

#### [不知道沒差但知道了會變厲害的版本特性](#head2)

###### [430 : String Templates (Preview)](https://openjdk.org/jeps/430)

- 引入字串模板(String Templates), 這是一種結合字串文字和嵌入表達式的新語法特性, 此功能旨在簡化字串與表達式混合的書寫, 提升程式碼的可讀性/高安全性, 特別是在構建 SQL 查詢等情境下
- 這類似於 `String.format()` 的操作, 但更為進階和靈活, 其不僅允許嵌入變數, 還支援直接嵌入複雜的 Java 表達式, 並且具備語法檢查功能

###### [439 : Generational ZGC](https://openjdk.org/jeps/439)

- 提案介紹了 **Generational ZGC**, 這是一種增強型 Z 垃圾收集器, 它將記憶體堆分為年輕代和老年代, 年輕代物件會更頻繁地被收集, 提升效能
- 此技術旨在減少記憶體分配阻塞/降低堆內記憶體使用率和 CPU 開銷, 且收集過程中停頓時間維持在 1 毫秒內, 未來可能全面替換現有的非分代 ZGC

###### [442 : Foreign Function &amp; Memory API (Third Preview)](https://openjdk.org/jeps/442)

- **Foreign Function & Memory API** 第三次預覽的, 旨在讓 Java 程式與 JVM 之外的程式碼和記憶體進行互操作, 此 API 取代了 JNI, 提供更高效的外部函數調用與記憶體存取, 並加強了安全性
- 更新包括改進內存管理/優化函數調用，以及移除 VaList 類別, 該 API 支援存取各類型外部記憶體, 並確保記憶體釋放時的安全性與預測性

###### [443 : Unnamed Patterns and Variables (Preview)](https://openjdk.org/jeps/443)

- 提出的 **匿名模式與變數** (Unnamed Patterns and Variables), 允許開發者使用底線字元 `_` 來表示匿名模式和變數
- 這些匿名變數可以被初始化, 但無法進一步讀取或寫入, 其主要用途是當變數不需要具名時, 避免代碼中的雜訊
  ```
    if (record instanceof ColoredPoint(Point(int x, _), _)) { // 不會實際操作到的變數可使用 `_` 來減少雜訊
      System.out.println("點的 X 座標: " + x);
    }
  ```

###### [445 : Unnamed Classes and Instance Main Methods (Preview)](https://openjdk.org/jeps/445)

- 為了讓新手能更容易地學習 Java, 引入了 **無名類別** 和實例的 **main** 方法, 不需要理解 static/String[] args 等進階概念
- 這一變更保留了 Java 原有的結構, 讓新手能逐步過渡到更大的專案

###### [446 : Scoped Values (Preview)](https://openjdk.org/jeps/446)

- 提出了 **Scoped Values** 作為預覽功能, 允許在不使用方法參數的情況下安全且高效地在方法間傳遞資料, 特別適用於大量虛擬執行緒環境
- 這個新功能解決了 `ThreadLocal` 的一些問題, 如無限制的可變性和高資源成本, 而 `ScopedValue` 提供一個不可變的/範圍有限的數據傳遞方式, 並且支持跨執行緒繼承

###### [448 : Vector API (Sixth Incubator)](https://openjdk.org/jeps/448)

- 引入了 Vector API 的第六次孵化版本, 允許開發者編寫高效能的向量運算程式, 此 API 提供跨平台的向量指令支援, 能夠在不同的 CPU 架構 (如 x64 和 AArch64)上進行最佳化
- 這次更新包括新增對向量遮罩的 **異或 (xor)** 操作, 改進向量洗牌操作的效能, 並強化跨平台的可攜性與可讀性

###### [451 : Prepare to Disallow the Dynamic Loading of Agents](https://openjdk.org/jeps/451)

- 提議為未來版本中不再允許動態載入 **agent** 做準備, 在 JDK 21 當動態載入 **agent** 時 JVM 會發出警告提示未來將預設禁止這種行為
- 此變更旨在提升系統完整性, 要求使用者明確允許動態載入 **agent**, 大多數工具將不受影響

###### [452 : Key Encapsulation Mechanism API](https://openjdk.org/jeps/452)

- 引入了 **Key Encapsulation Mechanism (KEM) API**, 這是一種加密技術, 旨在透過公開金鑰加密來保護對稱金鑰, 此 API 支援 RSA-KEM/ECIES 等演算法, 並為未來的量子抗攻擊標準提供基礎
- KEM API 特別適用於加密協議(如 TLS)及混合公鑰加密方案(HPKE), 並允許安全提供者以 Java 或原生程式碼實作 KEM 演算法

###### [453 : Structured Concurrency (Preview)](https://openjdk.org/jeps/453)

- 引入了 **結構化並發** 作為預覽功能, 簡化了並發程式設計, 它允許將多個相關的執行緒任務視為一個整體進行管理, 提升錯誤處理和取消操作的可靠性, 並增強可觀察性
- 此功能使得開發者能夠更有效地控制並行任務的生命周期, 減少資源浪費, 適合用於處理大量虛擬執行緒的應用

---

#### [不知道沒差但知道了也沒差的版本特性](#head3)

###### [449 : Deprecate the Windows 32-bit x86 Port for Removal](https://openjdk.org/jeps/449)

- 將 Windows 32-bit x86 埠標記為不建議使用, 並計劃在未來版本中移除, 這樣做是為了讓 OpenJDK 社群能更專注於新功能的開發, 尤其是 Windows 平台逐漸停止支援 32 位元作業模式
- **x86 port** : 是一個專門針對 x86 架構的作業系統或應用程式版本, 在這裡指的是 OpenJDK 對 32 位元 Windows x86 處理器的支援
