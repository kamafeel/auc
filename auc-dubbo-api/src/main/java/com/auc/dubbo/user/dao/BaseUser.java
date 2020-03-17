package com.auc.dubbo.user.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户基础信息
 * </p>
 *
 * @author GenerateCode
 * @since 2019-10-14
 */
@TableName("t_s_base_user")
public class BaseUser extends Model<BaseUser> {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID号
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 工作流同步(目前暂未使用)
     */
    @TableField("activitiSync")
    private Integer activitiSync;

    /**
     * 用户使用浏览器(暂未使用)
     */
    private String browser;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户真实姓名
     */
    @TableField("realName")
    private String realName;

    /**
     * 用户关联的数据源ID(费控的数据源ID,表明国美,在线等)
     */
    @TableField("sourceId")
    private Integer sourceId;

    @TableField(exist = false)
    private String sourceCode;

    /**
     * 用户头像等大文件信息
     */
    private String signature;

    /**
     * 用户状态(激活,禁用等)
     */
    private Integer status;

    /**
     * 所属角色标示(管理员,普通用户等)
     */
    private String userkey;

    /**
     * 登录用户名
     */
    @TableField("userName")
    private String userName;

    /**
     * 属于部门ID(目前对应t_s_depart表的具体ID,如是岗位,对应岗位ID,如是公司,对应公司ID)
     */
    private String departid;

    /**
     * 删除状态(目前是逻辑删除)
     */
    private Integer deleteFlag;

    /**
     * 人员编码
     */
    @TableField("PersonnelCode")
    private String PersonnelCode;

    /**
     * 直属主管帐号
     */
    @TableField("Supervisor")
    private String Supervisor;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    private LocalDateTime CreateTime;

    /**
     * 更新时间
     */
    @TableField("Updatetime")
    private LocalDateTime Updatetime;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 分部接单员邮编
     */
    private String zipcode;

    /**
     * 用户入职日期
     */
    @TableField("inDate")
    private LocalDate inDate;

    /**
     * 分部接单员邮寄地址
     */
    private String mailingaddress;

    /**
     * 0男 1表示女 未填表示末知与字典表对应的
     */
    private String sex;

    /**
     * 离职时间 YYYY-MM-DD
     */
    @TableField("leaveDate")
    private String leaveDate;

    /**
     * 出生时间YYYY-MM-DD
     */
    @TableField("bornDate")
    private String bornDate;

    /**
     * 公司邮箱
     */
    @TableField("companyMail")
    private String companyMail;

    /**
     * 职务级别
     */
    private String joblevel;

    /**
     * 上次登录时间
     */
    @TableField("lastLoginTime")
    private LocalDateTime lastLoginTime;

    /**
     * 域认证密码
     */
    @TableField("domainPassword")
    private String domainPassword;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司编码
     */
    private String companyCode;

    /**
     * 员工ID(PS人员数据关联ID)
     */
    private String emplid;

    /**
     * 接口表id
     */
    private Integer tempid;

    /**
     * 应用系统账号
     */
    private String appAccount;

    /**
     * 0表示需要每日提示待办，1不需要
     */
    @TableField("noticeTodo")
    private String noticeTodo;

    /**
     * 计算机名
     */
    @TableField("computerName")
    private String computerName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivitiSync() {
        return activitiSync;
    }

    public void setActivitiSync(Integer activitiSync) {
        this.activitiSync = activitiSync;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getPersonnelCode() {
        return PersonnelCode;
    }

    public void setPersonnelCode(String PersonnelCode) {
        this.PersonnelCode = PersonnelCode;
    }

    public String getSupervisor() {
        return Supervisor;
    }

    public void setSupervisor(String Supervisor) {
        this.Supervisor = Supervisor;
    }

    public LocalDateTime getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(LocalDateTime CreateTime) {
        this.CreateTime = CreateTime;
    }

    public LocalDateTime getUpdatetime() {
        return Updatetime;
    }

    public void setUpdatetime(LocalDateTime Updatetime) {
        this.Updatetime = Updatetime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public LocalDate getInDate() {
        return inDate;
    }

    public void setInDate(LocalDate inDate) {
        this.inDate = inDate;
    }

    public String getMailingaddress() {
        return mailingaddress;
    }

    public void setMailingaddress(String mailingaddress) {
        this.mailingaddress = mailingaddress;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getCompanyMail() {
        return companyMail;
    }

    public void setCompanyMail(String companyMail) {
        this.companyMail = companyMail;
    }

    public String getJoblevel() {
        return joblevel;
    }

    public void setJoblevel(String joblevel) {
        this.joblevel = joblevel;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getDomainPassword() {
        return domainPassword;
    }

    public void setDomainPassword(String domainPassword) {
        this.domainPassword = domainPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmplid() {
        return emplid;
    }

    public void setEmplid(String emplid) {
        this.emplid = emplid;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public String getAppAccount() {
        return appAccount;
    }

    public void setAppAccount(String appAccount) {
        this.appAccount = appAccount;
    }

    public String getNoticeTodo() {
        return noticeTodo;
    }

    public void setNoticeTodo(String noticeTodo) {
        this.noticeTodo = noticeTodo;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BaseUser{" +
        "id=" + id +
        ", activitiSync=" + activitiSync +
        ", browser=" + browser +
        ", password=" + password +
        ", realName=" + realName +
        ", sourceId=" + sourceId +
        ", signature=" + signature +
        ", status=" + status +
        ", userkey=" + userkey +
        ", userName=" + userName +
        ", departid=" + departid +
        ", deleteFlag=" + deleteFlag +
        ", PersonnelCode=" + PersonnelCode +
        ", Supervisor=" + Supervisor +
        ", CreateTime=" + CreateTime +
        ", Updatetime=" + Updatetime +
        ", salt=" + salt +
        ", zipcode=" + zipcode +
        ", inDate=" + inDate +
        ", mailingaddress=" + mailingaddress +
        ", sex=" + sex +
        ", leaveDate=" + leaveDate +
        ", bornDate=" + bornDate +
        ", companyMail=" + companyMail +
        ", joblevel=" + joblevel +
        ", lastLoginTime=" + lastLoginTime +
        ", domainPassword=" + domainPassword +
        ", companyName=" + companyName +
        ", companyCode=" + companyCode +
        ", emplid=" + emplid +
        ", tempid=" + tempid +
        ", appAccount=" + appAccount +
        ", noticeTodo=" + noticeTodo +
        ", computerName=" + computerName +
        "}";
    }
}
