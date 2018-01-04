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


    public SettingAdapter(@Nullable List<SettingItem> data) {
        super(R.layout.item_setting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingItem item) {
        if (item.isLogout()) {
            helper.setVisible(R.id.logout_item_setting, true);
            helper.setGone(R.id.text_item_setting, false);// title
            helper.setVisible(R.id.arrow_right_item_setting, false);// arrow
            helper.setVisible(R.id.switch_item_setting, false);// switcher
            helper.setVisible(R.id.content_item_setting, false);
            return;
        }
        helper.setGone(R.id.logout_item_setting, false);
        helper.setGone(R.id.text_item_setting, true);// title
        helper.setText(R.id.text_item_setting, item.getText());// title
        helper.setVisible(R.id.arrow_right_item_setting, !item.isHaveSwitcher());// arrow
        helper.setVisible(R.id.switch_item_setting, item.isHaveSwitcher());// switcher
        helper.setChecked(R.id.switch_item_setting, item.isChecked());// switcher is checked
        helper.setVisible(R.id.content_item_setting, item.isHaveContent());// any describe content?
        helper.setText(R.id.content_item_setting, item.getContent());// describe text
    }

    public static class SettingItem {
        private boolean isLogout;
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

        public boolean isLogout() {
            return isLogout;
        }

        public void setLogout(boolean logout) {
            isLogout = logout;
        }
    }

}
