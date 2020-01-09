package com.step.entity.bean.table;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 * 数据库映射
 */
public class DatabaseMapp {
    private String sourceName;
    private String nosqlTableName = "";
    /***
     * 表
     */
    private List<TableMapp> tables = new ArrayList();
    /***
     * 主键映射
     */
    private TableMapp pkTableMapp;
    private List<TableMapp> e;
    private Map<String, String> formColumnMaps= new HashMap();
   // private ArCache g;

    public DatabaseMapp() {
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public List<TableMapp> getTables() {
        return this.tables;
    }

    public void setTables(List<TableMapp> tables) {
        this.tables = tables;
    }

    public void addTable(TableMapp table) {
        this.tables.add(table);
    }

    public void prepare() {
        this.prepare(tables, "");
    }

    public Map<String, String> getLinkageRelations(FormComponent form) {
         HashMap ret  = new HashMap<String,String>();;
        TableMapp tableMapp=null;
//        for(int i=0;i <tables.size();i++){
//            tableMapp = tables.get(i);
//            for(Map.Entry<String,ColumnMapp> entry:tableMapp.getColumns().entrySet()) {
//                ColumnMapp columnMapp = entry.getValue();
//                FormComponent formComponent = form.d(columnMapp.getFormName());
//                if (formComponent != null && StringUtils.isNotEmpty(formComponent.getLinkCmopName())) {
//                    if (ret == null) {
//                        ret = new HashMap<String,String>();
//                    }
//                    ret.put(formComponent.getName(), formComponent.getName());
//                }
//            }
//        }
        return ret;
    }

    public void prepare( List<TableMapp> tables, String fdb) {
//        Iterator var3;
//        TableMapp var4;
//        if ("nosql".equalsIgnoreCase(fdb)) {
//            var3 = this.tables.iterator();
//
//            while(var3.hasNext()) {
//                var4 = (TableMapp)var3.next();
//                if (this.b.equalsIgnoreCase(var4.getName())) {
//                    this.d = var4;
//                } else {
//                    this.addSubTableMapp(var4);
//                }
//
//                Iterator var30 = var4.getColumns().entrySet().iterator();
//
//                while(var30.hasNext()) {
//                    ColumnMapp var31 = (ColumnMapp)((Map.Entry)var30.next()).getValue();
//                    this.f.put(var31.getFormName(), var31.getName());
//                    FormComponent var32;
//                    if (form != null && (var32 = form.d(var31.getFormName())) != null) {
//                        var32.setTableName(var4.getName());
//                        var32.setColumnName(var31.getName());
//                        if (var32.getSequences() != null && var32.getSequences().isSaveCreateSequence()) {
//                            var31.setSequence(true);
//                        }
//                    }
//
//                    int var33 = var31.getDataType();
//                    if (1 == var33) {
//                        var31.setColumnType(new g());
//                    } else if (2 == var33) {
//                        var31.setColumnType(new e());
//                    } else if (3 == var33) {
//                        var31.setColumnType(new f());
//                    } else if (4 == var33) {
//                        var31.setColumnType(new d());
//                    } else if (5 == var33) {
//                        if (form == null) {
//                            var31.setColumnType(new b());
//                        } else {
//                            String var34 = form.c(var31.getFormName());
//                            var31.setColumnType(new b(var34));
//                        }
//                    }
//                }
//
//                if (StringUtils.isEmpty(this.sourceName)) {
//                    this.sourceName = var4.getSource();
//                }
//            }
//
//            DialectDataSource var27;
//            if ((var27 = SourceManager.getDataSource(this.a)) != null && var27.getType() == 3) {
//                this.g = CacheFactory.createCache(this.b);
//            }
//
//            if (this.d != null) {
//                Iterator var28 = this.d.getColumns().entrySet().iterator();
//
//                while(var28.hasNext()) {
//                    Entry var29;
//                    if (((ColumnMapp)(var29 = (Entry)var28.next()).getValue()).getIspk() == 1) {
//                        this.d.addJoinPkColumn((ColumnMapp)var29.getValue());
//                    }
//                }
//            }
//
//        } else {
//            var3 = this.c.iterator();
//
//            while(var3.hasNext()) {
//                Dialect var5 = SourceManager.getDialect((var4 = (TableMapp)var3.next()).getSource());
//                com.anyrt.jform.d.d.b var6 = new com.anyrt.jform.d.d.b(var4.getQualityName());
//                com.anyrt.jform.d.d.c var7;
//                (var7 = new com.anyrt.jform.d.d.c()).a(var4.getQualityName());
//                a var8;
//                (var8 = new a()).b(var4.getQualityName());
//                com.anyrt.jform.d.d.d var9;
//                (var9 = new com.anyrt.jform.d.d.d()).a(var4.getQualityName());
//                com.anyrt.jform.d.d.d var10;
//                (var10 = new com.anyrt.jform.d.d.d()).a(var4.getQualityName());
//                Map var11 = var4.getColumns();
//                Map var12 = var4.getFks();
//                Iterator var13 = var11.entrySet().iterator();
//
//                while(true) {
//                    while(var13.hasNext()) {
//                        ColumnMapp var15 = (ColumnMapp)((Entry)var13.next()).getValue();
//                        this.f.put(var15.getFormName(), var15.getName());
//                        FormComponent var16;
//                        if (form != null && (var16 = form.d(var15.getFormName())) != null) {
//                            var16.setTableName(var4.getName());
//                            var16.setColumnName(var15.getName());
//                            if (var16.getSequences() != null && var16.getSequences().isSaveCreateSequence()) {
//                                var15.setSequence(true);
//                            }
//                        }
//
//                        int var35 = var15.getDataType();
//                        if (1 == var35) {
//                            var15.setColumnType(new g());
//                        } else if (2 == var35) {
//                            var15.setColumnType(new e());
//                        } else if (3 == var35) {
//                            var15.setColumnType(new f());
//                        } else if (4 == var35) {
//                            var15.setColumnType(new d());
//                        } else if (5 == var35) {
//                            if (form == null) {
//                                var15.setColumnType(new b());
//                            } else {
//                                String var17 = form.c(var15.getFormName());
//                                var15.setColumnType(new b(var17));
//                            }
//                        }
//
//                        if (var15.getIspk() == 1) {
//                            var4.setPkColumn(var15);
//                            if (var5 != null && var5.supportsSequences() || var15.isPkUUID()) {
//                                var6.a(var15.getName());
//                            }
//
//                            if (var12 == null) {
//                                var7.b(var15.getName());
//                                var8.a(var15.getName());
//                                this.d = var4;
//                            }
//                        } else {
//                            if (var12 != null && var12.containsKey(var15.getName())) {
//                                ForeignKey var36;
//                                ForeignKey var10000 = var36 = (ForeignKey)var12.get(var15.getName());
//                                String var10002 = var36.getRefTable();
//                                String var20 = var36.getRefColumn();
//                                String var19 = var10002;
//                                String var10001;
//                                if (!StringUtil.isEmpty(var19) && !StringUtil.isEmpty(var20)) {
//                                    Iterator var21 = this.c.iterator();
//
//                                    label175:
//                                    while(true) {
//                                        TableMapp var22;
//                                        Map var23;
//                                        do {
//                                            do {
//                                                if (!var21.hasNext()) {
//                                                    var10001 = "";
//                                                    break label175;
//                                                }
//
//                                                var22 = (TableMapp)var21.next();
//                                            } while(!var19.equals(var22.getName()));
//                                        } while((var23 = var22.getColumns()) == null);
//
//                                        Iterator var24 = var23.entrySet().iterator();
//
//                                        while(var24.hasNext()) {
//                                            ColumnMapp var26 = (ColumnMapp)((Entry)var24.next()).getValue();
//                                            if (var20.equals(var26.getName())) {
//                                                var10001 = var26.getFormName();
//                                                break label175;
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    var10001 = "";
//                                }
//
//                                var10000.setRefFormName(var10001);
//                                var15.setIsFk(1);
//                                var7.b(var15.getName());
//                                var8.a(var15.getName());
//                                this.addSubTableMapp(var4);
//                            }
//
//                            if (var15.getOptAdd() == 0) {
//                                var6.a(var15.getName());
//                            }
//
//                            if (var15.getOptEdit() == 0) {
//                                var10.b(var15.getName());
//                            }
//                        }
//                    }
//
//                    var9.b("FP_STAUTS_");
//                    var4.setInsertSql(var6.a(var5));
//                    var4.setSelectSql(var7.a());
//                    var4.setDeleteSql(var8.a());
//                    var4.setUpdateStatusSql(var9.a());
//                    var4.setUpdateSql(var10.a());
//                    if (StringUtils.isEmpty(this.a)) {
//                        this.a = var4.getSource();
//                    }
//                    break;
//                }
//            }
//
//        }
    }

    /***
     * 根据表名查找数据表
     * @param qualityName
     * @return
     */
    public TableMapp searchTable(String qualityName) {
        if (StringUtils.isEmpty(qualityName)) {
            return null;
        } else {

            if(tables==null||tables.size()<1){
                return null;
            }
            TableMapp ret =null;
            for(TableMapp table:tables){
                if(table.getQualityName().equalsIgnoreCase(qualityName)){
                    ret = table;
                    break;
                }
            }
            return ret;
        }
    }

    public TableMapp getTable(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        } else {
            if(tables==null||tables.size()<1){
                return null;
            }
            TableMapp ret =null;
            for(TableMapp table:tables){
                if(table.getName().equalsIgnoreCase(name)){
                    ret = table;
                    break;
                }
            }
            return ret;
        }
    }

    public String getColumnByForm(String formName) {

       String column= formColumnMaps.get(formName);
       if(StringUtils.isEmpty(column)){
           return "";
       }
        return column;
    }

    public boolean hasTable() {
        return !this.tables.isEmpty();
    }

    public TableMapp getPkTableMapp() {
        return this.pkTableMapp;
    }

    public void setPkTableMapp(TableMapp pkTableMapp) {
        this.pkTableMapp = pkTableMapp;
    }

    public List<TableMapp> getSubTableMapp() {
        return this.e;
    }

    public void addSubTableMapp(TableMapp tableMapp) {
        if (this.e == null) {
            this.e = new LinkedList();
        }

        this.e.add(tableMapp);
    }

    public String getNosqlTableName() {
        return this.nosqlTableName;
    }

    public void setNosqlTableName(String nosqlTableName) {
        this.nosqlTableName = nosqlTableName;
    }

//    public ArCache getArCache() {
//        return this.g;
//    }
//
//    public void setArCache(ArCache arCache) {
//        this.g = arCache;
//    }
}
