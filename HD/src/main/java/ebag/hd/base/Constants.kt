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
}