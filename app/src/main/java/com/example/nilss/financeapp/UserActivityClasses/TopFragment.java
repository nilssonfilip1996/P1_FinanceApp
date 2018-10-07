package com.example.nilss.financeapp.UserActivityClasses;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nilss.financeapp.CustomButton;
import com.example.nilss.financeapp.OnCreateViewLIstener;
import com.example.nilss.financeapp.Pojos.User;
import com.example.nilss.financeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends Fragment {
    private TextView mWelcometv, mCurrentBalancetv;
    private CustomButton mLogOutbtn;
    private ControllerInterface mUserController;
    private OnCreateViewLIstener listener;


    public void onInit(OnCreateViewLIstener listener){
        this.listener = listener;
    }

    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_top, container, false);
        initComponents(view);
        if(listener != null){
            listener.onViewsInitialized();
        }
        return view;
    }

    private void initComponents(View view) {
        mWelcometv = view.findViewById(R.id.welcometv);
        mCurrentBalancetv = view.findViewById(R.id.currentBalancetv);
        mLogOutbtn = view.findViewById(R.id.logOutbtn);
        mLogOutbtn.setOnClickListener((View v)->mUserController.logOutBtnPressed());
    }

    public void setController(ControllerInterface userController){
        this.mUserController = userController;
    }


    public void setBalancetv(String s) {
        String balance = "Current balance: " + s + "kr";
        this.mCurrentBalancetv.setText(balance);
    }

    public void setUsertv(String firstName) {
        String text = "Welcome " + firstName;
        this.mWelcometv.setText(text);
    }
}
