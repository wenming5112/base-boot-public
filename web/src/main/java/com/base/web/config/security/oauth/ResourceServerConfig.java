//package com.base.web.config.security;
//
//import com.base.web.config.security.handler.UserAccessDeniedHandler;
//import com.base.web.config.security.handler.UserAuthenticationEntryPointHandler;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//
//import javax.annotation.Resource;
//
///**
// * 资源服务
// *
// * @author ming
// * @version 1.0.0
// * @date 2021/1/14 10:11
// **/
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//// swagger v3
////    private static final String[] AUTH_WHITELIST = {
////
////            // -- swagger ui
////            "/swagger-ui.html",
////            "/swagger-ui/*",
////            "/swagger-resources/**",
////            "/v2/api-docs",
////            "/v3/api-docs",
////            "/webjars/**",
////            "/user/login",
////            "/user/logout",
////            // "/common"
////    };
//
//    private static final String[] AUTH_WHITELIST = {
//
//            // -- swagger ui
//            "/v2/api-docs", "/swagger-resources/configuration/ui",
//            "/swagger-resources", "/swagger-resources/configuration/security",
//            "/swagger-ui.html", "/css/**", "/js/**", "/images/**", "/webjars/**", "**/favicon.ico", "/index"
//    };
//
//    @Resource
//    private UserAuthenticationEntryPointHandler userAuthenticationEntryPoint;
//
//    @Resource
//    private UserAccessDeniedHandler userAccessDeniedHandler;
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.authenticationEntryPoint(userAuthenticationEntryPoint)
//                .accessDeniedHandler(userAccessDeniedHandler);
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(AUTH_WHITELIST).permitAll();
//
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();
//
//        http.csrf().disable();
//
//    }
//}
