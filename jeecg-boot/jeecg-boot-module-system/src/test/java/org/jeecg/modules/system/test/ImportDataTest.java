package org.jeecg.modules.system.test;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.contract.entity.Company;
import org.jeecg.modules.contract.entity.CompanyBank;
import org.jeecg.modules.contract.entity.Materiel;
import org.jeecg.modules.contract.service.ICompanyBankService;
import org.jeecg.modules.contract.service.ICompanyService;
import org.jeecg.modules.contract.service.IMaterielService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysPositionService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.test.entity.*;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author motb
 * @date 2021/4/8 16:51
 * @description //利用单元测试数据导入
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
public class ImportDataTest {
    // 组织机构excel文件路径
    private static final String DEPART_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\人资主数据\\组织 - 副本.XLSX";
    // 岗位excel文件路径
    private static final String POSITION_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\人资主数据\\岗位EXPORT - 副本.XLSX";
    // 用户excel文件路径
    private static final String USER_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\人资主数据\\员工EXPORT - 副本.XLSX";
    // 企业excel文件路径
    private static final String COMPANY_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\BP客商主数据202103291525\\企业主数据.xlsx";
    // 企业地址excel文件路径
    private static final String COMPANY_ADDRESS_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\BP客商主数据202103291525\\企业主数据 - 地址.xlsx";
    // 企业银行excel文件路径
    private static final String COMPANY_BANK_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\BP客商主数据202103291525\\企业主数据 - 银行.xlsx";
    // 物料excel文件路径
//    private static final String MATERIEL_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\物料主数据202103291525\\备品备件物料主数据20210329153526.xlsx";
//    private static final String MATERIEL_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\物料主数据202103291525\\辅料物料主数据20210329153905.xlsx";
    private static final String MATERIEL_FILE_PATH = "E:\\资料\\柳钢东信\\主数据20210329\\物料主数据202103291525\\六大类型物料主数据20210329152844.xlsx";


    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysPositionService sysPositionService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICompanyBankService companyBankService;

    @Autowired
    private IMaterielService materielService;

    /**
     * 导入组织机构
     */
    @Test
    public void importDepart() {
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        params.setNeedSave(true);
        File file = new File(DEPART_FILE_PATH);
        List<ImportDepartDo> importDepartDoList = ExcelImportUtil.importExcel(file, ImportDepartDo.class, params);
        // 排序
        importDepartDoList.sort(Comparator.comparing(ImportDepartDo::getId));
        // 转换系统部门
        List<SysDepart> sysDepartList = new ArrayList<>();
        for (ImportDepartDo departDo : importDepartDoList) {
            if ("01".equals(departDo.getStatus()) || StringUtils.isEmpty(departDo.getId())) { //已删除
                continue;
            }
            SysDepart sysDepart = new SysDepart();
            sysDepart.setId(departDo.getId());
            sysDepart.setParentId(departDo.getPid());
            sysDepart.setDepartName(departDo.getName());
            sysDepart.setOrgCategory(departDo.getCategory());
            sysDepart.setOrgType(departDo.getType());
            sysDepart.setStatus("1");
            sysDepart.setDelFlag("0");
            sysDepart.setDescription(departDo.getOtherCode());
            sysDepartList.add(sysDepart);
        }
        //构造树
        List<SysDepartTreeModel> sysDepartTreeModelList = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(sysDepartList);
        saveDepart(sysDepartTreeModelList, null);
    }

    private SysDepart createDepart(SysDepartTreeModel sysDepartTree) {
        SysDepart sysDepart = new SysDepart();
        sysDepart.setId(sysDepartTree.getId());
        sysDepart.setParentId(sysDepartTree.getParentId());
        sysDepart.setDepartName(sysDepartTree.getDepartName());
        sysDepart.setOrgCategory(sysDepartTree.getOrgCategory());
        sysDepart.setOrgType(sysDepartTree.getOrgType());
        sysDepart.setStatus("1");
        sysDepart.setDelFlag("0");
        sysDepart.setOrgCode(sysDepartTree.getOrgCode());
        sysDepart.setDescription(sysDepartTree.getDescription());
        return sysDepart;
    }

