package cn.xhl.client.manga;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by lixiuhao on 2017/5/9 0009.
 * <p>
 * 基类
 */
public class BaseActivity extends RxAppCompatActivity {
    public Activity this_;
    private QMUITopBar topBar;// 标题栏
    private QMUITipDialog loadingDialog;
    private LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_base);
        this_ = this;

        initView();
    }

    private void initView() {
        topBar = findViewById(R.id.topbar);
        llContent = findViewById(R.id.v_content); //v_content是在基类布局文件中预定义的layout区域
        loadingDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
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

    /**
     * 隐藏标题
     */
    public void hideTopBar() {
        topBar.setVisibility(View.GONE);
    }

    /**
     * 改变标题
     *
     * @param str
     */
    public void changeTitle(String str) {
        topBar.setTitle(str);
    }

    public void changeTitle(int resId) {
        topBar.setTitle(resId);
    }

    /**
     * 从edittext上那数据
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

    /**
     * 显示progressbar
     */
    public void showLoadingDialog() {
        loadingDialog.show();
    }

    /**
     * 隐藏progressbar
     */
    public void hideLoadingDialog() {
        loadingDialog.cancel();
    }

    public void showToast(String str, int time) {
        Toast.makeText(this, str, time).show();
    }

    public void showToast(String str) {
        showToast(str, Toast.LENGTH_SHORT);
    }

    public void closeBaseActivity() {
        this.finish();
    }

    /**
     * set screen view
     *
     * @param layoutResID
     */
    public void setBaseContentView(int layoutResID) {
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

    @Override
    protected void onStop() {
        loadingDialog.cancel();
        super.onStop();
    }
}
