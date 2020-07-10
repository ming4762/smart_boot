package com.gc.commons.txt.read.listener;

import com.gc.commons.txt.TxtBaseModel;

/**
 * 读取监听接口
 * @author shizhongming
 * 2020/7/7 10:48 下午
 */
public interface ReadListener<T extends TxtBaseModel> {

    /**
     * 异常处理
     * @param exception 异常信息
     * @throws Exception 抛出一样
     */
    void onException(Exception exception) throws Exception;

    /**
     * 处理数据
     * @param data 数据
     */
    void invoke(T data);

    /**
     * 解析完毕执行
     */
    void doAfterAllAnalysed();

    /**
     * 是否有下一行
     * @param lineData
     * @return 是否有下一行
     */
    boolean hasNext(String lineData);
}