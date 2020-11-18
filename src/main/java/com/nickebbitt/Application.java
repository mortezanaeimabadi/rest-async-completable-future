package com.nickebbitt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@SpringBootApplication
@RestController
@Slf4j
public class Application {

    static final String RESULT = "Output Result";

    @Autowired
    AsyncTest asyncTest;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
	}

    private String processRequest() {
        log.info("Start processing request.."+Thread.currentThread().getName());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Completed processing request..."+Thread.currentThread().getName());
        return RESULT;
    }

    private String reverseString(String s) {
        log.info("Start reversing string");
        String reversed = new StringBuilder(s).reverse().toString();
        log.info("Completed reversing string");
        return reversed;
    }

    @RequestMapping(path = "/sync", method = RequestMethod.GET)
    public String getValueSync() {

        log.info("Request received");

        return processRequest();

    }


    @RequestMapping(path = "/asyncCompletable", method = RequestMethod.GET)
    public CompletableFuture<String> getValueAsyncUsingCompletableFuture() {
        log.info("Request received"+Thread.currentThread().getName());
        CompletableFuture<String> future = CompletableFuture.completedFuture(processRequest());
        future.join();
        return future;
        //log.info("Servlet thread released");
        //return completableFuture;
    }

    @RequestMapping(path = "/asyncCompletableComposed", method = RequestMethod.GET)
    public CompletableFuture<String> getValueAsyncUsingCompletableFutureComposed() {
        log.info("Request received"+Thread.currentThread().getName());

       // CompletableFuture<String> stringCompletableFuture =
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(this::processRequest);
        future.join();
        return future;
        //.thenApplyAsync(this::reverseString);

//        log.info("Servlet thread released");
//
//        return stringCompletableFuture;

    }


    @RequestMapping(path = "/processCompletedFutureWithJoin", method = RequestMethod.GET)
    public CompletableFuture<String> processCompletedFuture() {
        log.info("Request received in controller thread:" + Thread.currentThread().getName());
        CompletableFuture<String> futureResult = asyncTest.processSupplyAsyncWithJoin();
        log.info("Request done in controller thread:" + Thread.currentThread().getName());
        return futureResult;

    }

    @RequestMapping(path = "/processSupplyAsyncWithJoin", method = RequestMethod.GET)
    public CompletableFuture<String> processSupplyAsync() {
        log.info("Request received in controller thread:" + Thread.currentThread().getName());
        CompletableFuture<String> futureResult = asyncTest.processSupplyAsyncWithJoin();
        log.info("Request done in controller thread:" + Thread.currentThread().getName());
        return futureResult;
    }

    @RequestMapping(path = "/processCompletedFutureNoJoin", method = RequestMethod.GET)
    public CompletableFuture<String> processCompletedFutureNoJoin() {
        log.info("Request received in controller thread:" + Thread.currentThread().getName());
        CompletableFuture<String> futureResult = asyncTest.processCompletedFutureWithoutJoin();
        log.info("Request done in controller thread:" + Thread.currentThread().getName());
        return futureResult;

    }

    @RequestMapping(path = "/processSupplyAsyncNoJoin", method = RequestMethod.GET)
    public CompletableFuture<String> processSupplyAsyncNoJoin() {
        log.info("Request received in controller thread:" + Thread.currentThread().getName());
        CompletableFuture<String> futureResult = asyncTest.processSupplyAsyncWithoutJoin();
        log.info("Request done in controller thread:" + Thread.currentThread().getName());
        return futureResult;
    }

}
