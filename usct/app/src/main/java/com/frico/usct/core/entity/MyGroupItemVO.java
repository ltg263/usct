package com.frico.usct.core.entity;


import java.io.Serializable;

/**
 * 我的团队 item对象
 */
public class MyGroupItemVO  implements Serializable {

     private String acqid;              //": 600007,
     private String available_money;    //": "2145.8822",  账户余额
     private int team_level;         //": 0,             会员等级  '0'=>'无','1'=>'经理', '2'=>'总监','3'=>'董事',
     private String team_nums;          //": 18,            团队数量
     private String mobile;             //": "13777094585", 手机号
     private String avater;             //": null           头像
    private String agentname;           //团队等级名称

    public String getAcqid() {
        return acqid;
    }

    public void setAcqid(String acqid) {
        this.acqid = acqid;
    }

    public String getAvailable_money() {
        return available_money;
    }

    public void setAvailable_money(String available_money) {
        this.available_money = available_money;
    }

    public int getTeam_level() {
        return team_level;
    }

    public void setTeam_level(int team_level) {
        this.team_level = team_level;
    }

    public String getTeam_nums() {
        return team_nums;
    }

    public void setTeam_nums(String team_nums) {
        this.team_nums = team_nums;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }
}
