//package com.base.web.config.security;
//
//import com.base.web.service.UserService;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//
//import javax.annotation.Resource;
//
///**
// * 认证服务
// *
// * @author ming
// * @version 1.0.0
// * @date 2021/1/14 10:09
// **/
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {
//    /**
//     * 注入authenticationManager
//     * 来支持 password grant type
//     */
//    @Resource
//    private AuthenticationManager authenticationManager;
//
//    @Resource
//    private PasswordEncoder passwordEncoder;
//
//    @Resource
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Resource
//    private UserService userService;
//
//    //http://127.0.0.1:8080/admin/hello?access_token=GOuq97fKw-O2eo-3yPp7jrTXc4A
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//        String clientId = "test";
//        String clientSecret = "123456";
//        clients.inMemory()
//                //这个好比账号
//                .withClient(clientId)
//                //授权同意的类型
//                .authorizedGrantTypes("password", "refresh_token")
//                //有效时间
//                .accessTokenValiditySeconds(1800)
//                .refreshTokenValiditySeconds(60 * 60 * 2)
//                .resourceIds("rid")
//                //作用域，范围
//                .scopes("all")
//                //密码
//                .secret(passwordEncoder.encode(clientSecret));
//    }
//
//    // 数据库加载
////    @Override
////    public void configure(ClientDetailsServiceConfigurer clients)
////            throws Exception {
////        //使用自定义的ClientDetailsService
////        clients.withClientDetails(clientDetailsService);
////    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory))
//                //身份验证管理
//                .authenticationManager(authenticationManager)
//                .userDetailsService(userService)
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE);
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        //允许客户端表单身份验证
//        security.allowFormAuthenticationForClients();
//        security.checkTokenAccess("permitAll()");
//    }
//}
