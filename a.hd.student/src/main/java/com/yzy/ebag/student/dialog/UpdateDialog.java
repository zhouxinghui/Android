package com.yzy.ebag.student.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzy.ebag.student.R;

/**
 * Created by unicho on 2017/11/25.
 */

public class UpdateDialog extends AppCompatDialogFragment implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.NoBackgroundDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        setCancelable(false);
    }

    private ImageView btnUpdateClose;
    private TextView btnUpdate;
    private TextView tvUpdateContent;
    private TextView tvUpdateTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_update,container);
        btnUpdateClose = view.findViewById(R.id.btnUpdateClose);
        btnUpdateClose.setOnClickListener(this);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        tvUpdateContent = view.findViewById(R.id.tvUpdateContent);
        tvUpdateTitle = view.findViewById(R.id.tvUpdateTitle);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateClose:
                dismiss();
                break;
            case R.id.btnUpdate:
                break;
        }
    }
}
