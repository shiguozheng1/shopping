package com.step.entity.bean.table;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 */
public class ForeignKey {
    private String column; //字段
    private String refTable; //引用表
    private String refColumn;//引用表字段
    private String refFormName;//引用表单

    public ForeignKey() {
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getRefTable() {
        return this.refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public String getRefColumn() {
        return this.refColumn;
    }

    public void setRefColumn(String refColumn) {
        this.refColumn = refColumn;
    }

    public String getRefFormName() {
        return this.refFormName;
    }

    public void setRefFormName(String refFormName) {
        this.refFormName = refFormName;
    }
}
