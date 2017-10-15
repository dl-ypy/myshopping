package com.myshopping.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2017/10/3.
 * 常量类
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public interface ProductListOrderBy{   //前台查询商品时的排序
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }
    public interface Cart {
        int CHECKED = 1;// 购物车选中状态
        int UN_CHECKED = 0; // 购物车未选中状态
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";   //表示购物车商品数量大于库存
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";   //表示购物车商品数量小于等于库存
    }
    public interface Role {
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }
    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}