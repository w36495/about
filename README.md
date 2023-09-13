![깃허브이미지](https://github.com/w36495/about/assets/52291662/cddd78e5-8f8f-43fe-a66b-f6257726f50a)
</br>
</br>
# 🔮 About
하나의 주제에 대한 생각들을 기록하는 어플리케이션
</br>
### 개발기간
2022.08. ~
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
- MVP(Model-View-Presenter) 디자인 패턴
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

![생각삭제](https://github.com/w36495/about/assets/52291662/d464a8f8-497e-4834-879c-8279de8d1514)
- 왼쪽으로 스와이프하여 해당 생각 아이템 삭제 가능
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

### Trouble Shooting

</br>

### 학습한 내용

**[1) MVP 디자인 패턴에서 View 와 Presenter 가 1:1 로 대응되어야 하는 이유](https://w36495.tistory.com/97)**
