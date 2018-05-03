package com.yzy.ebag.parents.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by unicho on 2018/2/27.
 */

public class ErrorTopicBean implements MultiItemEntity {

    /**
     * subCode : yw
     * subject : 语文
     * errorState : 0
     * errorHomeWorkVos : [{"homeWorkId":"4fd8b82f2a9446a89625db1bba81fa99","createDate":1519639942000,"content":"测试发布作业testtest","errorQuestionNumber":3,"notRevisedQuestionNum":3,"revisedState":"0"}]
     */

    private String subCode;
    private String subject;
    private String errorState;
    private List<ErrorHomeWorkVosBean> errorHomeWorkVos;
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

    public String getErrorState() {
        return errorState;
    }

    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    public List<ErrorHomeWorkVosBean> getErrorHomeWorkVos() {
        return errorHomeWorkVos;
    }

    public void setErrorHomeWorkVos(List<ErrorHomeWorkVosBean> errorHomeWorkVos) {
        this.errorHomeWorkVos = errorHomeWorkVos;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public static class ErrorHomeWorkVosBean implements Parcelable{
        /**
         * homeWorkId : 4fd8b82f2a9446a89625db1bba81fa99
         * createDate : 1519639942000
         * content : 测试发布作业testtest
         * errorQuestionNumber : 3
         * notRevisedQuestionNum : 3
         * revisedState : 0
         */

        private String homeWorkId;
        private long createDate;
        private String content;
        private int errorQuestionNumber;
        private int notRevisedQuestionNum;
        private String revisedState;

        public String getHomeWorkId() {
            return homeWorkId;
        }

        public void setHomeWorkId(String homeWorkId) {
            this.homeWorkId = homeWorkId;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getErrorQuestionNumber() {
            return errorQuestionNumber;
        }

        public void setErrorQuestionNumber(int errorQuestionNumber) {
            this.errorQuestionNumber = errorQuestionNumber;
        }

        public int getNotRevisedQuestionNum() {
            return notRevisedQuestionNum;
        }

        public void setNotRevisedQuestionNum(int notRevisedQuestionNum) {
            this.notRevisedQuestionNum = notRevisedQuestionNum;
        }

        public String getRevisedState() {
            return revisedState;
        }

        public void setRevisedState(String revisedState) {
            this.revisedState = revisedState;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.homeWorkId);
            dest.writeLong(this.createDate);
            dest.writeString(this.content);
            dest.writeInt(this.errorQuestionNumber);
            dest.writeInt(this.notRevisedQuestionNum);
            dest.writeString(this.revisedState);
        }

        public ErrorHomeWorkVosBean() {
        }

        protected ErrorHomeWorkVosBean(Parcel in) {
            this.homeWorkId = in.readString();
            this.createDate = in.readLong();
            this.content = in.readString();
            this.errorQuestionNumber = in.readInt();
            this.notRevisedQuestionNum = in.readInt();
            this.revisedState = in.readString();
        }

        public static final Creator<ErrorHomeWorkVosBean> CREATOR = new Creator<ErrorHomeWorkVosBean>() {
            @Override
            public ErrorHomeWorkVosBean createFromParcel(Parcel source) {
                return new ErrorHomeWorkVosBean(source);
            }

            @Override
            public ErrorHomeWorkVosBean[] newArray(int size) {
                return new ErrorHomeWorkVosBean[size];
            }
        };
    }
}
