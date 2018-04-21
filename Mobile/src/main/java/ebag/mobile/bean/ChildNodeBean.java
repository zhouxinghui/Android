package ebag.mobile.bean;

import java.util.List;

/**
 * Created by YZY on 2018/1/19.
 */
public class ChildNodeBean {
    /**
     * id : 2720
     * parentId : 2699
     * numCode : 302685
     * charCode : LWAQ
     * districtCnName : 荔湾区
     * districtEnName : Liwanqu
     * childNode : []
     */

    private int id;
    private int parentId;
    private int numCode;
    private String charCode;
    private String districtCnName;
    private String districtEnName;
    private List<ChildNodeBean> childNode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getNumCode() {
        return numCode;
    }

    public void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getDistrictCnName() {
        return districtCnName;
    }

    public void setDistrictCnName(String districtCnName) {
        this.districtCnName = districtCnName;
    }

    public String getDistrictEnName() {
        return districtEnName;
    }

    public void setDistrictEnName(String districtEnName) {
        this.districtEnName = districtEnName;
    }

    public List<ChildNodeBean> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<ChildNodeBean> childNode) {
        this.childNode = childNode;
    }
}
