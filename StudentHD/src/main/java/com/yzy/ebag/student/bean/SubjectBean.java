package com.yzy.ebag.student.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by caoyu on 2018/1/12.
 */

public class SubjectBean implements MultiItemEntity {

    /**
     * subCode : yw
     * subject : 语文
     * homeWorkInfoVos : [{"id":"2","content":"课后作业","state":"0","remark":"回家好好做题。","endTime":1515661597000,"questionCount":0,"questionComplete":0},{"id":"3","content":"家长推送作业","state":"1","remark":"这个题不错，赶紧做题","endTime":1515661664000,"questionCount":0,"questionComplete":0},{"id":"1","content":"课堂作业","state":"1","remark":"请认真做题","endTime":1515682800000,"questionCount":3,"questionComplete":1}]
     */

    private String subCode;
    private String subject;
    private String homeWorkComplete;
    private List<HomeWorkInfoBean> homeWorkInfoVos;
    private int itemType;

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHomeWorkComplete() {
        return homeWorkComplete;
    }

    public void setHomeWorkComplete(String homeWorkComplete) {
        this.homeWorkComplete = homeWorkComplete;
    }

    public List<HomeWorkInfoBean> getHomeWorkInfoVos() {
        return homeWorkInfoVos;
    }

    public void setHomeWorkInfoVos(List<HomeWorkInfoBean> homeWorkInfoVos) {
        this.homeWorkInfoVos = homeWorkInfoVos;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public static class HomeWorkInfoBean implements Parcelable{
        /**
         * id : 2
         * content : 课后作业
         * state : 0
         * remark : 回家好好做题。
         * endTime : 1515661597000
         * questionCount : 0
         * questionComplete : 0
         */

        private String id;
        private String content;
        private String state;
        private String remark;
        private String endTime;
        private int questionCount;
        private int questionComplete;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getQuestionCount() {
            return questionCount;
        }

        public void setQuestionCount(int questionCount) {
            this.questionCount = questionCount;
        }

        public int getQuestionComplete() {
            return questionComplete;
        }

        public void setQuestionComplete(int questionComplete) {
            this.questionComplete = questionComplete;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.content);
            dest.writeString(this.state);
            dest.writeString(this.remark);
            dest.writeString(this.endTime);
            dest.writeInt(this.questionCount);
            dest.writeInt(this.questionComplete);
        }

        public HomeWorkInfoBean() {
        }

        protected HomeWorkInfoBean(Parcel in) {
            this.id = in.readString();
            this.content = in.readString();
            this.state = in.readString();
            this.remark = in.readString();
            this.endTime = in.readString();
            this.questionCount = in.readInt();
            this.questionComplete = in.readInt();
        }

        public static final Creator<HomeWorkInfoBean> CREATOR = new Creator<HomeWorkInfoBean>() {
            @Override
            public HomeWorkInfoBean createFromParcel(Parcel source) {
                return new HomeWorkInfoBean(source);
            }

            @Override
            public HomeWorkInfoBean[] newArray(int size) {
                return new HomeWorkInfoBean[size];
            }
        };
    }
}
