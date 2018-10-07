package com.example.nilss.financeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nilss.financeapp.Pojos.Expense;
import com.example.nilss.financeapp.Pojos.Income;
import com.example.nilss.financeapp.Pojos.User;
import com.example.nilss.financeapp.UserActivityClasses.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bankdatabase.db";

    //Table names
    private static final String TABLE_EXPENSE = "expenses";
    private static final String TABLE_INCOME = "incomes";
    private static final String TABLE_USER = "users";

    //Common column names
    private static final String COLUMN_KEY_ID = "id";
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";

    //USER TABLE - column names
    private static final String COLUMN_FIRST_NAME = "firstname";
    private static final String COLUMN_LAST_NAME = "lastname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Database creation of table expenses
    private static final String DATABASE_CREATE_EXPENSE =
            "create table " + TABLE_EXPENSE + "(" +
                    COLUMN_KEY_ID + " integer primary key autoincrement, " +
                    COLUMN_USER_ID + " integer, " +
                    COLUMN_DATE + " text not null, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_CATEGORY + " text not null, " +
                    COLUMN_AMOUNT + " integer);";

    // Database creation of table expenses
    private static final String DATABASE_CREATE_INCOME =
            "create table " + TABLE_INCOME + "(" +
                    COLUMN_KEY_ID + " integer primary key autoincrement, " +
                    COLUMN_USER_ID + " integer, " +
                    COLUMN_DATE + " text not null, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_CATEGORY + " text not null, " +
                    COLUMN_AMOUNT + " integer);";

    // Database creation of table user
    private static final String DATABASE_CREATE_USER =
            "create table " + TABLE_USER + "(" +
                    COLUMN_USER_ID + " integer primary key autoincrement, " +
                    COLUMN_FIRST_NAME + " text not null, " +
                    COLUMN_LAST_NAME + " text not null, " +
                    COLUMN_EMAIL + " text not null, " +
                    COLUMN_PASSWORD + " text not null);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_EXPENSE);
        db.execSQL(DATABASE_CREATE_INCOME);
        db.execSQL(DATABASE_CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        onCreate(db);
    }

    /**
     * Think again before calling this method...
     */
    public void clearAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INCOME, null, null);
        db.delete(TABLE_EXPENSE, null, null);
        db.delete(TABLE_USER, null, null);
        db.delete("SQLITE_SEQUENCE", null, null);
    }

    public boolean insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
/*        db.delete(TABLE_INCOME, null, null);
        db.delete(TABLE_EXPENSE, null, null);
        db.delete(TABLE_USER, null, null);*/
        //check if email already registered
        Cursor cur = db.rawQuery("select * from " + TABLE_USER + " where " + COLUMN_EMAIL + " = ? ", new String[]{user.getEmail()});
        //db.close();
        if(cur.getCount()==0) { //email not yet registered
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_FIRST_NAME, user.getFirstName());
            contentValues.put(COLUMN_LAST_NAME, user.getLastName());
            contentValues.put(COLUMN_EMAIL, user.getEmail());
            contentValues.put(COLUMN_PASSWORD, user.getPassword());
            long result = db.insert(TABLE_USER, null, contentValues);
            if (result == -1) {
                return false;
            }
            return true;
        }
        return false;//error inserting
    }

    public User retrieveUserbyEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_USER + " where " + COLUMN_EMAIL + " = ? ", new String[]{email});
        //db.close();
        if (c != null)
            c.moveToFirst();
        if(c.getCount()==1) {
            User user = new User();
            user.setId(c.getInt(c.getColumnIndex(COLUMN_USER_ID)));
            user.setFirstName(c.getString(c.getColumnIndex(COLUMN_FIRST_NAME)));
            user.setLastName(c.getString(c.getColumnIndex(COLUMN_LAST_NAME)));
            user.setEmail(email);
            user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWORD)));
            return user;
        }
        return null;
    }

    public boolean insertIncome(Income income){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_ID, income.getUserId());
        contentValues.put(COLUMN_DATE, income.getDate());
        contentValues.put(COLUMN_TITLE, income.getTitle());
        contentValues.put(COLUMN_CATEGORY, income.getCategory());
        contentValues.put(COLUMN_AMOUNT, income.getAmount());
        long result = db.insert(TABLE_INCOME, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean insertExpense(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_ID, expense.getUserId());
        contentValues.put(COLUMN_DATE, expense.getDate());
        contentValues.put(COLUMN_TITLE,expense.getTitle());
        contentValues.put(COLUMN_CATEGORY, expense.getCategory());
        contentValues.put(COLUMN_AMOUNT, expense.getAmount());
        long result = db.insert(TABLE_EXPENSE, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<Transaction> getAllIncomesByUserId(String userId, String dateFrom, String dateTo){
        ArrayList<Transaction> income = new ArrayList<>();
        String query;
        if(dateFrom==null || dateTo==null) {    //No date interval selected
            query = "SELECT * FROM " + TABLE_INCOME + " WHERE " + COLUMN_USER_ID + "=" + userId + " ORDER BY " + COLUMN_DATE + " ASC";
        }
        else{
            query = "SELECT * FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND " + COLUMN_USER_ID + "=" + userId + " ORDER BY " + COLUMN_DATE + " ASC";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Income inc = new Income();
                inc.setId(c.getInt((c.getColumnIndex(COLUMN_KEY_ID))));
                inc.setId(c.getInt((c.getColumnIndex(COLUMN_USER_ID))));
                inc.setDate(c.getString((c.getColumnIndex(COLUMN_DATE))));
                inc.setTitle(c.getString((c.getColumnIndex(COLUMN_TITLE))));
                inc.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
                inc.setAmount(c.getInt((c.getColumnIndex(COLUMN_AMOUNT))));

                income.add(inc);
            } while (c.moveToNext());
        }
        return income;
    }

    public ArrayList<Transaction> getAllExpensesByUserId(String userId, String dateFrom, String dateTo){
        ArrayList<Transaction> expens = new ArrayList<>();
        String query;
        if(dateFrom==null || dateTo==null) {    //No date interval selected
            query = "SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_USER_ID + "=" + userId + " ORDER BY " + COLUMN_DATE + " ASC";
        }
        else{
            Log.d("Dates: ", dateFrom + " " + dateTo);
            query = "SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COLUMN_DATE + " BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND "
                    + COLUMN_USER_ID + "=" + userId + " ORDER BY " + COLUMN_DATE + " ASC";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Expense exp = new Expense();
                exp.setId(c.getInt((c.getColumnIndex(COLUMN_KEY_ID))));
                exp.setId(c.getInt((c.getColumnIndex(COLUMN_USER_ID))));
                exp.setDate(c.getString((c.getColumnIndex(COLUMN_DATE))));
                exp.setTitle(c.getString((c.getColumnIndex(COLUMN_TITLE))));
                exp.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
                exp.setAmount(c.getInt((c.getColumnIndex(COLUMN_AMOUNT))));

                expens.add(exp);
            } while (c.moveToNext());
        }
        return expens;
    }
}
