package com.step.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.step.entity.primary.*;
import com.step.entity.dto.FnaYearPeriodDto;
import com.step.entity.primary.vo.FnaBudgetInfoVo;
import com.step.exception.FnaException;
import com.step.repository.primary.*;
import com.step.service.FnaBudgetService;
import com.step.utils.JsonUtils;
import com.step.utils.StringCommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.step.entity.primary.form.*;
/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 预算控制
 */
@Service
@Transactional("transactionManagerPrimary")
public class FnaBudgetServiceImpl implements FnaBudgetService {
    @PersistenceContext(unitName="entityManagerFactoryPrimary")
    private EntityManager entityManager;
    @Autowired
    private FnaBudgetRepository fnaBudgetRepository;
    @Autowired
    private HrmDepartmentRepository hrmDepartmentRepository;
    @Autowired
    private MaycurLogRepository maycurLogRepository;
    //项目预算查找
    @Autowired
    private ProjectFnaBudgetRepository projectFnaBudgetRepository;
    //项目预算明细查找
    @Autowired
    private ProjectFnaExpenseItemRepository projectFnaExpenseItemRepository;
    @Override
    public Integer getCptCtrlSet() {
        return fnaBudgetRepository.isCptCtrlSet();
    }

    @Override
    @Cacheable
    public Integer getFnaYearId(String requestDate) {
       Integer ret= fnaBudgetRepository.getFnaYearId(requestDate);
        return ret;
    }

    /***
     * 获取部门预算信息明细表
     * @return map key 期，value 金额
     * @param bumen
     * @param fnaYearId
     * @param kemu
     */
    @Override
    public Map<Integer, BigDecimal> getFnaBudgetInfoDetailMap(String bumen, int fnaYearId, String kemu) {
        Query query= entityManager.createNativeQuery("select budgetperiodslist,budgetaccount from FnaBudgetInfoDetail where budgetinfoid = ( select id from FnaBudgetInfo where status=1 and organizationtype = 2 " +
               "and budgetorganizationid =:bumen and budgetperiods =:fnaYearId) and budgettypeid =:kemu");
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setParameter("bumen",bumen)
                .setParameter("fnaYearId",fnaYearId)
                .setParameter("kemu",kemu);
        List<Map<String,Object>> result=query.getResultList();
        HashMap<Integer,BigDecimal>yusuanmap = new HashMap();
        result.stream().forEach(i->{
            Integer key=null;
            BigDecimal account =null;
            try {
                 key =(int)i.get("budgetperiodslist");
                 account = new BigDecimal(i.get("budgetaccount") + "");
            }catch (Exception e){
                e.printStackTrace();
            }
              if(key!=null&&account!=null&&(account.compareTo(BigDecimal.ZERO)>0)) {
                  yusuanmap.put(key, account);
              }

        });
        return yusuanmap;
    }

    @Override
    public List<FnaYearPeriodDto> getFnaYearPeriod(int fnaYearId) {
        Query query=entityManager
                .createNativeQuery("select Periodsid,startdate,enddate " +
                        "from FnaYearsPeriodsList where fnayearid = ?1 " +
                        "and (startdate is not null and startdate <> '' " +
                        "and enddate is not null and enddate <> '')")
                .unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.aliasToBean(FnaYearPeriodDto.class))
                .setParameter(1,fnaYearId);

