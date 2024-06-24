## 개인 - 게시글 토이 프로젝트

<hr/>

### ⚡프로젝트 소개

#### 시작하게 된 이유
> SSR(Server Side Rendering) 프로젝트
> MyBatis를 이용하여 게시글을 만들기
> 페이징 처리 학습
> 게시글 좋아요 기능 구현
> Spring Batch 이용

<hr/>

### ⚙️ 기술 정보
<div>
<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=spring&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring Batch-6DB33F?style=flat-square&logo=spring&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/MyBatis-001202?style=flat-square&logo=mybatis&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/MYSQL-4479A1?style=flat-square&logo=mysql&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/H2 Database-4479A1.svg?&style=flat-square&logo=&logoColor=white">
<img src="https://img.shields.io/badge/Thymeleaf-005F0F.svg?&style=flat-square&logo=thymeleaf&logoColor=white">

<hr/>

### 🚧 버전 관리 및 진행
> 2023.10 ~ 2023.12 Version 1
- Spring Security를 이용하여 회원가입 및 로그인 구현 (권한 : ROLE_USER, ROLE_ADMIN)
- Member, Board 조회 쿼리 및 isnert 쿼리 작성 및 테스트
- Board(게시판) 페이징 처리 추가

- 권한에 따른 하이퍼링크 설정 (thymeleaf-extras-springsecurity5)
- 회원가입, 로그인 Bean Validation 설정 및 Thymeleaf, 필드에러 또는 글로벌 오류로 오류 메세지 나오게 하기 (BindingResult)
- FILE 테이블 작성 및 조회 쿼리 및 insert 쿼리 작성
- BoardFile domain 모델 작성 및 boardMapper resultMap 작성 및 1:N join 으로 한방 쿼리 작성 (SELECT)
- 게시글 작성 시 파일 업로드 가능하게 (FILE 테이블) 작성 (FileStore, UploadFile)

- 이미지 파일, 첨부 파일 구분하게 하여 (image_yn 컬럼) 첨부 파일은 다운로드 할 수 있게 하고, 이미지 파일은 img 태그에서 가져올 수 있게 분리 
- 이미지 파일 업로드 시 확장자 검사 (.jpg, .png, .gif)
- 파일 용량 크면 에러페이지 및 글로벌 오류 메세지 X -> MaxFileSize 및 MaxRequestSize 설정 및 Javascript 로 파일 용량 이상시 alert 처리
- 이미지 파일 이름으로 다운 받을 때 DB 에 값이 존재하지 않을 경우 404 오류 페이지 응답

- 이미지 파일은 최대 3개까지 업로드 할 수 있게 한다.
- 게시글 Repository(Mapper) 삭제 및 수정 쿼리 작성 및 테스트
- 사용자 Exception 정의 및 ControllerAdvice 및 ExceptionHandler 로 예외 페이지 반환
- 게시글을 삭제 할 때 board_delete_date 에 삭제 시간 값을 넣어 준다.
- 게시글을 삭제 하였을 때, 게시글 조회 시 삭제날짜가 null 이 아닌경우에 만 보여주게 한다. (findAll 에서 where 문 작성 (게시글 삭제 날짜가 null 일 경우에만 조회))
- 게시글을 조회 시 페이징 처리에서 필요한 getPageMaxCount 에서 where 문 작성 (게시글 삭제 날짜가 null 일 경우에만 카운트)
- 게시글을 삭제 및 수정할 때 존재하지 않는 게시글인지, 이미 삭제된 게시글인지, 게시글 글쓴이인지 확인 및 Exception 처리
- 게시글 삭제 폼 작성 및 컨트롤러 작성
- 게시글 수정 폼 작성 및 컨트롤러 작성 
- 게시글을 수정 할 때 첨부 파일 및 이미지 변경 시 존재 했던 파일이 존재 하지 않을 경우 DELETE, 파일이 추가된 경우 INSERT (저장소에서도 파일 삭제 및 추가)

- RuntimeException 상속 받는 사용자 정의 예외 만들기
- 사용자가 새로 업로드한 파일과 기존에 있던 파일(fileId)을 가지고 수정 요청하게 되는데,
- BoardService 내에 updateBoard 메서드에서 DB를 조회 후 업로드 되어있는 데이터가 수정 데이터에 없을 경우
- 업로드된 데이터를 삭제하게 하는 로직에서 예기치 못한 문제가 발생했다.
    - 문제 : 언체크 Exception으로 인하여 rollback 되면(트랜잭션) 파일을 삭제하게 하면 안된다.
    - 해결방안 : 파일을 삭제하기 전에 리스트로 담고 return 을 하고, BoardController 에서 return 받은 리스트를 통해 파일을 삭제한다. (중간에 언체크 예외가 throw 되면 ExceptionHandler 로 전가)
- Controller에서 책임이 많아지게 되었으므로, 추후 리팩토링을 통해 FileController 작성하여 책임을 분할
- File 관련된 로직을 BoardService에 작성하여 코드의 복잡도가 높아졌다.
    - FileService 를 따로 만들어 File에 대한 책임을 전가 하여 BoardService의 할일을 줄게 만들게 한다. (리팩토링)

- 댓글 작성 및 댓글 삭제 기능 추가
- 게시글 삭제 시 댓글 테이블 DELETE CASCADE
- 게시글 및 댓글을 각각 다른 페이징 처리(레코드 사이즈 및 페이지 사이즈 각각 다르게)로 분리해야 하기 때문에, 추상클래스 Search를 만들어서 BoardSearchDto(게시글DTO), ReplySearchDto(댓글DTO)로 분리한다.
- Pagination 생성자 메서드에서는 Search(상속 받은 추상 클래스)로 업 캐스팅 후 로직 진행 (다형성)

- 키워드로 검색하기, 최근 순 정렬, 
- 닉네임도 유일성으로 만들기 (회원가입 시 중복 제거)

- 스프링 배치를 이용하여 삭제 된지 7일이 지나면 DB 에서도 삭제하게 만들기
    - 게시글 삭제 시 파일 테이블 DELETE CASCADE
    - 배치(Batch): Board 삭제, 제약조건 ON DELETE CASCADE 로 인하여 FILE 테이블에 존재하는 연관된 컬럼 삭제
    - 게시글 삭제 시 FILE 테이블의 데이터도 삭제되기 때문에 스토리지에 있는 파일도 같이 삭제한다.
- 게시글 복구 페이지 작성 및 게시글 복구 로직 추가

> 2024.03 ~ 2024.03 Version 2
- mapper 클래스 DAO로 이름 변경 및 VO(Value Object) 분할(Setter는 없이, EqualsAndHashCode annotation으로 메서드 오버라이딩)
- 게시글 좋아요(Likes) 테이블 및 추가, 삭제 기능 추가
- 게시글 조회시 좋아요 정렬 기능 추가

<hr/>

### 🔍 프로젝트 상세

#### ERD-Cloud
![image-erd](./md_resource/image-erd.png)

<hr/>

### 🌳 개발 환경

- Project: Gradle
- SpringBoot: 2.7.15
- Language: Java 11
- Dependencies
  - mybatis-spring-boot-starter:2.3.1
  - spring-boot-starter-batch
  - spring-boot-starter-quartz
  - spring-boot-starter-security
  - spring-boot-starter-web
  - spring-boot-starter-validation
  - spring-boot-starter-thymeleaf
  - spring-boot-starter-test
  - mysql-connector-j
  - lombok