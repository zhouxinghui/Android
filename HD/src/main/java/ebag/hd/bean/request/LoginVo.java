package ebag.hd.bean.request;

/**
 * Created by unicho on 2017/11/13.
 */

public class LoginVo {
    /**
     * loginAccount : 1000725
     * password : ysb123456
     * userType : 1
     * devicesId : 0c3796bea76ce72828e881ffa25d99ae
     * loginType : 1
     * roleCode : student
     */

    private String loginAccount;
    private String password;
    private String userType;
    private String devicesId;
    private int loginType;
    private String roleCode;

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDevicesId() {
        return devicesId;
    }

    public void setDevicesId(String devicesId) {
        this.devicesId = devicesId;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
