package com.gc.module.file.controller;

import com.gc.module.file.model.SysFilePO;
import com.gc.module.file.service.SysFileService;
import com.gc.starter.crud.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jackson
 * 2020/1/27 7:51 下午
 */
@RestController
@RequestMapping("file")
public class SysFileController extends BaseController<SysFileService, SysFilePO> {
}
