package ebag.mobile.module.clazz.album

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadImage
import ebag.mobile.R
import ebag.mobile.base.Constants
import ebag.mobile.bean.AlbumBean
import ebag.mobile.http.EBagApi

/**
 * Created by unicho on 2018/3/1.
 */
class AlbumFragment: BaseListFragment<ArrayList<AlbumBean>, AlbumBean>() {

    companion object {
        fun newInstance(role: Int, classId: String, groupType: String): AlbumFragment {
            val fragment = AlbumFragment()
            val bundle = Bundle()
            bundle.putInt("role", role)
            bundle.putString("groupType", groupType)
            bundle.putString("classId", classId)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var groupType: String
    lateinit var classId: String
    var role: Int = Constants.ROLE_STUDENT
    override fun getBundle(bundle: Bundle?) {
        groupType = bundle?.getString("groupType") ?: ""
        classId = bundle?.getString("classId") ?: ""
        role = bundle?.getInt("role") ?: Constants.ROLE_STUDENT
    }

    override fun loadConfig() {
//        val list = ArrayList<Album>()
//        list.add(Album("", "创建相册", true))
//        list.add(Album("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123912727.jpg", "花样年华"))
//        list.add(Album("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123912727.jpg", "嘻嘻哈哈"))
//        list.add(Album("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123912727.jpg", "花样年华"))
//        list.add(Album("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123912727.jpg", "嘻嘻哈哈"))
//        list.add(Album("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123912727.jpg", "花样年华"))
//        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<AlbumBean>>) {
        EBagApi.albums(classId, groupType, page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<AlbumBean>?): ArrayList<AlbumBean>? {
        var result = parent
        if(isFirstPage && (role == Constants.ROLE_TEACHER || ((role == Constants.ROLE_STUDENT || role == Constants.ROLE_PARENT) && groupType == Constants.PERSONAL_TYPE))){
            if(result == null){
                result = ArrayList<AlbumBean>()
            }
            result.add(0, AlbumBean(true))
        }

        return result
    }

    override fun getAdapter(): BaseQuickAdapter<AlbumBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<AlbumBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 3)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as Adapter
        if(adapter.getItem(position)?.isAdd == true){
            albumAddDialog.updateGroupType(groupType)
            albumAddDialog.show(fragmentManager, "album_add")
        }else{
            AlbumDetailActivity.jump(this,
                    classId,
                    adapter.getItem(position)?.photoGroupId ?: "",
                    adapter.getItem(position)?.photosName ?: "",
                    groupType,
                    role)
        }
    }

    private val albumAddDialog by lazy {
        val dialog = AlbumAddDialog.newInstance(role, classId)
        dialog.successListener = {
            onRefresh()
            dialog.dismiss()
        }
        dialog
    }

    inner class Adapter: BaseQuickAdapter<AlbumBean, BaseViewHolder>(R.layout.item_fragment_album){
        override fun convert(helper: BaseViewHolder, item: AlbumBean?) {
            if(item?.isAdd == true){
                helper.setText(R.id.text,"创建相册")
                        .getView<ImageView>(R.id.image).setImageResource(R.drawable.add_pic)
            }else{
                val cover = item?.photoMap?.photo?.photoUrl?.split(",")
                helper.setText(R.id.text,item?.photosName)
                        .getView<ImageView>(R.id.image).loadImage(
                            if(cover != null && cover.isNotEmpty())
                                cover[0]
                            else
                                ""
                        )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.NORMAL_REQUEST && resultCode == Constants.NORMAL_RESULT){
            onRefresh()
        }
    }
}