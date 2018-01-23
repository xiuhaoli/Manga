package cn.xhl.client.manga.adapter.gallery;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.model.bean.response.gallery.Res_CommentList;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/21
 *     version: 1.0
 * </pre>
 */
public class CommentAdapter extends BaseQuickAdapter<Res_CommentList.CommentEntity,
        CommentAdapter.CommentViewHolder> {

    public CommentAdapter(@Nullable List<Res_CommentList.CommentEntity> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(CommentViewHolder helper, Res_CommentList.CommentEntity item) {
        helper.setImage(R.id.profile_header_item_comment, item.getProfile_picture());
        helper.addOnClickListener(R.id.profile_header_item_comment);// 为头像添加点击事件
        helper.setText(R.id.username_item_comment, item.getUsername());
        helper.setText(R.id.content_item_comment, item.getContent());
        helper.setText(R.id.floor_item_comment, "#" + item.getFloor());
        helper.setText(R.id.time_item_comment, stampFormat(item.getCreate_time()));
        if (UserInfo.getInstance().getUid() == item.getFrom_uid()) {
            // 若两id相等，则说明是自己发的评论，显示删除按钮
            helper.setGone(R.id.delete_item_comment, true);
            helper.addOnClickListener(R.id.delete_item_comment);
        } else {
            helper.setGone(R.id.delete_item_comment, false);
        }
    }


    private String stampFormat(long s) {
        Calendar calendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        calendar.setTimeInMillis(s * 1000);
        if (today.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
            int deltaYear = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
            return deltaYear == 1 ? "a year ago" : deltaYear + " years ago";
        }
        if (today.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            int deltaMonth = today.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
            return deltaMonth == 1 ? "a month ago" : deltaMonth + " months ago";
        }
        if (today.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)) {
            int deltaDay = today.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
            return deltaDay == 1 ? "one day ago" : deltaDay + " days ago";
        }
        if (today.get(Calendar.HOUR_OF_DAY) != calendar.get(Calendar.HOUR_OF_DAY)) {
            int deltaHour = today.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
            return deltaHour == 1 ? "an hour ago" : deltaHour + " hours ago";
        }
        return "just now";
    }

    class CommentViewHolder extends BaseViewHolder {

        private CommentViewHolder(View view) {
            super(view);
        }

        private void setImage(int resId, String profile_picture) {
            SimpleDraweeView simpleDraweeView = getView(resId);
            if (!"0".equals(profile_picture)) {
                simpleDraweeView.setImageURI(profile_picture);
            } else {
                simpleDraweeView.setActualImageResource(R.mipmap.profile_header);
            }
        }
    }
}
