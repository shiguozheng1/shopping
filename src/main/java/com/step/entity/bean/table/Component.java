package com.step.entity.bean.table;

import java.text.Format;
import java.util.Set;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 */
public abstract class Component {
    private int height = 22;
    private int width = 150;
    private String widthUnit = "px";
    private String heightUnit = "px";
    private int cellHeight;
    private int cellWidth;
    private int editStyle;
    private String name = "";
    private int verticalCount;
    private String tableName = "";
    private String tableDesc = "";
    private String columnName;
    private StringBuilder builder;
    private String formatId;
    private String formatPattern;
    private String fxf;

    public Component() {
    }

   // protected abstract void prepared(FormCell var1, Jform var2, Set<String> var3);

    public abstract String getDesc();

    public Format getForamt() {
        return FormatUtils.getFormat(this.formatId, false);
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getEditStyle() {
        return this.editStyle;
    }

    public void setEditStyle(int editStyle) {
        this.editStyle = editStyle;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getCellHeight() {
        return this.cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getCellWidth() {
        return this.cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public String getFxTarget() {
        return this.builder == null ? null : this.builder.toString();
    }

    public void setFxTarget(String fxTarget) {
        if (this.builder == null) {
            this.builder = new StringBuilder();
        }

        this.builder.append(fxTarget).append(",");
    }

    public String getFormatId() {
        return this.formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
        this.formatPattern = FormatUtils.toPattern(formatId);
    }

    public String getWidthUnit() {
        return this.widthUnit;
    }

    public void setWidthUnit(String widthUnit) {
        this.widthUnit = widthUnit;
    }

    public String getHeightUnit() {
        return this.heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public String getFxf() {
        return this.fxf;
    }

    public void setFxf(String fxf) {
        this.fxf = fxf;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getVerticalCount() {
        return this.verticalCount;
    }

    public void setVerticalCount(int verticalCount) {
        this.verticalCount = verticalCount;
    }

    public String getFormatPattern() {
        return this.formatPattern;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public String getTableDesc() {
        return this.tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }
}
