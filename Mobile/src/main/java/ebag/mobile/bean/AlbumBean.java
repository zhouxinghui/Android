package ebag.mobile.bean;

/**
 * Created by unicho on 2018/3/1.
 */

public class AlbumBean {


    /**
     * photoGroupId : 7b93cc259504467ea69e79a61301fd1a
     * photosName : test
     * photoMap : {"photoTotal":2,"photo":{"id":"57cc714d4219432ca9f62da58175534e","photoUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302","createDate":1519899592000}}
     */

    public AlbumBean(boolean isAdd){
        this.isAdd = isAdd;
    }

    /**
     * 需有默认的构造函数， 用以json解析框架
     */
    public AlbumBean(){}

    private boolean isAdd = false;
    private String photoGroupId;
    private String photosName;
    private PhotoMapBean photoMap;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(String photoGroupId) {
        this.photoGroupId = photoGroupId;
    }

    public String getPhotosName() {
        return photosName;
    }

    public void setPhotosName(String photosName) {
        this.photosName = photosName;
    }

    public PhotoMapBean getPhotoMap() {
        return photoMap;
    }

    public void setPhotoMap(PhotoMapBean photoMap) {
        this.photoMap = photoMap;
    }

    public static class PhotoMapBean {
        /**
         * photoTotal : 2
         * photo : {"id":"57cc714d4219432ca9f62da58175534e","photoUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302","createDate":1519899592000}
         */

        private int photoTotal;
        private PhotoBean photo;

        public int getPhotoTotal() {
            return photoTotal;
        }

        public void setPhotoTotal(int photoTotal) {
            this.photoTotal = photoTotal;
        }

        public PhotoBean getPhoto() {
            return photo;
        }

        public void setPhoto(PhotoBean photo) {
            this.photo = photo;
        }

        public static class PhotoBean {
            /**
             * id : 57cc714d4219432ca9f62da58175534e
             * photoUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302
             * createDate : 1519899592000
             */

            private String id;
            private String photoUrl;
            private long createDate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }
        }
    }
}
