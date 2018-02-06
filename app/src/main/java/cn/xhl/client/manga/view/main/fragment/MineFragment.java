package cn.xhl.client.manga.view.main.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.main.MineAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.main.MineContract;
import cn.xhl.client.manga.custom.CustomDialog;
import cn.xhl.client.manga.custom.MineItemDecoration;
import cn.xhl.client.manga.custom.TextImageSpan;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.PrefUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.view.gallery.ConcreteCategoryActivity;
import cn.xhl.client.manga.view.gallery.FavoriteActivity;
import cn.xhl.client.manga.view.main.FilterActivity;
import cn.xhl.client.manga.view.main.SettingActivity;

public class MineFragment extends BaseFragment implements
        MineContract.View, BaseQuickAdapter.OnItemClickListener,
        View.OnClickListener {
    private MineContract.Presenter presenter;
    private MineAdapter mineAdapter;
    private List<MineAdapter.MineItem> mRecyclerData;
    private TextImageSpan username;
    private SimpleDraweeView header;
    private boolean mIsVisible = true;

    @Override
    protected int layoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_mine);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        initAdapter();
        recyclerView.setAdapter(mineAdapter);
        recyclerView.addItemDecoration(new MineItemDecoration(mActivity, mRecyclerData.size()));
        TextView textView = view.findViewById(R.id.email_fragment_mine);
        textView.setText(UserInfo.getInstance().getEmail());
        header = view.findViewById(R.id.header_fragment_mine);
        header.setImageURI(UserInfo.getInstance().getProfile_picture());
        header.setOnClickListener(this);
        username = view.findViewById(R.id.username_fragment_mine);
        username.setText(UserInfo.getInstance().getUsername());
        username.setOnClickListener(this);
        ControlUtil.initControlOnClick(R.id.setting_fragment_mine, view, this);
        initToolbar(view);
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    private void initToolbar(View view) {
        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout_fragment_mine);
        final SimpleDraweeView finalHeader = view.findViewById(R.id.final_header_fragment_mine);
        finalHeader.setImageURI(UserInfo.getInstance().getProfile_picture());
        final TextView finalName = view.findViewById(R.id.final_username_fragment_mine);
        finalName.setText(UserInfo.getInstance().getUsername());
        startAlphaAnimation(finalHeader, finalName, 0, View.INVISIBLE);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollLength = appBarLayout.getTotalScrollRange();
                if (Math.abs(verticalOffset) / totalScrollLength > 0.8) {
                    showFinal(finalHeader, finalName);
                } else {
                    hideFinal(finalHeader, finalName);
                }
            }
        });
    }

    private void showFinal(View finalHeader, View finalName) {
        if (!mIsVisible) {
            startAlphaAnimation(finalHeader, finalName, 300, View.VISIBLE);
        }
    }

    private void hideFinal(View finalHeader, View finalName) {
        if (mIsVisible) {
            startAlphaAnimation(finalHeader, finalName, 300, View.INVISIBLE);
        }
    }

    private void startAlphaAnimation(final View finalHeader, final View finalName, int duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsVisible = !mIsVisible;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        finalHeader.startAnimation(alphaAnimation);
        finalName.startAnimation(alphaAnimation);
    }

    @Override
    public void initAdapter() {
        mRecyclerData = new ArrayList<>();
        MineAdapter.MineItem item;
        int[] text = {R.string.night_mode, R.string.favorites, R.string.history, R.string.filter};
        int[] img = {R.mipmap.night_mode, R.mipmap.favorite,
                R.mipmap.history, R.mipmap.filter};
        for (int i = 0, size = text.length; i < size; i++) {
            item = new MineAdapter.MineItem();
            item.setIcon(img[i]);
            item.setText(text[i]);
            item.setHaveContent(false);
            item.setHaveSwitcher(false);
            if (i == 0) {
                item.setHaveSwitcher(true);
                item.setChecked(UserInfo.getInstance().isNightMode());
            }
            mRecyclerData.add(item);
        }
        mineAdapter = new MineAdapter(mRecyclerData);
        mineAdapter.setOnItemClickListener(this);
    }

    @Override
    public void showLoading() {
        mActivity.showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        mActivity.hideLoadingDialog();
    }

    @Override
    public void showTipMsg(String msg) {
        mActivity.showToast(msg);
    }

    @Override
    public void changeThemeMode(boolean isNightMode) {
        showChangeThemeAnimation();
        UserInfo.getInstance().setNightMode(isNightMode);
        mActivity.changeTheme(isNightMode);
        PrefUtil.putBoolean(IConstants.NIGHT_MODE, isNightMode, mActivity);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 1:
                // jump to favorite
                FavoriteActivity.start(mActivity, IConstants.FAVORITE);
                break;
            case 2:
                // jump to history
                ConcreteCategoryActivity.Companion.start(mActivity,
                        UserInfo.getInstance().getCategoryMode(), IConstants.HISTORY);
                break;
            case 0:
                mRecyclerData.get(position).setChecked(!UserInfo.getInstance().isNightMode());
                changeThemeMode(!UserInfo.getInstance().isNightMode());
                mineAdapter.notifyItemChanged(position);
                break;
            case 3:
                ActivityUtil.jump2Activity(mActivity, FilterActivity.class);
                break;
            default:
                break;
        }
    }

    private void showChangeThemeAnimation() {
        final View decorView = mActivity.getWindow().getDecorView();
        Bitmap screenShot = screenShot(decorView);
        if (decorView instanceof ViewGroup && screenShot != null) {
            final View view = new View(mActivity);
            view.setBackground(new BitmapDrawable(getResources(), screenShot));
            ((ViewGroup) decorView).addView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
            animator.setDuration(300);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) decorView).removeView(view);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    private Bitmap screenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap drawing = view.getDrawingCache();
        Bitmap bitmap = null;
        if (drawing != null) {
            bitmap = Bitmap.createBitmap(drawing);
            view.setDrawingCacheEnabled(false);
        }
        return bitmap;
    }

    @Override
    public void notifyUI2ChangeUsername(String username) {
        this.username.setText(username);
        UserInfo.getInstance().setUsername(username);
        PrefUtil.putString(IConstants.USERNAME, username, mActivity);
    }

    @Override
    public void notifyUI2ChangeHeader(String url) {
        header.setImageURI(url);
        UserInfo.getInstance().setProfile_picture(url);
        PrefUtil.putString(IConstants.PROFILE_HEADER, url, mActivity);
    }

    private void createModifyUsernameDialog() {
        new CustomDialog.EditTextBuilder(mActivity)
                .setTitle(R.string.rename)
                .setHint(R.string.rename_hint)
                .setPositiveListener(new CustomDialog.OnInputClickListener() {
                    @Override
                    public void onClick(CustomDialog dialog, View view, String inputText) {
                        inputText = inputText.trim();
                        if (StringUtil.isEmpty(inputText)) {
                            showTipMsg("input a word at least");
                            return;
                        }
                        if (inputText.length() > 16) {
                            showTipMsg("length limit 16");
                            return;
                        }
                        presenter.modifyUsername(inputText);
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void createModifyHeaderDialog() {
        new CustomDialog.EditTextBuilder(mActivity)
                .setTitle(R.string.profile_header)
                .setHint(R.string.profile_header_hint)
                .setPositiveListener(new CustomDialog.OnInputClickListener() {
                    @Override
                    public void onClick(CustomDialog dialog, View view, String inputText) {
                        if (StringUtil.isEmpty(inputText)) {
                            showTipMsg("please input a url");
                            return;
                        }
                        if (inputText.length() > 128) {
                            showTipMsg("length limit 128");
                            return;
                        }
                        if (!inputText.endsWith(".jpg") && !inputText.endsWith(".png") &&
                                !inputText.endsWith(".webp") && !inputText.endsWith("bmp") &&
                                !inputText.endsWith("jpeg")) {
                            showTipMsg("invalid url");
                            return;
                        }
                        presenter.modifyProfileHeader(inputText);
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.username_fragment_mine:
                createModifyUsernameDialog();
                break;
            case R.id.header_fragment_mine:
                createModifyHeaderDialog();
                break;
            case R.id.setting_fragment_mine:
                ActivityUtil.jump2Activity(mActivity, SettingActivity.class);
                break;
            default:
                break;
        }
    }
}
