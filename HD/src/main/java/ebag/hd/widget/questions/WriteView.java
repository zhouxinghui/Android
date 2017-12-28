package ebag.hd.widget.questions;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.FileUtil;
import ebag.core.util.L;
import ebag.core.util.T;
import ebag.core.xRecyclerView.adapter.OnItemLongClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.DrawView;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * Created by YZY on 2017/12/23.
 */

public class WriteView extends LinearLayout implements IQuestionEvent {
    private Context context;
    /**
     * 是否是英语
     */
    private boolean isChinese;
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
    private GridLayoutManager layoutManager;
    public WriteView(Context context) {
        super(context);
        this.context = context;
    }

    public WriteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WriteView);
        isChinese = a.getBoolean(R.styleable.DrawView_isEnglish, false);
        a.recycle();
        this.context = context;
    }

    public WriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int spanCount;
        int itemLayoutRes;
        if (isChinese) {
            spanCount = 5;
            itemLayoutRes = R.layout.write_view_chinese_item;
            LayoutInflater.from(getContext()).inflate(R.layout.write_view_chinese, this);
        }else {
            spanCount = 3;
            itemLayoutRes = R.layout.write_view_english_item;
            LayoutInflater.from(getContext()).inflate(R.layout.write_view_english, this);
        }

        headTv = findViewById(R.id.head_tv_id);
        drawView = findViewById(R.id.draw_view);
        Button eraserBtn = findViewById(R.id.btn_eraser_id);
        RadioGroup penSizeGroup = findViewById(R.id.pen_size_group);
        checkBtn = findViewById(R.id.btn_check);
        recyclerView = findViewById(R.id.recycler_id);
        adapter = new MyAdapter();
        layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

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
                    drawView.setPaintSize(10);
                }else if (checkedId == R.id.pen_size_two){
                    drawView.setPaintSize(11);
                }else if (checkedId == R.id.pen_size_three){
                    drawView.setPaintSize(12);
                }else if (checkedId == R.id.pen_size_four){
                    drawView.setPaintSize(13);
                }
            }
        });
        checkBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = drawView.getBitmap(bagId, homeworkId, String.valueOf(questionId), adapter.getItemCount());
                if (file == null){
                    T.INSTANCE.show(getContext(), "你还没有写字");
                }else{
                    drawView.clearPaint();
                    adapter.addLastItem(file);
                    layoutManager.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerViewHolder holder, View view, int position) {
                File file1 = adapter.getItem(position);
                adapter.removeItem(file1);
                FileUtil.deleteFile(file1.getAbsolutePath());
                for(int i = position + 1; i < adapter.getItemCount(); i++){
                    File oldFile = adapter.getItem(i);
                    File newFile = new File(oldFile.getParent() + File.separator + (i - 1));
                    if(oldFile.renameTo(newFile)) {
                        adapter.setItem(i, newFile);
                    }
                }
                return false;
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
        List<File> fileList = FileUtil.getWriteViewItemFiles2(bagId, homeworkId, String.valueOf(questionId));
        adapter.setDatas(fileList);
        questionActive(active);
    }

    @Override
    public void questionActive(boolean active) {
        checkBtn.setEnabled(active);
        recyclerView.setEnabled(active);
    }

    @Override
    public boolean isQuestionActive() {
        return checkBtn.isEnabled();
    }

    @Override
    public void showResult() {
        questionActive(false);
    }

    @Override
    public String getAnswer() {
        return null;
    }

    @Override
    public void reset() {

    }

    private class MyAdapter extends RecyclerAdapter<File>{

        public MyAdapter() {
            super(R.layout.write_view_item);
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, File entity) {
            L.INSTANCE.e("WRITEVIEW",position +" : "+ entity.getPath());
            ImageView imageView = setter.getImageView(R.id.image_id);
//            if (entity.getBitmap() == null)
            SingleImageLoader.getInstance().loadImage(entity, imageView);
//            else
//                imageView.setImageBitmap(entity.getBitmap());
        }
    }
}
