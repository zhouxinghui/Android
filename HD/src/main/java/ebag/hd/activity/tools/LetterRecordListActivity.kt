package ebag.hd.activity.tools

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.base.BaseListActivity
import ebag.hd.bean.LetterDescBean
import ebag.hd.http.EBagApi

/**
 * Created by unicho on 2018/3/13.
 */
class LetterRecordListActivity: BaseListActivity<LetterDescBean, LetterDescBean.NewWordsBean>() {
    private var unitId = ""
    private var createDate : Long = 0
    private var classId = ""
    private lateinit var headLayout: TextView
    private lateinit var adapter: Adapter
    companion object {
        fun jump(unitId: String, createDate: Long, classId:String, context: Context){
            context.startActivity(
                    Intent(context, LetterRecordListActivity::class.java)
                            .putExtra("unitId", unitId)
                            .putExtra("createDate", createDate)
                            .putExtra("classId", classId)
            )
        }
    }
    private val scoreRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@LetterRecordListActivity, "正在上传评分")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(this@LetterRecordListActivity, "上传成功")
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@LetterRecordListActivity)
        }

    }
    override fun loadConfig(intent: Intent) {
        loadMoreEnabled(false)
        setPageTitle("生字默写")
        unitId = intent.getStringExtra("unitId") ?: ""
        createDate = intent.getLongExtra("createDate", 0)
        classId = intent.getStringExtra("classId") ?: ""

        titleBar.setRightText("提交评分", {
            val list = ArrayList<LetterDescBean.NewWordsBean>()
            adapter.data.forEach {
                if (!StringUtils.isEmpty(it.score)){
                    if (it.score.toInt() > 100){
                        T.show(this, "分数范围：0-100")
                        return@setRightText
                    }
                    list.add(it)
                }
            }
            EBagApi.uploadReadScore(list, scoreRequest)
        })
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<LetterDescBean>) {
        EBagApi.getLetterDesc(unitId, createDate, classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: LetterDescBean?): List<LetterDescBean.NewWordsBean>? {
        // 这里需要添加一个题目内容
        headLayout.text = parent?.word
        return parent?.newWords
    }

    override fun getAdapter(): BaseQuickAdapter<LetterDescBean.NewWordsBean, BaseViewHolder> {
        adapter = Adapter()
        headLayout = layoutInflater.inflate(R.layout.item_record_question_tip, null) as TextView
        adapter.addHeaderView(headLayout)
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LetterDescBean.NewWordsBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class Adapter: BaseQuickAdapter<LetterDescBean.NewWordsBean, BaseViewHolder>(R.layout.item_record_answer_tip){
        /*val question_type = 1
        val answer_type = 2
        init {
            addItemType(question_type, R.layout.item_record_question_tip)
            addItemType(answer_type, R.layout.item_record_answer_tip)
        }*/

        override fun convert(helper: BaseViewHolder, item: LetterDescBean.NewWordsBean?) {
            helper.getView<ImageView>(R.id.img_id).loadHead("")
            helper.setText(R.id.nameTv, "${item?.name} ${item?.ysbCode}")
                    .setText(R.id.tvTime, "时间：${DateUtil.getDateTime(item?.createDate ?: 0, "yyyy-MM-dd HH:mm")}")

            val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.isNestedScrollingEnabled = false
            if(recyclerView.adapter == null)
                recyclerView.adapter = ItemAdapter()
            if(recyclerView.layoutManager == null)
                recyclerView.layoutManager = GridLayoutManager(mContext,12)
            recyclerView.postDelayed({
                (recyclerView.adapter as ItemAdapter).setNewData(item?.wordUrl?.split(","))
            },20)

            val scoreEdit = helper.getView<EditText>(R.id.scoreEdit)
            if (scoreEdit.tag is TextWatcher)
                scoreEdit.removeTextChangedListener(scoreEdit.tag as TextWatcher)
            scoreEdit.setText(item?.score)
            val textWatcher = object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    if (!StringUtils.isEmpty(s.toString())){
                        item?.score = s.toString()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
            scoreEdit.addTextChangedListener(textWatcher)
            scoreEdit.tag = textWatcher
        }

    }

    inner class ItemAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_record_question_image){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.getView<ImageView>(R.id.tvChar).loadImage(item)
        }

    }
}