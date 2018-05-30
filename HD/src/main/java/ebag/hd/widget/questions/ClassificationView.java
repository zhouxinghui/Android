package ebag.hd.widget.questions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.StringUtils;
import ebag.core.widget.FlowLayout;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * 分类题
 * Created by YZY on 2017/12/27.
 */

public class ClassificationView extends BaseQuestionView implements  View.OnTouchListener, View.OnClickListener{
    private Context context;
    private QuestionBean questionBean;
    /**
     * 标题
     */
    private List<String> titleList;
    /**
     * 装类别框的父布局
     */
    private FlowLayout parentCategory;
    /**
     * 装待分类元素的布局
     */
    private FlowLayout elementLayout;
    /**
     * 标题
     */
    private String questionHead;
    /**
     * 内容
     */
    private String questionContent;
    /**
     * 正确答案
     */
    private String rightAnswer;
    /**
     * 学生答案
     */
    private String studentAnswer;
    /**
     * 当前题是否可编辑
     */
    private boolean isActive = true;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowManagerParams;
    /**
     * 拖拽X坐标
     */
    private int startX = 0;
    /**
     * 拖拽Y坐标
     */
    private int startY = 0;
    private AlertDialog permissionDialog;
    public ClassificationView(Context context) {
        super(context);
    }

    public ClassificationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassificationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManagerParams = new WindowManager.LayoutParams();
        windowManagerParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
        windowManagerParams.gravity = Gravity.TOP | Gravity.START;
        windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

        setOrientation(VERTICAL);

        parentCategory = new FlowLayout(context);
        LayoutParams parentParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parentParams.topMargin = (int)getResources().getDimension(R.dimen.y15);
        parentCategory.setLayoutParams(parentParams);

        elementLayout = new FlowLayout(context);
        elementLayout.setLayoutParams(parentParams);
//        elementLayout.setPadding(
//                (int)getResources().getDimension(R.dimen.y15),
//                (int)getResources().getDimension(R.dimen.y15),
//                0,0);
        elementLayout.setBackgroundResource(R.drawable.classify_element_layout_bg);
        elementLayout.setMotionEventSplittingEnabled(false);
        addView(parentCategory);
        addView(elementLayout);

        checkPermission();
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        questionHead = questionBean.getTitle();
        questionContent = questionBean.getContent();
        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();

        titleList = new ArrayList<>();
        titleList.add(questionHead);
    }

    @Override
    public void show(boolean active) {
        setTitle(titleList);
        parentCategory.removeAllViews();
        elementLayout.removeAllViews();

        List<String> categoryList = new ArrayList<>();
        List<String> elementList = new ArrayList<>();
        String[] questionSplit = questionContent.split(";");
        String[] elementSplit = questionSplit[0].split(",");
        String[] categorySplit = questionSplit[1].split(",");
        for (String category : categorySplit) {
            categoryList.add(category);
            LinearLayout categoryOut = new LinearLayout(context);
            categoryOut.setOrientation(VERTICAL);
            LayoutParams outParams = new LayoutParams(
                    (int)getResources().getDimension(R.dimen.x300), (int)getResources().getDimension(R.dimen.y200));
            outParams.leftMargin = (int) getResources().getDimension(R.dimen.x40);
            outParams.topMargin = (int) getResources().getDimension(R.dimen.y10);
            categoryOut.setLayoutParams(outParams);
            categoryOut.setBackgroundResource(R.drawable.classify_category_layout_bg);
            TextView categoryTv = new TextView(context);
            categoryTv.setGravity(Gravity.CENTER);
            LayoutParams tvParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            categoryTv.setPadding(
                    0,
                    (int) getResources().getDimension(R.dimen.y10),
                    0,
                    (int) getResources().getDimension(R.dimen.y10));
            categoryTv.setLayoutParams(tvParams);
            categoryTv.setText(category);
            categoryTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_content));
            categoryTv.setTextColor(getResources().getColor(R.color.question_normal));
            categoryOut.addView(categoryTv);

            NestedScrollView scrollView = new NestedScrollView(context);
            FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(scrollParams);
