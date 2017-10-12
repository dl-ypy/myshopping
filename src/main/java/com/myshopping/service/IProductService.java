package com.myshopping.service;

import com.github.pagehelper.PageInfo;
import com.myshopping.common.ServerResponse;
import com.myshopping.model.Product;
import com.myshopping.vo.ProductDetailVo;

/**
 * Created by Administrator on 2017/10/5.
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse setSaleStatus(Integer productId, Integer status);
    ServerResponse manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    ServerResponse<PageInfo> searchProduct(String productNum, Integer productId, int pageNum, int pageSize);
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
