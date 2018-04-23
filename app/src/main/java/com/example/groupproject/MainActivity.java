package com.example.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import com.example.groupproject.ReportFragment;
import com.example.groupproject.AlarmFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private AlarmFragment fragmentAlarm;
    private ReportFragment fragmentReport;
    private ImageView userIcon;
    private TextView userName;
    private String mUserName;
    private Bundle information = new Bundle();
    private boolean nightMode, isLogin;
    private Bundle sleepTime , sound;
    private int mDeep = 0,mLight = 0,mAwake = 0,soundIndex,rangeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle("Home");

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawerOpen,R.string.drawerClose);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        userIcon = (ImageView)headerView.findViewById(R.id.user_icon);
        userName = (TextView) headerView.findViewById(R.id.user_name);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(MainActivity.this,LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin",isLogin);
                login.putExtras(bundle);
                startActivityForResult(login,1);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadSleepData();
        sleepTime = new Bundle();
        sleepTime.putInt("deepsleep",mDeep);
        sleepTime.putInt("lightsleep",mLight);
        sleepTime.putInt("awaketime",mAwake);
        setSelection(0);

    }

    /*
动态切换Item图标
 */
/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if (nightMode) {
            menu.findItem(R.id.nav_night).setIcon(
                    R.drawable.ic_action_day);
            menu.findItem(R.id.nav_night).setCheckable(false);
        } else {
            menu.findItem(R.id.nav_night).setIcon(R.drawable.ic_action_night);
            menu.findItem(R.id.nav_night).setCheckable(true);
        }
        // getSupportMenuInflater().inflate(R.menu.book_detail, menu);
        return super.onPrepareOptionsMenu(menu);
    }*/


    @Override
    public void onAttachFragment(Fragment fragment){

        //当前的界面的保存状态，只是重新让新的Fragment指向了原本未被销毁的fragment，它就是onAttach方法对应的Fragment对象
        if(fragmentAlarm == null && fragment instanceof AlarmFragment){
            fragmentAlarm = (AlarmFragment)fragment;
        }else if(fragmentReport == null && fragment instanceof ReportFragment){
            fragmentReport = (ReportFragment)fragment;
        }
    }
    /*
    关于BottomNavigation 的选择
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragment = null;
            Class fragmentClass;
            switch (item.getItemId()) {
                case R.id.nav_alarm:
                    setSelection(0);
                    return true;
                case R.id.nav_report:
                    setSelection(1);
                    return true;
            }
            return false;
        }
    };

    private void setSelection(int position){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction mfragmentTransaction = fm.beginTransaction();
        hideAllFragments(mfragmentTransaction);

        switch (position){
            case 0:
                if (fragmentAlarm == null) {
                    fragmentAlarm = new AlarmFragment();
                    fragmentAlarm.setArguments(sound);
                    mfragmentTransaction.add(R.id.fragment_container, fragmentAlarm);
                }else {
                    fragmentAlarm.setArguments(sound);
                    mfragmentTransaction.show(fragmentAlarm);
                }
                break;
            case 1:
                if (fragmentReport == null) {
                    fragmentReport = new ReportFragment();
                    fragmentReport.setArguments(sleepTime);
                    mfragmentTransaction.add(R.id.fragment_container, fragmentReport);
                }else {
                    fragmentReport.setArguments(sleepTime);
                    mfragmentTransaction.show(fragmentReport);
                }
                break;
            default:
                break;
        }
        mfragmentTransaction.commit();
    }

    private void hideAllFragments(FragmentTransaction ft){

        if (fragmentAlarm != null) {
            ft.hide(fragmentAlarm);
        }
        if (fragmentReport != null) {
            ft.hide(fragmentReport);
        }
    }

    /*
    左上角选择Drawer的动作
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*
    Drawer 的选择
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)  {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id ==R.id.nav_information){
            Intent Information = new Intent(this,InformationActivity.class);
            startActivityForResult(Information,1);
        }else if(id == R.id.nav_setting){
            Intent Setting = new Intent(this,SettingActivity.class);
            startActivityForResult(Setting,1);
        }else if(id == R.id.nav_about){
            Intent About = new Intent(this,AboutActivity.class);
            startActivity(About);
        }
        else if(id == R.id.nav_night){
            if(nightMode){
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);//切换日间模式
                nightMode = false;
                recreate();
            }else{
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);//切换夜间模式
                nightMode = true;
                recreate();
            }
            savePersonalNightMode(nightMode);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            switch (resultCode) {
                case 1: {
                    Bundle personalInformation = data.getExtras();
                    boolean sex = personalInformation.getBoolean("sex");
                    int age = personalInformation.getInt("age");
                    Double BMI = personalInformation.getDouble("BMI");
                    if (sex) {
                        information.putString("sex", "male");
                    } else {
                        information.putString("sex", "female");
                    }
                    information.putInt("age", age);
                    information.putDouble("BMI", BMI);

                }
                case 2:{
                    Bundle loginState = data.getExtras();
                    isLogin = loginState.getBoolean("isLogin");
                    saveLoginState(isLogin,loginState.getString("userName"));
                    if(isLogin){
                        userIcon.setImageResource(R.mipmap.ic_head);
                        userName.setText(loginState.getString("userName"));
                    }else{
                        userIcon.setImageResource(R.drawable.ic_launcher);
                        userName.setText(loginState.getString("userName"));
                    }
                }
                case 3:{
                    Bundle settingSelect = data.getExtras();
                    soundIndex = settingSelect.getInt("soundIndex");
                    rangeIndex = settingSelect.getInt("rangeIndex");
                    saveSound(soundIndex,rangeIndex);
                }
            }
        }
        //requestCode == 2 是从AlarmFragment 请求的
        if(requestCode == 2){
            switch (resultCode){
                case 2:{
                    sleepTime = data.getExtras();
                    int deep = sleepTime.getInt("deepsleep");
                    int light =sleepTime.getInt("lightsleep");
                    int awake = sleepTime.getInt("awaketime");
                    sleepTime.putInt("deepsleep",deep);
                    sleepTime.putInt("lightsleep",light);
                    sleepTime.putInt("awaketime",awake);
                    saveSleepData(deep,light,awake);
                }
            }
        }
    }
    /**********
     save nightMode
     **********/
    public void savePersonalNightMode(boolean nightMode){
        SharedPreferences pref_nightMode = getSharedPreferences("NightMode", MODE_PRIVATE);
        pref_nightMode.edit().putBoolean("nightMode", nightMode).apply();

    }
    public void loadPersonalNightMode(){
        SharedPreferences pref_infor = getSharedPreferences("NightMode", MODE_PRIVATE);
        nightMode = pref_infor.getBoolean("nightMode",true);
        if(nightMode){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);//切换日间模式
        }else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);//切换夜间模式
        }
    }

    public void saveSleepData(int mDeep,int mLight,int mAwake){
        SharedPreferences pref_SleepData = getSharedPreferences("SleepData", MODE_PRIVATE);
        pref_SleepData.edit().putInt("mDeep", mDeep).apply();
        pref_SleepData.edit().putInt("mLight", mLight).apply();
        pref_SleepData.edit().putInt("mAwake", mAwake).apply();

    }
    public void loadSleepData(){
        SharedPreferences pref_SleepData = getSharedPreferences("SleepData", MODE_PRIVATE);
        mDeep = pref_SleepData.getInt("mDeep",0);
        mLight = pref_SleepData.getInt("mLight",0);
        mAwake = pref_SleepData.getInt("mAwake",0);

    }
    public void saveLoginState(boolean isLogin,String userName){
        SharedPreferences pref_loginState = getSharedPreferences("LoginState", MODE_PRIVATE);
        pref_loginState.edit().putBoolean("loginState",isLogin).apply();
        pref_loginState.edit().putString("userName",userName).apply();
    }
    public void loadLoginState(){
        SharedPreferences pref_loginState = getSharedPreferences("LoginState", MODE_PRIVATE);
        isLogin = pref_loginState.getBoolean("loginState",false);
        mUserName = pref_loginState.getString("userName","user name");
        if(isLogin){
            userIcon.setImageResource(R.mipmap.ic_head);
            userName.setText(mUserName);
        }else{
            userIcon.setImageResource(R.drawable.ic_launcher);
            userName.setText(mUserName);
        }
    }
    public void saveSound( int soundIndex,int rangeIndex) {
        SharedPreferences pref = getSharedPreferences("setting_main", MODE_PRIVATE);
        pref.edit().putInt("soundIndex",soundIndex).apply();
        pref.edit().putInt("rangeIndex",rangeIndex).apply();

    }
    public void loadSound() {
        SharedPreferences pref = getSharedPreferences("setting_main", MODE_PRIVATE);
        soundIndex = pref.getInt("soundIndex", 0);
        rangeIndex = pref.getInt("rangeIndex", 2);
        setSoundIndex(soundIndex);
        setRangeIndex(rangeIndex);

        //System.out.println("load  "+Integer.toString(rangeIndex));

    }
    @Override
    protected void onStart() {
        super.onStart();
        loadLoginState();
        loadPersonalNightMode();
        loadSound();
    }
    public int getSoundIndex() {
        return soundIndex;
    }
    public void setSoundIndex(int soundIndex){
        this.soundIndex = soundIndex;
    }
    public int getRangeIndex() {
        return rangeIndex;
    }
    public void setRangeIndex(int rangeIndex){
        this.rangeIndex = rangeIndex;
    }
}