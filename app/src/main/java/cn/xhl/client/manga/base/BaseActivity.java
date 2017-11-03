package cn.xhl.client.manga.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.xhl.client.manga.MyActivityManager;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.custom.QMUITipDialog;

public abstract class BaseActivity extends RxAppCompatActivity {
    public Activity this_;
    private View topBar;
    private TextView title;
    private QMUITipDialog loadingDialog;
    private LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        this_ = this;
        initView();
        setBaseContentView(layoutId());
        translucentStatusBar(this, true);
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
        title = findViewById(R.id.text_title);
    }

    private void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    protected void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        // 这段代码可以把view顶上去
//        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            mChildView.setFitsSystemWindows(false);
//            ViewCompat.requestApplyInsets(mChildView);
//        }
    }

    public void changeColor(View view, int colorId) {
        view.setBackgroundColor(ContextCompat.getColor(this_, colorId));
    }

    public void changeTopbarColor(int colorId) {
        changeColor(topBar, colorId);
    }

    public void hideTopBar() {
        topBar.setVisibility(View.GONE);
    }

    public void showTopBar() {
        topBar.setVisibility(View.VISIBLE);
    }

    public void changeTitle(String str) {
        title.setText(str);
    }

    public void changeTitle(int resId) {
        title.setText(resId);
    }

    public QMUITipDialog createLoading() {
        return createLoading("loading...");
    }

    public QMUITipDialog createLoading(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new QMUITipDialog.Builder(this).setTipWord(msg).create();
        }
        return loadingDialog;
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
     * child setLayout
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
     * get data from edittext
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
