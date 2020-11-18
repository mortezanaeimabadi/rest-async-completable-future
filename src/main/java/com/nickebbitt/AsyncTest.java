package com.nickebbitt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@EnableAsync
public class AsyncTest {
    private String reverseString(String s) {
        log.info("Start reversing string");
        try {
            log.info("sleep start for 3 second...");
            Thread.sleep(3000);
            log.info("sleep end for 3 second...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String reversed = new StringBuilder(s).reverse().toString();
        log.info("Completed reversing string");
        return reversed;
    }

    @Async
    public CompletableFuture<String> processCompletedFutureWithJoin() {
        log.info("Request received in @Async : "+Thread.currentThread().getName());
        CompletableFuture<String> future = CompletableFuture.completedFuture(reverseString("Hello World!"));
        future.join();
        log.info("Request done in @Async: "+Thread.currentThread().getName());
        return future;

    }

    @Async
    public CompletableFuture<String> processSupplyAsyncWithJoin() {
        log.info("Request received in @Async : "+Thread.currentThread().getName());
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(()-> reverseString("Hello World!"));
        future.join();
        log.info("Request done in @Async: "+Thread.currentThread().getName());
        return future;
    }

    @Async
    public CompletableFuture<String> processCompletedFutureWithoutJoin() {
        log.info("Request received in @Async : "+Thread.currentThread().getName());
        CompletableFuture<String> future = CompletableFuture.completedFuture(reverseString("Hello World!"));
       // future.join();
        log.info("Request done in @Async: "+Thread.currentThread().getName());
        return future;

    }

    @Async
    public CompletableFuture<String> processSupplyAsyncWithoutJoin() {
        log.info("Request received in @Async : "+Thread.currentThread().getName());
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(()-> reverseString("Hello World!"));
        //future.join();
        log.info("Request done in @Async: "+Thread.currentThread().getName());
        return future;
    }


}
