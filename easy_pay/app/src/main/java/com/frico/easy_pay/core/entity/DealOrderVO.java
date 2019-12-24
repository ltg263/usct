package com.frico.easy_pay.core.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期：2019/7/22 on 20:57
 * 作者:王红闯 hongchuangwang
 */
public class DealOrderVO {

    /**
     * amount : 1.00
     * price : 6.8
     * paymoney : 6.8
     * orderno : 201907222046351902
     * difftime : 106
     * tradeway : 1,2
     * tradeinfo : {"1":{"alias":"123","bankname":"中国银行","accountname":"123","cardnumber":"123"},"2":{"alias":"123","accountname":"1","img":"http://pay.localtest.me/uploads/2019_07_19/5d318d7c45681.jpg"},"3":{"alias":"","accountname":"","img":""},"4":{"alias":"","accountname":"","img":""}}
     */

    private String amount;
    private String price;
    private String cnymoney;
    private String orderno;
    private int difftime;
    private String tradeway;
    private TradeinfoBean tradeinfo;

    public DealOrderVO(String amount, String price, String cnymoney, String orderno, int difftime, String tradeway, TradeinfoBean tradeinfo) {
        this.amount = amount;
        this.price = price;
        this.cnymoney = cnymoney;
        this.orderno = orderno;
        this.difftime = difftime;
        this.tradeway = tradeway;
        this.tradeinfo = tradeinfo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCnymoney() {
        return cnymoney;
    }

    public void setCnymoney(String cnymoney) {
        this.cnymoney = cnymoney;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public int getDifftime() {
        return difftime;
    }

    public void setDifftime(int difftime) {
        this.difftime = difftime;
    }

    public String getTradeway() {
        return tradeway;
    }

    public void setTradeway(String tradeway) {
        this.tradeway = tradeway;
    }

    public TradeinfoBean getTradeinfo() {
        return tradeinfo;
    }

    public void setTradeinfo(TradeinfoBean tradeinfo) {
        this.tradeinfo = tradeinfo;
    }

    public static class TradeinfoBean {
        public TradeinfoBean(_$1Bean _$1, _$2Bean _$2, _$2Bean _$3, _$2Bean _$4, _$2Bean _$5) {
            this._$1 = _$1;
            this._$2 = _$2;
            this._$3 = _$3;
            this._$4 = _$4;
            this._$5 = _$5;
        }

        /**
         * 1 : {"alias":"123","bankname":"中国银行","accountname":"123","cardnumber":"123"}
         * 2 : {"alias":"123","accountname":"1","img":"http://pay.localtest.me/uploads/2019_07_19/5d318d7c45681.jpg"}
         * 3 : {"alias":"","accountname":"","img":""}
         * 4 : {"alias":"","accountname":"","img":""}
         */


        @SerializedName("1")
        private _$1Bean _$1;
        @SerializedName("2")
        private _$2Bean _$2;
        @SerializedName("3")
        private _$2Bean _$3;
        @SerializedName("4")
        private _$2Bean _$4;
        @SerializedName("5")
        private _$2Bean _$5;

        public _$1Bean get_$1() {
            return _$1;
        }

        public void set_$1(_$1Bean _$1) {
            this._$1 = _$1;
        }

        public _$2Bean get_$2() {
            return _$2;
        }

        public void set_$2(_$2Bean _$2) {
            this._$2 = _$2;
        }

        public _$2Bean get_$3() {
            return _$3;
        }

        public void set_$3(_$2Bean _$3) {
            this._$3 = _$3;
        }

        public _$2Bean get_$4() {
            return _$4;
        }

        public void set_$4(_$2Bean _$4) {
            this._$4 = _$4;
        }

        public _$2Bean get_$5() {
            return _$5;
        }

        public void set_$5(_$2Bean _$5) {
            this._$5 = _$5;
        }

        public static class _$1Bean {
            /**
             * alias : 123
             * bankname : 中国银行
             * accountname : 123
             * cardnumber : 123
             */

            private String alias;
            private String bankname;
            private String accountname;
            private String cardnumber;
            private String subbranch;//支行名称

            public _$1Bean(String alias, String bankname, String accountname, String cardnumber, String subbranch) {
                this.alias = alias;
                this.bankname = bankname;
                this.accountname = accountname;
                this.cardnumber = cardnumber;
                this.subbranch = subbranch;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getBankname() {
                return bankname;
            }

            public void setBankname(String bankname) {
                this.bankname = bankname;
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

            public String getSubbranch() {
                return subbranch;
            }

            public void setSubbranch(String subbranch) {
                this.subbranch = subbranch;
            }
        }

        public static class _$2Bean {
            /**
             * alias : 123
             * accountname : 1
             * img : http://pay.localtest.me/uploads/2019_07_19/5d318d7c45681.jpg
             */

            private String alias;
            private String accountname;
            private String img;

            public _$2Bean(String alias, String accountname, String img) {
                this.alias = alias;
                this.accountname = accountname;
                this.img = img;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getAccountname() {
                return accountname;
            }

            public void setAccountname(String accountname) {
                this.accountname = accountname;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
