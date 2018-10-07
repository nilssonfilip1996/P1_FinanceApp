package com.example.nilss.financeapp.MainActivityClasses;

/**
 * Author: Filip Nilsson
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.nilss.financeapp.MainActivityClasses.LogInController;
import com.example.nilss.financeapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogInController logInController = new LogInController(this);
    }


    public void setFragment(Fragment fragment, boolean addToBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        ft.replace(R.id.fragmentContainer,fragment);
        if(addToBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();
        //fm.executePendingTransactions();        //Muy importante
    }

    public void popFragmentFromBackStack(){
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
