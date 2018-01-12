package com.yzy.ebag.student

import android.content.Intent
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.meituan.android.walle.ChannelReader
import com.meituan.android.walle.ChannelWriter
import ebag.core.base.BaseActivity
import ebag.core.util.PatchUtils
import ebag.core.xRecyclerView.adapter.ExpandableAdapter
import ebag.core.xRecyclerView.adapter.ExpandableViewHolder
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File

/**
 * Created by unicho on 2017/12/19.
 */
class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initViews() {
        ExpandableTest()

    }

    private fun ExpandableTest(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TestAdapter()
        recyclerView.adapter = adapter

        val data = ArrayList<Group>()
        var group = Group("group 1")
        var children = ArrayList<String>()
        children.add("child 1  1")
        children.add("child 1  2")
        children.add("child 1  3")
        children.add("child 1  4")
        children.add("child 1  5")
        group.setSubItems(children)
        group.isExpanded = false
        data.add(group)

        group = Group("group 2")
        children = ArrayList<String>()
        children.add("child 2  1")
        children.add("child 2  2")
        children.add("child 2  3")
        children.add("child 2  4")
        group.setSubItems(children)
        data.add(group)

        group = Group("group 3")
        children = ArrayList<String>()
        children.add("child 3  1")
        children.add("child 3  2")
        children.add("child 3  3")
        children.add("child 3  4")
        children.add("child 3  5")
        children.add("child 3  6")
        children.add("child 3  7")
        children.add("child 3  8")
        group.setSubItems(children)
        data.add(group)

        adapter.setData(data)
    }




    private fun patchTest(){
        textView.text = "这是新的包 ：：："+PatchUtils.getChannel(this)+BuildConfig.VERSION_CODE

        install.setOnClickListener {  }

        patch.setOnClickListener {

            val root = Environment.getExternalStorageDirectory().absolutePath

            val file = File(root+ File.separator+"test")

            if(!file.exists())
                file.mkdirs()

            //旧的安装包， 从当前的安装包的路径拷贝出来的
            val fileOld = File(file.absolutePath+ File.separator+"app_oldTest.apk")
            //存在旧的安装包 删除
            if(fileOld.exists()){
                fileOld.delete()
            }
            //创建一个空的旧的安装包
            fileOld.createNewFile()

            //已安装的安装包所在的路径
            val appFile = File(PatchUtils.extract(this))

            //获取当前安装包的渠道信息
            val channelInfo = ChannelReader.get(appFile)

            Log.e("channelInfo  :  ","  "+channelInfo.channel)
            //当前包 出去
            if(PatchUtils.copyFile(appFile,fileOld)){
                //清除当前包的渠道信息
                ChannelWriter.remove(fileOld)
                //合成包所在的位置
                val newFile = File(file.absolutePath+ File.separator+"app_new.apk")
                //旧安装包和差分文件合成差分包
                PatchUtils.patch(fileOld.absolutePath,
                        newFile.absolutePath,
                        file.absolutePath+ File.separator+"test.patch")

                if(newFile.exists()){
                    //将合成的包打入之前的渠道信息
                    ChannelWriter.put(newFile,channelInfo.channel,channelInfo.extraInfo)
                    //安装当前包
                    PatchUtils.install(this,newFile.absolutePath)
                }
            }
        }

        jump.setOnClickListener {
            val intent = Intent(this, QuestionTestActivity :: class.java)
            startActivity(intent)
        }
    }
    class Group(val content: String = "group 1"): ExpandableAdapter.IExpandable<String>{
        private var expanded = true
        private var subItems: List<String>? = null

        override fun isExpanded(): Boolean {
            return expanded
        }

        override fun setExpanded(expanded: Boolean) {
            this.expanded = expanded
        }

        override fun getSubItems(): List<String>? {
            return subItems
        }

        fun setSubItems(list: List<String>) {
            this.subItems = list
        }
    }

    class TestAdapter: ExpandableAdapter<Group,String>(R.layout.test_group,R.layout.test_child){

        override fun fillGroupData(setter: ExpandableViewHolder, groupPosition: Int, group: Group?) {
            setter.setText(R.id.tv, group?.content)
        }

        override fun fillChildData(setter: ExpandableViewHolder, groupPosition: Int, childPosition: Int, child: String?) {
            setter.setText(R.id.tv, child)
        }

    }
}