package com.step.action;
import com.google.common.collect.Lists;
import com.step.config.WxCpConfiguration;
import com.step.entity.primary.HrmDepartment;
import com.step.entity.primary.HrmSubCompany;
import com.step.service.HrmDepartmentService;
import com.step.service.HrmSubCompanyService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpDepartmentService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-06-27.
 * email:604580436@qq.com
 * 微信企业号controller
 * cp 代表企业号，所有类得于Cp开头
 */
@RestController
@RequestMapping("/cp")
@Slf4j
public class WxCpPortalController {
    @Autowired
    private HrmSubCompanyService hrmSubCompanyService;
    @Autowired
    private HrmDepartmentService hrmDepartmentService;
    @Autowired
    private com.step.service.WxCpService wxCpService;

    @GetMapping("/dep/org")
    public String synData() {
        //创建公司
        List<Long> cids = createSubCompany();
        //创建部门
        createDepartment(cids);

        return "同步数据成功";
    }

    //修改人员
    @GetMapping("/usr/modify")
    public String modifyUser(){
        //修改人员对应的部门id
        wxCpService.modifyUsers();
        return "修改人员成功";
    }

    //创建人员
    @GetMapping("/usr/create")
    public String createUser(){
        wxCpService.createUser();
        return "创建人员成功";
    }

    //同步部门
    private  String createDepartment(List<Long> cids){
        //获取所有部门并转换成entity
        List<HrmDepartment> departments= hrmDepartmentService.findByCompanyIds(cids);
        //格式化数据
        List<WxCpDepart> dbDepartment= departments.stream().map(dep->{
            WxCpDepart wxCpDepart=new WxCpDepart();
            wxCpDepart.setId(dep.getId());
            wxCpDepart.setName(dep.getName());
            wxCpDepart.setOrder(Long.parseLong(dep.getShowOrder()+""));
            if(dep.getParent()==null){
                wxCpDepart.setParentId(dep.getHrmSubCompany().getCpId());
            }else{
                wxCpDepart.setParentId(dep.getParent().getId());
            }
            return wxCpDepart;
        }).distinct().collect(Collectors.toList());
        //排序
        List<WxCpDepart> dbDepartList = sortList(dbDepartment);
        modifyData(dbDepartList);
        return "";
    }

    //排序
    private List<WxCpDepart> sortList(List<WxCpDepart> wxCpDeparts){
        wxCpDeparts.sort((o1,o2)->{
            return o1.getId().compareTo(o2.getId());
        });
        return wxCpDeparts;
    }

    private List<Long> createSubCompany(){
        List<HrmSubCompany> companyRoots = hrmSubCompanyService.findRoots();
        //获取所有公司id,并新建
        List<WxCpDepart> wxCpSubCompany = Lists.newArrayList();
        List<Long> cids = Lists.newArrayList();
        companyRoots.forEach(e -> {
           //根据id查询子公司
            List<HrmSubCompany> childs = hrmSubCompanyService.findChildren(e, true, null);
            List<WxCpDepart> departs = childs.stream().map(company -> {
                WxCpDepart wxCpDepart=new WxCpDepart();
                wxCpDepart.setId(company.getCpId());
                wxCpDepart.setName(company.getName());
                wxCpDepart.setOrder(company.getShowOrder().longValue());
                wxCpDepart.setParentId(company.getCpParentedId());
                cids.add(company.getId());
                return wxCpDepart;
            }).collect(Collectors.toList());
            wxCpSubCompany.addAll(departs);
        });
        List<WxCpDepart> dbDepartList = sortList(wxCpSubCompany);
        modifyData(dbDepartList);
        return cids;
    }

    //创建微信公司或部门
    private void modifyData(List<WxCpDepart> dbDepartList){
        WxCpService wxCpService= WxCpConfiguration.getCpService(1000001);
        WxCpDepartmentService wxCpDepartmentService= wxCpService.getDepartmentService();
        List<WxCpDepart> wxCpDepartList=null;
        try{
            //微信部门List
            wxCpDepartList = wxCpDepartmentService.list(null);
        }catch (WxErrorException e){
            log.error(e.getMessage());
        }
        WxCpDepart wxCpDepart;
        String method="C";
        for(WxCpDepart dbDepartmentItem:dbDepartList){
            method="C";
            for(WxCpDepart wxCpDepartItem:wxCpDepartList){
                if(dbDepartmentItem.getId().equals(wxCpDepartItem.getId())){
                    method="U";
                }
            }
            if(method.equals("U")){
                try {
                    //修改部门
                    wxCpDepartmentService.update(dbDepartmentItem);
                }catch (WxErrorException e){
                    log.error(e.getMessage());
                }
            }else if(method.equals("C")){
                // 创建部门
                wxCpDepart=new WxCpDepart();
                wxCpDepart.setId(dbDepartmentItem.getId());
                wxCpDepart.setName(dbDepartmentItem.getName());
                wxCpDepart.setOrder(dbDepartmentItem.getOrder());
                wxCpDepart.setParentId(dbDepartmentItem.getParentId());
                try {
                    wxCpDepartmentService.create(wxCpDepart);
                }catch (WxErrorException e){
                    log.error(e.getMessage());
                }
            }
        }
    }
}
