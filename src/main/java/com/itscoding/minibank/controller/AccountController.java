package com.itscoding.minibank.controller;
 
import jakarta.validation.Valid;
import com.itscoding.minibank.dto.AccountCreateRequestDto;
import com.itscoding.minibank.dto.AccountResponseDto;
import com.itscoding.minibank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController // JSON 데이터를 반환하는 RESTful 컨트롤러임을 명시합니다.
@RequestMapping("/api/accounts") // 이 컨트롤러의 기본 URL 경로를 설정합니다.
@RequiredArgsConstructor
public class AccountController {
 
    private final AccountService accountService;
 
    // POST /api/accounts (계좌 생성)
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody AccountCreateRequestDto request) {
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created 반환
    }
 
    // GET /api/accounts/{id} (단건 조회)
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id)); // 200 OK 반환
    }
 
    // GET /api/accounts (전체 조회)
    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
}
