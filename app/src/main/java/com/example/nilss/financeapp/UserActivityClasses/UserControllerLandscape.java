package com.example.nilss.financeapp.UserActivityClasses;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import com.example.nilss.financeapp.DataBaseHelper;
import com.example.nilss.financeapp.MainActivityClasses.MainActivity;
import com.example.nilss.financeapp.Pojos.Expense;
import com.example.nilss.financeapp.Pojos.Income;
import com.example.nilss.financeapp.Pojos.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.support.constraint.Constraints.TAG;

public class UserControllerLandscape implements ControllerInterface{
    private UserActivity mUserActivity;
    private TopFragment mTopFragment;
    private DataBaseHelper dbHelper;
    private User currentUser;
    private ArrayList<Transaction> expensesList;
    private ArrayList<Transaction> incomesList;
    private int balance;
    private PieChart pieChartIncome;
    private PieChart pieChartExpense;

    public UserControllerLandscape(UserActivity userActivity, TopFragment tf, PieChart pieIncome, PieChart pieExpense, String userEmail) {
        this.mUserActivity = userActivity;
        this.mTopFragment = tf;
        this.pieChartIncome = pieIncome;
        this.pieChartExpense = pieExpense;
        mTopFragment.setController(this);
        this.dbHelper = new DataBaseHelper(mUserActivity);
        currentUser = dbHelper.retrieveUserbyEmail(userEmail);
        initializeUserScreen();
    }

    @Override
    public void logOutBtnPressed() {
        Intent intent = new Intent(mUserActivity, MainActivity.class);
        mUserActivity.startActivity(intent);
        mUserActivity.finish();  // This call is missing.
    }

    private void initializeUserScreen() {
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

        pieChartIncome.setUsePercentValues(true);
        pieChartIncome.getDescription().setEnabled(true);
        pieChartIncome.getDescription().setText("Income categories");
        pieChartIncome.setCenterText("Income\ncategories");
        pieChartIncome.setEntryLabelColor(Color.BLACK);
        pieChartIncome.setCenterTextSize(10f);
        pieChartIncome.setExtraOffsets(5,10,5,5);
        pieChartIncome.setDragDecelerationFrictionCoef(0.95f);
        pieChartIncome.setDrawHoleEnabled(true);
        pieChartIncome.setHoleColor(0xffc489);
        pieChartIncome.setTransparentCircleRadius(0.61f);
        pieChartIncome.setDrawEntryLabels(false);

        pieChartExpense.setUsePercentValues(true);
        pieChartExpense.getDescription().setEnabled(true);
        pieChartExpense.getDescription().setText("Expense categories");
        pieChartExpense.setCenterText("Expense\ncategories");
        pieChartExpense.setEntryLabelColor(Color.BLACK);
        pieChartExpense.setCenterTextSize(10f);
        pieChartExpense.setExtraOffsets(5,10,5,5);
        pieChartExpense.setDragDecelerationFrictionCoef(0.95f);
        pieChartExpense.setDrawHoleEnabled(true);
        pieChartExpense.setHoleColor(0xffc489);
        pieChartExpense.setTransparentCircleRadius(0.61f);
        pieChartExpense.setDrawEntryLabels(false);
        pieChartExpense.removeAllViews();

        addValuesToPieChart(pieChartIncome,incomesList);
        addValuesToPieChart(pieChartExpense,expensesList);

    }

    private void addValuesToPieChart(PieChart chart, ArrayList<Transaction> transactions){
        if(transactions.size()<1){
            chart.removeAllViews();
            return;
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        //if arraylist contains Expenses
        if(transactions.get(0) instanceof Income){
            map.put("Salary",0);
            map.put("Other",0);
            for(int i=0; i<transactions.size(); i++){
                switch (transactions.get(i).getCategory()){
                    case "Salary":
                        Log.d(TAG, "Salary");
                        map.put("Salary",map.get("Salary")+transactions.get(i).getAmount());
                        break;
                    case "Other":
                        Log.d(TAG, "other");
                        map.put("Other",map.get("Other")+transactions.get(i).getAmount());
                        break;
                }
            }
            Log.d(TAG, "Salary amount: " + String.valueOf(map.get("Salary")));
            ArrayList<PieEntry> pieValues = new ArrayList<>();
            pieValues.add(new PieEntry(map.get("Salary"), "Salary"));
            pieValues.add(new PieEntry(map.get("Other"), "Other"));

            PieDataSet dataSet = new PieDataSet(pieValues,"Categories");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);

            chart.setData(data);
        }
        //otherwise arraylist contains expenses
        else{
            map.put("Groceries",0);
            map.put("Entertainment",0);
            map.put("Travel",0);
            map.put("Food",0);
            map.put("Other",0);
            for(int i=0; i<transactions.size(); i++){
                switch (transactions.get(i).getCategory()){
                    case "Groceries":
                        Log.d(TAG, "Groceries");
                        map.put("Groceries",map.get("Groceries")+transactions.get(i).getAmount());
                        break;
                    case "Entertainment":
                        Log.d(TAG, "Entertainment");
                        map.put("Entertainment",map.get("Entertainment")+transactions.get(i).getAmount());
                        break;
                    case "Travel":
                        Log.d(TAG, "Travel");
                        map.put("Travel",map.get("Travel")+transactions.get(i).getAmount());
                        break;
                    case "Food":
                        Log.d(TAG, "Food");
                        map.put("Food",map.get("Food")+transactions.get(i).getAmount());
                        break;
                    case "Other":
                        Log.d(TAG, "Other");
                        map.put("Other",map.get("Other")+transactions.get(i).getAmount());
                        break;
                }
            }
            Log.d(TAG, "Groceries amount: " + String.valueOf(map.get("Groceries")));
            ArrayList<PieEntry> pieValues = new ArrayList<>();
            pieValues.add(new PieEntry(map.get("Groceries"), "Groceries"));
            pieValues.add(new PieEntry(map.get("Entertainment"), "Entertainment"));
            pieValues.add(new PieEntry(map.get("Travel"), "Travel"));
            pieValues.add(new PieEntry(map.get("Food"), "Food"));
            pieValues.add(new PieEntry(map.get("Other"), "Other"));

            PieDataSet dataSet = new PieDataSet(pieValues,"Categories");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);

            chart.setData(data);
        }

    }
}
