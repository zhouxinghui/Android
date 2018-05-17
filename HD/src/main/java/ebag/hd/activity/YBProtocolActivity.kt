package ebag.hd.activity

import android.content.Context
import android.content.Intent
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_ybprotocol.*

class YBProtocolActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_ybprotocol

    override fun initViews() {

        tv.text = """
    一、 云币与人民币比例：
    100Y  :  1RMB。
    二、 老师或学生接收礼物类别
    1、 鲜花；
    2、 贺卡；
    3、 钢笔；
    4、 台灯；
    5、 按摩椅。
    三、 礼物与云币关系
    1、 鲜花 ：1朵鲜花等于10个云币；
    2、 贺卡 ：1个贺卡50个云币；
    3、 钢笔 ：1支钢笔 100 个云币；
    4、 台灯 ：1个台灯150个云币；
    5、 按摩椅 ：1个按摩椅 200个云币。
            """
    }


    companion object {

        fun start(c: Context){
            c.startActivity(Intent(c, YBProtocolActivity::class.java))
        }
    }
}