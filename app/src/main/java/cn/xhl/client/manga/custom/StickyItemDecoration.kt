package cn.xhl.client.manga.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/02/03
 *     version: 1.0
 * </pre>
 */
class StickyItemDecoration(context: Context, private var data: List<StickyEntity>)
    : DividerItemDecoration(context, VERTICAL) {
    var stickyViewHeight = dp2Px(context, 30f)
    var stickyTextSize: Int = dp2Px(context, 15f)
    var defaultHeight: Int = dp2Px(context, 1f)
    // if you have head views change this
    var headViewCount: Int = 0
    var stickyViewColor = Color.WHITE
    var stickyTextColor = Color.BLACK
    var textPaddingLeft: Int = dp2Px(context, 15f)

    private val paint = Paint()
    private var rect = Rect()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    /**
     * 仅仅只会绘制当前可见的item
     * 因此第一个headerView被绘制的时候，
     * item不一定为第0项
     */
    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        if (c == null || parent == null) return
        var preText = ""
        // 注意，此处的parent.childCount只代表当前recyclerView被绘制到界面上的可见item数
        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child) - headViewCount
            if (pos >= data.size || pos < 0) return
            val text = data[pos].getStickyText()
            if (preText == text) {
                continue// 如果发现和前一个item是一样的就不管
                // 不管下一个item是否一样
            }
            preText = text
            // 这个height是根据列表的滑动而计算出的headView应该被绘制的位置
            var height = Math.max(child.top, stickyViewHeight)
            if (pos + 1 < data.size) {
                val nextText = data[pos + 1].getStickyText()
                if (nextText != text && child.bottom < stickyViewHeight) {
                    height = child.bottom
                }
            }
            drawText(text, height, child.right, c)
        }
    }

    private fun drawText(content: String, height: Int, width: Int, canvas: Canvas) {
        paint.color = stickyViewColor
        canvas.drawRect(0.0f, height - stickyViewHeight.toFloat(),
                width.toFloat(), height.toFloat(), paint)
        paint.color = stickyTextColor
        paint.textSize = stickyTextSize.toFloat()
        paint.getTextBounds(content, 0, content.length, rect)
        canvas.drawText(content, textPaddingLeft.toFloat(),
                height - (stickyViewHeight - rect.height()) * 0.5f, paint)
    }

    override fun getItemOffsets(outRect: Rect?, view: View?,
                                parent: RecyclerView?, state: RecyclerView.State?) {
        if (outRect == null || view == null || parent == null) return
        var position: Int = parent.getChildAdapterPosition(view)
        // 需要减去头布局的个数
        position -= headViewCount
        if (position > 0 && position < data.size) {
            // 只要这个position是在有效范围内的，都会被拿进来处理
            if (data[position].getStickyText() != data[position - 1].getStickyText()) {
                outRect.top = this.stickyViewHeight
            } else {
                outRect.top = this.defaultHeight
            }
        } else if (position == 0) {
            outRect.top = this.stickyViewHeight
        }
    }

    private fun dp2Px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    interface StickyEntity {
        fun getStickyText(): String
    }
}
