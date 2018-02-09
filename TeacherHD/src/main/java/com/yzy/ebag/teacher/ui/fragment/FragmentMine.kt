package com.yzy.ebag.teacher.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.PersonalInfoActivity
import com.yzy.ebag.teacher.ui.activity.SettingActivity
import ebag.core.base.BaseFragment
import ebag.core.util.SerializableUtils
import ebag.core.util.T
import ebag.core.util.loadHead
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentMine : BaseFragment(), View.OnClickListener {
    companion object {
        fun newInstance() : Fragment {
            val fragment = FragmentMine()
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

    override fun initViews(rootView: View) {
        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.TEACHER_USER_ENTITY)
        if (userEntity != null) {
            headImg.loadHead(userEntity.headUrl)
//            subjectTv.text = "英"
            name.text = userEntity.name
            bagNumber.text = getString(R.string.bag_number, userEntity.ysbCode)
        }
        personalInfo.setOnClickListener(this)
        myShop.setOnClickListener(this)
        operation.setOnClickListener(this)
        systemSetting.setOnClickListener(this)
        setting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.personalInfo ->{
                startActivity(Intent(mContext, PersonalInfoActivity::class.java))
            }
            R.id.myShop ->{
                T.show(mContext, "我的商城")
            }
            R.id.operation ->{
                T.show(mContext, "操作指南")
            }
            R.id.systemSetting ->{
                T.show(mContext, "系统设置")
            }
            R.id.setting ->{
                startActivity(Intent(mContext, SettingActivity::class.java))
            }
        }
    }

}