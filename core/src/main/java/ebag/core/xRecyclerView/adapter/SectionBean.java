package ebag.core.xRecyclerView.adapter;

import java.util.List;

/**
 * Created by unicho on 2018/1/9.
 */

public class SectionBean<T> {
    private List<T> sectionList;

    public List<T> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<T> sectionList) {
        this.sectionList = sectionList;
    }
}
