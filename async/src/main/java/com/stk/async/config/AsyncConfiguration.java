package com.stk.async.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
/*@EnableAsync: This annotation is used to enable Spring's asynchronous method execution capability.
It allows methods annotated with @Async to run in a separate thread pool, enabling non-blocking operations.*/
@EnableAsync
public class AsyncConfiguration {

    @Bean("asyncTaskExecutor")
    public Executor asyncTaskExecutor(){
        /*ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();: This line creates a new instance of
        ThreadPoolTaskExecutor. ThreadPoolTaskExecutor is a Spring class that provides an implementation of the
         Executor interface, using a thread pool to manage asynchronous tasks.*/
        ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();
        /*taskExecutor.setCorePoolSize(4);: This sets the core number of threads in the pool to 4.
        The core pool size is the minimum number of threads that the pool will always keep alive.*/
        taskExecutor.setCorePoolSize(4);
        /*taskExecutor.setQueueCapacity(150);: This sets the capacity of the queue that holds tasks before they are
        executed. If all core threads are busy, tasks will be placed in the queue. Here, the queue can hold up to
        150 tasks. */
        taskExecutor.setQueueCapacity(150);
        /*taskExecutor.setMaxPoolSize(4);: This sets the maximum number of threads that the pool can contain.
        Even if the queue is full, the pool will not create more than this number of threads. Here,
        the maximum pool size is also set to 4, meaning the pool will not exceed 4 threads.*/
        taskExecutor.setMaxPoolSize(4);
        /*taskExecutor.setThreadNamePrefix("AsyncTaskThread-");: This sets the prefix for the names of threads
        created by the pool. This can be useful for debugging and logging purposes. Here, threads will have names
        like AsyncTaskThread-1, AsyncTaskThread-2, etc.*/
        taskExecutor.setThreadNamePrefix("AsyncTaskThread-");
        /*taskExecutor.initialize();: This method initializes the task executor, preparing it to accept tasks.
        It must be called after all the properties are set.*/
        taskExecutor.initialize();;
        return taskExecutor;
    }
}