package com.example.nilss.financeapp.MainActivityClasses;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nilss.financeapp.CustomButton;
import com.example.nilss.financeapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private CustomButton mLogInBtn;
    private TextView mRegistertv;
    private EditText mEmailetv, mPasswordetv;
    private LogInController mLogInController;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        initComponents(view);
        registerListeners();
        if(savedInstanceState!=null){
            mEmailetv.setText(savedInstanceState.getString("email"));
            //mPasswordetv.setText(savedInstanceState.getString("password"));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginFragment", Activity.MODE_PRIVATE);
        String emailetvText = sharedPreferences.getString("emailetv",null);
        if(emailetvText!=null) {
            mEmailetv.setText(emailetvText);
            //mPasswordetv.setText(sharedPreferences.getString("passwordetv", ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginFragment", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailetv",mEmailetv.getText().toString());
        editor.putString("passwordetv", mPasswordetv.getText().toString());
        editor.apply();
    }

    private void initComponents(View view) {
        mLogInBtn = view.findViewById(R.id.logInBtn);
        mRegistertv = view.findViewById(R.id.registertv);
        mEmailetv = view.findViewById(R.id.logInEmailetv);
        mPasswordetv = view.findViewById(R.id.logInPasswordetv);
    }

    private void registerListeners() {
        mLogInBtn.setOnClickListener((View v)-> {
            mLogInController.logInPressed(mEmailetv.getText().toString(),mPasswordetv.getText().toString());
        });
        mRegistertv.setOnClickListener((View v)-> mLogInController.registerPressed());
    }

    public void setController(LogInController logInController) {
        this.mLogInController = logInController;
    }

    public void setEmailetv(String email) {
        this.mEmailetv.setText(email);
    }

    public void clearPasswordetv() {
        this.mPasswordetv.setText("");
    }
}
