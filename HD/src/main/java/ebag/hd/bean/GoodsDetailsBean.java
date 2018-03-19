package ebag.hd.bean;

import java.util.List;

/**
 * Created by fansan on 2018/3/16.
 */

public class GoodsDetailsBean {
    /**
     * id : 1
     * shoppingName : 昂达平板
     * price : 2000
     * discountPrice : 1500
     * ysbMoney : 10000
     * freight : 10
     * saleVolume : null
     * repertory : 0
     * shipAdress : null
     * categoryName : null
     * shopUrl : null
     * productParametersVo : {"resultGruopVOS":[{"groupName":"储存","productParameterVos":[{"groupName":"储存","parameterName":"储存容量","parameterValue":"128G"},{"groupName":"储存","parameterName":"内存容量","parameterValue":"128G"}]},{"groupName":"显示","productParameterVos":[{"groupName":"显示","parameterName":"屏幕尺寸","parameterValue":"8.4英寸"},{"groupName":"显示","parameterName":"分辨率","parameterValue":"1980*1000"}]},{"groupName":"基本参数","productParameterVos":[]}]}
     * productCommentVo : {"resultComentVos":[{"uid":null,"userName":null,"createDate":null,"comment":"太好啦","photoUrl":"http:www.baidu.com","imgUrlList":null},{"uid":null,"userName":null,"createDate":null,"comment":"这是我迄今为止用过最好的平板","photoUrl":"adsfadfadf","imgUrlList":null},{"uid":null,"userName":null,"createDate":null,"comment":"很不错","photoUrl":"dfaadfa","imgUrlList":null}]}
     */

    /*id : 1
            * shopCartId : null
            * shoppingName : 昂达平板
         * price : 2000
            * discountPrice : 1500
            * ysbMoney : 10000
            * freight : 10
            * categoryName : null
            * shopUrl : null
            * imgUrls : ["111111111111"]
            * numbers : 0*/

    private int id;
    private String shoppingName;
    private String price;
    private String discountPrice;
    private String ysbMoney;
    private String freight;
    private Object saleVolume;
    private int repertory;
    private Object shipAdress;
    private Object categoryName;
    private Object shopUrl;

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    private List<String> imgUrls;
    private ProductParametersVoBean productParametersVo;
    private ProductCommentVoBean productCommentVo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public Object getSaleVolume() {
        return saleVolume;
    }

    public void setSaleVolume(Object saleVolume) {
        this.saleVolume = saleVolume;
    }

    public int getRepertory() {
        return repertory;
    }

    public void setRepertory(int repertory) {
        this.repertory = repertory;
    }

    public Object getShipAdress() {
        return shipAdress;
    }

    public void setShipAdress(Object shipAdress) {
        this.shipAdress = shipAdress;
    }

    public Object getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public Object getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(Object shopUrl) {
        this.shopUrl = shopUrl;
    }

    public ProductParametersVoBean getProductParametersVo() {
        return productParametersVo;
    }

    public void setProductParametersVo(ProductParametersVoBean productParametersVo) {
        this.productParametersVo = productParametersVo;
    }

    public ProductCommentVoBean getProductCommentVo() {
        return productCommentVo;
    }

    public void setProductCommentVo(ProductCommentVoBean productCommentVo) {
        this.productCommentVo = productCommentVo;
    }

    public static class ProductParametersVoBean {
        private List<ResultGruopVOSBean> resultGruopVOS;

        public List<ResultGruopVOSBean> getResultGruopVOS() {
            return resultGruopVOS;
        }

        public void setResultGruopVOS(List<ResultGruopVOSBean> resultGruopVOS) {
            this.resultGruopVOS = resultGruopVOS;
        }

        public static class ResultGruopVOSBean {
            /**
             * groupName : 储存
             * productParameterVos : [{"groupName":"储存","parameterName":"储存容量","parameterValue":"128G"},{"groupName":"储存","parameterName":"内存容量","parameterValue":"128G"}]
             */

            private String groupName;
            private List<ProductParameterVosBean> productParameterVos;

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public List<ProductParameterVosBean>     getProductParameterVos() {
                return productParameterVos;
            }

            public void setProductParameterVos(List<ProductParameterVosBean> productParameterVos) {
                this.productParameterVos = productParameterVos;
            }

            public static class ProductParameterVosBean {
                /**
                 * groupName : 储存
                 * parameterName : 储存容量
                 * parameterValue : 128G
                 */

                private String groupName;
                private String parameterName;
                private String parameterValue;

                public String getGroupName() {
                    return groupName;
                }

                public void setGroupName(String groupName) {
                    this.groupName = groupName;
                }

                public String getParameterName() {
                    return parameterName;
                }

                public void setParameterName(String parameterName) {
                    this.parameterName = parameterName;
                }

                public String getParameterValue() {
                    return parameterValue;
                }

                public void setParameterValue(String parameterValue) {
                    this.parameterValue = parameterValue;
                }
            }
        }
    }

    public static class ProductCommentVoBean {
        private List<ResultComentVosBean> resultComentVos;

        public List<ResultComentVosBean> getResultComentVos() {
            return resultComentVos;
        }

        public void setResultComentVos(List<ResultComentVosBean> resultComentVos) {
            this.resultComentVos = resultComentVos;
        }

        public static class ResultComentVosBean {
            /**
             * uid : null
             * userName : null
             * createDate : null
             * comment : 太好啦
             * photoUrl : http:www.baidu.com
             * imgUrlList : null
             */

            private Object uid;
            private Object userName;
            private Object createDate;
            private String comment;
            private String photoUrl;
            private Object imgUrlList;

            public Object getUid() {
                return uid;
            }

            public void setUid(Object uid) {
                this.uid = uid;
            }

            public Object getUserName() {
                return userName;
            }

            public void setUserName(Object userName) {
                this.userName = userName;
            }

            public Object getCreateDate() {
                return createDate;
            }

            public void setCreateDate(Object createDate) {
                this.createDate = createDate;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }

            public Object getImgUrlList() {
                return imgUrlList;
            }

            public void setImgUrlList(Object imgUrlList) {
                this.imgUrlList = imgUrlList;
            }
        }
    }
}
