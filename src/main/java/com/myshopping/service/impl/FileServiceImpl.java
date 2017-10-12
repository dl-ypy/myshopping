package com.myshopping.service.impl;

import com.google.common.collect.Lists;
import com.myshopping.service.IFileService;
import com.myshopping.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/10/6.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upLoad(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;  //通过UUID保证不重名，加上后缀名
        logger.info("开始上传文件，上传文件名：{}，上传的路径：{}，新文件名：{}", fileName,path,uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);   // 可写权限
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            //文件上传成功
            file.transferTo(targetFile);
            logger.info("文件上传到本地成功！");
            //将targetFile上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));   // TODO: 2017/10/6
            logger.info("文件上传到服务器成功！");
            //上传完，删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
