## 개인 - 게시글 토이 프로젝트

<hr/>

### ⚡프로젝트 소개

#### 시작하게 된 이유
> - SSR(Server Side Rendering) 프로젝트
> - MyBatis를 이용하여 게시글을 만들기
> - 페이징 처리 학습
> - 게시글 좋아요 기능 구현
> - Spring Batch 이용

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
> 2023.10~2023.12 Version 1
- Spring Security를 이용하여 회원가입 및 로그인 구현 (권한 : ROLE_USER, ROLE_ADMIN)
- Member, Board 조회 질의 및 insert 질의 작성 및 테스트
- Board(게시판) 페이징 처리 추가

- 권한에 따른 하이퍼링크 설정 (thyme leaf-extras-spring security 5)
- 회원가입, 로그인 Bean Validation 설정 및 Thymeleaf, 필드에서 또는 글로벌 오류로 오류 메시지 나오게 하기 (Binding Result)
- FILE 테이블 작성 및 조회 질의 및 insert 질의 작성
- Board File domain 모델 작성 및 board Mapper result Map 작성 및 1:N join으로 한방 질의 작성 (SELECT)
- 게시글 작성 시 파일 업로드 가능하게 (FILE 테이블) 작성 (FileStore, UploadFile)

- 이미지 파일, 첨부 파일 구분하게 하여 (FILE_IMAGE_NY 칼럼) 첨부 파일은 내려받을 수 있게 하고, 이미지 파일은 IMG 태그에서 가져올 수 있게 분리
- 이미지 파일 업로드 시 확장자 검사 (.jpg, .png, .gif)
- 파일 용량 크면 에러 페이지 및 글로벌 오류 메시지 X -> MaxFileSize 및 MaxRequestSize 설정 및 Javascript로 파일 용량 이상 시 alert 처리
- 이미지 파일 이름으로 다운 받을 때 DB에 값이 존재하지 않을 경우 404 오류 페이지 응답

- 이미지 파일은 최대 3개까지 올릴 수 있게 한다.
- 게시글 Repository(Mapper) 삭제 및 수정질의 작성 및 테스트
- 사용자 Exception 정의 및 Controller Advice 및 Exception Handler으로 예외 페이지 반환
- 게시글을 삭제 할 때 board_delete_date에 삭제 시간 값을 넣어 준다.
    - 게시글을 삭제 하였을 때, 게시글 조회 시 삭제날짜가 null이 아닌 경우에 만 보여주게 한다. (find All 에서 where 문 작성 (게시글 삭제 날짜가 null 일 경우에만 조회))
- 게시글을 조회 시 페이징 처리에서 필요한 getPageMaxCount 에서 where 문 작성 (게시글 삭제 날짜가 null 일 경우에만 카운트)
- 게시글을 삭제 및 수정할 때 존재하지 않는 게시글인지, 이미 삭제된 게시글인지, 게시글 글쓴이인지 확인 및 Exception 처리
- 게시글 삭제 폼 작성 및 컨트롤러 작성
- 게시글 수정 폼 작성 및 컨트롤러 작성
- 게시글을 수정 할 때 첨부 파일 및 이미지 변경 시 존재 했던 파일이 존재 하지 않을 경우 DELETE, 파일이 추가된 경우 INSERT (저장소에서도 파일 삭제 및 추가)

- RuntimeException 상속 받는 사용자 정의 예외 만들기
- 사용자가 새로 올린 파일과 기존에 있던 파일(fileId)을 가지고 수정 요청하게 되는데, BoardService 내에 updateBoard 메서드에서 DB를 조회 후 업로드 되어있는 데이터가 수정 데이터에 없을 경우 업로드된 데이터를 삭제하게 하는 로직에서 예기치 못한 문제가 발생했다.
    - 문제 : 언 체크 Exception으로 인하여 rollback 되면(트랜잭션) 파일을 삭제하게 하면 안된다.
    - 해결 방안 : 파일을 삭제하기 전에 리스트로 담고 return을 하고, Board Controller에서 return 받은 리스트를 통해 파일을 삭제한다. (중간에 언 체크 예외가 throw 되면 ExceptionHandler에 전가)
    - Controller에서 책임이 많아지게 되었으므로, 추후 리팩토링을 통해 File Controller 작성하여 책임을 분할
- File 관련된 로직을 Board Service에 작성하여 코드의 복잡도가 높아졌다.
    - File Service를 따로 만들어 File에 대한 책임을 전가 하여 Board Service의 할 일을 줄게 만들게 한다. (리팩토링)

