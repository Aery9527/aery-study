## Java 9 Platform Module System (JPMS)

| subproject                                                   | description                                                                                                                                                                                                                                                                                                                                              |
|--------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [java-module](../java-module/src/main/java/)                 | 可以理解為應用程式入口, 基本透過 `ServiceLoader` 載入 [java-module-api:TextGraphicsService](../java-module-api/src/main/java/org/aery/study/java/module/api/TextGraphicsService.java) 的實作來展示解耦合的過程                                                                                                                                                                        |
| [java-module-api](../java-module-api/src/main/java/)         | 可以理解應用程式所有介面, 入口與實作由此模組耦合                                                                                                                                                                                                                                                                                                                                |
| [java-module-phantom](../java-module-phantom/src/main/java/) | 同 [java-module](../java-module/src/main/java/) 為應用程式入口, 但其實由 [java-module:ModuleLayerStudy_LoadPhantomModule](../java-module/src/main/java/org/aery/study/java/module/ModuleLayerStudy_LoadPhantomModule.java) 載入, 用於展示 `ModuleLayer` 概念                                                                                                                 | 
| [java-module-utils](../java-module-utils/src/main/java/)     | 放整個模組系列的公用程式                                                                                                                                                                                                                                                                                                                                             |
| [java-module-x](../java-module-x/src/main/java/)             | [java-module-api:TextGraphicsService](../java-module-api/src/main/java/org/aery/study/java/module/api/TextGraphicsService.java) 實作之一, 由 [java-module](../java-module/src/main/java/) gradle相依 (class-path)                                                                                                                                               |
| [java-module-y](../java-module-y/src/main/java/)             | [java-module-api:TextGraphicsService](../java-module-api/src/main/java/org/aery/study/java/module/api/TextGraphicsService.java) 實作之一, 由 [java-module](../java-module/src/main/java/) gradle相依 (class-path)                                                                                                                                               |
| [java-module-z](../java-module-z/src/main/java/)             | [java-module-api:TextGraphicsService](../java-module-api/src/main/java/org/aery/study/java/module/api/TextGraphicsService.java) 實作之一, 不由 [java-module](../java-module/src/main/java/) gradle相依 (class-path), 而是透過 [java-module:PluginStudy](../java-module/src/main/java/org/aery/study/java/module/PluginStudy.java) 動態載入 module 使用, 用於展示 **plugin** 概念 |

- java9之前的jdk是單一的巨大架構, 有臃腫(對小型應用來說), 複雜性與耦合性(不同部件之間耦合度高，不易獨立發展)等問題
- 此架構目標是透過明確的模組邊界與依賴關係, 來解決上述問題

  | 優勢     | 說明                      |
  |--------|-------------------------|
  | 減少內存佔用 | 應用程序只需包含所需的模塊，而不是整個JDK。 |
  | 加快啟動時間 | 較小的模塊集合意味著更少的類需要加載和初始化。 |
  | 提升安全性  | 模塊邊界和依賴關係明確，使得代碼更加安全。   |
  | 更好的維護性 | 模塊化結構使得維護和更新更簡單、更清晰。    |
  | 促進應用分離 | 允許開發者構建和運行輕量級的Java應用。   |

- java9開始, jdk就被劃分為多個功能不同的模組, 透過 `module-info.java` 來定義模組的依賴關係
- 引入 **module-path** (基本上就同Maven依賴圖概念) 與 **class-path** 協同運作
- 於 compile 根目錄放一個 `module-info.java` 就可以將其定義為一個模組
- 在 `module-info.java` 裡可以使用 `import` 簡化名稱
- 未定義 `module-info.java` 則會被視為 **Unnamed Module (無名模組)**, 它可無視模組存取限制(這是為了相向相容做的取捨)
- **自動模組功能** 可將未模組化的 jar 自動變成一個 **Automatic Module (自動模組)**, 這是向下相容的機制
- **自動模組** 可以讀取所有模組, 包含 **Unnamed Module(無名模組)** (class-path上的class), 而一般模組無法存取無名模組
- 模組無法循環相依, 也就是 **module-path** 相依圖攤開來看, 必須是個tree不會有loop
- `module-info.java` 內使用的關鍵字

  | key word                              | description                                                            |
  |---------------------------------------|------------------------------------------------------------------------|
  | `[open] module {module-name}`         | 用來定義當前這個 module 的 module name, `open` 可以直接將整個 module 開放 refraction 權限            |
  | `opens {package} [to {package}]`      | 指定 package 可以被 refraction 操作, 在使用 spring 這種 IoC 框架時就非常重要               |
  | `exports {package} [to {package}]`    | 匯出指定的 package |
  | `requires [transitive] {module-name}` | 相依指定的 module, `transitive` 用來宣告傳遞性依賴                               |
  | `uses {interface}`                    | 指定某個 interface 為服務接口, 就可以透過 `ServiceLoader` 搜尋並載入                      |
  | `provides {interface} with {class}`   | 指定某個 class 為 interface 服務接口的實作, 就可以透過 `ServiceLoader` 搜尋並載入            |

- 上面的 `to {package}` 可以用來指定對象 module
- `exports … to` 限定匯出會讓 **module-path** 看起來像有loop, 但被限定匯出的 module 並非當前 module 的 `requires` 相依, 此情況是允許的
- `exports` 的 package 依然無法被 refraction 操作, 除非使用 `open` 關鍵字
- 一個 module 內可以 `exports` 多個 package, 但每個 `exports` 不包含其子 package
- 不同 module 內不可有相同 package, 就算沒有 `exports`, 也會拋出 `LayerInstantiationException`
- stacktrace 會在 package 前面多個 **/** 表達來自哪個 module, 方便識別該程式來自於哪個 module
- 透過 ClassLoader 取得的 module, 可動態設定 `requires` / `exports` / `uses` 等 `module-info.java` 內的設定 (有點像 reflection 操作)
- 可以讀取其他 module 內的 root 資源, 而其他就算是 `exports` 的 package 內的資源一樣會被封裝讀不到
- 可以讀取 META-INF 內的資源 (因為非合法package名稱)
- annotation 可以用於 module-info 上, 例如 `@Deprecated`
- `jlink` 可以製作最小的 java runtime 映像檔, 就是根據 `module-info.java` 匯出最小模組集合 (輕量化java程序)
- `jlink` 不會把 `providers` 自動納入映像檔, 需要在建立期主動想要納入的 `providers` 指定進去
- `jdeps` 是 jdk 自帶的模組分析工具
