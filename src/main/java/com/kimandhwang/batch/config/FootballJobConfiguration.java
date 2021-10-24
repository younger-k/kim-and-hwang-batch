/*
 * Created by Yeonha Kim on 2021/10/24
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <jongsang@bigin.io>, 2021/10/24
 */
package com.kimandhwang.batch.config;

import com.kimandhwang.batch.domain.Player;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * create on 2021/10/24. create by IntelliJ IDEA.
 *
 * <p> Football Manager 2017 게임의 선수 dataset {@code csv} 파일을 DB에 저장. </p>
 *
 * @author Yeonha Kim
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Slf4j
public class FootballJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;

  public FootballJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      EntityManagerFactory entityManagerFactory
  ) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Bean
  public Job footballPlayerJob() throws Exception {
    return this.jobBuilderFactory.get("footballPlayerJob")
        .incrementer(new RunIdIncrementer())
        .start(this.footballPlayerStep())
        .listener(jobListener())
        .build();
  }

  private JobExecutionListener jobListener() {
    return new JobExecutionListener() {
      @Override
      public void beforeJob(JobExecution jobExecution) {
        log.info("Start footballPlayerJob ...");
      }

      @Override
      public void afterJob(JobExecution jobExecution) {
        log.info("footballPlayerJob has done with: {}", jobExecution.getExitStatus().getExitCode());
      }
    };
  }

  @Bean
  public Step footballPlayerStep() throws Exception {
    return this.stepBuilderFactory.get("footballPlayerStep")
        .<Player, Player>chunk(100)
        .reader(playerReader())
//            .processor()
        .writer(playerWriter())
        .listener(stepChunkListener())
        .build();
  }

  private ChunkListener stepChunkListener() {
    return new ChunkListener() {
      @Override
      public void beforeChunk(ChunkContext context) {
      }

      @Override
      public void afterChunk(ChunkContext context) {
        final int count = context.getStepContext().getStepExecution().getReadCount();
        log.info("{} items done.", count);
      }

      @Override
      public void afterChunkError(ChunkContext context) {
      }
    };
  }


  private ItemReader<? extends Player> playerReader() throws Exception {
    final String delimiter = DelimitedLineTokenizer.DELIMITER_COMMA;
    DefaultLineMapper<Player> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(delimiter);

    String fields = "UID,Name,NationID,Born,Age,Height,Weight,PositionsDesc";
    String[] fieldsArr = fields.split(delimiter);
    tokenizer.setNames(fieldsArr);
    lineMapper.setLineTokenizer(tokenizer);

    // 필드 매퍼 세팅
    lineMapper.setFieldSetMapper(fieldSet -> {
      long uid = fieldSet.readLong("UID");
      String name = fieldSet.readString("Name");
      long nationId = fieldSet.readLong("NationID");
      String born = fieldSet.readString("Born");
      int age = fieldSet.readInt("Age");
      int height = fieldSet.readInt("Height");
      int weight = fieldSet.readInt("Weight");
      String positionsDesc = fieldSet.readString("PositionsDesc");

      return new Player(uid, name, nationId, born, age, height, weight, positionsDesc);
    });

    FlatFileItemReader<Player> itemReader = new FlatFileItemReaderBuilder<Player>()
        .name("playerReader")
        .encoding("UTF-8")
        .resource(new ClassPathResource("csv/dataset-filtered.csv"))
        .linesToSkip(1)
        .lineMapper(lineMapper)
        .build();
    itemReader.afterPropertiesSet();

    return itemReader;
  }

  private ItemWriter<? super Player> playerWriter() throws Exception {
    JpaItemWriter<Player> itemWriter = new JpaItemWriterBuilder<Player>()
        .entityManagerFactory(entityManagerFactory)
        .build();
    itemWriter.afterPropertiesSet();

    return itemWriter;
  }
}
