package com.base.web.config.security.utils;

import com.base.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * JWT utils
 *
 * @author : ming
 * @version 1.0
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * 密钥key
     */
    private static final String SECRET = "security";

    /**
     * JWT的发行人
     */
    private static final String ISS = "ming";

    /**
     * 自定义用户信息
     */
    private static final String ROLE_CLAIMS = "rol";

    /**
     * 过期时间是3600秒，既是1个小时
     */
    public static final long EXPIRATION = 3600L * 1000;

    /**
     * 选择了记住我之后的过期时间为7天
     */
    public static final long EXPIRATION_REMEMBER = 604800L * 1000;

    /**
     * 创建token
     *
     * @param details      登录名
     * @param isRememberMe 是否记住我
     * @return String
     */
    public static String createToken(UserDetails details, boolean isRememberMe) throws BusinessException {
        // 如果选择记住我，则token的过期时间为
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;

        HashMap<String, Object> map = new HashMap<>();
        // 角色名字,"ROLE_"开头的权限才是用户角色
        map.put(ROLE_CLAIMS, details.getAuthorities());
        return Jwts.builder()
                // 加密算法
                .signWith(SignatureAlgorithm.HS512, SECRET)
                // 自定义信息
                .setClaims(map)
                // jwt发行人
                .setIssuer(ISS)
                // jwt面向的用户
                .setSubject(details.getUsername())
                // jwt发行时间
                .setIssuedAt(new Date())
                // jwt过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    /**
     * 从token获取用户信息
     *
     * @param token token
     * @return username
     */
    public static String getUsername(String token) throws BusinessException {
        return getTokenBody(token).getSubject();
    }

    /**
     * 从token中获取用户角色
     *
     * @param token token
     * @return GrantedAuthority
     */
    @SuppressWarnings("unchecked")
    public static Set<String> getUserRole(String token) throws BusinessException {
        List<GrantedAuthority> userAuthorities = (List<GrantedAuthority>) getTokenBody(token).get(ROLE_CLAIMS);
        return AuthorityUtils.authorityListToSet(userAuthorities);
    }

    /**
     * 是否已过期
     *
     * @param token token
     * @return boolean
     */
    private static boolean isExpiration(String token) throws BusinessException {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 解析token
     *
     * @param token token
     * @return Claims
     * @throws BusinessException e
     */
    private static Claims getTokenBody(String token) throws BusinessException {

        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    /**
     * 验证token
     *
     * @param token       token
     * @param userDetails user
     * @return boolean
     */
    public static boolean validateToken(String token, UserDetails userDetails) throws BusinessException {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isExpiration(token));
    }

}
