package com.myshopping.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2017/10/6.
 */
public interface IFileService {
    String upLoad(MultipartFile file, String path);
}
