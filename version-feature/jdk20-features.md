## [form OpenJDK - JDK20](https://openjdk.org/projects/jdk/20)

here is [[study code](./src/test/java/org/aery/study/jdk20)]

---

### 不能不知道的版本特性

### 不知道沒差但知道了會變厲害的版本特性

- [429 : Scoped Values (Incubator)](https://openjdk.org/jeps/429)
    - 引入 **Scoped Values**, 這是一種能在多執行緒間共享不可變數據的機制, 尤其適用於虛擬執行緒環境, 與傳統的 `ThreadLocal` 變數相比, **Scoped Values**
      更簡單/性能更好, 避免了可變性和內存洩漏等問題
    - 其主要目標是提供易用的數據共享模式/提高代碼可讀性及可維護性, 並能在不同執行緒之間高效傳遞數據, 支持大型應用程序的數據流管理
- [432 : Record Patterns (Second Preview)](https://openjdk.org/jeps/432)
    - 提出了 **Record Patterns** 的第二次預覽, 擴展了模式匹配功能, 允許 `Record` 在 Java 中被解構
    - 此功能允許將記錄類型的組件直接解構為變量, 並支援嵌套模式使得處理複雜數據結構更加簡單
    - 與第一預覽相比, 新增了泛型類型參數推斷/在增強型 for 語句中使用記錄模式等改進, 此功能提升了 Java 語言的表達能力和可讀性
- [433 : Pattern Matching for switch (Fourth Preview)](https://openjdk.org/jeps/433)
    - 提出將"模式匹配"擴展至 `switch` 表達式和語句, 允許 switch 支援不同類型的模式匹配, 而不僅是常量
    - 此功能可以簡化多分支的邏輯處理, 提升代碼的可讀性和安全性, 第四次預覽版的改進包括在模式 switch 表達式中對 enum 類型的處理/簡化的語法和泛型記錄模式的類型推斷
- [434 : Foreign Function &amp; Memory API (Second Preview)](https://openjdk.org/jeps/434)
    - 提出了**Foreign Function & Memory API** 的第二次預覽, 該 API 讓 Java 程式能與 JVM 之外的本機程式碼和資料進行互操作, 它支援安全且高效地調用外部函數和管理外部記憶體,
      而不需使用複雜且危險的 JNI
    - 這次預覽增加了如 MemorySegment 與 MemoryAddress 的統一, 並改進了 MemoryLayout 和 MemorySession
- [436 : Virtual Threads (Second Preview)](https://openjdk.org/jeps/436)
    - 提出了 **Virtual Threads** 的第二次預覽, 虛擬執行緒是一種輕量級的執行緒實現, 可顯著提升高併發應用程式的效能, 且簡化了併發應用的開發
    - 與傳統的作業系統執行緒不同, 虛擬執行緒不會永久佔用 OS 執行緒資源, 並能在需要時自動暫停和恢復, 這使得應用程式能以更佳的硬體資源利用率運行大量執行緒
- [437 : Structured Concurrency (Second Incubator)](https://openjdk.org/jeps/437)
    - 提出 **結構化並發** 的第二次孵化預覽, 這是一種新的 API, 用於簡化多執行緒編程, 其通過將多個並發任務視為一個整體, 來改善錯誤處理/取消機制和可觀察性
    - 此 API 允許開發者將子任務的生命週期結構化, 並自動協調錯誤傳播和取消行為, 提升程式的可靠性與可維護性
    - 此次預覽版與前一版的主要改進是支持作用域值的繼承
- [438 : Vector API (Fifth Incubator)](https://openjdk.org/jeps/438)
  - 提出 **Vector API** 的第五次孵化預覽, 旨在提供一種 API 來表達向量計算, 使其能在運行時編譯為最佳的向量指令, 從而在支援的 CPU 架構上實現比標量計算更高的效能
  - 這次預覽包括小幅的錯誤修復和性能增強, 並繼續與 Project Valhalla 對齊, 最終目標是透過該專案的增強功能來優化向量 API
  - **Project Valhalla** :  是 OpenJDK 的一個長期研發計劃, 旨在引入增強的 Java 語言功能, 特別針對數據密集型應用來提高性能, 其主要目標是通過"值類型"(
    Value Types)等新概念來消除對象分配和垃圾回收的性能開銷, 實現更接近原生數據的操作效率. 值類型可提供與原始類型相似的性能表現, 同時保留對象的語義,
    這使得在處理大量數據時可以避免不必要的內存分配

### 不知道沒差但知道了也沒差的版本特性
