package com.douyin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class DouyinServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DouyinServerApplication.class, args);
    }

}
