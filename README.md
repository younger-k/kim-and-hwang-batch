[![java](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
)](https://spring.io/projects/spring-batch)


----

[TOC]

----

# kim-and-hwang-batch

`Spring Batch` 실습 관련 프로젝트.

## Batch Jobs

- FootballJob
  Football Manager 2017 게임의 선수에 대한 dataset `csv` 파일을 DB로 저장하는 배치. (약 10만개의 row)

## Skills

- Docker
- Gradle
- Spring Batch
- MariaDB
- JPA

## How to run a batch job

1. `docker-compose up -d`
   - `MariaDB` container를 생성하면서 관련 메타 데이터와 테이블 등을 같이 생성.
2. arguments: `--job.name=footballPlayerJob`

## Reference

- FootballManager 2017 dataset csv file: [kaggle.com - @ajinkyablaze](https://www.kaggle.com/ajinkyablaze/football-manager-data)