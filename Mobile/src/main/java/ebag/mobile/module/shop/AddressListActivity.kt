package ebag.mobile.module.shop

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.mobile.R
import ebag.mobile.bean.AddressListBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_address.*

/**
 * Created by fansan on 2018/3/14.
 */
class AddressListActivity : BaseActivity(), AddressContract.View {
    private var mData: MutableList<AddressListBean> = mutableListOf()
    private lateinit var mAdapter: AddressListAdapter
    private val mPresenter: AddressListPersenter by lazy { AddressListPersenter(this, this) }
    private var flag = false

    override fun initViews() {

        mAdapter = AddressListAdapter(this, R.layout.item_address, mData, object : onChecked {
            override fun setDefault(position: Int) {
                //设置为默认收货地址，所有参数都要传
                EBagApi.updateAddress(mData[position].id, mData[position].consignee, mData[position].phone, mData[position].preAddress, mData[position].address, "0", object : RequestCallBack<String>() {
                    override fun onSuccess(entity: String?) {
                        T.show(this@AddressListActivity, "设置成功")
                        mData.indices.forEach { i ->
                            if (i == position) mData[i].type = "0" else mData[i].type = "1"
                        }
                        mAdapter.notifyDataSetChanged()
                    }

                    override fun onError(exception: Throwable) {
                        T.show(this@AddressListActivity, "设置失败")
                    }
                })
            }

        })
        flag = intent.getBooleanExtra("choose", false)
        activity_address_recyclerview.layoutManager = LinearLayoutManager(this)
        activity_address_recyclerview.adapter = mAdapter
        activity_address_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        activity_address_stateview.setOnRetryClickListener {
            mPresenter.start()
        }
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.item_address_edit -> edit(position)
                R.id.item_address_delete -> {
                    AlertDialog.Builder(this).setMessage("确定要删除地址吗？").setPositiveButton("确定") { _, _ ->
                        mPresenter.deleteAddress(mData[position].id, position)
                    }.setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                }

            }
        }



        mAdapter.setOnItemClickListener { adapter, view, position ->

            if (flag) {
                val build = StringBuilder()
                build.append(mData[position].consignee + "/")
                build.append(mData[position].phone + "/")
                build.append(mData[position].preAddress + "/")
                build.append(mData[position].address + "/")
                build.append(mData[position].id)
                val intent = Intent()
                intent.putExtra("result", build.toString())
                setResult(666, intent)
                finish()
            }

        }
        activity_address_add_layout.setOnClickListener {
            val intent = Intent(this, EditAddressActivity::class.java)
            startActivityForResult(intent, 98)
        }

        mPresenter.start()
    }

    override fun showLoading() {
        activity_address_stateview.showLoading()
    }

    override fun showError() {
        activity_address_stateview.showError()
    }

    override fun showEmpty() {
        activity_address_stateview.showEmpty()
    }

    override fun showSuccess(data: MutableList<AddressListBean>) {
        if (mData.isNotEmpty()) {
            mData.clear()
        }
        mData.addAll(data)
        mAdapter.notifyDataSetChanged()
        activity_address_stateview.showContent()
    }

    override fun delete(position: Int) {
        mAdapter.remove(position)
        T.show(this, "删除成功")
    }

    override fun edit(position: Int) {
        val intent = Intent(this, EditAddressActivity::class.java)
        intent.putExtra("name", mData[position].consignee)
        intent.putExtra("phone", mData[position].phone)
        intent.putExtra("preAddress", mData[position].preAddress)
        intent.putExtra("address", mData[position].address)
        intent.putExtra("id", mData[position].id)
        startActivityForResult(intent, 100)
    }

    override fun setDefult() {
    }


    override fun getLayoutId(): Int = R.layout.activity_address

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 99) {//添加地址成功
            mPresenter.start()
        }
    }

    interface onChecked {
        fun setDefault(position: Int)
    }
}