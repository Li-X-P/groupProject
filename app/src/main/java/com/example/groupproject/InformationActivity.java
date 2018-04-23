package com.example.groupproject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    int age,age_index;
    double height;
    double weight;
    String[] ageTips;
    EditText etAge;
    EditText etHeight;
    EditText etWeight;
    Button inforApply;
    RadioGroup radioSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setTitle("Personal Information");

        ageTips = getResources().getStringArray(R.array.ageTips);
        etAge = (EditText)findViewById(R.id.et_Age);
        etHeight = (EditText)findViewById(R.id.et_height);
        etWeight = (EditText)findViewById(R.id.et_weight);
        inforApply = (Button)findViewById(R.id.info_apply);
        inforApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAge.getText().toString().equals("")){
                    Toast.makeText(InformationActivity.this, R.string.ageWarning,
                            Toast.LENGTH_LONG).show();
                }else if(etHeight.getText().toString().equals("")){
                    Toast.makeText(InformationActivity.this, R.string.heightWarning,
                            Toast.LENGTH_LONG).show();
                }else if(etWeight.getText().toString().equals("")){
                    Toast.makeText(InformationActivity.this, R.string.weightWarning,
                            Toast.LENGTH_LONG).show();
                }else {
                age =  Integer.parseInt(etAge.getText().toString());
                height = Double.parseDouble(etHeight.getText().toString());
                weight = Double.parseDouble(etWeight.getText().toString());
                }

                savePersonalInfor(sex,age,etHeight.getText().toString(),etWeight.getText().toString());
                double BMI = weight /((height/100)*(height/100));

                if(age == 0){
                    age_index = 0;
                } else if(age<=3){
                    age_index = 1;
                }else if(age<=12){
                    age_index = 2;
                }else if(age<=29){
                    age_index = 3;
                }else if(age<=60){
                    age_index = 4;
                }else{
                    age_index = 5;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean("sex",sex);
                bundle.putInt("age",age);
                bundle.putDouble("BMI",BMI);
                intent.putExtras(bundle);
                setResult(1,intent);

                AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                builder.setTitle("Tips");
                builder.setMessage(ageTips[age_index]);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(age_index == 0)
                                {

                                }else
                                    finish();
                            }
                        });
                builder.create();
                builder.show();

            }
        });
        radioSex = (RadioGroup)findViewById(R.id.radio_Sex);
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
    public void savePersonalInfor(boolean sex,int age,String height,String weight){
        SharedPreferences pref_infor = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        pref_infor.edit().putBoolean("sex", sex).apply();
        pref_infor.edit().putInt("age", age).apply();
        pref_infor.edit().putString("height", height).apply();
        pref_infor.edit().putString("weight", weight).apply();
    }
    public void loadPersonalInfor(){
        SharedPreferences pref_infor = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        etAge.setText(String.valueOf(pref_infor.getInt("age",0)));
        setRadioSex(pref_infor.getBoolean("sex",false));
        etHeight.setText(pref_infor.getString("height","0"));
        etWeight.setText(pref_infor.getString("weight","0"));

    }
    @Override
    protected void onStart() {
        super.onStart();
        loadPersonalInfor();}
}
