package com.frico.usct.core.entity;

import java.util.List;

/**
 * 创建日期：2019/7/25 on 14:21
 * 作者:王红闯 hongchuangwang
 */
public class MoneyDetailsVO {

    /**
     * list : {"total":1,"per_page":15,"current_page":1,"last_page":1,"data":[{"id":1313,"type":"1","beforemoney":"0.0000","money":"1000000.0000","endmoney":"1000000.0000","linkorder":"","createtime":"2019-07-24 20:17:40"}]}
     * start :
     * end :
     * type : 1
     */

    private ListBean list;
    private String start;
    private String end;
    private String type;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class ListBean {
        /**
         * total : 1
         * per_page : 15
         * current_page : 1
         * last_page : 1
         * data : [{"id":1313,"type":"1","beforemoney":"0.0000","money":"1000000.0000","endmoney":"1000000.0000","linkorder":"","createtime":"2019-07-24 20:17:40"}]
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
             * id : 1313
             * type : 1
             * beforemoney : 0.0000
             * money : 1000000.0000
             * endmoney : 1000000.0000
             * linkorder :
             * createtime : 2019-07-24 20:17:40
             */

            private int id;
            private String type;
            private String beforemoney;
            private String money;
            private String endmoney;
            private String linkorder;
            private String createtime;
            private String type_text;
            private String memo;//资金备注

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getBeforemoney() {
                return beforemoney;
            }

            public void setBeforemoney(String beforemoney) {
                this.beforemoney = beforemoney;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getEndmoney() {
                return endmoney;
            }

            public void setEndmoney(String endmoney) {
                this.endmoney = endmoney;
            }

            public String getLinkorder() {
                return linkorder;
            }

            public void setLinkorder(String linkorder) {
                this.linkorder = linkorder;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getType_text() {
                return type_text;
            }

            public void setType_text(String type_text) {
                this.type_text = type_text;
            }

            public String getMemo() {
                return memo;
            }

            public void setMemo(String memo) {
                this.memo = memo;
            }
        }
    }
}
