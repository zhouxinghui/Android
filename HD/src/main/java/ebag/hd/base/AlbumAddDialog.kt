package ebag.hd.base

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.dialog_album_add_student.*

/**
 * Created by unicho on 2018/3/2.
 */
class AlbumAddDialog: BaseFragmentDialog() {

    companion object {
        const val TEACHER_ROLE = 1
        const val STUDENT_ROLE = 2
        fun newInstance(role: Int, classId: String): AlbumAddDialog{
            val dialog = AlbumAddDialog()
            val bundle = Bundle()
            bundle.putInt("role", role)
            bundle.putString("classId", classId)
            dialog.arguments = bundle
            return dialog
        }
    }

    var successListener: (() -> Unit)? = null
    private var isDataUpdate = false

    private var role: Int = STUDENT_ROLE
    private var groupType: String = ""
    private var classId: String = ""
    override fun getBundle(bundle: Bundle?) {
        role = bundle?.getInt("role") ?: STUDENT_ROLE
        classId = bundle?.getString("classId") ?: ""
    }

    override fun getLayoutRes(): Int {
        return if(role == STUDENT_ROLE)
                    R.layout.dialog_album_add_student
                else
                    R.layout.dialog_album_add_teacher
    }

    fun updateGroupType(groupType: String){
        this.groupType = groupType
    }

    override fun initView(view: View) {
        btnClose.setOnClickListener {
            dismiss()
        }

        tvConfirm.isEnabled = false

        etCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tvConfirm.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        tvConfirm.setOnClickListener {
            EBagApi.createAlbum(classId, groupType, etCode.text.toString(), object : RequestCallBack<String>(){

                override fun onStart() {
                    LoadingDialogUtil.showLoading(mContext)
                }

                override fun onSuccess(entity: String?) {
                    T.show(mContext, "创建相册成功")
                    if(successListener != null)
                        successListener!!.invoke()
                    LoadingDialogUtil.closeLoadingDialog()
                }

                override fun onError(exception: Throwable) {
                    exception.handleThrowable(mContext)
                    LoadingDialogUtil.closeLoadingDialog()
                }
            })
        }
    }
}