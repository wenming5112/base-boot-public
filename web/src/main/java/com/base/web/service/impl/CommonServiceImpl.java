package com.base.web.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.common.ApiResponse;
import com.base.common.constant.WebConstant;
import com.base.common.entity.sys.SysFileStore;
import com.base.common.enumeration.ResponseMessageEnum;
import com.base.common.exception.BusinessException;
import com.base.common.utils.FileHashUtils;
import com.base.common.utils.RedisCacheUtil;
import com.base.web.config.upload.FileUploadProperties;
import com.base.web.dao.SysFileStoreMapper;
import com.base.web.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * 公共模块 实现类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/18
 */

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Resource
    private SysFileStoreMapper fileStoreMapper;

    @Resource
    private FileUploadProperties fileUploadProperties;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse uploadFile(MultipartFile file) throws BusinessException {
        if (ObjectUtils.isEmpty(file)) {
            throw new BusinessException("不能上传空文件");
        }
        File newFile;
        try {
            String fileHash = FileHashUtils.getSha256ByStream(file.getInputStream());
            String path = fileUploadProperties.getLocal().getPath();
            File folderPath = new File(path);
            if (!folderPath.exists() && !folderPath.isDirectory()) {
                if (folderPath.mkdirs()) {
                    log.warn(String.format("Path %s is not exists, it will create new directory!!", path));
                }
            }
            //获取文件名，带后缀
            String fileName = file.getOriginalFilename();
            String fileSuffix = "";
            if (Objects.requireNonNull(fileName).contains(WebConstant.FILE_POINT)) {
                fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            }
            fileName = fileHash;
            newFile = new File(path + fileName + fileSuffix);
            String url = fileUploadProperties.getLocal().getUrl() + fileName + fileSuffix;

            //将上传的文件写到服务器上指定的文件路径
            file.transferTo(newFile);

            SysFileStore fileCache = fileStoreMapper.selectOne(new QueryWrapper<SysFileStore>().lambda()
                    .eq(SysFileStore::getName, fileName)
                    .and(wrapper -> wrapper.eq(SysFileStore::getValid, Boolean.TRUE)));
            if (ObjectUtils.isEmpty(fileCache)) {
                fileCache = new SysFileStore();
                fileCache.setSize(file.getSize());
                fileCache.setName(fileName);
                fileCache.setSuffix(fileSuffix);
                fileCache.setUrl(url);
                //fileCache.setCreator(JwtUtil.getUserNameFromRedis());
                fileCache.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                if (fileStoreMapper.insert(fileCache) < 1) {
                    throw new BusinessException("文件上传失败");
                }
            }

        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
        return ApiResponse.successful(ResponseMessageEnum.OPERATION_SUCCESS.getCode(), "上传成功");
    }

    /**
     * 邮箱验证(内部验证使用)
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return res
     * @throws BusinessException e
     */
    @Override
    public Boolean emailVerify(String email, String code) throws BusinessException {
        if (!redisCacheUtil.hasKey(email)) {
            throw new BusinessException("Your email verification code is timeout!! Please send again!!");
        }
        String verifyCode = String.valueOf(redisCacheUtil.getString(email));
        if (verifyCode.equals(code)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 删除
     *
     * @param files f
     */
    @Override
    public void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                String path = file.getAbsolutePath();
                if (file.delete()) {
                    log.info(String.format("File: %s delete success!!", path));
                }
            }
        }
    }

    /**
     * 普通文件上传(一般不推荐在后端使用，
     * 当上传文件超过1M，AWS服务器(海外节点)调用的时候非常的慢，
     * 尽量在前端控制文件上传，并取得url提供给后端)
     *
     * @param file 文件
     * @return ApiResponse
     * @throws BusinessException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse ossUploadFile(MultipartFile file) throws BusinessException {
        try {
            // 处理文件
            String fileName = file.getOriginalFilename();
            Assert.isTrue(!ObjectUtils.isEmpty(fileName), "请选择要上传的文件");
            String fileSuffix = "";
            if (Objects.requireNonNull(fileName).contains(WebConstant.FILE_POINT)) {
                fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            }
            // 新文件名
            String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
            // 阿里云ossSDK客户端
            OSS client = new OSSClientBuilder().build(fileUploadProperties.getOss().getAliOss().getEndPoint(),
                    fileUploadProperties.getOss().getAliOss().getAccessKey(),
                    fileUploadProperties.getOss().getAliOss().getAccessSecret());
            InputStream stream = file.getInputStream();
            // 把文件流上传
            client.putObject(fileUploadProperties.getOss().getAliOss().getBucketName(), newFileName, stream);
            client.shutdown();
            // 数据库存储上传记录
            SysFileStore fileCache = new SysFileStore();
            fileCache.setSize(file.getSize());
            fileCache.setName(newFileName);
            fileCache.setUrl(fileUploadProperties.getOss().getAliOss().getUrl() + "/" + newFileName);
            fileCache.setSuffix(fileSuffix);
            fileCache.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            fileCache.setCreator("临时测试用户");
            if (fileStoreMapper.insert(fileCache) > 0) {
                return ApiResponse.successful(11111, "文件上传成功", fileCache.getUrl());
            }
        } catch (Exception e) {
            log.error("oss文件上传异常信息 ： " + e.getMessage());
            throw new BusinessException(e.getMessage());
        }
        return ApiResponse.failed(11111, "文件上传失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse ossDeleteFile(String fileName) throws BusinessException {
        try {
            // 阿里云ossSDK客户端
            OSS client = new OSSClientBuilder().build(fileUploadProperties.getOss().getAliOss().getEndPoint(),
                    fileUploadProperties.getOss().getAliOss().getAccessKey(),
                    fileUploadProperties.getOss().getAliOss().getAccessSecret());
            // 把文件流上传
            client.deleteObject(fileUploadProperties.getOss().getAliOss().getBucketName(), fileName);
            // 数据库操作
            return ApiResponse.successful(1111, "oss文件删除成功");
        } catch (Exception e) {
            log.error("oss文件删除异常信息 ： " + e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }
}
