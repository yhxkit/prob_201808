파일럿 프로젝트 계획 

### 기본 기능 
- 회원가입 기능 (이메일, 패스워드, 이름)
- 로그인 기능
- 게시판 CRUD 기능 

#### 권한
##### 게스트  
- 회원가입 / 로그인
- 게시글 리스트 보기 / 게시글 상세보기 
- 댓글 보기 
- 게시글 검색 

##### 회원 
- 게스트 기능 포함 
- 로그아웃/가입
- 게시글 작성
- 자신이 쓴 게시글 수정 / 삭제 
- 댓글 달기
- 자신이 쓴 댓글 수정 / 삭제 
 
##### 관리자
- 유저 기능 모두 포함 
- 유저 리스트 보기 
- 유저 상태 변경 (탈퇴 처리)


### 엔티티
#### Member
- String email 
- String password(암호화)
- String name
- String state
- Post post

#### Post
- int postIdx
- String title
- String content
- LocalDateTime postTime
- Comment comment

#### Comment
- int commentIdx
- LocalDateTime commentTime
- String comment
  
