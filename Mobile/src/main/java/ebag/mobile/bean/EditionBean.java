package ebag.mobile.bean;

import java.util.List;

/**
 * @author caoyu
 * @date 2018/1/26
 * @description
 */

public class EditionBean {

    private String bookVersion;
    private String classId;
    private String className;
    private List<UnitBean> resultBookUnitOrCatalogVos;

    public String getBookVersion() {
        return bookVersion;
    }

    public void setBookVersion(String bookVersion) {
        this.bookVersion = bookVersion;
    }

    public List<UnitBean> getResultBookUnitOrCatalogVos() {
        return resultBookUnitOrCatalogVos;
    }

    public void setResultBookUnitOrCatalogVos(List<UnitBean> resultBookUnitOrCatalogVos) {
        this.resultBookUnitOrCatalogVos = resultBookUnitOrCatalogVos;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
