package com.frico.easy_pay.core.netty;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.service.NotificationUtils;
import com.frico.easy_pay.ui.activity.response.LoginVO;
import com.frico.easy_pay.ui.activity.response.PingVO;
import com.frico.easy_pay.ui.activity.response.ScoketVO;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.TimeUtil;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class NettyHandler extends CustomHeartbeatHandler<String> {

    private static NettyHandler nettyHandler;

    public static NettyHandler getInstance() {
        if (nettyHandler == null) {
            synchronized (NettyHandler.class) {
                if (nettyHandler == null) {
                    nettyHandler = new NettyHandler();
                }
            }
        }
        return nettyHandler;
    }

    private ChannelHandlerContext handlerContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String data) throws Exception {
        LogUtils.e("---NettyHandler--" + data);
        //这里的data  是Object类型，可以自定义对象去接受
        //判断类型 如果某个type类型是 ** 就是需要心跳回应,  **** 就是需要解析数据进行处理  与后台对接数据类型做处理
        ScoketVO scoketVO = new Gson().fromJson(data, ScoketVO.class);
        if (scoketVO.getType().equals(NettyConst.SUCCESS)) {
            LogUtils.e("----接收到服务端成功回复---");
        } else if (scoketVO.getType().equals(NettyConst.HEARTBEAT_RESP)) {
            LogUtils.e("----接收到服务端心跳回复---");
        } else if (scoketVO.getType().equals(NettyConst.LOGIN)) {
            LogUtils.e("----接收到登录服务端回复---");
        } else if (scoketVO.getType().equals(NettyConst.LOGOUT)) {
            LogUtils.e("----接收到退出服务端回复---");
        } else if (scoketVO.getType().equals(NettyConst.PUBLISH)) {
            String msgType = scoketVO.getMsgtype();
            int msgTypeInt = Integer.parseInt(msgType);
            //解析数据
            if (scoketVO.getMsgtype().equals("2") || (msgTypeInt >= 3 && msgTypeInt <= 8) || msgTypeInt == 12) { //收单页面
                RxBus.get().post(BusAction.ORDER_LIST, "");
            } else if (scoketVO.getMsgtype().equals("13")) { //交易中心页面
                RxBus.get().post(BusAction.DEAL_LIST, "");
            }
            if(TextUtils.equals(scoketVO.getMsgtype(),"88")){
                //成功买币
//                RxBus.get().post(BusAction.SOCKET_ORDER_PUBLIC_NOTIFY, scoketVO.getContent() + "  " + TimeUtil.formatVideoTimeHMS(System.currentTimeMillis()));
                RxBus.get().post(BusAction.SOCKET_ORDER_PUBLIC_NOTIFY, scoketVO.getContent() );
            }else if(TextUtils.equals(scoketVO.getMsgtype(),"99")){
                //成功收款
//                RxBus.get().post(BusAction.SOCKET_ORDER_PUBLIC_NOTIFY, scoketVO.getContent()+ "  " + TimeUtil.formatVideoTimeHMS(System.currentTimeMillis()));
                RxBus.get().post(BusAction.SOCKET_ORDER_PUBLIC_NOTIFY, scoketVO.getContent());
            }else {
                handlerData(ctx, scoketVO);
            }
        }else if(TextUtils.equals(scoketVO.getType(),NettyConst.LOGIN_OUT)){
            //当前用户被其他终端登录挤掉了,终止socket连接
            LogUtils.e("----当前用户被其他终端登录挤掉了---  ：："+ scoketVO.getContent());
            if(NettyConnector.getConnector() != null) {
                //挤掉了，就清空登录数据
                showNotifictionLoginOut(SctApp.getInstance().getApplicationContext());
                Prefer.getInstance().clearData();
//                NettyConnector.getConnector().stop();
            }
        }
    }

    /**
     * 读取的数据进行解析
     *
     * @param channelHandlerContext
     */
    private void handlerData(ChannelHandlerContext channelHandlerContext, ScoketVO scoketVO) {
        //在通知栏生成通知信息
        LogUtils.e("--通知内容--" + scoketVO.getMsgtype() + "----" + scoketVO.getContent());
        RxBus.get().post(BusAction.SOCKET_CONTENT, scoketVO);
    }


    /**
     * 平台有新的成交信息sockect广播通知
     *
     * @param channelHandlerContext
     */
    private void handlerSocketPublicOrderNotifyData(ChannelHandlerContext channelHandlerContext, ScoketVO scoketVO) {
        //在通知栏生成通知信息
        LogUtils.e("--通知内容--" + scoketVO.getMsgtype() + "--平台订单通知--" + scoketVO.getContent());
        RxBus.get().post(BusAction.SOCKET_ORDER_PUBLIC_NOTIFY, scoketVO);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    protected String getPingMsg() {
        PingVO pingVO = new PingVO();
        pingVO.setType(NettyConst.HEARTBEAT);
        String ping = new Gson().toJson(pingVO);
        LogUtils.e("--写数据---" + ping);
        return ping;
    }

    @Override
    protected String getPongMsg() {
        PingVO pingVO = new PingVO();
        pingVO.setType(NettyConst.HEARTBEAT_RESP);
        String pong = new Gson().toJson(pingVO);
        return pong;
    }

    @Override
    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        super.handleWriterIdle(ctx);
        //心跳维持连接状态  发送心跳数据
        sendPingMsg(ctx);
    }

    /**
     * 连接上了
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //执行 SocketBind  调取后台接口 通知后台某某连接上
        handlerContext = ctx;
        login();
        LogUtils.e("---STC 长链接---连接上了");
    }


    /**
     * 连接失败，设置重连
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //断开连接以后尝试连接
        NettyConnector connector = (NettyConnector) Connector.getConnector();
        connector.connect(true);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }

    /**
     * 登录scoket
     */
    public void login() {
        if (!TextUtils.isEmpty(Prefer.getInstance().getToken())) {
            LoginVO loginVO = new LoginVO();
            loginVO.setType("login");
            loginVO.setUser_id(Prefer.getInstance().getUserId());
            loginVO.setExpiretime(Prefer.getInstance().getLoginTime());
            //手机厂商和型号
            String deviceBrand = android.os.Build.BRAND + android.os.Build.MODEL;
            loginVO.setUser_name(Prefer.getInstance().getUserName()+ "_"+ deviceBrand);

            String s = new Gson().toJson(loginVO);



            if (handlerContext != null) {
                handlerContext.writeAndFlush(s + "\n");
                LogUtils.e("--登录---" + s + "----" + handlerContext);
            }else{
                LogUtils.e("--登录---" + s + "--handlerContext 为 空，没发送消息--" + handlerContext);
            }
        }
    }

    /**
     * 退出登录scoket
     */
    public void logout() {
        if (!TextUtils.isEmpty(Prefer.getInstance().getToken())) {
            LoginVO loginVO = new LoginVO();
            loginVO.setType("logout");
            loginVO.setUser_id(Prefer.getInstance().getUserId());
            String s = new Gson().toJson(loginVO);

            LogUtils.e("--退出登录---" + s + "----" + handlerContext);

            if (handlerContext != null) {
                handlerContext.writeAndFlush(s + "\n");
            }
        }
    }


    public void showNotifictionLoginOut(Context context) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification(NotificationUtils.LOGIN_OUT_NOTIFICATION_ID, "易支付", "您的账号在其他终端登录了！");
    }

    public void closeSocket(){
        if(handlerContext != null){
            handlerContext.close();
        }
    }
}
