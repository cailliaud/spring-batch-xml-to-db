package com.example.demo.configuration;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import com.example.demo.bean.RtfeUser;
import com.example.demo.bean.User;
import com.example.demo.exception.BatchExceptionHandler;
import com.example.demo.listener.JobCompletionNotificationListener;
import com.example.demo.processor.UserProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;


  @Bean
  public StaxEventItemReader<RtfeUser> reader() {

    StaxEventItemReader<RtfeUser> staxEventItemReader = new StaxEventItemReader<>();
    staxEventItemReader.setResource(new ClassPathResource("/rtfe.xml"));
    staxEventItemReader.setFragmentRootElementName("user");
    
    Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
    studentMarshaller.setClassesToBeBound(RtfeUser.class);
    staxEventItemReader.setUnmarshaller(studentMarshaller);
    
    return staxEventItemReader;

  }

  @Bean
  public UserProcessor processorRtfeUserToUser() {
    return new UserProcessor();
  }
  

  
  @Bean
  public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
      return new JdbcBatchItemWriterBuilder<User>()
          .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
          .sql("INSERT INTO users (id,email,first_name, last_name) VALUES ( :id, :email, :firstName, :lastName)")
          .dataSource(dataSource)
          .build();
  }
  
  
  @Bean
  public Step step1(JdbcBatchItemWriter<User> writer) {
      return stepBuilderFactory.get("Extract XML -> Transform RFTE -> Save H2")
          .<RtfeUser, User> chunk(2)
          .reader(reader())
          .processor(processorRtfeUserToUser())
          .writer(writer)
          .exceptionHandler(new BatchExceptionHandler())
          .build();
  }
  
  @Bean
  public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
      return jobBuilderFactory.get("importUserJob")
          .incrementer(new RunIdIncrementer())
          .listener(listener)
          .flow(step1)
          .end()
          .build();
  }

  

}
