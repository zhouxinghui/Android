package ebag.hd.widget.drawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ebag.hd.R;


/**
 * Created by unicho on 2017/11/23.
 */

public class TitleBar extends RelativeLayout {

    private String title;   //标题文字
    private String rightText;    //右侧按钮文字
    private float rightTextSize; //右侧文字大小
    private int rightTextColor;   //标题文字颜色
    private int titleTextColor;   //标题文字颜色
    private float titleTextSize;    //标题文字大小
    private ImageView backView; //左侧返回按钮控件
    private View rightView;
    private TextView titleTv;   //标题文本控件
    private LayoutParams leftParams;  //左侧控件布局参数
    private LayoutParams rightParams; //右侧控件布局参数
    private LayoutParams titleParams; //标题文本布局参数
    public OnTitleBarClickListener listener;
    private boolean showRightImage;
    private boolean needBottomLine;
    private boolean toMainTab;

    private Drawable leftBackground;
    private Drawable titleBackground;
    private Drawable rightBackground;
    private Drawable leftImage;
    private Drawable rightImage;
    private Context mContext;
    private boolean showRightText;
    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(final Context context, AttributeSet attrs) {
        mContext = context;
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Title_bar);
        titleBackground = typedArray.getDrawable(R.styleable.Title_bar_titleBackground);
        leftBackground = typedArray.getDrawable(R.styleable.Title_bar_leftBackground);

        rightBackground = typedArray.getDrawable(R.styleable.Title_bar_rightBackground);

        showRightImage = typedArray.getBoolean(R.styleable.Title_bar_isRightImage,false);

        showRightText = typedArray.getBoolean(R.styleable.Title_bar_showRightText,false);

        leftImage = typedArray.getDrawable(R.styleable.Title_bar_backImage);

        rightImage = typedArray.getDrawable(R.styleable.Title_bar_rightImage);

        toMainTab = typedArray.getBoolean(R.styleable.Title_bar_toMainTab,false);
        title = typedArray.getString(R.styleable.Title_bar_titleText);
        titleTextSize = typedArray.getDimension(R.styleable.Title_bar_titleTextSize, getResources().getDimension(R.dimen.x24));
        titleTextColor = typedArray.getColor(R.styleable.Title_bar_titleTextColor, Color.parseColor("#ffffff"));

        rightText = typedArray.getString(R.styleable.Title_bar_rightText);
        rightTextColor = typedArray.getColor(R.styleable.Title_bar_rightTextColor, Color.parseColor("#ffffff"));
        rightTextSize = typedArray.getDimension(R.styleable.Title_bar_rightTextSize, getResources().getDimension(R.dimen.x20));

        needBottomLine = typedArray.getBoolean(R.styleable.Title_bar_needBottomLine,true);
        typedArray.recycle();

        if(titleBackground == null)
            setBackgroundResource(R.color.colorPrimary);
        else
            setBackground(titleBackground);

