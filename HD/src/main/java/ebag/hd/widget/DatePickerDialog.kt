package ebag.hd.widget

import android.content.Context
import ebag.core.base.BaseDialog
import ebag.core.util.L
import ebag.core.util.T
import ebag.hd.R
import kotlinx.android.synthetic.main.dialog_date_picker.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by YZY on 2018/2/2.
 */
class DatePickerDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_date_picker
    }

    init {
        confirmTv.setOnClickListener {
            val pickDate = "${datePicker.year}-${datePicker.month}-${datePicker.day}"
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val pickMillion = sdf.parse(pickDate).time + 24 * 60 * 60 * 1000
            val currentMillion = System.currentTimeMillis()
            L.e("当前时间：$currentMillion , 选择时间：$pickMillion")
            if(currentMillion > pickMillion){
                T.show(context, "选择日期早于当前日期")
                return@setOnClickListener
            }
            onConfirmClick?.invoke(pickDate)
            dismiss()
        }
    }
    var onConfirmClick : ((date: String) -> Unit)? = null
}