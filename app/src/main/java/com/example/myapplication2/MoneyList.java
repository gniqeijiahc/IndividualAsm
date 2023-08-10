package com.example.myapplication2;

public class MoneyList {
    private String name;
    private double money;

    public MoneyList(String name, double money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public String getString(){
        return name + " RM " + money;
    }
}


