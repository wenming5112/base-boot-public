package com.base.common.utils;

import com.base.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取IP地址
 *
 * @author ming
 * @date 2019:08:22 15:15
 */
@Slf4j
public class IpUtil {

    /**
     * 获得用户远程地址
     */
    public static String getIp(HttpServletRequest request) {
        String i = "0:0:0:0:0:0:0:1";
        String unknown = "unknown";
        String dot = ",";
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !unknown.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(dot)) {
                ip = ip.split(dot)[0];
            }
        }

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (i.equals(ip)) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ip = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 解析域名是否可用
     *
     * @param domain 域名
     * @return true or false
     */
    public static boolean analysisDomainName(String domain) {
        InetAddress net;
        boolean isReachable = false;
        try {
            net = InetAddress.getByName(domain);
            // tcp 测试连接 timeout单位毫秒
            isReachable = net.isReachable(10000);
        } catch (UnknownHostException e) {
            log.info("无法解析域名");
            e.printStackTrace();
        } catch (IOException e) {
            log.info("解析域名超时");
            e.printStackTrace();
        }
        return isReachable;
    }

    public static boolean isDomain(String domainName) throws BusinessException {
        // 验证域名字符串的正确性
        if (StringUtils.isNotBlank(domainName)) {
            // 检验是否是 ip，如果不是ip和域名，那后面都不能被成功解析，这里不需要我验证
            String reg = "\\.";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(domainName);
            int count = 0;
            while (m.find()) {
                count++;
            }
            int i = 3;
            if (count >= i) {
                throw new BusinessException("域名不正确，例如：(xxx.com/cn/xyz/club)");
            }
        }
        return true;
    }

    /**
     * IpUtils工具类方法
     * 获取真实的ip地址
     *
     * @param request request
     * @return String
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private static Pattern p = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>");

    public static String getLocalHostExternalIp() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";
        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            ip = m.group(1);
        }
        return ip;
    }
}
