package com.example.nilss.financeapp.Pojos;

public class Product {
    private String description, Serialnbr;
    private int cost;

    public Product(String description, String serialnbr, int cost) {
        this.description = description;
        this.Serialnbr = serialnbr;
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialnbr() {

        return Serialnbr;
    }

    public void setSerialnbr(String serialnbr) {
        Serialnbr = serialnbr;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
