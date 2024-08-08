package org.personal.batchexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 스케줄링 어노테이션 활성화 시킴
@EnableScheduling
public class BatchExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchExampleApplication.class, args);
    }

}