        List<FnaYearPeriodDto>  res=  query.getResultList();
        return res;
        //return fnaBudgetRepository.getFnaYearsPeriod(fnaYearId);
    }

    @Override
    public BigDecimal getFnaExpenseInfo(String finalKemu, String finalBumen, String startdate, String enddate, int status) {
        return fnaBudgetRepository.getFnaExpenseInfoSumAmount(finalKemu,finalBumen,startdate,enddate,status);
    }

    @Override
    public HrmDepartment getDepartment(String bumen) {
        HrmDepartment  hrmDepartment =hrmDepartmentRepository.findDepartmentById(Long.parseLong(bumen));
        return hrmDepartment;
    }

    @Override
    public List getFnaExpenseInfoList(String kemu, String bumen, List<FnaYearPeriodDto> periodDateDtos) {

        Specification<FnaExpenseInfo> spc= new Specification<FnaExpenseInfo>() {
            @Override
            public Predicate toPredicate(Root<FnaExpenseInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate =null;

                Predicate predicateOr= cb.or();
                for(FnaYearPeriodDto dto:periodDateDtos){
                    predicateOr.getExpressions().add(cb.or(cb.between(root.get("occurdate"),dto.getStartdate(),dto.getEnddate())));
                }
                predicate = cb.and(cb.equal(root.get("subject"),kemu)
                              ,predicateOr,
                              cb.equal(root.get("organizationtype"), "2")
                             ,cb.equal(root.get("organizationid"),bumen));
                return predicate;
            }
        };
        return fnaBudgetRepository.findAll(spc);




    }

    /***
     * 获取预算占用详情
     * @return
     */
    @Override
    public FnaBudgetInfoVo getFnaBudgetInfoByDepId(Date budgetExecDate, String budgetAccountBizCode, String bumen) {
        HrmDepartment hrmDepartment  = getDepartment(bumen);
        if(hrmDepartment==null){

            return null;
        }
        HrmSubCompany hrmSubCompany =null;
        Long fenbu =null;
        if(hrmDepartment!=null&&hrmDepartment.getHrmSubCompany()!=null){
             fenbu = hrmDepartment.getHrmSubCompany().getId();
        }
        Integer ctrlSet = getCptCtrlSet();
        DateTime dateTime = new DateTime(budgetExecDate);
        String requestDate = dateTime.toString("yyyy-MM-dd");//申请日期
        int fnaYearId=getFnaYearId(requestDate);//获取费用年id
        String kemu = "0";
        //费用科目
        if(StringUtils.isNotEmpty(budgetAccountBizCode)){
            kemu = budgetAccountBizCode;
        }
        if ("".equals(kemu)) {
            kemu = "0";
        }
        //预算
        Map<Integer,BigDecimal> yusuanmap =getFnaBudgetInfoDetailMap(bumen,fnaYearId,kemu);
        //获取开始时间及结算时间
        List<FnaYearPeriodDto> periodDateDtos =getFnaYearPeriod(fnaYearId);

        String finalKemu = kemu;
        String finalBumen = bumen;
        HashMap<Integer,BigDecimal> usedMap = new HashMap();//已使用费用
        HashMap<Integer,BigDecimal> spzfymap = new HashMap();//审批中费用
        final int[] Periodsid = {0};//会计期间
        List<FnaExpenseInfo>  fnaExpenseInfo= getFnaExpenseInfoList(kemu,bumen,periodDateDtos);
        IntStream.range(0, periodDateDtos.size()).forEach(i->{
            FnaYearPeriodDto  item = periodDateDtos.get(i);
            if(StringCommonUtils.isBetweens(item.getStartdate(),item.getEnddate(),requestDate) ){
                Periodsid[0] = item.getPeriodsid();
            }
            final BigDecimal[] accountSum = {BigDecimal.ZERO,BigDecimal.ZERO};
            List<FnaExpenseInfo>  tmpFnaExpenseInfos= fnaExpenseInfo.stream().filter(e->{
                if(StringCommonUtils.isBetweens(item.getStartdate(),item.getEnddate(),e.getOccurdate())){
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

            for(FnaExpenseInfo info:tmpFnaExpenseInfos){
                if(info.getStatus().equals(1)||info.getStatus().equals("1")){
                    accountSum[0] = accountSum[0].add(info.getAmount());
                }else{
                    accountSum[1] = accountSum[1].add(info.getAmount());
                }
            }
            usedMap.put(i,accountSum[0]);
//            //审批中费用
            spzfymap.put(i,accountSum[1]);
        });

        BigDecimal yusuan = BigDecimal.ZERO;
        BigDecimal yfsfy =  BigDecimal.ZERO;
        BigDecimal spzfy =  BigDecimal.ZERO;
        int periodsId = Periodsid[0];
        if (ctrlSet==1 && (30!=fenbu)) {
            yusuan =StringCommonUtils.convertBigDeciaml(yusuanmap.get(periodsId));
            yfsfy = StringCommonUtils.convertBigDeciaml(usedMap.get(periodsId));
            spzfy = StringCommonUtils.convertBigDeciaml(spzfymap.get(periodsId)) ;
        } else if (2==ctrlSet && (30!=fenbu)) {
            int num = periodsId / 3;
            if (periodsId == 3) {
                num = 0;
            } else if (periodsId == 6) {
                num = 1;
            } else if (periodsId == 9) {
                num = 2;
            } else if (periodsId == 12) {
                num = 3;
            }
            int num2 = periodsId % 3;
            if (num2 == 0) {
                num2 = 3;
            }
            BigDecimal yusuantmp = BigDecimal.ZERO;
            BigDecimal yfsfytmp = BigDecimal.ZERO;
            BigDecimal spzfytmp =BigDecimal.ZERO;
            for (int j = (num * 3) + 1; j <= ((num * 3) + 3); j++) {
                //new weaver.general.BaseBean().writeLog("yusuan:"+j+";"+Util.getFloatValue((String)yusuanmap.get(j+""),0.0f));
                yusuan = yusuan.add( StringCommonUtils.convertBigDeciaml(yusuanmap.get(j)));
                yfsfy = yfsfy.add(StringCommonUtils.convertBigDeciaml(usedMap.get(j)));
                spzfy = spzfy.add(StringCommonUtils.convertBigDeciaml(spzfymap.get(j)));
            }
        } else if (3==ctrlSet && (30!=fenbu)) {
            for (int n = 1; n <= periodsId; n++) {
                yusuan = yusuan.add( StringCommonUtils.convertBigDeciaml(yusuanmap.get(n))) ;
                yfsfy = yfsfy.add( StringCommonUtils.convertBigDeciaml(usedMap.get(n)));
                spzfy = spzfy.add( StringCommonUtils.convertBigDeciaml(spzfymap.get(n)));
            }
        } else if (4==ctrlSet || (30==fenbu)) {
            for (int m = 1; m <= 12; m++) {
                yusuan = yusuan.add(StringCommonUtils.convertBigDeciaml(yusuanmap.get(m )));
                yfsfy = yfsfy.add(StringCommonUtils.convertBigDeciaml(usedMap.get(m)));
                spzfy = spzfy.add(StringCommonUtils.convertBigDeciaml(spzfymap.get(m)));
            }
        }
        //返回每刻需要的数据
        FnaBudgetInfoVo fnaBudgetVo = new FnaBudgetInfoVo();
        fnaBudgetVo.setTotalAmount(yusuan.toString());
        fnaBudgetVo.setFreezedAmount(spzfy.toString());
        fnaBudgetVo.setUsedAmount(yfsfy.toString());
        fnaBudgetVo.setLeftAmount(yusuan.subtract(spzfy).subtract(yfsfy).toString());
        Joiner joiner = Joiner.on(":").skipNulls();
        String departmentName = hrmDepartment.getName();
        //预算编码唯一，uuid
        String uuid = StringCommonUtils.getUUID();
        String budgetCode =StringCommonUtils.encoderToBase64(joiner.join(bumen,budgetAccountBizCode,requestDate,uuid));
        fnaBudgetVo.setBudgetCode(budgetCode);
        joiner = Joiner.on("-").skipNulls();
        String subjectName = getSubjectName(budgetAccountBizCode);
        fnaBudgetVo.setBudgetName(joiner.join(departmentName,subjectName));
        return fnaBudgetVo;
    }

    @Override
    @Cacheable
    public boolean isFnaController() {
        Integer ret=  fnaBudgetRepository.isFnaController();
        if(ret >0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional("transactionManagerPrimary")
    public List<BudgetExecEntrie> execBudget(FnaBudgetExecForm fnaBudgetExecForm) throws FnaException {
       Optional<MaycurLog> maycurLog = maycurLogRepository.findById(fnaBudgetExecForm.getRequestId());
        if(maycurLog.isPresent()){
            //如果已经存在不允许重复几条
            throw new FnaException("记录已存在不允许重复提交");
        }
        List<BudgetExecEntrie> entries=   fnaBudgetExecForm.getBudgetExecEntries();
        Map<String,BudgetExecEntrie>  budgetInfoMap=new ConcurrentHashMap<String,BudgetExecEntrie>();
        BudgetExecEntrie tmpBudgetExecEntrie;
        //校验通过执行
        List<BudgetExecEntrie> releasList = new ArrayList<>();
        List<BudgetExecEntrie> freezeList = new ArrayList<>();
        List<BudgetExecEntrie> occupList = new ArrayList<>();
        for(BudgetExecEntrie budgetInfo:entries){
            if(budgetInfo.getOpt().equals("FREEZE")){
                freezeList.add(budgetInfo);
            }else if(budgetInfo.getOpt().equals("RELEASE")){
                releasList.add(budgetInfo);
            }else if(budgetInfo.getOpt().equals("OCCUPY")){
                occupList.add(budgetInfo);
            }

            if(budgetInfoMap.containsKey(budgetInfo.getBudgetCode())) {
                tmpBudgetExecEntrie= budgetInfoMap.get(budgetInfo.getBudgetCode());

                if(budgetInfo.getOpt().equals("FREEZE")) {
                    tmpBudgetExecEntrie.setDecAmount(tmpBudgetExecEntrie.getDecAmount().add(budgetInfo.getDecAmount()));
                }else  if(budgetInfo.getOpt().equals("RELEASE")) {
                    tmpBudgetExecEntrie.setDecAmount(tmpBudgetExecEntrie.getDecAmount().subtract(budgetInfo.getDecAmount()));
                }
                budgetInfoMap.put(budgetInfo.getKey(),tmpBudgetExecEntrie);
            }else{
                //不存在丢到budgetInfoMap中
                try {
                    budgetInfoMap.put(budgetInfo.getKey(), (BudgetExecEntrie) budgetInfo.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        List<BudgetExecEntrie> error = new ArrayList<>();
        for(BudgetExecEntrie entity:budgetInfoMap.values()){
            try {
                if(entity.getOpt()!=null&&entity.getOpt().equals("FREEZE")) {
                    validateAmount(entity);
                }
            } catch (FnaException e) {
                e.printStackTrace();
                entity.setErrMsgEn(e.getMessage());
                error.add(entity);
            }
        }

        if(error!=null&&error.size()>0){
            //抛出异常信息
            return error;
        }




        //释放预算
        for(BudgetExecEntrie  entrie:releasList){
            //根据BudgetCode 删除预算
            String decoderBudgetCode = StringCommonUtils.decoderToString(entrie.getBudgetCode());
            if(decoderBudgetCode.endsWith("000000")) {
                //000000结尾为项目预算
                List<String> result = Splitter.on(":").trimResults().splitToList(decoderBudgetCode);
                String sn = result.get(0);
                 ProjectFnaExpenseInfo info =  getProjectFnaExpenseInfoBySn(sn);
                 BigDecimal amount = StringCommonUtils.convert2BigDecimal(entrie.getAmount());
                ProjectFnaExpenseInfoItem item = new ProjectFnaExpenseInfoItem(entrie.getBudgetCode(),amount,info.getId());
                item.setOpt(entrie.getOpt());
                //保存操作记录
                projectFnaExpenseItemRepository.save(item);
                BigDecimal  freezedPrice =StringCommonUtils.convertBigDeciaml(info.getFreezedPrice()).subtract(amount);
                 info.setFreezedPrice(freezedPrice.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:freezedPrice);
                 //剩余金额不能小于0
                BigDecimal rediducePrice= StringCommonUtils.convertBigDeciaml(info.getTotalPrice())
                        .subtract(StringCommonUtils.convertBigDeciaml(info.getUsedPrice()))
                        .subtract(StringCommonUtils.convertBigDeciaml(info.getFreezedPrice()));
                 info.setResiduedPrice(rediducePrice.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:rediducePrice);
                 projectFnaBudgetRepository.save(info);
            }else {
                //部门预算
                fnaBudgetRepository.deleteByBudgetCode(entrie.getBudgetCode());
            }
        }




        //冻结预算
       for(BudgetExecEntrie entrie:freezeList){
           String decoderBudgetCode = StringCommonUtils.decoderToString(entrie.getBudgetCode());
           if(decoderBudgetCode.endsWith("000000")) {
               //项目预算占用
               List<String> result = Splitter.on(":").trimResults().splitToList(decoderBudgetCode);
               String sn = result.get(0);
               ProjectFnaExpenseInfo info =  getProjectFnaExpenseInfoBySn(sn);
               BigDecimal amount = StringCommonUtils.convert2BigDecimal(entrie.getAmount());
               ProjectFnaExpenseInfoItem item = new ProjectFnaExpenseInfoItem(entrie.getBudgetCode(),amount,info.getId());
               item.setOpt(entrie.getOpt());
               //保存操作记录
               projectFnaExpenseItemRepository.save(item);


               info.setFreezedPrice(StringCommonUtils.convertBigDeciaml(info.getFreezedPrice()).add(amount));
               //剩余金额不能小于0
               BigDecimal rediducePrice= StringCommonUtils.convertBigDeciaml(info.getTotalPrice())
                       .subtract(StringCommonUtils.convertBigDeciaml(info.getUsedPrice()))
                       .subtract(StringCommonUtils.convertBigDeciaml(info.getFreezedPrice()));
               info.setResiduedPrice(rediducePrice.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:rediducePrice);
//               修改项目预算总金额
               projectFnaBudgetRepository.save(info);


           }else {
               //部门预算占用
               FnaExpenseInfo fnaExpenseInfo =convertEntityToFnaExpenseInfo(entrie,0);
               if (getSequenceId() == null) {
                   throw new FnaException("冻结预算，ID不能为空");
               }
               try {
                   //fnaBudgetRepository.setOn();
                   insert(fnaExpenseInfo);
                   //fnaBudgetRepository.save(fnaExpenseInfo);
               } finally {
                   //fnaBudgetRepository.setOff();
               }
           }
       }

        //占用预算
        try {
            for (BudgetExecEntrie entrie : occupList) {
                String decoderBudgetCode = StringCommonUtils.decoderToString(entrie.getBudgetCode());
                if (decoderBudgetCode.endsWith("000000")) {
                    List<String> result = Splitter.on(":").trimResults().splitToList(decoderBudgetCode);
                    String sn = result.get(0);
                    ProjectFnaExpenseInfo info = getProjectFnaExpenseInfoBySn(sn);
                    BigDecimal amount = StringCommonUtils.convert2BigDecimal(entrie.getAmount());

                    ProjectFnaExpenseInfoItem item = new ProjectFnaExpenseInfoItem(entrie.getBudgetCode(), amount, info.getId());
                    item.setOpt(entrie.getOpt());
                    //保存操作记录
                    projectFnaExpenseItemRepository.save(item);

//                    BigDecimal freezedPrice = StringCommonUtils.convertBigDeciaml(info.getFreezedPrice());
//                    if (freezedPrice.compareTo(amount) < 0) {
//                        entrie.setErrMsgEn("没有足够的冻结预算来占用");
//                        error.add(entrie);
//                        throw new FnaException("没有足够的冻结预算来占用");
//                    }
                    //占用费用
                    info.setUsedPrice(StringCommonUtils.convertBigDeciaml(info.getUsedPrice()).add(amount));
                    //info.setFreezedPrice(StringCommonUtils.convertBigDeciaml(info.getFreezedPrice()).subtract(amount));
                    //剩余金额不能小于0
                    BigDecimal rediducePrice = StringCommonUtils.convertBigDeciaml(info.getTotalPrice())
                            .subtract(StringCommonUtils.convertBigDeciaml(info.getUsedPrice()))
                            .subtract(StringCommonUtils.convertBigDeciaml(info.getFreezedPrice()));
                    info.setResiduedPrice(rediducePrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : rediducePrice);
//               修改项目预算总金额
                    projectFnaBudgetRepository.save(info);


                } else {
                    //部门预算
                    FnaExpenseInfo fnaExpenseInfo = fnaBudgetRepository.getOneByBudgetCode(entrie.getBudgetCode());
                    if(fnaExpenseInfo==null){
                        fnaExpenseInfo= convertEntityToFnaExpenseInfo(entrie,1);
                        insert(fnaExpenseInfo);
                    }else {
                        fnaExpenseInfo.setStatus(1);
                        fnaBudgetRepository.save(fnaExpenseInfo);
                    }
                }
            }
        }catch (Exception e){
            if(error!=null&&error.size()>0){
                return error;
            }
        }
        /***
         * 新增日志记录
         */
        MaycurLog tmpMaycurLog = new MaycurLog();
        tmpMaycurLog.setRequestId(fnaBudgetExecForm.getRequestId());
        tmpMaycurLog.setLogType(MaycurLog.LogType.FnaBudgetExec);
        tmpMaycurLog.setLastModifyTime(new Date());
        String content = JsonUtils.toString(fnaBudgetExecForm);
        if(content.length()>1995){
            content=content.substring(0,1995);
        }
        tmpMaycurLog.setContent(content);
        tmpMaycurLog.setStatus("0");
        maycurLogRepository.save(tmpMaycurLog);


        return  null;
    }

    /***
     * 将entity转成FnaExpenseInfo
     * @param entrie
     * @return
     */
    private FnaExpenseInfo convertEntityToFnaExpenseInfo(BudgetExecEntrie entrie,Integer status) throws FnaException {
        String decoderBudgetCode = StringCommonUtils.decoderToString(entrie.getBudgetCode());
        List<String> result = Splitter.on(":").trimResults()
                .splitToList(decoderBudgetCode);
        FnaExpenseInfo fnaExpenseInfo = new FnaExpenseInfo();
        if (getSequenceId() == null) {
            throw new FnaException("冻结预算，ID不能为空");
        }

        fnaExpenseInfo.setId(getSequenceId());
        fnaExpenseInfo.setGuid(entrie.getBudgetCode());//主key
        fnaExpenseInfo.setOrganizationtype(2);
        fnaExpenseInfo.setOrganizationid(Integer.parseInt(result.get(0)));
        fnaExpenseInfo.setSubject(Integer.parseInt(result.get(1)));
        fnaExpenseInfo.setOccurdate(result.get(2));
        fnaExpenseInfo.setAmount(new BigDecimal(entrie.getAmount()));
        fnaExpenseInfo.setStatus(status);
        fnaExpenseInfo.setType(2);
        fnaExpenseInfo.setRelatedprj(0);
        fnaExpenseInfo.setRelatedcrm(0);
        fnaExpenseInfo.setDescription("");
        return fnaExpenseInfo;
    }


    /***
     * 保存记录
      * @param fnaExpenseInfo
     */
    private void insert(FnaExpenseInfo fnaExpenseInfo){
       Query query= entityManager.createNativeQuery("SET IDENTITY_INSERT FnaExpenseInfo ON " +
                "insert into FnaExpenseInfo (id, organizationid, organizationtype,occurdate,amount,subject,status,type,guid,relatedcrm,relatedprj) " +
               "values (:id,:organizationid,:organizationtype,:occurdate,:amount,:subject,:status,:type,:guid,:relatedcrm,:relatedprj)" +
                " SET IDENTITY_INSERT FnaExpenseInfo OFF");
        query.setParameter("id",fnaExpenseInfo.getId());
        query.setParameter("organizationid",fnaExpenseInfo.getOrganizationid());
        query.setParameter("organizationtype",fnaExpenseInfo.getOrganizationtype());
        query.setParameter("occurdate",fnaExpenseInfo.getOccurdate());
        query.setParameter("amount",fnaExpenseInfo.getAmount());
        query.setParameter("subject",fnaExpenseInfo.getSubject());
        query.setParameter("status",fnaExpenseInfo.getStatus());
        query.setParameter("type",fnaExpenseInfo.getType());
        query.setParameter("guid",fnaExpenseInfo.getGuid());
        query.setParameter("relatedcrm",fnaExpenseInfo.getRelatedcrm());
        query.setParameter("relatedprj",fnaExpenseInfo.getRelatedprj());
        query.executeUpdate();
    }

    @Override
    public Integer getSequenceId() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class); //查询结果

//在哪个表查
        Root<FnaExpenseInfo> root = cq.from(FnaExpenseInfo.class);
        cq.select(cb.greatest((Path)root.get("id")));

//结果
        TypedQuery<Integer> typedQuery = entityManager.createQuery(cq);
        Integer ret = typedQuery.getSingleResult();
        if(ret!=null){
            ret +=1;
        }

        return ret;
    }


    /***
     *
     * 根据项目号查询预算信息
     *
     * @param budgetExecDate
     * @param code 业务实体预算信息
     * @return
     */
    @Override
    @Transactional("transactionManagerPrimary")
    public FnaBudgetInfoVo getFnaBudgetInfoByProjectBizCode(Date budgetExecDate, String code) {
        //返回每刻需要的数据
        DateTime dateTime = new DateTime(budgetExecDate);
        String requestDate = dateTime.toString("yyyy-MM-dd");//申请日期
        FnaBudgetInfoVo fnaBudgetVo = new FnaBudgetInfoVo();
        ProjectFnaExpenseInfo entity = getProjectFnaExpenseInfoBySn(code);
       if(entity!=null){
           //返回每刻需要的数据
           fnaBudgetVo.setTotalAmount(StringCommonUtils.convertBigDeciamlToString(entity.getTotalPrice()));
           fnaBudgetVo.setFreezedAmount(StringCommonUtils.convertBigDeciamlToString(entity.getFreezedPrice()));
           fnaBudgetVo.setUsedAmount(StringCommonUtils.convertBigDeciamlToString(entity.getUsedPrice()));
           fnaBudgetVo.setLeftAmount(StringCommonUtils.convertBigDeciamlToString(entity.getResiduedPrice()));
           Joiner joiner = Joiner.on(":").skipNulls();

           //预算编码唯一，uuid 部门：项目编号：请求日期：uuid
           String uuid = StringCommonUtils.getUUID();
           String budgetCode =StringCommonUtils.encoderToBase64(joiner.join(entity.getSn(),requestDate,uuid,"000000"));
           fnaBudgetVo.setBudgetCode(budgetCode);
           joiner = Joiner.on("-").skipNulls();
           fnaBudgetVo.setBudgetName(joiner.join(entity.getBusinessDepartmentName(),entity.getName()));
           fnaBudgetVo.setBudgetCode(budgetCode);
           return fnaBudgetVo;
       }
        return null;
    }

    private ProjectFnaExpenseInfo getProjectFnaExpenseInfoBySn(String code) {
        Specification<ProjectFnaExpenseInfo> spc= new Specification<ProjectFnaExpenseInfo>() {
            @Override
            public Predicate toPredicate(Root<ProjectFnaExpenseInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate =null;
                predicate = cb.and(cb.equal(root.get("sn"),code));
                return predicate;
            }
        };
       Optional<ProjectFnaExpenseInfo> opt= projectFnaBudgetRepository.findOne(spc);
       if(opt.isPresent()){
           return opt.get();
       }
        return null;
    }

    private String getSubjectName(String budgetAccountBizCode) {
        return fnaBudgetRepository.getSubjectName(budgetAccountBizCode);
    }

    /***
     * @param budgetExecEntrie 预算金额
     * 金额预算是否符合规范
     * @return
     */
    private void validateAmount(BudgetExecEntrie budgetExecEntrie)  throws FnaException {
        String budgetCode= budgetExecEntrie.getBudgetCode();
        String decoderBudgetCode = StringCommonUtils.decoderToString(budgetCode);
        if(decoderBudgetCode.endsWith("000000")) {
            //000000结尾为项目预算
            List<String> result = Splitter.on(":").trimResults()
                    .splitToList(decoderBudgetCode);
            if (result == null && result.size() <= 3) {
                throw new FnaException("预算编码格式不对，请使用部门:项目号:日期");
            }
            String sn = result.get(0);
            if(StringUtils.isEmpty(sn)){
                throw new FnaException("项目号不允许为空");
            }
           ProjectFnaExpenseInfo projectFnaExpenseInfo= getProjectFnaExpenseInfoBySn(sn);
           if(projectFnaExpenseInfo==null){
               throw new FnaException("此项目预算不存在:"+sn);
           }
           //剩余预算
            BigDecimal rediducePrice= StringCommonUtils.convertBigDeciaml(projectFnaExpenseInfo.getTotalPrice())
                    .subtract(StringCommonUtils.convertBigDeciaml(projectFnaExpenseInfo.getUsedPrice()))
                    .subtract(StringCommonUtils.convertBigDeciaml(projectFnaExpenseInfo.getFreezedPrice()));
            projectFnaExpenseInfo.setResiduedPrice(rediducePrice.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:rediducePrice);
           BigDecimal amout = StringCommonUtils.convert2BigDecimal(budgetExecEntrie.getAmount());
           if(rediducePrice.compareTo(BigDecimal.ZERO)<0||(rediducePrice.compareTo(amout)<0)){
               throw new FnaException("超预算请填项目预算追加流程:" + JsonUtils.toString(projectFnaExpenseInfo));
           }


            Date budgetExecDate = StringCommonUtils.stringToDate(result.get(2), "yyyy-MM-dd");

        }else if(decoderBudgetCode.endsWith("000001")){
            //个人预算
        }else{
            //为OA 部门预算
            if (isFnaController() && !budgetExecEntrie.isSpecial()) {
                List<String> result = Splitter.on(":").trimResults()
                        .splitToList(decoderBudgetCode);
                if (result == null && result.size() <= 3) {
                    throw new FnaException("预算编码格式不对，请使用部门:科目:日期");
                }
                //如果不控制预算

                Date budgetExecDate = StringCommonUtils.stringToDate(result.get(2), "yyyy-MM-dd");

                FnaBudgetInfoVo fnaBudgetInfo = getFnaBudgetInfoByDepId(budgetExecDate, result.get(1), result.get(0));
                BigDecimal LeftAmount =  StringCommonUtils.convert2BigDecimal(fnaBudgetInfo.getLeftAmount());
                if (fnaBudgetInfo.getLeftAmount() != null && LeftAmount.compareTo(BigDecimal.ZERO) > 0) {
                    //还有预算，否则走预算追加流程
                    if ( StringCommonUtils.convert2BigDecimal(budgetExecEntrie.getAmount()).compareTo(LeftAmount) > 0) {
                        throw new FnaException("超预算请填预算追加流程:" + JsonUtils.toString(fnaBudgetInfo));
                    }

                } else {
                    //预算追加流程
                    throw new FnaException("超预算请填预算追加流程:" + fnaBudgetInfo.toString() + ";" + fnaBudgetInfo.toString());
                }

            }
        }


    }
}
