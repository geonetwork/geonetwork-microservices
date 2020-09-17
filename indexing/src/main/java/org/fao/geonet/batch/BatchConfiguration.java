package org.fao.geonet.batch;

import java.util.Collections;
import javax.sql.DataSource;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.domain.Metadata_;
import org.fao.geonet.indexing.model.IndexRecord;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

  @Autowired
  DataSource dataSource;

  @Autowired
  MetadataRepository metadataRepository;

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Override
  protected JobRepository createJobRepository() throws Exception {
    MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
    factory.setTransactionManager(new ResourcelessTransactionManager());
    return factory.getObject();
  }

  /**
   * Read all metadata record from database.
   */
  @Bean(name = "metadataReader")
  @StepScope
  public RepositoryItemReader<Metadata> metadataReader() {
    RepositoryItemReader<Metadata> reader = new RepositoryItemReader<>();
    reader.setRepository(metadataRepository);
    reader.setMethodName("findAll");
    reader.setSort(Collections.singletonMap(
        Metadata_.id.getName(),
        Sort.Direction.DESC));
    return reader;
  }

  /**
   * Convert a metadata record to an index record.
   */
  @Bean
  public DbRecordIndexItemProcessor processor() {
    return new DbRecordIndexItemProcessor();
  }

  /**
   * How to execute the job.
   */
  @Bean
  public TaskExecutor taskExecutor() {
    SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("gn-batch");
    taskExecutor.setConcurrencyLimit(3);
    return taskExecutor;
  }

  /**
   * Indexing step.
   */
  @Bean
  public Step indexStep() {
    return stepBuilderFactory.get("indexingStep")
        .<Metadata, IndexRecord>chunk(10)
        .reader(metadataReader())
        .processor(processor())
        .writer(new NoOpItemWriter())
        // Multithread
        .taskExecutor(taskExecutor())
        .build();
  }

  /**
   * Indexing job.
   */
  @Bean
  public Job indexRecordsJob(Step indexStep) {
    return jobBuilderFactory.get("indexRecordsJob")
        .flow(indexStep)
        .end()
        .build();
  }
}