package com.frico.usct.core.entity;

import java.util.List;

/**
 * 我的团队列表
 */
public class MyGroupMemberListVO {

    /**
     * total : 40
     * per_page : 10
     * current_page : 1
     * last_page : 4
     * data : [{"id":402,"ordernum":"pd3fbf34d2a7fd6e5d8c6-876","amount":"11.9000","paytype":"3","payamount":"0.0000","payaccount":"","paytime":null,"createtime":"2019-07-22 11:18:09","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"1","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1112},{"id":401,"ordernum":"pd2169cb664fd3550061e-905","amount":"6.9000","paytype":"3","payamount":"0.0000","payaccount":"","paytime":null,"createtime":"2019-07-22 11:17:49","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"1","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1132},{"id":400,"ordernum":"pf72d41286065af82d8ba-945","amount":"13.9000","paytype":"3","payamount":"0.0000","payaccount":"","paytime":null,"createtime":"2019-07-22 11:16:44","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"1","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1197},{"id":399,"ordernum":"pf33bf6f3ddc3d3ef1ec6-482","amount":"6.9000","paytype":"3","payamount":"0.0000","payaccount":"","paytime":null,"createtime":"2019-07-22 11:16:28","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"1","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1213},{"id":398,"ordernum":"ped2be9adeedf38e4e7d3-505","amount":"15.9000","paytype":"3","payamount":"0.0000","payaccount":"","paytime":null,"createtime":"2019-07-22 11:13:38","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"1","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1383},{"id":397,"ordernum":"p1c0baf4bb3120064a4f4-455","amount":"13.9000","paytype":"3","payamount":"13.9000","payaccount":"cb","paytime":"2019-07-22 11:13","createtime":"2019-07-22 11:13:34","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"4","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1387},{"id":396,"ordernum":"pc8f15bab179cff4594b0-712","amount":"13.9000","paytype":"3","payamount":"0.0000","payaccount":"","paytime":null,"createtime":"2019-07-22 11:11:07","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"1","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1534},{"id":395,"ordernum":"p20742bd4026a101e0e14-120","amount":"8.9000","paytype":"3","payamount":"8.9000","payaccount":"cv","paytime":"2019-07-22 11:11","createtime":"2019-07-22 11:11:03","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"4","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1538},{"id":394,"ordernum":"pe2dcc308632a99ae74d2-908","amount":"73.0000","paytype":"3","payamount":"73.0000","payaccount":"cv","paytime":"2019-07-22 11:10","createtime":"2019-07-22 11:10:06","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"4","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":1595},{"id":393,"ordernum":"pfe54bafabf9f725702c4-992","amount":"14.8000","paytype":"3","payamount":"14.8000","payaccount":"1","paytime":"2019-07-20 11:12","createtime":"2019-07-20 11:12:06","info":{"id":83,"accountname":"陈彬73","img":"http://pay.fricobloc.com/uploads/2019_07_17/5d2eff015f36d.png","alias":"73"},"status":"4","callbackstatus":"0","bank_or_code":83,"acq_profit_percentage":null,"acqprofit":"0.0000","receivetime":null,"difftime":174275}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<MyGroupItemVO> data;

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

    public List<MyGroupItemVO> getData() {
        return data;
    }

    public void setData(List<MyGroupItemVO> data) {
        this.data = data;
    }
}
