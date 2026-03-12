<!-- Parent: ../AI-CONTEXT.md -->

# global/_core/util/

## 목적
AJAX 엔드포인트의 JSON 응답 형식을 통일하는 공통 응답 래퍼 유틸리티.

## 주요 파일
| 파일명 | 설명 |
|--------|------|
| `Resp.java` | 제네릭 응답 래퍼. `status(int)`, `msg(String)`, `body(T)` 필드 + 정적 팩토리 메서드 |

## Resp 사용법
```java
// 성공 응답 (AJAX 컨트롤러에서)
return Resp.ok(someData);
// → ResponseEntity<Resp<T>> {status:200, msg:"성공", body:{...}}

// 실패 응답 (GlobalExceptionHandler에서)
return Resp.fail(HttpStatus.BAD_REQUEST, "에러 메시지");
// → ResponseEntity<?> {status:400, msg:"에러 메시지", body:null}
```

## AI 작업 지침
- **AJAX 엔드포인트 필수 사용**: 모든 AJAX 응답은 `Resp.ok()` 또는 `Resp.fail()`로 반환하여 프론트엔드 파싱 통일
- **일반 폼 요청에는 사용 금지**: 폼 요청 성공 → `redirect:/{path}`, 실패 → `GlobalExceptionHandler`의 Script 응답
- **타입 안정성**: `Resp<T>`는 제네릭이므로 `Resp.ok(List<UserResponse> data)` 등 구체 타입 지정 권장

## 의존성
- 외부: Lombok (`@Data`), Spring WebMvc (`ResponseEntity`, `HttpStatus`)
