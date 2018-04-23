package com.example.groupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingActivity extends AppCompatActivity {

    Spinner sp_sounds,sp_wakeRange;
    int sound,range;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Setting");

        final String[] soundsList;
        final String[]  rangeList;
        soundsList = getResources().getStringArray(R.array.soundList);
        rangeList = getResources().getStringArray(R.array.wakeRange);

        ArrayAdapter<String> adapterSoundsList = new ArrayAdapter<String>(this,
                R.layout.dropdown_question, soundsList);

        ArrayAdapter<String> adapterRangeList = new ArrayAdapter<String>(this,
                R.layout.dropdown_question, rangeList);

        sp_sounds = (Spinner)findViewById(R.id.sp_sound) ;
        sp_sounds.setAdapter(adapterSoundsList);
        sp_sounds.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                sound = index;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        sp_wakeRange = (Spinner)findViewById(R.id.sp_range);
        sp_wakeRange.setAdapter(adapterRangeList);
        sp_wakeRange.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                range = index;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void savePreferences( int soundIndex,int rangeIndex) {
        SharedPreferences pref = getSharedPreferences("Preferences_setting", MODE_PRIVATE);
        pref.edit().putInt("soundIndex",soundIndex).apply();
        pref.edit().putInt("rangeIndex",rangeIndex).apply();
    }
    private void loadPreferences() {

        SharedPreferences pref = getSharedPreferences("Preferences_setting", MODE_PRIVATE);
        sp_sounds.setSelection(pref.getInt("soundIndex", 0));
        sp_wakeRange.setSelection(pref.getInt("rangeIndex", 2));
        sound =pref.getInt("soundIndex", 0);
        range =pref.getInt("rangeIndex", 2);
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    @Override
    public void onBackPressed() {

        savePreferences(sound,range);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("soundIndex",sound);
        bundle.putInt("rangeIndex",range);
        intent.putExtras(bundle);
        setResult(3,intent);
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            savePreferences(sound,range);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("soundIndex",sound);
            bundle.putInt("rangeIndex",range);
            intent.putExtras(bundle);
            setResult(3,intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
