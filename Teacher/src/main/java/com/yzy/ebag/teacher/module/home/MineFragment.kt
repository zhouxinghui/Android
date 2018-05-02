package com.yzy.ebag.teacher.module.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.module.personal.PersonalInfoActivity
import com.yzy.ebag.teacher.module.personal.SettingActivity
import ebag.core.base.BaseFragment
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
import ebag.mobile.bean.UserEntity
import ebag.mobile.module.account.YBCenterActivity
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by YZY on 2018/4/16.
 */
class MineFragment: BaseFragment(), View.OnClickListener {
    companion object {
        fun newInstance(): MineFragment{
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_mine
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            modifyInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        modifyInfo()
    }


    override fun initViews(rootView: View) {
        personalInfo.setOnClickListener(this)
        operation.setOnClickListener(this)
        setting.setOnClickListener(this)
        myShop.setOnClickListener(this)
    }

    private fun modifyInfo(){
        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.TEACHER_USER_ENTITY)
        if (userEntity != null) {
            headImg.loadHead(userEntity.headUrl, true, System.currentTimeMillis().toString())
            name.text = userEntity.name
            bagNumber.text = "书包号：${userEntity.ysbCode}"
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.personalInfo ->{
                startActivity(Intent(mContext, PersonalInfoActivity::class.java))
            }
            R.id.operation ->{
//                OperationActivity.jump(mContext)
            }
            R.id.setting ->{
                startActivity(Intent(mContext, SettingActivity::class.java))
            }
            R.id.myShop ->{
                YBCenterActivity.start(mContext)
            }
        }
    }
}