package com.itscoding.minibank.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j                 // log 변수를 Lombok이 만들어 준다
@Component
public class StartupLogger implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("미니뱅크가 시작되었습니다");
        log.debug("DEBUG 로그 — dev 프로파일에서만 보입니다");
    }
}