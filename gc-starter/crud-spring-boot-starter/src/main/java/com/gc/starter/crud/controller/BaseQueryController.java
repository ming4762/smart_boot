package com.gc.starter.crud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.constants.CrudConstants;
import com.gc.starter.crud.model.BaseModel;
import com.gc.starter.crud.model.PageData;
import com.gc.starter.crud.model.Sort;
import com.gc.starter.crud.service.BaseService;
import com.gc.starter.crud.utils.CrudUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础查询controller
 * @author shizhongming
 * 2020/1/12 6:08 下午
 */
@Slf4j
public abstract class BaseQueryController<K extends BaseService<T>, T extends BaseModel> {

    @Autowired
    protected K service;

//    @RequestMapping("list")
//    @ResponseBody
    protected Result<Object> list(@RequestBody Map<String, Object> parameter) {
        final Page<T> page = this.doPage(parameter);
        final QueryWrapper<T> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter, this.getModelType());
        final Object keyword = parameter.get(CrudConstants.keyword.name());
        if (keyword instanceof String) {
            this.addKeyword(queryWrapper, (String) keyword);
        }
        final List<T> data = this.service.list(queryWrapper, parameter, page != null);
        if (page != null) {
            return Result.success(new PageData<T>(data, page.getTotal()));
        }
        return Result.success(data);
    }

//    @RequestMapping("get")
//    @ResponseBody
    private Result<T> get(@RequestBody T model) {
        return Result.success(this.service.get(model));
    }

    /**
     * 执行分页
     * @param parameter
     * @return
     */
    @Nullable
    protected Page<T> doPage(@NotNull Map<String, Object> parameter) {
        Page<T> page = null;
        final Object limit = parameter.get(CrudConstants.limit.name());
        Object offset = parameter.get(CrudConstants.offset.name());
        if (limit != null) {
            if (!(limit instanceof Integer)) {
                log.warn("参数类型无效，limit应为int类型，limit：{}", limit);
                return null;
            }
            if (ObjectUtils.isEmpty(offset)) {
                offset = 0;
            } else if (!(offset instanceof Integer)) {
                log.warn("参数类型无效，offset应为int类型，offset：{}，使用默认值：0", offset);
                offset = 0;
            }
            // 解析排序字段
            final String orderMessage = this.analysisOrder(parameter);
            if (!StringUtils.isEmpty(orderMessage)) {
                PageHelper.orderBy(orderMessage);
            }
            page = PageHelper.offsetPage((Integer) offset, (Integer) limit);
        }
        return page;
    }

    /**
     * 解析排序字段
     * @param parameter 前台参数
     * @return
     */
    @Nullable
    protected String analysisOrder(@NotNull Map<String, Object> parameter) {
        final String sortName = (String) parameter.get(CrudConstants.sortName.name());
        if (!StringUtils.isEmpty(sortName)) {
            final String sortOrder = (String) parameter.get(CrudConstants.sortOrder.name());
            final Class<? extends BaseModel> clazz = CrudUtils.getModelClassByType(this.getModelType());
            final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, clazz);
            if (sortList.isEmpty()) {
                return null;
            }
            return sortList
                    .stream()
                    .map(sort -> MessageFormat.format("{1} {2}", sort.getDbName(), sort.getOrder()))
                    .collect(Collectors.joining(","));
        }
        return null;
    }


    /**
     * 获取实体类类型
     * @return 实体类类型
     */
    private Type getModelType() {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 添加关键字查询
     * @param queryWrapper
     * @param keyword
     */
    private void addKeyword(@NotNull QueryWrapper<T> queryWrapper, @NotNull String keyword) {
        final Class<? extends BaseModel>  clazz = CrudUtils.getModelClassByType(getModelType());
        if (clazz != null) {
            final Field[] fieldList = clazz.getDeclaredFields();
            if (fieldList.length > 0) {
                queryWrapper.and(wrapper -> {
                    Arrays.asList(fieldList)
                            .forEach(field -> {
                                wrapper.or().like(CrudUtils.getDbField(field), keyword);
                            });
                });
            }
        }
    }
}
