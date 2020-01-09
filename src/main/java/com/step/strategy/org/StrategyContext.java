package com.step.strategy.org;

import com.step.exception.OrgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service(value = "context")
public class StrategyContext {

    //获取所有的策略模式
    @Autowired
    private List<Strategy> strategys = new ArrayList<Strategy>();
    @Autowired
    private Map<String,Strategy> stringStrategyMap = new HashMap<>();

    public List<Strategy> getStrategys() {
        return strategys;
    }

    public Strategy getStrategy(String key){
        return stringStrategyMap.get(key);
    }

    public StrategyContext() { }

    public List invoke() {
        List<Object> ret = new ArrayList<>();
        strategys.stream().forEach(e -> {
            try {
                e.doSync();
            } catch (OrgException e1) {
                if(e1.getErrors()!=null){
                    ret.add(e1.getErrors());
                }else{
                    ret.add(e1.getMessage());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                ret.add(e1.getMessage());
            }
        });

        return ret;
    }

}
