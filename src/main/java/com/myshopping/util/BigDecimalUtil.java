package com.myshopping.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/10/14.
 * 封装BigDecimal类型的加减乘除
 * 思路：数据库中存储的是double类型的，在此工具类中将其转换为String类型
 *       再调用BigDecimal的加减乘除方法。
 */
public class BigDecimalUtil {
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //要考虑除不尽的情况
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP); //四舍五入，保留2位小数
    }
}
