package com.step.entity.bean.table;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 */
public class FormatUtils {
    private static Map<String, SimpleDateFormat> dateFormatMaps = new HashMap();
    private static Map<String, DecimalFormat> decimalFormatMaps = new HashMap();

    public static String dateFormat(String key, Object  v) {
        Format format=getFormat(key,false);
        if(format==null){
            return  String.valueOf(v);
        }
        return format.format(v);
    }

    public static Format getFormat(String formatId, boolean isDecimal) {
        if (StringUtils.isEmpty(formatId)) {
            return null;
        } else {
            Format format=null;
          if(isDecimal){
              format=decimalFormatMaps.get(formatId);
          }else{
              format = dateFormatMaps.get(formatId);
          }
          if(format==null){
              return null;
          }
          return format;
        }
    }

    public static Format dateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static Format decimalFormat(String pattern) {
        return new DecimalFormat(pattern);
    }

    /***
     * 获取pattern
     * @param formatId
     * @return
     */
    public static String toPattern(String formatId) {
       SimpleDateFormat smd = dateFormatMaps.get(formatId);
       if(smd==null){
           return "";
       }
       return smd.toPattern();
    }

    static {
        dateFormatMaps.put("101", new SimpleDateFormat("0"));
        dateFormatMaps.put("102", new SimpleDateFormat("0.0"));
        dateFormatMaps.put("103", new SimpleDateFormat("0.00"));
        dateFormatMaps.put("104", new SimpleDateFormat("0.000"));
        dateFormatMaps.put("105", new SimpleDateFormat("0.0000"));
        dateFormatMaps.put("106", new SimpleDateFormat("0.00000"));
        dateFormatMaps.put("107", new SimpleDateFormat("#"));
        dateFormatMaps.put("108", new SimpleDateFormat("#.#"));
        dateFormatMaps.put("109", new SimpleDateFormat("#.##"));
        dateFormatMaps.put("110", new SimpleDateFormat("#.###"));
        dateFormatMaps.put("111", new SimpleDateFormat("#.####"));
        dateFormatMaps.put("112", new SimpleDateFormat("#.#####"));
        decimalFormatMaps.put("101", new DecimalFormat("#,##0"));
        decimalFormatMaps.put("102", new DecimalFormat("#,##0.0"));
        decimalFormatMaps.put("103", new DecimalFormat("#,##0.00"));
        decimalFormatMaps.put("104", new DecimalFormat("#,##0.000"));
        decimalFormatMaps.put("105", new DecimalFormat("#,##0.0000"));
        decimalFormatMaps.put("106", new DecimalFormat("#,##0.00000"));
        decimalFormatMaps.put("107", new DecimalFormat("#,###"));
        decimalFormatMaps.put("108", new DecimalFormat("#,###.#"));
        decimalFormatMaps.put("109", new DecimalFormat("#,###.##"));
        decimalFormatMaps.put("110", new DecimalFormat("#,###.###"));
        decimalFormatMaps.put("111", new DecimalFormat("#,###.####"));
        decimalFormatMaps.put("112", new DecimalFormat("#,###.#####"));
        dateFormatMaps.put("201", new SimpleDateFormat("yyyy"));
        dateFormatMaps.put("202", new SimpleDateFormat("yyyy-MM"));
        dateFormatMaps.put("203", new SimpleDateFormat("yyyy-MM-dd"));
        dateFormatMaps.put("204", new SimpleDateFormat("yyyy年"));
        dateFormatMaps.put("205", new SimpleDateFormat("yyyy年MM月"));
        dateFormatMaps.put("206", new SimpleDateFormat("yyyy年MM月dd日"));
        dateFormatMaps.put("207", new SimpleDateFormat("yyyy/MM"));
        dateFormatMaps.put("208", new SimpleDateFormat("yyyy/MM/dd"));
        dateFormatMaps.put("209", new SimpleDateFormat("MM"));
        dateFormatMaps.put("210", new SimpleDateFormat("MM月"));
        dateFormatMaps.put("211", new SimpleDateFormat("MM-dd"));
        dateFormatMaps.put("212", new SimpleDateFormat("MM月dd日"));
        dateFormatMaps.put("213", new SimpleDateFormat("MM/dd"));
        dateFormatMaps.put("214", new SimpleDateFormat("dd"));
        dateFormatMaps.put("215", new SimpleDateFormat("dd日"));
        dateFormatMaps.put("301", new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        dateFormatMaps.put("302", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        dateFormatMaps.put("303", new SimpleDateFormat("yyyy年MM月dd日  HH时mm分"));
        dateFormatMaps.put("304", new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒"));
        dateFormatMaps.put("401", new SimpleDateFormat("HH:mm"));
        dateFormatMaps.put("402", new SimpleDateFormat("HH:mm:ss"));
        dateFormatMaps.put("403", new SimpleDateFormat("HH时mm分"));
        dateFormatMaps.put("404", new SimpleDateFormat("HH时mm分ss秒"));
        dateFormatMaps.put("405", new SimpleDateFormat("HH"));
        dateFormatMaps.put("406", new SimpleDateFormat("HH时"));
        dateFormatMaps.put("407", new SimpleDateFormat("mm"));
        dateFormatMaps.put("408", new SimpleDateFormat("mm分"));
        dateFormatMaps.put("501", new SimpleDateFormat("￥0"));
        dateFormatMaps.put("502", new SimpleDateFormat("￥0.0"));
        dateFormatMaps.put("503", new SimpleDateFormat("￥0.00"));
        dateFormatMaps.put("504", new SimpleDateFormat("￥0.000"));
        dateFormatMaps.put("505", new SimpleDateFormat("￥0.0000"));
        dateFormatMaps.put("506", new SimpleDateFormat("￥0.00000"));
        dateFormatMaps.put("507", new SimpleDateFormat("￥#"));
        dateFormatMaps.put("508", new SimpleDateFormat("￥#.#"));
        dateFormatMaps.put("509", new SimpleDateFormat("￥#.##"));
        dateFormatMaps.put("510", new SimpleDateFormat("￥#.###"));
        dateFormatMaps.put("511", new SimpleDateFormat("￥#.####"));
        dateFormatMaps.put("512", new SimpleDateFormat("￥#.#####"));
        decimalFormatMaps.put("501", new DecimalFormat("￥#,##0"));
        decimalFormatMaps.put("502", new DecimalFormat("￥#,##0.0"));
        decimalFormatMaps.put("503", new DecimalFormat("￥#,##0.00"));
        decimalFormatMaps.put("504", new DecimalFormat("￥#,##0.000"));
        decimalFormatMaps.put("505", new DecimalFormat("￥#,##0.0000"));
        decimalFormatMaps.put("506", new DecimalFormat("￥#,##0.00000"));
        decimalFormatMaps.put("507", new DecimalFormat("￥#,###"));
        decimalFormatMaps.put("508", new DecimalFormat("￥#,###.#"));
        decimalFormatMaps.put("509", new DecimalFormat("￥#,###.##"));
        decimalFormatMaps.put("510", new DecimalFormat("￥#,###.###"));
        decimalFormatMaps.put("511", new DecimalFormat("￥#,###.####"));
        decimalFormatMaps.put("512", new DecimalFormat("￥#,###.#####"));
        dateFormatMaps.put("521", new SimpleDateFormat("$0"));
        dateFormatMaps.put("522", new SimpleDateFormat("$0.0"));
        dateFormatMaps.put("523", new SimpleDateFormat("$0.00"));
        dateFormatMaps.put("524", new SimpleDateFormat("$0.000"));
        dateFormatMaps.put("525", new SimpleDateFormat("$0.0000"));
        dateFormatMaps.put("526", new SimpleDateFormat("$0.00000"));
        dateFormatMaps.put("527", new SimpleDateFormat("$#"));
        dateFormatMaps.put("528", new SimpleDateFormat("$#.#"));
        dateFormatMaps.put("529", new SimpleDateFormat("$#.##"));
        dateFormatMaps.put("530", new SimpleDateFormat("$#.###"));
        dateFormatMaps.put("531", new SimpleDateFormat("$#.####"));
        dateFormatMaps.put("532", new SimpleDateFormat("$#.#####"));
        decimalFormatMaps.put("521", new DecimalFormat("$#,##0"));
        decimalFormatMaps.put("522", new DecimalFormat("$#,##0.0"));
        decimalFormatMaps.put("523", new DecimalFormat("$#,##0.00"));
        decimalFormatMaps.put("524", new DecimalFormat("$#,##0.000"));
        decimalFormatMaps.put("525", new DecimalFormat("$#,##0.0000"));
        decimalFormatMaps.put("526", new DecimalFormat("$#,##0.00000"));
        decimalFormatMaps.put("527", new DecimalFormat("$#,###"));
        decimalFormatMaps.put("528", new DecimalFormat("$#,###.#"));
        decimalFormatMaps.put("529", new DecimalFormat("$#,###.##"));
        decimalFormatMaps.put("530", new DecimalFormat("$#,###.###"));
        decimalFormatMaps.put("531", new DecimalFormat("$#,###.####"));
        decimalFormatMaps.put("532", new DecimalFormat("$#,###.#####"));
        dateFormatMaps.put("541", new SimpleDateFormat("US$0"));
        dateFormatMaps.put("542", new SimpleDateFormat("US$0.0"));
        dateFormatMaps.put("543", new SimpleDateFormat("US$0.00"));
        dateFormatMaps.put("544", new SimpleDateFormat("US$0.000"));
        dateFormatMaps.put("545", new SimpleDateFormat("US$0.0000"));
        dateFormatMaps.put("546", new SimpleDateFormat("US$0.00000"));
        dateFormatMaps.put("547", new SimpleDateFormat("US$#"));
        dateFormatMaps.put("548", new SimpleDateFormat("US$#.#"));
        dateFormatMaps.put("549", new SimpleDateFormat("US$#.##"));
        dateFormatMaps.put("550", new SimpleDateFormat("US$#.###"));
        dateFormatMaps.put("551", new SimpleDateFormat("US$#.####"));
        dateFormatMaps.put("552", new SimpleDateFormat("US$#.#####"));
        decimalFormatMaps.put("541", new DecimalFormat("US$#,##0"));
        decimalFormatMaps.put("542", new DecimalFormat("US$#,##0.0"));
        decimalFormatMaps.put("543", new DecimalFormat("US$#,##0.00"));
        decimalFormatMaps.put("544", new DecimalFormat("US$#,##0.000"));
        decimalFormatMaps.put("545", new DecimalFormat("US$#,##0.0000"));
        decimalFormatMaps.put("546", new DecimalFormat("US$#,##0.00000"));
        decimalFormatMaps.put("547", new DecimalFormat("US$#,###"));
        decimalFormatMaps.put("548", new DecimalFormat("US$#,###.#"));
        decimalFormatMaps.put("549", new DecimalFormat("US$#,###.##"));
        decimalFormatMaps.put("550", new DecimalFormat("US$#,###.###"));
        decimalFormatMaps.put("551", new DecimalFormat("US$#,###.####"));
        decimalFormatMaps.put("552", new DecimalFormat("US$#,###.#####"));
        dateFormatMaps.put("601", new SimpleDateFormat("0%"));
        dateFormatMaps.put("602", new SimpleDateFormat("0.0%"));
        dateFormatMaps.put("603", new SimpleDateFormat("0.00%"));
        dateFormatMaps.put("604", new SimpleDateFormat("0.000%"));
        dateFormatMaps.put("605", new SimpleDateFormat("0.0000%"));
        dateFormatMaps.put("606", new SimpleDateFormat("0.00000%"));
        dateFormatMaps.put("607", new SimpleDateFormat("#%"));
        dateFormatMaps.put("608", new SimpleDateFormat("#.#%"));
        dateFormatMaps.put("609", new SimpleDateFormat("#.##%"));
        dateFormatMaps.put("610", new SimpleDateFormat("#.###%"));
        dateFormatMaps.put("611", new SimpleDateFormat("#.####%"));
        dateFormatMaps.put("612", new SimpleDateFormat("#.#####%"));
        decimalFormatMaps.put("601", new DecimalFormat("#,##0%"));
        decimalFormatMaps.put("602", new DecimalFormat("#,##0.0%"));
        decimalFormatMaps.put("603", new DecimalFormat("#,##0.00%"));
        decimalFormatMaps.put("604", new DecimalFormat("#,##0.000%"));
        decimalFormatMaps.put("605", new DecimalFormat("#,##0.0000%"));
        decimalFormatMaps.put("606", new DecimalFormat("#,##0.00000%"));
        decimalFormatMaps.put("607", new DecimalFormat("#,###%"));
        decimalFormatMaps.put("608", new DecimalFormat("#,###.#%"));
        decimalFormatMaps.put("609", new DecimalFormat("#,###.##%"));
        decimalFormatMaps.put("610", new DecimalFormat("#,###.###%"));
        decimalFormatMaps.put("611", new DecimalFormat("#,###.####%"));
        decimalFormatMaps.put("612", new DecimalFormat("#,###.#####%"));
    }



}
