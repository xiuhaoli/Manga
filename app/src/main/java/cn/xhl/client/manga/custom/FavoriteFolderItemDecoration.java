package cn.xhl.client.manga.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.DpUtil;

/**
 * Created by xiuhaoli on 2017/11/18.
 */

public class FavoriteFolderItemDecoration extends DividerItemDecoration {
    private Context mContext;

    public FavoriteFolderItemDecoration(Context context) {
        super(context, LinearLayout.VERTICAL);
        this.mContext = context;
        // the default drawable will lead to draw a white line, so I should change the color as same as background
        Drawable drawable = mContext.getDrawable(R.drawable.mine_divider);
        if (drawable != null) {
            setDrawable(drawable);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.top = DpUtil.dp2Px(mContext, 20);
            outRect.bottom = DpUtil.dp2Px(mContext, 1);
        } else {
            outRect.bottom = DpUtil.dp2Px(mContext, 1);
        }
    }


}