    private void saveDepart(List<SysDepartTreeModel> sysDepartTreeModelList, String parentCode) {
        List<SysDepart> saveList = new ArrayList<>();
        int index = 0;
        SysDepartTreeModel temp = null;
        for (SysDepartTreeModel sysDepartTreeModel : sysDepartTreeModelList) {
            String code = "";
            if (index == 0 && StringUtils.isEmpty(parentCode)) {
                code = YouBianCodeUtil.getNextYouBianCode(null);
            } else if (index == 0 && StringUtils.hasText(parentCode)) {
                code = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else if (index != 0 && StringUtils.isEmpty(parentCode)) {
                code = YouBianCodeUtil.getNextYouBianCode(temp.getOrgCode());
            } else if (index != 0 && StringUtils.hasText(parentCode)) {
                code = YouBianCodeUtil.getSubYouBianCode(parentCode, temp.getOrgCode());
            }
            temp = sysDepartTreeModel;
            index++;
            sysDepartTreeModel.setOrgCode(code);
            saveList.add(createDepart(sysDepartTreeModel));
            if (!CollectionUtils.isEmpty(sysDepartTreeModel.getChildren())) {
                saveDepart(sysDepartTreeModel.getChildren(), code);
            }
        }
        sysDepartService.saveBatch(saveList);
    }

    /**
     * 导入岗位
     */
    @Test
    public void importPosition() {
        File file = new File(POSITION_FILE_PATH);
        List<ImportPositionDo> positionDoList = ExcelImportUtil.importExcel(file, ImportPositionDo.class, new ImportParams());
        List<SysPosition> sysPositions = new ArrayList<>();
        for (ImportPositionDo positionDo : positionDoList) {
            if ("01".equals(positionDo.getStatus()) || StringUtils.isEmpty(positionDo.getId())) { //已删除
                continue;
            }
            SysPosition sysPosition = new SysPosition();
            sysPosition.setId(positionDo.getId());
            sysPosition.setCode(positionDo.getId());
            sysPosition.setCompanyId(positionDo.getCompanyId());
            sysPosition.setName(positionDo.getName());
            sysPosition.setPostRank(positionDo.getLevel());
            SysDepart sysDepart = sysDepartService.getById(positionDo.getCompanyId());
            if (sysDepart == null) {
                sysPosition.setSysOrgCode("A01");
            } else {
                sysPosition.setSysOrgCode(sysDepart.getOrgCode());
            }
            sysPositions.add(sysPosition);
        }
        sysPositionService.saveBatch(sysPositions);
    }

    /**
     * 导入用户
     */
    @Test
    public void importUsers() throws ParseException {
        // 在岗，实习生
        List<String> notDelete = Arrays.asList("01", "05");
        File file = new File(USER_FILE_PATH);
        List<ImportUserDo> userDoList = ExcelImportUtil.importExcel(file, ImportUserDo.class, new ImportParams());
        List<SysUser> sysUsers = new ArrayList<>();
        List<SysUserDepart> sysUserDeparts = new ArrayList<>();
        for (ImportUserDo userDo : userDoList) {
            if (StringUtils.isEmpty(userDo.getId())) {
                continue;
            }
            SysUser sysUser = new SysUser();
            sysUser.setId(userDo.getId());
            sysUser.setBirthday(StringUtils.isEmpty(userDo.getBirthday()) ? null : DateUtils.parseDate(userDo.getBirthday(), "yyyyMMdd"));
            sysUser.setPost(userDo.getPost());
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            sysUser.setPassword(PasswordUtil.encrypt(userDo.getId(), userDo.getId(), salt));
            sysUser.setRealname(userDo.getName());
            sysUser.setSex(StringUtils.isEmpty(userDo.getSex()) ? 0 : Integer.parseInt(userDo.getSex()));
            sysUser.setRelTenantIds("1");
            sysUser.setUsername(userDo.getId());
            sysUser.setWorkNo(userDo.getId());
            sysUser.setUserIdentity(1);
            sysUser.setActivitiSync(0);
            if (notDelete.contains(userDo.getStatus())) {
                sysUser.setDelFlag(0);//未删除
                sysUser.setStatus(1); // 正常
                // 添加部门关联
                if (StringUtils.hasText(userDo.getOrgId())) {
                    sysUserDeparts.add(new SysUserDepart(userDo.getId(), userDo.getOrgId()));
                }
            } else {
                sysUser.setDelFlag(1);//已删除
                sysUser.setStatus(2);// 冻结
            }
            sysUsers.add(sysUser);
        }
        sysUserService.saveBatch(sysUsers);
        sysUserDepartService.saveBatch(sysUserDeparts);
    }

    /**
     * 导入公司所有数据
     */
    @Test
    public void importCompany() {
        ImportParams importParams = new ImportParams();
        // 企业数据
        File file = new File(COMPANY_FILE_PATH);
        Set<ImportCompanyDo> companyDoList = new HashSet<>(ExcelImportUtil.importExcel(file, ImportCompanyDo.class, importParams));//存在重复id
        List<Company> companyList = new ArrayList<>();

        // 企业地址
        File file1 = new File(COMPANY_ADDRESS_FILE_PATH);
        List<ImportAddressDo> addressDos = ExcelImportUtil.importExcel(file1, ImportAddressDo.class, importParams);

        for (ImportCompanyDo companyDo : companyDoList) {
            if (StringUtils.isEmpty(companyDo.getId())) {
                continue;
            }
            Company company = new Company();
            company.setId(companyDo.getId());
            company.setCode(companyDo.getId());
            company.setSysOrgCode("A01");
            company.setGroups(companyDo.getGroup());
            company.setContext(companyDo.getContext());
            company.setAttr(companyDo.getAttr()); // 属性
            company.setLegal(companyDo.getLeader());
            company.setNameCn(companyDo.getName1());
            company.setBlack("X".equals(companyDo.getBlack()) ? 1 : 0); //黑名单
            company.setType(companyDo.getRole()); // 业务角色
            company.setCreditCode(companyDo.getCreditCode());// 信用代码
            company.setLiveStatus(1); //存续状态
            company.setStatus(3); // 状态
            //地址信息
            List<ImportAddressDo> filter = addressDos.stream().filter(m -> companyDo.getId().equals(m.getCompanyId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filter)) {
                ImportAddressDo addressDo = filter.get(0);
                company.setAddress(addressDo.getAddress());
                company.setFax(addressDo.getFax());
                company.setCountry(addressDo.getCountry());
                company.setCity(addressDo.getCity());
                company.setLink(addressDo.getPhone());
                company.setContactPhone(addressDo.getMobile());
                company.setEmail(addressDo.getEmail());
                company.setPostCode(addressDo.getPostCode());
            }
            companyList.add(company);
        }
        companyService.saveBatch(companyList);

        // 企业银行
        File file2 = new File(COMPANY_BANK_FILE_PATH);
        List<ImportBankDo> bankDos = ExcelImportUtil.importExcel(file2, ImportBankDo.class, importParams);
        List<CompanyBank> companyBanks = new ArrayList<>();
        for (ImportBankDo bankDo : bankDos) {
            if (StringUtils.isEmpty(bankDo.getCompanyId())) {
                continue;
            }
            CompanyBank companyBank = new CompanyBank();
            companyBank.setCompanyId(bankDo.getCompanyId());
            companyBank.setBank(bankDo.getBank());
            companyBank.setBankNo(bankDo.getBankNo());
            companyBank.setBankName(bankDo.getBankName());
            companyBank.setBankCode(bankDo.getBankCode());
            companyBank.setSysOrgCode("A01");
            companyBank.setSettleWay("1");
            companyBanks.add(companyBank);
        }
        companyBankService.saveBatch(companyBanks);
    }

    /**
     * 导入公司地址
     */
    @Test
    public void importAddress() {
        File file = new File(COMPANY_ADDRESS_FILE_PATH);
        // 企业地址
        List<ImportAddressDo> addressDos = ExcelImportUtil.importExcel(file, ImportAddressDo.class, new ImportParams());
        for (ImportAddressDo addressDo : addressDos) {
            if (StringUtils.isEmpty(addressDo.getCompanyId())) {
                continue;
            }
            Company company = companyService.getById(addressDo.getCompanyId());
            if (company == null) {
                continue;
            }
            company.setAddress(addressDo.getAddress());
            company.setFax(addressDo.getFax());
            company.setCountry(addressDo.getCountry());
            company.setCity(addressDo.getCity());
            company.setLink(addressDo.getPhone());
            company.setContactPhone(addressDo.getMobile());
            company.setEmail(addressDo.getEmail());
            company.setPostCode(addressDo.getPostCode());
            companyService.saveOrUpdate(company);
        }
    }

    /**
     * 导入公司银行
     */
    @Test
    public void importBanks() {
        File file = new File(COMPANY_BANK_FILE_PATH);
        // 企业银行
        List<ImportBankDo> bankDos = ExcelImportUtil.importExcel(file, ImportBankDo.class, new ImportParams());
        List<CompanyBank> companyBanks = new ArrayList<>();
        for (ImportBankDo bankDo : bankDos) {
            if (StringUtils.isEmpty(bankDo.getCompanyId())) {
                continue;
            }
            CompanyBank companyBank = new CompanyBank();
            companyBank.setCompanyId(bankDo.getCompanyId());
            companyBank.setBank(bankDo.getBank());
            companyBank.setBankNo(bankDo.getBankNo());
            companyBank.setBankName(bankDo.getBankName());
            companyBank.setBankCode(bankDo.getBankCode());
            companyBank.setSysOrgCode("A01");
            companyBank.setSettleWay("1");
            companyBanks.add(companyBank);
        }
        companyBankService.saveBatch(companyBanks);
    }

    /**
     * 导入物料信息
     */
    @Test
    public void importMateriel() {
        File file = new File(MATERIEL_FILE_PATH);
        List<Materiel> materielList = ExcelImportUtil.importExcel(file, Materiel.class, new ImportParams());
        materielList = materielList.stream().filter(f -> !"X".equals(f.getDelFlag()) || StringUtils.hasText(f.getCode())).collect(Collectors.toList());
        materielList.forEach(m -> m.setSysOrgCode("A01"));
        materielService.saveBatch(materielList);
    }
}
