## [form OpenJDK - JDK17](https://openjdk.org/projects/jdk/17)

here is [[study code](./src/test/java/org/aery/study/jdk17)]

---

### 不能不知道的版本特性

- [356 : Enhanced Pseudo-Random Number Generators](https://openjdk.org/jeps/356)
    - [study-code](./src/test/java/org/aery/study/jdk17/JEP356_Enhanced_PseudoRandom_Number_Generators.java)
    - 提供了增強的偽隨機數生成器(PRNG)介面和實現, 包括可跳躍和可分割的 PRNG 演算法
    - **PRNG** (Pseudo-Random Number Generator, 偽隨機數生成器) : 是一種使用演算法來生成一系列看似隨機的數字, 但實際上是根據初始種子值進行計算,
      因此可以預測和重現
    - **可跳躍** (Jumpable PRNG) : 允許生成器"跳過"一段預定長度的數值序列, 這樣不同的執行緒可以使用相同生成器但產生不重複的隨機數
    - **可分割** (Splittable PRNG) : 允許生成器被拆分成多個生成器, 每個生成器可以獨立運行並生成不同的隨機數序列, 適合並行程式設計中的需求
    - 它的目標是讓開發者可以更容易地互換使用不同的 PRNG 演算法, 支援基於流的程式設計, 並減少現有 PRNG 類中的代碼重複
    - 新演算法(例如 LXM 家族)提供更高的質量和效能, 並解決了舊演算法中的一些已知問題
    - 基本上可以取代舊有的 `Random`, 不過由於 `RandomGenerator` 並非 thread safety, 因此多執行續環境仍應使用 `ThreadLocalRandom`
    - 以上提到的隨機生成器都是PRNG, 因此若需要接近真正含意的隨機生成器則應該採用 `SecureRandom`, 它是基於外部的隨機源(例如作業系統提供的隨機數生成器,
      通常依賴硬體現象如熱噪音或電磁現象)生成, 不過其其生成速度比 PRNG 慢, 因此通常用於安全相關的應用
- [409 : Sealed Classes](https://openjdk.org/jeps/409)
    - [study-code](./src/test/java/org/aery/study/jdk17/JEP409_Sealed_Classes.java)
    - 正式引入了密封類(Sealed Classes), 允許類或介面限制其可擴展或實作的子類或子介面, 增強了 Java 對模式匹配和類階層封閉控制的支援
    - 開發者可以使用 `sealed` 修飾符來指定哪些具體類可以擴展密封類, 這有助於更好地控制繼承層次結構, 同時保持類的可讀性和安全性

### 不知道沒差但知道了會變厲害的版本特性

- [382 : New macOS Rendering Pipeline](https://openjdk.org/jeps/382)
    - 引入了針對 macOS 的新 Java 2D 渲染管道, 基於 Apple 的 Metal API, 替代過時的 OpenGL API
    - 主要目的是在 macOS 上提供與現有 OpenGL 管道功能對等的渲染性能, 並確保在未來 OpenGL 被移除時能持續運行
    - 該管道將與 OpenGL 共存, 並且當 OpenGL 初始化失敗時, 才會使用 Metal 渲染, 預計在未來成為預設渲染管道
- [403 : Strongly Encapsulate JDK Internals](https://openjdk.org/jeps/403)
    - 此為 jdk 16 的 JEP 396 後續, 強化了 JDK 的內部封裝, 除了少數關鍵的內部 API（如 sun.misc.Unsafe）外, 所有內部元素都將被嚴格封裝, 鼓勵開發者轉向使用標準
      API
- [406 : Pattern Matching for switch (Preview)](https://openjdk.org/jeps/406)
    - [study-code](./src/test/java/org/aery/study/jdk17/JEP406_Pattern_Matching_for_switch.java)
    - 引入了針對 switch 表達式與語句的模式匹配, 允許在 case 標籤中使用模式, 並引入了守護模式(guarded patterns)和括號模式(parenthesized patterns)
    - 這項功能旨在簡化處理多種條件的邏輯, 使程式碼更具表達性和可讀性, 並改善 null 值處理方式
- [412 : Foreign Function &amp; Memory API (Incubator)](https://openjdk.org/jeps/412)
    - 引入了外部函數與記憶體 API, 讓 Java 程式能夠與 JVM 之外的代碼和數據進行互操作
    - 該 API 提供了高效的方式來調用外部函數(如本地程式庫)並安全地訪問外部記憶體, 避免了 JNI 帶來的複雜性和潛在危險
    - 此 API 還提供了針對異質內存的操作與資源管理功能, 提升了 Java 語言的靈活性和性能
- [414 : Vector API (Second Incubator)](https://openjdk.org/jeps/414)
  - 提供了向量 API 的第二次孵化, 允許開發者以高效方式在多種 CPU 架構上進行向量運算, 從而超越純標量運算的性能
  - 此 API 支援針對 x64 和 AArch64 架構的最佳向量指令映射, 並加入對字符操作和超越函數(如三角運算)的增強功能
  - 目標是提供可移植且高效的 API, 適用於數學運算/機器學習/加密等多領域, 並且能夠在不支援向量指令的硬體上優雅降級
- [415 : Context-Specific Deserialization Filters](https://openjdk.org/jeps/415)
  - [study-code](./src/test/java/org/aery/study/jdk17/JEP415_ContextSpecific_Deserialization_Filters.java)
  - 引入了上下文特定的反序列化過濾器, 允許應用程序通過 JVM 全域過濾器工廠來為每個反序列化操作動態選擇過濾器
  - 這提升了反序列化過程的安全性, 允許根據應用程序的具體情況設置特定的過濾規則, 防止反序列化攻擊
  - 這項功能改進了現有的 JVM 靜態全域過濾器, 適用於複雜應用中的多層次執行上下文

### 不知道沒差但知道了也沒差的版本特性

- [306 : Restore Always-Strict Floating-Point Semantics](https://openjdk.org/jeps/306)
    - 恢復 Java 浮點運算的嚴格語義, 取消 Java SE 1.2 中引入的兩種不同的浮點模式(`strictfp` 和默認模式), 讓浮點運算始終使用嚴格模式
    - 這樣做可以減少開發數值敏感庫(如 `java.lang.Math` 和 `java.lang.StrictMath`)的複雜性, 也避免平台在這方面的混亂
    - 現代處理器(如 SSE2)已經能有效支持嚴格浮點運算, 因此不再需要保留默認的非嚴格浮點模式
- [391 : macOS/AArch64 Port](https://openjdk.org/jeps/391)
    - 提供對 macOS/AArch64 的支援, 將 JDK 移植到使用 Apple Silicon 的 macOS 系統
    - 這項工作重點在於確保 JDK 能在 AArch64 平台上原生運行, 而非依賴 Rosetta 2 模擬器, 從而提升性能
    - 這個移植計劃重用了其他 AArch64 平台（如 Linux 和 Windows）的代碼，並加入了對 macOS 特有功能的支援, 例如 Write XOR Execute (W^X) 記憶體保護策略
- [398 : Deprecate the Applet API for Removal](https://openjdk.org/jeps/398)
    - 將 Applet API 標記為計劃移除, 原因是現今所有主流瀏覽器已經停止支援 Java 瀏覽器插件, 最終移除與 Applet 相關的類別和介面, 包括
      `java.applet.Applet`/`javax.swing.JApplet` 等, 並清除相關的 API 元素
    - 這項變更符合目前的網頁技術趨勢, 讓 Java 平台更加現代化
- [407 : Remove RMI Activation](https://openjdk.org/jeps/407)
    - 移除了 RMI 激活機制(RMI Activation), 但保留了其餘的 RMI 功能
    - RMI Activation 已過時且不再使用, 已於 jdk 15 JEP 358 中被標記為即將移除, 最終於 jdk 17 移除相關的 API 包/實現代碼/測試以及文件說明
    - 這項移除不會影響其他技術, 如 **JavaBeans Activation Framework** 和 **Jakarta Activation**
- [410 : Remove the Experimental AOT and JIT Compiler](https://openjdk.org/jeps/410)
    - 移除了實驗性的 AOT(提前編譯)與 JIT(即時編譯)編譯器, 這些功能自引入以來使用率很低, 且維護成本過高
    - 雖然移除了 `jaotc` 工具和 `Graal` 編譯器模組, 但保留了 JVM 編譯器接口(JVMCI), 開發者仍可使用外部的 GraalVM 進行 AOT 或 JIT 編譯
- [411 : Deprecate the Security Manager for Removal](https://openjdk.org/jeps/411)
    - 將 Security Manager 標記為計劃移除, 該機制自 Java 1.0 引入以來已過時, 主要用於保護 Java applets 的安全
    - 由於現代應用不再依賴這一機制, 並且維護成本高, 未來將逐步減少其功能, 最終完全移除, 取而代之的是更現代的安全技術, 如容器和模組隔離等
    - **Security Manager** : 是一種 Java 平台安全機制, 允許應用程式在運行時根據策略檔案限制程式執行的操作, 其主要功能包括控制檔案讀寫、網路連線、執行系統命令等操作,
      確保程式在沙盒環境中運行時不能做出惡意行為. 最早是為了保護執行於瀏覽器中的 applet 而設計, 但隨著技術發展已逐漸過時並計劃移除
