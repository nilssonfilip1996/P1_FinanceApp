package com.example.nilss.financeapp.UserActivityClasses;

public interface Transaction {
    int getId();
    void setId(int id);
    int getUserId();
    void setUserId(int userId);
    int getAmount();
    void setAmount(int amount);
    String getDate();
    void setDate(String date);
    String getTitle();
    void setTitle(String title);
    String getCategory();
    void setCategory(String category);
}
