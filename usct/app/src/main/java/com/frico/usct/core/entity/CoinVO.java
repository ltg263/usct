package com.frico.usct.core.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

public class CoinVO implements CustomTabEntity {
    private String title;

    public CoinVO(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
