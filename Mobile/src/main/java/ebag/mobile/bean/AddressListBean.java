package ebag.mobile.bean;

/**
 * Created by fansan on 2018/3/14.
 */

public class AddressListBean {

    /**
     * uid : null
     * id : 090ebed4197b4abe898b223ddf4ffab0
     * address : 1
     * phone : 1
     * consignee : 1
     * type : 0
     */

    private String uid;
    private String id;
    private String address;
    private String phone;
    private String consignee;
    private String type;
    private String preAddress;

    public String getPreAddress() {
        return preAddress;
    }

    public void setPreAddress(String preAddress) {
        this.preAddress = preAddress;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
