package com.stk.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchDemoApplication {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	public static String[] tokens = new String[] {"order_id", "first_name", "last_name", "email", "cost", "item_id", "item_name", "ship_date"};

	@Bean
	public ItemReader<Order> itemReader() {
		FlatFileItemReader<Order> itemReader = new FlatFileItemReader<>();
//		skip header
		itemReader.setLinesToSkip(1);
//		location from where to read
		itemReader.setResource(new ClassPathResource("orders.csv"));
//		we need something to map data from csv to order pojo, for this we use linemapper
		DefaultLineMapper<Order> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//		letting know list of headers to map to
		lineTokenizer.setNames(tokens);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(new OrderFieldSetMapper());
		itemReader.setLineMapper(lineMapper);
		return itemReader;
	}

	@Bean
	public Step chunkBasedStep() {
		return this.stepBuilderFactory.get("chunkBasedStep")
				.<Order, Order>chunk(3)
				.reader(itemReader())
				.writer(new ItemWriter<Order>() {
					@Override
					public void write(List<? extends Order> list) throws Exception {
						System.out.println("Received list of size: " + list.size());
						list.forEach(System.out::println);
					}
				})
				.build();
	}

	@Bean
	public Job job(){
		return this.jobBuilderFactory.get("job")
				.start(chunkBasedStep())
				.build();
	}



	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemoApplication.class, args);
	}

}
