package com.step.service.impl;

import com.step.config.ErrorCode;
import com.step.config.LdapCondition;
import com.step.entity.primary.Element;
import com.step.properties.LdapProperties;
import com.step.service.LdapService;
import com.step.utils.StringCommonUtils;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by user on 2019-04-17.
 * 新增判断是否启用域控同步
 */
@Service
@Transactional("transactionManagerPrimary")
@Conditional(value = LdapCondition.class)
public class LdapServiceImpl implements LdapService {
    @Autowired
    private LDAPConnection ldapConnection;
    @Autowired
    @Qualifier("sslLdapConnection")
    private LDAPConnection sslLdapConnection;
    @Autowired
    private LdapProperties ldapProperties;

    @Override
    public void createDC(String baseDN, String dc) throws LDAPException {
        String entryDN = "dc=" + dc + "," + baseDN;
        // 连接LDAP

        SearchResultEntry entry = ldapConnection.getEntry(entryDN);
        if (entry == null) {
            // 不存在则创建
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            attributes.add(new Attribute("objectClass", "top", "organization", "dcObject"));
            attributes.add(new Attribute("dc", dc));
            attributes.add(new Attribute("o", dc));
            ldapConnection.add(entryDN, attributes);
        }
    }

    /**
     * 创建组织
     */
    @Override
    public int createO(String baseDN, String dn, String name, String cid) throws LDAPException {
        String entryDN = dn + "," + baseDN;
        // 连接LDAP
        SearchResultEntry entry = ldapConnection.getEntry(entryDN);
        if (entry == null) {
            // 不存在则创建
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            attributes.add(new Attribute("objectClass", "top", "organization"));
            attributes.add(new Attribute("o", name));
            attributes.add(new Attribute("oaid", cid));
            ldapConnection.add(entryDN, attributes);
            return ErrorCode.success.ordinal();//成功返回0
        } else {
            return ErrorCode.exist.ordinal();//存在返回-1
        }
    }

    /***
     * 创建组织单元
     * @param baseDN
     * @param dn
     * @return
     */
    @Override
    public Integer createOU(String baseDN, String dn, String depId, String name) throws LDAPException {
        if(dn.indexOf("+")!=-1){
            dn=dn.replace("+","\\+");
        }
        String entryDN = dn + "," + baseDN;
        // 连接LDAPs
        SearchResultEntry entry = ldapConnection.getEntry(entryDN);
        if (entry == null) {
            // 不存在则创建
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            attributes.add(new Attribute("objectClass", "top", "organizationalUnit"));
            attributes.add(new Attribute("ou", name));
            attributes.add(new Attribute("oaid", depId));
            ldapConnection.add(entryDN, attributes);
            return ErrorCode.success.ordinal();
        } else {
            return ErrorCode.exist.ordinal();
        }
    }

    public Long findParentId(String dn)throws LDAPException{
        SearchResultEntry entry = ldapConnection.getEntry(dn);
        String oaid=entry.getAttributeValue("oaid");
        Long parentId;
        if(oaid==null){
            parentId=0L;
        }else if(oaid.startsWith("10000")){
            parentId= StringCommonUtils.formatHrmSubCompanyId(oaid);
        }else{
            parentId=Long.valueOf(oaid);
        }
        return parentId;
    }


