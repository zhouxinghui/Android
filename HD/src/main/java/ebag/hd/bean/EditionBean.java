package ebag.hd.bean;

import java.util.List;

/**
 * @author caoyu
 * @date 2018/1/26
 * @description
 */

public class EditionBean {

    private String bookVersion;
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
}
