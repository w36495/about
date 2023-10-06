![깃허브이미지](https://github.com/w36495/about/assets/52291662/cddd78e5-8f8f-43fe-a66b-f6257726f50a)
</br>
</br>
# 🔮 About
하나의 주제에 대한 다양한 생각들을 기록할 수 있는 서비스
</br>
## 개발기간
2022.08. ~ 2023.02. (버전 1.0.0)  
2023.02. ~ 2023.10. (버전 1.1.0)
2023.10. ~
</br>
## PlayStore
https://play.google.com/store/apps/details?id=com.w36495.about
</br>
## Index

- [빌드환경](#빌드-환경)
- [사용기술](#사용-기술)
- [데이터흐름](#데이터-흐름)
- [기능](#기능)
- [Trouble Shooting](#trouble-shooting)
- [학습한 내용](#학습한-내용)

---

## 빌드 환경

||Version|
|--|--|
|Android Gradle Plugin|7.3.1|
|Gradle Version|7.4|
|minSdk|21|
|targetSdk|33|

</br>  

## 사용 기술
- Kotlin
- MVP(Model-View-Presenter)
- Coroutine + Flow 
- viewBinding
- Room Database
- Jetpack Navigation
</br>

## 데이터 흐름  

![데이터흐름](https://github.com/w36495/about/assets/52291662/4392d92c-4a0e-4b5b-b722-24bd1280e357)

</br>

## 기능

![기능소개2](https://github.com/w36495/about/assets/52291662/865cd504-f56c-4ed4-aa8d-eeff87205d96)


## Trouble Shooting

### 주제 목록에 해당하는 총 생각 개수가 일치하지 않는 문제
- 문제 상황

  ![버그_생각개수](https://github.com/w36495/about/assets/52291662/ba11c323-63cf-4e5d-8902-3010aeb8e741)

  - '취미' 목록의 생각 개수와 '독서' 목록의 생각 개수가 일치함 (데이터베이스에는 일치하지 않음) 
- 해결 방법 [(링크)](https://w36495.tistory.com/99)
  - 데이터베이스의 topics 테이블과 thinks 테이블을 조인하여 select query 문 적용
  ``` SQL
  SELECT topics.id, topics.topic, COUNT(topicId) AS countOfThink, topics.registDate, topics.updateDate
  FROM topics
  LEFT JOIN thinks ON topics.id = thinks.topicId
  GROUP BY topics.id
  ```

### 생각 화면에서 Item을 삭제했을 때 NullPointerException 발생  
- 문제 상황

  ![생각삭제-버그](https://github.com/w36495/about/assets/52291662/68372fc4-af0c-4663-9e65-87870af10a16)
  
  - 아이템 삭제 시, NullPointerException 발생하며 앱 종료

- 해결 방법 [(링크)](https://w36495.tistory.com/100)
  - UiState 클래스에서 데이터를 가져올 때 호출되는 Success 클래스의 매개변수를 nullable 로 변경
  - 생각 화면에서 데이터를 관찰할 때, 값이 null 인 경우 popBackStack() 을 통해 이전 화면으로 이동 처리
</br>

## 학습한 내용

**[1) MVP 디자인 패턴에서 View 와 Presenter 가 1:1 로 대응되어야 하는 이유](https://w36495.tistory.com/97)**
