package com.example.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private AlarmFragment fragmentAlarm;
    private ReportFragment fragmentReport;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    private Bundle information = new Bundle();
    private boolean nightMode;


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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragments();


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
                    if (lastShowFragment != 0) {
                        switchFragment(lastShowFragment, 0);
                        lastShowFragment = 0;
                    }
                    return true;
                case R.id.nav_report:
                    if (lastShowFragment != 1) {
                        switchFragment(lastShowFragment, 1);
                        lastShowFragment = 1;
                    }
                    return true;
                default:
                    fragmentClass = AlarmFragment.class;
            }
            return false;
        }
    };
    public void switchFragment(int lastIndex,int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.fragment_container, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }
    private void initFragments() {
        fragmentAlarm = new AlarmFragment();
        fragmentReport = new ReportFragment();
        fragments = new Fragment[]{fragmentAlarm, fragmentReport};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragmentAlarm)
                .show(fragmentAlarm)
                .commit();
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
            startActivity(Setting);
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
                    System.out.println(information);

                }
            }
        }
        if(requestCode == 2){
            switch (resultCode){
                case 2:{
                    Bundle sleepTime = data.getExtras();
                    int deep = sleepTime.getInt("deepsleep");
                    int light = sleepTime.getInt("lightsleep");
                    int awake = sleepTime.getInt("awaketime");
                    System.out.println(String.valueOf(deep)+" "+String.valueOf(light)+" "+String.valueOf(awake));
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
    @Override
    protected void onStart() {
        super.onStart();
        loadPersonalNightMode();}
}