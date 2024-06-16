## 투두 프로젝트

### 사용 기술
- java
- Spring Boot, Spring Security
- Spring Date JPA
- MySQL, Redis
- AWS EC2, AWS RDS, Docker
- GitHub Actions
- Prometheus, Grafana

### 아키텍쳐
![스크린샷 2024-04-23 오후 6 43 28](https://github.com/mini-todo/todo-server/assets/83914354/417a760a-a6db-47d7-8ab3-66ae0145435d)

### 1. EC2 프리티어의 메모리 부족 현상을 Swap 가상 메모리 설정으로 해결

### 문제 상황

- AWS EC2 프리티어 제품 사용중 메모리 부족으로 서버가 다운되는 문제 발생

### **EC2 인스턴스 스팩**

- 메모리 : 1GB
- 스토리지 : 20GB

### 해결 방법

- Swap 파일을 생성해서 RAM의 공간이 부족할 때 단기적으로 스왑 공간을 대체로 사용하도록 설정
- 권장되는 스왑 메모리 크기에 따라서 1GB의 2배인 2GB로 설정

### 결과

- 스왑 메모리 공간을 설정해서 약 2GB의 임시 메모리 공간을 확보했다.
  <img width="746" alt="스크린샷 2024-04-18 오후 2 03 56" src="https://github.com/mini-todo/todo-server/assets/83914354/c1e15501-d432-4916-9d1b-6bef8009e759">

### 2. JWT 재발급에 사용하는 RefreshToken 저장 장소를 MySQL에서 Redis로 이동

### 문제 상황

- RefreshToken이 User 테이블에 컬럼으로 존재
- AccessToken의 유효기간이 1시간이기 때문에 최악의 경우 1시간마다 DB에 조회와 업데이트 발생
- RefreshToken의 경우 변동성이 크기 때문에 RDB의 성질과 어울리지 않는다.

### 해결 방법

- RefreshToken을 MySQL에서 Redis로 옮기기로 결정
- email정보와 RefreshToken 값을 가지는 객체 형식으로 저장
- Memcached라는 선택지도 있지만 특징을 비교해 Redis로 선택
### 결론

- Redis로 refreshToken의 저장 위치를 바꿔서 기존 MySQL의 부화 감소
- key-value 방식으로 저장되기 때문에 O(1)의 시간복잡도를 가져 성능 향상
- refreshToken의 경우 저장후 바로 사용되지 않기 때문에 이벤트를 활용해 비동기적으로 처리
### 3. 쿼리 카운터를 통한 쿼리 발생 횟수 추적

### 문제 상황

- 운영 서버에서 쿼리 발생 횟수에 대한 추적 필요
- 모든 쿼리를 전부 로그로 출력해서 카운팅하는 것은 현실적으로 불가능

### 스프링 AOP를 활용

### 포인트 컷

기본 Connection 객체는 싱글톤이 아니지만, DataSource는 Bean에 등록 되어 있고, \
getConnection() 메서드를 통해 Connection 객체를 얻기 때문에 해당 시점에 포인트 컷을 설정해주었습니다.
![스크린샷 2024-05-15 오전 8 46 02](https://github.com/mini-todo/todo-server/assets/83914354/9a704e6f-7a86-4253-a739-123f7dcb8e7b)

### 어드바이스

**동적 프록시 활용**
원본 객체에 원하는 기능을 추가하기 위해 프록시를 사용하는데, 프록시 객체를 직접 생성하기 위해선 모든 메서드를 구현해주어야 합니다. \
하지만 동적 프록시를 사용하면 런타임시에 프록시 객체를 만들어주기 때문에 간편하게 프록시를 사용할 수 있습니다.
![스크린샷 2024-05-15 오전 8 46 19](https://github.com/mini-todo/todo-server/assets/83914354/d86f63c8-a566-4092-885b-12347a842d3b)

### 결과

- 모든 요청 마다 발생한 쿼리의 수를 로그로 출력
- 10개 이상의 쿼리 발생시 경고 로그 출력
<img width="864" alt="스크린샷 2024-04-22 오후 2 51 39" src="https://github.com/mini-todo/todo-server/assets/83914354/1bdace23-3f2b-4781-beea-41be0c0a1716">

### 4. JDBC를 활용한 Bulk Insert 최적화

### 문제 상황

- 고정 할 일의 경우 매일 자정에 자신의 할 일에 추가
- JPA를 사용할 경우 모든 고정 할 일에 대해 각각 insert 쿼리가 발생

### 해결 방법

- Bulk Insert 사용해서 많은 데이터를 1번의 쿼리로 DB에 저장
- JDBCTemplate를 사용해서 직접 SQL을 작성해서 벌크 연산 처리
- ![스크린샷 2024-05-15 오전 8 51 43](https://github.com/mini-todo/todo-server/assets/83914354/311560bf-1536-46ba-9e64-999d8aeda0da)

### 결과

- 기존 JPA 방식과 비교시 약 1.76초 → 0.81초로 단축 (데이터 10,000개 기준)

### 5. 로그 추적기 구현

### 문제상황

- 에러 발생시 ExceptionHandler 를 통해 모든 에러가 응답 값으로 처리
- 에러가 발생한 위치를 정확히 파악 할 수 없는 문제 발생

### 해결 방법

- AOP를 활용해서 모든 로직을 대상으로 각 메서드 시그니처와 파라미터 값을 확인할 수 있는 로그 추적기를 구현
- 각 메서드의 depth를 함께 추적해서 함께 표시 → 어떤 메서드에서 에러가 발생했는지 추적
  ![스크린샷 2024-05-19 오전 9 30 59](https://github.com/mini-todo/todo-server/assets/83914354/910da150-a3ef-4da8-9d62-cf2d38260ade)

### 결과

- 메서드의 깊이와 함께 어떤 로직이 실행 되었고 어떤 파라미터가 전달 되었는지 로그 기록
- 쿼리 카운트와 함께 출력되어서 한번에 호출에 대한 정보 확인
- 에러 발생시 로그 추적기를 통해 파악 가능 해졌습니다.
  ![스크린샷 2024-05-17 오후 8 10 35](https://github.com/mini-todo/todo-server/assets/83914354/6af93cc4-d288-4bb1-99d5-919b28d03eb2)
  ![스크린샷 2024-05-19 오전 9 29 05](https://github.com/mini-todo/todo-server/assets/83914354/0950b532-248c-43ad-9e02-8dd64787f6c6)