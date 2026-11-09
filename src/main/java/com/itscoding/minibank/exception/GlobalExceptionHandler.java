package com.itscoding.minibank.exception;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import java.util.Map;
 
@RestControllerAdvice // 애플리케이션 전역에서 발생하는 예외를 낚아채서 처리합니다.
public class GlobalExceptionHandler {
 
    // 우리가 만든 AccountNotFoundException이 발생하면 이 메서드가 실행됩니다.
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 상태 코드
                .body(Map.of("message", e.getMessage()));
    }
 
    // @Valid 검증에 실패하면 이 예외가 발생합니다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException e) {
 
        // 발생한 에러 중 첫 번째 에러 메시지를 꺼내옵니다.
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
 
        return ResponseEntity.badRequest() // 400 상태 코드
                .body(Map.of("message", message));
    }

    // [6장 추가] 고객을 찾을 수 없을 때 (404)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFound(
            CustomerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
    }

    // [6장 추가] 업무 규칙 충돌 — 예: 계좌가 있는 고객 삭제 (409)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(
            IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
    }


    // 🔥 [신규] 잔액 부족 에러 처리 (409)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientBalance(
            InsufficientBalanceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
    }

    // 🔥 [신규] 잘못된 요청 값 (예: 같은 계좌로 이체, 0 이하의 한도) (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
    }

    // 🔥 [신규] 낙관적 락 충돌 — 다른 요청이 같은 계좌를 먼저 수정함 (409)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLock(
            ObjectOptimisticLockingFailureException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "다른 거래가 먼저 처리되었습니다."));
    }

}