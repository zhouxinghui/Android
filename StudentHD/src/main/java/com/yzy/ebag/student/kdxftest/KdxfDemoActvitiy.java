package com.yzy.ebag.student.kdxftest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.yzy.ebag.student.R;

import org.json.JSONException;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018年4月27日 16:54:07
 * wy
 * 科大讯飞语音评测，打分类、
 **/
public class KdxfDemoActvitiy extends AppCompatActivity implements View.OnClickListener {
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    private Button bt6;
    private Button bt7;
    private int v;
    private Button btEnd;
    private SpeechEvaluator evaluator;
    private ArrayList<String> strings;
    private String jsonObject;
    private String resultString;
    private TextView tv;
    private Gson gson;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_actvitiy);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);
        tv = findViewById(R.id.tv);
        btEnd = findViewById(R.id.end);
        ll = findViewById(R.id.ll);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        strings = new ArrayList<>();
        evaluator = SpeechEvaluator.createEvaluator(this, null);
        evaluator.setParameter(SpeechConstant.LANGUAGE, "en_us");
        evaluator.setParameter(SpeechConstant.ISE_CATEGORY, "read_sentence");
        evaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        evaluator.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        evaluator.setParameter(SpeechConstant.ASR_AUDIO_PATH, "");
        btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sjdfhi", "结束");
                evaluator.stopEvaluating();
                ll.removeAllViews();
            }
        });
    }

    private void aa(String cc, final Button button) {
        evaluator.startEvaluating("[content]" + cc, null, new EvaluatorListener() {


            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {
                Log.d("sjdfhi", "onBeginOfSpeech: 开始");
            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(EvaluatorResult evaluatorResult, boolean b) {
                resultString = evaluatorResult.getResultString();
                try {
                    jsonObject = XML.toJSONObject(resultString).toString();
                    Log.d("TAG2", jsonObject);
                    gson = new Gson();
                    SpeechEvaluatorVo speechEvaluatorVo = gson.fromJson(jsonObject, SpeechEvaluatorVo.class);
                    if (null != speechEvaluatorVo.getXml_result()) {
//                        isIs_rejected  为 true证明为乱读
                        boolean isIs_rejected = speechEvaluatorVo.getXml_result().getRead_sentence().getRec_paper().getRead_chapter().isIs_rejected();
                        Log.d("TAG", "onResult: " + isIs_rejected);

//                    文字变色
                        List<SpeechEvaluatorVo.XmlResultBean.ReadSentenceBean.RecPaperBean.ReadChapterBean.SentenceBean.WordBean> word = speechEvaluatorVo.getXml_result().getRead_sentence().getRec_paper().getRead_chapter().getSentence().getWord();
                        for (int i = 0; i < word.size(); i++) {
                            if (null != word.get(i).getIndex()) {
                                    TextView textView = new TextView(KdxfDemoActvitiy.this);
                                    textView.setText("  " + word.get(i).getContent());
                                    if (word.get(i).getTotal_score() < 2.8) {
                                        textView.setTextColor(Color.RED);
                                    } else {
                                        textView.setTextColor(Color.BLACK);
                                    }
                                    ll.addView(textView);
                                    Log.d("cca", word.get(i).getIndex());
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.d("TAG", "onError: " + speechError.toString());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                aa(bt1.getText().toString(), bt1);
//                tv.setText(bt1.getText().toString()+" :    "+v+"分");
                break;
            case R.id.bt2:
                aa(bt2.getText().toString(), bt2);
//                tv.setText(bt2.getText().toString()+" :    "+v+"分");
                break;
            case R.id.bt3:
                aa(bt3.getText().toString(), bt3);
//                tv.setText(bt3.getText().toString()+" :    "+v+"分");
                break;
            case R.id.bt4:
                aa(bt4.getText().toString(), bt4);
//                tv.setText(bt4.getText().toString()+" :    "+v+"分");
                break;
            case R.id.bt5:
                aa(bt5.getText().toString(), bt5);
//                tv.setText(bt5.getText().toString()+" :    "+v+"分");
                break;
            case R.id.bt6:
                aa(bt6.getText().toString(), bt6);
//                tv.setText(bt6.getText().toString()+" :    "+v+"分");
                break;
            case R.id.bt7:
                aa(bt7.getText().toString(), bt7);
//                tv.setText(bt7.getText().toString()+" :    "+v+"分");
                break;
        }
    }
}
