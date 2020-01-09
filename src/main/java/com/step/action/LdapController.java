package com.step.action;

import com.step.config.LdapCondition;
import com.step.entity.primary.Element;
import com.step.entity.primary.HrmDepartment;
import com.step.entity.primary.HrmResource;
import com.step.entity.primary.HrmSubCompany;
import com.step.properties.LdapProperties;
import com.step.service.HrmDepartmentService;
import com.step.service.HrmResourceService;
import com.step.service.HrmSubCompanyService;
import com.step.service.LdapService;
import com.step.strategy.StrategyContext;
import com.step.utils.StringCommonUtils;
import com.unboundid.ldap.sdk.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.step.entity.primary.Element.ElementType;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by user on 2019-04-17.
 */
@RestController
@RequestMapping("/ldap")
@Slf4j
@Conditional(value= LdapCondition.class)
public class LdapController {
    public static String movePeople = "移出人员";//移出的父级目录
    @Autowired
    private LdapService ldapService;
    @Autowired
    private HrmSubCompanyService hrmSubCompanyService;
    @Autowired
    private HrmDepartmentService hrmDepartmentService;
    @Autowired
    private HrmResourceService hrmResourceService;
    @Autowired
    private LdapProperties ldapProperties;
    @GetMapping("test")
    public String test() {
        String baseDn = "dc=" + ldapProperties.getDc() + ",dc=" +ldapProperties.getRoot();
        List<Long> cids = syncCompany(baseDn);
        //同步部门信息
        List<Long> depids = syncDepartment(baseDn, cids);

        //同步部门获取人员
        syncHrmResource(baseDn, depids);
        return "成功";
    }

    private List<Long> syncCompany(String baseDn) {
        List<HrmSubCompany> roots = hrmSubCompanyService.findRoots();
        List<HrmSubCompany> subCompanyList = new ArrayList<>();
        List<HrmSubCompany> childrens = null;
        for (HrmSubCompany hrmSubCompany : roots) {
            childrens = hrmSubCompanyService.findChildren(hrmSubCompany, true, null);
            subCompanyList.addAll(childrens);
        }
        //创建公司(组织)
        List<Long> cids = new ArrayList<>();
        for (HrmSubCompany hrmSubCompany : subCompanyList) {
            String dn = StringCommonUtils.formatString(hrmSubCompany.getTreePath(), 0);
            cids.add(hrmSubCompany.getId());
            //o="新时达"
            try {
                if (dn.contains("ou=")) {
                    ldapService.createOU(baseDn, dn, "10000" + hrmSubCompany.getId(), hrmSubCompany.getName());
                } else {
                ldapService.createO(baseDn, dn, hrmSubCompany.getName(), "10000" + hrmSubCompany.getId());
                }
            } catch (LDAPException e) {
                log.error(e.getMessage());
            }
        }
        return cids;
    }

    /***
     * 同步部门信息
     * @param cids
     * @return
     */
    private List<Long> syncDepartment(String baseDn, List<Long> cids) {
        //创建组织单元
        List<HrmDepartment> departments = hrmDepartmentService.findByCompanyIds(cids);
        List<Long> depids = new ArrayList<Long>();
        for (HrmDepartment hrmDepartment : departments) {
            depids.add(hrmDepartment.getId());
            String dn = StringCommonUtils.formatString(hrmDepartment.getTreePath(), 0);
            try {
                ldapService.createOU(baseDn, dn, hrmDepartment.getId() + "", hrmDepartment.getName());
            } catch (LDAPException e) {
                log.error(e.getMessage());
            }
        }
        return depids;
    }

