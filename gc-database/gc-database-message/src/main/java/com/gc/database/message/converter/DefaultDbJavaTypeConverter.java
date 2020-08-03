package com.gc.database.message.converter;

import com.gc.database.message.constants.TypeMappingConstant;
import com.gc.database.message.pojo.bo.ColumnBO;
import com.gc.database.message.utils.CacheUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 默认的类型转换器
 * @author ShiZhongMing
 * 2020/8/3 11:00
 * @since 1.0
 */
public class DefaultDbJavaTypeConverter implements DbJavaTypeConverter {

    /**
     * 返回java类型
     * @param column 列信息
     * @return 类型映射
     */
    @Nullable
    @Override
    public TypeMappingConstant convert(@NonNull ColumnBO column) {
        // NUMERIC类型处理
        if (Objects.equals(column.getDataType(), TypeMappingConstant.NUMERIC.getDataType())) {
            return this.convertNumberType(column);
        }
        return CacheUtils.getFieldMapping(column.getDataType());
    }

    /**
     * 转换数字类型
     * @param column 列信息
     * @return 类型映射
     */
    @NonNull
    private TypeMappingConstant convertNumberType(@NonNull ColumnBO column) {
        // 判断是否是小数类型
        if (column.getDecimalDigits() > 0) {
            return TypeMappingConstant.NUMERIC;
        }
        // 判断长度是否是Integer Long
        int columnSize = column.getColumnSize();
        if (columnSize <= 32 && column.getDecimalDigits() != -127) {
            return TypeMappingConstant.INTEGER;
        }
        return TypeMappingConstant.BIGINT;
    }
}