    /***
     * 创建用户信息
     * @param baseDN
     * @param dn
     * @return
     * @throws LDAPException
     */
    @Override
    public Integer createEntry(String baseDN, String dn, String loginid, String name) throws LDAPException, UnsupportedEncodingException {
        String entryDN = dn + "," + baseDN;

        BindResult auth = sslLdapConnection.bind(ldapProperties.getLdapBindDN(), ldapProperties.getLdapPassword());
        if (auth.getResultCode().isConnectionUsable()) {
            SearchResultEntry entry = sslLdapConnection.getEntry(entryDN);
            if (entry == null) {
                //不存在则创建
                ArrayList<Attribute> attributes = new ArrayList<Attribute>();
                attributes.add(new Attribute("objectClass", "top", "person", "organizationalPerson", "user"));
                attributes.add(new Attribute("sAMAccountName", loginid));
                attributes.add(new Attribute("cn", loginid));
                attributes.add(new Attribute("sn", loginid));
                attributes.add(new Attribute("userPrincipalName", loginid + "@stepelectric.com"));
                attributes.add(new Attribute("description", name));
                attributes.add(new Attribute("name", name));
                attributes.add(new Attribute("displayName", name));
                attributes.add(new Attribute("userAccountControl", "544"));
                attributes.add(new Attribute("unicodePwd", "\"Passw0rd\"".getBytes("UTF-16LE")));
                // attributes.add(new Attribute("userAccountControl", "544"));

                sslLdapConnection.add(entryDN, attributes);
                return ErrorCode.success.ordinal();
            } else {
                return ErrorCode.exist.ordinal();
            }
        }
        return null;

    }
    /**
     * 查找Ldap上的数据 返回部门
     */
    public List<Element> fetchElementsByType(String baseDN, Element.ElementType elementType) throws LDAPException {
        List<Element> result = new ArrayList<Element>();
        Element element;
        SearchResult searchResult = searchResult(baseDN, elementType);
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {
            element=new Element();
            String parentDn=entry.getParentDNString();
            String oaid=entry.getAttributeValue("oaid");
            String name=entry.getAttributeValue("name");
            if(oaid!=null && !name.isEmpty()) {
                element.setDn(entry.getDN());
                element.setId(StringCommonUtils.formatHrmSubCompanyId(oaid));
                element.setName(name);
                if(parentDn.equalsIgnoreCase(baseDN)){
                    element.setParentId(0L);
                }else{
                    Long parentId=findParentId(parentDn);
                    element.setParentId(parentId);
                }
                element.setParentDn(parentDn.toLowerCase());
                result.add(element);
            }
        }
        return result;
    }
    /***
     * 修改用户信息
     * @param requestDn
     * @param data
     * @return int
     */
    @Override
    public Integer modifyEntry(String requestDn, Map<String, String> data) throws LDAPException, UnsupportedEncodingException {
        SearchResultEntry entry = ldapConnection.getEntry(requestDn);
        if (entry == null) {
            System.out.println(requestDn + " user:" + requestDn + "" + entry);
            return null;
        }
        // 修改信息
        ArrayList<Modification> md = new ArrayList<Modification>();
        for (String key : data.keySet()) {
            if (key.equalsIgnoreCase("unicodePwd")) {
                md.add(new Modification(ModificationType.REPLACE, key, "\"Qazxsw123\"".getBytes("UTF-16LE")));
            } else {
                md.add(new Modification(ModificationType.REPLACE, key, data.get(key)));
            }
        }
        ldapConnection.modify(requestDn, md);

        System.out.println("修改用户信息成！");
        return null;
    }

    /**
     * 修改公司或部门的用户信息
     * @param element
     * @param baseDn
     * @return
     * @throws LDAPException
     */
    public Integer modifyName(Element element, String baseDn) throws LDAPException {
        String dn=element.getDn();
        int index=element.getDn().indexOf("=");
        String separator=element.getDn().substring(0,index);
        if(element.getModifyName()!=null){
            dn=separator+"="+element.getModifyName()+","+element.getParentDn();
        }
        SearchResultEntry  entry = ldapConnection.getEntry(dn);
        if(entry==null){
            return null;
        }
        ModifyDNRequest modifyDNRequest =
                new ModifyDNRequest(dn, separator+"="+element.getName(), true);
        LDAPResult modifyDNResult=ldapConnection.modifyDN(modifyDNRequest);
        return null;
    }

    /**
     * 修改部门或子公司的parentDN
     * @param element
     * @param baseDn
     * @return
     * @throws LDAPException
     */
    public Integer modifyParentDN(Element element, String baseDn)throws LDAPException{
        String requestDn="ou="+element.getName()+","+element.getModifyName();
        SearchResultEntry  entry = ldapConnection.getEntry(requestDn,"oaid="+element.getId()+"");
        if(entry==null){
            return null;
        }
        ModifyDNRequest modifyDNRequest =
                new ModifyDNRequest(requestDn,"ou="+element.getName(), true,element.getParentDn());
        LDAPResult modifyDNResult=ldapConnection.modifyDN(modifyDNRequest);
        return null;
    }