    /***
     * 同步用户信息
     * @param depids
     */
    private void syncHrmResource(String baseDn, List<Long> depids) {
        List<HrmResource> hrmResources = hrmResourceService.findByDepartmentIds(depids);
        for (HrmResource hrmResource : hrmResources) {
            try {
                String dn = StringCommonUtils.formatString(hrmResource.getTreePath(), 0);
                if (dn.indexOf("+") != -1) {
                    dn = dn.replace("+", "\\+");
                }
                ldapService.createEntry(baseDn, dn, hrmResource.getLoginid(), hrmResource.getLastname());
            } catch (LDAPException e) {
                log.error(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("create")
    public String createDc() {
        try {
            //创建dc
            ldapService.createDC("dc=" + ldapProperties.getRoot(), ldapProperties.getDc());
        } catch (LDAPException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        //
        return "asdsadasd";
    }


    /**
     * 修改公司
     * fetch 从ldap加载数据
     * find 从db加载数据
     */
    @GetMapping
    public String modifyAttribute() {
        String baseDn = "dc=" + ldapProperties.getDc() + ",dc=" + ldapProperties.getRoot();
        List<Element> rootElementList = findElementsByDn(baseDn);
        List<Element> dbElementList = findElementChildsByDn(baseDn);
        dbElementList.addAll(rootElementList);
        try {
            //修改操作
            List<Element> ldapHrmSubCompanyRoot = ldapService.fetchElementsByType(baseDn, ElementType.organization);
            List<Element> ldapElementList = ldapService.fetchElementsByType(baseDn, ElementType.organizationalUnit);
            ldapElementList.addAll(ldapHrmSubCompanyRoot);
            syncOrganization(dbElementList, ldapElementList, baseDn);
            log.info("修改成功");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "修改完成";
    }

    /**
     * Element修改名字和parentDn
     *
     * @param ldapElements
     * @param dbElements
     * @param baseDn
     * @throws LDAPException
     */
    private Integer syncOrganization(List<Element> dbElements, List<Element> ldapElements, String baseDn) throws LDAPException {
        List<Element> datas = formatDatas(dbElements, ldapElements,baseDn);
       //修改名字列表
        List<Element> modifyNameList = datas.stream().filter(element -> element.getMethod() == "U").collect(Collectors.toList());
       //需要创建列表
        List<Element> createDataList = datas.stream().filter(element -> element.getMethod() == "C").collect(Collectors.toList());
        //修改parentDn列表
        List<Element> modifyParentDnList = datas.stream().filter(element -> element.getMethod() == "update-parentDn").collect(Collectors.toList());

        modifyNameList.stream().forEach((element)->{
            StrategyContext  strategyContext =new StrategyContext((element1,baseDn1,persons) ->{
                    ldapService.modifyName(element, baseDn);
            });
            try{
             strategyContext.invoke(element,baseDn,null);
            }catch (LDAPException e){
                log.error(e.getMessage());
            }
        });

        createDataList.stream().forEach((element)->{
            StrategyContext  strategyContext =new StrategyContext((element1,baseDn1,persons) ->{
                if(element.getHrmSubCompany()!=null){
                    String dn = StringCommonUtils.formatString(element.getHrmSubCompany().getTreePath(), 0);
                    if (dn.contains("ou=")) {
                        ldapService.createOU(baseDn, dn, "10000" + element.getHrmSubCompany().getId(), element.getHrmSubCompany().getName());
                    } else {
                        ldapService.createO(baseDn, dn, element.getHrmSubCompany().getName(), "10000" + element.getHrmSubCompany().getId());
                    }
                }else if(element.getHrmDepartment()!=null){
                    String dn = StringCommonUtils.formatString(element.getHrmDepartment().getTreePath(), 0);
                    ldapService.createOU(baseDn, dn, element.getHrmDepartment().getId() + "", element.getHrmDepartment().getName());
                }
            });
            try{
                strategyContext.invoke(element,baseDn,null);
            }catch (LDAPException e){
                log.error(e.getMessage());
            }

        });
        List<Element> ldapPersons = ldapService.fetchPersons(baseDn, ElementType.organizationalPerson);
        modifyParentDnList.stream().forEach((element)->{
            StrategyContext  strategyContext =new StrategyContext((element1,baseDn1,persons) ->{
                if (ldapProperties.getIsMove()) {
                    //移出人员
                    for (Element innerElement : ldapPersons) {
                        String ElementDn =innerElement.getParentDn().toLowerCase();
                        String hDTODn = element.getModifyName();
                        if (ElementDn.indexOf(hDTODn) != -1) {
                            ldapService.moveOutPeople(innerElement, baseDn, Element.ElementType.organizationalUnit, LdapController.movePeople);
                        }
                    }
                    ldapService.modifyParentDN(element, baseDn);
                } else {
                    ldapService.modifyParentDN(element, baseDn);
                }
            });
            try{
                strategyContext.invoke(element,baseDn,ldapPersons);
            }catch (LDAPException e){
                log.error(e.getMessage());
            }
        });
        return null;
    }


    /**
     * DB查询根目录公司
     * @return
     */
    private List<Element> findElementsByDn(String baseDn) {
        List<HrmSubCompany> hrmSubCompanies = hrmSubCompanyService.findRoots();
        List<Element> elements = new ArrayList<>();
        Element Element;
        for (HrmSubCompany hrmSubCompany : hrmSubCompanies) {
            Element = new Element();
            Element.setId(hrmSubCompany.getId());
            Element.setName(hrmSubCompany.getName());
            String dn=hrmSubCompany.getTreePath()+baseDn;
            Element.setDn(dn);
            Element.setParentId(0L);
            Element.setHrmSubCompany(hrmSubCompany);
            Element.setParentDn(StringCommonUtils.formatParentDn(hrmSubCompany.getTreePath(), baseDn));
            elements.add(Element);
        }
        return elements;
    }

    /**
     * DB查询部门以及子公司列表
     * @return
     */
    private List<Element> findElementChildsByDn(String baseDn) {
        List<HrmSubCompany> dbSubCompanys = hrmSubCompanyService.findAll();
        List<HrmDepartment> dbDepartments = hrmDepartmentService.findAllPO();
        List<Element> objectOU = new ArrayList<>();
        Element Element;
        for (HrmSubCompany hsc : dbSubCompanys) {
            Element = new Element();
            String dn=hsc.getTreePath()+baseDn;
            Element.setDn(dn);
            Element.setId(hsc.getId());
            Element.setName(hsc.getName());
            if(hsc.getParent()==null){
                Element.setParentId(0L);
            }else{
                Element.setParentId(hsc.getParent().getId());
            }
            Element.setHrmSubCompany(hsc);
            Element.setParentDn(StringCommonUtils.formatParentDn(hsc.getTreePath(), baseDn).toLowerCase());
            objectOU.add(Element);
        }
        for (HrmDepartment hd : dbDepartments) {
            Element = new Element();
            Element.setId(hd.getId());
            String dn=hd.getTreePath()+baseDn;
            Element.setDn(dn);
            Element.setName(hd.getName());
            Element.setParentDn(StringCommonUtils.formatParentDn(hd.getTreePath(), baseDn).toLowerCase());
            if (hd.getParent() == null) {
                Element.setParentId(hd.getHrmSubCompany().getId());
            } else {
                Element.setParentId(hd.getParent().getId());
            }
            Element.setHrmDepartment(hd);
            objectOU.add(Element);
        }
        return objectOU;
    }

    /**
     * 添加标记 create or update
     *
     * @param ldapElements
     * @param dbElements
     * @return
     */
    private List<Element> formatDatas(List<Element> dbElements, List<Element> ldapElements,String baseDn) {
        List<Element> results = new ArrayList<>();
        for (Element element : dbElements) {
            boolean flag = false;
            for (Element innerElement : ldapElements) {
                  if(innerElement.getId().equals(element.getId())){
                      //修改或不做
                      if (!innerElement.equals(element)){
                          if (innerElement.getParentId().equals(element.getParentId())) {
                              element.setModifyName(innerElement.getName());
                              element.setMethod("U");
                              results.add(element);
                          } else {
                              element.setModifyName(innerElement.getParentDn());
                              element.setMethod("update-parentDn");
                              results.add(element);
                          }
                      }else if(!StringCommonUtils.formatName(innerElement).equalsIgnoreCase(StringCommonUtils.formatName(element).replace("+","\\+"))){
                          try {
                              ldapService.modifyAttributes(innerElement,baseDn);
                          }catch (LDAPException e){
                              log.error(e.getMessage());
                          }
                      }
                      flag = true;
                      break;
                  }
            }
            if (!flag) {
                element.setMethod("C");
                results.add(element);
                log.info("执行创建:id="+element.getId()+",name="+element.getName());
            }
        }
        return results;
    }


    /**
     * 还原移出的人员
     *
     * @return
     */
    @GetMapping(value = "recoveryPerson")
    public String recovery() {
        String baseDn = "dc=" + ldapProperties.getDc() + ",dc=" + ldapProperties.getRoot();
        List<Element> peopleListVO = new ArrayList<>();
        try {
            List<Element> peopleList = ldapService.fetchPersons(baseDn, ElementType.organizationalPerson);
            peopleListVO.addAll(peopleList);
        } catch (LDAPException e) {
            log.error(e.getMessage());
        }
        List<Element> moveInList = new ArrayList<>();
        for (Element list : peopleListVO) {
            if (list.getParentDn().toLowerCase().equals("ou=" + movePeople + "," + baseDn)) {
                moveInList.add(list);
            }
        }
        List<Element> getRootPO = findElementsByDn(baseDn);
        List<Element> getChildPO = findElementChildsByDn(baseDn);
        List<HrmResource> hrmResourceListPO = hrmResourceService.findAll();
        for (Element Element : moveInList) {
            for (HrmResource hr : hrmResourceListPO) {
                if (Element.getName().equals(hr.getLoginid())) {
                    Integer index = hr.getTreePath().indexOf(",");
                    String parentDn = hr.getTreePath().substring(index + 1);
                    Element.setParentDn(parentDn);
                    try {
                        ldapService.moveInPeople(Element, baseDn, movePeople);
                    } catch (LDAPException e) {
                        log.error(e.getMessage());
                    }
                    break;
                }
            }
        }
        log.info("还原人员成功");
        return "还原人员成功";
    }

}