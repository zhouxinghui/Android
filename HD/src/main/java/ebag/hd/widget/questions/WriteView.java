package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.FileUtil;
import ebag.core.util.T;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.ViewHolderHelper;
import ebag.hd.R;
import ebag.hd.widget.DrawView;

/**
 * Created by YZY on 2017/12/23.
 */

public class WriteView extends LinearLayout implements IQuestionEvent {
    private Context context;
    /**
     * 画板
     */
    private DrawView drawView;
    /**
     * 书包号
     */
    private String bagId = "123456";
    /**
     * 本次作业id
     */
    private String homeworkId = "1";
    /**
     * 试题id
     */
    private long questionId;
    /**
     * 题目内容Tv
     */
    private TextView headTv;
    /**
     * 题目内容
     */
    private String questionHead;
    /**
     * 作答图片URL
     */
    private String questionContent;
    private Button checkBtn;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    public WriteView(Context context) {
        super(context);
        this.context = context;
    }

    public WriteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.write_view_chinese, this);

        headTv = findViewById(R.id.head_tv_id);
        drawView = findViewById(R.id.draw_view);
        Button eraserBtn = findViewById(R.id.btn_eraser_id);
        RadioGroup penSizeGroup = findViewById(R.id.pen_size_group);
        checkBtn = findViewById(R.id.btn_check);
        recyclerView = findViewById(R.id.recycler_id);

        adapter = new MyAdapter(recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        List<String> list = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setDatas(list);

        eraserBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bitmap bitmap = drawView.getBitmap();
//                if (bitmap != null)
                    drawView.clearPaint();
            }
        });
        penSizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pen_size_one){
                    drawView.setPaintSize(4);
                }else if (checkedId == R.id.pen_size_two){
                    drawView.setPaintSize(6);
                }else if (checkedId == R.id.pen_size_three){
                    drawView.setPaintSize(8);
                }else if (checkedId == R.id.pen_size_four){
                    drawView.setPaintSize(10);
                }
            }
        });
        checkBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String imagePath = drawView.getBitmapPath(bagId, homeworkId, String.valueOf(questionId), adapter.getItemCount());
                if (imagePath == null){
                    T.INSTANCE.show(getContext(), "你还没有写字");
                }else{
                    drawView.clearPaint();
                    adapter.addData(imagePath);
                }
            }
        });
    }

    @Override
    public void setData(QuestionBean questionBean) {
        questionId = questionBean.getId();
        questionHead = questionBean.getQuestionHead();
    }

    @Override
    public void show(boolean active) {
        //TODO 初始化bagId，homeworkId
        headTv.setText(questionHead);
        List<String> fileList = FileUtil.getWriteViewItemFiles(bagId, homeworkId, String.valueOf(questionId));
        if (fileList != null){
            adapter.setDatas(fileList);
        }
        enable(active);
    }

    @Override
    public void enable(boolean active) {
        checkBtn.setEnabled(active);
        recyclerView.setEnabled(active);
    }

    @Override
    public void showResult() {
        enable(false);
    }

    @Override
    public String getAnswer() {
        return null;
    }

    private class MyAdapter extends RecyclerAdapter<String>{

        public MyAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.write_view_item);
        }

        public void addData(String bitmap){
            mDatas.add(bitmap);
            notifyDataSetChanged();
        }

        @Override
        protected void fillData(ViewHolderHelper setter, int position, String entity) {
            ImageView imageView = setter.getImageView(R.id.image_id);
            SingleImageLoader.getInstance().loadImage(entity, imageView);
        }
    }
}
