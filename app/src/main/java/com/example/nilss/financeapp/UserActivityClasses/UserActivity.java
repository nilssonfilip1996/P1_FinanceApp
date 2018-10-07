package com.example.nilss.financeapp.UserActivityClasses;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.nilss.financeapp.CustomButton;
import com.example.nilss.financeapp.R;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Calendar;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "USERACTIVITY";
    private CustomButton mToggleBtn, mSearchBtn, mClearSearchBtn, mAddTransactionBtn;
    private EditText mDateFrometv, mDateToetv;
    private DatePickerDialog.OnDateSetListener mDateFromSetListener;
    private DatePickerDialog.OnDateSetListener mDateToSetListener;
    UserController userController;
    UserControllerLandscape userControllerLandscape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle bundle = getIntent().getExtras();
        //Portrait mode!
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            TopFragment tf = new TopFragment();
            setFragment(tf, R.id.fragmentTopContainer, false);
            userController = new UserController(this, tf, bundle.getString("userEmail"));
            initComponents();
        }
        //Landscape mode!
        else{
            TopFragment tf = new TopFragment();
            setFragment(tf, R.id.fragmentTopContainerLandscape,false);
            PieChart pieIncome = findViewById(R.id.pieChart1);
            PieChart pieExpense = findViewById(R.id.pieChart2);
            userControllerLandscape = new UserControllerLandscape(this, tf, pieIncome, pieExpense, bundle.getString("userEmail"));
        }
    }

    private void initComponents() {
        this.mToggleBtn = findViewById(R.id.toggleBtn);
        this.mSearchBtn = findViewById(R.id.searchBtn);
        this.mClearSearchBtn = findViewById(R.id.clearBtn);
        this.mAddTransactionBtn = findViewById(R.id.addTransactionBtn);
        this.mDateFrometv = findViewById(R.id.seachFrometv);
        this.mDateToetv = findViewById(R.id.seachtoetv);
        initListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //When pressing back in transactionfragment.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private void initListeners() {
        mAddTransactionBtn.setOnClickListener((View v)->userController.transactionBtnPressed());
        mToggleBtn.setOnClickListener((View v)->userController.toggleRecyclerViewContent());
        mSearchBtn.setOnClickListener((View v)->userController.searchBetweenDatesPressed(
                mDateFrometv.getText().toString(),
                mDateToetv.getText().toString()
        ));
        //Refresh recycler and clear search fields.
        mClearSearchBtn.setOnClickListener((View v)->{
            userController.clearUserSearch();
            this.mDateFrometv.setText("");
            this.mDateToetv.setText("");
        });
        mDateFrometv.setOnClickListener((View v)->{
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateFromSetListener,
                    year,month,day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        this.mDateFromSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                // String date = dayOfMonth + "/" + month + "/" + year;
                String monthStr = String.valueOf(month);
                String dayOfMonthStr = String.valueOf(dayOfMonth);
                if(monthStr.length()<2){
                    monthStr = "0"+monthStr;
                }
                if(dayOfMonthStr.length()<2){
                    dayOfMonthStr = "0"+dayOfMonthStr;
                }
                String date = year + "-" + monthStr + "-" + dayOfMonthStr;
                mDateFrometv.setText(date);
            }
        };
        mDateToetv.setOnClickListener((View v)->{
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateToSetListener,
                    year,month,day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        this.mDateToSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                // String date = dayOfMonth + "/" + month + "/" + year;
                String monthStr = String.valueOf(month);
                String dayOfMonthStr = String.valueOf(dayOfMonth);
                if(monthStr.length()<2){
                    monthStr = "0"+monthStr;
                }
                if(dayOfMonthStr.length()<2){
                    dayOfMonthStr = "0"+dayOfMonthStr;
                }
                String date = year + "-" + monthStr + "-" + dayOfMonthStr;
                mDateToetv.setText(date);
            }
        };
    }

    public void setRecyclerFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        ft.replace(R.id.fragmentRecyclerViewContainer,fragment);
        if(addToBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();
        //fm.executePendingTransactions();        //Muy importante
    }

    private void setFragment(Fragment fragment, int container, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        ft.replace(container,fragment);
        if(addToBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();
        //fm.executePendingTransactions();        //Muy importante
    }

    public void setFragment(Fragment fragment, boolean addToBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        ft.replace(R.id.fragmentContainer2,fragment);
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
