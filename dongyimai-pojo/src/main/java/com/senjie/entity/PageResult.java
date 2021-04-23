package com.senjie.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author SenJie
 * @Data 2021/3/30 21:16
 */

public class PageResult implements Serializable {
    /*总记录数*/
    private long total;
    /*分页的结果集*/
    private List rows;

    public PageResult() {
        super();
    }

    public PageResult(long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
