package com.frico.easy_pay.core.entity;

public class MyGroupInfoVO {

        private int team_level;           //": "无",等级
        private String teamleveltext;           //": "无",等级 描述
        private String direct_nums;             //": 4,  直推的数量
        private int team_nums;               //": 22,    团队数量
        private String total_profit;            //": 0


    public String getTeamleveltext() {
        return teamleveltext;
    }

    public void setTeamleveltext(String teamleveltext) {
        this.teamleveltext = teamleveltext;
    }

    public String getDirect_nums() {
        return direct_nums;
    }

    public void setDirect_nums(String direct_nums) {
        this.direct_nums = direct_nums;
    }

    public int getTeam_nums() {
        return team_nums;
    }

    public void setTeam_nums(int team_nums) {
        this.team_nums = team_nums;
    }

    public int getTeam_level() {
        return team_level;
    }

    public void setTeam_level(int team_level) {
        this.team_level = team_level;
    }

    public String getTotal_profit() {
        return total_profit;
    }

    public void setTotal_profit(String total_profit) {
        this.total_profit = total_profit;
    }
}
