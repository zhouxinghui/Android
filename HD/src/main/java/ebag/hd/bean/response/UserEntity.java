package ebag.hd.bean.response;

import java.io.Serializable;

/**
 * 用户信息
 * Created by caoyu on 2017/11/13.
 */

public class UserEntity implements Serializable{


    /**
     * id : 1326
     * uid : 1596
     * ysbCode : 书包号
     * name : 学生A
     * sex :
     * province : BJ
     * city : DCQU
     * county :
     * address : 天津市和平区
     * birthday :
     * headUrl :
     * depId : 1
     * schoolName :
     * 下为第三方登录
     * thirdPartyToken
     * thirdPartyUnionid
     */

    private String uid;
    private String ysbCode;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;
    private String roleCode;
    private String token;
    private String thirdPartyToken;
    private String thirdPartyUnionid;

    public String getThirdPartyToken() {
        return thirdPartyToken;
    }

    public void setThirdPartyToken(String thirdPartyToken) {
        this.thirdPartyToken = thirdPartyToken;
    }

    public String getThirdPartyUnionid() {
        return thirdPartyUnionid;
    }

    public void setThirdPartyUnionid(String thirdPartyUnionid) {
        this.thirdPartyUnionid = thirdPartyUnionid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getYsbCode() {
        return ysbCode;
    }

    public void setYsbCode(String ysbCode) {
        this.ysbCode = ysbCode;
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

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid='" + uid + '\'' +
                ", ysbCode='" + ysbCode + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", depId='" + depId + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", token='" + token + '\'' +
                ", thirdPartyToken='" + thirdPartyToken + '\'' +
                ", thirdPartyUnionid='" + thirdPartyUnionid + '\'' +
                '}';
    }
}
