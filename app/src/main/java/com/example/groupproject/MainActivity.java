package com.example.groupproject;

import android.app.Activity;
import android.content.Intent;
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
    }
}