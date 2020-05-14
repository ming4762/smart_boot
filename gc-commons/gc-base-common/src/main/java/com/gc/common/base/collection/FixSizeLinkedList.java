package com.gc.common.base.collection;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;

/**
 * 固定长度List
 * 如果List里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
 * @author shizhongming
 * 2020/5/8 11:15 上午
 */
public class FixSizeLinkedList<T> extends LinkedList<T> {
    private static final long serialVersionUID = 175167908539941771L;

    /**
     * LIST 容量
     */
    private final int capacity;

    public FixSizeLinkedList(int capacity) {
        super();
        this.capacity = capacity;
    }

    public  static <T> FixSizeLinkedList<T> init(int capacity) {
        return new FixSizeLinkedList<T>(capacity);
    }

    @Override
    public boolean add(T t) {
        if (this.size() == capacity) {
            super.removeFirst();
        }
        return super.add(t);
    }
}
