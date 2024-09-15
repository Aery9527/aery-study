## [form OpenJDK - JDK15](https://openjdk.org/projects/jdk/15)

here is [[study code](./src/test/java/org/aery/study/jdk15)]

---

### 不能不知道的版本特性

- [377 : ZGC: A Scalable Low-Latency Garbage Collector](https://openjdk.org/jeps/377)
    - 將 ZGC (Z Garbage Collector)從實驗性功能轉為正式產品功能, ZGC 是一個可擴展的低延遲垃圾回收器, 適合大規模應用場景, 支持 Linux/Windows 和 macOS
      等平台
    - 此更新移除了啟用 ZGC 時所需的實驗選項, 並增加了多項改進, 包括更好的 NUMA 感知和多線程 heap 處理
- [378 : Text Blocks](https://openjdk.org/jeps/378)
    - [study-code](./src/test/java/org/aery/study/jdk15/JEP378_Text_Blocks.java)
    - 引入了 **Text Blocks** 作為 Java 語言的一部分, 允許開發者使用多行字串文字表示法, 減少不必要的跳脫字元並改善程式碼的可讀性, 特別適用於需要嵌入
      HTML、SQL 或 JSON 等非 Java 語言的情境
    - **Text Blocks** 使用 """ 作為邊界符號, 並提供新的跳脫字元, 如 \s(表示空格)及 \<line-terminator> 來控制新行及空白字元
- [379 : Shenandoah: A Low-Pause-Time Garbage Collector](https://openjdk.org/jeps/379)
    - 將 **Shenandoah** 垃圾回收器(GC)從實驗性功能升級為正式產品功能, **Shenandoah** 是一個低延遲的垃圾回收器, 旨在最大限度地減少 GC
      暫停時間，適用於需要高可擴展性且對延遲敏感的應用

### 不知道沒差但知道了會變厲害的版本特性

- [339 : Edwards-Curve Digital Signature Algorithm (EdDSA)](https://openjdk.org/jeps/339)
    - 增加了對 Edwards 曲線數位簽名演算法 (EdDSA) 的支持, 集成於 Java 加密庫 SunEC 中, 將應用於簽名、密鑰生成和驗證
    - 這是一種現代的橢圓曲線簽名方案, 具有比現有方案更高的安全性和性能, 並且在保持相同安全強度下, 提供比 ECDSA 更快的性能, 該實作防範側信道攻擊
- [360 : Sealed Classes (Preview)](https://openjdk.org/jeps/360)
    - 引入了密封類(Sealed Classes), 強制指定允許的子類, 從而提高了代碼的清晰度和安全性
   ```
    public abstract sealed class Shape permits Circle, Rectangle {
        public abstract double area();
    }  
   ```
    - 上述的 `Shape` 類聲明為密封類, 並且只允許 `Circle` 和 `Rectangle` 兩個子類
- [371 : Hidden Classes](https://openjdk.org/jeps/371)
    - [study-code](./src/test/java/org/aery/study/jdk15/JEP371_Hidden_Classes.java)
    - 引入了隱藏類(Hidden Classes), 這些類僅能通過反射訪問而無法被其他類直接使用, 主要針對運行時生成框架的實作(例如spring data介面的實作)而設計
    - 隱藏類可以在不需要時被卸載, 有助於減少內存佔用, 此功能提升了框架的靈活性, 並取代了過時的 `sun.misc.Unsafe::defineAnonymousClass` API
- [372 : Remove the Nashorn JavaScript Engine](https://openjdk.org/jeps/372)
    - 移除 Nashorn JavaScript 引擎及相關 API 和工具 jjs
    - Nashorn 最早在 JDK 8 中引入, 取代 Rhino 引擎, 但由於 ECMAScript 的快速演變維護 Nashorn 變得困難, 因此移除將使 JDK 能更專注於其他功能
- [375 : Pattern Matching for instanceof (Second Preview)](https://openjdk.org/jeps/375)
    - 提供了一個有關 instanceof 操作符的模式匹配功能的第二次預覽, 此功能讓程式能夠更簡潔/安全地進行類型檢測與轉換
    - 例如, 原本需要 if (obj instanceof String) 及後續轉型的代碼, 現在可以簡化為 if (obj instanceof String s)
- [383 : Foreign-Memory Access API (Second Incubator)](https://openjdk.org/jeps/383)
    - 引入了第二次試驗性的 **Foreign-Memory Access** API, 這個 API 讓 Java 程式可以安全且高效地訪問 JVM 堆外的外部記憶體
    - 此 API 的主要目標包括通用性/安全性/決定性, 以及替代過去不安全的 `sun.misc.Unsafe` 介面, 此版本包含對記憶體佈局及並行處理的改進,
      並提供更多與原生記憶體互操作的選項
- [384 : Records (Second Preview)](https://openjdk.org/jeps/384)
    - 提供了 **Records** 功能的第二次預覽, Records 是一種特殊的類, 專門用來作為不可變數據的簡單載體
    - 這次更新改善了其用法, 使開發者能夠更簡單地定義只包含數據的類, 並自動生成 equals、hashCode、toString 等方法

### 不知道沒差但知道了也沒差的版本特性

- [373 : Reimplement the Legacy DatagramSocket API](https://openjdk.org/jeps/373)
    - 重新實作了 `java.net.DatagramSocket` 和 `java.net.MulticastSocket` API, 使用更簡單、易維護的實作方式,
    - 新實作基於 NIO (非阻塞 IO), 解決了 IPv4 和 IPv6 的兼容性挑戰, 並解決了過去的並發問題, 同時保留了舊版的回溯兼容機制
    - 此變更使其更容易與虛擬執行緒(如 Project Loom)兼容, 並改善了過去實作中的錯誤與效能問題
    - **DatagramSocket** : 用於傳送和接收不可靠的數據報(Datagram), 這是一種無連接的網路通信方式, 它適合用於需要高效/低延遲的應用, 但數據可能會丟失或順序錯亂,
      例如視頻串流或遊戲通信
    - **MulticastSocket** : 是一個特殊的 `DatagramSocket`, 用來處理多播(multicast)通信, 允許一個發送者將數據報發送給多個接收者, 常用於視頻廣播或音頻會議等場景
- [374 : Disable and Deprecate Biased Locking](https://openjdk.org/jeps/374)
    - 棄用並停用偏向鎖定(Biased Locking), 這是 HotSpot 虛擬機器中用來優化無爭用同步操作的技術
    - 由於偏向鎖定的效能優勢已不再明顯, 並且維護成本高, 因此逐步移除相關的命令行選項, 藉此簡化 HotSpot 的同步子系統, 使未來的設計變更更易實現
    - **偏向鎖** : 減少多執行緒環境下鎖競爭的開銷, 在無鎖競爭的情況下, 鎖會「偏向」於某個特定執行緒, 意味著只要這個執行緒重複進入臨界區, 就不需要進行昂貴的鎖操作.
      當另一個執行緒嘗試獲取同一個鎖時，才會撤銷偏向鎖，進行正常的鎖競爭
- [381 : Remove the Solaris and SPARC Ports](https://openjdk.org/jeps/381)
    - 移除對 Solaris 和 SPARC 平台的支援, 這包括相關的原始碼和建構系統邏輯
    - 由於 Solaris 和 SPARC 平台的使用日益減少, 這樣的移除將有助於簡化 OpenJDK 的開發, 並讓資源集中於更具前景的平台
    - 此提案的目的是促進新功能如 Valhalla/Loom/Panama 的開發, 加速 Java 平台的演進
- [385 : Deprecate RMI Activation for Removal](https://openjdk.org/jeps/385)
    - 棄用 RMI Activation, 並在未來移除它, RMI Activation 是 RMI 的一部分, 自 Java 8 起已成為可選功能, 使用率極低, 且分佈式系統現今多依賴網絡技術,
      移除這一功能將減少維護成本, 並簡化 Java 平台的開發, 無需對其他 RMI 部分進行改變
    - **RMI Activation** :  是 Java RMI(Remote Method Invocation)的一部分, 用來啟動和管理遠端物件, 它允許在需要時將不活躍的遠端物件啟動, 並將它們的狀態恢復到
      JVM 中, 這樣的機制使得遠端物件能夠在不活躍時節省資源, 僅在需要時才會重新載入或啟動
