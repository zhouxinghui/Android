package ebag.hd.widget.questions;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.ArrayUtil;
import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.MyLineView;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * 连线
 * Created by YZY on 2017/12/29.
 */

public class ConnectionView extends BaseQuestionView implements View.OnTouchListener{
    /**
     * 标题
     */
    private List<String> titleList;
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
     * 画线view
     */
    private MyLineView myLineView;
    private RecyclerView recyclerView;
    /**
     * 适配器
     */
    private MyAdapter adapter;
    /**
     * recyclerView数据源
     */
    private List<String> list;
    /**
     * 临时变量，判断左边的item是否已经被点击，-1代表没有被点击
     */
    private int left = -1;
    /**
     * 临时变量，判断右边的item是否已经被点击，-1代表没有被点击
     */
    private int right = -1;
    /**
     * 保存上一次被点击的item的position
     */
    private int tempPosition = -1;
    /**
     * 保存上一次被点击的item的Y坐标
     */
    private int tempY;
    private GestureDetector detector;
    /**
     * 当前被双击的view
     */
    private View currentTouchView;
    private boolean isActive;
    public ConnectionView(Context context) {
        super(context);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(relativeLayout,layoutParams);

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView = new RecyclerView(context);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new MyAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setId(R.id.connection_recycler_id);
        relativeLayout.addView(recyclerView,relativeParams);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(RecyclerViewHolder holder, View view, int position) {
                if (!isActive)
                    return;
                if (position % 2 == 0){     //点击左边
                    left = 1;
                    if (right != -1) {
                        myLineView.setLine(position, tempPosition, getPointY(holder.getConvertView()), tempY, true);
                        left = -1;
                        right = -1;
                    }else{
                        tempPosition = position;
                        tempY = getPointY(holder.getConvertView());
                    }
                }else{                      //点击右边
                    right = 1;
                    if (left != -1) {
                        myLineView.setLine(position, tempPosition, tempY, getPointY(holder.getConvertView()), true);
                        left = -1;
                        right = -1;
                    }else{
                        tempPosition = position;
                        tempY = getPointY(holder.getConvertView());
                    }
                }
                adapter.setCurrentPressPosition(position);
            }
        });

        myLineView = new MyLineView(context);
        relativeParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.connection_margin) * 2,
                ViewGroup.LayoutParams.MATCH_PARENT);
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeParams.addRule(RelativeLayout.ALIGN_TOP,R.id.connection_recycler_id);
        relativeParams.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.connection_recycler_id);
        relativeLayout.addView(myLineView,relativeParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        titleList = new ArrayList<>();
        titleList.add(questionBean.getQuestionHead());
        questionContent = questionBean.getQuestionContent();
        studentAnswer = questionBean.getAnswer();
        rightAnswer = questionBean.getRightAnswer();
        list = new ArrayList<>();
        String[] allSplit = questionContent.split(";");
        String[] elementSplit1 = allSplit[0].split(",");
        String[] elementSplit2 = allSplit[1].split(",");
        int count = Math.min(elementSplit1.length, elementSplit2.length);
        for (int i = 0; i < count; i ++){
            list.add(elementSplit1[i]);
            list.add(elementSplit2[i]);
        }

        detector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (currentTouchView == null || !isActive)
                    return false;
                int position = (int) currentTouchView.getTag(R.id.element_id);
                int[] positions = myLineView.removeLine(position);
                if (positions == null || positions.length == 0)
                    return false;
                for (int p : positions){
                    adapter.setNormalPosition(p);
                }
                return true;
            }
        });
    }

    @Override
    public void show(boolean active) {
        questionActive(active);
        setTitle(titleList);
        if (list != null && list.size() > 0)
            adapter.setDatas(list);
        else
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!StringUtils.INSTANCE.isEmpty(studentAnswer)){
                    String[] studentAnswers = studentAnswer.split(";");
                    for (String answer : studentAnswers) {
                        String[] answers = answer.split(",");
                        int position1 = list.indexOf(answers[0]);
                        int position2 = list.indexOf(answers[1]);
                        myLineView.setLine(
                                position1,
                                position2,
                                getPointY(recyclerView.getChildAt(position1)),
                                getPointY(recyclerView.getChildAt(position2)),
                                true);
                    }
                }
            }
        }, 500);
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
        questionActive(false);
        setWrong();
    }

    private void setWrong(){
        if (StringUtils.INSTANCE.isEmpty(rightAnswer))
            return;
        String[] rightSplit = rightAnswer.split(";");
        List<MyLineView.MyLine> answerPositions = myLineView.getAnswer();
        if (answerPositions == null || answerPositions.size() == 0)
            return ;
        List<Integer> wrongPositions = new ArrayList<>();
        for (int i = 0; i < answerPositions.size(); i++) {
            String[] positions = answerPositions.get(i).getAnswer().split(",");
            if (positions.length != 2)
                continue;
            int position1 = Integer.parseInt(positions[0]);
            int position2 = Integer.parseInt(positions[1]);
            String answer1 = adapter.getItem(Integer.parseInt(positions[0]));
            String answer2 = adapter.getItem(Integer.parseInt(positions[1]));
            String[] answers = new String[]{answer1, answer2};
            for (String rightAnswer : rightSplit){
                String[] rightAnswers = rightAnswer.split(",");
                if (rightAnswers.length != 2)
                    return;
                //比较耗性能，可以改成普通循环的方式
                List<String> list = Arrays.asList(rightAnswers);
                if (list.contains(answer1) || list.contains(answer2)) {
                    if (!ArrayUtil.containsSame(answers, rightAnswers)) {
                        wrongPositions.add(position1);
                        wrongPositions.add(position2);
                        myLineView.setWrongLine(
                                position1,
                                position2,
                                getPointY(recyclerView.getChildAt(position1)),
                                getPointY(recyclerView.getChildAt(position2)));
                    }
                }
            }
        }
        adapter.setWrongPosition(wrongPositions);
    }

    @Override
    public String getAnswer() {
        List<MyLineView.MyLine> answerPositions = myLineView.getAnswer();
        if (answerPositions == null || answerPositions.size() == 0)
            return "";
        StringBuilder answer = new StringBuilder("");
        for (int i = 0; i < answerPositions.size(); i++) {
            String[] positions = answerPositions.get(i).getAnswer().split(",");
            if (positions.length != 2)
                continue;
            answer.append(adapter.getItem(Integer.parseInt(positions[0])))
                    .append(",").append(adapter.getItem(Integer.parseInt(positions[1])))
                    .append(";");
        }
        answer.deleteCharAt(answer.lastIndexOf(";"));
        return answer.toString();
    }

    @Override
    public void reset() {

    }

    private class MyAdapter extends RecyclerAdapter<String>{
        private final static int TYPE_LEFT_IMG = 1;
        private final static int TYPE_LEFT_TEXT = 2;
        private final static int TYPE_RIGHT_IMG = 3;
        private final static int TYPE_RIGHT_TEXT = 4;
        private int currentPressPosition = -1;
        private int normalPosition = -1;
        private List<Integer> wrongPosition;
        public MyAdapter() {
            addItemType(TYPE_LEFT_IMG, R.layout.item_connection_left_img);
            addItemType(TYPE_LEFT_TEXT, R.layout.item_connection_left_text);
            addItemType(TYPE_RIGHT_IMG, R.layout.item_connection_right_img);
            addItemType(TYPE_RIGHT_TEXT, R.layout.item_connection_right_text);
        }

        public void setCurrentPressPosition(int currentPressPosition){
            this.currentPressPosition = currentPressPosition;
            notifyDataSetChanged();
        }

        public void setNormalPosition(int normalPosition){
            this.normalPosition = normalPosition;
            notifyItemChanged(normalPosition);
        }

        public void setWrongPosition(List<Integer> wrongPosition){
            this.wrongPosition = wrongPosition;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if(position % 2 == 0){
                if (getItem(position).startsWith("http"))
                    return TYPE_LEFT_IMG;
                else
                    return TYPE_LEFT_TEXT;
            }else{
                if (getItem(position).startsWith("http"))
                    return TYPE_RIGHT_IMG;
                else
                    return TYPE_RIGHT_TEXT;
            }
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, String entity) {
            switch (setter.getItemViewType()){
                case TYPE_LEFT_IMG:
                case TYPE_RIGHT_IMG:
                    SingleImageLoader.getInstance().setImage(entity, setter.getImageView(R.id.element_id));
                    setter.addClickListener(R.id.element_id);
                    break;
                case TYPE_LEFT_TEXT:
                case TYPE_RIGHT_TEXT:
                    setter.setText(R.id.element_id, entity);
                    setter.addClickListener(R.id.element_id);
                    break;
            }
            //单击时设置选中状态
            if (currentPressPosition != -1 && currentPressPosition == position && !myLineView.hasLine(position)) {
                setter.getView(R.id.element_id).setSelected(true);
            }else {
                if (myLineView.hasLine(position))
                    setter.getView(R.id.element_id).setSelected(true);
                else
                    setter.getView(R.id.element_id).setSelected(false);
            }
            //双击时设置normal状态
            View elementView = setter.getView(R.id.element_id);
            elementView.setTag(R.id.element_id, position);
            elementView.setOnTouchListener(ConnectionView.this);
            if (normalPosition != -1 && normalPosition == position){
                setter.getView(R.id.element_id).setSelected(false);
                normalPosition = -1;
            }
            //设置错误时背景
            if (wrongPosition != null && wrongPosition.size() != 0 && wrongPosition.contains(position)) {
                setter.getView(R.id.element_id).setBackgroundResource(R.drawable.connection_wrong_bg);
            }else{
                setter.getView(R.id.element_id).setBackgroundResource(R.drawable.connection_element_bg);
            }
        }
    }

    private int getPointY(View view){
        return view.getTop() + view.getHeight() / 2;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        currentTouchView = v;
        return detector.onTouchEvent(event);
    }
}
