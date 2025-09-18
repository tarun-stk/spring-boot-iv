package com.stk.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchDemoApplication {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job deliverPackageJob(){
		return this.jobBuilderFactory.get("deliverPackageJob").start(packageItemStep()).build();
	}

	@Bean
	public Step packageItemStep() {
		return this.stepBuilderFactory.get("packageItemStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("The item has been packaged");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemoApplication.class, args);
	}

}
