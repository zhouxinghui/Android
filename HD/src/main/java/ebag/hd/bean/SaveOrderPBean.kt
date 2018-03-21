package ebag.hd.bean

/**
 * Created by fansan on 2018/3/20.
 */
data class SaveOrderPBean(var addressId:String, var price:String, var allPrice:String, var listBean:ArrayList<ListBean>,var oid:String){

    class ListBean(var ShopId:String, var numbers:String)
}