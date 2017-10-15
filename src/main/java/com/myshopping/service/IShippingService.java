package com.myshopping.service;

import com.github.pagehelper.PageInfo;
import com.myshopping.common.ServerResponse;
import com.myshopping.model.Shipping;

/**
 * Created by Administrator on 2017/10/15.
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse del(Integer userId, Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
