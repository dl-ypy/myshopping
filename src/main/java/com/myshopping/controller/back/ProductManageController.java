package com.myshopping.controller.back;

import com.google.common.collect.Maps;
import com.myshopping.common.Const;
import com.myshopping.common.ResponseCode;
import com.myshopping.common.ServerResponse;
import com.myshopping.model.Product;
import com.myshopping.model.User;
import com.myshopping.service.IFileService;
import com.myshopping.service.IProductService;
import com.myshopping.service.IUserService;
import com.myshopping.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/5.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 添加或修改产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value="product_save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //添加或修改产品的业务逻辑
            System.out.println("----------------"+product.getSubtitle());
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("权限不够，需要管理员登陆！");
        }
    }

    /**
     * 修改商品上下架状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value="set_Sale_Status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //商品上下架的业务逻辑
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("权限不够，需要管理员登陆！");
        }
    }

    /**
     * 获取商品详细信息
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value="detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getDetail(HttpSession session, Integer productId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取商品信息的业务逻辑
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("权限不够，需要管理员登陆！");
        }
    }

    /**
     * 分页查询
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //分页查询的业务逻辑
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("权限不够，需要管理员登陆！");
        }
    }

    /**
     * 根据名称或id分页查询商品
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //分页搜索的业务逻辑
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        } else {
            return ServerResponse.createByErrorMessage("权限不够，需要管理员登陆！");
        }
    }

    /**
     * 上传文件
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value="upLoad.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upLoad(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upLoad");
            String targetFileName = iFileService.upLoad(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("权限不够，需要管理员登陆！");
        }
    }

    /**
     * 上传富文本
     * @param session
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="richtext_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登陆管理员!");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upLoad");
            String targetFileName = iFileService.upLoad(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败！");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;

        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "不是管理员!");
            return resultMap;
        }
    }
}
