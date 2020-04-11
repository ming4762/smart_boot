package com.gc.module.faq.controller;

import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import com.gc.common.base.model.Tree;
import com.gc.common.base.utils.TreeUtils;
import com.gc.module.faq.model.FaqCategoryPO;
import com.gc.module.faq.service.FaqCategoryService;
import com.gc.starter.crud.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/4/11 11:36 上午
 */
@RestController
@RequestMapping("faq/category")
public class FaqCategoryController extends BaseController<FaqCategoryService, FaqCategoryPO> {

    /**
     * 加载所有树形结构
     * @return
     */
    @PostMapping("listAllTree")
    public Result<List<Tree<FaqCategoryPO>>> listAllTree() {
        List<Tree<FaqCategoryPO>> faqCategoryList = this.service.list()
                .stream()
                .map(item -> {
                    Tree<FaqCategoryPO> tree = new Tree<>();
                    tree.setId(item.getCategoryId());
                    tree.setText(item.getCategoryName());
                    tree.setData(item);
                    tree.setParentId(item.getParentId());
                    return tree;
                }).collect(Collectors.toList());
        return Result.success(TreeUtils.buildList(faqCategoryList, 0L));
    }

    /**
     * 保存修改操作
     * @param model
     * @return
     */
    @Override
    @PostMapping("saveUpdate")
    protected Result<Boolean> saveUpdate(@RequestBody FaqCategoryPO model) {
        return super.saveUpdate(model);
    }

    /**
     * 批量删除操作
     * @param idList
     * @return
     */
    @PostMapping("batchDeleteById")
    public Result<Boolean> batchDeleteById(@RequestBody List<Long> idList) {
        if (idList.isEmpty()) {
            return Result.ofStatus(HttpStatus.PARAM_NOT_NULL, "id集合不能为空");
        }
        return Result.success(this.service.removeByIds(idList));
    }

}
