package com.base.web.config.security;

import com.base.common.utils.Md5Utils;
import com.base.web.config.security.filter.JwtAuthenticationTokenFilter;
import com.base.web.config.security.handler.*;
import com.base.web.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security config
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/13 15:04
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService userService;

    @Resource
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Resource
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Resource
    private CustomAuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Resource
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Resource
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

//    @Resource
//    private CustomPermissionEvaluator permissionEvaluator;

    /**
     * BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Implementation of user defined password encryption
     */
    @Bean
    public PasswordEncoder customPasswordEncoder() {
        return new CustomPasswordEncoder();
    }

//    @Bean
//    public MethodSecurityExpressionHandler expressionHandler() {
//        //DefaultWebSecurityExpressionHandler
//        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//        expressionHandler.setPermissionEvaluator(permissionEvaluator);
//        return expressionHandler;
//    }

    /**
     * URL whitelist
     */
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/css/**",
            "/js/**",
            "/images/**",
            "/webjars/**",
            "**/favicon.ico",
            "/index",
            "/",
            "/csrf",
            // -- user & common
            "/user/registry",
            "/user/login",
            "/user/logout",
            "/common"
    };

    /**
     * 核心配置
     *
     * @param http HttpSecurity
     * @throws Exception e
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                //登录处理接口
                //.loginPage("/login_page")
                .loginProcessingUrl("/user/login")
                //定义登录时，用户名的 key，默认为 username
                .usernameParameter("username")
                //定义登录时，用户密码的 key，默认为 password
                .passwordParameter("password")
                //登录成功处理
                .successHandler(authenticationSuccessHandler)
                //登录失败处理
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                //登出处理
                .logout()
                .logoutUrl("/user/logout")
                //清除身份信息
                .clearAuthentication(true)
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                .and()
                .cors()
                .and()
                .csrf().disable()
                //基于token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //Url白名单
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                //.expressionHandler(expressionHandler())
                .anyRequest()
                .authenticated();

        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT filter
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //无token或者token无效的相关处理
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler);

    }

    /**
     * 配置用户签名服务 主要是user-details 机制
     * (可以采用内存、数据库、loap、自定义) 这里是自定义
     *
     * @param auth 签名管理器构造器，用于构建用户具体权限控制
     * @throws Exception e
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * 自定义密码加密实现(md5)
     */
    private static class CustomPasswordEncoder implements PasswordEncoder {

        @Override
        public String encode(CharSequence plaintext) {
            return Md5Utils.md5(String.valueOf(plaintext));
        }

        @Override
        public boolean matches(CharSequence plaintext, String cipher) {
            return cipher.equals(Md5Utils.md5(String.valueOf(plaintext)));
        }
    }

}
