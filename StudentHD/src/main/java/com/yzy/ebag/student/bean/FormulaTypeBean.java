package com.yzy.ebag.student.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by unicho on 2018/3/9.
 */

public class FormulaTypeBean implements Parcelable {


    /**
     * id : asdfasfas
     * formulaType : 勾股定理
     * formulaDetailDtos : [{"id":null,"formulaTitle":"xxxx","formulaContent":"阿斯顿发送到asdfasdfasd#R#思考的减肥了深刻的积分凉快圣诞节福利卡","formulaId":null}]
     */

    private String id;
    private String formulaType;
    private List<FormulaBean> formulaDetailDtos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(String formulaType) {
        this.formulaType = formulaType;
    }

    public List<FormulaBean> getFormulaDetailDtos() {
        return formulaDetailDtos;
    }

    public void setFormulaDetailDtos(List<FormulaBean> formulaDetailDtos) {
        this.formulaDetailDtos = formulaDetailDtos;
    }

    public static class FormulaBean implements Parcelable{
        /**
         * id : null
         * formulaTitle : xxxx
         * formulaContent : 阿斯顿发送到asdfasdfasd#R#思考的减肥了深刻的积分凉快圣诞节福利卡
         * formulaId : null
         */


        private String id;
        private String formulaTitle;
        private String formulaContent;
        private String formulaId;
        private String handleContent;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFormulaTitle() {
            return formulaTitle;
        }

        public void setFormulaTitle(String formulaTitle) {
            this.formulaTitle = formulaTitle;
        }

        public String getFormulaContent() {
            return formulaContent;
        }

        public String getHandleContent(){
            if(handleContent == null && formulaContent != null){
                handleContent = formulaContent.replaceAll("#R#", "\n");
            }
            return handleContent;
        }

        public void setFormulaContent(String formulaContent) {
            this.formulaContent = formulaContent;
        }

        public String getFormulaId() {
            return formulaId;
        }

        public void setFormulaId(String formulaId) {
            this.formulaId = formulaId;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.formulaTitle);
            dest.writeString(this.formulaContent);
            dest.writeString(this.formulaId);
        }

        public FormulaBean() {
        }

        protected FormulaBean(Parcel in) {
            this.id = in.readString();
            this.formulaTitle = in.readString();
            this.formulaContent = in.readString();
            this.formulaId = in.readString();
        }

        public static final Creator<FormulaBean> CREATOR = new Creator<FormulaBean>() {
            @Override
            public FormulaBean createFromParcel(Parcel source) {
                return new FormulaBean(source);
            }

            @Override
            public FormulaBean[] newArray(int size) {
                return new FormulaBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.formulaType);
        dest.writeTypedList(this.formulaDetailDtos);
    }

    public FormulaTypeBean() {
    }

    protected FormulaTypeBean(Parcel in) {
        this.id = in.readString();
        this.formulaType = in.readString();
        this.formulaDetailDtos = in.createTypedArrayList(FormulaBean.CREATOR);
    }

    public static final Creator<FormulaTypeBean> CREATOR = new Creator<FormulaTypeBean>() {
        @Override
        public FormulaTypeBean createFromParcel(Parcel source) {
            return new FormulaTypeBean(source);
        }

        @Override
        public FormulaTypeBean[] newArray(int size) {
            return new FormulaTypeBean[size];
        }
    };
}
