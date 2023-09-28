![깃허브이미지](https://github.com/w36495/about/assets/52291662/cddd78e5-8f8f-43fe-a66b-f6257726f50a)
</br>
</br>
# 🔮 About
하나의 주제에 대한 생각들을 기록하는 어플리케이션
</br>
### 개발기간
2022.08. ~ 2023.02. (버전 1.0.0)  
2023.02. ~ 업데이트 준비중 🚀
</br>
### PlayStore
https://play.google.com/store/apps/details?id=com.w36495.about
</br>
### Index
- 빌드환경
- 사용기술
- 데이터흐름
- 기능
- Trouble Shooting
- 학습한 내용

---
### 빌드환경

||Version|
|--|--|
|Android Gradle Plugin|7.3.1|
|Gradle Version|7.4|
|minSdk|21|
|targetSdk|33|

</br>  

### 사용기술
- Kotlin
- MVP(Model-View-Presenter)
- Coroutine + Flow 
- viewBinding
- Room Database
- Jetpack Navigation
</br>

### 데이터흐름
![데이터흐름](https://github.com/w36495/about/assets/52291662/4392d92c-4a0e-4b5b-b722-24bd1280e357)

</br>

### 기능

**주제 등록 및 조회**  

![주제등록및조회](https://github.com/w36495/about/assets/52291662/80f8648c-f3cc-4f81-8df4-306ca474740b)

- 주제가 등록된 날짜로부터 며칠이 지났는지 확인 가능
- 각 주제에 등록된 생각 개수 확인 가능
</br>

**주제 삭제**

![주제삭제](https://github.com/w36495/about/assets/52291662/fda973f9-6e59-4d51-b85e-1ed13701ff4f)
- 해당 주제를 길게 터치하면 삭제 다이얼로그 표시
</br>

**생각 목록 조회 및 생각 등록**

![생각등록및조회](https://github.com/w36495/about/assets/52291662/57007a82-1854-493e-9723-44b96bca1189)
- 앱바 타이틀을 통해 현재 주제 확인 가능
</br>

**생각 삭제**

![생각삭제_수정후](https://github.com/w36495/about/assets/52291662/e4012de3-b18e-4864-9909-9b0c54bcffc4)
- 왼쪽으로 스와이프하여 삭제 버튼 표시
- 이전에 선택한 아이템과 현재 선택한 아이템이 다른 경우, 이전 아이템의 삭제 버튼 사라짐
- 삭제 버튼 클릭 시, 해당 아이템의 삭제 버튼 사라짐
- 삭제 시, 하단에 토스트 메세지 표시

</br>

**생각 조회 및 코멘트 목록 조회**

![생각목록및코멘트목록](https://github.com/w36495/about/assets/52291662/04822bec-bb77-4477-8e19-3456ea7f874c)
- 앱바 메뉴를 통해 생각 수정/삭제 가능 (구현 전)
- 코멘트 총 개수 확인 가능
</br>

**코멘트 삭제**

![코멘트삭제](https://github.com/w36495/about/assets/52291662/5be8a6d1-5172-4740-8968-130a452c2aec)
- 오른쪽으로 스와이프하여 삭제 버튼 표시
- 삭제 버튼 클릭 시, 보여지는 하단 다이얼로그를 통해 삭제 가능
</br>

---
### Trouble Shooting

**주제 목록에 해당하는 총 생각 개수가 일치하지 않음**
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

**생각 화면에서 Item을 삭제했을 때 NullPointerException 발생**  
- 문제 상황

  ![생각삭제-버그](https://github.com/w36495/about/assets/52291662/68372fc4-af0c-4663-9e65-87870af10a16)
  
  - 아이템 삭제 시, NullPointerException 발생하며 앱 종료

- 해결 방법 [(링크)](https://w36495.tistory.com/100)
  - UiState 클래스에서 데이터를 가져올 때 호출되는 Success 클래스의 매개변수를 nullable 로 변경
  - 생각 화면에서 데이터를 관찰할 때, 값이 null 인 경우 popBackStack() 을 통해 이전 화면으로 이동 처리
</br>

### 학습한 내용

**[1) MVP 디자인 패턴에서 View 와 Presenter 가 1:1 로 대응되어야 하는 이유](https://w36495.tistory.com/97)**
