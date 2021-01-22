package com.base.web;

import com.github.hiwepy.ip2region.spring.boot.EnableIP2region;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * boot
 *
 * @author ming
 * @version 1.0.0
 * @since 2020/12/31 1:56
 **/
@EnableIP2region
@SpringBootApplication()
@ComponentScan(basePackages = {"com.base.web", "com.base.common", "com.base.message"})
@MapperScan({"com.base.web.dao"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

//    /**
//     * 密码加密解密的配置
//     *
//     * @return PasswordEncoder
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