        /*设置标题文本*/
        titleTv = new TextView(context);
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleTv.setText(title);
        titleTv.setTextColor(titleTextColor);
        titleTv.setGravity(Gravity.CENTER);
        titleTv.setLines(1);
        titleTv.setEllipsize(TextUtils.TruncateAt.END);
        titleTv.setPadding(titleTv.getPaddingLeft()+ (int) getResources().getDimension(R.dimen.x45),titleTv.getPaddingTop()
                ,titleTv.getPaddingRight()+(int) getResources().getDimension(R.dimen.x45),titleTv.getPaddingBottom());
        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.x48));
        titleParams.addRule(CENTER_IN_PARENT, TRUE);
        titleParams.addRule(CENTER_VERTICAL);
        addView(titleTv, titleParams);


        /*设置左侧按钮*/
        backView = new ImageView(context);
        backView.setPadding((int) getResources().getDimension(R.dimen.x18),(int) getResources().getDimension(R.dimen.x11)
                ,(int) getResources().getDimension(R.dimen.x12),(int) getResources().getDimension(R.dimen.x11));
        if(leftImage == null){
            backView.setImageResource(R.drawable.icon_back);
        }else{
            backView.setImageDrawable(leftImage);
        }

        if(leftBackground != null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                backView.setBackground(leftBackground);
            }else{
                backView.setBackgroundDrawable(leftBackground);
            }
        }else{
            backView.setBackgroundResource(R.drawable.bac_transparent_selector);
        }

        leftParams = new LayoutParams((int) getResources().getDimension(R.dimen.x45), (int) getResources().getDimension(R.dimen.x48));
        leftParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        addView(backView, leftParams);

        if(showRightImage || toMainTab){
            rightView = new ImageView(context);
            rightView.setPadding((int) getResources().getDimension(R.dimen.x13),(int) getResources().getDimension(R.dimen.x11)
                    ,(int) getResources().getDimension(R.dimen.x13),(int) getResources().getDimension(R.dimen.x11));
            if(toMainTab){
                ((ImageView)rightView).setImageResource(R.drawable.invite_register_tip);

            }else{
                ((ImageView)rightView).setImageDrawable(rightImage);
            }
            if(rightBackground != null){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    rightView.setBackground(rightBackground);
                }else{
                    rightView.setBackgroundDrawable(rightBackground);
                }
            }else{
                rightView.setBackgroundResource(R.drawable.bac_transparent_selector);
            }
            addRight(false);
        }else if(showRightText){
            rightView = new TextView(context);
            rightView.setPadding((int) getResources().getDimension(R.dimen.x13),0
                    ,(int) getResources().getDimension(R.dimen.x13),0);
            ((TextView)rightView).setGravity(Gravity.CENTER_VERTICAL);
            ((TextView)rightView).setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            ((TextView)rightView).setText(rightText);
            ((TextView)rightView).setTextColor(rightTextColor);
            addRight(true);
        }


        if(needBottomLine){
            View view = new View(context);
            view.setBackgroundColor(Color.parseColor("#e1e2e3"));
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dip2px(context,0.5f));
            layoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            addView(view, layoutParams);
        }

        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener == null){
                    if(context instanceof Activity)
                        ((Activity)context).finish();
                }else{
                    listener.leftClick();
                }
            }
        });
//        if(rightView!=null){
//            rightView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(toMainTab){
//                        context.startActivity(new Intent(context, MainActivity.class));
//                    }else {
//                        if(listener != null){
//                            listener.rightClick();
//                        }
//                    }
//                }
//            });
//        }
    }
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void setRightText(String str,OnClickListener onClickListener){
        if(rightView == null){
            rightText = str;
            rightView = new TextView(mContext);
            ((TextView)rightView).setGravity(Gravity.CENTER_VERTICAL);
            ((TextView)rightView).setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            ((TextView)rightView).setText(rightText);
            ((TextView)rightView).setTextColor(rightTextColor);
            rightView.setOnClickListener(onClickListener);
            addRight(true);
        }
    }

    private void addRight(boolean isText){
        if(isText)
            rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.x48));
        else
            rightParams = new LayoutParams((int) getResources().getDimension(R.dimen.x45), (int) getResources().getDimension(R.dimen.x48));
        rightParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            rightView.setBackground(rightBackground);
        }else{
            rightView.setBackgroundDrawable(rightBackground);
        }
        addView(rightView, rightParams);
    }

    public void hiddenTitleLeftButton(){
        if(backView!=null){
            backView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, (int) getResources().getDimension(R.dimen.x48));
    }

    /**
     * 定义按钮点击接口，实现回调机制，通过映射的接口对象调用接口中的方法
     * 而不用去考虑如何实现，具体实现由调用者去创建
     */
    public interface OnTitleBarClickListener{
        void leftClick();   //左侧按钮点击事件
        void rightClick();  //右侧按钮点击事件
    }

    /**
     * 通过接口来获得回调者对接口的实现
     * @param listener
     */
    public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
        this.listener = listener;
    }

    public void setTitle(String str){
        titleTv.setText(str);
    }
    public void hideAllButtons(boolean hide){
        if (hide){
            if(backView!=null){
                backView.setVisibility(GONE);
            }
            if(rightView!=null){
                rightView.setVisibility(GONE);
            }
        }else {
            if(backView!=null){
                backView.setVisibility(VISIBLE);
            }
            if(rightView!=null){
                rightView.setVisibility(VISIBLE);
            }
        }
    }
    /**
     * 设置左侧按钮是否可见
     * @param flag  是否可见
     */
    public void setLeftBtnVisable(boolean flag){
        if (flag){
            backView.setVisibility(VISIBLE);
        }else {
            backView.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧按钮是否可见
     * @param flag 是否可见
     */
    public void setRightBtnVisable(boolean flag){
        if (flag){
            rightView.setVisibility(VISIBLE);
        }else {
            rightView.setVisibility(GONE);
        }
    }

    public void setRightTextColor(int color){
        if(rightView instanceof TextView){
            ((TextView)rightView).setTextColor(color);
        }
    }


}