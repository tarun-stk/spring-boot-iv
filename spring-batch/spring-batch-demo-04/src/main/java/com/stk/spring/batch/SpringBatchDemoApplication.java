package com.stk.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchDemoApplication {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Value("${got.lost}")
	private boolean gotLost;

	@Bean
	public StepExecutionListener selectFlowersListener(){
		return new FlowersSelectionStepExecutionListener();
	}

	@Bean
	public Step selectFlowersStep(){
		return this.stepBuilderFactory.get("selectFlowersStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Gathering flowers for order");
				return RepeatStatus.FINISHED;
			}
		})
//				adding listener, which will execute both b4 and after execution logic
				.listener(selectFlowersListener())
				.build();
	}

	@Bean
	public Step removeThornsStep(){
		return this.stepBuilderFactory.get("removeThornsStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Remove thorns from roses");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step arrangeFlowersStep(){
		return this.stepBuilderFactory.get("arrangeFlowersStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Arrange flowers for the order");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Job prepareFlowers(){
		return this.jobBuilderFactory.get("prepareFlowerJobs")
				.start(selectFlowersStep())
					.on("TRIM_REQUIRED").to(removeThornsStep()).next(arrangeFlowersStep())
				.from(selectFlowersStep()).on("NO_TRIM_REQUIRED").to(arrangeFlowersStep())
				.end().build();
	}

	@Bean
	public JobExecutionDecider decider(){
		return new DeliveryDecider();
	}

	@Bean
	public JobExecutionDecider correctPackageDecider(){
		return new CorrectPackageDecider();
	}

	@Bean
	public Job deliverPackageJob(){

		return this.jobBuilderFactory.get("deliverPackageJob").start(packageItemStep())
				.next(driveToAddressStep())
//				below fails the job, instead of marking job as completed
				.on("FAILED").fail()
				.from(driveToAddressStep())
				.on("*").to(decider())
				.on("PRESENT").to(givePackageToCustomerStep())
				.next(correctPackageDecider())
				.on("CORRECT").to(thankCustomerStep())
				.from(correctPackageDecider())
				.on("NOT_CORRECT").to(giveRefundToCustomerStep())
				.from(decider())
				.on("NOT_PRESENT").to(leavePackageAtDoorStep())
				.end()
				.build();
	}

	@Bean
	public Step giveRefundToCustomerStep() {
		return this.stepBuilderFactory.get("giveRefundToCustomerStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Giving refund to customer");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step thankCustomerStep() {
		return this.stepBuilderFactory.get("thankCustomerStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Thank yOu customer");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step leavePackageAtDoorStep() {
		return this.stepBuilderFactory.get("leavePackageAtDoorStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Package left at door");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step storePackageStep() {
		return this.stepBuilderFactory.get("storePackageStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Package stored");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step givePackageToCustomerStep() {
		return this.stepBuilderFactory.get("givePackageToCustomerStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("given package to the customer step");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step driveToAddressStep() {
		return this.stepBuilderFactory.get("driveToAddressStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				if(gotLost){
					throw new RuntimeException("Got lost while driving");
				}
				System.out.println("Successfully arrived at the address");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step packageItemStep() {
		return this.stepBuilderFactory.get("packageItemStep").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				String item = chunkContext.getStepContext().getJobParameters().get("item").toString();
				String date = chunkContext.getStepContext().getJobParameters().get("run.date").toString();
				System.out.println(String.format("The %s has been packaged on %s", item, date));
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemoApplication.class, args);
	}

}
