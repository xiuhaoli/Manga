package cn.xhl.client.manga.adapter.main;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xhl.client.manga.R;

/**
 * @author lixiuhao on 2017/10/26 0026.
 */

public class MineAdapter extends BaseQuickAdapter<MineAdapter.MineItem, BaseViewHolder> {

    public MineAdapter(@Nullable List<MineItem> data) {
        super(R.layout.item_mine, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineItem item) {
        helper.setImageResource(R.id.icon_item_mine, item.getIcon());
        helper.setText(R.id.text_item_mine, item.getText());
        if (item.isSwitcher()) {
            // this code fix a crash, maybe you can not set the listener twice
            helper.setOnCheckedChangeListener(R.id.switch_item_mine, null);
            helper.setChecked(R.id.switch_item_mine, item.isChecked());
            helper.setVisible(R.id.switch_item_mine, true);
            helper.setOnCheckedChangeListener(R.id.switch_item_mine, item.getListener());
        } else {
            helper.setVisible(R.id.arrow_right_item_mine, true);
        }
    }

    public static class MineItem {
        private int text;
        private boolean switcher;
        private int icon;
        private boolean checked;
        private CompoundButton.OnCheckedChangeListener listener;


        public MineItem() {
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public CompoundButton.OnCheckedChangeListener getListener() {
            return listener;
        }

        public void setListener(CompoundButton.OnCheckedChangeListener listener) {
            this.listener = listener;
        }

        public int getText() {
            return text;
        }

        public void setText(int text) {
            this.text = text;
        }

        public boolean isSwitcher() {
            return switcher;
        }

        public void setSwitcher(boolean switcher) {
            this.switcher = switcher;
        }

    }
}
