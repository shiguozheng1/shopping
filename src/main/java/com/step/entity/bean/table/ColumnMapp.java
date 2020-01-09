package com.step.entity.bean.table;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 * 工作流使用根据表单字段字段建立数据库
 */
public class ColumnMapp {
    public static final int CAN_NULL = 0;
    public static final int NOT_NULL = 1;
    public static final int NOT_PK = 0;
    public static final int IS_PK = 1;
    public static final int NOT_FK = 0;
    public static final int IS_FK = 1;
    public static final int OPT_ADD = 0;
    public static final int OPT_EIDT = 0;
    private Boolean isNull =false;
    /***
     * 是否主键
     */
    private Boolean isPk;
    private int length;//字段长度
    private boolean isAutoIncrement; //是否自增长
    private int e = 0;
    private int f = 0;
    private int g;
    private String formName; //表单名称
    private String formNameDesc= "";
    /***
     * 字段名
     */
    private String name;
    private boolean k;
    private int l;
    private Boolean isFk;
    private String columnType;
    private boolean n;

    public ColumnMapp() {
    }

    public Boolean getIsPk() {
        return isPk;
    }

    public void setIsPk(Boolean pk) {
        isPk = pk;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsNull() {
        return this.isNull;
    }

    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    public boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getFormName() {
        return this.formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Boolean getIsFk() {
        return this.isFk;
    }

    public void setIsFk(boolean isFk) {
        this.isFk = isFk;
    }

    public boolean isPk() {
        return this.getIsPk() ;
    }

    public boolean isFk() {
        return this.getIsFk();
    }

    public boolean isNotNull() {
        return this.k;
    }

    public void setNotNull(boolean notNull) {
        this.k = notNull;
    }

    public int getDataType() {
        return this.l;
    }

    public void setDataType(int dataType) {
        this.l = dataType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getFormNameDesc() {
        return formNameDesc;
    }

    public void setFormNameDesc(String formNameDesc) {
        this.formNameDesc = formNameDesc;
    }

    public boolean isSequence() {
        return this.n;
    }

    public void setSequence(boolean sequence) {
        this.n = sequence;
    }

    public int getOptAdd() {
        return this.e;
    }

    public void setOptAdd(int optAdd) {
        this.e = optAdd;
    }

    public int getOptEdit() {
        return this.f;
    }

    public void setOptEdit(int optEdit) {
        this.f = optEdit;
    }

//    public boolean isPkUUID() {
//        return this.columnType instanceof g;
//    }
}
