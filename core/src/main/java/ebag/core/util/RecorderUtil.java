package ebag.core.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 录制音频文件工具类
 * Created by liyimin on 2017/6/17.
 */

public class RecorderUtil {
    private Context context;

    private OnTimeChangeListener onTimeChangeListener;
    private OnPlayProgressChange onPlayProgressChange;

    // 语音文件
    private String fileName = "";
    private String finalFileName = "";//语音文件的最终路径
    // 音频文件保存的路径
    private String path = "";

    // 语音操作对象
    private MediaPlayer mPlayer = null;// 播放器
    private MediaRecorder mRecorder = null;// 录音器
    private boolean isPause = true;// 当前录音是否处于暂停状态
    private boolean isRecording = false;//当前是否处于录音状态
    private ArrayList<String> mList = new ArrayList<>();// 待合成的录音片段
    private ArrayList<String> list = new ArrayList<>();// 已合成的录音片段
    private Timer timer, playTimer;
    // 相关变量
    private int second = 0;
    private int minute = 0;
    private int hour = 0;
    private long limitTime = 0;// 录音文件最短事件1秒

    public static boolean IS_PLAYING_VOICE = false;

    public RecorderUtil(Context context) {
        this.context = context;
        //        path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/ysb/" + context.getPackageName() + "/Record";

        initList();
    }

