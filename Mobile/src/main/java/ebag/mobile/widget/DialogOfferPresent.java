package ebag.mobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebag.core.util.SerializableUtils;
import ebag.mobile.R;
import ebag.mobile.base.Constants;
import ebag.mobile.bean.GiftBean;
import ebag.mobile.bean.GiftPayBean;
import ebag.mobile.bean.UserEntity;

/**
 * Created by YZY on 2017/7/5.
 */

public class DialogOfferPresent extends Dialog implements View.OnClickListener{
    private TextView num1,num2,num3,num4,num5,tv1,tv2,tv3,tv4,tv5,totalNum;
    private ImageView img2,img3,img4,img5;
    private Button cut1,cut2,cut3,cut4,cut5,add1,add2,add3,add4,add5;
    private String homeworkId;
    private Context mContext;
    /**对话框类型：0 送老师；1 送孩子*/
    private int type = 0;
    public DialogOfferPresent(Context context, int type,String id) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext = context;
        init(mContext);
        this.type = type;
        homeworkId = id;
    }

    private void init(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_offer_teacher_present, null);
        //TODO 初始化控件
        cut1 = (Button) contentView.findViewById(R.id.cut1);
        cut2 = (Button) contentView.findViewById(R.id.cut2);
        cut3 = (Button) contentView.findViewById(R.id.cut3);
        cut4 = (Button) contentView.findViewById(R.id.cut4);
        cut5 = (Button) contentView.findViewById(R.id.cut5);
        cut1.setOnClickListener(this);
        cut2.setOnClickListener(this);
        cut3.setOnClickListener(this);
        cut4.setOnClickListener(this);
        cut5.setOnClickListener(this);
        add1 = (Button) contentView.findViewById(R.id.add1);
        add2 = (Button) contentView.findViewById(R.id.add2);
        add3 = (Button) contentView.findViewById(R.id.add3);
        add4 = (Button) contentView.findViewById(R.id.add4);
        add5 = (Button) contentView.findViewById(R.id.add5);
        add1.setOnClickListener(this);
        add2.setOnClickListener(this);
        add3.setOnClickListener(this);
        add4.setOnClickListener(this);
        add5.setOnClickListener(this);
        num1 = (TextView) contentView.findViewById(R.id.num1);
        num2 = (TextView) contentView.findViewById(R.id.num2);
        num3 = (TextView) contentView.findViewById(R.id.num3);
        num4 = (TextView) contentView.findViewById(R.id.num4);
        num5 = (TextView) contentView.findViewById(R.id.num5);

        tv1 = (TextView) contentView.findViewById(R.id.tv1);
        tv2 = (TextView) contentView.findViewById(R.id.tv2);
        tv3 = (TextView) contentView.findViewById(R.id.tv3);
        tv4 = (TextView) contentView.findViewById(R.id.tv4);
        tv5 = (TextView) contentView.findViewById(R.id.tv5);
        totalNum = (TextView) contentView.findViewById(R.id.total_count_tv);
        img2 = (ImageView) contentView.findViewById(R.id.img2);
        img3 = (ImageView) contentView.findViewById(R.id.img3);
        img4 = (ImageView) contentView.findViewById(R.id.img4);
        img5 = (ImageView) contentView.findViewById(R.id.img5);

        setContentView(contentView);
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //为dialog中的view设置圆角时需要位dialog设置空背景
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);

        contentView.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectPresent();
                clearPresent();
                onOfferSuccessListener.onOfferSuccess(bean);
                dismiss();
            }
        });
    }

    /**
     * @param type 0 送老师；1 送孩子
     */
    public void show(int type) {
        if (type == 1){
            img2.setImageResource(R.drawable.icon_paper_notebook);
            img3.setImageResource(R.drawable.icon_paper_palette);
            img4.setImageResource(R.drawable.icon_paper_piggy_bank);
            img5.setImageResource(R.drawable.icon_paper_medal);
            tv2.setText("笔记本");
            tv3.setText("画板");
            tv4.setText("储蓄罐");
            tv5.setText("奖章");
        }
        super.show();
    }

    private int count1,count2,count3,count4,count5;
    int payCount1,payCount2,payCount3,payCount4,payCount5;
    private int totalPayCount;
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.cut1) {
            count1--;
            if (count1 == 0) {
                cut1.setEnabled(false);
            }
            if (count1 == 98) {
                add1.setEnabled(true);
            }
            num1.setText(String.valueOf(count1));
            payCount1 = 10 * count1;

        } else if (i == R.id.cut2) {
            count2--;
            if (count2 == 0) {
                cut2.setEnabled(false);
            }
            if (count2 == 98) {
                add2.setEnabled(true);
            }
            payCount2 = 50 * count2;
            num2.setText(String.valueOf(count2));

        } else if (i == R.id.cut3) {
            count3--;
            if (count3 == 0) {
                cut3.setEnabled(false);
            }
            if (count3 == 98) {
                add3.setEnabled(true);
            }
            payCount3 = 100 * count3;
            num3.setText(String.valueOf(count3));

        } else if (i == R.id.cut4) {
            count4--;
            if (count4 == 0) {
                cut4.setEnabled(false);
            }
            if (count4 == 98) {
                add4.setEnabled(true);
            }
            payCount4 = 150 * count4;
            num4.setText(String.valueOf(count4));

        } else if (i == R.id.cut5) {
            count5--;
            if (count5 == 0) {
                cut5.setEnabled(false);
            }
            if (count5 == 98) {
                add5.setEnabled(true);
            }
            payCount5 = 200 * count5;
            num5.setText(String.valueOf(count5));

        } else if (i == R.id.add1) {
            count1++;
            if (count1 == 99) {
                add1.setEnabled(false);
            }
            if (count1 == 1) {
                cut1.setEnabled(true);
            }
            payCount1 = 10 * count1;
            num1.setText(String.valueOf(count1));

        } else if (i == R.id.add2) {
            count2++;
            if (count2 == 99) {
                add2.setEnabled(false);
            }
            if (count2 == 1) {
                cut2.setEnabled(true);
            }
            payCount2 = 50 * count2;
            num2.setText(String.valueOf(count2));

        } else if (i == R.id.add3) {
            count3++;
            if (count3 == 99) {
                add3.setEnabled(false);
            }
            if (count3 == 1) {
                cut3.setEnabled(true);
            }
            payCount3 = 100 * count3;
            num3.setText(String.valueOf(count3));

        } else if (i == R.id.add4) {
            count4++;
            if (count4 == 99) {
                add4.setEnabled(false);
            }
            if (count4 == 1) {
                cut4.setEnabled(true);
            }
            payCount4 = 150 * count4;
            num4.setText(String.valueOf(count4));

        } else if (i == R.id.add5) {
            count5++;
            if (count5 == 99) {
                add5.setEnabled(false);
            }
            if (count5 == 1) {
                cut5.setEnabled(true);
            }
            payCount5 = 200 * count5;
            num5.setText(String.valueOf(count5));

        }
        totalPayCount = payCount1 + payCount2 + payCount3 + payCount4 + payCount5;
        totalNum.setText(String.valueOf(totalPayCount) + "YB");
    }
    private OnOfferSuccessListener onOfferSuccessListener;
    public void setOnOfferSuccessListener(OnOfferSuccessListener onOfferSuccessListener){
        this.onOfferSuccessListener = onOfferSuccessListener;
    }

    public interface OnOfferSuccessListener{
        void onOfferSuccess(GiftPayBean bean);
    }

    private void clearPresent(){
        count1 = 0;
        count2 = 0;
        count3 = 0;
        count4 = 0;
        count5 = 0;
        payCount1 = 0;
        payCount2 = 0;
        payCount3 = 0;
        payCount4 = 0;
        payCount5 = 0;
        totalPayCount = 0;
        num1.setText("0");
        num2.setText("0");
        num3.setText("0");
        num4.setText("0");
        num5.setText("0");
        totalNum.setText("0YB");
    }

    private List<GiftBean> list;
    private GiftPayBean bean = null;
    private void collectPresent(){
        if (list == null){
            list = new ArrayList<>();
        }
        list.clear();
        if (count1 > 0){
            GiftBean bean = new GiftBean();
            bean.setGiftName("鲜花");
            bean.setGiftNum(count1);
            list.add(bean);
        }
        if (count2 > 0){
            GiftBean bean = new GiftBean();
            bean.setGiftName(type == 0 ? "贺卡" : "笔记本");
            bean.setGiftNum(count2);
            list.add(bean);
        }
        if (count3 > 0){
            GiftBean bean = new GiftBean();
            bean.setGiftName(type == 0 ? "钢笔" : "画板");
            bean.setGiftNum(count3);
            list.add(bean);
        }
        if (count4 > 0){
            GiftBean bean = new GiftBean();
            bean.setGiftName(type == 0 ? "台灯" : "储蓄罐");
            bean.setGiftNum(count4);
            list.add(bean);
        }
        if (count5 > 0){
            GiftBean bean = new GiftBean();
            bean.setGiftName(type == 0 ? "按摩椅" : "奖章");
            bean.setGiftNum(count5);
            list.add(bean);
        }

        String uid;
        if (mContext.getPackageName().contains("parents")){
            uid = ((UserEntity)SerializableUtils.getSerializable(Constants.PARENTS_USER_ENTITY)).getUid();
        }else{
            uid = ((UserEntity)SerializableUtils.getSerializable(Constants.TEACHER_USER_ENTITY)).getUid();
        }
        bean = new GiftPayBean(uid,homeworkId,totalPayCount,list);
    }
}
