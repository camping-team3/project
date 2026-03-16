Reporter: KimNaKim

## 작업 요약
관리자 페이지의 레이아웃 배치 오류(사이드바와 메인 콘텐츠 정렬 문제)를 해결하기 위해 `common.css`와 `admin-header.mustache`를 비롯한 레이아웃 파일들을 수정하였습니다.

## 변경 사항

### 1. `common.css` 레이아웃 통합 및 Flexbox 적용
- 기존에 `fixed` 포지셔닝으로 인해 발생하던 정렬 문제를 해결하기 위해 `display: flex`와 `position: sticky` 조합으로 레이아웃을 재설계했습니다.
- 중복되거나 혼란을 주던 관리자 관련 클래스(`.fh-admin-wrapper` 등)를 제거하고 사용 중인 클래스(`.admin-sidebar`, `.admin-content`, `.admin-header`)로 스타일을 일원화했습니다.
- 사이드바 너비를 280px로 고정하고, 메인 콘텐츠 영역이 나머지 가변 너비를 모두 차지하도록 `flex: 1`을 적용했습니다.

### 2. `admin-header.mustache` 구조 최적화
- 모든 관리자용 CSS를 헤더에서 로드하던 방식을 정리하고, 공통 스타일인 `common.css` 위주로 정돈했습니다.
- HTML 구조를 `fh-admin-layout > (admin-sidebar + admin-content > (admin-header + main))` 순서로 체계화했습니다.

### 3. `admin-footer.mustache` 및 `dashboard.mustache` 보정
- 헤더에서 시작된 `admin-content` 및 `main` 태그가 푸터에서 올바르게 닫히도록 수정했습니다.
- `dashboard.mustache`에서 불필요하게 중복된 여백용 `div`를 제거하여 레이아웃 일관성을 확보했습니다.

## 검증 결과
- **정렬 확인**: 사이드바가 왼쪽에 고정되고, 메인 콘텐츠가 오른쪽 나머지 영역을 가득 채우는 것을 확인했습니다.
- **스크롤 동작**: 메인 콘텐츠가 길어질 경우 상단 헤더와 좌측 사이드바가 적절히 고정(Sticky)되어 사용자 경험이 개선되었습니다.
- **반응성 고려**: `min-width: 0` 설정을 통해 Flex 자식 요소 내의 테이블 등이 레이아웃을 깨뜨리지 않도록 방지했습니다.

## 비유를 통한 설명
이전의 레이아웃은 마치 **"포스트잇(Fixed)"**을 모니터 여기저기에 붙여놓은 것 같아, 화면 크기가 바뀌면 글자가 가려지거나 정렬이 어긋나는 상태였습니다. 이번 작업을 통해 화면을 **"책상(Flex Layout)"**으로 만들고, 왼쪽에는 **"수납장(Sidebar)"**을 고정하고 오른쪽에는 **"작업대(Main Content)"**를 넓게 배치하여, 어떤 작업물이 올라와도 질서 정연하게 유지되도록 개선한 것과 같습니다.
