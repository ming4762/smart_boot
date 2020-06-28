package com.gc.system.controller;

import com.gc.starter.crud.controller.BaseController;
import com.gc.system.model.SysFunctionPO;
import com.gc.system.service.SysFunctionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能controller
 * @author 史仲明
 * 2020/1/27 12:16 下午
 */
@RestController
@RequestMapping("sys/function")
public class SysFunctionController extends BaseController<SysFunctionService, SysFunctionPO> {
}
