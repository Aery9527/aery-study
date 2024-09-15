## [form OpenJDK - JDK16](https://openjdk.org/projects/jdk/16)

here is [[study code](./src/test/java/org/aery/study/jdk16)]

---

### 不能不知道的版本特性

- [390 : Warnings for Value-Based Classes](https://openjdk.org/jeps/390)
    - [study-code](./src/test/java/org/aery/study/jdk16/JEP390_Warnings_for_ValueBased_Classes.java)
    - 提議針對 **值導向類別** (value-based classes)提供警告, 尤其是對原始類別包裝器(如 Integer、Double 等)進行調整, 將其標示為值導向類別並移除其建構子,
      並警告開發者避免對這些類別進行同步操作
    - 此更動旨在為未來的原始類別(primitive classes)遷移做準備, 提升 Java 平台的效能和記憶體管理
- [392 : Packaging Tool](https://openjdk.org/jeps/392)
    - 提供了 `jpackage` 工具, 用於將 Java 應用程式打包成自包含的可安裝包, 支援 Windows(msi、exe)/macOS(pkg、dmg)和 Linux(deb、rpm)等平台格式
    - 它可以處理傳統 JAR 和模組化應用, 並允許指定啟動參數和平台專屬選項, 此工具簡化了應用分發, 提升了跨平台打包能力, 適合需要安裝體驗的應用
    - 使用方式如下
    ```
    jpackage --input input_folder --main-jar app.jar --name YourAppName --type exe
    ```
    - 或添加圖標、版本號等細節
    ```
    jpackage --input input_folder --main-jar app.jar --name YourAppName --type exe --icon icon.ico --app-version 1.0
    ```
- [394 : Pattern Matching for instanceof](https://openjdk.org/jeps/394)
    - [study-code](./src/test/java/org/aery/study/jdk16/JEP394_Pattern_Matching_for_instanceof.java)
    - 提議為 instanceof 操作符引入模式匹配, 簡化類型檢查與轉型的代碼, 通過模式匹配開發者可以在一次操作中檢查物件是否為某類型, 並直接取得該類型的變數,
      省去顯式轉型操作。
- [395 : Records](https://openjdk.org/jeps/395)
    - [study-code](./src/test/java/org/aery/study/jdk16/JEP395_Records.java)
    - 引入 `Record` 類別, 這是一種用來簡單表示不可變數據的透明載體, 它會自動生成如 equals/hashCode/toString 等基本方法
    - 此功能旨在提高代碼的可讀性和可維護性, 同時保持 Java 的面向對象特性

### 不知道沒差但知道了會變厲害的版本特性

- [338 : Vector API (Incubator)](https://openjdk.org/jeps/338)
    - 提供了一個初期的 Vector API, 旨在透過 `jdk.incubator.vector` 模組來表達向量運算, 以便在 x64 和 AArch64 架構上, 將其編譯為最佳的硬體向量指令,
      提升執行效能
    - 目標包括提供清晰/簡潔且平台無關的 API, 並確保即使在不支援的硬體上也能優雅降級, 主要應用於高效能運算領域, 例如機器學習/線性代數/密碼學和金融等
- [376 : ZGC: Concurrent Thread-Stack Processing](https://openjdk.org/jeps/376)
    - 提出了將 ZGC 垃圾回收器的線程堆疊處理從 safepoint 遷移到並行階段的方案
    - 此改進旨在減少 GC 停頓時間, 尤其是在大型多線程應用中, 通過引入堆疊水印屏障, 線程堆疊會在 safepoint 之後逐步並行處理, 確保效能的提升和最低的延遲影響
    - 這項技術能夠有效地改善 ZGC 的可擴展性和效能
- [380 : Unix-Domain Socket Channels](https://openjdk.org/jeps/380)
    - 新增了對 Unix 域套接字(AF_UNIX)通道的支持, 主要用於同一台主機上的進程間通信(IPC)
    - 它通過 `java.nio.channels` API 支持 Unix 域套接字, 提供更快的設置時間和更高的數據吞吐量, 提高了本地通信的效率和安全性
    - 這些套接字的尋址基於檔案系統路徑, 而非 IP 地址和端口號, 適用於 Unix 和 Windows 10 平台
- [386 : Alpine Linux Port](https://openjdk.org/jeps/386)
    - 要是將 JDK 移植到 Alpine Linux 和其他使用 musl 作為主要 C 函式庫的 Linux 發行版, 支持 x64 和 AArch64 架構
    - 旨在縮小 Java 運行時的體積, 特別是在雲端部署/微服務和容器環境中有利於減少映像檔大小
    - 它還使用了 `jlink` 來創建更小的 Java 執行環境, 並且進行了相關測試來確保穩定性與性能, 這對於嵌入式系統和雲端應用尤為重要
    - **Alpine Linux** : 是一個基於安全性和輕量化的 Linux 發行版，特別適合容器化應用，因為其基礎系統體積非常小
    - **musl** : 是 C 函式庫的一種替代方案, 專為輕量化和高效能而設計, 替代傳統的 GNU C Library (glibc), musl 提供了一個更簡潔的運行時環境, 與 Alpine
      Linux 一起使用, 特別適合在資源有限的系統或容器中執行應用程式
- [387 : Elastic Metaspace](https://openjdk.org/jeps/387)
    - 實現 **彈性 Metaspace**, 以更快將未使用的 HotSpot 類別元數據記憶體歸還給作業系統, 減少 Metaspace 的佔用空間, 並簡化其代碼, 降低維護成本
    - 它通過引入基於 **buddy allocation** 的分配方式減少內存碎片增加靈活性, 能按需求分配和釋放記憶體, 從而提升內存利用率, 特別是在類別加載與卸載頻繁的應用中
- [389 : Foreign Linker API (Incubator)](https://openjdk.org/jeps/389)
    - 提議引入 Foreign Linker API (孵化器模式), 允許 Java 程式以純 Java 方式進行與本機代碼(如 C 函式庫)的互操作, 替代傳統的 Java Native Interface (
      JNI)
    - 這個 API 簡化了與外部函式的綁定過程, 提高了可用性與效能, 並與 Foreign Memory API 一起使用, 此 API 旨在提供對 C 函式庫的高效支援，未來將擴展到其他語言的函式
- [393 : Foreign-Memory Access API (Third Incubator)](https://openjdk.org/jeps/393)
    - 引入了 Foreign-Memory Access API(第三次孵化), 允許 Java 程式安全且高效地存取 Java 堆外的外部記憶體
    - 此 API 提供了三個主要抽象 : `MemorySegment` `MemoryAddress` `MemoryLayout`, 用於更好地管理和存取記憶體
    - 透過這個 API, 開發者能更靈活地操作本地內存, 避免使用不安全的 API (如 `sun.misc.Unsafe`), 並提高效能和記憶體使用控制
- [396 : Strongly Encapsulate JDK Internals by Default](https://openjdk.org/jeps/396)
    - 將 JDK 內部 API 預設進行強封裝, 限制應用程式對 JDK 非公開元素的存取, 從而提升 Java 平台的安全性和可維護性, 此變更要求開發者遷移到標準 API,
      避免使用反射等方法存取內部元素
    - 使用 `--illegal-access` 命令選項可控制封裝行為, 但在未來將逐步移除該選項, 最終達到完全封裝內部 API 的目標
- [397 : Sealed Classes (Second Preview)](https://openjdk.org/jeps/397)
  - 引入了 **封閉類別**(Sealed Classes)功能, 允許開發者限制哪些類別或介面可以繼承或實作指定的父類別或介面
  - 這種設計可以讓 API 的作者更嚴密地控制繼承層次, 確保系統的穩定性, 並提高編譯時的安全性

### 不知道沒差但知道了也沒差的版本特性

- [347 : Enable C++14 Language Features](https://openjdk.org/jeps/347)
    - 允許在 JDK 的 C++ 原始碼中使用 C++14 語言特性, 尤其針對 HotSpot 編譯器進行特定指導
    - 目標是將 C++ 語言標準從 C++98/03 升級至 C++14, 改善代碼可讀性與開發效率, 並保持與不同編譯器版本的兼容性
    - 該提案不影響非 HotSpot 的 JDK 代碼, 且要求在所有平台上指定最低支持的編譯器版本
- [357 : Migrate from Mercurial to Git](https://openjdk.org/jeps/357)
    - 將 OpenJDK 的原始碼管理系統從 Mercurial 遷移至 Git, 這項提案旨在提高效能並改善代碼庫的操作體驗
- [369 : Migrate to GitHub](https://openjdk.org/jeps/369)
    - 將 OpenJDK 的 Git 儲存庫遷移到 GitHub, 這個過程將所有單一儲存庫的 OpenJDK 專案(包括 JDK 功能和更新版本)轉移至 GitHub 託管
    - 目標是提升效能、加強工具整合/減少克隆和拉取的時間, 並利用 GitHub 的 API 提供更多自動化功能
    - 此提案還允許開發者使用多種工作流程來互動, 包括命令行工具/網頁瀏覽器/IDE 插件支援
- [388 : Windows/AArch64 Port](https://openjdk.org/jeps/388)
    - 將 JDK 移植到 Windows/AArch64 平台, 以支援新興的消費級和伺服器級 ARM64 硬體, 滿足用戶需求
    - 該移植工作包含了模板解釋器/C1 和 C2 JIT 編譯器, 以及多種垃圾回收器(如 G1、ZGC 等)
    - 此舉有助於在 Windows 10 和 Windows Server 2016 等系統上運行 Java, 並增強 ARM64 架構的支援, 特別在輕量型裝置上具備更好的效能

---

