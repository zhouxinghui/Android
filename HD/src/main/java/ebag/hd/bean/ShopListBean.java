package ebag.hd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fansan on 2018/3/16.
 */

public class ShopListBean implements Serializable{

    /**
     * total : -1
     * pages : 1
     * list : [{"id":1,"shopCartId":null,"shoppingName":"昂达平板","price":"2000","discountPrice":"1500","ysbMoney":"10000","freight":"10","categoryName":null,"shopUrl":null,"imgUrls":["111111111111"],"numbers":0},{"id":2,"shopCartId":null,"shoppingName":"华为平板","price":"1111","discountPrice":"11","ysbMoney":"11","freight":"1","categoryName":null,"shopUrl":null,"imgUrls":["22222222222"],"numbers":0}]
     */

    private int total;
    private int pages;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * id : 1
         * shopCartId : null
         * shoppingName : 昂达平板
         * price : 2000
         * discountPrice : 1500
         * ysbMoney : 10000
         * freight : 10
         * categoryName : null
         * shopUrl : null
         * imgUrls : ["111111111111"]
         * numbers : 0
         */

        private int id;
        private Object shopCartId;
        private String shoppingName;
        private String price;
        private String discountPrice;
        private String ysbMoney;
        private Object categoryName;
        private String shopUrl;
        private int numbers;
        private List<String> imgUrls;

        public String getFreight() {
            return freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        private String freight;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        private boolean checked;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getShopCartId() {
            return shopCartId;
        }

        public void setShopCartId(Object shopCartId) {
            this.shopCartId = shopCartId;
        }

        public String getShoppingName() {
            return shoppingName;
        }

        public void setShoppingName(String shoppingName) {
            this.shoppingName = shoppingName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getYsbMoney() {
            return ysbMoney;
        }

        public void setYsbMoney(String ysbMoney) {
            this.ysbMoney = ysbMoney;
        }


        public Object getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(Object categoryName) {
            this.categoryName = categoryName;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public int getNumbers() {
            return numbers;
        }

        public void setNumbers(int numbers) {
            this.numbers = numbers;
        }

        public List<String> getImgUrls() {
            return imgUrls;
        }

        public void setImgUrls(List<String> imgUrls) {
            this.imgUrls = imgUrls;
        }
    }
}
