package com.scheduling.example.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*@Component ano is required because Spring's scheduling functionality relies on managed beans.*/
@Component
@Slf4j
public class SchedulingDemo {

    /*Scheduling is mainly useful when working with cron jobs, you can define some fixedDelay
     * for which it will execute at each 10sec(ex) Whenever working with this
     * annotate @EnableScheduling on top of main class
     * fixedDelay = 10000 -> for each 10sec this meth will execute
     *
     * initialDelay = 5000 -> starts to work after 5sec of startup*/
    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void performTask(){
        log.info("performing task at: {}", LocalDateTime.now());
    }
}