//            scrollView.setNestedScrollingEnabled(false);

            FlowLayout categoryInside = new FlowLayout(context);
            LayoutParams insideParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            categoryInside.setPadding(
//                    (int)getResources().getDimension(R.dimen.y15),
//                    (int)getResources().getDimension(R.dimen.y15),
//                    0,0);
            categoryInside.setLayoutParams(insideParams);
            scrollView.addView(categoryInside);
            categoryOut.addView(scrollView);
            parentCategory.addView(categoryOut);
        }

        Collections.addAll(elementList, elementSplit);

        List<String> answerList = new ArrayList<>();
        String[] answerSplit = StringUtils.INSTANCE.isEmpty(studentAnswer) ? new String[]{} : studentAnswer.split(";");
        for (String allSplit: answerSplit) {
            String[] categoryAndElement = allSplit.split("#R#");
            LinearLayout categoryOut = (LinearLayout) parentCategory.getChildAt(categoryList.indexOf(categoryAndElement[0]));
            NestedScrollView scrollView = (NestedScrollView) categoryOut.getChildAt(1);
            FlowLayout categoryInside = (FlowLayout) scrollView.getChildAt(0);
            if (categoryAndElement.length > 1 && categoryInside != null){
                String[] elementSplit2 = categoryAndElement[1].split(",");
                for (String element : elementSplit2) {
                    answerList.add(element);
                    if (element.startsWith("http"))
                        addChildPic(element, categoryInside, elementList.indexOf(element), false, true);
                    else
                        addChildWorld(element, categoryInside, elementList.indexOf(element), false, true);
                }
            }
        }
        boolean isShowElement;

        for (int i = 0; i < elementSplit.length; i++) {
            isShowElement = !answerList.contains(elementSplit[i]);
            if (elementSplit[i].startsWith("http"))
                addChildPic(elementSplit[i], elementLayout, i, true, isShowElement);
            else
                addChildWorld(elementSplit[i], elementLayout, i, true, isShowElement);
        }

        questionActive(active);
    }

    private void setWrong(){
        String[] split = rightAnswer.split(";");
        for (int i = 0; i < parentCategory.getChildCount(); i++) {
            LinearLayout categoryOut = (LinearLayout) parentCategory.getChildAt(i);
            NestedScrollView scrollView = (NestedScrollView) categoryOut.getChildAt(1);
            FlowLayout categoryInside = (FlowLayout) scrollView.getChildAt(0);
            String[] rightAnswers = null;
            if(split.length == parentCategory.getChildCount()){
                String[] sss = split[i].split("#R#");
                if(sss.length == 2)
                    rightAnswers = sss[1].split(",");
            }
            if (rightAnswers == null || rightAnswers.length == 0)
                return;
            List<String> rightList = Arrays.asList(rightAnswers);
            for (int j = 0; j < categoryInside.getChildCount(); j++) {
                String answer = (String) categoryInside.getChildAt(j).getTag(R.id.tv_id);
                if (!rightList.contains(answer))
                    categoryInside.getChildAt(j).setSelected(true);
            }

        }
    }

    @Override
    public void questionActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean isQuestionActive() {
        return isActive;
    }

    @Override
    public void showResult() {
        setWrong();
        questionActive(false);
    }

    @Override
    public String getAnswer() {
        boolean hasDone = false;
        for (int i = 0; i < elementLayout.getChildCount(); i++) {
            if(elementLayout.getChildAt(i).getVisibility() != View.VISIBLE)
                hasDone = true;
        }
        if(!hasDone){
            return "";
        }else{
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < parentCategory.getChildCount(); i++) {
                LinearLayout categoryOut = (LinearLayout) parentCategory.getChildAt(i);
                NestedScrollView scrollView = (NestedScrollView) categoryOut.getChildAt(1);
                FlowLayout categoryInside = (FlowLayout) scrollView.getChildAt(0);
                TextView categoryText = (TextView) categoryOut.getChildAt(0);
                sb.append(categoryText.getText().toString()).append("#R#");
                if (categoryInside.getChildAt(0) == null)
                    sb.append(";");
                for (int j = 0; j < categoryInside.getChildCount(); j++) {
                    if (j == categoryInside.getChildCount() - 1)
                        sb.append(categoryInside.getChildAt(j).getTag(R.id.tv_id).toString()).append(";");
                    else
                        sb.append(categoryInside.getChildAt(j).getTag(R.id.tv_id).toString()).append(",");
                }
            }
            if (sb.substring(sb.length() - 1, sb.length()).equals("#")){
                return sb.substring(0, sb.length() - 3);
            }else{
                return sb.substring(0, sb.length() - 1);
            }
            /*String[] split_stu = answer.split(";");
            String[] split_right = rightAnswer.split(";");
            for (int i = 0; i < split_stu.length; i++) {
                String[] split_stu_1 = split_stu[i].split("#R#");
                String[] split_right_1 = split_right[i].split("#R#");
                if (split_stu_1.length > 1 && split_right_1.length > 1) {
                    List<String> stuList = Arrays.asList(split_stu_1[1].split(","));
                    List<String> rightList = Arrays.asList(split_right_1[1].split(","));
                    if (stuList.size() != rightList.size() && !stuList.containsAll(rightList))
                        return answer;
                } else {
                    return answer;
                }
            }
            return "";*/
        }

    }

    @Override
    public void reset() {
        isActive = true;
    }

    private void addChildWorld(String text, ViewGroup parent, int index, boolean isTouch, boolean isShowElement){
        TextView elementTv = new TextView(context);
        elementTv.setPadding(
                (int) getResources().getDimension(R.dimen.x10),
                (int) getResources().getDimension(R.dimen.x5),
                (int) getResources().getDimension(R.dimen.x10),
                (int) getResources().getDimension(R.dimen.x5));
        elementTv.setText(text);
        elementTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_content));
        elementTv.setTextColor(getResources().getColor(R.color.white));
        elementTv.setBackgroundResource(R.drawable.classify_element_bg);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) getResources().getDimension(R.dimen.x8);
        params.topMargin = (int) getResources().getDimension(R.dimen.y5);
        params.rightMargin = (int) getResources().getDimension(R.dimen.x8);
        params.bottomMargin = (int) getResources().getDimension(R.dimen.y10);
        elementTv.setLayoutParams(params);
        elementTv.setTag(R.id.tv_id, text);
        elementTv.setTag(R.id.image_id, index);
