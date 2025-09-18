package com.stk.spring.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

public class CorrectPackageDecider implements JobExecutionDecider {

    @Value("${is.correct.package}")
    private boolean isCorrectPackage;
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String result = isCorrectPackage ? "CORRECT": "NOT_CORRECT";
        System.out.println("Package decider result is: " + result);
        return new FlowExecutionStatus(result);
    }
}
