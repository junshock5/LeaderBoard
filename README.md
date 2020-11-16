# readerboard
목적
---
- ### 리그 오브 레전드를위한 간단한 리더 보드 백엔드 애플리케이션 작성
- 초기 데이터는 25,000 행의 데이터를 포함하며 각 행은 playerId 및 mmr (매치 메이킹 등급)로 구성됩니다.

사용 기술
---
- Maven Project
- Java 8
- Junit 4
- assertj

기술적인 집중 요소
---
- 객체지향의 기본 원리와 의미 있는 코드 작성
- 라이브러리 및 기능 추가 시 이유 있는 선택과 사용 목적 고려
- 테스트 코드 작성

REST API 사양
---
1. 총 플레이어 수를 반환하는 API
2. 플레이어의 계층을 반환하는 API
3. 상위 10 개 플레이어 목록을 반환하는 API
4. 주어진 플레이어 ID 근처의 플레이어 목록을 반환하는 API. 
예) playerId가 5이고 범위가 5 인 경우. 5 명의 상위 플레이어와 5 명의 하위 플레이어를 찾아야합니다.
5. 플레이어를 업데이트 / 추가하는 API (리더 보드도 업데이트해야 함)
6. 플레이어 삭제 API

플레이어 모델은
- long playerId
- int mmr
- int rank
- Tier tier

계층 모델은 다음으로 구성됩니다.
- CHALLENGER // top 100 players
- MASTER, // top 1% players
- DIAMOND, // top 5% players
- PLATINUM, // top 10% players
- GOLD, // top 25% players
- SILVER, // top 65% players
- BROZNE, // others

설계 고려 사항
---
InitialData.txt 로드 후 DB insert시 userID가 존재한다면 데이터 갱신하고 없다면 insert
순위 및 티어 변동 시 전체 데이터 재갱신 되게 stream api를 이용해서 sorting 후 데이터 처리
Mockito Framework를 활용하여 고립된 테스트 코드를 작성

실행 방법
---
압축파일 해제 후 readerboard\target 경로에서 java -jar readerboard-0.0.1-SNAPSHOT.jar (jre or jdk 필요)
http://localhost:8080/swagger-ui.html#/users (Swagger를 통해 API 확인 가능) 

DB 데이터 확인 방법
---
http://localhost:8080/h2-console/ 접속 후
setting 값을 Generic h2 (embedded)
JDBC url 값을 jdbc:h2:~/board 접속

ERD
---
![image](https://user-images.githubusercontent.com/61732452/99238175-c6007680-283c-11eb-8a34-3fe19ab4c2a5.png)

참고
---
### **Java8 표준 라이브러리 주소** <br/> https://docs.oracle.com/javase/8/docs/api/
