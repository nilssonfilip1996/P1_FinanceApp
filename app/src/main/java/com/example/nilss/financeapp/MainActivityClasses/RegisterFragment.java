package com.example.nilss.financeapp.MainActivityClasses;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.nilss.financeapp.CustomButton;
import com.example.nilss.financeapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private EditText mFirstNameetv, mLastNameetv,mEmailetv, mPasswordetv, mRePasswordetv;
    private CustomButton mRegisterBtn;
    private LogInController mLogInController;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public void setController(LogInController logInController) {
        this.mLogInController = logInController;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        initComponents(view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("registerFragment", Activity.MODE_PRIVATE);
        String firstNameText = sharedPreferences.getString("firstName",null);
        if(firstNameText!=null) {
            mFirstNameetv.setText(firstNameText);
            mLastNameetv.setText(sharedPreferences.getString("lastName", ""));
            mEmailetv.setText(sharedPreferences.getString("email", ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("registerFragment", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName",mFirstNameetv.getText().toString());
        editor.putString("lastName",mLastNameetv.getText().toString());
        editor.putString("email",mEmailetv.getText().toString());
        editor.apply();
    }

    private void initComponents(View view) {
        mFirstNameetv = view.findViewById(R.id.registerFirstNameetv);
        mLastNameetv = view.findViewById(R.id.registerLastNameetv);
        mEmailetv = view.findViewById(R.id.registerEmailetv);
        mPasswordetv = view.findViewById(R.id.registerPasswordetv);
        mRePasswordetv = view.findViewById(R.id.registerPasswordetv2);
        mRegisterBtn = view.findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener((View v)-> {
            mLogInController.verifyNewUserCreation(
                    mFirstNameetv.getText().toString(),
                    mLastNameetv.getText().toString(),
                    mEmailetv.getText().toString(),
                    mPasswordetv.getText().toString(),
                    mRePasswordetv.getText().toString());
        });
    }


    public void setEmailError(String error) {
        mEmailetv.setError(error);
    }
}
