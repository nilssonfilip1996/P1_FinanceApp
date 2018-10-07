package com.example.nilss.financeapp.UserActivityClasses;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.nilss.financeapp.DataBaseHelper;
import com.example.nilss.financeapp.MainActivityClasses.MainActivity;
import com.example.nilss.financeapp.Pojos.Expense;
import com.example.nilss.financeapp.Pojos.Income;
import com.example.nilss.financeapp.Pojos.Product;
import com.example.nilss.financeapp.Pojos.User;

import java.util.ArrayList;

public class UserController implements ControllerInterface{
    private static final String TAG = "USERCONTROLLER";
    private static final int ISINCOMEVIEW = 0;
    private static final int ISEXPENSEVIEW = 1;
    private int currentRecyclerContentType;
    private UserActivity mUserActivity;
    private TopFragment mTopFragment;
    private TransactionFragment mTransactionFragment;
    private User currentUser;
    private DataBaseHelper dbHelper;
    private ArrayList<Transaction> expensesList;
    private ArrayList<Transaction> incomesList;
    private static ArrayList<Product> scannableProductList;
    private RecyclerFragment mRecyclerFragment;
    private int balance=0;

    public UserController(UserActivity userActivity, TopFragment tf, String userEmail) {
        this.mUserActivity = userActivity;
        this.mTopFragment = tf;
        mTopFragment.setController(this);
        this.dbHelper = new DataBaseHelper(mUserActivity);
        currentUser = dbHelper.retrieveUserbyEmail(userEmail);
        initializeUserScreen();
        mTransactionFragment = new TransactionFragment();
        populateScannableProductList();
        mTransactionFragment.setUserControllerAndProductList(this, scannableProductList);
    }

    private void populateScannableProductList() {
        scannableProductList = new ArrayList<>();
        scannableProductList.add(new Product("Frixion pen", "4902505417511", 39));
        scannableProductList.add(new Product("Algorithms book", "9780073523408", 599));
        scannableProductList.add(new Product("Milk", "7310867001823", 19));
    }

    private void initializeUserScreen() {
        //Get the users expensesList
        expensesList = dbHelper.getAllExpensesByUserId(Integer.toString(currentUser.getId()), null, null);
        incomesList = dbHelper.getAllIncomesByUserId(Integer.toString(currentUser.getId()), null, null);
        for(int i = 0; i< incomesList.size(); i++){
            balance+= incomesList.get(i).getAmount();
            Log.d(TAG, incomesList.get(i).getCategory());
        }
        for(int i = 0; i< expensesList.size(); i++){
            balance-= expensesList.get(i).getAmount();
        }
        mTopFragment.onInit(()->{
            mTopFragment.setUsertv(currentUser.getFirstName());
            mTopFragment.setBalancetv(String.valueOf(balance));
        });
        //Initialize th recyclerview with incomesList
        mRecyclerFragment = new RecyclerFragment();
        mRecyclerFragment.setUserController(this);
        mUserActivity.setRecyclerFragment(mRecyclerFragment,false);
        mRecyclerFragment.onInit(()->{
            mRecyclerFragment.updateList(incomesList);
        });
        currentRecyclerContentType = ISINCOMEVIEW;
    }

