package com.example.groupproject;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RetrieveActivity extends AppCompatActivity {
    EditText mET_rid,mET_rsqan,mET_rnpasswd,mET_rrepasswd;
    Spinner spinnerSecurityQuestion;
    DatabaseHelper accountDb;
    Button changePassword;
    protected boolean isAccount, isAnswer, isEqual, isPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        setTitle("Retrieve");
        Bundle bundle = getIntent().getExtras();
        mET_rid = (EditText) findViewById(R.id.ET_rid);
        mET_rid.setText(bundle.getString("Account"));
        mET_rsqan = (EditText) findViewById(R.id.ET_rsqan);
        mET_rnpasswd = (EditText) findViewById(R.id.ET_rnpasswd);
        mET_rrepasswd = (EditText) findViewById(R.id.ET_rrepasswd);
        changePassword = (Button)findViewById(R.id.RetriSubBtn) ;
        accountDb = new DatabaseHelper(this);

        final String[] SecurityQuestionArray;
        SecurityQuestionArray = getResources().getStringArray(R.array.SecurityQuestion);
        spinnerSecurityQuestion = (Spinner) findViewById(R.id.SecurityQuestion);
        ArrayAdapter<String> adapterSecurityQuestion = new ArrayAdapter<String>(this,
                R.layout.dropdown_question, SecurityQuestionArray);
        spinnerSecurityQuestion.setAdapter(adapterSecurityQuestion);
        spinnerSecurityQuestion.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        changePassword.setOnClickListener( new MyListener());
    }
    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(mET_rid.getText().toString().equals("")){
                isAccount = false;
            }else
                isAccount = true;

            if(mET_rsqan.getText().toString().equals("")){
                isAnswer = false;
            }else
                isAnswer = true;
            if(mET_rnpasswd.getText().toString().equals("")){
                isPassword = false;
            }else
                isPassword = true;
            if(mET_rnpasswd.getText().toString().equals(mET_rrepasswd.getText().toString())){
                isEqual = true;
            }else
                isEqual = false;


            if(isAccount){
                Cursor res = accountDb.getData(mET_rid.getText().toString());
                if (res.getCount() == 0) {
                    Toast.makeText(getBaseContext(),
                            "User name does not exist..",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String getAnswer = null;

                while (res.moveToNext()) {
                    getAnswer = res.getString(4);
                    //System.out.println(getPassword);
                    //buffer.append("Account :"+ res.getString(1)+"\n");
                    //buffer.append("Password :"+ res.getString(2)+"\n");
                    //buffer.append("Question :"+ res.getString(3)+"\n");
                    //buffer.append("Answer :"+ res.getString(4)+"\n\n");
                }
                String putAnswer = mET_rsqan.getText().toString();
                if(getAnswer.equals(putAnswer)){
                    if(isPassword){
                        if(isEqual){
                            boolean isUpdate = accountDb.updateData(mET_rid.getText().toString(),
                                    mET_rnpasswd.getText().toString());
                            if (isUpdate){
                                Toast.makeText(getBaseContext(),
                                        "The record is updated." ,
                                        Toast.LENGTH_LONG).show();
                                finish();
                                 } else{
                                Toast.makeText(getBaseContext(),
                                        "The record cannot be updated." ,
                                        Toast.LENGTH_LONG).show();
                                 }
                        }else{
                            Toast.makeText(getBaseContext(),
                                    "Two passwords must be equal" ,
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getBaseContext(),
                                "Please input the password" ,
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),
                            "Please input the correct answer",
                            Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getBaseContext(),
                        "Please input the account",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



}
