package com.frico.usct.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 创建日期：2019/7/24 on 16:49
 * 作者:王红闯 hongchuangwang
 */
public class PayWayListVO implements Serializable {

    /**
     * list : [{"id":99,"codetype":"2","account":null,"accountname":"vyvh","img":"http://pay.fricobloc.com/paycode/5d37f97a2b4b1.jpg","alias":"gvhvibin","receivemoney":"0.00","status":"normal","verifystatus":"1","updatetime":"2019-07-24 14:23:56","createtime":"2019-07-24 14:23:56"},{"id":98,"codetype":"3","account":null,"accountname":"微信2","img":"http://pay.localtest.me/uploads/2019_07_24/5d3738bdcf3bc.jpg","alias":"微2","receivemoney":"0.00","status":"normal","verifystatus":"1","updatetime":"2019-07-24 00:41:38","createtime":"2019-07-24 00:41:38"},{"id":97,"codetype":"2","account":null,"accountname":"王自正支付宝2","img":"http://pay.localtest.me/uploads/2019_07_24/5d3738a8f0fe8.jpg","alias":"宝2","receivemoney":"0.00","status":"normal","verifystatus":"1","updatetime":"2019-07-24 00:41:19","createtime":"2019-07-24 00:41:19"},{"id":95,"codetype":"2","account":null,"accountname":"王自正支付宝84","img":"http://pay.fricobloc.com/paycode/5d369f6b1f418.jpg","alias":"宝4","receivemoney":"0.00","status":"normal","verifystatus":"1","updatetime":"2019-07-23 13:47:24","createtime":"2019-07-23 13:47:24"},{"id":94,"codetype":"3","account":null,"accountname":"微信84","img":"http://pay.fricobloc.com/paycode/5d369e7b0e57a.jpg","alias":"微信84","receivemoney":"14356.23","status":"normal","verifystatus":"1","updatetime":"2019-07-23 13:43:24","createtime":"2019-07-23 13:43:24"}]
     * accountname :
     */

    private String accountname;
    private List<ListBean> list;

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * id : 99
         * codetype : 2
         * account : null
         * accountname : vyvh
         * img : http://pay.fricobloc.com/paycode/5d37f97a2b4b1.jpg
         * alias : gvhvibin
         * receivemoney : 0.00
         * status : normal
         * verifystatus : 1         0:待审核   1：通过   2：驳回
         * updatetime : 2019-07-24 14:23:56
         * createtime : 2019-07-24 14:23:56
         */

        private String id;
        private int codetype;
        private String account;
        private String accountname;
        private String img;
        private String alias;
        private String receivemoney;
        private String status;
        private int verifystatus;
        private String updatetime;
        private String createtime;
        private String alipay_pid;
        private int isdefault;

        public String getId(){
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCodetype() {
            return codetype;
        }

        public void setCodetype(int codetype) {
            this.codetype = codetype;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
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

        public String getAlipayPid() {
            return alipay_pid;
        }

        public void setAlipayPid(String alipayPid) {
            this.alipay_pid = alipayPid;
        }

        public int getIsDefault() {
            return isdefault;
        }

        public void setIsDefault(int isDefault) {
            this.isdefault = isDefault;
        }
    }
}
