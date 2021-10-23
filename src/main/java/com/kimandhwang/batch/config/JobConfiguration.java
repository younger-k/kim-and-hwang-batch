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
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create on 2021/10/24.
 * create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author Yeonha Kim
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Configuration
@Slf4j
public class JobConfiguration {

  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;

  public JobConfiguration(
          JobBuilderFactory jobBuilderFactory,
          StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public Job SoccerPlayerJob() {
    return this.jobBuilderFactory.get("SoccerPlayerJob")
            .incrementer(new RunIdIncrementer())
            .start(this.SoccerPlayerStep())
            .build();
  }

  @Bean
  public Step SoccerPlayerStep() {
    return this.stepBuilderFactory.get("SoccerPlayerStep")
            .<Player, Player>chunk(100)
            .reader(playerCsvReader())
//            .processor()
            .writer(playerMongoWriter())
            .build();
  }

  private ItemReader<? extends Player> playerCsvReader() {
    return null;
  }

  private ItemWriter<? super Player> playerMongoWriter() {
    return null;
  }


}