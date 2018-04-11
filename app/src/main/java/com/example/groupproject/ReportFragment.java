package com.example.groupproject;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TitlesFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinkedHashMap kindsMap = new LinkedHashMap<String, Integer>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private PieChatView pieChatView ;
    private boolean if_drawPie;
    private int mDeep,mLight,mAwake;
    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
             if (!hidden) {
                    Log.i(TAG, getClass().getSimpleName() + ":enter onHiddenChanged()");
                    Bundle bundle = getArguments();
                    int deep = bundle.getInt("deepsleep");
                    int light = bundle.getInt("lightsleep");
                    int awake = bundle.getInt("awaketime");
                 if((deep == 0)&&(light == 0)&&(awake == 0)){
                     Toast.makeText(getActivity(), "There is no data!", Toast.LENGTH_LONG).show();
                 }else {
                     if((mDeep == deep)&&(mLight == light)&&(mAwake == awake)) {
                         Toast.makeText(getActivity(), "The data are not changed!", Toast.LENGTH_LONG).show();
                     }else{
                         mDeep = deep;
                         mLight = light;
                         mAwake = awake;
                         drawPie(deep,light,awake);
                     }
                 }

             }else{
                    Log.i(TAG, getClass().getSimpleName() + ":outoff onHiddenChanged()");
             }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onDetach()");
        super.onAttach(context);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onCreateView()");
        if_drawPie = false;
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        pieChatView = view.findViewById(R.id.pie);
        Bundle mBundle = getArguments();

        mDeep = mBundle.getInt("deepsleep");
        mLight = mBundle.getInt("lightsleep");
        mAwake = mBundle.getInt("awaketime");
        if((mDeep == 0)&&(mLight == 0)&&(mAwake == 0)){
            Toast.makeText(getActivity(), "There is no data!", Toast.LENGTH_LONG).show();
        }else{
            drawPie(mDeep,mLight,mAwake);
            if_drawPie =true;
        }

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onActivityCreated()");
        super.onActivityCreated(savedState);

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
    }


    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onDestroyView()");
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onDestroy()");
        super.onDestroy();
    }
    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered Report onDetach()");
        super.onDetach();
        mListener = null;
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
    private void drawPie(int deep,int light, int awake){

        kindsMap.put("Deep sleep", deep);
        kindsMap.put("Light sleep", light);
        kindsMap.put("Awake", awake);

        for (int i = 1; i <= 40; i++){
            int r= (new Random().nextInt(100)+10)*i;
            int g= (new Random().nextInt(100)+10)*3*i;
            int b= (new Random().nextInt(100)+10)*2*i;
            int color = Color.rgb(r,g,b);
            if(Math.abs(r-g)>10 && Math.abs(r-b)>10 && Math.abs(b-g)>10){
                colors.add(color);
            }
        }
        pieChatView.setCenterTitle("Sleep cycle");
        pieChatView.setDataMap(kindsMap);
        pieChatView.setColors(colors);
        pieChatView.setMinAngle(50);
        pieChatView.startDraw();
    }
}
