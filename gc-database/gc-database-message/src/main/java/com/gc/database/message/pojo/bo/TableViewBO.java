package com.gc.database.message.pojo.bo;

import com.gc.common.base.utils.StringUtils;
import com.gc.database.message.pojo.dbo.TableViewDO;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库表实体
 * @author shizhongming
 * 2020/1/18 8:52 下午
 */
@Getter
@ToString
public class TableViewBO extends TableViewDO {
    private static final long serialVersionUID = 763710787360425049L;

    private String className;

    /**
     * 主键信息
     */
    private List<ColumnBO> primaryKeyList;

    /**
     * 其他字段信息
     */
    private List<ColumnBO> baseColumnList;

    /**
     * 设置列信息
     * @param columnList 所有列信息
     */
    public void setColumnList(List<ColumnBO> columnList) {
        if (CollectionUtils.isNotEmpty(columnList)) {
            // 以是否是主键进行分组，区分主键和非主键字段
            Map<Boolean, List<ColumnBO>> columnMap = columnList.stream()
                    .collect(Collectors.groupingBy(ColumnBO :: getPrimaryKey));
            this.primaryKeyList = columnMap.get(Boolean.TRUE);
            this.baseColumnList = columnMap.get(Boolean.FALSE);
        }
    }


    /**
     * 批量通过DO 创建BO
     * @author shizhongming
     * @param tableViewList 表格DO列表
     * @return 表格BO列表
     */
    public static List<TableViewBO> batchCreateFromDo(@NonNull List<TableViewDO> tableViewList) {
        return tableViewList.stream()
                .map(TableViewBO::createFromDo)
                .collect(Collectors.toList());
    }

    /**
     * 通过DO 创建BO
     * @author shizhongming
     * @param tableViewDo 表格DO
     * @return 表格BO
     */
    public static TableViewBO createFromDo(@NonNull TableViewDO tableViewDo) {
        final TableViewBO tableView = new TableViewBO();
        BeanUtils.copyProperties(tableViewDo, tableView);
        // 设置className
        tableView.className = org.apache.commons.lang3.StringUtils.capitalize(StringUtils.lineToHump(tableView.getTableName()));
        return tableView;
    }

}
