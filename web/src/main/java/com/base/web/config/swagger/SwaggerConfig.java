package com.base.web.config.swagger;

import com.base.web.config.security.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * Swagger config
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/15
 **/
@Configuration
//@EnableOpenApi
@EnableSwagger2
@AllArgsConstructor
public class SwaggerConfig {

    private final SwaggerProperties properties;

    /**
     * frps-console (7000)
     * frp_0.34.3_linux_386 (ali Centos 7)
     * url: http://gua.esbug.com:7000/
     * username: admin
     * password: GuaAdmin
     * <p>
     * Client
     * cmd : frpc: frpc.exe -c frpc.ini
     * <p>
     * http://gua.esbug.com:7000/swagger-ui/index.html
     * swagger-ui V3:
     * http://localhost:8818/swagger-ui/index.html
     * swagger-ui V2:
     * http://localhost:8818/swagger-ui.html
     */

    @Bean
    public Docket createRestApi(Environment environment) {
        //设置要显示swagger的环境
        Profiles profiles = Profiles.of("uat", "dev");
        //判断不同环境中profiles的布尔值,并将enable传到enable(enable)方法中
        Boolean enable = environment.acceptsProfiles(profiles);

        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        parameterBuilder.name(JwtTokenUtils.TOKEN_HEADER).description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .groupName("通用模块")
                .enable(enable)
                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo())
                // 选择哪些接口作为swagger的doc发布
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(newHashSet("https", "http"))
                .globalOperationParameters(parameters)
                //.globalRequestParameters()
                // 授权信息设置，必要的header token等认证信息
                /*.securitySchemes(Collections.singletonList(securitySchema()))
                // 授权信息全局应用
                .securityContexts(Collections.singletonList(securityContext()))*/;
    }

    /**
     * API 文档相关信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .termsOfServiceUrl(properties.getUrl())
                .version(properties.getVersion())
                .contact(new Contact(properties.getContact().getName(), properties.getContact().getUrl(), properties.getContact().getEmail()))
                .build();
    }


//---
//
//    @Bean
//    public SecurityScheme apiKey() {
//        return new ApiKey(HttpHeaders.AUTHORIZATION, "apiKey", "header");
//    }
//
//    private OAuth securitySchema() {
//        List<AuthorizationScope> authorizationScopeList = new ArrayList<>();
//        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
//        authorizationScopeList.add(new AuthorizationScope("write", "access all"));
//        List<GrantType> grantTypes = new ArrayList<>();
//        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
//        grantTypes.add(passwordCredentialsGrant);
//
//        return new OAuth("oauth2", authorizationScopeList, grantTypes);
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth())
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
//        authorizationScopes[0] = new AuthorizationScope("read", "read all");
//        authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
//        authorizationScopes[2] = new AuthorizationScope("write", "write all");
//        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
//    }
//
//    @Bean
//    SecurityConfiguration security() {
//        return SecurityConfigurationBuilder.builder()
//                .clientId("client_test")
//                .clientSecret("secret_test")
//                .realm("test-app-realm")
//                .appName("test-app")
//                .scopeSeparator(",")
//                .additionalQueryStringParams(null)
//                .useBasicAuthenticationWithAccessCodeGrant(false)
//                .build();
//    }
//
//    @Bean
//    UiConfiguration uiConfig() {
//        return UiConfigurationBuilder.builder()
//                .deepLinking(true)
//                .displayOperationId(false)
//                .defaultModelsExpandDepth(1)
//                .defaultModelExpandDepth(1)
//                .defaultModelRendering(ModelRendering.EXAMPLE)
//                .displayRequestDuration(false)
//                .docExpansion(DocExpansion.NONE)
//                .filter(false)
//                .maxDisplayedTags(null)
//                .operationsSorter(OperationsSorter.ALPHA)
//                .showExtensions(false)
//                .tagsSorter(TagsSorter.ALPHA)
//                .validatorUrl(null)
//                .build();
//    }

//---

//    @Bean
//    List<GrantType> grantTypes() {
//        List<GrantType> grantTypes = new ArrayList<>();
//        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
//                "accessTokenUri",
//                "client_123", "123456");
//        TokenEndpoint tokenEndpoint = new TokenEndpoint(accessTokenUri, "access_token");
//        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
//        return grantTypes;
//    }
//
//    @Bean
//    SecurityScheme oauth() {
//        return new OAuthBuilder()
//                .name("OAuth2")
//                .scopes(scopes())
//                .grantTypes(grantTypes())
//                .build();
//    }
//
//    private List<AuthorizationScope> scopes() {
//        return Collections.singletonList(new AuthorizationScope("openid", "Grants openid access"));
//    }
//
//    @Bean
//    public SecurityConfiguration securityInfo() {
//        return new SecurityConfiguration("client_123",
//                "123456",
//                "realm", "client_123",
//                "apiKey", ApiKeyVehicle.HEADER, "api_key", "");
//    }

// ---

//    private AuthorizationScope[] scopes() {
//        return new AuthorizationScope[]{
//                new AuthorizationScope("all", "all scope")
//        };
//    }
//
//    private SecurityScheme securityScheme() {
//        GrantType grant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
//        return new OAuthBuilder().name("OAuth2")
//                .grantTypes(Collections.singletonList(grant))
//                .scopes(Arrays.asList(scopes()))
//                .build();
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(Arrays.asList(new SecurityReference("OAuth2", scopes())))
//                .forPaths(PathSelectors.any())
//                .build();
//    }

    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }

}
