package com.frico.easy_pay.impl;

import com.frico.easy_pay.refresh.PtrFrameLayout;

public interface PtrHandler {

    /**
     * When refresh begin
     *
     * @param frame
     */
    public void onRefreshBegin(final PtrFrameLayout frame);
}