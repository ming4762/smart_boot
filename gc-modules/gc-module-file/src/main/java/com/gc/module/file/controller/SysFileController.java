package com.gc.module.file.controller;

import com.gc.common.base.message.Result;
import com.gc.module.file.model.SysFilePO;
import com.gc.module.file.pojo.bo.SysFileBO;
import com.gc.module.file.pojo.dto.SaveFileDTO;
import com.gc.module.file.service.SysFileService;
import com.gc.starter.crud.controller.BaseController;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/27 7:51 下午
 */
@RestController
@RequestMapping("file")
@Slf4j
@Api(value = "文件管理", tags = "文件管理")
public class SysFileController extends BaseController<SysFileService, SysFilePO> {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件信息", dataTypeClass = MultipartFile.class, required = true),
            @ApiImplicitParam(name = "fileName", value = "文件名", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "文件类型", defaultValue = "TEMP", dataTypeClass = String.class),
    })
    public Result<SysFilePO> upload(
            @RequestParam("file")MultipartFile multipartFile,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "type", required = false) String type
    ) {
        try {
            return Result.success(this.service.saveFile(multipartFile, SaveFileDTO.builder().type(type).filename(fileName).build()));
        } catch (Exception e) {
            log.error("上传文件发生IO错误", e);
            return Result.failure("保存文件发生IO错误");
        }
    }


    /**
     * 批量上传文件
     * @param multipartFileList
     * @param type
     * @return
     */
    @PostMapping("batchUpload")
    @ApiOperation(value = "批量上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "文件集合", required = true),
            @ApiImplicitParam(name = "type", value = "文件类型", defaultValue = "TEMP", dataTypeClass = String.class),
    })
    public Result<List<SysFilePO>> batchUpload(
            @RequestParam("files")List<MultipartFile> multipartFileList,
            @RequestParam(value = "type", required = false) String type
    ) {
        if (multipartFileList.isEmpty()) {
            return Result.success(ImmutableList.of());
        }
        try {
            return Result.success(
                    multipartFileList.stream()
                            .map(item -> this.service.saveFile(item, type))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            log.error("上传文件发生错误", e);
            return Result.failure("保存文件发生错误");
        }
    }

    /**
     * 下载文件接口
     * @param id
     * @param response
     * @throws Exception
     */
    @GetMapping("download/{id}")
    @ApiOperation(value = "下载文件")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        final SysFileBO file = this.service.downLoad(id);
        if (file != null) {
            //设置文件名并转码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getFile().getFileName(), "UTF-8"));
            response.setContentType(file.getFile().getContentType());
            IOUtils.copy(file.getInputStream(), response.getOutputStream());
        }
    }

    /**
     * 批量删除文件
     * @param ids
     * @return
     */
    @PostMapping("batchDeleteById")
    @ApiOperation("批量删除文件")
    public Result<Boolean> batchDeleteById(@RequestBody List<Long> ids) {
        return Result.success(this.service.batchDeleteFile(ids));
    }

}
