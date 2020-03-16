package com.frico.usct.ui.activity.me.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountInfoEditNickNameActivity extends BaseActivity implements View.OnClickListener {
    public static String KEY_NICK = "nick";
    @BindView(R.id.tv_cancel_edit_btn)
    TextView tvCancelEditBtn;
    @BindView(R.id.tv_edit_info_title)
    TextView tvEditInfoTitle;
    @BindView(R.id.tv_commit_edit_btn)
    TextView tvCommitEditBtn;
    @BindView(R.id.et_input_cotent)
    EditText etInputCotent;
    @BindView(R.id.iv_edit_clear)
    ImageView ivEditClear;

    private String mCurrentNick;

    public static void start(Activity activity, String nickName,int requestCode) {
        Intent intent = new Intent(activity, AccountInfoEditNickNameActivity.class);
        intent.putExtra(KEY_NICK, nickName);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_account_info_edit;
    }

    @Override
    public void initTitle() {
        tvCancelEditBtn.setOnClickListener(this);
        tvCommitEditBtn.setOnClickListener(this);
        ivEditClear.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mCurrentNick = getIntent().getStringExtra(KEY_NICK);
        if(!TextUtils.isEmpty(mCurrentNick)) {
            etInputCotent.setText(mCurrentNick);
            etInputCotent.setSelection(etInputCotent.getText().toString().length());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if(vId == R.id.tv_cancel_edit_btn){
            //取消
            finish();
        }else if(vId == R.id.tv_commit_edit_btn){
            //提交
            setResult();
        }else if(vId == R.id.iv_edit_clear){
            etInputCotent.setText("");
        }
    }

    private void setResult(){
        String newNickName = etInputCotent.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra(KEY_NICK,newNickName);
        setResult(RESULT_OK,intent);
        finish();
    }
}
