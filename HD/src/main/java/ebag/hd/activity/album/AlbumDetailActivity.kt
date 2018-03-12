package ebag.hd.activity.album

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.hd.R
import ebag.hd.activity.album.AlbumDetailActivity.Adapter.Companion.TYPE_STICKY_HEAD
import ebag.hd.base.Constants
import ebag.hd.bean.PhotoBean
import ebag.hd.bean.PhotoRequestBean
import ebag.hd.http.EBagApi
import ebag.hd.widget.stickyItem.FullSpanUtil
import ebag.hd.widget.stickyItem.StickyHeadContainer
import ebag.hd.widget.stickyItem.StickyItemDecoration


/**
 * Created by unicho on 2018/3/2.
 */
class AlbumDetailActivity: BaseListActivity<ArrayList<PhotoBean>, PhotoBean>() {

    companion object {
        fun jump(context: Context, classId: String, photoGroupId: String, groupName: String, groupType: String, role: Int){
            context.startActivity(
                    Intent(context, AlbumDetailActivity::class.java)
                            .putExtra("classId", classId)
                            .putExtra("photoGroupId", photoGroupId)
                            .putExtra("groupName", groupName)
                            .putExtra("groupType", groupType)
                            .putExtra("role", role)
            )
        }
    }

    private val requestBean = PhotoRequestBean()
    private lateinit var cancelBtn: TextView
    private lateinit var chooseBtn: TextView
    private lateinit var shareBtn: TextView
    private lateinit var deleteBtn: TextView
    private lateinit var editBtn: TextView
    private lateinit var uploadBtn: TextView
    private lateinit var photoGroupId: String
    private lateinit var groupType: String
    private lateinit var classId: String
    private var isAllChoose = false
    var role: Int = Constants.ROLE_STUDENT

    override fun loadConfig(intent: Intent) {
        photoGroupId = intent.getStringExtra("photoGroupId") ?: ""
        groupType = intent.getStringExtra("groupType") ?: ""
        classId = intent.getStringExtra("classId") ?: ""
        role = intent.getIntExtra("role", Constants.ROLE_STUDENT)

        requestBean.photoGroupId = photoGroupId
        requestBean.groupType = groupType
        setPageTitle(intent.getStringExtra("groupName") ?: "")
        refreshEnabled(false)
        initStickyView()
        initOptionViews()
    }

    /**
     * // 分组粘性头
     */
    private fun initStickyView(){
        val stickyHeadContainer = StickyHeadContainer(this)
        val view = layoutInflater.inflate(R.layout.item_activity_album_time, null)
        stickyHeadContainer.addView(view,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        stickyHeadContainer.setDataCallback {
            val entity = adapter.getItem(it)
            (view as TextView).text = entity?.createDate
        }
        contentView.addView(
                stickyHeadContainer,
                1,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )
        mRecyclerView.addItemDecoration(StickyItemDecoration(stickyHeadContainer, TYPE_STICKY_HEAD))
    }

    /**
     * 初始化操作选项
     */
    private fun initOptionViews(){
        val optionView: View = layoutInflater.inflate(R.layout.layout_activity_album_detail_option, null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.title_bar_height))
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        rootLayout.addView(optionView, params)

        cancelBtn = optionView.findViewById(R.id.cancelBtn)
        chooseBtn = optionView.findViewById(R.id.chooseBtn)
        shareBtn = optionView.findViewById(R.id.shareBtn)
        deleteBtn = optionView.findViewById(R.id.deleteBtn)
        editBtn = optionView.findViewById(R.id.editBtn)
        uploadBtn = optionView.findViewById(R.id.uploadBtn)

        if(role == Constants.ROLE_STUDENT && groupType != Constants.PERSONAL_TYPE){
            editBtn.visibility = View.GONE
            uploadBtn.visibility = View.GONE
        }

        // 取消
        cancelBtn.setOnClickListener {
            showOptions(false)
        }

        // 编辑
        editBtn.setOnClickListener {
            showOptions(true)
        }

        // 选择
        chooseBtn.setOnClickListener {
            isAllChoose = !isAllChoose
            adapter.data.filter { it.isPhoto }.forEach { it.isSelected = isAllChoose}
            adapter.notifyDataSetChanged()
            if(isAllChoose){
                chooseBtn.text = "全不选"
            }else{
                chooseBtn.text = "全选"
            }
        }

        // 分享
        shareBtn.setOnClickListener {
            val list = adapter.data.filter { it.isPhoto && it.isSelected }
            if(list.isEmpty()){
                T.show(this,"请选择需要分享的图片")
            }else{
                requestBean.clear()
                list.forEach { requestBean.addPhoto(it.id) }
                shareDialog.show()
            }
        }

        // 删除
        deleteBtn.setOnClickListener {
            val list = adapter.data.filter { it.isPhoto && it.isSelected }
            if(list.isEmpty()){
                T.show(this,"请选择需要删除的图片")
            }else{
                requestBean.clear()
                list.forEach { requestBean.addPhoto(it.id) }
                deleteDialog.show()
            }
        }

        // 上传
        uploadBtn.setOnClickListener {
            PhotoUploadActivity.jump(this, classId, photoGroupId, groupType)
        }

        showOptions(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.UPLOAD_REQUEST && resultCode == Constants.UPLOAD_RESULT){
            onRetryClick()
        }
    }

    private val shareRequest by lazy {
        object :RequestCallBack<String>(){

            override fun onStart() {
                LoadingDialogUtil.showLoading(this@AlbumDetailActivity, "照片分享中...")
            }

            override fun onSuccess(entity: String?) {
                T.show(this@AlbumDetailActivity, "分享成功")
                showOptions(false)
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                if(exception is MsgException)
                    exception.handleThrowable(this@AlbumDetailActivity)
                else
                    T.show(this@AlbumDetailActivity, "分享失败，请稍后再试！")
            }

        }
    }

