package com.frico.usct.core.entity;

import java.util.List;

/**
 * 提现记录数据
 */
public class WithdrawListVO {

    /**
     * total : 40
     * per_page : 10
     * current_page : 1
     * last_page : 4
     * data : {
     *         "list":{
     *             "total":2,
     *             "per_page":10,
     *             "current_page":1,
     *             "last_page":1,
     *             "data":[
     *                 {
     *                     "id":4,
     *                     "orderno":"20190927215005600006414",
     *                     "acqid":600006,
     *                     "money":"66.0000",
     *                     "realmoney":"66.0000",
     *                     "cnymoney":"448.8000",
     *                     "servicemoney":"0.0000",
     *                     "bankname":"工商银行",
     *                     "subbranch":"江南大道支行",
     *                     "accountname":"银行",
     *                     "cardnumber":"6226663358477856",
     *                     "status":"2",
     *                     "refusetime":null,
     *                     "refusereason":null,
     *                     "paytime":0,
     *                     "adminip":null,
     *                     "adminid":null,
     *                     "cancleip":null,
     *                     "cancletime":0,
     *                     "updatetime":"2019-09-27 21:50:05",
     *                     "createip":"115.205.0.102",
     *                     "createtime":"2019-09-27 21:50:05"
     *                 },
     *                 {
     *                     "id":3,
     *                     "orderno":"20190927200657600006717",
     *                     "acqid":600006,
     *                     "money":"100.0000",
     *                     "realmoney":"100.0000",
     *                     "cnymoney":"680.0000",
     *                     "servicemoney":"0.0000",
     *                     "bankname":"工商银行",
     *                     "subbranch":"江南大道支行",
     *                     "accountname":"银行",
     *                     "cardnumber":"6226663358477856",
     *                     "status":"2",
     *                     "refusetime":null,
     *                     "refusereason":null,
     *                     "paytime":0,
     *                     "adminip":null,
     *                     "adminid":null,
     *                     "cancleip":null,
     *                     "cancletime":0,
     *                     "updatetime":"2019-09-27 20:06:57",
     *                     "createip":"115.205.0.102",
     *                     "createtime":"2019-09-27 20:06:57"
     *                 }
     *             ]
     *         }     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<WithdrawItemVO> data;

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

    public List<WithdrawItemVO> getData() {
        return data;
    }

    public void setData(List<WithdrawItemVO> data) {
        this.data = data;
    }
}