//        elementTv.setPressed(false);
        if (isTouch){
            elementTv.setOnTouchListener(this);
        }else{
            elementTv.setOnClickListener(this);
        }
        parent.addView(elementTv);
        if (!isShowElement)
            elementTv.setVisibility(View.INVISIBLE);
    }

    private void addChildPic(String url, ViewGroup parent, int index, boolean isTouch, boolean isShowElement){
        ImageView imageView = new ImageView(context);
        imageView.setPadding(
                (int) getResources().getDimension(R.dimen.x5),
                (int) getResources().getDimension(R.dimen.x5),
                (int) getResources().getDimension(R.dimen.x5),
                (int) getResources().getDimension(R.dimen.x5));
        imageView.setBackgroundResource(R.drawable.classify_element_bg);
        LayoutParams params = new LayoutParams(
                (int) getResources().getDimension(R.dimen.x80),
                (int) getResources().getDimension(R.dimen.x80));
        params.leftMargin = (int) getResources().getDimension(R.dimen.x8);
        params.topMargin = (int) getResources().getDimension(R.dimen.y5);
        params.rightMargin = (int) getResources().getDimension(R.dimen.x8);
        params.bottomMargin = (int) getResources().getDimension(R.dimen.y10);
        imageView.setLayoutParams(params);
        SingleImageLoader.getInstance().setImage(url, imageView);
        imageView.setTag(R.id.tv_id, url);
        imageView.setTag(R.id.image_id, index);
        imageView.setPressed(false);
        if (isTouch){
            imageView.setOnTouchListener(this);
        }else{
            imageView.setOnClickListener(this);
        }
        parent.addView(imageView);
        if (!isShowElement)
            imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (!isActive)
            return;
        int index = (int) v.getTag(R.id.image_id);
        FlowLayout parent = (FlowLayout) v.getParent();
        parent.removeView(v);
        elementLayout.getChildAt(index).setVisibility(VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isActive)
            return false;
        if (v instanceof TextView || v instanceof ImageView){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    checkPermission();
                    if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context))
                        return true;
                    v.setPressed(true);
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    elementLayout.requestDisallowInterceptTouchEvent(true);
                    int[] viewLocation = new int[2];
                    v.getLocationOnScreen(viewLocation);
                    windowManagerParams.x = viewLocation[0];
                    windowManagerParams.y = viewLocation[1] - (v.getHeight()/2);
                    v.setVisibility(View.INVISIBLE);
                    createDragImage(v);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int currentX = (int) event.getRawX();
                    int currentY = (int) event.getRawY();
                    int dx = currentX - startX;
                    int dy = currentY - startY;
                    v.setPressed(true);
                    windowManagerParams.x += dx;
                    windowManagerParams.y += dy;
                    windowManager.updateViewLayout(mDragImageView, windowManagerParams);
                    startX = currentX;
                    startY = currentY;
                    break;
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    elementLayout.requestDisallowInterceptTouchEvent(false);
                    int[] tvLocation = new int[2];
                    mDragImageView.getLocationOnScreen(tvLocation);// 获取当前view的中心点坐标
                    int centerX = tvLocation[0] + mDragImageView.getWidth() / 2;
                    int centerY = tvLocation[1] + mDragImageView.getHeight() / 2;
                    LinearLayout categoryOut = (LinearLayout) parentCategory.getChildAt(0);
                    NestedScrollView scrollView = (NestedScrollView) categoryOut.getChildAt(1);
                    FlowLayout llChild0 = (FlowLayout) scrollView.getChildAt(0);
                    int[] location0 = new int[2];
                    categoryOut.getLocationOnScreen(location0);
                    int llChildX0 = location0[0];
                    int llChildY0 = location0[1];
                    windowManager.removeView(mDragImageView);
                    if ((centerY > llChildY0 && centerY < llChildY0 + categoryOut.getHeight())
                            && (centerX > llChildX0 && centerX < llChildX0 + categoryOut.getWidth())) {
                        // 在第一个圈子内
                        String mTag = (String) mDragImageView.getTag(R.id.image_id);
                        if (mTag.startsWith("http:"))
                            addChildPic(mTag, llChild0, (Integer) mDragImageView.getTag(), false, true);
                        else
                            addChildWorld(mTag, llChild0, (Integer) mDragImageView.getTag(), false,true);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    if (parentCategory.getChildCount() == 1) {
                        elementLayout.getChildAt((Integer) mDragImageView.getTag()).setVisibility(View.VISIBLE);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    LinearLayout categoryOut1 = (LinearLayout) parentCategory.getChildAt(1);
                    NestedScrollView scrollView1 = (NestedScrollView) categoryOut1.getChildAt(1);
                    FlowLayout llChild1 = (FlowLayout) scrollView1.getChildAt(0);
                    int[] location1 = new int[2];
                    categoryOut1.getLocationOnScreen(location1);
                    int llChildX1 = location1[0];
                    int llChildY1 = location1[1];
                    if ((centerY > llChildY1 && centerY < llChildY1 + categoryOut1.getHeight())
                            && (centerX > llChildX1 && centerX < llChildX1 + categoryOut1.getWidth())) {
                        // 在第2个圈子内
                        String mTag = (String) mDragImageView.getTag(R.id.image_id);
                        if (mTag.startsWith("http:"))
                            addChildPic(mTag, llChild1, (Integer) mDragImageView.getTag(), false, true);
                        else
                            addChildWorld(mTag, llChild1, (Integer) mDragImageView.getTag(), false, true);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    if (parentCategory.getChildCount() == 2) {
                        elementLayout.getChildAt((Integer) mDragImageView.getTag()).setVisibility(View.VISIBLE);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    LinearLayout categoryOut2 = (LinearLayout) parentCategory.getChildAt(2);
                    NestedScrollView scrollView2 = (NestedScrollView) categoryOut2.getChildAt(1);
                    FlowLayout llChild2 = (FlowLayout) scrollView2.getChildAt(0);
                    int[] location2 = new int[2];
                    categoryOut2.getLocationOnScreen(location2);
                    int llChildX2 = location2[0];
                    int llChildY2 = location2[1];
                    if ((centerY > llChildY2 && centerY < llChildY2 + categoryOut2.getHeight())
                            && (centerX > llChildX2 && centerX < llChildX2 + categoryOut2.getWidth())) {
                        // 在第3个圈子内
                        String mTag = (String) mDragImageView.getTag(R.id.image_id);
                        if (mTag.startsWith("http:"))
                            addChildPic(mTag, llChild2, (Integer) mDragImageView.getTag(), false, true);
                        else
                            addChildWorld(mTag, llChild2, (Integer) mDragImageView.getTag(), false, true);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    if (parentCategory.getChildCount() == 3) {
                        elementLayout.getChildAt((Integer) mDragImageView.getTag()).setVisibility(View.VISIBLE);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    LinearLayout categoryOut3 = (LinearLayout) parentCategory.getChildAt(3);
                    NestedScrollView scrollView3 = (NestedScrollView) categoryOut3.getChildAt(1);
                    FlowLayout llChild3 = (FlowLayout) scrollView3.getChildAt(0);
                    int[] location3 = new int[2];
                    categoryOut3.getLocationOnScreen(location3);
                    int llChildX3 = location3[0];
                    int llChildY3 = location3[1];
                    if ((centerY > llChildY3 && centerY < llChildY3 + categoryOut3.getHeight())
                            && (centerX > llChildX3 && centerX < llChildX3 + categoryOut3.getWidth())) {
                        // 在第4个圈子内
                        String mTag = (String) mDragImageView.getTag(R.id.image_id);
                        if (mTag.startsWith("http:"))
                            addChildPic(mTag, llChild3, (Integer) mDragImageView.getTag(), false, true);
                        else
                            addChildWorld(mTag, llChild3, (Integer) mDragImageView.getTag(), false, true);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    if (parentCategory.getChildCount() == 4) {
                        elementLayout.getChildAt((Integer) mDragImageView.getTag()).setVisibility(View.VISIBLE);
                        if(onDoingListener != null)
                            onDoingListener.onDoing(this);
                        this.questionBean.setAnswer(getAnswer());
                        break;
                    }
                    break;
            }
        }
        return true;
    }

    private ImageView mDragImageView;
    /**
     * 创建拖动的镜像
     */
    private void createDragImage(View view){
        view.setPressed(true);
        //开启mDragItemView绘图缓存
        view.setDrawingCacheEnabled(true);
        //获取mDragItemView在缓存中的Bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        //这一步很关键，释放绘图缓存，避免出现重复的镜像
        view.destroyDrawingCache();
        view.setPressed(false);
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        int index = elementLayout.indexOfChild(view);
        mDragImageView.setTag(R.id.image_id, view.getTag(R.id.tv_id));
        mDragImageView.setTag(index);
        windowManager.addView(mDragImageView, windowManagerParams);
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (! Settings.canDrawOverlays(context)) {
                if (permissionDialog == null)
                permissionDialog = new AlertDialog.Builder(context)
                        .setTitle("权限申请")
                        .setMessage("分类功能涉及window权限，请手动开启当前权限")
                        .setCancelable(false)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (context instanceof Activity) {
                                    ((Activity)context).finish();
                                }
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                            }
                        }).create();
                permissionDialog.show();
            }
        }
    }
}
