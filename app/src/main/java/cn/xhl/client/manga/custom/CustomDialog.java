package cn.xhl.client.manga.custom;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.DpUtil;

/**
 * 需要定义别的dialog只要添加不同的Builder就行
 * <p>
 * Created by xiuhaoli on 2017/11/20.
 */

public class CustomDialog extends Dialog {

    private CustomDialog(@NonNull Context context) {
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

    public static class DefaultBuilder {
        private Context mContext;
        private CustomDialog mDialog;
        private LayoutInflater mInflater;
        private View rootView;

        private String title;
        private String content;

        private TextView titleView;
        private TextView contentView;
        private Button negativeButton;
        private Button positiveButton;
        private View.OnClickListener positiveListener;
        private View.OnClickListener negativeListener;

        public DefaultBuilder(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public DefaultBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public DefaultBuilder setTitle(int resId) {
            this.title = mContext.getResources().getString(resId);
            return this;
        }

        public DefaultBuilder setContent(int resId) {
            this.content = mContext.getResources().getString(resId);
            return this;
        }

        public DefaultBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public DefaultBuilder setPositiveListener(View.OnClickListener positiveListener) {
            this.positiveListener = positiveListener;
            return this;
        }

        public DefaultBuilder setNegativeListener(View.OnClickListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        public CustomDialog create() {
            mDialog = new CustomDialog(mContext);
            rootView = mInflater.inflate(R.layout.dialog_default, null);

            titleView = rootView.findViewById(R.id.title_dialog_default);
            contentView = rootView.findViewById(R.id.content_dialog_default);
            negativeButton = rootView.findViewById(R.id.negative_dialog_default);
            positiveButton = rootView.findViewById(R.id.positive_dialog_default);

            if (title == null || title.equals("")) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setText(title);
            }
            contentView.setText(content);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (negativeListener != null) {
                        negativeListener.onClick(v);
                    }
                    mDialog.dismiss();
                }
            });
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (positiveListener != null) {
                        positiveListener.onClick(v);
                    }
                    mDialog.dismiss();
                }
            });
            mDialog.addContentView(rootView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mDialog;
        }
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
        private String title;
        private String hint;
        private String content;

        private Button negativeButton;
        private Button positiveButton;
        private OnInputClickListener negativeListener;
        private OnInputClickListener positiveListener;

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

        public EditTextBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public EditTextBuilder setContent(int resId) {
            this.content = mContext.getResources().getString(resId);
            return this;
        }

        public EditTextBuilder setNegativeListener(OnInputClickListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        public EditTextBuilder setPositiveListener(OnInputClickListener positiveListener) {
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
            if (content != null && !content.equals("")) {
                inputView.setText(content);
            }
            defaultListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.cancel();
                }
            };
            negativeButton.setOnClickListener(negativeListener == null ? defaultListener : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeListener.onClick(mDialog, v, inputView.getText().toString());
                }
            });
            positiveButton.setOnClickListener(positiveListener == null ? defaultListener : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveListener.onClick(mDialog, v, inputView.getText().toString());
                }
            });
            mDialog.addContentView(rootView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mDialog;
        }
    }

    public static class SingleSelectViewBuilder {
        private Context mContext;
        private CustomDialog mDialog;
        private LayoutInflater mInflater;

        private View rootView;
        private TextView titleView;

        private RadioGroup radioGroup;
        private SparseIntArray radioButtonIds;
        private int checkedPosition;

        private String[] selectType;
        private String title;

        private Button negativeButton;
        private Button positiveButton;
        private OnSingleSelectClickListener negativeListener;
        private OnSingleSelectClickListener positiveListener;

        public SingleSelectViewBuilder(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public SingleSelectViewBuilder setNegativeListener(OnSingleSelectClickListener onCheckedListener) {
            this.negativeListener = onCheckedListener;
            return this;
        }

        public SingleSelectViewBuilder setPositiveListener(OnSingleSelectClickListener onCheckedListener) {
            this.positiveListener = onCheckedListener;
            return this;
        }

        public SingleSelectViewBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SingleSelectViewBuilder setSelectType(String... selectType) {
            this.selectType = selectType;
            return this;
        }

        public SingleSelectViewBuilder setTitle(int resId) {
            this.title = mContext.getResources().getString(resId);
            return this;
        }

        public CustomDialog create() {
            mDialog = new CustomDialog(mContext);
            rootView = mInflater.inflate(R.layout.dialog_single_select, null);

            titleView = rootView.findViewById(R.id.title_dialog_single_select);
            radioGroup = rootView.findViewById(R.id.radio_group_dialog_single_select);
            negativeButton = rootView.findViewById(R.id.negative_dialog_single_select);
            positiveButton = rootView.findViewById(R.id.positive_dialog_single_select);

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (negativeListener != null) {
                        negativeListener.onClick(v, checkedPosition);
                    }
                    mDialog.dismiss();
                }
            });
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (positiveListener != null) {
                        positiveListener.onClick(v, checkedPosition);
                    }
                    mDialog.dismiss();
                }
            });

            titleView.setText(title);
            createRadioButton();

            mDialog.addContentView(rootView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mDialog;
        }

        private void createRadioButton() {
            if (selectType == null) {
                radioGroup.setVisibility(View.GONE);
                return;
            }
            int num = selectType.length;
            radioButtonIds = new SparseIntArray(num);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < num; i++) {
                int id = View.generateViewId();
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(id);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setButtonDrawable(null);
                radioButton.setText(selectType[i]);
                radioButton.setPadding(DpUtil.dp2Px(mContext, 15), 0, 0, 0);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ActivityCompat.getDrawable(mContext, R.drawable.icon_check), null);
                radioButton.setTextColor(getTextColor());
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
                    checkedPosition = radioButtonIds.indexOfValue(checkedId);
                }
            });
        }

        private int getTextColor() {
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(
                    R.attr.item_text, typedValue, true);
            return typedValue.data;
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

        public SearchViewBuilder setSearchType(String... searchType) {
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
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ActivityCompat.getDrawable(mContext, R.drawable.icon_check), null);
                radioButton.setTextColor(getTextColor());
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

        private int getTextColor() {
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(
                    R.attr.item_text, typedValue, true);
            return typedValue.data;
        }
    }

    public interface OnCheckedListener {
        void checked(int position, String checked);
    }

    public interface OnInputClickListener {
        void onClick(CustomDialog dialog, View view, String inputText);
    }

    public interface OnSingleSelectClickListener {
        void onClick(View view, int checkedPosition);
    }
}
