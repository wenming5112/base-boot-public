package com.base.web.service;

import com.base.common.ApiResponse;
import com.base.common.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author ming
 * @version 1.0.0
 * @date 2019 19:35
 */
public interface CommonService {


    /**
     * 文件上传(local)
     *
     * @param file 文件
     * @return json
     * @throws BusinessException e
     */
    ApiResponse uploadFile(MultipartFile file) throws BusinessException;

    /**
     * 邮箱验证
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return res
     * @throws BusinessException e
     */
    Boolean emailVerify(String email, String code) throws BusinessException;

    void deleteFile(File... files);

    /**
     * 文件上传(oss)
     *
     * @param file 文件
     * @return json
     * @throws BusinessException e
     */
    ApiResponse ossUploadFile(MultipartFile file) throws BusinessException;

    /**
     * 文件删除(oss)
     *
     * @param fileName 文件名
     * @return json
     * @throws BusinessException e
     */
    ApiResponse ossDeleteFile(String fileName) throws BusinessException;

}
