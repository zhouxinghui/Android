package com.yzy.ebag.student.bean;

/**
 * Created by unicho on 2018/3/16.
 */

public class BaiduOauthBean {

    /**
     * access_token : 24.52cc2771f2a331503022e72d316db842.2592000.1523772919.282335-7984464
     * session_key : 9mzdDoDySfcQvgz6tOCMtysqF+CBa7JwvhB6xLVN2VR2lGVWFpXfsuqgGY25hiojlAOEYSiOwO/2GQQL8KXTenAz0ELx
     * scope : public brain_all_scope wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower bnstest_fasf lpq_开放 cop_helloScope ApsMis_fangdi_permission
     * refresh_token : 25.a9738d9e9117a9dfab16eec5de07ef02.315360000.1836540919.282335-7984464
     * session_secret : df3157d77d9d93f2580a982f7369f117
     * expires_in : 2592000
     */

    private String access_token;
    private String session_key;
    private String scope;
    private String refresh_token;
    private String session_secret;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
