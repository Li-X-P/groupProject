package com.example.groupproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

public class SleepActivity extends AppCompatActivity {



    //  private MediaRecorderDetector mrd;

    private ScrollView scrollView;

    private LinearLayout linlayContent;

    private ImageView imageView = null;

    Button reportbutton;

    private LinearLayout.LayoutParams linLayParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    @SuppressWarnings("HanderLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    double voice = (double) msg.obj;
                    display(voice, linLayParams);

                    break;
            }
            //  mResult.setText("The background noise "Double.toString(st));
        }
    };

    double ave = 0;

    private static final String TAG = "MainActivity";
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.RECORD_AUDIO,
    };

    private MediaPlayer mediaPlayer;
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun, isSleepingNow = false;
    Object mLock;
    double st = 0;
    boolean mtest = false;
    TextView mResult;
    int hour_system, minute_system, light_sleep = 0, deep_sleep = 0, awake = 0;
    private  int mHour,mMinute,mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        verifyPermissions(SleepActivity.this);
        Bundle goSleeping= getIntent().getExtras();
        mHour = goSleeping.getInt("hour");
        mMinute = goSleeping.getInt("minute");
        mDuration = goSleeping.getInt("Duration");


        scrollView = (ScrollView) findViewById(R.id.scrollView);
        linlayContent = (LinearLayout) findViewById(R.id.linlay_content);
        mResult = (TextView) findViewById(R.id.textView2);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.wake_up);
        Uri uri = Uri.parse("android.resource://"+ getPackageName()+"/"+ R.raw.fakereal);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        reportbutton = (Button)findViewById(R.id.button4);
        reportbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(awake == 0 && light_sleep ==0 && deep_sleep ==0){
                    Toast.makeText(SleepActivity.this, "You are awake now!",
                            Toast.LENGTH_LONG).show();
                }else{
                    /*Intent intent = new Intent (SleepActivity.this, ReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("awaketime",awake);
                    bundle.putInt("lightsleep", light_sleep);
                    bundle.putInt("deepsleep", deep_sleep);
                    intent.putExtras(bundle);
                    startActivity(intent);*/
                }
            }
        });
    }

    //click on the test button
    public void MeasureNoise(View view){
        if(isSleepingNow == true){
            Toast.makeText(this, "You are in sleeping mode now! Please switch to the get up mode first!",
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Start to measure your background noise level! Please wait for several seconds...",
                    Toast.LENGTH_LONG).show();
            linlayContent.addView(createTextView("Start measuring!", linLayParams));
            mtest = true;
            if(ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            getNoiseLevel(new OnGetVoiceListener() {
                @Override
                public void onGetVoice(double voice) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = voice;
                    msg.what = 100;
                    mHandler.sendMessage(msg);

                }
            });
        }
    }
    // click on the start button
    public void StartSleep(View view){
        if(isSleepingNow == false){
            isSleepingNow = true;
            linlayContent.addView(createTextView("You are now in the sleeping mode...", linLayParams));
            Toast.makeText(this, "Good night!",
                    Toast.LENGTH_LONG).show();
            imageView.setImageResource(R.drawable.sleeping);
            mtest = false;
            if(ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            getNoiseLevel(new OnGetVoiceListener() {
                @Override
                public void onGetVoice(double voice) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = voice;
                    msg.what = 100;
                    mHandler.sendMessage(msg);
                }
            });
        }else{
            Toast.makeText(this, "You are already in the sleeping mode!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void GetUp(View view){
        if(isSleepingNow == false){
            Toast.makeText(this, "You are awake now!",
                    Toast.LENGTH_LONG).show();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(SleepActivity.this);
            builder.setTitle(R.string.get_up);
            builder.setMessage(R.string.get_up_warning);
            builder.setPositiveButton(R.string.get_up_yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isSleepingNow = false;
                            if(mediaPlayer.isPlaying() == true){
                                mediaPlayer.stop();
                            }
                            imageView.setImageResource(R.drawable.wake_up);
                            isGetVoiceRun = false;
                            linlayContent.addView(createTextView("Total deep sleeping time: "+String.valueOf(deep_sleep), linLayParams));
                            linlayContent.addView(createTextView("Total light sleeping time: "+String.valueOf(light_sleep), linLayParams));
                            linlayContent.addView(createTextView("Total awake time: "+String.valueOf(awake), linLayParams));
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
            builder.setNegativeButton(R.string.get_up_no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    private void display(double voice, LinearLayout.LayoutParams linLayParams) {
        DecimalFormat nf = new DecimalFormat("0.00");
        linlayContent.addView(createTextView("Measuring finished! Your background noise is about "+String.valueOf(nf.format(voice))+" dB.", linLayParams));
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private TextView createTextView(String text, LinearLayout.LayoutParams linLayParams) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(linLayParams);
        return textView;
    }

    public SleepActivity() {
        mLock = new Object();
    }

    public void getNoiseLevel(final OnGetVoiceListener onGetVoiceListener) {
        if (isGetVoiceRun) {
            Log.e(TAG, "正在录音");
            return;
        }

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        if (mAudioRecord == null) {
            Log.e(TAG, "mAudioRecord初始化失败");
        }
        isGetVoiceRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mtest == true){
                    mAudioRecord.startRecording();
                    short[] buffer = new short[BUFFER_SIZE];
                    int count = 0;
                    while (isGetVoiceRun) {
                        //r是实际读取的数据长度，一般而言r会小于buffersize
                        int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                        long v = 0;
                        // 将 buffer 内容取出，进行平方和运算
                        for (int i = 0; i < buffer.length; i++) {
                            v += buffer[i] * buffer[i];
                        }
                        // 平方和除以数据总长度，得到音量大小。
                        double mean = v / (double) r;
                        double volume = 10 * Math.log10(mean);
                        if(count == 0){
                            ave = volume;
                        }else{
                            ave = (ave+volume)/2;
                        }
                        Log.d(TAG, "分贝值:" + volume);


                        //onGetVoiceListener.onGetVoice(volume);
                        // 大概一秒十次
                        synchronized (mLock) {
                            try {
                                mLock.wait(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        count ++;
                        if(count == 50){
                            isGetVoiceRun = false;
                            mtest = false;
                            onGetVoiceListener.onGetVoice(ave);
                        }
                    }
                }else{
                    Calendar c = Calendar.getInstance();
                    hour_system= c.get(Calendar.HOUR_OF_DAY);
                    minute_system = c.get(Calendar.MINUTE);
                    System.out.println(hour_system);
                    System.out.println(minute_system);
                    mAudioRecord.startRecording();
                    short[] buffer = new short[BUFFER_SIZE];
                    int count1 = 0, sample_count = 0;
                    double [] Voice_Sample = new double[15];
                    double sample_sum = 0, sample_ave = 0, sample_var = 0;
                    while (isGetVoiceRun) {
                        //r是实际读取的数据长度，一般而言r会小于buffersize
                        int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                        long v = 0;
                        // 将 buffer 内容取出，进行平方和运算
                        for (int i = 0; i < buffer.length; i++) {
                            v += buffer[i] * buffer[i];
                        }
                        // 平方和除以数据总长度，得到音量大小。
                        double mean = v / (double) r;
                        double volume = 10 * Math.log10(mean);
                        Voice_Sample[sample_count] = volume;
                        sample_sum += volume;
                        sample_count++;
                        if(sample_count == 15){
                            sample_ave = sample_sum / 15;
                            sample_sum = 0;
                            double delta = 0;
                            for(sample_count = 0; sample_count < 15; sample_count++){
                                delta = Voice_Sample[sample_count] - sample_ave;
                                sample_var += (delta * delta);
                            }
                            sample_var = sample_var/15;
                            System.out.println(Double.toString(sample_var));
                            sample_count = 0;
                            if(sample_var <30){
                                deep_sleep++;
                            }else if(sample_var < 400){
                                light_sleep++;
                                if((hour_system * 60 + minute_system ) >= ( mHour * 60 + mMinute - mDuration)) {
                                    count1++;
                                }else{
                                    count1 = 0;
                                }
                                if(count1 == 3){
                                    isGetVoiceRun = false;
                                    mediaPlayer.start();
                                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(3000);
                                }
                            }else{
                                awake++;
                            }
                        }
                        //onGetVoiceListener.onGetVoice(volume);
                        // 大概一秒十次
                        synchronized (mLock) {
                            try {
                                mLock.wait(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }).start();
    }

    public interface OnGetVoiceListener {
        void onGetVoice(double voice);
    }
    //check the micphone permission
    private static boolean verifyPermissions(SleepActivity activity){
        //Check if we have micphone permission
        int voice_permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        if(voice_permission != PackageManager.PERMISSION_GRANTED){
            //we do not have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        }else{
            return true;
        }
    }
}

