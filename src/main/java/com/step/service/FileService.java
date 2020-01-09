package com.step.service;

import com.step.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by zhushubin  on 2019-10-29.
 * email:604580436@qq.com
 * Service - 文件
 */
public interface FileService {
    /**
     * 文件验证
     *
     * @param fileType
     *            文件类型
     * @param multipartFile
     *            上传文件
     * @return 文件验证是否通过
     */
    boolean isValid(FileType fileType, MultipartFile multipartFile);

    /**
     * 文件上传
     *
     * @param fileType
     *            文件类型
     * @param multipartFile
     *            上传文件
     * @param async
     *            是否异步
     * @return 访问URL
     */
    String upload(FileType fileType, MultipartFile multipartFile, boolean async);

    /**
     * 文件上传(异步)
     *
     * @param fileType
     *            文件类型
     * @param multipartFile
     *            上传文件
     * @return 访问URL
     */
    String upload(FileType fileType, MultipartFile multipartFile);

    /**
     * 文件上传至本地(同步)
     *
     * @param fileType
     *            文件类型
     * @param multipartFile
     *            上传文件
     * @return 路径
     */
    String uploadLocal(FileType fileType, MultipartFile multipartFile);
}
