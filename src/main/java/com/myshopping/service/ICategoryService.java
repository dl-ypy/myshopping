package com.myshopping.service;

import com.myshopping.common.ServerResponse;
import com.myshopping.model.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/10/5.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(String categoryName, Integer categoryId);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
