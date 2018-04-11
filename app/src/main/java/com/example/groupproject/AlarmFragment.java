package com.example.groupproject;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment implements TimePickerDialog.OnTimeSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "TitlesFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView tvTime;
    private Button btnSleeping;
    private ImageButton alarmState;
    private boolean mode24Hour = true;
    private boolean bo_alarmState;

    private int mHour, mMinute;

    public AlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreateView()");
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        tvTime = view.findViewById(R.id.tv_time);
        btnSleeping = view.findViewById(R.id.button_sleeping);
        alarmState = view.findViewById(R.id.ib_alarmState);
        loadAlarmState();
        loadTime();


        view.findViewById(R.id.tv_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                new android.app.TimePickerDialog(
                        getActivity(),
                        new android.app.TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                Log.d("Original", "Got clicked");
                                //timeSet.putInt("hour",hour); //不能在这里使用，会报错
                                //timeSet.putInt("minute",minute);
                                mHour = hour;
                                mMinute = minute;
                                showTime(mHour,mMinute);
                                saveTime(mHour,mMinute);
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        mode24Hour
                ).show();
            }
        });
        btnSleeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSleeping = new Intent(getActivity(),SleepActivity.class);
                Bundle timeSet = new Bundle();
                timeSet.putInt("hour",mHour);
                timeSet.putInt("minute",mMinute);
                timeSet.putInt("Duration",30);
                goSleeping.putExtras(timeSet);
                getActivity().startActivityForResult(goSleeping,2);
            }
        });


        alarmState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bo_alarmState){
                    alarmState.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_closealarm));
                    tvTime.setText("Alarm is closed");
                    btnSleeping.setEnabled(false);
                    bo_alarmState = false;

                }else{
                    alarmState.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_openalarm));
                    showTime(mHour,mMinute);
                    btnSleeping.setEnabled(true);
                    bo_alarmState = true;
                }
                saveAlarmState(bo_alarmState);
            }
        });
        return view;

    }

    public void showTime( int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+":"+minuteString;
        tvTime.setText(time);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
        super.onAttach(context);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
        super.onActivityCreated(savedState);

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
    }

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+"h"+minuteString+"m";
        tvTime.setText(time);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /**********
     save data
     **********/
    public void saveTime(int hour,int minute){
        SharedPreferences saveTime = this.getActivity().getSharedPreferences("Time", Context.MODE_PRIVATE);
        saveTime.edit().putInt("hour", hour).apply();
        saveTime.edit().putInt("minute", minute).apply();
    }
    public void loadTime(){
        SharedPreferences saveTime = this.getActivity().getSharedPreferences("Time", Context.MODE_PRIVATE);
        mHour = saveTime.getInt("hour",0);
        mMinute = saveTime.getInt("minute",0);
        if(bo_alarmState) {
            tvTime.setText((mHour < 10? String.valueOf("0"+mHour):String.valueOf(""+mHour))+ ":"
                    +(mMinute<10? String.valueOf("0"+mMinute): String.valueOf(""+mMinute)));
        }else{
            tvTime.setText("Alarm is closed");
        }
    }

    public void saveAlarmState(boolean state){
        SharedPreferences saveAlarmState = this.getActivity().getSharedPreferences("AlarmState", Context.MODE_PRIVATE);
        saveAlarmState.edit().putBoolean("state", state).apply();
    }
    public void loadAlarmState(){
        SharedPreferences saveAlarmState = this.getActivity().getSharedPreferences("AlarmState", Context.MODE_PRIVATE);
        if(saveAlarmState.getBoolean("state",true)){
            bo_alarmState = saveAlarmState.getBoolean("state",true);
            alarmState.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_openalarm));
        }else{
            bo_alarmState = saveAlarmState.getBoolean("state",true);
            alarmState.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_closealarm));
        }
    }

}