    private val shareDialog by lazy {
        AlertDialog.Builder(this)
                .setMessage("确定分享这些照片到班级空间么？")
                .setNegativeButton("考虑一下", null)
                .setPositiveButton("确定") { dialog, which ->
                    share()
                }.create()
    }
    private fun share(){
        requestBean.groupType = Constants.CLASS_TYPE
        EBagApi.photosShare(requestBean, shareRequest)
    }


    private val deleteRequest by lazy {
        object :RequestCallBack<String>(){

            override fun onStart() {
                LoadingDialogUtil.showLoading(this@AlbumDetailActivity, "照片删除中...")
            }

            override fun onSuccess(entity: String?) {
                T.show(this@AlbumDetailActivity, "删除成功")
                showOptions(false)
                onRefresh()
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                if(exception is MsgException)
                    exception.handleThrowable(this@AlbumDetailActivity)
                else
                    T.show(this@AlbumDetailActivity, "删除失败，请稍后再试！")
            }

        }
    }
    private val deleteDialog by lazy {
        AlertDialog.Builder(this)
                .setMessage("确定删除这些照片么？")
                .setNegativeButton("考虑一下", null)
                .setPositiveButton("确定") { dialog, which ->
                    delete()
                }.create()
    }
    private fun delete(){
        requestBean.groupType = groupType
        EBagApi.photosDelete(requestBean, deleteRequest)
    }

    private fun showOptions(showOption: Boolean){
        if(showOption){
            cancelBtn.visibility = View.VISIBLE
            chooseBtn.visibility = View.VISIBLE
            shareBtn.visibility = if(groupType == Constants.PERSONAL_TYPE) View.VISIBLE else View.GONE
            deleteBtn.visibility = View.VISIBLE
            editBtn.visibility = View.GONE
            uploadBtn.visibility = View.GONE
        }else{
            cancelBtn.visibility = View.GONE
            chooseBtn.visibility = View.GONE
            shareBtn.visibility = View.GONE
            deleteBtn.visibility = View.GONE
            editBtn.visibility = View.VISIBLE
            uploadBtn.visibility = if(groupType != Constants.PERSONAL_TYPE && role == Constants.ROLE_STUDENT) View.GONE else View.VISIBLE

            adapter.data.filter { it.isPhoto }.forEach { it.isSelected = false}
        }
        isAllChoose = false
        chooseBtn.text = "全选"
        adapter.showOption = showOption
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<PhotoBean>>) {
        EBagApi.albumDetail(photoGroupId, groupType, page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<PhotoBean>?): List<PhotoBean>? {
        showOptions(false)
        val list = ArrayList<PhotoBean>()
        parent?.filter { it.photoUrls != null }
            ?.forEach {
                list.add(it)
                list.addAll(it.photoUrls)
            }

        return list
    }
    val adapter = Adapter()
    override fun getAdapter(): BaseQuickAdapter<PhotoBean, BaseViewHolder> {

        adapter.setSpanSizeLookup { gridLayoutManager, position ->
            if(adapter.getItem(position)?.itemType == Adapter.TYPE_STICKY_HEAD){
                gridLayoutManager.spanCount
            }else{
                1
            }
        }
        return adapter
    }


    override fun getLayoutManager(adapter: BaseQuickAdapter<PhotoBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 6)
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        adapter as Adapter
        if(adapter.getItem(position)?.isPhoto == true){ // 点击的是照片
            if(adapter.showOption){
                adapter.updateSelected(position)
            }else{// 跳转照片预览
                val list = ArrayList<String>()
                var index = -1
                (0 until  adapter.itemCount).forEach {
                    if(adapter.getItem(it)?.isPhoto == true) {
                        list.add(adapter.getItem(it)!!.photoUrl)
                        if(it <= position)
                            index++
                    }
                }
                PhotoPreviewActivity.jump(this, list, index)
            }
        }
    }

    /**
     * 照片的 adapter
     */
    class Adapter: BaseMultiItemQuickAdapter<PhotoBean, BaseViewHolder>(null){
        companion object {
            const val TYPE_STICKY_HEAD = 1
            const val TYPE_DATA = 2
        }

        var showOption = false
        set(value) {
            field = value
            if(!showOption){// 取消操作时 数据全部改为未选中状态
                data.forEach { it.isSelected = false }
            }
            notifyDataSetChanged()
        }

        init {
            addItemType(TYPE_STICKY_HEAD, R.layout.item_activity_album_time)
            addItemType(TYPE_DATA, R.layout.item_activity_album_photo)
        }

        fun updateSelected(position: Int){
            data[position].isSelected = !data[position].isSelected
            notifyItemChanged(position)
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD)
        }

        override fun onViewAttachedToWindow(holder: BaseViewHolder) {
            super.onViewAttachedToWindow(holder)
            FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD)
        }

        override fun convert(helper: BaseViewHolder, item: PhotoBean?) {
            when(helper.itemViewType){
                TYPE_DATA -> {
                    helper.setGone(R.id.selectedView, showOption)
                            .getView<ImageView>(R.id.image).loadImage(item?.photoUrl)
                    if(showOption){
                        helper.getView<View>(R.id.selectedView).isSelected = item?.isSelected == true
                    }
                }
                TYPE_STICKY_HEAD -> {
                    helper.setText(R.id.text, item?.createDate)

                }
            }
        }
    }
}