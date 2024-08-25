## [form OpenJDK - JDK14](https://openjdk.org/projects/jdk/14)

here is [[study code](./src/test/java/org/aery/study/jdk14)]

---

### 不能不知道的版本特性

- [349 : JFR Event Streaming](https://openjdk.org/jeps/349)
  - [study-code](./src/test/java/org/aery/study/jdk14/JEP349_JFR_Event_Streaming.java) 
  - 提出 Java Flight Recorder (JFR) 事件流功能, 方便從 jfr 檔案讀取事件處理
  - jfr 在 jdk11 的 jep328 有提到, 但是只能用 Java Mission Control (JMC) 來開啟 .jfr 檔案

### 不知道沒差但知道了會變厲害的版本特性

- [305 : Pattern Matching for instanceof (Preview)](https://openjdk.org/jeps/305)
    - 提出了 Java 語言中對 instanceof 操作符的模式匹配功能, 允許在檢查對象類型的同時直接將變量綁, 進而簡化代碼
  ```
    if (obj instanceof String str) {
      System.out.println(str.toUpperCase()); // 直接使用 str 變量，無需顯式轉型
    }
  ```
- [343 : Packaging Tool (Incubator)](https://openjdk.org/jeps/343)
  - 介紹了一個名為 jpackage 的工具, 旨在為Java應用程式創建自包含的本機安裝包
  - 它支持不同操作系統的原生包格式，如 Windows 的 msi 和 exe, macOS 的 pkg 和 dmg, 以及 Linux 的 deb 和 rpm
  - 該工具基於 JavaFX 的 javapackager, 提供簡單的命令行界面, 但不支持 Java Web Start 和 JavaFX 相關功能

### 不知道沒差但知道了也沒差的版本特性

- [345 : NUMA-Aware Memory Allocation for G1](https://openjdk.org/jeps/345)
  - 提出了在 G1 垃圾收集器中實現 NUMA 感知的記憶體分配, 以改善大規模機器上的性能
  - NUMA(非均勻記憶體存取) : 技術考慮了多插槽機器中不同插槽之間的記憶體訪問延遲, 此功能僅適用於 Linux 系統

---




- [352 : Non-Volatile Mapped Byte Buffers](https://openjdk.org/jeps/352)
- [358 : Helpful NullPointerExceptions](https://openjdk.org/jeps/358)
- [359 : Records (Preview)](https://openjdk.org/jeps/359)
- [361 : Switch Expressions (Standard)](https://openjdk.org/jeps/361)
- [362 : Deprecate the Solaris and SPARC Ports](https://openjdk.org/jeps/362)
- [363 : Remove the Concurrent Mark Sweep (CMS) Garbage Collector](https://openjdk.org/jeps/363)
- [364 : ZGC on macOS](https://openjdk.org/jeps/364)
- [365 : ZGC on Windows](https://openjdk.org/jeps/365)
- [366 : Deprecate the ParallelScavenge + SerialOld GC Combination](https://openjdk.org/jeps/366)
- [367 : Remove the Pack200 Tools and API](https://openjdk.org/jeps/367)
- [368 : Text Blocks (Second Preview)](https://openjdk.org/jeps/368)
- [370 : Foreign-Memory Access API (Incubator)](https://openjdk.org/jeps/370)
