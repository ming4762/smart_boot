package com.gc.module.faq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.module.faq.mapper.FaqCategoryMapper;
import com.gc.module.faq.model.FaqCategoryPO;
import com.gc.module.faq.service.FaqCategoryService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.google.common.collect.Sets;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/4/11 11:36 上午
 */
@Service
public class FaqCategoryServiceImpl extends BaseServiceImpl<FaqCategoryMapper, FaqCategoryPO> implements FaqCategoryService {

    /**
     * 重写添加保存方法
     * @param entity
     * @return
     */
    @Override
    public boolean saveOrUpdate(FaqCategoryPO entity) {
        boolean isAdd = this.isAdd(entity);
        if (isAdd) {
            entity.setCreateTime(new Date());
            return this.save(entity);
        } else {
            entity.setUpdateTime(new Date());
            return this.updateById(entity);
        }
    }

    /**
     * 重写删除方法 删除下级
     * @param idList ID列表
     * @return
     */
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        Set<Serializable> setIdList = Sets.newHashSet(idList.iterator());
        Set<Serializable> deleteIds = Sets.newHashSet();
        deleteIds.addAll(setIdList);
        // 删除本级
        this.removeByParentId(setIdList, deleteIds);
        return retBool(this.baseMapper.deleteBatchIds(deleteIds));
    }

    /**
     * 使用递归获取要删除的ID
     * @param parentIds
     * @param deleteIds
     */
    private void removeByParentId(@NonNull Set<? extends Serializable> parentIds, @NonNull Set<Serializable> deleteIds) {
        if (!parentIds.isEmpty()) {
            // 查询下级
            Set<Serializable> ids = this.list(
                    new QueryWrapper<FaqCategoryPO>().lambda()
                            .select(FaqCategoryPO :: getCategoryId)
                            .in(FaqCategoryPO :: getParentId, parentIds)
            ).stream().map(FaqCategoryPO :: getCategoryId).collect(Collectors.toSet());
            deleteIds.addAll(ids);
            this.removeByParentId(ids, deleteIds);
        }
    }
}
