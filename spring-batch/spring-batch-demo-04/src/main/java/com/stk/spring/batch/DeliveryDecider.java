package com.stk.spring.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public class DeliveryDecider implements JobExecutionDecider {
    @Value("${is.present}")
    private boolean isPresent;
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String result = isPresent ? "PRESENT": "NOT_PRESENT";
        System.out.println("Decider result is: " + result);
        return new FlowExecutionStatus(result);
    }
}
