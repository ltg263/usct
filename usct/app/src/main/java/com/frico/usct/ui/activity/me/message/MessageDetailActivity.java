package com.frico.usct.ui.activity.me.message;

import android.os.Build;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.core.entity.MessageItemVO;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;

public class MessageDetailActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private MessageItemVO messageItemVO;

    @Override
    protected int setLayout() {
        return R.layout.activity_message_detail;
    }

    @Override
    public void initTitle() {
        actionbar.setData("消息详情", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        messageItemVO = (MessageItemVO) getIntent().getSerializableExtra("messBean");
        tvContent.setText(messageItemVO.getContent());
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

}
