package ebag.hd.dialog

import android.os.Bundle
import android.view.View
import ebag.core.util.loadHead
import ebag.hd.R
import ebag.hd.base.BaseFragmentDialog
import ebag.hd.bean.ClassMemberBean
import kotlinx.android.synthetic.main.dialog_classmateinfo.*

/**
 * Created by fansan on 2018/4/8.
 */

class ClazzmateInfoDIalog:BaseFragmentDialog(){

    private lateinit var datas:ClassMemberBean.StudentsBean

    companion object {
        fun newInstance(): ClazzmateInfoDIalog{
            return ClazzmateInfoDIalog()
        }
    }

    override fun getBundle(bundle: Bundle?) {
            @Suppress("UNCHECKED_CAST")
            datas = bundle?.get("data") as ClassMemberBean.StudentsBean

    }

    override fun getLayoutRes(): Int = R.layout.dialog_classmateinfo

    override fun initView(view: View) {

        clazzmate_iv.loadHead(datas.headUrl)
        clazzmate_name.text = datas.name
        clazzmate_info.text = "${datas.sex} ${datas.birthday} ${datas.county}"
        clazzmate_bagnum.text = "书包号：${datas.ysbCode}"
        clazzmate_phone.text = "电话：${datas.phone}"
        clazzmate_school.text = "学校：${datas.schoolName}"

    }

}