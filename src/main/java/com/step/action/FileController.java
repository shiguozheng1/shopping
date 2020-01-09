package com.step.action;

import com.step.enums.FileType;
import com.step.service.FileService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhushubin  on 2019-10-29.
 * email:604580436@qq.com
 * Controller - 文件上传
 */
@Controller("fileController")
@RequestMapping("/file")
public class FileController extends BaseController
{
    @Autowired
    private FileService fileService;
    /**
     * 上传
     */
    @PostMapping("/upload")
    @ResponseBody
    public Wrapper upload(FileType fileType, MultipartFile file) {
         String url = fileService.upload(fileType,file);

        return WrapMapper.ok("http://localhost:8080/upload/profile/"+url);
    }
}
