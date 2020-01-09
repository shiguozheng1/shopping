package com.step.entity.bean.table;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
/**
 * @author zhushubin
 * @date 2019-11-18
 * email:604580436@qq.com
 * 用于自动创建数据库表描述
 */
public class TableMapp {
    public TableMapp() {
    }
    /***
     * 数据库模式
     */
    private String schema;
    private String name;
    private String source;
    /***
     * 数据库语句
     */
    private String insertSql;
    private String selectSql;
    private String deleteSql;
    private String updateStatusSql;
    private String updateSql;
    /***
     * 单个主键
     */
    private ColumnMapp pkColumn;
    /***
     * 联合主键
     */
    private List<ColumnMapp> joinPkColumns;
    /***
     * 字段集
     */
    private Map<String, ColumnMapp> columns = new LinkedHashMap();
    /***
     * 表单关联的字段
     */
    private Map<String, String> fromRefColumnMaps = new LinkedHashMap();
    /***
     * 外键
     */
    private Map<String, ForeignKey> fkMaps;
    /***
     * 参考字段
     */
    private Map<String, ForeignKey> refFkColumnMaps;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ColumnMapp> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnMapp> columns) {
        this.columns = columns;
    }

    public Map<String, ForeignKey> getFks() {
        return this.fkMaps;
    }

    public void setFks(Map<String, ForeignKey> fks) {
        this.fkMaps = fks;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void addColumn(ColumnMapp column) {
        this.columns.put(column.getName(), column);
        this.fromRefColumnMaps.put(column.getFormName(), column.getName());
    }

    public void addExcelColumn(ColumnMapp column) {
        this.columns.put(column.getName(), column);
    }

    public ColumnMapp getColumn(String name) {
        return this.columns.get(name);
    }

    public String getColumnByFormName(String formName) {
        return this.fromRefColumnMaps.get(formName);
    }

    public void addFk(ForeignKey fk) {
        if (this.fkMaps == null) {
            this.fkMaps = new LinkedHashMap();
            this.refFkColumnMaps = new LinkedHashMap();
        }

        this.fkMaps.put(fk.getColumn(), fk);
        this.refFkColumnMaps.put(fk.getRefColumn(), fk);
    }

    public String getColumnByRefColumn(String refColumn) {
       ForeignKey foreignKey= this.refFkColumnMaps.get(refColumn);
        return foreignKey== null ? "" : foreignKey.getColumn();
    }

    public String getInsertSql() {
        return this.insertSql;
    }

    public void setInsertSql(String sql) {
        this.insertSql = sql;
    }

    public String getQualityName() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(this.schema)) {
            builder.append(this.schema);
            builder.append(".");
        }

        builder.append(this.name);
        return builder.toString();
    }

    public ColumnMapp getPkColumn() {
        return this.pkColumn;
    }

    public void setPkColumn(ColumnMapp pkColumn) {
        this.pkColumn = pkColumn;
    }

    /***
     * 查找主键字段
     * @return map
     */
    public ColumnMapp searchPkColumn() {
        if(this.columns==null||this.columns.size()<1){
            return null;
        }
        ColumnMapp columnMapp=null;
        for(Map.Entry<String,ColumnMapp> entry:this.columns.entrySet()){
            columnMapp = entry.getValue();
            if(columnMapp.isPk()){
              break;
            }
        }
        return columnMapp;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String getDeleteSql() {
        return this.deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getUpdateStatusSql(String whereColumn) {
        StringBuilder builder= new StringBuilder();
        builder.append(updateStatusSql).append(" where ").append(whereColumn).append(" = ?");
        return builder.toString();
    }

    public void setUpdateStatusSql(String updateStatusSql) {
        this.updateStatusSql = updateStatusSql;
    }

    public String getUpdateSql() {
        return this.updateSql;
    }

    public void setUpdateSql(String updateSql) {
        StringBuilder builder = new StringBuilder();
        String key = pkColumn != null ? pkColumn.getName() : "";
        builder.append(updateSql).append(" where ").append(key).append(" = ?");
        this.updateSql = builder.toString();
    }

    public List<ColumnMapp> getJoinPkColumns() {
        return this.joinPkColumns;
    }

    public void setJoinPkColumns(List<ColumnMapp> joinPkColumns) {
        this.joinPkColumns = joinPkColumns;
    }

    public void addJoinPkColumn(ColumnMapp column) {
        if (this.joinPkColumns == null) {
            this.joinPkColumns = new ArrayList();
        }
        this.joinPkColumns.add(column);
    }
}
