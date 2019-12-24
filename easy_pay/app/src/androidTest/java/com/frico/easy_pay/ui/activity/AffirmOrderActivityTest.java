package com.frico.easy_pay.ui.activity;

import com.frico.easy_pay.core.entity.DealOrderVO;

import org.junit.Test;

import static org.junit.Assert.*;

public class AffirmOrderActivityTest {
    private DealOrderVO dealOrderVO;

    @Test
    public void goPay() {
        dealOrderVO = new DealOrderVO("50000.00",
                "1",
                "500000",
                "201912050710536172",
                -1073492,
                "1,3,4,5",
                new DealOrderVO.TradeinfoBean(
                        new DealOrderVO.TradeinfoBean._$1Bean("11","中国农业银行","中国农业银行龙港支行","11","13545145523434"),
                        new DealOrderVO.TradeinfoBean._$2Bean("1","1","1"),
                        new DealOrderVO.TradeinfoBean._$2Bean("1","1","1"),
                        new DealOrderVO.TradeinfoBean._$2Bean("1","1","1"),
                        new DealOrderVO.TradeinfoBean._$2Bean("1","1","1")
                        ));
    }
}