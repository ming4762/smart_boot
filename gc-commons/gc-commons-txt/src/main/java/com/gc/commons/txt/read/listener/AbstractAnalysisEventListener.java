package com.gc.commons.txt.read.listener;

import com.gc.commons.txt.TxtBaseModel;

/**
 * @author shizhongming
 * 2020/7/8 4:26 下午
 */
public abstract class AbstractAnalysisEventListener<T extends TxtBaseModel> implements ReadListener<T> {

    @Override
    public void onException(Exception exception) throws Exception {
        throw exception;
    }

    @Override
    public boolean hasNext(String lineData) {
        return true;
    }
}
