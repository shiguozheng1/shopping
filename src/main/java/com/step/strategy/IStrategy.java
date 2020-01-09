package com.step.strategy;

import com.step.entity.primary.Element;
import com.unboundid.ldap.sdk.LDAPException;
import java.util.List;
public interface IStrategy {
    //定义的抽象算法方法 来约束具体的算法实现方法
     void algorithmMethod(Element element, String baseDn, List<Element> persons) throws LDAPException;
}

