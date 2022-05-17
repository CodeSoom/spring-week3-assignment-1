# 테스트 작성하기

## 과제 목표

![image](https://user-images.githubusercontent.com/14071105/104756530-45f47f80-579f-11eb-9bbf-d47d065e207b.png)

우리가 작성한 코드가 올바르게 동작하는지 자동으로 확인해 줄 수 있는 유닛 테스트를 작성해 주세요.  
컨트롤러는 유닛 테스트뿐만 아니라 실제로 웹에서 동작하는 것처럼 작성하는 Spring Mock MVC 
테스트를 작성해 주세요.

* HelloController 유닛 테스트
* HelloController MockMVC 테스트
* Task 모델 테스트
* TaskService 테스트
* TaskController 유닛 테스트
* TaskController MockMVC 테스트

## 요구 사항

- 모든 인수 테스트를 통과해야 합니다.
- 테스트 커버리지 100%를 달성해야 합니다.

## 테스트 시나리오
### Domain Model (Task)
- 현재 타이틀 조회
  - [x] Task 객체 내 현재 title를 반환.
- 타이틀 유효성 검증
  - [x] title이 존재하는 경우, true 반환
  - [x] title이 존재하지 않는 경우, false 반환
- 타이틀 수정
  - [x] title을 수정하면 수정 후, 현재의 task 반환.
- Task 객체 이용한 Task 객체 생성
  - [x] id와 Task 객체를 이용하여 동일한 타이틀을 갖는 Task 객체를 생성/반환.
### Service
- 할일 목록 전체 조회
  - [ ] 목록에 할일이 있는 경우, 목록 반환
  - [ ] 목록에 할일이 하나도 없는 경우, 빈 array 반환
- 할일 목록 단건 조회
  - [ ] 일치하는 할일이 있는 경우, 할일 반환
  - [ ] 일치하는 할일이 없는 경우, 
- 할일 등록
  - [ ] 할일 등록
  - [ ] 등록된 할일에 대해 조회
- 할일 수정
  - 수정할 할일이 있는 경우
    - [ ] 
  - 수정할 할일이 없는 경우
    - [ ] 
- [ ] 할일 삭제
    - 삭제할 할일이 있는 경우
        - [ ]
    - 삭제할 할일이 없는 경우
        - [ ]
### Controller
- [ ] GET
- [ ] POST
- [ ] PATCH
- [ ] PUT
- [ ] DELETE

## API 실행하기

```bash
./gradlew run
```

## Web 실행하기

```bash
cd web
npm install
npm run dev
```

## 테스트

### Spring 테스트 실행

```bash
$ ./gradlew test
```

### 커버리지 확인하기

테스트를 실행하면 자동으로 커버리지 정보를 수집하여 저장합니다. 커버리지 정보는 `app/build/reports`
폴더에 저장됩니다. 커버리지 정보를 확인하려면 `app/build/reports/jacoco/test/html/index.html`파일을
브라우저에서 열면 확인할 수 있습니다.

### 설치하기

```bash
$ cd tests
$ npm install
```

### E2E 테스트 실행하기

테스트는 실제로 동작하는 서버에 테스트하므로 서버가 동작하고 있는 상태여야 올바르게 동작합니다.  
프론트엔드 개발용 서버도 동작하고 있는 상태여야 올바르게 동작합니다.

```bash
$ npm run e2e
```


