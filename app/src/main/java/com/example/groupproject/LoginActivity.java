package com.example.groupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    Button CreateButton;
    Button ForgetButton;
    Button LoginButton, LogoutButton;
    private boolean isAccount, isPassword,isLogin, isQQ;
    DatabaseHelper userDb;
    EditText mET_password, mET_account;

    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1106857284";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private ImageView head;
    private boolean ifCheck;
    private CheckBox cb_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTencent = Tencent.createInstance(APP_ID,LoginActivity.this.getApplicationContext());
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setTitle("Login");

        DisplayImageOptions op=new DisplayImageOptions.Builder().build();
        ImageLoaderConfiguration con=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(op)
                .build();
        ImageLoader.getInstance().init(con);

        if(bundle.getBoolean("isLogin")) {
            setContentView(R.layout.activity_haslogin);
            head = (ImageView) findViewById(R.id.imageView3);
            LogoutButton = (Button)findViewById(R.id.logout);

            LogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLogin = false;
                    isQQ = false;
                    mTencent.logout(LoginActivity.this);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isLogin",isLogin);
                    bundle.putString("userName","user name");
                    intent.putExtras(bundle);
                    setResult(2,intent);
                    finish();
                }
            });
        }
        else{
            setContentView(R.layout.activity_login);
            userDb = new DatabaseHelper(this);
            mET_account = (EditText)findViewById(R.id.ET_id) ;
            mET_password = (EditText)findViewById(R.id.ET_password) ;
            cb_remember = (CheckBox)findViewById(R.id.cb_remember) ;
            cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ifCheck =true;
                        saveCheckState(ifCheck);

                    }else{
                        ifCheck = false;
                        saveCheckState(ifCheck);
                    }
                }
            });
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

                                //buffer.append("Account :"+ res.getString(1)+"\n");
                                //buffer.append("Password :"+ res.getString(2)+"\n");
                                //buffer.append("Question :"+ res.getString(3)+"\n");
                                //buffer.append("Answer :"+ res.getString(4)+"\n\n");
                            }
                            String putPassword = mET_password.getText().toString();
                            if(putPassword.equals(getPassword)){
                                isLogin = true;
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isLogin",isLogin);
                                bundle.putString("userName",getUserName);
                                intent.putExtras(bundle);
                                setResult(2,intent);
                                if(ifCheck){
                                    saveAccount(mET_account.getText().toString(),mET_password.getText().toString());
                                }
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


    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "Log in successfully!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {

                        isLogin = true;
                        isQQ = true;
                        Log.e(TAG,"登录成功"+response.toString());
                        JSONObject oo= (JSONObject) response;

                        try {
                            String   na = oo.getString("nickname");
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isLogin",isLogin);
                            bundle.putString("userName",na);
                            intent.putExtras(bundle);
                            setResult(2,intent);

                        /*    String url=oo.getString("figureurl_2");
                            System.out.println("===解析的url"+url);
                            ImageLoader.getInstance().displayImage(url,head);*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        finish();
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "Log in failed!", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "Cancel log in by Tencent QQ", Toast.LENGTH_SHORT).show();

        }

    }
    public void buttonLogin(View v){
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */

        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(LoginActivity.this,"all", mIUiListener);
    }
    /*
    要想处理从其他Activity 中返回来的数据 在这里处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
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
    private void saveAccount(String name, String password){
        SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
        pref.edit().putString("name",name).apply();
        pref.edit().putString("password",password).apply();
    }
    private void loadAccount(){
        SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
        if(ifCheck){
            mET_account.setText(pref.getString("name",""));
            mET_password.setText(pref.getString("password",""));
        }else{
            mET_account.setText("");
            mET_password.setText("");
        }

    }
    private void saveCheckState(boolean state){
        SharedPreferences pref = getSharedPreferences("CheckState", MODE_PRIVATE);
        pref.edit().putBoolean("state",state).apply();
    }
    private void loadCheckState(){
        SharedPreferences pref = getSharedPreferences("CheckState", MODE_PRIVATE);
        ifCheck = pref.getBoolean("state",false);
        if(ifCheck) {
            cb_remember.setChecked(true);
        }
        else {
            cb_remember.setChecked(false);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if(bundle.getBoolean("isLogin")){

        }else{
            loadCheckState();
            loadAccount();
        }

    }
}
