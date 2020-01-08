package com.frico.easy_pay.core.api;

import com.frico.easy_pay.core.entity.AdVO;
import com.frico.easy_pay.core.entity.ApplyInfo;
import com.frico.easy_pay.core.entity.BankVO;
import com.frico.easy_pay.core.entity.ChongBiInfoVO;
import com.frico.easy_pay.core.entity.DealOrderVO;
import com.frico.easy_pay.core.entity.DealVO;
import com.frico.easy_pay.core.entity.HomeTopNotifyV0;
import com.frico.easy_pay.core.entity.MbDealOrderVO;
import com.frico.easy_pay.core.entity.MbShareVO;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.MessageVO;
import com.frico.easy_pay.core.entity.MoneyDetailsVO;
import com.frico.easy_pay.core.entity.MyGroupInfoVO;
import com.frico.easy_pay.core.entity.MyGroupMemberListVO;
import com.frico.easy_pay.core.entity.NoticeVO;
import com.frico.easy_pay.core.entity.OrderListVO;
import com.frico.easy_pay.core.entity.PayWayListVO;
import com.frico.easy_pay.core.entity.QrcodeVo;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.entity.StartVO;
import com.frico.easy_pay.core.entity.TodayIncomeOrderInfoVO;
import com.frico.easy_pay.core.entity.UpdateVO;
import com.frico.easy_pay.core.entity.WithdrawListBaseVO;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {
    /**
     * 登录
     *
     * @param account
     * @param password
     * @param source
     * @return
     */
    @POST("api_login")
    @FormUrlEncoded
    Observable<Result<MbpUserVO>> login(@Field("account") String account, @Field("password") String password, @Field("source") int source);

    /**
     * 验证码登录
     *
     * @param mobile
     * @param verify
     * @param source
     * @return
     */
    @POST("api_smslogin")
    @FormUrlEncoded
    Observable<Result<MbpUserVO>> loginByInventorCode(@Field("mobile") String mobile, @Field("verify") String verify, @Field("source") int source);


    /**
     * 注册
     *
     * @param invitecode
     * @param username
     * @param password
     * @param repassword
     * @param email
     * @param mobile
     * @param verify
     * @param source
     * @return
     */
    @POST("api_register")
    @FormUrlEncoded
    Observable<Result> register(@Field("invitecode") String invitecode, @Field("username") String username, @Field("password") String password, @Field("repassword") String repassword, @Field("email") String email, @Field("mobile") String mobile, @Field("verify") String verify, @Field("source") int source);

    @POST("api_smsregister")
    @FormUrlEncoded
    Observable<Result<String>> smsRegister(@Field("invitecode") String invitecode, @Field("mobile") String mobile, @Field("verify") String verify, @Field("source") int source);


    /**
     * 获取验证码
     *
     * @param mobile
     * @param event  1注册  2忘记密码
     * @return
     */
    @POST("api_sendmsg")
    @FormUrlEncoded
    Observable<Result> sendmsg(@Field("mobile") String mobile, @Field("event") int event);


    /**
     * 用户协议
     *
     * @return
     */
    @GET("api_agreement")
    Observable<Result<String>> agreement();


    /**
     * 修改密码
     *
     * @param newpass
     * @param renewpass
     * @return
     */
    @PUT("api_changepass")
    @FormUrlEncoded
    Observable<Result> changepass(@Field("newpass") String newpass, @Field("renewpass") String renewpass);


    /**
     * 找回密码
     *
     * @param mobile
     * @param verify
     * @param newpass
     * @param renewpass
     * @return
     */
    @POST("api_findpass")
    @FormUrlEncoded
    Observable<Result> findpass(@Field("mobile") String mobile, @Field("verify") String verify, @Field("newpass") String newpass, @Field("renewpass") String renewpass);


    /**
     * 修改支付密码
     *
     * @param newpass
     * @param renewpass
     * @return
     */
    @PUT("api_changepaypass")
    @FormUrlEncoded
    Observable<Result> changepaypass(@Field("newpass") String newpass, @Field("renewpass") String renewpass);


    /**
     * 我的分享
     *
     * @return
     */
    @GET("api_share")
    Observable<Result<MbShareVO>> share();


    /**
     * 上传文件
     *
     * @return
     */
    @Multipart
    @POST("api_uploadimg")
    Observable<Result<String>> uploadimg(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> map);


    /**
     * 添加收款方式
     */
    @POST("api_paycodeadd")
    @FormUrlEncoded
    Observable<Result> paycodeadd(@Field("codetype") int codetype, @Field("img") String img, @Field("accountname") String accountname, @Field("account") String account, @Field("alias") String alias, @Field("alipay_pid") String alipay_pid);


    /**
     * 收单列表
     *
     * @return
     */
    @GET("api_orderlist")
    Observable<Result<OrderListVO>> orderlist(@Query("page") int page, @Query("status") int status,
                                              @Query("paytype") int paytype,
                                              @Query("start") String startTime,
                                              @Query("end") String endTime);

    /**
     * 提现列表
     *
     * @return
     */
    @GET("api_withdrawlist")
    Observable<Result<WithdrawListBaseVO>> withdrawlist(@Query("page") int page);


    /**
     * 取消收单
     *
     * @param ordernum
     * @return
     */
    @HTTP(method = "DELETE", path = "api_ordercancle", hasBody = true)
    @FormUrlEncoded
    Observable<Result> ordercancle(@Field("ordernum") String ordernum);


    /**
     * 确认放币
     *
     * @param ordernum
     * @param paypassword
     * @return
     */
    @PUT("api_orderok")
    @FormUrlEncoded
    Observable<Result> orderok(@Field("ordernum") String ordernum, @Field("paypassword") String paypassword);


    /**
     * 交易订单列表
     *
     * @param status
     * @return
     */
    @GET("api_tradeorder")
    Observable<Result<MbDealOrderVO>> tradeorder(@Query("page") int page, @Query("status") int status);


    /**
     * 交易中心
     *
     * @param page
     * @return
     */
    @GET("api_tradeadvert")
    Observable<Result<DealVO>> tradeadvert(@Query("page") int page);

    /**
     * 激活接口
     *
     * @return
     */
    @GET("api_activate")
    Observable<Result<DealVO>> vipActivate();


    /**
     * 交易抢单购买
     *
     * @param advertno
     * @param amount
     * @return
     */
    @POST("api_buytradeadvert")
    @FormUrlEncoded
    Observable<Result<String>> buytradeadvert(@Field("advertno") String advertno, @Field("amount") String amount);


    /**
     * 确认订单界面
     *
     * @param orderno
     * @return
     */
    @POST("api_gopay")
    @FormUrlEncoded
    Observable<Result<DealOrderVO>> gopay(@Field("orderno") String orderno);

    /**
     * 提现
     *
     * @param
     * @return
     */
    @POST("api_withdraw")
    @FormUrlEncoded
    Observable<Result> withdraw(@Field("withmoney") String withmoney, @Field("bankcard") String bankcard, @Field("paypassword") String paypassword);


    /**
     * 我已付款
     *
     * @param orderno
     * @param payway
     * @param username
     * @return
     */
    @POST("api_paycomplete")
    @FormUrlEncoded
    Observable<Result> paycomplete(@Field("orderno") String orderno, @Field("payway") int payway, @Field("username") String username, @Field("paypassword") String paypassword, @Field("transferimage") String transferimage);


    /**
     * 取消付款
     *
     * @param orderno
     * @return
     */
    @HTTP(method = "DELETE", path = "api_paycancle", hasBody = true)
    @FormUrlEncoded
    Observable<Result> paycancle(@Field("orderno") String orderno);


    /**
     * 广告列表
     *
     * @param status
     * @return
     */
    @GET("api_advert")
    Observable<Result<AdVO>> advert(@Query("status") int status, @Query("tradeway") int tradeway, @Query("page") int page);

    /**
     * 申请信息会员
     *
     * @return
     */
    @GET("api_apply_info")
    Observable<Result<ApplyInfo>> applyInfo(@Query("type") int type);

    /**
     * 代理/超级会员申请 
     *
     * @return
     */
    @POST("api_agent_apply")
    @FormUrlEncoded
    Observable<Result<DealOrderVO>> agentApply(@Field("real_name") String real_name, @Field("mobile") String mobile, @Field("type") int type);

    /**
     * 发布广告
     *
     * @param amount
     * @param amountmin
     * @param amountmax
     * @param tradeway
     * @return
     */
    @POST("api_publishadvert")
    @FormUrlEncoded
    Observable<Result> publishadvert(@Field("amount") String amount, @Field("amountmin") String amountmin, @Field("amountmax") String amountmax, @Field("tradeway") String tradeway);


    /**
     * 下架广告
     *
     * @param advertno
     * @return
     */
    @HTTP(method = "DELETE", path = "api_cancleadvert", hasBody = true)
    @FormUrlEncoded
    Observable<Result> cancleadvert(@Field("advertno") String advertno);


    /**
     * 验证旧手机号
     *
     * @param mobile
     * @param verify
     * @return
     */
    @PUT("api_changemobile")
    @FormUrlEncoded
    Observable<Result> changemobile(@Field("mobile") String mobile, @Field("verify") String verify);


    /**
     * 验证旧手机号
     *
     * @param mobile
     * @param verify
     * @return
     */
    @PUT("api_changenewmobile")
    @FormUrlEncoded
    Observable<Result> changenewmobile(@Field("mobile") String mobile, @Field("verify") String verify);


    /**
     * 交易订单放币
     *
     * @param orderno
     * @return
     */
    @PUT("api_tradeorderok")
    @FormUrlEncoded
    Observable<Result> tradeorderok(@Field("orderno") String orderno, @Field("paypassword") String paypassword);


    /**
     * 添加银行卡
     *
     * @param bankname
     * @param subbranch
     * @param accountname
     * @param cardnumber
     * @param cardmobile
     * @param alias
     * @return
     */
    @POST("api_bankadd")
    @FormUrlEncoded
    Observable<Result> bankadd(@Field("bankname") String bankname, @Field("subbranch") String subbranch, @Field("accountname") String accountname, @Field("cardnumber") String cardnumber, @Field("cardmobile") String cardmobile, @Field("alias") String alias);


    /**
     * 获取邮箱验证码
     *
     * @param email
     * @return
     */
    @POST("api_sendemail")
    @FormUrlEncoded
    Observable<Result> sendemail(@Field("email") String email);


    /**
     * 修改邮箱
     *
     * @param email
     * @return
     */
    @PUT("api_changeemail")
    @FormUrlEncoded
    Observable<Result> changeemail(@Field("email") String email, @Field("verify") String verify);


    /**
     * 获取会员用户信息
     *
     * @return
     */
    @GET("api_getusernfo")
    Observable<Result<MbpUserVO>> getusernfo();


    /**
     * 获取会员今日交易信息
     *
     * @return
     */
    @GET("api_dayreceiveinfo")
    Observable<Result<TodayIncomeOrderInfoVO>> getUserTodayOrderInfo();

    /**
     * 交易订单申诉
     *
     * @param orderno
     * @param reason
     * @param image
     * @return
     */
    @POST("api_appeal")
    @FormUrlEncoded
    Observable<Result> appeal(@Field("orderno") String orderno, @Field("reason") String reason, @Field("image") String image);


    /**
     * 获取公告
     *
     * @return
     */
    @GET("api_notice")
    Observable<Result<List<NoticeVO>>> notice();


    /**
     * 获取消息
     *
     * @return
     */
    @GET("api_message")
    Observable<Result<MessageVO>> message(@Query("page") int page);


    /**
     * 读取消息
     *
     * @param id
     * @return
     */
    @PUT("api_readmessage")
    @FormUrlEncoded
    Observable<Result> readmessage(@Field("id") String id);


    /**
     * 删除收单
     *
     * @param ordernum
     * @return
     */
    @HTTP(method = "DELETE", path = "api_orderdel", hasBody = true)
    @FormUrlEncoded
    Observable<Result> orderdel(@Field("ordernum") String ordernum);


    /**
     * 短信  支付宝  微信的通知消息
     *
     * @param type
     * @param content
     * @return
     */
    @POST("api_sendnotice")
    @FormUrlEncoded
    Observable<Result> sendnotice(@Field("type") int type, @Field("content") String content);


    /**
     * 收款码列表
     *
     * @return
     */
    @GET("api_paycodelist")
    Observable<Result<PayWayListVO>> paycodelist();


    /**
     * 收款切换状态
     *
     * @param id
     * @return
     */
    @PUT("api_paycodeswitch")
    @FormUrlEncoded
    Observable<Result> paycodeswitch(@Field("id") String id);


    /**
     * 收款切换状态
     *
     * @param id
     * @return
     */
    @PUT("api_bankswitch")
    @FormUrlEncoded
    Observable<Result> bankswitch(@Field("id") String id);


    /**
     * 编辑收款码
     *
     * @param id
     * @param codetype
     * @param img
     * @param accountname
     * @param account
     * @param alias
     * @return
     */
    @PUT("api_paycodeupdate")
    @FormUrlEncoded
    Observable<Result> paycodeupdate(@Field("id") String id, @Field("codetype") int codetype, @Field("img") String img, @Field("accountname") String accountname, @Field("account") String account, @Field("alias") String alias, @Field("alipay_pid") String alipay_pid);


    /**
     * 银行卡列表
     *
     * @return
     */
    @GET("api_banklist")
    Observable<Result<BankVO>> banklist();


    /**
     * 编辑银行卡
     *
     * @param id
     * @param bankname
     * @param subbranch
     * @param accountname
     * @param cardnumber
     * @param cardmobile
     * @param alias
     * @return
     */
    @PUT("api_bankupdate")
    @FormUrlEncoded
    Observable<Result> bankupdate(@Field("id") String id, @Field("bankname") String bankname, @Field("subbranch") String subbranch, @Field("accountname") String accountname, @Field("cardnumber") String cardnumber, @Field("cardmobile") String cardmobile, @Field("alias") String alias);


    /**
     * 删除银行卡
     *
     * @param id
     * @return
     */
    @HTTP(method = "DELETE", path = "api_bankdel", hasBody = true)
    @FormUrlEncoded
    Observable<Result> bankdel(@Field("id") String id, @Field("type") String type);


    /**
     * 可用资产明细
     *
     * @param type
     * @param page
     * @return
     */
    @GET("api_moneylog")
    Observable<Result<MoneyDetailsVO>> moneylog(@Query("type") int type, @Query("page") int page);


    /**
     * 冻结资产明细
     *
     * @param type
     * @param page
     * @return
     */
    @GET("api_frozemoneylog")
    Observable<Result<MoneyDetailsVO>> frozemoneylog(@Query("type") int type, @Query("page") int page);


    /**
     * 收益资产明细
     *
     * @param type
     * @param page
     * @return
     */
    @GET("api_profitlog")
    Observable<Result<MoneyDetailsVO>> profitlog(@Query("type") int type, @Query("page") int page);


    /**
     * 赠送资产明细
     *
     * @param type
     * @param page
     * @return
     */
    @GET("api_givemoneylog")
    Observable<Result<MoneyDetailsVO>> givemoneylog(@Query("type") int type, @Query("page") int page);


    /**
     * 检测版本更新
     *
     * @return
     */
    @GET("api_newversion")
    Observable<Result<UpdateVO>> newversion();


    /**
     * 获取广告页
     *
     * @return
     */
    @GET("api_startimg")
    Observable<Result<StartVO>> startimg();

    /**
     * 获取通知
     *
     * @return
     */
    @GET("api_newnotice")
    Observable<Result<HomeTopNotifyV0>> getNoticeToday();

    /**
     * 获取充币信息
     *
     * @return
     */
    @GET("api_chongbiinfo")
    Observable<Result<ChongBiInfoVO>> getChongBiInfo();

    /**
     * 提交确认充币/提币信息  1：充币，2：提币
     *
     * @return
     */
    @POST("api_subbi")
    @FormUrlEncoded
    Observable<Result> submitChongBi(@Field("type") String type, @Field("amount") String amount, @Field("address") String address, @Field("paypassword") String paypassword);

    /**
     * 获取收款二维码
     *
     * @return
     */
    @GET("api_receiveqrcode")
    Observable<Result<QrcodeVo>> getReceiveQrCode();

    /**
     * 统计下载量
     *
     * @param version
     * @return
     */
    @PUT("api_downloadnum")
    @FormUrlEncoded
    Observable<Result> downloadnum(@Field("version") String version);


    /**
     * 更改用户图像
     *
     * @param avaterUrl
     * @return
     */
    @PUT("api_setavater")
    @FormUrlEncoded
    Observable<Result> uploadUserHeaderUrl(@Field("avater") String avaterUrl);

    /**
     * 更改用户昵称
     *
     * @param username
     * @return
     */
    @PUT("api_setusername")
    @FormUrlEncoded
    Observable<Result> uploadUserNickName(@Field("username") String username);


    /**
     * 收款开关
     * normal  开
     * hidden 关
     *
     * @return
     */
    @PUT("api_changepaymentstatus")
    @FormUrlEncoded
    Observable<Result> changepaymentstatus(@Field("paymentstatus") String paymentstatus);


    /**
     * 自动收款回调开关
     * normal  开
     * hidden 关
     *
     * @return
     */
    @PUT("api_changeorderautostatus")
    @FormUrlEncoded
    Observable<Result> changeOrderAutoStatus(@Field("orderautostatus") String orderAutoStatus);

    /**
     * 一键转余额（佣金）
     *
     * @return
     */
    @PUT("api_profitsettlement")
    Observable<Result> profitsettlement();

    /**
     * 获取团队信息
     *
     * @return
     */
    @GET("api_myteam")
    Observable<Result<MyGroupInfoVO>> getMyTeam(@Query("acqid") String acqid);

    /**
     * 获取我的团队成员 的团队成员
     *
     * @return
     */
    @GET("api_myteamlist")
    Observable<Result<MyGroupMemberListVO>> getMyTeamLower(@Query("acqid") String acqid, @Query("page") int page);

    /**
     * 二维码转账
     * <p>
     * @param  type        -{int}            0-转账，1-提现（默认0）
     * @param  paycode   -{string}           二维码地址(type == 1必传)
     *  @param  paycompany -{string}          支付公司（微信支付/支付宝）
     * @return
     */
    @POST("api_transfer")
    @FormUrlEncoded
    Observable<Result<MyGroupMemberListVO>> transferBalance(@Field("type") int type, @Field("amount") String amount, @Field("paypassword") String paypassword,@Field("paycode") String paycode,@Field("paycompany") String paycompany);

    /**
     * 会员间转账
     *
     * @return
     */
    @POST("api_transfer")
    @FormUrlEncoded
    Observable<Result<MyGroupMemberListVO>> transferBalance(@Field("acqid") String acqid, @Field("amount") String amount, @Field("paypassword") String paypassword);

    /**
     * 会员间转账
     *
     * @return
     */
    @POST("api_transfer")
    @FormUrlEncoded
    Observable<Result<MyGroupMemberListVO>> transferBalance(@Field("acqid") String acqid, @Field("amount") String amount, @Field("paypassword") String paypassword, @Field("isnum") int isnum);

    /**
     * 会员间转账
     * 收款码ID
     * 类型:2=支付宝,3=微信
     *
     * @return
     */
    @PUT("api_setdefaultcode")
    @FormUrlEncoded
    Observable<Result<MyGroupMemberListVO>> setDefaultCode(@Field("id") String id, @Field("codetype") String codetype);


    /**
     * 上传身份证正反面地址
     * @param type type -上传图片用途类型（1-身份认证）
     * @param image 图片
     * @return
     */
    @POST("api_upload")
    @FormUrlEncoded
    Observable<Result<StartVO>> uploadImg(@Field("type")int type, @Field("image")File image);

    /**
     * 实名认证
     * @param name 名字
     * @param card_no   身份证号
     * @param card_obverse_side 正面地址
     * @param card_reverse_side 反面地址
     * @return
     */
    @POST("api_certification")
    @FormUrlEncoded
    Observable<Result<StartVO>> certification(@Field("name") String name,@Field("card_no") String card_no,@Field("card_obverse_side")String card_obverse_side,@Field("card_reverse_side") String card_reverse_side);

}
