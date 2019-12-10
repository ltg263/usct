package com.frico.easy_pay.core.netty;


public class Connector {
    public static IConnector getConnector() {
        return NettyConnector.connect(new NettyHandler());
    }
}
