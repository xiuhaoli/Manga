package cn.xhl.client.manga.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.xhl.client.manga.MyActivityManager;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.custom.QMUITipDialog;
import cn.xhl.client.manga.custom.SlipBackLayout;
import cn.xhl.client.manga.utils.DpUtil;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.QMUIStatusBarHelper;
import cn.xhl.client.manga.utils.ResourceUtil;

public abstract class BaseActivity extends AppCompatActivity {
    public Activity this_;
    private View topBar;
    private TextView title;
    private QMUITipDialog loadingDialog;
    private LinearLayout llContent;
    private ImageButton leftButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        this_ = this;
        setContentView(R.layout.activity_base);
        initView();
        setBaseContentView(layoutId());
        MyActivityManager.add(this_);
    }

    private void initTheme() {
        if (UserInfo.getInstance().isNightMode()) {
            setTheme(R.style.Theme_Transparent_Night);
            QMUIStatusBarHelper.translucent(this,
                    ActivityCompat.getColor(this, R.color.night_main_background));
        } else {
            setTheme(R.style.Theme_Transparent_Light);
            QMUIStatusBarHelper.translucent(this,
                    ActivityCompat.getColor(this, R.color.day_main_background));
        }
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
        leftButton = findViewById(R.id.button_back);
    }

    public void setSlipClose() {
        new SlipBackLayout.Builder(this)
                .setListener(new SlipBackLayout.OnWindowCloseListener() {
                    @Override
                    public void onFinish() {
                        this_.finish();
                    }
                })
                .build();
    }

    public void changeTheme(boolean isNightMode) {
        UserInfo.getInstance().setNightMode(isNightMode);
        if (isNightMode) {
            setTheme(R.style.AppTheme_Night);
            QMUIStatusBarHelper.translucent(this,
                    ActivityCompat.getColor(this, R.color.night_main_background));
        } else {
            setTheme(R.style.AppTheme);
            QMUIStatusBarHelper.translucent(this,
                    ActivityCompat.getColor(this, R.color.day_main_background));
        }
        changeMode(llContent);
    }

    private void changeMode(View view) {
        TypedValue typedValue = new TypedValue();
        if (view instanceof ViewGroup) {
            if (view instanceof BottomNavigationView) {
                int resId = UserInfo.getInstance().isNightMode() ?
                        R.color.night_main_background : R.color.day_main_background;
                ((BottomNavigationView) view).setItemBackgroundResource(resId);
                return;
            }
            if (view instanceof TabLayout) {
                ((TabLayout) view).setTabTextColors(
                        ResourceUtil.getAttrData(R.attr.tab_text_color, typedValue),
                        ActivityCompat.getColor(this, R.color.colorAccent)
                );
                return;
            }
            if (view instanceof CollapsingToolbarLayout) {
                ((CollapsingToolbarLayout) view).setContentScrimColor(
                        ResourceUtil.getAttrData(R.attr.main_background, typedValue)
                );
            }
            if (getString(R.string.tag_item_bg).equals(view.getTag())) {
                view.setBackgroundColor(
                        ResourceUtil.getAttrData(R.attr.item_background, typedValue)
                );
            } else if (getString(R.string.tag_constant_bg).equals(view.getTag())) {
                // do nothing
            } else if (getString(R.string.tag_recycler).equals(view.getTag())) {
                RecyclerView recyclerView = (RecyclerView) view;
                LogUtil.e("BaseActivity:RecyclerView", "itemCount = " +
                        recyclerView.getAdapter().getItemCount());
                try {
                    Field declaredField = RecyclerView.class.getDeclaredField("mRecycler");
                    declaredField.setAccessible(true);
                    Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName())
                            .getDeclaredMethod("clear");
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(declaredField.get(recyclerView));
                    recyclerView.getRecycledViewPool().clear();
                } catch (IllegalAccessException | InvocationTargetException |
                        NoSuchMethodException | NoSuchFieldException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (getString(R.string.tag_item_round_bg).equals(view.getTag())) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(DpUtil.dp2Px(this, 5));
                drawable.setStroke(1,
                        ResourceUtil.getAttrData(R.attr.item_background, typedValue));
                drawable.setColor(ResourceUtil.getAttrData(R.attr.item_background, typedValue));
                view.setBackground(drawable);
            } else {
                view.setBackgroundColor(
                        ResourceUtil.getAttrData(R.attr.main_background, typedValue)
                );
            }
            for (int i = 0, len = ((ViewGroup) view).getChildCount(); i < len; i++) {
                changeMode(((ViewGroup) view).getChildAt(i));
            }
        } else if (view instanceof TextView) {
            if (!getString(R.string.tag_constant_bg).equals(view.getTag())) {
                ((TextView) view).setTextColor(
                        ResourceUtil.getAttrData(R.attr.item_text, typedValue)
                );
            }
        }
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

    public void addLeftBackListener() {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void addLeftListener(View.OnClickListener listener) {
        leftButton.setOnClickListener(listener);
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
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
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
            InputMethodManager methodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
            if (methodManager != null) {
                methodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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
