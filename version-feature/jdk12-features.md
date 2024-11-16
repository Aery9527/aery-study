<a id="head"></a>

# [form OpenJDK - JDK12](https://openjdk.org/projects/jdk/12)

#### <a id="head1"></a> [不能不知道的版本特性](#不能不知道的版本特性)

#### <a id="head2"></a> [不知道沒差但知道了會變厲害的版本特性](#不知道沒差但知道了會變厲害的版本特性)

1. [189 : Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)](#189--shenandoah--a-low-pause-time-garbage-collector--experimental-)
1. [230 : Microbenchmark Suite](#230--microbenchmark-suite)
1. [325 : Switch Expressions (Preview)](#325--switch-expressions--preview-)
1. [344 : Abortable Mixed Collections for G1](#344--abortable-mixed-collections-for-g1)
1. [346 : Promptly Return Unused Committed Memory from G1](#346--promptly-return-unused-committed-memory-from-g1)

#### <a id="head3"></a> [不知道沒差但知道了也沒差的版本特性](#不知道沒差但知道了也沒差的版本特性)

1. [334 : JVM Constants API](#334--jvm-constants-api)
1. [340 : One AArch64 Port, Not Two](#340--one-aarch64-port-not-two)
1. [341 : Default CDS Archives](#341--default-cds-archives)

---

#### [不能不知道的版本特性](#head1)

---

#### [不知道沒差但知道了會變厲害的版本特性](#head2)

###### [189 : Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)](https://openjdk.org/jeps/189)

- 介紹了一種名為 Shenandoah 的新垃圾收集(GC)算法, 旨在通過與運行中的Java線程同時進行疏散工作來減少GC暫停時間
- 這個算法的特點是暫停時間不隨堆大小變化, 無論堆是 200MB 還是 200GB 都能保持一致的暫停時間
- Shenandoah 通過增加每個 Java 對象的間接指針來實現，在 Java 線程運行時對堆進行壓縮. 此外, 標記和壓縮操作是同步進行的, 僅需要在掃描線程棧以更新對象圖的根部時暫停
  Java 線程

###### [230 : Microbenchmark Suite](https://openjdk.org/jeps/230)

- 在 JDK 源碼中添加一套基本的微基準測試套件, 方便開發者運行現有的微基準測試並創建新的測試
- 該套件基於 Java 微基準測試架構(JMH), 旨在提供穩定且調整過的基準測試, 以進行持續的性能測試
- JMH 與 Junit 雖然都是測試, 但目的不同. JMH 專注於性能測試, 而 Junit 則專注於業務邏輯的正確性測試
- 可參考 [JHM_use_case](./src/test/java/org/aery/study/JHM_use_case.java)

###### [325 : Switch Expressions (Preview)](https://openjdk.org/jeps/325)

- 引入 "Switch Expressions" (預覽特性), 允許 switch 也可用作表達式, 此變更包括簡化作用域和控制流程, 解決了傳統 switch 的問題
- 已被 JEP 354 (jdk13) 取代

###### [344 : Abortable Mixed Collections for G1](https://openjdk.org/jeps/344)

- 改進 G1 垃圾收集器, 使得在混合收集中能夠中途取消（abort）操作, 以避免超過預設的暫停時間
- 當 G1 判斷當前收集可能會超出時間限制時, 它將收集分為強制部分和可選部分, 只有時間允許才會進行可選部分的收集
- 這將提高 G1 的效率和靈活性, 特別是在應用行為變動較大的情況下

###### [346 : Promptly Return Unused Committed Memory from G1](https://openjdk.org/jeps/346)

- 提出了改進 G1 垃圾收集器, 使其能夠在應用程式閒置時, 自動將未使用的 Java 堆記憶體返還給作業系統
- 這樣在容器環境中, 有助於減少不必要的資源佔用和成本, 這個功能對於提升應用程式效能和資源使用效率非常有用, 特別是當應用程式在長時間閒置時

---

#### [不知道沒差但知道了也沒差的版本特性](#head3)

###### [334 : JVM Constants API](https://openjdk.org/jeps/334)

- 提出了 JVM 常量 API, 用於在 `java.lang.invoke.constant` 包中提供一組新的符號引用類型, 這些類型可描述 JVM 常量池中的加載常量
- 此 API 的目的是簡化字節碼解析、生成、編譯器和工具的開發, 允許在不需要類加載的情況下操作這些常量

###### [340 : One AArch64 Port, Not Two](https://openjdk.org/jeps/340)

- 將 JDK 中的 64 位 ARM 處理器移植版本簡化為單一的 AArch64 實現，移除舊的 arm64 移植版本
- 這樣減少了維護兩個相似移植版本的重複工作, 並集中資源在 AArch64 移植版本上, 提升其穩定性和效能, 同時保留了 32 位 ARM 移植版本的支持

###### [341 : Default CDS Archives](https://openjdk.org/jeps/341)

- 提出了在 JDK 構建過程中生成默認的 Class Data Sharing (CDS) 檔案, 以改進啟動時間並消除用戶手動執行 `-Xshare:dump` 的需求
- 這將在 64 位平台上自動啟用 CDS 功能, 從而提升應用程式的啟動效能, 尤其是在預設情況下啟動 Java 應用時
- *Class Data Sharing (CDS) 在 jdk10 的 JEP310 有被擴展*
