package com.frico.easy_pay.core.entity;

import java.io.Serializable;
import java.util.List;

public class BankVO implements Serializable {

    /**
     * list : [{"id":85,"bankname":"光大银行","subbranch":"下午我给你","accountname":"王自正84","cardnumber":"64646495643169495","cardmobile":"17682329221","alias":"卡2","receivemoney":"2324.87","status":"normal","verifystatus":"1","isdelete":"0","updatetime":"2019-07-23 13:46:24","createtime":"2019-07-23 13:46:24"},{"id":84,"bankname":"招商银行","subbranch":"世茂中心行","accountname":"wangzizheng84","cardnumber":"645481819946466494955","cardmobile":"17682329232","alias":"卡1","receivemoney":"2851.74","status":"normal","verifystatus":"1","isdelete":"0","updatetime":"2019-07-23 13:45:07","createtime":"2019-07-23 13:45:07"}]
     * cardnumber :
     */

    private String cardnumber;
    private List<ListBean> list;

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * id : 85
         * bankname : 光大银行
         * subbranch : 下午我给你
         * accountname : 王自正84
         * cardnumber : 64646495643169495
         * cardmobile : 17682329221
         * alias : 卡2
         * receivemoney : 2324.87
         * status : normal
         * verifystatus : 1
         * isdelete : 0
         * updatetime : 2019-07-23 13:46:24
         * createtime : 2019-07-23 13:46:24
         */

        private String id;
        private String bankname;
        private String subbranch;
        private String accountname;
        private String cardnumber;
        private String cardmobile;
        private String alias;
        private String receivemoney;
        private String status;
        private int verifystatus;//审核状态:0=待审核,1=通过审核,2=未通过审核'
        private String isdelete;//是否删除:0=否,1=是
        private String updatetime;
        private String createtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getSubbranch() {
            return subbranch;
        }

        public void setSubbranch(String subbranch) {
            this.subbranch = subbranch;
        }

        public String getAccountname() {
            return accountname;
        }

        public void setAccountname(String accountname) {
            this.accountname = accountname;
        }

        public String getCardnumber() {
            return cardnumber;
        }

        public void setCardnumber(String cardnumber) {
            this.cardnumber = cardnumber;
        }

        public String getCardmobile() {
            return cardmobile;
        }

        public void setCardmobile(String cardmobile) {
            this.cardmobile = cardmobile;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getReceivemoney() {
            return receivemoney;
        }

        public void setReceivemoney(String receivemoney) {
            this.receivemoney = receivemoney;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getVerifystatus() {
            return verifystatus;
        }

        public void setVerifystatus(int verifystatus) {
            this.verifystatus = verifystatus;
        }

        public String getIsdelete() {
            return isdelete;
        }

        public void setIsdelete(String isdelete) {
            this.isdelete = isdelete;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
