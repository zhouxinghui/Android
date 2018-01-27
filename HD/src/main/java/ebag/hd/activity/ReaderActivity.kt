package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.view.View
import ebag.core.base.BaseActivity
import ebag.core.util.AnimationUtil
import ebag.core.util.T
import ebag.core.util.ZipUtils
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_reader.*

class ReaderActivity : BaseActivity() , View.OnClickListener{
    companion object {
        fun jump(context: Context, fileName: String){
            context.startActivity(Intent(context, ReaderActivity::class.java).putExtra("fileName", fileName))
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    override fun initViews() {
        pageView.setAdapter(PageViewAdapter(this, ZipUtils.getAllImgs(intent.getStringExtra("fileName"))))
        baseFab.registerButton(clearBtn)
        baseFab.registerButton(eraserBtn)
        baseFab.registerButton(penBtn)
        baseFab.setOnClickListener(this)
        clearBtn.setOnClickListener(this)
        eraserBtn.setOnClickListener(this)
        penBtn.setOnClickListener(this)
        pageView.setOnPageTurnListener { count, currentPosition ->
            if(!baseFab.isDraftable)
                AnimationUtil.unExpandFab(baseFab)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.baseFab ->{
                AnimationUtil.slideButtons(this,baseFab)
            }
            R.id.clearBtn ->{
                T.show(this, "清空")
            }
            R.id.eraserBtn ->{
                T.show(this, "橡皮擦")
            }
            R.id.penBtn ->{
                T.show(this, "画笔")
            }
        }
    }
}
