package com.example.nilss.financeapp.Pojos;

import com.example.nilss.financeapp.UserActivityClasses.Transaction;

public class Expense implements Transaction{
    private int id, userId, amount;
    private String date, title, category;

    public Expense() {
        this.id=0;
        this.userId=0;
        this.amount=0;
        this.date="";
        this.title="";
        this.category="";
    }

    public Expense(int id, int userId, int amount, String date, String title, String category) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.title = title;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
