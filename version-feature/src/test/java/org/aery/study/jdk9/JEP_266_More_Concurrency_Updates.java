package org.aery.study.jdk9;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class JEP_266_More_Concurrency_Updates {

    @Test
    void CompletableFuture() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // 模擬長時間運行的任務
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Hello, World!";
        });

        future.thenAccept(System.out::println); // 當任務完成時, 會執行此方法
        CompletableFuture<Object> completableFuture = future.newIncompleteFuture(); // 產生一個新的未完成的CompletableFuture
        Executor executor = future.defaultExecutor(); // 取得預設的執行緒池
        CompletableFuture<String> a = future.copy(); // 複製一個新的CompletableFuture
        CompletionStage<String> b = future.minimalCompletionStage(); // 轉換為 CompletionStage
        CompletableFuture<String> c = future.completeAsync(() -> "Hello, World!"); // 立即完成異步任務
        CompletableFuture<String> d = future.completeAsync(() -> "Hello, World!", executor); // 立即完成異步任務
        CompletableFuture<String> e = future.orTimeout(1, TimeUnit.SECONDS); // 設定超時時間
        CompletableFuture<String> f = future.completeOnTimeout("Hello, World!", 1, TimeUnit.SECONDS); // 設定超時時間
        Executor h = future.delayedExecutor(1, TimeUnit.SECONDS); // 設定延遲時間
        Executor i = future.delayedExecutor(1, TimeUnit.SECONDS, executor); // 設定延遲時間

        // 主線程等待異步任務完成
        try {
            future.get(); // 阻塞等待結果
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }
    }

}
