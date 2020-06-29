package com.gc.module.file.controller;

import com.gc.module.file.pojo.bo.SysFileBO;
import com.gc.module.file.service.SysFileService;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 公共图片接口
 * @author shizhongming
 * 2020/6/28 6:13 下午
 */
@RequestMapping("image")
public class ImageController {

    private final SysFileService sysFileService;


    public ImageController(SysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }

    /**
     * 显示图片
     * @param id 图片ID
     * @param response HttpServletResponse
     */
    @GetMapping("show/{id}")
    public void show(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        SysFileBO file = this.sysFileService.download(id);
        if (Objects.nonNull(file)) {
            IOUtils.copy(file.getInputStream(), response.getOutputStream());
        }
    }
}
