package com.base.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * @author ming
 * @version 1.0.0
 * @since 2020/12/31 0:04
 **/

public class BigDecimalUtil {

    public static String format(String target) {
        DecimalFormat df = new DecimalFormat("#0.###");
        return df.format(new BigDecimal(target));
    }

    public static String formatSuffix(String target, int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < length; i++) {
            s.append("#");
        }
        DecimalFormat df = new DecimalFormat("#0.#" + s);
        BigDecimal var = new BigDecimal(target);
        return df.format(var);
    }

    public static String format(String target, Integer length) {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < length; i++) {
            s.append("#");
        }
        DecimalFormat df = new DecimalFormat("#0.#" + s);
        BigDecimal var = new BigDecimal(target).setScale(length, ROUND_DOWN);
        return df.format(var);
    }


    /**
     * 格式化，处理小数点后面的0，最多保持8位
     */
    public static String formatZero(String target) {
        DecimalFormat df = new DecimalFormat("#0.########");
        BigDecimal var = new BigDecimal(target);
        return df.format(var);
    }

    /**
     * 加，默认保留小数点八位
     */
    public static String add(String s, String add) {
        BigDecimal var1 = new BigDecimal(s);
        BigDecimal var2 = new BigDecimal(add);
        DecimalFormat df = new DecimalFormat("#0.########");
        return df.format(var1.add(var2));
    }

    /**
     * 减
     */
    public static String subtract(String score, String subtract) {
        BigDecimal var1 = new BigDecimal(score).setScale(8, ROUND_DOWN);
        BigDecimal var2 = new BigDecimal(subtract).setScale(5, ROUND_DOWN);
        DecimalFormat df = new DecimalFormat("#0.########");
        return df.format(var1.subtract(var2));
    }

    /**
     * 乘
     */
    public static String multiply(String score, String multiply) {
        BigDecimal var1 = new BigDecimal(score).setScale(8, ROUND_DOWN);
        BigDecimal var2 = new BigDecimal(multiply).setScale(8, ROUND_DOWN);
        DecimalFormat df = new DecimalFormat("#0.########");
        return df.format(var1.multiply(var2));
    }

    /**
     * 除
     */
    public static String divide(String score, String divide) {
        BigDecimal var1 = new BigDecimal(score).setScale(8, ROUND_DOWN);
        BigDecimal var2 = new BigDecimal(divide).setScale(8, ROUND_DOWN);
        DecimalFormat df = new DecimalFormat("#0.########");
        return df.format(var1.divide(var2, 8, ROUND_DOWN));
    }

    /**
     * 除(指定格式化长度)
     */
    public static String divide(String score, String divide, Integer length) {
        BigDecimal var1 = new BigDecimal(score).setScale(8, ROUND_DOWN);
        BigDecimal var2 = new BigDecimal(divide).setScale(8, ROUND_DOWN);
        DecimalFormat df = new DecimalFormat("#0.########");
        return df.format(var1.divide(var2, length, ROUND_DOWN));
    }

    public static String rateMultiply(String score, String rate) {
        rate = subtract("1", rate);
        return multiply(score, rate);
    }

    /**
     * 比较大小
     */
    public static int than(String score, String target) {
        BigDecimal var1 = new BigDecimal(score).setScale(8, ROUND_DOWN);
        BigDecimal var2 = new BigDecimal(target).setScale(8, ROUND_DOWN);
        return var1.compareTo(var2);
    }
}
