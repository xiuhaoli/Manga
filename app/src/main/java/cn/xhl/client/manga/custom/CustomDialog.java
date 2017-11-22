package cn.xhl.client.manga.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.DpUtil;

/**
 * 需要定义别的dialog只要添加不同的Builder就行
 * <p>
 * Created by xiuhaoli on 2017/11/20.
 */

public class CustomDialog extends Dialog {

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.customDialogStyle);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();

    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setDimAmount(0.6f); // 部分刷机会导致背景透明，这里保证一次
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(wmlp);
    }

    public static class TextViewBuilder {

    }

    /**
     * 构建有编辑框的dialog
     */
    public static class EditTextBuilder {
        private Context mContext;
        private CustomDialog mDialog;
        private LayoutInflater mInflater;

        private View rootView;
        private TextView titleView;
        private EditText inputView;
        private Button negativeButton;
        private Button positiveButton;

        private String title;
        private String hint;
        private OnClickListener negativeListener;
        private OnClickListener positiveListener;

        private View.OnClickListener defaultListener;


        public EditTextBuilder(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public EditTextBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EditTextBuilder setTitle(int resId) {
            this.title = mContext.getResources().getString(resId);
            return this;
        }

        public EditTextBuilder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public EditTextBuilder setHint(int resId) {
            this.hint = mContext.getResources().getString(resId);
            return this;
        }

        public EditTextBuilder setNegativeListener(OnClickListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        public EditTextBuilder setPositiveListener(OnClickListener positiveListener) {
            this.positiveListener = positiveListener;
            return this;
        }

        public CustomDialog create() {
            mDialog = new CustomDialog(mContext);
            rootView = mInflater.inflate(R.layout.dialog_edittext, null);

            titleView = rootView.findViewById(R.id.title_dialog_edittext);
            inputView = rootView.findViewById(R.id.input_dialog_edittext);
            negativeButton = rootView.findViewById(R.id.negative_dialog_edittext);
            positiveButton = rootView.findViewById(R.id.positive_dialog_edittext);

            titleView.setText(title);
            inputView.setHint(hint);
            defaultListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.cancel();
                }
            };
            negativeButton.setOnClickListener(negativeListener == null ? defaultListener : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeListener.onClick(v, inputView.getText().toString());
                }
            });
            positiveButton.setOnClickListener(positiveListener == null ? defaultListener : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveListener.onClick(v, inputView.getText().toString());
                }
            });
            mDialog.addContentView(rootView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mDialog;
        }
    }

    public static class SearchViewBuilder {
        private Context mContext;
        private CustomDialog mDialog;
        private LayoutInflater mInflater;

        private View rootView;
        private TextView titleView;
        private SearchView searchView;
        private SearchView.OnQueryTextListener searchListener;
        private OnCheckedListener onCheckedListener;

        private RadioGroup radioGroup;
        private SparseIntArray radioButtonIds;

        private String[] searchType;
        private String title;
        private String hint;

        public SearchViewBuilder(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public SearchViewBuilder setSearchListener(SearchView.OnQueryTextListener searchListener) {
            this.searchListener = searchListener;
            return this;
        }

        public SearchViewBuilder setOnCheckedListener(OnCheckedListener onCheckedListener) {
            this.onCheckedListener = onCheckedListener;
            return this;
        }

        public SearchViewBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SearchViewBuilder setSearchType(String[] searchType) {
            this.searchType = searchType;
            return this;
        }

        public SearchViewBuilder setTitle(int resId) {
            this.title = mContext.getResources().getString(resId);
            return this;
        }

        public SearchViewBuilder setSearchViewHint(String hint) {
            this.hint = hint;
            return this;
        }

        public SearchViewBuilder setSearchViewHint(int resId) {
            this.hint = mContext.getResources().getString(resId);
            return this;
        }

        public CustomDialog create() {
            mDialog = new CustomDialog(mContext);
            rootView = mInflater.inflate(R.layout.dialog_search, null);

            titleView = rootView.findViewById(R.id.title_dialog_search);
            searchView = rootView.findViewById(R.id.input_dialog_search);
            radioGroup = rootView.findViewById(R.id.radio_group_dialog_search);

            titleView.setText(title);
            searchView.setQueryHint(hint);
            searchView.setOnQueryTextListener(searchListener);
            createRadioButton();

            mDialog.addContentView(rootView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mDialog;
        }

        private void createRadioButton() {
            if (searchType == null) {
                radioGroup.setVisibility(View.GONE);
                return;
            }
            int num = searchType.length;
            radioButtonIds = new SparseIntArray(num);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < num; i++) {
                int id = View.generateViewId();
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(id);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setButtonDrawable(null);
                radioButton.setText(searchType[i]);
                radioButton.setPadding(DpUtil.dp2Px(mContext, 15), 0, 0, 0);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, ActivityCompat.getDrawable(mContext, R.drawable.icon_check), null);
                radioButton.setTextColor(ActivityCompat.getColor(mContext, R.color.item_text));
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                radioButtonIds.put(i, id);
                if (i == 0) {
                    radioButton.setChecked(true);
                }
                radioGroup.addView(radioButton);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int position = radioButtonIds.indexOfValue(checkedId);
                    searchView.setQueryHint(searchType[position]);
                    // 把确认的内容回调到前台去
                    if (onCheckedListener != null) {
                        onCheckedListener.checked(position, searchType[position]);
                    }
                }
            });
        }
    }

    public interface OnCheckedListener {
        void checked(int position, String checked);
    }

    public interface OnClickListener {
        void onClick(View view, String inputText);
    }
}
