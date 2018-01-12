package cn.xhl.client.manga.adapter.main;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xhl.client.manga.R;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/04
 *     version: 1.0
 * </pre>
 */
public class SettingAdapter extends BaseQuickAdapter<SettingAdapter.SettingItem, BaseViewHolder> {
    public static final int ITEM_TEXT_TEXT = 0;
    public static final int ITEM_TEXT_ARROW = 1;
    public static final int ITEM_TEXT_TEXT_ARROW = 2;
    public static final int ITEM_TEXT_SWITCHER = 3;
    public static final int ITEM_LOGOUT = 4;

    public SettingAdapter(@Nullable List<SettingItem> data) {
        super(R.layout.item_setting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingItem item) {
        initId(helper, item);
    }

    private void initId(BaseViewHolder helper, SettingItem item) {
        helper.setGone(R.id.logout_item_setting, false);// logout gone
        helper.setGone(R.id.text_item_setting, false);// title visible
        helper.setGone(R.id.arrow_right_item_setting, false);// arrow
        helper.setGone(R.id.switch_item_setting, false);// switcher
        helper.setChecked(R.id.switch_item_setting, false);// switcher is checked
        helper.setGone(R.id.content_item_setting, false);// any describe content?
        switch (item.level) {
            case ITEM_TEXT_TEXT:
                helper.setGone(R.id.text_item_setting, true);// title visible
                helper.setText(R.id.text_item_setting, item.getText());// title content
                helper.setVisible(R.id.content_item_setting, true);// any describe content?
                helper.setText(R.id.content_item_setting, item.getContent());// describe text
                break;
            case ITEM_TEXT_ARROW:// 第二种情况是左边文字右边文字
                helper.setGone(R.id.text_item_setting, true);// title visible
                helper.setText(R.id.text_item_setting, item.getText());// title content
                helper.setVisible(R.id.arrow_right_item_setting, true);// arrow
                break;
            case ITEM_TEXT_TEXT_ARROW:
                helper.setGone(R.id.text_item_setting, true);// title visible
                helper.setText(R.id.text_item_setting, item.getText());// title content
                helper.setVisible(R.id.content_item_setting, true);// any describe content?
                helper.setText(R.id.content_item_setting, item.getContent());// describe text
                helper.setVisible(R.id.arrow_right_item_setting, true);// arrow
                break;
            case ITEM_TEXT_SWITCHER:
                helper.setGone(R.id.text_item_setting, true);// title visible
                helper.setText(R.id.text_item_setting, item.getText());// title content
                helper.setVisible(R.id.switch_item_setting, true);// switcher
                helper.setChecked(R.id.switch_item_setting, item.isChecked());// switcher is checked
                break;
            case ITEM_LOGOUT:
                helper.setGone(R.id.text_item_setting, false);// title visible
                helper.setVisible(R.id.logout_item_setting, true);
                break;
            default:
                break;
        }
    }

    public static class SettingItem {
        /**
         * 用于区分item
         */
        private int level;
        /**
         * 左边的文字
         */
        private int text;
        /**
         * 右边的文字
         */
        private String content;
        /**
         * 切换按钮是否被选中
         */
        private boolean isChecked;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getText() {
            return text;
        }

        public void setText(int text) {
            this.text = text;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
