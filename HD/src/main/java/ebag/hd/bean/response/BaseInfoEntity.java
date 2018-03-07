package ebag.hd.bean.response;

/**
 * Created by unicho on 2018/3/7.
 */

public class BaseInfoEntity {


    /**
     * id : 4857
     * groupCode : subject
     * groupName : 科目
     * keyCode : yw
     * keyValue : 语文
     * parentCode : null
     */

    private int id;
    private String groupCode;
    private String groupName;
    private String keyCode;
    private String keyValue;
    private String parentCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
