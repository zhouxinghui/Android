package ebag.hd.bean

import java.io.Serializable

/**
 * Created by YZY on 2018/1/19.
 */
class CurrentCityBean: Serializable {
    var province: String? = null
    var city: String? = null
    var county: String? = null
    var provinceName: String? = null
    var cityName: String? = null
    var countyName: String? = null

    var schoolCode: String? = null
    var schoolName: String? = null
}