package com.step.service.impl;

import com.step.enums.FileType;
import com.step.properties.ApplicationProperties;
import com.step.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zhushubin  on 2019-10-29.
 * email:604580436@qq.com
 * Service - 文件
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Override
    public boolean isValid(FileType fileType, MultipartFile multipartFile) {
        return false;
    }

    @Override
    public String upload(FileType fileType, MultipartFile multipartFile, boolean async) {
        return null;
    }

    @Override
    public String upload(FileType fileType, MultipartFile multipartFile) {
        try {
            String destPath =  UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            File destFile = new File(applicationProperties.getProfile() +destPath);
            //File destFile = new File(servletContext.getRealPath(destPath));
            File destDir = destFile.getParentFile();
            if (destDir != null) {
                destDir.mkdirs();
            }
            multipartFile.transferTo(destFile);

            return destPath;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String uploadLocal(FileType fileType, MultipartFile multipartFile) {
        return null;
    }
}
