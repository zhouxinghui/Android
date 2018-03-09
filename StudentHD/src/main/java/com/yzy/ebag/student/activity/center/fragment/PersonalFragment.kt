package com.yzy.ebag.student.activity.center.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.account.LoginActivity
import ebag.core.base.BaseFragment
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.fragment_center_personal.*

/**
 * Created by caoyu on 2018/1/13.
 */
class PersonalFragment: BaseFragment() {
    companion object {
        fun newInstance() : Fragment {
            val fragment = PersonalFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_center_personal
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun initViews(rootView: View) {
//        showContent()
    }

    override fun onResume() {
        super.onResume()
        showContent()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            showContent()
        }
    }

    private fun showContent(){
        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        if(userEntity != null){
            tvName.text = userEntity.name
            tvId.text = userEntity.ysbCode
            ivAvatar.loadHead(userEntity.headUrl)
            tvGender.text = when(userEntity.sex){
                                "1" -> "男  "
                                "2" -> "女  "
                                else -> ""
                            }
//            tvContact.text = userEntity.
            tvAddress.text = userEntity.address
            tvSchool.text = userEntity.schoolName
//            tvClass.text = userEntity.
        }else{
            startActivity(
                    Intent(mContext, LoginActivity::class.java)
                            .putExtra(Constants.KEY_TO_MAIN,true)
            )
        }
    }
}