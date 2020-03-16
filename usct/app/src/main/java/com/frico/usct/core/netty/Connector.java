package com.frico.usct.core.netty;


public class Connector {
    public static IConnector getConnector() {
        return NettyConnector.connect(new NettyHandler());
    }
}
