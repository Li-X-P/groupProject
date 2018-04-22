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

    Spinner sp_sounds;
    int sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Setting");

        final String[] soundsList;
        soundsList = getResources().getStringArray(R.array.soundList);
        ArrayAdapter<String> adapterSoundsList = new ArrayAdapter<String>(this,
                R.layout.dropdown_question, soundsList);
        sp_sounds = (Spinner)findViewById(R.id.sp_sound) ;
        sp_sounds.setAdapter(adapterSoundsList);

        sp_sounds.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                savePreferences(index);
                sound = index;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void savePreferences( int soundIndex) {
        SharedPreferences pref = getSharedPreferences("sound_setting", MODE_PRIVATE);
        pref.edit().putInt("soundIndex",soundIndex).apply();
    }
    private void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("sound_setting", MODE_PRIVATE);
        sp_sounds.setSelection(pref.getInt("soundIndex", 0));
        sound =pref.getInt("soundIndex", 0);
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("index",sound);
        intent.putExtras(bundle);
        setResult(3,intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("index",sound);
            intent.putExtras(bundle);
            setResult(3,intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