- 댓글 작성 및 댓글 삭제 기능 추가
    - 게시글 삭제 시 댓글 테이블 DELETE CASCADE
    - 게시글 및 댓글을 각각 다른 페이징 처리(레코드 크기 및 페이지 크기 각각 다르게)로 분리해야 하기 때문에, 추상 클래스 Search를 만들어서 BoardSearchDto(게시글DTO), ReplySearchDto(댓글DTO)로 분리한다.
    - Pagination 생성자 방법에서는 Search(상속받은 추상 클래스)로 업 캐스팅 후 로직 진행 (다형성)

- 키워드로 검색하기, 최근 순 정렬,
- 닉네임도 유일성으로 만들기 (회원가입 시 중복 제거)

- 스프링 배치를 이용하여 삭제된 지 7일이 지나면 DB에서도 삭제하게 만들기
    - 게시글 삭제 시 파일 테이블 DELETE CASCADE
    - 배치(Batch): Board 삭제, 제약조건 ON DELETE CASCADE로 인하여 FILE 테이블에 존재하는 연관된 칼럼 삭제
    - 게시글 삭제 시 FILE 테이블의 데이터도 삭제되기 때문에 스토리지에 있는 파일도 같이 삭제한다.
- 게시글 복구 페이지 작성 및 게시글 복구 로직 추가

> 2024.03~2024.03 Version 2
- mapper 클래스 DAO로 이름 변경 및 VO(Value Object) 분할(Setter는 없이, EqualsAndHashCode annotation으로 equals, hashCode메서드 오버라이딩)
- 게시글 좋아요(Likes) 테이블 및 추가, 삭제 기능 추가
- 게시글 조회 시 좋아요 정렬 기능 추가

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

<br/>
<hr/>

### 배포 과정
#### 사용 툴
- VMWare Workstation 17 player, Putty, FileZilla
- HostOS: Windows 11
- guestOS: CentOS Stream 9

#### 버전
- nginx: 1.20.1
- MySQL: 8.4.1
- JDK: 17.0.6

#### 순서
1. 가상 OS VM Ware 프로그램 설치
2. 운영체제 CentOS Stream 9 설치
3. Putty로 접속할 관리자 계정 생성
4. 접속은 외부 서버가 있다고 가정하여 Putty로 SSH 접속 및 파일은 FileZilla를 통해 파일 옮기기
5. Java Development Kit(JDK) 설치
6. nginx 설치, 접속 테스트
7. Host OS 공유기에 포트 포워딩 설정(80, 443), VMWare 포트 포워딩 설정(80, 443 외부 접속 허용)
8. Firewall(방화벽) 80, 443 포트 열기
9. '내도메인.한국' 무료 DNS 사용 및 Host 외부 IP 연결
10. MySQL 설치 및 계정 생성, 데이터베이스 생성 및 테이블 추가
11. Spring Boot 이용하여 빌드(BootJar) 후 파일 systemctl(서비스) 등록
12. Certbot 이용하여 무료 lets' encrypt SSL/TLS 설치 및 nginx.conf 파일 설정
13. 서비스 실행 및 외부 사용자 접속 테스트

#### nginx.conf
```nginx
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    client_max_body_size 10M;

    upstream app {
       server 127.0.0.1:8080;
    }

    underscores_in_headers on;
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    # Redirect all Traffic to HTTPS
    server {
      listen 80;

	  location ~ /\.well-known/acme-challenge/ {
	  	allow all;
		root /var/www/letsencrypt;
	  }

      return 301 https://$host$request_uri;
    }
 
    server {
      listen 443 ssl http2;
      ssl_certificate /etc/letsencrypt/live/nineto6.p-e.kr/fullchain.pem;
      ssl_certificate_key /etc/letsencrypt/live/nineto6.p-e.kr/privkey.pem;

      # Disable SSL
      ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
     
      # 통신과정에서 사용할 암호화 알고리즘
      ssl_prefer_server_ciphers on;
      ssl_ciphers ECDH+AESGCM:ECDH+AES256:ECDH+AES128:DH+3DES:!ADH:!AECDH:!MD5;

      # Enable HSTS
      # client의 browser에게 http로 어떠한 것도 load하지 말라고 규제합니다.
      add_header Strict-Transport-Security "max-age=31536000" always;

      # SSL sessions
      ssl_session_cache shared:SSL:10m;
      ssl_session_timeout 10m;
    
      location / {
        proxy_pass http://app;
        proxy_set_header Host $host;
        proxy_set_header X-Nginx-Proxy true;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
      } 
    }
}
```

#### 96talk.service
```
[Unit]
Description=MyBoard Description
After=syslog.target network.target myboard.service

[Service]
ExecStart=/bin/bash -c "exec java -jar /home/ikavon/server/myboard/myboard-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod"
Restart=on-failure
RestartSec=10

User=root
group=root

[Install]
WantedBy=multi-user.taget
```