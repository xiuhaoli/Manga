package cn.xhl.client.manga.adapter.main;

import android.support.annotation.Nullable;

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
        helper.setVisible(R.id.arrow_right_item_mine, true);
        if (item.isHaveContent()) {
            helper.setVisible(R.id.content_item_mine, true);
            helper.setText(R.id.content_item_mine, item.getContent());
        }
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


        public MineItem() {
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
