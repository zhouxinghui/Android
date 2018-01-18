package ebag.hd.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import ebag.core.base.BaseActivity
import ebag.core.util.loadImage
import ebag.hd.R
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.activity_publish_content.*



/**
 * Created by YZY on 2018/1/17.
 */
abstract class BPublishContentActivity: BaseActivity() {
    private val imgList = ArrayList<String>()
    private val imgAdapter by lazy { MyAdapter(imgList) }
    override fun getLayoutId(): Int {
        return R.layout.activity_publish_content
    }

    override fun initViews() {
        titleBar.setRightText(resources.getString(R.string.commit), {
            commit()
        })
        imgList.add("")
        recyclerView.layoutManager = GridLayoutManager(this, 8)
        recyclerView.adapter = imgAdapter
        numberOfWord.text = getString(R.string.number_of_word, 0)
        contentEdit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                numberOfWord.text = getString(R.string.number_of_word, s?.length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    abstract fun commit()

    inner class MyAdapter(list: ArrayList<String>): BaseQuickAdapter<String, BaseViewHolder>(R.layout.imageview, list){
        override fun convert(helper: BaseViewHolder, item: String) {
            val position = helper.adapterPosition
            val imageView = helper.getView<ImageView>(R.id.imageView)
            if (position == data.size - 1){
                imageView.setImageResource(R.drawable.add_pic)
            }else {
                imageView.loadImage(item)
            }
            helper.itemView.setOnClickListener{
                if (position == data.size - 1){
                    startSelectPicture()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    val list = ArrayList<String>()
                    selectList.forEach { list.add(it.path) }
                    imgAdapter.addData(0, list)
                }
            }
        }
    }
}