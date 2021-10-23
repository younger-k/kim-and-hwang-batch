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

import lombok.Getter;

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
@Getter
public class Player {
  private long id;
  private String name;
  private int age;
  private int height;
  private int weight;
  private String position;

  public Player(long id, String name, int age, int height, int weight, String position) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.height = height;
    this.weight = weight;
    this.position = position;
  }
}
