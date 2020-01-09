package com.step.utils;

import com.google.common.collect.Lists;
import com.step.entity.dto.DifferEntity;
import com.step.properties.MaycurProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

/***
 * 差异工具
 */
@Slf4j
public class DifferUtils {
    public static <T extends DifferEntity> List<T>  diff(List<T> sourceList, List<T> targetList) {
        List<T> formatList= Lists.newArrayList();
        for( T source :sourceList){
            boolean flag=false;
            Iterator<T> iterator = targetList.iterator();
            while(iterator.hasNext()) {
                T target = iterator.next();
                if(source.getKey().equals(target.getKey())){
                    if (!source.equals(target)) {
                        source.setMethod("U");
                        formatList.add(source);
                        log.info("修改业务Code:{},name为:{}",source.getKey(),source.getName());
                    }
                    flag = true;
                    iterator.remove();
                }
            }
            if(!flag){
                source.setMethod("C");
                formatList.add(source);
                log.info("创建业务Code:{},name:{}",source.getKey(),source.getName());
            }
        }
        if(!targetList.isEmpty()){
            targetList.stream().forEach(e->{
                e.setMethod("D");
                formatList.add(e);
            });
        }
        return formatList;
    }


}
