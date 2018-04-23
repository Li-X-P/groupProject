package com.example.groupproject;



import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class AudioService extends Service implements MediaPlayer.OnCompletionListener{

    MediaPlayer player;

    private final IBinder binder = new AudioBinder();
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return binder;
    }
    /**
     * 当Audio播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
        stopSelf();//结束了，则结束Service
    }

    public void onCreate(){
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId){

        int index = intent.getIntExtra("musicIndex",0);
        Uri uri = null;
        if(index == 0){
            uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.childhood);
        }else if(index == 1){
            uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.dreamofsnow);
        }else if(index == 2){
            uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.happy);
        }else if(index == 3){
            uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.kisstherain);
        }else if(index == 4){
            uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.morning);
        }else{
            uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.pianoking);
        }

        player = MediaPlayer.create(this, uri);
        player.setLooping(true);
        player.setOnCompletionListener(this);

        if(!player.isPlaying()){
            player.start();
        }
        return START_STICKY;
    }

    public void onDestroy(){
        //super.onDestroy();
        if(player.isPlaying()){
            player.stop();
        }
        player.release();
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    class AudioBinder extends Binder{
        //返回Service对象
        AudioService getService(){
            return AudioService.this;
        }
    }

    //后退播放进度
    public void haveFun(){
        if(player.isPlaying() && player.getCurrentPosition()>2500){
            player.seekTo(player.getCurrentPosition()-2500);
        }
    }
}
