package cn.xhl.client.manga.adapter.main;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xhl.client.manga.R;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/13
 *     version: 1.0
 * </pre>
 */
public class FilterAdapter extends BaseQuickAdapter<FilterAdapter.FilterItem, BaseViewHolder> {

    public FilterAdapter(List<FilterItem> data) {
        super(R.layout.item_filter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterItem item) {
        helper.setChecked(R.id.checkbox_item_filter, item.isChecked());
        helper.setText(R.id.checkbox_item_filter, item.getText());
    }

    public static class FilterItem {
        private boolean checked;
        private String text;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }
}


