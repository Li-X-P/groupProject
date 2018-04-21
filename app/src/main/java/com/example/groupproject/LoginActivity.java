package com.example.groupproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    Button CreateButton;
    Button ForgetButton;
    Button LoginButton, LogoutButton;
    private boolean isAccount, isPassword,isLgoin;
    DatabaseHelper userDb;
    EditText mET_password, mET_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setTitle("Login");
        if(bundle.getBoolean("isLogin")) {
            setContentView(R.layout.activity_haslogin);
            System.out.println("true");
            LogoutButton = (Button)findViewById(R.id.logout);
            LogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLgoin = false;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isLogin",isLgoin);
                    bundle.putString("userName","user name");
                    intent.putExtras(bundle);
                    setResult(2,intent);
                    finish();
                }
            });
        }
        else{
            setContentView(R.layout.activity_login);
            System.out.println("false");
            userDb = new DatabaseHelper(this);
            mET_account = (EditText)findViewById(R.id.ET_id) ;
            mET_password = (EditText)findViewById(R.id.ET_password) ;
            CreateButton = (Button) findViewById(R.id.CreatBtn);
            CreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), CreateUserActivity.class);
                    startActivityForResult(intent,3);

                }
            });
            ForgetButton = (Button) findViewById(R.id.ForgetBtn);
            ForgetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), RetrieveActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Account",mET_account.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            LoginButton = (Button) findViewById(R.id.LogBtn);
            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mET_account.getText().toString().equals("")){
                        isAccount = false;
                    }else
                        isAccount = true;

                    if(mET_password.getText().toString().equals("")){
                        isPassword = false;
                    }else
                        isPassword = true;
                    if(isAccount){
                        if(isPassword){
                            Cursor res = userDb.getData(mET_account.getText().toString());
                            if (res.getCount() == 0) {
                                Toast.makeText(getBaseContext(),
                                        "User name does not exist..",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            //StringBuffer buffer = new StringBuffer();
                            String getPassword = null;
                            String getUserName = null;
                            while (res.moveToNext()) {
                                getPassword = res.getString(2);
                                getUserName = res.getString(1);
                                //System.out.println(getPassword);
                                //buffer.append("Account :"+ res.getString(1)+"\n");
                                //buffer.append("Password :"+ res.getString(2)+"\n");
                                //buffer.append("Question :"+ res.getString(3)+"\n");
                                //buffer.append("Answer :"+ res.getString(4)+"\n\n");
                            }
                            String putPassword = mET_password.getText().toString();
                            if(putPassword.equals(getPassword)){
                                isLgoin = true;
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isLogin",isLgoin);
                                bundle.putString("userName",getUserName);
                                intent.putExtras(bundle);
                                setResult(2,intent);
                                finish();
                            }else{
                                Toast.makeText(getBaseContext(),
                                        "Please input the correct password",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getBaseContext(),
                                    "Please input the password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getBaseContext(),
                                "Please input the account",
                                Toast.LENGTH_LONG).show();
                    }

                }

            });

        }

    }

    /*
    要想处理从其他Activity 中返回来的数据 在这里处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3) {
            switch (resultCode) {
                case 1: {                               //处理从CreateActivity 中返回的数据
                    Bundle bundle = data.getExtras();
                    mET_account.setText(bundle.getString("Account"));
                }
                case 2: {                               //处理从RetrieveActivity 中返回的数据

                }
            }
        }
    }
}
