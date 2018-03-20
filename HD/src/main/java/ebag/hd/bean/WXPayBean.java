package ebag.hd.bean;

/**
 * Created by fansan on 2018/3/20.
 */

public class WXPayBean {


    /**
     * appid : wx4adbb68ec1c80484
     * partnerid : 1355549802
     * package : Sign=WXPay
     * noncestr : 7upk8mEKvSKVjDx2
     * timestamp : 1521510624
     * prepayid : wx2018032009502725182b9d7b0991660914
     * sign : C62EF33E7A9F816A7854D26FD4C4FEEC
     */

    private String appid;
    private String partnerid;

    public String getPackage_() {
        return package_;
    }

    public void setPackage_(String package_) {
        this.package_ = package_;
    }

    private String package_;
    private String noncestr;
    private int timestamp;
    private String prepayid;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }


    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
