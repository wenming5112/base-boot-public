package com.base.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PatternUtil {

    private static final String ID_CARD_REGEX = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$";

    /**
     * 手机正则
     */
    public final static String TELEPHONE_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    /**
     * 邮箱正则
     */
    public final static String EMAIL_REG = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 用户名正则，首个必须是字母,并且只由数字和字母组成,长度4-20位
     */
    public final static String USERNAME_REG = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$";

    /**
     * IP正则
     */
    public final static String IP_REG = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

    /**
     * 首个必须是字母,并且只由数字和字母组成,长度4-20位
     */
    public final static String NUMBER_CHAR_REG = "^[a-zA-Z][a-zA-Z0-9]{3,19}$";

    /**
     * 密码正则，首个必须是字母，有数字字母下划线组成长度位6-20位
     */
    public final static String PASSWORD_REG = "^[a-zA-Z][a-zA-Z0-9_]{5,19}";

    /**
     * 强密码规则，密码强度正则，最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
     */
    public final static String PASSWORD_STRICT_REG = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$";

    /**
     * Verify mailbox format
     *
     * @return boolean
     */
    public static boolean verifyEmail(String email) {
        return Pattern.matches(EMAIL_REG, email);
    }

    /**
     * Verify cell-phone number format
     *
     * @return boolean
     */
    public static boolean verifyTel(String tel) {
        return Pattern.matches(TELEPHONE_REG, tel);
    }

    /**
     * Verify ID number format
     *
     * @return boolean
     */
    public static boolean verifyIdNum(String idNum) {
        return Pattern.matches(ID_CARD_REGEX, idNum);
    }

    /**
     * Verify SMS verification code format
     *
     * @return boolean
     */
    public static boolean verifyCode(String cacheCode, String code) {
        return StringUtils.equalsIgnoreCase(cacheCode, code);
    }

    /**
     * \w Matching letters or numbers or underscores or Chinese characters is equivalent to '[^A-Za-z0-9_]'。
     * Verify string length
     *
     * @return boolean
     */
    public static boolean verifyLength(String str, Integer min, Integer max) {
        return Pattern.matches("\\w*" + min + "," + max + "}", str);
    }

    /**
     * Verify whether it is a pure number (can contain negative numbers, can't have spaces)
     *
     * @return boolean
     */
    public static boolean verifyNumber(String number) {
        return Pattern.matches("^-?[0-9]+", number.trim());
    }

}
