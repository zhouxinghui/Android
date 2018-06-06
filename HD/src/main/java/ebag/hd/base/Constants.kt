package ebag.hd.base

/**
 * 平板lib公用的 常量
 * Created by YZY on 2018/1/6.
 */
object Constants {
    val STUDENT_USER_ENTITY = "student_entity"
    val TEACHER_USER_ENTITY = "teacher_entity"
    val KEY_TO_MAIN = "key_to_main"
    val CODE_LOGIN_RESULT = 11

    //职责(duties) 1 班主任 2 科任老师 3 学生 4 班长 5 课代表 6 组长
    val TEACHER_IN_CHARGE = "1"
    val TEACHER = "2"
    val STUDENT = "3"
    val MONITOR_OF_CLASS = "4"
    val CLASS_REPRESENTATIVE = "5"
    val GROUP_LEADER = "6"

    val PROVINCE_INDEX = "PROVENCE_INDEX"
    val CITY_INDEX = "CITY_INDEX"
    val AREA_INDEX = "AREA_INDEX"
    val BACK_TO_CREATE_CLASS = 101
    val SELECT_SCHOOL = 102
    val CURRENT_CITY = "CURRENT_CITY"

    //状态 1 未批改 2 已批改 0 未完成 3 老师评语完成 4 家长签名和评语完成
    val CORRECT_UNFINISH =  "0"
    val CORRECT_UNCORRECT =  "1"
    val CORRECT_CORRECTED =  "2"
    val CORRECT_TEACHER_REMARKED = "3"
    val CORRECT_PARENT_REMARKED = "4"

    val UPDATE_TEACHER = "teacher"
    val UPDATE_STUDENT = "student"

    val NORMAL_REQUEST = 111
    val NORMAL_RESULT = 112

    val ROLE_STUDENT = 1
    val ROLE_TEACHER = 2
    val ROLE_PARENT = 3

    val CLASS_TYPE = "2" // 班级相册
    val PERSONAL_TYPE = "3" // 个人相册
    val HONOR_TYPE = "1" // 班级荣誉

    val STZY_TYPE = "1"
    val KHZY_TYPE = "2"
    val PARENT_TYPE = "3"
    val KSSJ_TYPE = "4"
    val ERROR_TOPIC_TYPE = "5"
    val REPORT_TYPE = "6"
    const val CLASS_NAME: String = "class_name"
    const val GRADE_CODE: String = "grade_code"
    //18319032590
    val MOBILENUMBER_REGEX = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"

    const val USER_ACCOUNT = "userCount"
    const val USER_PASS_WORD = "userPassWord"
    const val LOGIN_TYPE = "loginType"
    const val THIRD_PARTY_TYPE = "third_party_type"
    const val ROLE_CODE = "role_code"
    const val THIRD_PARTY_TOKEN = "thirdPartyToken"
    const val THIRD_PARTY_UNION_ID = "thirdPartyUnionid"
    const val CLASS_ID: String = "class_id"

    const val IS_X5_INIT = "is_x5_init"
}