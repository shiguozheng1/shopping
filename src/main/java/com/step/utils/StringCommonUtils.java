package com.step.utils;

import com.step.entity.primary.Element;
import com.step.task.BaseJob;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Created by user on 2019-04-18.
 */
public class StringCommonUtils {
    public static String formatString(String treePath, int length) {
        if (StringUtils.isNotEmpty(treePath)) {
            if (treePath.endsWith(",")) {
                return treePath.substring(length, treePath.length() - 1);
            }
        }
        return "";
    }

    public static Long formatHrmSubCompanyId(String oaid) {
        Long id;
        if (StringUtils.isNotEmpty(oaid)) {
            if (oaid.startsWith("10000")) {
                String idString = oaid.substring(4);
                id = Long.valueOf(idString);
            } else {
                id = Long.valueOf(oaid);
            }
            return id;
        }
        return null;
    }

    public static String formatParentDn(String treePath, String baseDn) {
        int index = treePath.indexOf(",") + 1;
        String parentBaseDn;
        if (index == treePath.length()) {
            parentBaseDn = baseDn;
        } else {
            parentBaseDn = treePath.substring(index) + baseDn;
        }
        return parentBaseDn;
    }

    public static String formatName(Element element) {
        if (element != null) {
            int index = element.getDn().indexOf(",");
            String formatName = element.getDn().substring(0, index);
            return formatName;
        }
        return null;
    }

    /***
     * 日期格式化
     * @param param
     * @return
     */
    public static String formatDate(Date param) {

        return null;
    }

    /***
     * 判断日期是否存在期间内
     * @param start
     * @param end
     * @param d
     * @return
     */
    public static boolean isBetweens(String start, String end, String d) {
        DateTime beginDate = new DateTime(start);
        DateTime endDate = new DateTime(end);
        DateTime tmpDate = new DateTime(d);
        if (tmpDate.isAfter(beginDate) && tmpDate.isBefore(endDate)) {
            return true;
        }
        if(tmpDate.equals(beginDate) ||tmpDate.equals(endDate)){
            return true;
        }
        return false;
    }

    public static BigDecimal convertBigDeciaml(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        return bigDecimal;
    }
    public static String convertBigDeciamlToString(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return "0";
        }
        return bigDecimal.toString();
    }
    public static String encoderToBase64(String param) {
        try {
            return Base64.getEncoder().encodeToString(param.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decoderToString(String param) {
        try {
            return new String(Base64.getDecoder().decode(param), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public static Date stringToDate(String param, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据类名称，通过反射得到该类，然后创建一个BaseJob的实例。
     * 由于NewJob和HelloJob都实现了BaseJob，
     * 所以这里不需要我们手动去判断。这里涉及到了一些java多态调用的机制
     *
     * @param classname
     * @return
     * @throws Exception
     */
    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }

    public static BigDecimal convert2BigDecimal(String amount) {
        if(StringUtils.isEmpty(amount)){
            return BigDecimal.ZERO;
        }
        return new BigDecimal(amount);
    }
}
