package com.gc.database.generator.pojo.bo;

import com.gc.database.message.pojo.bo.TableViewBO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2020/7/3 1:42 下午
 */
@Getter
@Setter
@Builder
@ToString
public class DatabaseTemplateModel implements Serializable {
    private static final long serialVersionUID = 3609925977487065194L;

    /**
     * 当前日期
     */
    private String currentDate;

    /**
     * 表集合
     */
    private List<TableViewBO> tableList;
}
