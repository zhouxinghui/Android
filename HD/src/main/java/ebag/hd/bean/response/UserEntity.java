package ebag.hd.bean.response;

import ebag.core.bean.TokenBean;

/**
 * 用户信息
 * Created by unicho on 2017/11/13.
 */

public class UserEntity extends TokenBean{

    /**
     * id : 1326
     * uid : 1596
     * name : 学生A
     * sex :
     * province : BJ
     * city : DCQU
     * county :
     * address : 北京市东城区
     * birthday :
     * headUrl :
     * depId :
     * schoolName :
     * personalAbout : null
     */

    private String id;
    private String uid;
    private String name;
    private String sex;
    private String province;
    private String city;
    private String county;
    private String address;
    private String birthday;
    private String headUrl;
    private String depId;
    private String schoolName;
    private Object personalAbout;
    private String roleCode;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Object getPersonalAbout() {
        return personalAbout;
    }

    public void setPersonalAbout(Object personalAbout) {
        this.personalAbout = personalAbout;
    }
}
