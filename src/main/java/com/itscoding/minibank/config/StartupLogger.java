package com.itscoding.minibank.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor     // final 필드를 받는 생성자를 만들어 준다 → 생성자 주입
public class StartupLogger implements CommandLineRunner {

    private final Clock clock;   // AppConfig에서 @Bean으로 등록한 Clock이 주입된다

    @Override
    public void run(String... args) {
        log.info("미니뱅크가 시작되었습니다 — 현재 시각: {}", LocalDateTime.now(clock));
        log.debug("DEBUG 로그 — dev 프로파일에서만 보입니다");
    }
}