package com.leon.chat.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.android.xlibrary.LoadingLayout.LoadingLayout;
import com.android.xlibrary.weixinPic.utils.ToastUtil;
import com.leon.chat.R;
import com.leon.chat.activity.MainActivity;
import com.leon.chat.base.BaseApplication;
import com.leon.chat.base.BaseFragment;
import com.leon.chat.base.BaseNavigationBar;
import com.leon.chat.databinding.FragmentLoginBinding;
import com.leon.chat.service.IMService;
import com.leon.chat.service.PushService;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.ThreadPoolFactory;
import com.leon.chat.utils.UIUtils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;


/**
 * Created by leon on 16/11/1.
 */

public class LoginFragment extends BaseFragment{

    @Override
    public void initData() {
        mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
    }

    @Override
    public void setupNavigationBar(BaseNavigationBar navigationBar) {
        super.setupNavigationBar(navigationBar);
        navigationBar.setNavTitle("登录");
        navigationBar.mNavigationBinding.btnNavBack.setVisibility(View.GONE);
    }

    @Override
    public View initSuccessView() {
        View view = View.inflate(mActivity, R.layout.fragment_login, null);
        FragmentLoginBinding loginBinding = DataBindingUtil.bind(view);
        loginBinding.etAccount.setText("user1");
        loginBinding.etPassword.setText("123456");
        initListener(loginBinding);
        return view;
    }


    private void initListener(final FragmentLoginBinding loginBinding) {
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount(loginBinding);
            }
        });
    }

    private void loginAccount(FragmentLoginBinding loginBinding) {
        mActivity.showLoadingDialog();
        final String account = loginBinding.etAccount.getText().toString().trim();
        final String password = loginBinding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            ToastUtil.show(mActivity, "手机号不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.show(mActivity, "密码不能为空");
            return;
        }

        ThreadPoolFactory.getNormalProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionConfiguration configuration = new ConnectionConfiguration(BaseApplication.IP,BaseApplication.PORT);
                    configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); // 明文传输
                    configuration.setDebuggerEnabled(true); // 调试模式
                    XMPPConnection conn = new XMPPConnection(configuration);
                    conn.connect();
                    conn.login(account,password);
                    IMService.conn = conn;
                    IMService.curAccount = account + "@" +BaseApplication.REALM_NAME;

                    Intent imServiceIntent = new Intent(mActivity,IMService.class);
                    mActivity.startService(imServiceIntent);

                    Intent pushIntent = new Intent(mActivity,PushService.class);
                    mActivity.startService(pushIntent);

                    launchActivity(MainActivity.class);
                    mActivity.destroy();
                } catch (XMPPException e) {
                    e.printStackTrace();
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(mActivity,"登录失败！");
                        }
                    });
                }finally {
                    mActivity.hideLoadingDialog();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        LogUtils.sf("LoginFragment销毁");
        super.onDestroy();
    }
}
