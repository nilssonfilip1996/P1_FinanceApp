package com.example.nilss.financeapp.UserActivityClasses;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nilss.financeapp.Pojos.Product;
import com.example.nilss.financeapp.R;
import com.example.nilss.financeapp.UserActivityClasses.UserController;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {
    private static final String TAG = "TransactionFragment";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private EditText mDateetv, mTitleetv, mAmountetv;
    private Spinner mCategorySpinner;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private UserController mUserController;
    private static ArrayList<Product> productArrayList;
    private Button commitBtn, scanBtn;
    private Switch mTransactionSwitch;


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transaction, container, false);
        initComponents(view);
        return view;
    }

    public void setUserControllerAndProductList(UserController userController, ArrayList<Product> productArrayList){
        this.mUserController = userController;
        this.productArrayList = productArrayList;
    }

    private void initComponents(View view) {
        this.mDateetv = view.findViewById(R.id.transactionDateetv);
        this.mTitleetv = view.findViewById(R.id.transactionTitleetv);
        this.mCategorySpinner = view.findViewById(R.id.categorySpinner);
        this.mAmountetv = view.findViewById(R.id.transactionAmountetv);
        this.commitBtn = view.findViewById(R.id.coimmitBtn);
        this.scanBtn = view.findViewById(R.id.scanBtn);
        this.mTransactionSwitch = view.findViewById(R.id.typeOfTransactionSwitch);
        mTransactionSwitch.setChecked(false);
        initListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initListeners() {
        this.mDateetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        this.mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                //String date2 = dayOfMonth + "/" + month + "/" + year;
                String monthStr = String.valueOf(month);
                String dayOfMonthStr = String.valueOf(dayOfMonth);
                if(monthStr.length()<2){
                    monthStr = "0"+monthStr;
                }
                if(dayOfMonthStr.length()<2){
                    dayOfMonthStr = "0"+dayOfMonthStr;
                }
                String date = year + "-" + monthStr + "-" + dayOfMonthStr;
                Log.d(TAG, date);
                mDateetv.setText(date);
            }
        };
        this.mTransactionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.expenseList, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                scanBtn.setEnabled(true);
                // Apply the adapter to the spinner
                mCategorySpinner.setAdapter(adapter);
            }
            else{
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.incomeList, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                scanBtn.setEnabled(false);
                // Apply the adapter to the spinner
                mCategorySpinner.setAdapter(adapter);
            }
        });
        this.commitBtn.setOnClickListener((View view)->{
            mUserController.commitTransactionPressed(
                    mTransactionSwitch.isChecked(),
                    mDateetv.getText().toString(),
                    mTitleetv.getText().toString(),
                    mCategorySpinner.getSelectedItem().toString(),
                    mAmountetv.getText().toString());
        });
        this.scanBtn.setOnClickListener(this::scanBar);
    }

    //product barcode mode
    public void scanBar(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private Dialog showDialog(final Activity act, CharSequence title,
                              CharSequence message,
                              CharSequence buttonYes,
                              CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                String serialNbr = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d(TAG, "contents: " + serialNbr);
                Product scannedProduct = null;
                Log.d(TAG, "arraylist size: " + String.valueOf(productArrayList.size()));
                for(int i=0;i<productArrayList.size();i++){
                    if(productArrayList.get(i).getSerialnbr().equals(serialNbr)){
                        scannedProduct = productArrayList.get(i);
                        break;
                    }
                }
                if(scannedProduct!=null) {
                    mTitleetv.setText(scannedProduct.getDescription());
                    mAmountetv.setText(String.valueOf(scannedProduct.getCost()));
                }
                else{
                    Toast.makeText(getActivity(), "Scanned product is not valid!", Toast.LENGTH_SHORT).show();
                }
                //mUserController.barCodeScanned(contents);
            }
        }
    }

}
