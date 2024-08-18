## [form OpenJDK - JDK13](https://openjdk.org/projects/jdk/13)

---

### 不能不知道的版本特性

### 不知道沒差但知道了會變厲害的版本特性

- [350 : Dynamic CDS Archives](https://openjdk.org/jeps/350)
    - 引入了動態 Class Data Sharing (CDS) 檔案功能, 允許在 Java 應用程序執行結束時動態存檔已載入的類別, 改善啟動時間和內存使用效率
    - 這消除了以往需要多次試運行來生成類別列表的步驟, 並且支持內建和自定義的類別載入器
    - 該功能旨在簡化和優化應用程序的 CDS 使用體驗，特別是在多進程環境下能夠節省內存
    - *Class Data Sharing (CDS) 在 jdk12 的 JEP341 有被擴展*
- [351 : ZGC: Uncommit Unused Memory](https://openjdk.org/jeps/351)
    - 提出了增強 ZGC（Z Garbage Collector）的功能, 使其能夠將未使用的堆內存返還給作業系統
    - 這對於記憶體使用受限的環境（如容器）特別有用, 因為它可以減少應用程序的記憶體佔用, 從而優化資源利用
    - 此功能允許在不影響應用程序效能的情況下, 動態釋放閒置內存
    - *同 jdk12 的 JEP346 一樣對 G1 的優化*
- [353 : Reimplement the Legacy Socket API](https://openjdk.org/jeps/353)
    - 重新實現舊的 Socket API, 使用更現代、易於維護的實現來取代原有的 `java.net.Socket` 和 `java.net.ServerSocket` 底層實現, 提高效能並減少原有實現中的錯誤和維護成本
    - 新的實現稱為 `NioSocketImpl`, 它更容易調試並且更適合與未來可能引入的用戶態執行緒(纖程, Fiber, 是一種lightweight threads)一起工作
- [354 : Switch Expressions (Preview)](https://openjdk.org/jeps/354)
    - "Switch Expressions" 的第二次預覽, 此次更新將原先的 break 陳述式替換為 yield 陳述式, 用於從 switch 表達式中返回值, 進一步簡化代碼, 提高可讀性
- [355 : Text Blocks (Preview)](https://openjdk.org/jeps/355)
    - 引入了 "Text Blocks" 作為 Java 的一種新型字面量, 允許開發者更簡單地定義多行字符串, 避免大部分轉義序列, 並自動格式化字符串
    - 這使得編寫包括 HTML、SQL 等在內的多行文本變得更容易, 提升了代碼的可讀性和維護性
  ```
    String html = """
        <html>
        <body>
            <p>Hello, world</p>
        </body>
        </html>
    """;
  ```

### 不知道沒差但知道了也沒差的版本特性

---
