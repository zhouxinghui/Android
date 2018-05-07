package ebag.mobile.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by fansan on 2018/3/23.
 */

public class CitysBean implements IPickerViewData {


    /**
     * name : 北京市
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","平谷区","怀柔区","密云县","延庆县"]}]
     */

    public String         name;
    public Integer        id;
    public List<AreaBean> city;

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class AreaBean {
        /**
         * name : 北京市
         * area : ["东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","平谷区","怀柔区","密云县","延庆县"]
         */

        public Integer id;
        public String       name;
        public List<SecondBean> district;

        public static class SecondBean {
            public Integer id;
            public String  name;
        }
    }
}
