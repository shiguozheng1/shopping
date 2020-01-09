package com.step.service;

import com.step.entity.primary.Element;
import com.unboundid.ldap.sdk.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2019-04-17.
 */
public interface LdapService {
    /***
     * 创建DC
     * @param baseDN
     * @param dc
     * @throws LDAPException
     */
   void createDC(String baseDN, String dc) throws LDAPException;
    /** 创建组织 */
    int createO(String s, String o,String name,String cid) throws LDAPException;

    /***
     * 创建组织单元
     * @param dn
     * @param baseDN
     * @return
     */
    Integer createOU(String baseDN, String dn,String depId,String name) throws LDAPException;
    /***
     * 创建用户
     * @param baseDN
     * @param dn
     * @return loginid
     */
    Integer createEntry(String baseDN, String dn,String loginid,String name) throws LDAPException, UnsupportedEncodingException;

    /***
     * 修改用户信息
     * @param requestDn
     * @param data
     * @return loginid
     */
    Integer modifyEntry( String requestDn, Map<String, String> data) throws LDAPException, UnsupportedEncodingException;

 /**
  * 修改公司跟部门name
  * @param element
  * @param baseDn
  * @return
  * @throws LDAPException
  */
    Integer modifyName(Element element, String baseDn)throws LDAPException;

 /**
  * 查找LDAP list目录
  * @param baseDN
  * @param elementType
  * @return
  * @throws LDAPSearchException
  */
   List<Element> fetchElementsByType(String baseDN, Element.ElementType elementType)throws LDAPException;

 /**
  * 修改父级parentDn
  * @param element
  * @param baseDn
  * @return
  * @throws LDAPException
  */
    Integer modifyParentDN(Element element, String baseDn)throws LDAPException;

    /**
     * ldap所有人员
     * @param baseDN
     * @param elementType
     * @return
     * @throws LDAPException
     */
    public List<Element> fetchPersons(String baseDN,Element.ElementType elementType)throws LDAPException;

    /**
     * 移出人员
     * @param element
     * @param baseDn
     * @param elementType
     * @param newParentDn
     * @return
     * @throws LDAPException
     */
    Integer moveOutPeople(Element element, String baseDn ,Element.ElementType elementType,String newParentDn) throws LDAPException;

    /**
     * 还原人员
     * @param element
     * @param baseDn
     * @param oldParentDn
     * @return
     * @throws LDAPException
     */
    Integer moveInPeople(Element element,String baseDn,String oldParentDn)throws LDAPException;

    Integer modifyAttributes(Element element,String baseDn) throws LDAPException;
}
