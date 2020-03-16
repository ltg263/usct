package com.frico.usct.core.netty;

import com.frico.usct.core.utils.Constants;
import com.frico.usct.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyConnector implements IConnector {
    private static final String TAG = NettyConnector.class.getSimpleName();
    private static NettyConnector connector;

    private static final int RE_CONN_WAIT_SECONDS = 5;    //多长时间为请求后，发送心跳
    private ScheduledExecutorService executorService;
    private Channel channel;
    private Bootstrap b;
    private int connectIndex = 0;
    private static List<HostPort> hostPortList = new ArrayList<>();

    private boolean mIsLoginOut;//是否是被挤掉的

    private boolean isNeedReconnect = true;//是否需要重连

    public static NettyConnector connect(ChannelHandler channelHandler) {
        if (connector == null) {
            synchronized (NettyConnector.class) {
                if (connector == null) {
                    connector = new NettyConnector(channelHandler);
                }
            }
        }
        return connector;
    }

    public static NettyConnector getConnector(){
        return connector;
    }

    public static void addHostPort() {
        String host = Constants.APP_HOST;
        int port = Constants.APP_PORT;
        hostPortList.clear();
        LogUtils.e("socket","host = "+ host+" -- "+ port);
        hostPortList.add(new HostPort(host, port));
    }

    private NettyConnector(final ChannelHandler channelHandler) {
        EventLoopGroup group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //使用换行符分包
                        ch.pipeline().addLast(new LineBasedFrameDecoder(2048)); //如果有换行策略的话 可以加上
                        ch.pipeline().addLast(new IdleStateHandler(0, 20, 0,TimeUnit.SECONDS));  //修改写数据的时间间隔
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(channelHandler);
                    }
        });
    }

    //停止服务
    public void stop() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }



    public synchronized void connect(boolean isLogin) {
        if(isLogin){
            isNeedReconnect = true;
        }
        if (hostPortList.isEmpty()) {
            throw new RuntimeException("remote server is empty");
        }
        if (executorService != null && !executorService.isShutdown()) {
            return;
        }
        //以固定延迟（时间）来反复进行重连，用于开始没有连接成功的情况
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(new Runnable() {
            boolean success = false;

            @Override
            public void run() {
                try {
                    //连接服务器
                    if (!isConnected()) {
                        HostPort hostPort = hostPortList.get(connectIndex);
                        synchronized (NettyConnector.class) {
                            channel = b.connect(hostPort.host, hostPort.port).channel();
                            success = channel.isActive();


//                            b.connect(hostPort.host, hostPort.port).addListener(new ChannelFutureListener() {
//                                @Override
//                                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                                    if (channelFuture.isSuccess()) {
//                                        LogUtils.e(TAG, "连接成功");
//                                        success = true;
//                                        channel = channelFuture.channel();
//                                    } else {
//                                        LogUtils.e(TAG, "连接失败");
//                                        success = false;
//                                    }
//                                }
//                            }).sync();

                        }
                    } else {
                        success = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    success = false;
                    if (connectIndex == (hostPortList.size() - 1)) {
                        connectIndex = 0;
                    } else {
                        connectIndex++;
                    }
                } finally {
                    if (success || ! isNeedReconnect) {
                        if (executorService != null) {
                            executorService.shutdown();
                            executorService = null;
                        }
                    }
                }
            }
        }, 0, RE_CONN_WAIT_SECONDS, TimeUnit.SECONDS);

    }

    /**
     * 判断是否连接上
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (channel != null && channel.isActive()) {
            return true;
        }
        return false;
    }

    private static class HostPort {
        HostPort(String host, int port) {
            this.host = host;
            this.port = port;
        }

        private String host;
        private int port;
    }


}
