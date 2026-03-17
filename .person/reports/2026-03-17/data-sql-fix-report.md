Reporter: KimNaKim

# data.sql 무결성 제약 조건 위반 수정 보고서

## 작업 요약
- `data.sql` 실행 시 `qna_tb`의 `CATEGORY` 컬럼 누락으로 인한 H2 DB 에러를 수정하였습니다.
- 잘못 작성된 `review_tb` 섹션의 SQL 문을 올바르게 수정하였습니다.

## 상세 변경 사항

### 1. qna_tb INSERT 문 수정
- `Qna` 엔티티에서 `nullable = false`로 설정된 `category` 컬럼이 `data.sql`의 INSERT 문에서 누락되어 있었습니다.
- `category` 컬럼(`FACILITY` 값 사용)과 `hits` 컬럼(기본값 0을 고려한 초기값)을 추가하여 제약 조건을 충족시켰습니다.

### 2. 이용 후기(review_tb) 섹션 정상화
- 기존 `data.sql`에서 "이용 후기" 섹션임에도 불구하고 `qna_tb`에 중복된 데이터를 입력하고 있던 오류를 발견하였습니다.
- 이를 `review_tb`에 대한 올바른 INSERT 문으로 수정하여, 예약 정보와 연동된 실제 리뷰 데이터가 생성되도록 하였습니다.

## 검증 결과
- 수정 후 `DataSourceScriptDatabaseInitializer`가 오류 없이 실행됨을 확인하였습니다.
- 애플리케이션 시작 시 발생하는 `BeanCreationException`이 해결되었습니다.

## 비유로 설명하기
마치 **"회원 가입 양식에 필수 항목(카테고리)을 적지 않고 제출해서 거절당한 상황"**과 같았습니다. 꼼꼼하게 빠진 항목을 채워 넣고, 엉뚱한 칸(질문 게시판)에 적혀 있던 일기(이용 후기)를 제자리(리뷰 게시판)로 옮겨 주었습니다. 이제 시스템이 모든 정보를 올바르게 인식하고 정상적으로 작동합니다.
