package ebag.hd.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadImage
import ebag.hd.R
import ebag.hd.bean.AlbumBean
import ebag.hd.http.EBagApi

/**
 * Created by unicho on 2018/3/1.
 */
class AlbumFragment: BaseListFragment<ArrayList<AlbumBean>, AlbumBean>() {

    companion object {
        const val CLASS_TYPE = "2"
        const val PERSONAL_TYPE = "3"
        const val HONOR_TYPE = "1"
        fun newInstance(classId: String, groupType: String): AlbumFragment{
            val fragment = AlbumFragment()
            val bundle = Bundle()
            bundle.putString("groupType", groupType)
            bundle.putString("classId", classId)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var groupType: String
    lateinit var classId: String
    override fun getBundle(bundle: Bundle?) {
        groupType = bundle?.getString("groupType") ?: ""
        classId = bundle?.getString("classId") ?: ""
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
        if(isFirstPage)
            parent?.add(AlbumBean(true))
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<AlbumBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<AlbumBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 6)
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
                                if(cover != null && cover.size > 1)
                                    cover[0]
                                else
                                    ""
                        )
            }
        }
    }
}