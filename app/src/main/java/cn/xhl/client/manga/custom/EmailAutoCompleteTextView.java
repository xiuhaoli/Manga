package cn.xhl.client.manga.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Mike on 2017/9/23 0023.
 * <p>
 * 自动补全email
 */
public class EmailAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView implements TextWatcher {
    private String[] emailSuffixs = {"@gmail.com"};
    private List<String> suffixList;
    private ArrayAdapter<String> arrayAdapter;

    public EmailAutoCompleteTextView(Context context) {
        super(context);
        init(context);
    }

    public EmailAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmailAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        suffixList = new ArrayList<>();
        suffixList.addAll(Arrays.asList(emailSuffixs));
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, suffixList);
        this.setAdapter(arrayAdapter);
        this.addTextChangedListener(this);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering(text, keyCode);
    }

    /**
     *
     * @param s 改变后的字符串
     */
    @Override
    public void afterTextChanged(Editable s) {
        if (emailSuffixs == null) {
            return;
        }
        if (s.length() < getThreshold()) {
            return;
        }
        String input = s.toString();
        // 如果输入的内容中没有@，说明补全框中的数据已经足够不需要更新
        if (!input.contains("@")) {
            suffixList.clear();
            for (String suffix : emailSuffixs) {
                suffixList.add(input + suffix);
            }
            arrayAdapter.clear();
            arrayAdapter.addAll(suffixList);
            arrayAdapter.notifyDataSetChanged();
        }
//        showDropDown();
    }

    /**
     * @param s     改变前的字符串
     * @param start 被改变的位数(比如在第二个字符后面加字符会返回2)
     * @param count 被删除字符的数量
     * @param after 被添加字符的数量
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        if (length() < getThreshold()) {
//            return;
//        }

        // 选中粘贴会出现count!=0，after!=0的情况，因此要排除
//        if (count != 0 && after == 0) {
//            String input = getText().toString();
//
//        }
    }

    /**
     * @param s      改变后的字符串
     * @param start  被改变的位数
     * @param before 被删除的字符数
     * @param count  被添加的字符数
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // discard
    }
}
