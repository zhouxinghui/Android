package ebag.hd.widget.questions;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.FileUtil;
import ebag.core.util.L;
import ebag.core.util.T;
import ebag.core.xRecyclerView.adapter.OnItemClickListener;
import ebag.core.xRecyclerView.adapter.OnItemLongClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.bean.WriteViewBean;
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
        } else {
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
        adapter = new MyAdapter(itemLayoutRes);
        layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        eraserBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bitmap bitmap = drawView.getBitmap();
//                if (bitmap != null)
                drawView.clearPaint();
                if (adapter.selectPosition != -1) {
                    String fileName = adapter.getItem(adapter.selectPosition).getPath();
                    FileUtil.deleteFile(fileName);
                    adapter.getItem(adapter.selectPosition).setPath(null);
                    adapter.getItem(adapter.selectPosition).setBitmap(null);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        penSizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pen_size_one) {
                    drawView.setPaintSize(10);
                } else if (checkedId == R.id.pen_size_two) {
                    drawView.setPaintSize(11);
                } else if (checkedId == R.id.pen_size_three) {
                    drawView.setPaintSize(12);
                } else if (checkedId == R.id.pen_size_four) {
                    drawView.setPaintSize(13);
                }
            }
        });
        checkBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position;
                if (adapter.selectPosition != -1)
                    position = adapter.selectPosition;
                else
                    position = adapter.getItemCount();
                WriteViewBean bean = drawView.getBitmap(bagId, homeworkId, String.valueOf(questionId), position);
                if (bean == null) {
                    T.INSTANCE.show(getContext(), "你还没有写字");
                } else {
                    drawView.clearPaint();
                    if (adapter.selectPosition != -1) {
                        adapter.setItem(position, bean);
                        adapter.setSelectPosition(-1);
                    } else {
                        adapter.addLastItem(bean);
                        layoutManager.scrollToPosition(adapter.getItemCount() - 1);
                    }
                }
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerViewHolder holder, View view, int position) {
                String path = adapter.getItem(position).getPath();
                adapter.removeItem(position);
                FileUtil.deleteFile(path);
                for(int i = position; i < adapter.getItemCount(); i++){
                    File oldFile = new File(adapter.getItem(i).getPath());
                    String fileName = oldFile.getParent() + File.separator + (i);
                    File newFile = new File(fileName);
                    if(oldFile.renameTo(newFile)) {
                        WriteViewBean bean = adapter.getItem(i);
                        bean.setPath(fileName);
                        adapter.setItem(i, bean);
                    }
                }
                /*String path = adapter.getItem(position).getPath();
                File file = new File(path);
                FileUtil.deleteFile(file.getParent());*/
                return false;
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewHolder holder, View view, int position) {
                drawView.clearPaint();
                adapter.setSelectPosition(position);
                Bitmap bitmap = adapter.getItem(position).getBitmap();
                if (bitmap == null)
                    drawView.setCacheBitmap(adapter.getItem(position).getPath());
                else
                    drawView.setCacheBitmap(adapter.getItem(position).getBitmap());
            }
        });
    }

    @Override
    public void setData(QuestionBean questionBean) {
        //TODO 初始化bagId，homeworkId
        questionId = questionBean.getId();
        questionHead = questionBean.getQuestionHead();
    }

    @Override
    public void show(boolean active) {
        headTv.setText(questionHead);
        List<String> fileList = FileUtil.getWriteViewItemFiles(bagId, homeworkId, String.valueOf(questionId));
        List<WriteViewBean> list = new ArrayList<>();
        if (fileList != null && fileList.size() > 0)
            for (String path : fileList) {
                WriteViewBean bean = new WriteViewBean();
                bean.setPath(path);
                list.add(bean);
            }
        adapter.setDatas(list);
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

    private class MyAdapter extends RecyclerAdapter<WriteViewBean> {
        private int selectPosition = -1;
        private int oldPosition = -1;

        public MyAdapter(int itemLayoutId) {
            super(itemLayoutId);
        }


        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            notifyItemChanged(selectPosition);
            notifyItemChanged(oldPosition);
            oldPosition = selectPosition;
        }


        @Override
        protected void fillData(RecyclerViewHolder setter, int position, WriteViewBean entity) {
            L.INSTANCE.e("WRITEVIEW", position + " : " + entity.getPath());
            ImageView imageView = setter.getImageView(R.id.image_id);
            if (selectPosition == position) {
                if (isChinese) {
                    imageView.setBackgroundResource(R.drawable.write_chinese_big);
                } else {
                    imageView.setBackgroundResource(R.drawable.write_english_big);
                }
            } else {
                if (isChinese) {
                    imageView.setBackgroundResource(R.drawable.write_chinese_small);
                } else {
                    imageView.setBackgroundResource(R.drawable.write_english_small);
                }
            }
            if (entity.getBitmap() == null)
                SingleImageLoader.getInstance().loadImage(entity.getPath(), imageView);
            else
                imageView.setImageBitmap(entity.getBitmap());
        }
    }
}
