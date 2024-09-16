## [form OpenJDK - JDK18](https://openjdk.org/projects/jdk/18)

here is [[study code](./src/test/java/org/aery/study/jdk18)]

---

### 不能不知道的版本特性

- [408 : Simple Web Server](https://openjdk.org/jeps/408)
  - [study-code](./src/test/java/org/aery/study/jdk18/JEP408_Simple_Web_Server.java)
  - 提供了一個簡單的 Web 伺服器工具, 旨在讓開發者能夠快速啟動一個用於測試和開發的靜態文件伺服器, 這個伺服器工具可透過命令列使用, 也提供 API 供程式化使用
  - 伺服器僅支持基本的 HTTP(僅處理 HEAD 和 GET) 功能, 不包含 CGI、Servlet 等進階功能, 此工具主要用於簡單的測試/教育和臨時開發, 並不適合生產環境使用
- [413 : Code Snippets in Java API Documentation](https://openjdk.org/jeps/413)
  - [study-code](./src/test/java/org/aery/study/jdk18/JEP408_Simple_Web_Server.java)
  - 引入了 `@snippet` 標籤, 用於在 Java API 文件中簡化代碼片段的展示
  - 這個功能改善了過去使用 `<pre>{@code ...}</pre>` 的方式, 並支援語法高亮/代碼驗證/以及與 IDE 整合
  - `@snippet` 可以內嵌或引用外部文件中的代碼, 並提供高階標記功能, 如高亮/替換和鏈接等, 方便開發者更容易維護和編寫文件中的示例代碼

### 不知道沒差但知道了會變厲害的版本特性

- [400 : UTF-8 by Default](https://openjdk.org/jeps/400)
    - 宣布在 Java 18 及後續版本中, 將 UTF-8 設為標準 Java API 的預設字元集, 如果需要保留先前版本的行為，開發者仍可透過參數進行調整
    - 在這之前的版本中是基於作業系統的區域設置, 例如: Windows 上是 MS950, Linux 上是 UTF-8, 這導致了不同平台上的 Java 程式在處理文件和文字時的行為不一致
- [417 : Vector API (Third Incubator)](https://openjdk.org/jeps/417)
    - 提供了向量 API 的第三次孵化, 旨在支持在多種 CPU 架構上進行高效的向量運算, 特別是對 x64 和 AArch64 架構
    - 它允許開發者使用明確的向量運算來替代標量運算達到更高的效能, 此 API 加強了對硬體掩碼操作的支援, 並提升了效能表現, 尤其針對 ARM SVE 架構的向量操作進行了優化
    - 這些改進為機器學習、密碼學等領域提供了顯著的效能提升
- [418 : Internet-Address Resolution SPI](https://openjdk.org/jeps/418)
    - [study-code](./src/test/java/org/aery/study/jdk18/JEP418_InternetAddress_Resolution_SPI.java)
    - 定義了一個用於網路位址解析的 SPI (Service Provider Interface), 使得 `java.net.InetAddress` 可以使用平台內建解析器以外的其他解析器
    - 這個 SPI 主要動機為避免因為解析阻塞導致的效能問題/支援新興網路協議 (如 DNS over HTTPS), 以及提供更大的定制和測試能力
    - 此 SPI 允許開發替代的解析器實作來替換預設的內建解析器, 特別對虛擬線程的應用有幫助
- [419 : Foreign Function &amp; Memory API (Second Incubator)](https://openjdk.org/jeps/419)
  - 外部函數與記憶體 API 第二次孵化, 允許 Java 程式與 JVM 之外的原生程式碼與記憶體進行高效且安全的互操作
  - 這個 API 旨在取代 JNI 提供更易用/效能更佳且通用的替代方案, 特別針對外部函式調用和訪問非 JVM 管理的記憶體
  - 本次更新加入了新的記憶體和方法處理機制, 並改善了對外部資源的控制
- [420 : Pattern Matching for switch (Second Preview)](https://openjdk.org/jeps/420)
  - [study-code](./src/test/java/org/aery/study/jdk18/JEP420_Pattern_Matching_for_switch.java)
  - 對 switch 語句進行模式匹配的增強的第二次預覽, 此功能允許在 switch 語句中使用模式來測試變數, 並對每個模式執行特定操作, 從而提升表達能力
  - 這次增加了如支援封閉類型的精確匹配及更好的可讀性等改進, 此功能允許更靈活的控制流, 並保持對現有 switch 語句的相容性
- [421 : Deprecate Finalization for Removal](https://openjdk.org/jeps/421)
  - [study-code](./src/test/java/org/aery/study/jdk18/JEP420_Pattern_Matching_for_switch.java)
  - 標記 **finalization** 機制即將移除, 該機制被認為有許多缺陷, 包括不可預測的延遲/無法保證執行順序和效能問題
  - 此提案鼓勵開發者轉而使用 try-with-resources 或 Java 9 引入的 `Cleaner` 來管理資源, 避免使用 finalization 來釋放資源

### 不知道沒差但知道了也沒差的版本特性

- [416 : Reimplement Core Reflection with Method Handles](https://openjdk.org/jeps/416)
    - 主要重新實作 `java.lang.reflect.Method`/`Constructor`/`Field`, 以 `java.lang.invoke` 的 method handles 作為基礎機制, 減少維護與開發成本
    - 這改變僅限於實作層面, 對外部 API 無影響, 新實作效能在某些情況下可達 43-57% 提升, 但某些非靜態場景中有些性能下降
    - 這實作未來將簡化 JVM, 並支援新語言功能如 Project Valhalla
