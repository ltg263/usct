package com.frico.usct.core.entity;

public class TodayIncomeOrderInfoVO {
    private double receive_success_amount;      //":"0.0000",
    private int receive_success_nums;        //":0

    public double getReceive_success_amount() {
        return receive_success_amount;
    }

    public void setReceive_success_amount(double receive_success_amount) {
        this.receive_success_amount = receive_success_amount;
    }

    public int getReceive_success_nums() {
        return receive_success_nums;
    }

    public void setReceive_success_nums(int receive_success_nums) {
        this.receive_success_nums = receive_success_nums;
    }
}
