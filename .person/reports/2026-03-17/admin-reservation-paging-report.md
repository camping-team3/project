# 리포트: 관리자 예약 관리 페이징(Paging) 구현 및 기술 해설
날짜: 2026-03-17
작성자: parkcoding
작업 단계: Phase 2 - Task 6 (성능 최적화)

## 📚 1. 페이징(Paging)이란 무엇인가?

데이터가 1,000개, 10,000개가 넘어가면 한 화면에 모두 보여줄 수 없습니다. 이때 데이터를 **'특정 개수(예: 10개)'**씩 나누어 번호를 매겨 관리하는 기술을 **페이징**이라고 합니다.

- **서버 사이드 페이징(Server-side Paging)**: 이번에 우리가 쓴 방식입니다. DB에서 아예 "11번부터 20번까지만 가져와!"라고 요청합니다. 데이터가 아무리 많아도 서버와 네트워크에 부담이 적습니다.
- **클라이언트 사이드 페이징(Client-side Paging)**: 일단 10,000개를 다 가져온 뒤 브라우저(JS)에서만 숨기는 방식입니다. 데이터가 적을 땐 빠르지만, 많아지면 브라우저가 멈출 수 있습니다.

---

## 💻 2. 핵심 코드 및 기술 해설

### A. Repository: DB에 "딱 이만큼만" 요청하기
Spring Data JPA의 `Pageable` 인터페이스를 사용하면 SQL의 `LIMIT`, `OFFSET`을 직접 짜지 않아도 됩니다.

```java
// ReservationRepository.java
@Query(value = "SELECT r FROM Reservation r JOIN FETCH ... ",
       countQuery = "SELECT COUNT(r) FROM Reservation r ...") // 💡 카운트 쿼리 필수!
Page<Reservation> findAllAdminSearch(..., Pageable pageable);
```
- **핵심**: `Page<T>`를 반환형으로 쓰면, JPA는 데이터를 가져오는 쿼리 외에 **"전체 데이터가 총 몇 개인가?"**를 묻는 `countQuery`를 자동으로 실행합니다. 그래야 전체 페이지 수를 계산할 수 있기 때문입니다.

### B. Service: 페이징 메타데이터 계산기
DB에서 가져온 `Page` 객체에는 데이터뿐만 아니라 현재 몇 페이지인지, 다음 페이지가 있는지 등의 정보가 들어있습니다.

```java
// ReservationService.java
public AdminResponse.ReservationPageDTO findAllForAdmin(..., Pageable pageable) {
    Page<Reservation> page = reservationRepository.findAllAdminSearch(..., pageable);
    
    // 💡 Mustache는 숫자를 1, 2, 3 나열하는 로직이 약하므로 서버에서 미리 계산합니다.
    int currentPage = page.getNumber(); // 0부터 시작
    int startPage = Math.max(0, (currentPage / 5) * 5); // 5개씩 묶음의 시작 (0, 5, 10...)
    int endPage = Math.min(startPage + 4, page.getTotalPages() - 1);

    List<AdminResponse.PageNumberDTO> pageNumbers = IntStream.rangeClosed(startPage, endPage)
            .mapToObj(n -> AdminResponse.PageNumberDTO.builder()
                    .number(n)        // 서버가 이해하는 0, 1, 2
                    .displayDigit(n+1) // 사용자가 보는 1, 2, 3
                    .isCurrent(n == currentPage) // 현재 페이지 강조용
                    .build())
            .toList();
            
    // ... DTO에 담아서 반환
}
```

### C. Controller: 사용자의 요청 전달
사용자가 "3페이지 보여줘!"라고 하면 이를 수신합니다.

```java
// AdminController.java
@GetMapping("/admin/reservations")
public String reservationList(..., @RequestParam(defaultValue = "0") int page, Model model) {
    // 💡 PageRequest.of(페이지번호, 한 페이지 크기, 정렬방식)
    Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
    AdminResponse.ReservationPageDTO response = reservationService.findAllForAdmin(searchDTO, pageable);
    
    model.addAttribute("response", response);
    return "admin/reservation/list";
}
```

---

## 🧸 3. 초등학생도 이해하는 쉬운 비유

페이징은 **'두꺼운 백과사전의 목차'**와 같습니다.

1.  **데이터가 1,000개인 상태**: 1,000페이지짜리 백과사전이 있습니다.
2.  **페이징이 없는 경우**: 누가 "사자"를 찾으라고 하면, 1페이지부터 1,000페이지까지 모든 종이를 한꺼번에 다 펼쳐서 바닥에 깔아놓는 것과 같습니다. (컴퓨터가 힘들어하겠죠?)
3.  **페이징이 있는 경우**: 
    - 사전 아래에 **[1] [2] [3] ... [100]** 이라는 버튼이 있습니다.
    - 내가 **[3]**번 버튼을 누르면, 사전은 딱 **21페이지부터 30페이지**까지만 내 눈앞에 보여줍니다. 
    - 나머지 990페이지는 책장에 그대로 꽂혀 있습니다. (컴퓨터가 아주 편안해합니다!)

---

## 💡 4. 공부 포인트 (핵심 용어)

1.  **Offset(오프셋)**: "어디서부터 건너뛸까?" (0부터 시작)
2.  **Limit(리미트)**: "몇 개나 가져올까?" (보통 10개)
3.  **Pageable**: Spring에서 오프셋과 리미트를 아주 쉽게 관리해주는 도구함.
4.  **Page vs List**: `List`는 그냥 데이터 뭉치지만, `Page`는 **"데이터 + 총 개수 + 다음 페이지 존재 여부"**가 합쳐진 똑똑한 패키지 상품.

---

이 내용을 바탕으로 코드를 다시 보시면, 왜 `Pageable`을 파라미터로 넘기고 `PageNumbers`를 따로 계산했는지 더 명확하게 이해되실 겁니다!
