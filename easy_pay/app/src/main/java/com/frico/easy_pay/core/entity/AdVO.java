package com.frico.easy_pay.core.entity;

import java.util.List;

/**
 * 创建日期：2019/7/23 on 10:10
 * 作者:王红闯 hongchuangwang
 */
public class AdVO {

    /**
     * list : {"total":1,"per_page":10,"current_page":1,"last_page":1,"data":[{"id":248,"advertno":"S201907230955552401","userid":600080,"tradetype":"1","total_amount":"25.2500","amount":"25.0000","frozenamount":"0.0000","tradeamount":"0.0000","amountmin":"12.0000","amountmax":"25.0000","sell_rate":"1.00","tradeway":"2,3","status":"1","completetime":null,"cancleip":null,"cancletime":null,"createtip":"36.24.96.145","updatetime":"2019-07-23 09:55:56","createtime":"2019-07-23 09:55:56","tradeway_text":"支付宝/微信"}]}
     * status : 1
     * tradeway : 0
     */

    private ListBean list;
    private String status;
    private String tradeway;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeway() {
        return tradeway;
    }

    public void setTradeway(String tradeway) {
        this.tradeway = tradeway;
    }

    public static class ListBean {
        /**
         * total : 1
         * per_page : 10
         * current_page : 1
         * last_page : 1
         * data : [{"id":248,"advertno":"S201907230955552401","userid":600080,"tradetype":"1","total_amount":"25.2500","amount":"25.0000","frozenamount":"0.0000","tradeamount":"0.0000","amountmin":"12.0000","amountmax":"25.0000","sell_rate":"1.00","tradeway":"2,3","status":"1","completetime":null,"cancleip":null,"cancletime":null,"createtip":"36.24.96.145","updatetime":"2019-07-23 09:55:56","createtime":"2019-07-23 09:55:56","tradeway_text":"支付宝/微信"}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 248
             * advertno : S201907230955552401
             * userid : 600080
             * tradetype : 1
             * total_amount : 25.2500
             * amount : 25.0000
             * frozenamount : 0.0000
             * tradeamount : 0.0000
             * amountmin : 12.0000
             * amountmax : 25.0000
             * sell_rate : 1.00
             * tradeway : 2,3
             * status : 1
             * completetime : null
             * cancleip : null
             * cancletime : null
             * createtip : 36.24.96.145
             * updatetime : 2019-07-23 09:55:56
             * createtime : 2019-07-23 09:55:56
             * tradeway_text : 支付宝/微信
             */

            private int id;
            private String advertno;
            private int userid;
            private String tradetype;
            private String total_amount;
            private String amount;
            private String frozenamount;
            private String tradeamount;
            private String amountmin;
            private String amountmax;
            private String sell_rate;
            private String tradeway;
            private String status;
            private Object completetime;
            private Object cancleip;
            private Object cancletime;
            private String createtip;
            private String updatetime;
            private String createtime;
            private String tradeway_text;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAdvertno() {
                return advertno;
            }

            public void setAdvertno(String advertno) {
                this.advertno = advertno;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getTradetype() {
                return tradetype;
            }

            public void setTradetype(String tradetype) {
                this.tradetype = tradetype;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getFrozenamount() {
                return frozenamount;
            }

            public void setFrozenamount(String frozenamount) {
                this.frozenamount = frozenamount;
            }

            public String getTradeamount() {
                return tradeamount;
            }

            public void setTradeamount(String tradeamount) {
                this.tradeamount = tradeamount;
            }

            public String getAmountmin() {
                return amountmin;
            }

            public void setAmountmin(String amountmin) {
                this.amountmin = amountmin;
            }

            public String getAmountmax() {
                return amountmax;
            }

            public void setAmountmax(String amountmax) {
                this.amountmax = amountmax;
            }

            public String getSell_rate() {
                return sell_rate;
            }

            public void setSell_rate(String sell_rate) {
                this.sell_rate = sell_rate;
            }

            public String getTradeway() {
                return tradeway;
            }

            public void setTradeway(String tradeway) {
                this.tradeway = tradeway;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getCompletetime() {
                return completetime;
            }

            public void setCompletetime(Object completetime) {
                this.completetime = completetime;
            }

            public Object getCancleip() {
                return cancleip;
            }

            public void setCancleip(Object cancleip) {
                this.cancleip = cancleip;
            }

            public Object getCancletime() {
                return cancletime;
            }

            public void setCancletime(Object cancletime) {
                this.cancletime = cancletime;
            }

            public String getCreatetip() {
                return createtip;
            }

            public void setCreatetip(String createtip) {
                this.createtip = createtip;
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

            public String getTradeway_text() {
                return tradeway_text;
            }

            public void setTradeway_text(String tradeway_text) {
                this.tradeway_text = tradeway_text;
            }
        }
    }
}
