package com.myshopping.service;

import com.myshopping.common.ServerResponse;
import com.myshopping.vo.CartVo;

/**
 * Created by Administrator on 2017/10/13.
 */
public interface ICartService {
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> add(Integer userId, Integer prouctId, Integer count);
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);
    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
