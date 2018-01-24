package ebag.core.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VoicePlayerOnline implements OnCompletionListener, MediaPlayer.OnPreparedListener{
    public MediaPlayer mediaPlayer;
    private OnPlayChangeListener onPlayChangeListener;

    private Timer playTimer;
    private Context context;

    public VoicePlayerOnline(Context context){
        this.context = context;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }  
    }
      
    public void play(){
        mediaPlayer.start();
        isPause = false;
    }  
      
    public void playUrl(String videoUrl){
        isPause = false;
        try {  
            mediaPlayer.reset();  
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepareAsync();//prepare之后自动播放
            //mediaPlayer.start();  
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setOnPlayChangeListener(OnPlayChangeListener onPlayChangeListener){
        this.onPlayChangeListener = onPlayChangeListener;
    }

    private boolean isPause = true;

    public void pause(){
        mediaPlayer.pause();
        isPause = true;
    }  
      
    public void stop(){
        if (mediaPlayer != null) {   
            mediaPlayer.stop();  
            mediaPlayer.release();   
            mediaPlayer = null;   
        }
        isPlaying = false;
    }

    private boolean isPlaying;

    @Override
    /**  
     * 通过onPrepared播放  
     */  
    public void onPrepared(MediaPlayer arg0) {
        playTime();
        isPlaying = true;
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }  
  
    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
        isPlaying = false;
        onPlayChangeListener.onProgressChange(100);
        if(playTimer != null) {
            playTimer.cancel();
        }
        onPlayChangeListener.onCompletePlay();
    }

   public interface OnPlayChangeListener{
        void onProgressChange(int progress);
        void onCompletePlay();
   }

   public boolean isPause(){
        return isPause;
   }
   public boolean isPlaying(){
       return isPlaying;
   }

    private void playTime() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer == null)
                    return;
                if (mediaPlayer.isPlaying()) {
                    final int position = mediaPlayer.getCurrentPosition();
                    final int duration = mediaPlayer.getDuration();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onPlayChangeListener.onProgressChange(100 * position / duration);
                        }
                    });
                }
            }
        };
        playTimer = new Timer();
        playTimer.schedule(mTimerTask, 0, 1000);
    }

}