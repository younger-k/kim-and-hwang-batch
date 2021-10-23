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
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  public JobConfiguration(
          JobBuilderFactory jobBuilderFactory,
          StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public Job soccerPlayerJob() throws Exception {
    return this.jobBuilderFactory.get("soccerPlayerJob")
            .incrementer(new RunIdIncrementer())
            .start(this.soccerPlayerStep())
            .build();
  }

  @Bean
  public Step soccerPlayerStep() throws Exception {
    return this.stepBuilderFactory.get("soccerPlayerStep")
            .<Player, Player>chunk(100)
            .reader(playerCsvReader())
//            .processor()
            .writer(playerMongoWriter())
            .build();
  }

  private ItemReader<? extends Player> playerCsvReader() throws Exception {
    DefaultLineMapper<Player> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

    // UID,Name,NationID,Born,Age,IntCaps,IntGoals,U21Caps,U21Goals,Height,Weight,AerialAbility,CommandOfArea,Communication,Eccentricity,Handling,Kicking,OneOnOnes,Reflexes,RushingOut,TendencyToPunch,Throwing,Corners,Crossing,Dribbling,Finishing,FirstTouch,Freekicks,Heading,LongShots,Longthrows,Marking,Passing,PenaltyTaking,Tackling,Technique,Aggression,Anticipation,Bravery,Composure,Concentration,Vision,Decisions,Determination,Flair,Leadership,OffTheBall,Positioning,Teamwork,Workrate,Acceleration,Agility,Balance,Jumping,LeftFoot,NaturalFitness,Pace,RightFoot,Stamina,Strength,Consistency,Dirtiness,ImportantMatches,InjuryProness,Versatility,Adaptability,Ambition,Loyalty,Pressure,Professional,Sportsmanship,Temperament,Controversy,PositionsDesc,Goalkeeper,Sweeper,Striker,AttackingMidCentral,AttackingMidLeft,AttackingMidRight,DefenderCentral,DefenderLeft,DefenderRight,DefensiveMidfielder,MidfielderCentral,MidfielderLeft,MidfielderRight,WingBackLeft,WingBackRight
    String fields = "UID,Name,NationID,Born,Age,IntCaps,IntGoals,U21Caps,U21Goals,Height,Weight,AerialAbility,CommandOfArea,Communication,Eccentricity,Handling,Kicking,OneOnOnes,Reflexes,RushingOut,TendencyToPunch,Throwing,Corners,Crossing,Dribbling,Finishing,FirstTouch,Freekicks,Heading,LongShots,Longthrows,Marking,Passing,PenaltyTaking,Tackling,Technique,Aggression,Anticipation,Bravery,Composure,Concentration,Vision,Decisions,Determination,Flair,Leadership,OffTheBall,Positioning,Teamwork,Workrate,Acceleration,Agility,Balance,Jumping,LeftFoot,NaturalFitness,Pace,RightFoot,Stamina,Strength,Consistency,Dirtiness,ImportantMatches,InjuryProness,Versatility,Adaptability,Ambition,Loyalty,Pressure,Professional,Sportsmanship,Temperament,Controversy,PositionsDesc,Goalkeeper,Sweeper,Striker,AttackingMidCentral,AttackingMidLeft,AttackingMidRight,DefenderCentral,DefenderLeft,DefenderRight,DefensiveMidfielder,MidfielderCentral,MidfielderLeft,MidfielderRight,WingBackLeft,WingBackRight";
    String[] fieldsArr = fields.split(",");
    tokenizer.setNames(fieldsArr);
    lineMapper.setLineTokenizer(tokenizer);

    // 필드 매퍼 세팅
    lineMapper.setFieldSetMapper(fieldSet -> {
      long id = fieldSet.readLong("UID");
      String name = fieldSet.readString("Name");
      int age = fieldSet.readInt("Age");
      int height = fieldSet.readInt("Height");
      int weight = fieldSet.readInt("Weight");
      String position = fieldSet.readString("PositionsDesc");

      return new Player(id, name, age, height, weight, position);
    });

    FlatFileItemReader<Player> itemReader = new FlatFileItemReaderBuilder<Player>()
            .name("playerCsvReader")
            .encoding("UTF-8")
            .resource(new ClassPathResource("csv/dataset.csv"))
            .linesToSkip(1)
            .lineMapper(lineMapper)
            .build();

    itemReader.afterPropertiesSet();

    return itemReader;
  }

  private ItemWriter<? super Player> playerMongoWriter() {
    return items -> {
      log.info("writer is called");
      items.forEach(item -> log.info(item.toString()));
    };
  }

}