    /**
     * 查询Ldap上的所有的人员信息
     * @param baseDN
     * @return
     * @throws LDAPException
     */
    public List<Element> fetchPersons(String baseDN, Element.ElementType elementType) throws LDAPException {
        List<Element> result = new ArrayList<Element>();
        Element element;
        SearchResult searchResult = searchResult(baseDN, elementType);
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {
            element = new Element();
            String parentDN = entry.getParentDNString();
            element.setParentDn(parentDN);
            String name = entry.getAttributeValue("name");
            element.setName(name);
            result.add(element);
        }
        return result;
    }

    /**
     * objectclass ldap查询数据
     * @param baseDN
     * @param elementType
     * @return
     * @throws LDAPException
     */
    private SearchResult searchResult(String baseDN, Element.ElementType elementType)throws LDAPException {
        Filter filter = Filter.createEqualityFilter("objectClass", elementType.name());
        SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, filter);
        searchRequest.addControl(new SubentriesRequestControl());
        SearchResult searchResult = ldapConnection.search(searchRequest);
        return searchResult;
    }

    /**
     * 移出对应部门下的所有人员
     * @param hrmDTO
     * @param baseDn
     * @param elementType
     * @param newParentDn
     * @return
     * @throws LDAPException
     */
    public Integer moveOutPeople(Element hrmDTO, String baseDn ,Element.ElementType elementType,String newParentDn) throws LDAPException {
        String requestDn = "cn=" + hrmDTO.getName() + "," + hrmDTO.getParentDn();
        String entryDn="ou="+newParentDn+","+baseDn;
        SearchResultEntry entry = ldapConnection.getEntry(requestDn);
        SearchResultEntry entryParentOU=ldapConnection.getEntry("ou="+newParentDn+","+baseDn);
        if (entryParentOU == null) {
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            attributes.add(new Attribute("objectClass", "top", elementType.name()));
            attributes.add(new Attribute("ou", newParentDn));
            ldapConnection.add(entryDn, attributes);
        }else if(entry==null){
            return null;
        }
        ModifyDNRequest modifyDNRequest =
                new ModifyDNRequest(requestDn, "cn=" + hrmDTO.getName(), true,"ou="+newParentDn+","+baseDn);
        LDAPResult modifyDNResult = ldapConnection.modifyDN(modifyDNRequest);
        return null;
    }

    /**
     * 还原部门下对应的人员
     * @param element
     * @param baseDn
     * @param oldParentDn
     * @return
     * @throws LDAPException
     */
    public Integer moveInPeople(Element element,String baseDn,String oldParentDn)throws LDAPException{
        String requestDn="cn="+element.getName()+",ou="+oldParentDn+","+baseDn;
        SearchResultEntry entry=ldapConnection.getEntry(requestDn);
        if(entry==null){
            return null;
        }
        String parentDn=element.getParentDn().replace("+","\\+")+baseDn;
        ModifyDNRequest modifyDNRequest =
                new ModifyDNRequest(requestDn, "cn="+element.getName(), true,parentDn);
        LDAPResult modifyDNResult = ldapConnection.modifyDN(modifyDNRequest);
        return null;
    }

    public Integer modifyAttributes(Element element,String baseDn) throws LDAPException{
        element.setModifyName(element.getName());
        element.setName(element.getName()+"Delete");
        modifyName(element,baseDn);
        int index=element.getDn().indexOf("=");
        String separator=element.getDn().substring(0,index);
        String dn=separator+"="+element.getName()+","+element.getParentDn();
        SearchResultEntry  entry = ldapConnection.getEntry(dn);
        if(entry==null){
            return null;
        }
        List<Modification> modifications=new ArrayList<>();
        modifications.add(new Modification(ModificationType.REPLACE,"oaid",element.getId()+"10000"));
        ModifyRequest modifyRequest=new ModifyRequest(dn,modifications);
        ldapConnection.modify(modifyRequest);
        return null;
    }
}
