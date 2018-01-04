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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.ConnectionLineView;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * 连线
 * Created by YZY on 2017/12/29.
 */

public class ConnectionView extends LinearLayout implements IQuestionEvent{
    private Context context;
    /**
     * 内容
     */
    private String questionContent;
    /**
     * 标题
     */
    private String questionHead;
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
    private ConnectionLineView connectionLineView;
    private RecyclerView recyclerView;
    /**
     * 适配器
     */
    private MyAdapter adapter;
    /**
     * recyclerView数据源
     */
    private List<ConnectionBean> list;
    /**
     * 保存上一次被点击的item
     */
    private ConnectionBean tempConnection;

    private GestureDetector detector;

    private View currentTouchView;

    private boolean isActive;
    public ConnectionView(Context context) {
        super(context);
        init(context);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        setOrientation(VERTICAL);
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


        connectionLineView = new ConnectionLineView(context);
        relativeParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.connection_margin) * 2,
                ViewGroup.LayoutParams.MATCH_PARENT);
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeParams.addRule(RelativeLayout.ALIGN_TOP,R.id.connection_recycler_id);
        relativeParams.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.connection_recycler_id);
        relativeLayout.addView(connectionLineView,relativeParams);

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(RecyclerViewHolder holder, View view, int position) {
                if(view.getId() == R.id.element_id){
                    //不可操作
                    if (!isActive)
                        return;
                    //获取被点击的Item
                    ConnectionBean connectionBean = adapter.getItem(position);
                    //选中当前的Item
                    connectionBean.selected = true;

                    if(tempConnection != null){
                        //当前选中的和上一个Item 是同一个
                        if(connectionBean.equals(tempConnection)){
                            return;
                        }
                        //点击的是同一边
                        if(tempConnection.isRight == connectionBean.isRight){
                            //上一个选中的item  改为非选中
                            if(tempConnection.connectionBean == null)
                                tempConnection.selected = false;
                            tempConnection = connectionBean;
                        }else{//点击的不是同一边

                            //如果需要连接的 两个已经做过连接了 去除线
                            if(connectionBean.connectionBean != null){
                                connectionLineView.removeLine(connectionBean);
                            }
                            if(tempConnection.connectionBean != null){
                                connectionLineView.removeLine(tempConnection);
                            }

                            //建立连接
                            line(connectionBean,tempConnection);
                            //画线
                            connectionLineView.setLine(connectionBean, true);
                            tempConnection = null;
                        }
                    }else{
                        tempConnection = connectionBean;
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (currentTouchView == null || !isActive)
                    return false;
                if(tempConnection != null){
                    if(tempConnection.connectionBean == null){
                        tempConnection.selected = false;
                    }
                    tempConnection = null;
                }
                int position = (int) currentTouchView.getTag(R.id.element_id);
                ConnectionBean connectionBean = adapter.getItem(position);
                if(connectionBean.connectionBean != null){
                    connectionLineView.removeLine(connectionBean);
                    unLine(connectionBean);
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    /**
     * 解除关联
     */
    private void unLine(ConnectionBean connectionBean){
        if(connectionBean.connectionBean != null){
            connectionBean.connectionBean.unLine();
            connectionBean.unLine();
        }
    }

    private void line(ConnectionBean connectionBean1, ConnectionBean connectionBean2){
        unLine(connectionBean1);
        unLine(connectionBean2);
        connectionBean1.selected = true;
        connectionBean2.selected = true;
        connectionBean1.connectionBean = connectionBean2;
        connectionBean2.connectionBean = connectionBean1;
    }

    @Override
    public void setData(QuestionBean questionBean) {
        questionContent = questionBean.getQuestionContent();
        studentAnswer = questionBean.getAnswer();
        rightAnswer = questionBean.getRightAnswer();
        list = new ArrayList<>();
        String[] allSplit = questionContent.split(";");
        String[] elementSplit1 = allSplit[0].split(",");
        String[] elementSplit2 = allSplit[1].split(",");
        int count = Math.min(elementSplit1.length, elementSplit2.length);
        for (int i = 0; i < count; i ++){
            list.add(new ConnectionBean(elementSplit1[i], false));
            list.add(new ConnectionBean(elementSplit2[i],true));
        }
    }

    @Override
    public void show(boolean active) {
        questionActive(active);
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
                        ConnectionBean connectionLeft = null;
                        ConnectionBean connectionRight = null;
                        for(int i = 0; i < list.size(); i ++) {
                            if (list.get(i).content.equals(answers[0]))
                                connectionLeft = list.get(i);
                            if (list.get(i).content.equals(answers[1]))
                                connectionRight = list.get(i);
                        }
                        //建立连接
                        if(connectionLeft != null && connectionRight != null){
                            line(connectionLeft,connectionRight);
                            connectionLineView.setLine(connectionLeft, true);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        }, 1000);
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
        List<ConnectionBean> lines = connectionLineView.getLines();
        if (lines == null || lines.size() == 0)
            return ;
        for (int i = 0; i < lines.size(); i++) {

            ConnectionBean connectionBean = lines.get(i);
            if(!ConnectionLineView.isConnectionRegular(connectionBean))
                continue;

            if(!isAnswerRight(connectionBean,rightSplit)){
                connectionLineView.setWrongLine(connectionBean);
            }
        }
        adapter.setResult(true);
    }

    private boolean isAnswerRight(ConnectionBean connectionBean, String[] rightSplit){
        for (String rightAnswer : rightSplit){
            String[] rightAnswers = rightAnswer.split(",");
            if (rightAnswers.length != 2)
                continue;
            //比较耗性能，可以改成普通循环的方式
            if((connectionBean.content.equals(rightAnswers[0])
                    && connectionBean.connectionBean.content.equals(rightAnswers[1])
                    ) || (connectionBean.content.equals(rightAnswers[1])
                    && connectionBean.connectionBean.content.equals(rightAnswers[0]))){
                connectionBean.isCorrect = true;
                connectionBean.connectionBean.isCorrect = true;
                return true;
            }
        }
        connectionBean.isCorrect = false;
        connectionBean.connectionBean.isCorrect = false;
        return false;
    }

    @Override
    public String getAnswer() {
        List<ConnectionBean> lines = connectionLineView.getLines();
        if (lines == null || lines.size() == 0)
            return "";
        StringBuilder answer = new StringBuilder("");
        for (ConnectionBean line : lines) {
            answer.append(line.content)
                    .append(",")
                    .append(line.connectionBean.content)
                    .append(";");
        }
        answer.deleteCharAt(answer.lastIndexOf(";"));
        return answer.toString();
    }

    @Override
    public void reset() {

    }

    private class MyAdapter extends RecyclerAdapter<ConnectionBean> implements OnTouchListener{
        private final static int TYPE_LEFT_IMG = 1;
        private final static int TYPE_LEFT_TEXT = 2;
        private final static int TYPE_RIGHT_IMG = 3;
        private final static int TYPE_RIGHT_TEXT = 4;
        private boolean isResult = false;
        public MyAdapter() {
            addItemType(TYPE_LEFT_IMG, R.layout.item_connection_left_img);
            addItemType(TYPE_LEFT_TEXT, R.layout.item_connection_left_text);
            addItemType(TYPE_RIGHT_IMG, R.layout.item_connection_right_img);
            addItemType(TYPE_RIGHT_TEXT, R.layout.item_connection_right_text);
        }

        public void setResult(boolean result) {
            isResult = result;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if(getItem(position).isRight){
                if (getItem(position).content.startsWith("http"))
                    return TYPE_RIGHT_IMG;
                else
                    return TYPE_RIGHT_TEXT;
            }else{
                if (getItem(position).content.startsWith("http"))
                    return TYPE_LEFT_IMG;
                else
                    return TYPE_LEFT_TEXT;
            }
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, ConnectionBean entity) {
            switch (setter.getItemViewType()){
                case TYPE_LEFT_IMG:
                case TYPE_RIGHT_IMG:
                    SingleImageLoader.getInstance().setImage(entity.content, setter.getImageView(R.id.element_id));
                    break;
                case TYPE_LEFT_TEXT:
                case TYPE_RIGHT_TEXT:
                    setter.setText(R.id.element_id, entity.content);
                    break;
            }

            entity.pointY = getPointY(setter.getConvertView());
            setter.addClickListener(R.id.element_id);
            setter.getView(R.id.element_id).setOnTouchListener(this);
            setter.setTag(R.id.element_id, R.id.element_id, position);
            if(isResult){//查看结果
                setter.setBackgroundRes(R.id.element_id, R.drawable.connection_wrong_bg);
                setter.getView(R.id.element_id).setSelected(entity.isCorrect);
            }else{
                setter.setBackgroundRes(R.id.element_id,R.drawable.connection_element_bg);
                setter.getView(R.id.element_id).setSelected(entity.selected);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            currentTouchView = v;
            return detector.onTouchEvent(event);
        }
    }

    private int getPointY(View view){
        return view.getTop() + view.getHeight() / 2;
    }

    public static class ConnectionBean{
        public String content;
        //是否正确
        public boolean isCorrect = false;
        //是否选中
        private boolean selected = false;
        //连接的item
        public ConnectionBean connectionBean;
        //是否是右边的item
        public boolean isRight;

        public int pointY;

        ConnectionBean(String content, boolean isRight){
            this.content = content;
            this.isRight = isRight;
        }

        void unLine(){
            connectionBean = null;
            selected = false;
            isCorrect = false;
        }

    }
}
