## [form OpenJDK - JDK19](https://openjdk.org/projects/jdk/19)

here is [[study code](./src/test/java/org/aery/study/jdk19)]

---

### 不能不知道的版本特性

### 不知道沒差但知道了會變厲害的版本特性

- [405 : Record Patterns (Preview)](https://openjdk.org/jeps/405)
    - 引入了 Java 語言中的 "Record Patterns" (記錄模式), 用於解構 record 類型的值, 並支持嵌套使用模式匹配以進行更複雜的資料處理
    - 這個功能使得在操作資料時可以更具宣告性和組合性, 提升代碼的簡潔性和可讀性, 目前與預覽特性, 未來可能會擴展至支援更多模式, 如陣列模式和泛型推斷等
- [424 : Foreign Function &amp; Memory API (Preview)](https://openjdk.org/jeps/424)
    - 引入了外部函數和記憶體 API< 讓 Java 程式能夠與 JVM 外的代碼和資料互操作
    - 這個 API 允許有效地調用本機函數(如 C 函數)並安全地存取外部記憶體, 改善了 Java Native Interface (JNI) 的繁瑣性與不安全性
    - 此 API 提供的功能如記憶體分配、存取和釋放, 並支持方法處理器來簡化外部函數的呼叫操作
- [425 : Virtual Threads (Preview)](https://openjdk.org/jeps/425)
    - 引入了虛擬執行緒(Virtual Threads), 用於減輕 Java 應用程式中編寫高併發應用的負擔
    - 虛擬執行緒是輕量級的執行緒, 能夠顯著提升應用程式的擴展性, 支援高效處理大量並發請求
    - 它們不像平台執行緒那樣依賴昂貴的操作系統資源, 使得每個任務都能擁有自己的執行緒, 而不需要複雜的非同步編程
- [426 : Vector API (Fourth Incubator)](https://openjdk.org/jeps/426)
    - 介紹了 Vector API 的第四次孵化版本, 旨在提供一個表達向量運算的 API, 能夠在執行時編譯為最佳化的向量指令, 提升多核處理器上的運算效能
    - 此版本增加了支援 MemorySegment 的向量載入與儲存/新的跨通道操作(如壓縮和擴展)及位元操作(如反轉位元順序), 該 API 可在多種 CPU 架構上運行
- [427 : Pattern Matching for switch (Third Preview)](https://openjdk.org/jeps/427)
    - 提供了針對 `switch` 表達式和語句的模式匹配, 允許在 case 標籤中使用模式進行更靈活的資料處理
    - 這第三次預覽版改善了語法, 加入了 when 子句取代守衛模式, 並對 switch 在選擇器表達式為 `null` 的情況下的執行行為進行了優化
    - 這使得 switch 更加表達性強且安全, 減少了程式中的複雜性與潛在錯誤
- [428 : Structured Concurrency (Incubator)](https://openjdk.org/jeps/428)
    - 引入了結構化並發 API, 旨在簡化多執行緒程式設計, 這個 API 將多個執行緒中的任務視為單一工作單元, 統一管理錯誤處理與取消, 提升程式的可維護性/可靠性與可觀察性
    - 結構化並發使得任務與子任務之間的關係更加明確, 有助於避免執行緒泄漏和取消延遲等常見問題

### 不知道沒差但知道了也沒差的版本特性

- [422 : Linux/RISC-V Port](https://openjdk.org/jeps/422)
    - 提供對 Linux/RISC-V 的支援, 將 JDK 移植到基於 RISC-V 的 Linux 平台
    - 這次移植的重點是支援 64 位元的 RV64GV 通用指令集架構, 並整合 HotSpot 的主要子系統如模板解釋器、C1/C2 編譯器和多種垃圾回收器
    - 此移植已通過多項測試，並由華為、阿里巴巴、紅帽等公司提供支援，以確保與其他平台的相容性
    - **RISC-V** 是一種開放式指令集架構(ISA), 最初由加州大學柏克萊分校開發, 與傳統的封閉式指令集(如 Intel 的 x86 或 ARM)不同, RISC-V
      是基於精簡指令集計算（RISC）理念, 並且具有開放授權任何人都可以自由使用和修改. 它以模組化設計著稱, 允許不同應用場景下的靈活擴展,
      因此特別適合嵌入式系統/物聯網/人工智慧等領域
