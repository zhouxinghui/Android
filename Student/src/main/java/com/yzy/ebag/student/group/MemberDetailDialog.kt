package com.yzy.ebag.student.group

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.GroupUserBean
import ebag.core.base.BaseFragmentDialog
import ebag.core.util.DateUtil
import ebag.core.util.StringUtils
import ebag.core.util.loadHead
import kotlinx.android.synthetic.main.dialog_member_detail.*

/**
 * Created by unicho on 2018/3/6.
 */
class MemberDetailDialog: BaseFragmentDialog() {

    private var isDataUpdate = false
    private var groupUserBean: GroupUserBean? = null

    companion object {
        fun newInstance(): MemberDetailDialog{
            return MemberDetailDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun getBundle(bundle: Bundle?) {
    }


    fun updateData(groupUserBean: GroupUserBean?){
        this.groupUserBean = groupUserBean
        this.isDataUpdate = true
    }

    override fun onResume() {
        super.onResume()
        if(this.isDataUpdate){
            this.isDataUpdate = false
            showData()
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.dialog_member_detail
    }

    override fun initView(view: View) {
    }

    fun showData(){
        imgHead.loadHead(groupUserBean?.headUrl)
        tvName.text = groupUserBean?.name

        // 性别
        val sex = if(StringUtils.isEmpty(groupUserBean?.sex)) {
            ""
        }else {
            when(groupUserBean?.sex){
                "1" -> "男  "
                "2" -> "女  "
                else -> ""
            }
        }

        // 年龄
        val age = if(StringUtils.isEmpty(groupUserBean?.birthday) || groupUserBean!!.birthday.length < 4) {
            ""
        }else{
            "${DateUtil.getCurrentYear().toInt() - groupUserBean!!.birthday.substring(0, 4).toInt()}岁  "
        }

        // 城市
        val city = if(StringUtils.isEmpty(groupUserBean?.city)) {
            ""
        }else{
            "${groupUserBean?.city}  "
        }
        // 区县
        val county = if(StringUtils.isEmpty(groupUserBean?.county)) {
            ""
        }else{
            "${groupUserBean?.county}  "
        }

        tvBaseInfo.text = "${sex}${age}${city}${county}".trim()

        tvYsbCode.text = "书  包  号    ${if(StringUtils.isEmpty(groupUserBean?.ysbCode)) "暂无" else groupUserBean?.ysbCode}"
        tvPhone.text = "电        话    ${if(StringUtils.isEmpty(groupUserBean?.phone)) "暂无" else groupUserBean?.phone}"
        tvSchool.text = "所在学校    ${if(StringUtils.isEmpty(groupUserBean?.schoolName)) "暂无" else groupUserBean?.schoolName}"
    }
}