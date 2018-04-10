package com.example.groupproject;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(AboutActivity.this, "New Rating: "+ rating, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void SendEmail (View view){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:xiaopenli4-c@my.cityu.edu.cn"));
        data.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.mail_subject));
        data.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.mail_content));
        startActivity(data);
    }
}