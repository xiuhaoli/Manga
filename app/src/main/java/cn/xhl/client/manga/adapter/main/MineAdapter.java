package cn.xhl.client.manga.adapter.main;

import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.ResourceUtil;

/**
 * @author lixiuhao on 2017/10/26 0026.
 */
public class MineAdapter extends BaseQuickAdapter<MineAdapter.MineItem, BaseViewHolder> {

    public MineAdapter(@Nullable List<MineItem> data) {
        super(R.layout.item_mine, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineItem item) {
        changeItemColor(helper);
        helper.setImageResource(R.id.icon_item_mine, item.getIcon());// icon
        helper.setText(R.id.text_item_mine, item.getText());// title
        helper.setVisible(R.id.arrow_right_item_mine, !item.isHaveSwitcher());// arrow
        helper.setVisible(R.id.switch_item_mine, item.isHaveSwitcher());// switcher
        helper.setChecked(R.id.switch_item_mine, item.isChecked());// switcher is checked
        helper.setVisible(R.id.content_item_mine, item.isHaveContent());// any describe content?
        helper.setText(R.id.content_item_mine, item.getContent());// describe text
    }

    /**
     * 在刷新item的时候会出现丢失theme的颜色值，这里保证一次
     *
     * @param helper
     */
    private void changeItemColor(BaseViewHolder helper) {
        TypedValue typedValue = new TypedValue();
        helper.itemView.setBackgroundColor(
                ResourceUtil.getAttrData(R.attr.item_background, typedValue));
        helper.setTextColor(R.id.text_item_mine,
                ResourceUtil.getAttrData(R.attr.item_text, typedValue));
        helper.setTextColor(R.id.content_item_mine,
                ResourceUtil.getAttrData(R.attr.item_text, typedValue));
    }

    public static class MineItem {
        /**
         * 左边的图标
         */
        private int icon;
        /**
         * 左边的文字
         */
        private int text;
        /**
         * 右边是否有文字
         */
        private boolean haveContent;
        /**
         * 右边的文字
         */
        private String content;
        /**
         * 右边是否是切换按钮
         */
        private boolean haveSwitcher;
        /**
         * 切换按钮是否被选中
         */
        private boolean isChecked;

        public boolean isHaveSwitcher() {
            return haveSwitcher;
        }

        public void setHaveSwitcher(boolean haveSwitcher) {
            this.haveSwitcher = haveSwitcher;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public int getText() {
            return text;
        }

        public void setText(int text) {
            this.text = text;
        }

        public boolean isHaveContent() {
            return haveContent;
        }

        public void setHaveContent(boolean haveContent) {
            this.haveContent = haveContent;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
