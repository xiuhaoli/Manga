package cn.xhl.client.manga.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.xhl.client.manga.MyActivityManager;
import cn.xhl.client.manga.R;

/**
 * Created by lixiuhao on 2017/5/9 0009.
 * <p>
 * 基类
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public Activity this_;
    private QMUITopBar topBar;// 标题栏
    //        private QMUITopBarLayout topBar;
    private QMUITipDialog loadingDialog;
    private QMUITipDialog failDialog;
    private QMUITipDialog successDialog;
    private LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        this_ = this;
        initView();
        setBaseContentView(layoutId());

        MyActivityManager.add(this_);
    }

    @Override
    protected void onDestroy() {
        MyActivityManager.remove(this_);
        super.onDestroy();
    }

    private void initView() {
        topBar = findViewById(R.id.topbar);
        llContent = findViewById(R.id.v_content); //v_content是在基类布局文件中预定义的layout区域
    }

    public void changeColor(View view, int colorId) {
        view.setBackgroundColor(ContextCompat.getColor(this_, colorId));
    }

    public void changeTopbarColor(int colorId) {
        changeColor(topBar, colorId);
    }

    /**
     * 添加顶部返回按钮
     */
    public void addLeftTopBackButton() {
        topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void hideTopBar() {
        topBar.setVisibility(View.GONE);
    }

    public void showTopBar() {
        topBar.setVisibility(View.VISIBLE);
    }

    public void changeTitle(String str) {
        topBar.setTitle(str);
    }

    public void changeTitle(int resId) {
        topBar.setTitle(resId);
    }

    /**
     * 创建loading
     *
     * @return
     */
    public QMUITipDialog createLoading(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg)
                    .create();
        }
        return loadingDialog;
    }

    public QMUITipDialog createLoading() {
        return createLoading("正在加载");
    }

    /**
     * 创建失败框
     *
     * @return
     */
    public QMUITipDialog createFail(String msg) {
        if (failDialog == null) {
            failDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord(msg)
                    .create();
        }
        return failDialog;
    }

    public QMUITipDialog createFail() {
        return createFail("发送失败");
    }

    /**
     * 创建成功框
     *
     * @return
     */
    public QMUITipDialog createSuccess(String msg) {
        if (successDialog == null) {
            successDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(msg)
                    .create();
        }
        return successDialog;
    }

    public QMUITipDialog createSuccess() {
        return createSuccess("发送成功");
    }

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            createLoading();
        }
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        loadingDialog.cancel();
    }

    public void showSuccessDialog() {
        successDialog.show();
    }

    public void hideSuccessDialog() {
        successDialog.cancel();
    }

    public void showFailDialog() {
        failDialog.show();
    }

    public void hideFailDialog() {
        failDialog.cancel();
    }

    public void showToast(String str, int time) {
        Toast.makeText(this, str, time).show();
    }

    public void showToast(String str) {
        showToast(str, Toast.LENGTH_SHORT);
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void closeBaseActivity() {
        this.finish();
    }

    /**
     * 子类setLayout
     *
     * @return layout id
     */
    protected abstract int layoutId();

    /**
     * set screen view
     *
     * @param layoutResID
     */
    private void setBaseContentView(int layoutResID) {
        if (layoutResID == 0) {
            return;
        }
        // 通过LayoutInflater填充基类的layout区域
        LayoutInflater inflater = LayoutInflater.from(this_);
        View v = inflater.inflate(layoutResID, llContent, false);
        llContent.addView(v);

    }

    public void changeBackgroundColor(int color) {
        if (Build.VERSION.SDK_INT >= 23) {
            llContent.setBackgroundColor(getResources().getColor(color, null));
        } else {
            llContent.setBackgroundColor(getResources().getColor(color));
        }
    }

    /**
     * 从edittext上获取数据
     *
     * @param editText
     * @return
     */
    public String getTextFromEt(EditText editText) {
        if (editText == null) {
            return "";
        }
        return editText.getText().toString();
    }
}
