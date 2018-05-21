package ebag.mobile.base

/**
 * Created by YZY on 2018/4/16.
 */
object Constants {
    const val STUDENT_USER_ENTITY = "student_entity"
    const val TEACHER_USER_ENTITY = "teacher_entity"
    const val PARENTS_USER_ENTITY = "parends_entity"
    const val CHILD_USER_ENTITY = "child_entity"
    const val KEY_TO_MAIN = "key_to_main"
    const val CODE_LOGIN_RESULT = 11

    const val USER_ACCOUNT = "userCount"
    const val USER_PASS_WORD = "userPassWord"
    const val LOGIN_TYPE = "loginType"
    const val THIRD_PARTY_TYPE = "third_party_type"
    const val ROLE_CODE = "role_code"
    const val THIRD_PARTY_TOKEN = "thirdPartyToken"
    const val THIRD_PARTY_UNION_ID = "thirdPartyUnionid"
    const val CLASS_ID: String = "class_id"

    const val ROLE_STUDENT = 1
    const val ROLE_TEACHER = 2
    const val ROLE_PARENT = 3

    const val UPDATE_TEACHER = "teacher"
    const val UPDATE_STUDENT = "student"
    const val UPDATE_PARENT = "parent"

    const val NORMAL_REQUEST = 111
    const val NORMAL_RESULT = 112

    const val CLASS_TYPE = "2" // 班级相册
    const val PERSONAL_TYPE = "3" // 个人相册
    const val HONOR_TYPE = "1" // 班级荣誉

    //职责(duties) 1 班主任 2 科任老师 3 学生 4 班长 5 课代表 6 组长
    const val TEACHER_IN_CHARGE = "1"
    const val TEACHER = "2"
    const val STUDENT = "3"
    const val MONITOR_OF_CLASS = "4"
    const val CLASS_REPRESENTATIVE = "5"
    const val GROUP_LEADER = "6"

    //状态 1 未批改 2 已批改 0 未完成 3 老师评语完成 4 家长签名和评语完成
    const val CORRECT_UNFINISH =  "0"
    const val CORRECT_UNCORRECT =  "1"
    const val CORRECT_CORRECTED =  "2"
    const val CORRECT_TEACHER_REMARKED = "3"
    const val CORRECT_PARENT_REMARKED = "4"

    const val STZY_TYPE = "1"
    const val KHZY_TYPE = "2"
    const val PARENT_TYPE = "3"
    const val KSSJ_TYPE = "4"
    const val ERROR_TOPIC_TYPE = "5"
    const val REPORT_TYPE = "6"

    const val CLASS_NAME: String = "class_name"
    const val GRADE_CODE: String = "grade_code"

    const val PROVINCE_INDEX = "PROVENCE_INDEX"
    const val CITY_INDEX = "CITY_INDEX"
    const val AREA_INDEX = "AREA_INDEX"
    const val BACK_TO_CREATE_CLASS = 101
    const val SELECT_SCHOOL = 102
    const val CURRENT_CITY = "CURRENT_CITY"

    val MOBILENUMBER_REGEX = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"
    const val WXPAY_FLAG = "wxpay_flag"
}