    /**
     * 初始化录音列表
     */
    private void initList() {
        path = FileUtil.getRecorderPath();
        // 判断SD卡是否存在
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡状态异常，无法获取录音列表！", Toast.LENGTH_LONG).show();
        } else {    //else 以下在项目中暂时没有用处
            // 根据后缀名进行判断、获取文件夹中的音频文件
            File file = new File(path);
            File files[] = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().contains(".")) {
                        // 只取.amr .mp3
                        // .mp4 文件
                        String fileStr = files[i].getName().substring(
                                files[i].getName().indexOf("."));
                        if (fileStr.toLowerCase().equals(".mp3")
                                || fileStr.toLowerCase().equals(".amr")
                                || fileStr.toLowerCase().equals(".mp4"))
                            list.add(files[i].getName());
                    }
                }
            }
        }
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(finalFileName);
        if (file1.exists()) {
            file1.delete();
        }
        fileName = path + File.separator + System.currentTimeMillis() + ".amr";
        isPause = false;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 选择amr格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            // 若录音器启动失败就需要重启应用，屏蔽掉按钮的点击事件。 否则会出现各种异常。
            Toast.makeText(context, "录音器启动失败，请返回重试！", Toast.LENGTH_LONG).show();
            mRecorder.release();
            mRecorder = null;
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡状态异常，请检查后重试！", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (mRecorder != null) {
            mRecorder.start();
            isRecording = true;
            limitTime = System.currentTimeMillis();
        }
        recordTime();
    }

    /**
     * 暂停录音
     */
    public void pauseRecord() throws InterruptedException {
        if (System.currentTimeMillis() - limitTime < 1100) {
            //录音文件不得低于一秒钟
            Toast.makeText(context, "录音时间长度不得低于1秒钟！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            timer.cancel();
            isPause = true;
            isRecording = false;
            // 将录音片段加入列表
            mList.add(fileName);
        }
    }

    /**
     * 完成录音
     */
    public String finishRecord() {
        if (!isPause) {
            try {
                pauseRecord();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
            isPause = true;
            isRecording = false;
        }
        timer.cancel();
        // 最后合成的音频文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(finalFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream = null;
        try {
            for (int i = 0; i < mList.size(); i++) {
                File file = new File(mList.get(i));
                // 把因为暂停所录出的多段录音进行读取
                fileInputStream = new FileInputStream(file);
                byte[] mByte = new byte[fileInputStream.available()];
                int length = mByte.length;
                // 第一个录音文件的前六位是不需要删除的
                if (i == 0) {
                    while (fileInputStream.read(mByte) != -1) {
                        fileOutputStream.write(mByte, 0, length);
                    }
                }
                // 之后的文件，去掉前六位
                else {
                    while (fileInputStream.read(mByte) != -1) {
                        fileOutputStream.write(mByte, 6, length - 6);
                    }
                }
            }
        } catch (Exception e) {
            // 这里捕获流的IO异常，万一系统错误需要提示用户
            e.printStackTrace();
            Toast.makeText(context, "录音合成出错，请重试！", Toast.LENGTH_LONG).show();
        } finally {
            try {
                fileOutputStream.flush();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 录音结束 、时间归零
            minute = 0;
            hour = 0;
            second = 0;
            if (onTimeChangeListener != null)
                onTimeChangeListener.onTimeChange("00:00:00");
        }
        // 不管合成是否成功、删除录音片段
        for (int i = 0; i < mList.size(); i++) {
            File file = new File(mList.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
        // 新录音清空列表
        mList.clear();
        return finalFileName;
    }

    /**
     * 删除录音文件
     */
    public void deleteRecord() {
        // 删除所选中的录音文件
        File file = new File(finalFileName);
        if (file.exists()) {
            file.delete();
        }
        /*// 录音结束 、时间归零
        minute = 0;
        hour = 0;
        second = 0;
        onTimeChangeListener.onTimeChange("00:00:00");*/
    }

    /**
     * 录音计时
     */
    private void recordTime() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                second++;
                if (second >= 60) {
                    second = 0;
                    minute++;
                    if (minute >= 60) {
                        minute = 0;
                        hour++;
                    }
                }
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onTimeChangeListener != null)
                            onTimeChangeListener.onTimeChange(
                                String.format(Locale.getDefault(),
                                "%1$02d:%2$02d:%3$02d", hour, minute, second));
                    }
                });
            }

        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void playTime() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mPlayer == null)
                    return;
                if (mPlayer.isPlaying()) {
                    final int position = mPlayer.getCurrentPosition();
                    final int duration = mPlayer.getDuration();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (onPlayProgressChange != null)
                                onPlayProgressChange.onChange(100 * position / duration);
                        }
                    });
                }
            }
        };
        playTimer = new Timer();
        playTimer.schedule(mTimerTask, 0, 1000);
    }

    /**
     * 录音时间监听
     */
    public void setOnTimeChangeLisener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

    /**
     * 录音时间监听器
     */
    public interface OnTimeChangeListener {
        void onTimeChange(String time);
    }

    public void setOnPlayProgressChange(OnPlayProgressChange onPlayProgressChange) {
        this.onPlayProgressChange = onPlayProgressChange;
    }

    /**
     * 语音播放进度监听器
     */
    public interface OnPlayProgressChange {
        void onChange(int progress);
    }

    /**
     * 判断当前录音是否暂停
     */
    public boolean isPauseRecord() {
        if (isPause) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRecording(){
        return isRecording;
    }

    /**
     * 暂停播放录音
     */
    public void pausePlayRecord() {
        IS_PLAYING_VOICE = false;
        if (mPlayer != null) {
            mPlayer.pause();
            if (playTimer != null) playTimer.cancel();
        }
    }

    public void stopPlayRecord() {
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            if (playTimer != null) playTimer.cancel();
        }
    }

    /**
     * 播放录音
     */
    public void playRecord(String playFileName) {
        IS_PLAYING_VOICE = true;
        if (mPlayer != null) {
            mPlayer.start();
            return;
        }
        // 对按钮的可点击事件的控制是保证不出现空指针的重点！！
        /*if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }*/
        mPlayer = new MediaPlayer();
        // 播放完毕的监听
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完毕改变状态，释放资源
                playTimer.cancel();
                mPlayer.release();
                mPlayer = null;
                IS_PLAYING_VOICE = false;
                if (onPlayProgressChange != null)
                    onPlayProgressChange.onChange(100);
            }
        });
        try {
            // 播放所选中的录音
            mPlayer.setDataSource(playFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            // 若出现异常被捕获后，同样要释放掉资源
            // 否则程序会不稳定，不适合正式项目上使用
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
            Toast.makeText(context, "播放失败！", Toast.LENGTH_LONG).show();
        }
        playTime();
    }

    /**
     * Activity销毁时释放资源
     */
    public void recycle() {
        if (null != mRecorder) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        // 删除片段
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                File file = new File(mList.get(i));
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (playTimer != null) playTimer.cancel();
    }

    /**
     * Activity onPause方法被调用时需要暂停录音
     */
    public void onPauseInvoked() {
        if (mRecorder != null) {
            // 暂停录音
            try {
                pauseRecord();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFinalFileName(String finalFileName) {
        this.finalFileName = finalFileName;
    }
}
