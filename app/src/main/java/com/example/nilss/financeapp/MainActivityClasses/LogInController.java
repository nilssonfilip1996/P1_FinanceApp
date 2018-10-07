package com.example.nilss.financeapp.MainActivityClasses;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.nilss.financeapp.DataBaseHelper;
import com.example.nilss.financeapp.Pojos.User;
import com.example.nilss.financeapp.UserActivityClasses.UserActivity;

public class LogInController {
    private static final int LOGIN_CONST = 0;
    private static final int REGISTER_CONST = 1;
    private MainActivity mMainActivity;
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;
    private DataBaseHelper dbHelper;
    private User currentUser;
    public LogInController(MainActivity mainActivity){
        this.mMainActivity = mainActivity;
        this.dbHelper = new DataBaseHelper(mMainActivity);
        this.mLoginFragment = new LoginFragment();
        this.mLoginFragment.setController(this);
        mMainActivity.setFragment(mLoginFragment,false);//Set the fragment
        SharedPreferences sharedPreferences = mMainActivity.getSharedPreferences("activeFragment", Activity.MODE_PRIVATE);
        if(sharedPreferences.getInt("AF", 0)==REGISTER_CONST){
            registerPressed();
        }
    }

    public void logInPressed(String emailString, String passwordString) {
        if((emailString.equals("")) || (passwordString.equals(""))){
            Toast.makeText(mMainActivity, "Make sure you have entered all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        currentUser = dbHelper.retrieveUserbyEmail(emailString);
        //User not found
        if(currentUser == null){
            //Toast.makeText(mMainActivity, "Email " + emailString + " " + "is not registered!", Toast.LENGTH_SHORT).show();
            Toast.makeText(mMainActivity, "Wrong credentials", Toast.LENGTH_SHORT).show();
        }
        //User found, switch to userActivity.
        else{
            //Correct pw
            if(currentUser.getPassword().equals(passwordString)) {
                Intent intent = new Intent(mMainActivity, UserActivity.class);
                intent.putExtra("userEmail", currentUser.getEmail());
                mMainActivity.startActivity(intent);
            }
            else{
                Toast.makeText(mMainActivity, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void registerPressed() {
        mRegisterFragment = new RegisterFragment();
        mRegisterFragment.setController(this);
        mMainActivity.setFragment(mRegisterFragment,true);
        //Update sharedpreferences with currentfragment(register)
        SharedPreferences sharedPreferences1 = mMainActivity.getSharedPreferences("activeFragment", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putInt("AF", REGISTER_CONST);
        editor1.apply();
    }

    private boolean evaluateEmail(String email){
        String htmlAddr = "@hotmail.com";
        String gmailAddr = "@gmail.com";
        String emailType;
        try {
            emailType = email.substring(email.indexOf('@'), email.length()).toLowerCase();
            Log.d("ending", emailType);
        } catch (IndexOutOfBoundsException e){
            return false;
        }
        if(emailType.equals(htmlAddr) || emailType.equals(gmailAddr)){
            return true;
        }
        return false;
    }

    public void verifyNewUserCreation(String firstName, String lastName, String email, String password1, String password2) {

        if((firstName.equals("")) || (lastName.equals("")) || (email.equals("")) || (password1.equals("")) || (password2.equals(""))){
            Toast.makeText(mMainActivity, "Make sure you have entered all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!evaluateEmail(email)){
            String error = "Only hotmail.com or gmail.com addesses allowed";
            mRegisterFragment.setEmailError(error);
            return;
        }
        //check pw fields
        if(password1.equals(password2)){
            Log.d("equal: ", "Same pw!");
            User user = new User(firstName, lastName, email, password1);
            //Try to add user to db. If successful go to loginscreen.
            if(dbHelper.insertUser(user)){
                Toast.makeText(mMainActivity, "Created user: " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();
                mMainActivity.popFragmentFromBackStack();
                mLoginFragment.setEmailetv(email);
                mLoginFragment.clearPasswordetv();
                //Update sharedpreferences with currentfragment(login)
                SharedPreferences sharedPreferences1 = mMainActivity.getSharedPreferences("activeFragment", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putInt("AF", LOGIN_CONST);
                editor1.apply();
                SharedPreferences sharedPreferences2 = mMainActivity.getSharedPreferences("registerFragment", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.clear();
                editor2.apply();
            }
            //Email already in database
            else{
                Toast.makeText(mMainActivity, "Failed to create user", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(mMainActivity, "Password fields doesn't match!", Toast.LENGTH_SHORT).show();
        }
    }
}