    public void transactionBtnPressed() {
        mTransactionFragment = new TransactionFragment();
        populateScannableProductList();
        mTransactionFragment.setUserControllerAndProductList(this, scannableProductList);
        mUserActivity.setFragment(mTransactionFragment,true);
        mUserActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void logOutBtnPressed() {
        Intent intent = new Intent(mUserActivity, MainActivity.class);
        mUserActivity.startActivity(intent);
        mUserActivity.finish();  // This call is missing.
    }

    public void commitTransactionPressed(boolean isExpense, String date, String title, String category, String amount) {
        //Check empty fields
        if((date.equals("")) || (title.equals("")) || (category.equals("")) || (amount.equals(""))){
            Toast.makeText(mUserActivity, "Make sure you have entered all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(title.length()>15){
            Toast.makeText(mUserActivity, "A title should not contain more then 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        //Check balance against the ne
        if(((balance-Integer.parseInt(amount))<0) && isExpense){
            Toast.makeText(mUserActivity, "Not enough funds.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Is the transaction an expense?
        if(isExpense) {
            Expense expense = new Expense();
            expense.setUserId(currentUser.getId());
            expense.setDate(date);
            expense.setTitle(title);
            expense.setCategory(category);
            try {
                expense.setAmount(Integer.parseInt(amount));
            } catch (NumberFormatException e){
                Log.d(TAG, "numberformatexception!");
                return;
            }
            if(dbHelper.insertExpense(expense)){
                mUserActivity.popFragmentFromBackStack();
                mUserActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
            else{
                //Log.d("Success: ", "False");
                Toast.makeText(mUserActivity, "Error adding to db", Toast.LENGTH_SHORT).show();
            }
        }
        //otherwise it is an income!
        else{
            Income inc = new Income();
            inc.setUserId(currentUser.getId());
            inc.setDate(date);
            inc.setTitle(title);
            inc.setCategory(category);
            try {
                inc.setAmount(Integer.parseInt(amount));
            } catch (NumberFormatException e){
                Log.d(TAG, "numberformatexception!");
                return;
            }
            if (dbHelper.insertIncome(inc)) {
                mUserActivity.popFragmentFromBackStack();
                mUserActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                //Log.d("Success: ", "False");
                Toast.makeText(mUserActivity, "Error adding to db", Toast.LENGTH_SHORT).show();
            }
        }
        refreshRecyclerView(null, null);
        mUserActivity.closeKeyboard();
    }

    private void refreshRecyclerView(String dateFrom, String dateTo) {
        balance=0;
        expensesList = dbHelper.getAllExpensesByUserId(Integer.toString(currentUser.getId()), dateFrom, dateTo);
        incomesList = dbHelper.getAllIncomesByUserId(Integer.toString(currentUser.getId()), dateFrom, dateTo);
        if((dateFrom==null) && (dateTo==null)) {
            for (int i = 0; i < incomesList.size(); i++) {
                balance += incomesList.get(i).getAmount();
                Log.d(TAG, incomesList.get(i).getCategory());
            }
            for (int i = 0; i < expensesList.size(); i++) {
                balance -= expensesList.get(i).getAmount();
            }
            mTopFragment.setBalancetv(String.valueOf(balance));
        }
        if(currentRecyclerContentType==ISINCOMEVIEW){
            mRecyclerFragment.updateList(incomesList);
            Log.d(TAG, "Switched to expenselist!");
        }
        else{
            mRecyclerFragment.updateList(expensesList);
            Log.d(TAG, "Switched to incomelist!");
        }
    }

    public void incomeListClicked(Transaction income) {
        Log.d(TAG, String.valueOf(income.getCategory()));
    }

    public void toggleRecyclerViewContent() {
        if(currentRecyclerContentType==ISINCOMEVIEW){
            currentRecyclerContentType = ISEXPENSEVIEW;
            mRecyclerFragment.updateList(expensesList);
            Log.d(TAG, "Switched to expenselist!");
        }
        else{
            currentRecyclerContentType = ISINCOMEVIEW;
            mRecyclerFragment.updateList(incomesList);
            Log.d(TAG, "Switched to incomelist!");
        }
    }

    public void searchBetweenDatesPressed(String from, String to) {
        if(from.equals("") || to.equals("")){
            Toast.makeText(mUserActivity, "Please specify dates!", Toast.LENGTH_SHORT).show();
            return;
        }
        refreshRecyclerView(from,to);
    }

    public void barCodeScanned(String contents) {
        try {
            Product scannedProduct = scannableProductList.get(scannableProductList.indexOf(contents));
            Log.d(TAG, scannedProduct.getDescription());
        } catch(Exception e){
            Log.d(TAG, "failed");
        }
    }

    public void clearUserSearch() {
        refreshRecyclerView(null,null);
    }
}
