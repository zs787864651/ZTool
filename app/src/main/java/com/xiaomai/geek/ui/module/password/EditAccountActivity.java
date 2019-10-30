
package com.xiaomai.geek.ui.module.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.InputMethodUtils;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.ActivityComponent;
import com.xiaomai.geek.di.component.DaggerActivityComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.event.PasswordEvent;
import com.xiaomai.geek.presenter.password.EditAccountPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.view.IEditAccountView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/3/30 14:01.
 */

public class EditAccountActivity extends BaseActivity
        implements IEditAccountView, IComponent<ActivityComponent> {

    public static final int MODE_CREATE = 1;

    public static final int MODE_UPDATE = 2;

    private static final String EXTRA_PASSWORD = "EXTRA_CONTENT";

    @BindView(R.id.iv_generate_pwd)
    ImageView ivGeneratePwd;

    private int mCurrentMode = MODE_CREATE;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.edit_platform)
    EditText editPlatform;

    @BindView(R.id.edit_userName)
    EditText editUserName;

    @BindView(R.id.edit_password)
    EditText editPassword;

    @BindView(R.id.edit_note)
    EditText editNote;

    @BindView(R.id.layout_platform)
    TextInputLayout layoutPlatform;

    @BindView(R.id.layout_userName)
    TextInputLayout layoutUserName;

    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;

    @BindView(R.id.circle_view_icon)
    ImageView circleViewIcon;

    private String mPlatform;

    private String mUserName;

    private String mPassword;

    private String mNote;

    private int mPasswordId;

    private EditAccountPresenter mPresenter;

    private final ColorGenerator mGenerator = ColorGenerator.MATERIAL;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, EditAccountActivity.class));
    }

    public static void launch(Context context, Password password) {
        Intent intent = new Intent(context, EditAccountActivity.class);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        Password password = intent.getParcelableExtra(EXTRA_PASSWORD);
        if (password == null) {
            return;
        }
        mCurrentMode = MODE_UPDATE;
        mPasswordId = password.getId();
        editPlatform.setText(password.getPlatform());
        editUserName.setText(password.getUserName());
        editPassword.setText(password.getPassword());
        editNote.setText(password.getNote());
    }

    private void initViews() {
        mPresenter = new EditAccountPresenter();
        mPresenter.attachView(this);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editPlatform.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String platform = editPlatform.getText().toString().trim();
                if (platform.length() > 0) {
                    String substring = platform.substring(0, 1);
                    TextDrawable textDrawable = TextDrawable.builder().beginConfig().toUpperCase()
                            .endConfig().buildRound(substring, mGenerator.getColor(platform));
                    circleViewIcon.setImageDrawable(textDrawable);
                } else {
                    circleViewIcon.setImageResource(R.drawable.ic_jike);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutPlatform.setErrorEnabled(false);
            }
        });
        editUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutUserName.setErrorEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 必须先调用setSupportActionBar(toolBar),否则菜单不显示
         */
        getMenuInflater().inflate(R.menu.edit_account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_ok:
                saveInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveInfo() {
        mPlatform = editPlatform.getText().toString().trim();
        mUserName = editUserName.getText().toString().trim();
        mPassword = editPassword.getText().toString().trim();
        mNote = editNote.getText().toString().trim();
        if (mCurrentMode == MODE_CREATE) {
            mPresenter.savePassword(mContext, mPlatform, mUserName, mPassword, mNote);
        } else if (mCurrentMode == MODE_UPDATE) {
            mPresenter.updatePassword(mContext, mPasswordId, mPlatform, mUserName, mPassword,
                    mNote);
        }
    }

    @Override
    public void onSaveComplete(boolean save) {
        if (save) {
            EventBus.getDefault().post(new PasswordEvent(PasswordEvent.TYPE_ADD, new Password()));
            finish();
        } else {
            Snackbar.make(toolBar, "保存失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpdateComplete(boolean update) {
        if (update) {
            EventBus.getDefault()
                    .post(new PasswordEvent(PasswordEvent.TYPE_UPDATE, new Password()));
            finish();
        } else {
            Snackbar.make(toolBar, "修改失败", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlatformError() {
        layoutPlatform.setError("不能为空");
        layoutPlatform.setEnabled(true);
    }

    @Override
    public void onUserNameError() {
        layoutUserName.setError("不能为空");
        layoutUserName.setEnabled(true);
    }

    @Override
    public void onPasswordError() {
        layoutPassword.setError("不能为空");
        layoutPassword.setEnabled(true);
    }

    @Override
    public Password generateRandomPassword() {
        return null;
    }

    @Override
    public ActivityComponent getComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(GeekApplication.get(mContext).getComponent())
                .activityModule(new ActivityModule(this)).build();
    }

    private int mLength = 6;

    private int mPasswordType = EditAccountPresenter.TYPE_ALL;

    private AlertDialog mDialog = null;

    @OnClick(R.id.iv_generate_pwd)
    public void onClick() {
        InputMethodUtils.hideSoftInput(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_generate_pwd, null);
        final TextView tvPassword = (TextView) view.findViewById(R.id.tv_password);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        final TextView tvLength = (TextView) view.findViewById(R.id.tv_length);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        View refresh = view.findViewById(R.id.refresh);
        final View cancel = view.findViewById(R.id.bt_cancel);
        View ok = view.findViewById(R.id.bt_ok);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLength = progress + 6;
                tvLength.setText("密码长度：" + mLength);
                tvPassword.setText(getPassword());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mPasswordType = EditAccountPresenter.TYPE_ALL;
                        break;
                    case 1:
                        mPasswordType = EditAccountPresenter.TYPE_NUM_LETTER;
                        break;
                    case 2:
                        mPasswordType = EditAccountPresenter.TYPE_NUM;
                        break;
                    case 3:
                        mPasswordType = EditAccountPresenter.TYPE_LETTER;
                        break;
                }
                tvPassword.setText(getPassword());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPassword.setText(getPassword());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                mLength = 6;
                mPasswordType = EditAccountPresenter.TYPE_ALL;
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                editPassword.setText(mPassword);
            }
        });
        mDialog = new AlertDialog.Builder(mContext).setView(view).create();
        mDialog.show();
    }

    private String getPassword() {
        mPassword = mPresenter.generatePassword(mPasswordType, mLength);
        return mPassword;
    }
}
