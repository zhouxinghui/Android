package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import ebag.core.util.T
import ebag.hd.widget.ListBottomShowDialog
import kotlinx.android.synthetic.main.activity_create_class.*

class CreateClassActivity : BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_create_class
    }

    private val gradeDialog by lazy {
        object : ListBottomShowDialog<String>(this@CreateClassActivity, ArrayList<String>()){
            override fun setText(data: String?): String {
                return data ?: ""
            }
        }
    }
    private val testList by lazy {
        ArrayList<String>()
    }

    override fun initViews() {
        schoolBtn.setOnClickListener(this)
        gradeBtn.setOnClickListener(this)
        courseBtn.setOnClickListener(this)

        testList.add("一年级")
        testList.add("二年级")
        testList.add("三年级")
        testList.add("四年级")
        testList.add("五年级")
        testList.add("六年级")
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.schoolBtn ->{
                startActivity(Intent(this, SelectSchoolActivity::class.java))
            }
            R.id.gradeBtn ->{
                gradeDialog.setOnDialogItemClickListener { dialog, data, position ->
                    gradeName.text = data
                    gradeName.isSelected = true
                }
                gradeDialog.show(testList)
            }
            R.id.courseBtn ->{
                T.show(this, "课程")
            }
        }
    }

}
