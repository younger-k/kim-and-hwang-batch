/*
 * Created by Yeonha Kim on 2021/10/24
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <jongsang@bigin.io>, 2021/10/24
 */
package com.kimandhwang.batch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * create on 2021/10/24. create by IntelliJ IDEA.
 *
 * <p> 선수의 상세 정보 </p>
 * <p> 대한민국, 잉글랜드, 오스트레일리아 소속의 선수들로만 가공된 csv 파일 이용. </p>
 * <p> 스탯 정보는 제외 (1대1 능력, 가속력, 포지션 별 능력 등) </p>
 *
 * @author Yeonha Kim
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "PLAYER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Player {

  /* 선수 고유 식별자 */
  @Id
  @Column(name = "UID")
  private long uid;

  /* 선수 이름 */
  @Column(name = "NAME")
  private String name;

  /* 선수의 소속 국가 ID */
  @Column(name = "NATION_ID")
  private long nationId;

  /* 선수 생년월일 */
  @Column(name = "BORN")
  private String born;

  /* 선수 나이 */
  @Column(name = "AGE")
  private int age;

  /* 선수 신장 */
  @Column(name = "HEIGHT")
  private int height;

  /* 선수 체중 */
  @Column(name = "WEIGHT")
  private int weight;

  /* 선수의 포지션 정보 */
  @Column(name = "POSITIONS_DESC")
  private String positionsDesc;

  public Player(long uid, String name, long nationId, String born, int age, int height,
      int weight, String positionsDesc) {
    this.uid = uid;
    this.name = name;
    this.nationId = nationId;
    this.born = born;
    this.age = age;
    this.height = height;
    this.weight = weight;
    this.positionsDesc = positionsDesc;
  }
}
