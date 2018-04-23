package com.example.groupproject;

import android.content.Intent;
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

public class CreateUserActivity extends AppCompatActivity {
    EditText mET_account, mET_password, mET_rePassword, mET_answer;
    Spinner spinnerSecurityQuestion;
    Button createUser;
    protected String account, password, rePassword,question, answer;
    private boolean isAccount ,isPassword,isAnswer,isQuestion;
    DatabaseHelper userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setTitle("Create User");

        mET_account = (EditText) findViewById(R.id.ET_cid);
        mET_password = (EditText) findViewById(R.id.ET_cpasswd);
        mET_rePassword = (EditText) findViewById(R.id.ET_crppasswd);
        mET_answer = (EditText) findViewById(R.id.ET_csqan);
        spinnerSecurityQuestion = (Spinner) findViewById(R.id.SecurityQuestion);
        userDb = new DatabaseHelper(this);
        createUser = (Button)findViewById(R.id.CreateSubBtn);

        final String[] SecurityQuestionArray;
        SecurityQuestionArray = getResources().getStringArray(R.array.SecurityQuestion);
        ArrayAdapter<String> adapterSecurityQuestion = new ArrayAdapter<String>(this,
                R.layout.dropdown_question, SecurityQuestionArray);
        spinnerSecurityQuestion.setAdapter(adapterSecurityQuestion);
        spinnerSecurityQuestion.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                question = SecurityQuestionArray[index];
                if(question.equals("")){
                    isQuestion = false;
                }else
                    isQuestion = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        createUser.setOnClickListener( new MyListener());
    }
        private class MyListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                account = mET_account.getText().toString();
                if(account.equals("")){
                    isAccount = false;
                }else
                    isAccount = true;

                password = mET_password.getText().toString();
                if(password.equals("")){
                    isPassword = false;
                }else
                    isPassword = true;

                rePassword = mET_rePassword.getText().toString();
                answer = mET_answer.getText().toString();
                if(answer.equals("")){
                    isAnswer = false;
                }else
                    isAnswer =true;

                if(password.equals(rePassword)) {
                    if (isAccount) {
                        if(isPassword){
                            if(isQuestion){
                                if(isAnswer){
                                    if(password.length()>=9){
                                        boolean isInserted = userDb.insertData(account, password, question, answer);
                                        if (isInserted) {
                                            Toast.makeText(getBaseContext(),
                                                    "you have created account ",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Account",mET_account.getText().toString());
                                            intent.putExtras(bundle);
                                            setResult(1,intent);
                                            finish();

                                        } else {
                                            Toast.makeText(getBaseContext(),
                                                    "account can not be created ",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(getBaseContext(),
                                                "The password should be more than 9 characters" ,
                                                Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(getBaseContext(),
                                            "Please input the answer" ,
                                            Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getBaseContext(),
                                        "Please select the question" ,
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getBaseContext(),
                                    "Please input the password" ,
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(),
                                "Please input the user name" ,
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),
                            "Two passwords must be equal" ,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
