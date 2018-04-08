package com.example.groupproject;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InformationActivity extends AppCompatActivity {

    boolean sex;
    int age;
    EditText edAge;
    Button inforApply;
    RadioGroup radioSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setTitle("Personal Information");

        edAge = (EditText)findViewById(R.id.etAge);
        inforApply = (Button)findViewById(R.id.info_apply);
        inforApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edAge.getText().toString().equals("")){
                    Toast.makeText(InformationActivity.this, R.string.inPutWarning,
                            Toast.LENGTH_LONG).show();
                }else
                age =  Integer.parseInt(edAge.getText().toString());
                savePersonalInfor(sex,age);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean("sex",sex);
                bundle.putInt("age",age);
                intent.putExtras(bundle);
                setResult(1,intent);
                finish();
            }
        });
        radioSex = (RadioGroup)findViewById(R.id.radioSex);
        radioSex.setOnCheckedChangeListener(radioGroupListener);
        
    }
    RadioGroup.OnCheckedChangeListener radioGroupListener=new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checked) {
            // TODO Auto-generated method stub
            //设置TextView的内容显示CheckBox的选择结果
            switch (radioSex.getCheckedRadioButtonId()){
                case R.id.radio_male:
                    sex = true;
                    break;
                case R.id.radio_female:
                    sex = false;
                    break;
            }

        }
    };
    public void setRadioSex(boolean sex){
        if(sex){
            radioSex.check(R.id.radio_male);
        }else{
            radioSex.check(R.id.radio_female);
        }
    }
    /**********
     save data
     **********/
    public void savePersonalInfor(boolean sex,int age){
        SharedPreferences pref_infor = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        pref_infor.edit().putBoolean("sex", sex).apply();
        pref_infor.edit().putInt("age", age).apply();
    }
    public void loadPersonalInfor(){
        SharedPreferences pref_infor = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        edAge.setText(String.valueOf(pref_infor.getInt("age",0)));
        setRadioSex(pref_infor.getBoolean("sex",false));
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadPersonalInfor();}
}
