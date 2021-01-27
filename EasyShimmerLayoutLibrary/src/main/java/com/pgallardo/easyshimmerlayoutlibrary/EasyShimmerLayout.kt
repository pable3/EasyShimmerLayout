package com.pgallardo.easyshimmerlayoutlibrary

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.res.getResourceIdOrThrow
import com.facebook.shimmer.ShimmerFrameLayout
import kotlin.math.ceil


class EasyShimmerLayout : ShimmerFrameLayout {

    lateinit var inflater: LayoutInflater

    var num: Int = 4
    var placeholderId: Int = 0
    var type: Int = 0
    var layoutOrientation: Int = 1
    var colNum: Int = 2

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        consumeAttributes(context, attrs)

        if (type == 0) setLinearLayout(context) else setGridLayout(context)
    }

    private fun consumeAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.EasyShimmerLayout, 0, 0).apply {
            try {
                num = getInteger(R.styleable.EasyShimmerLayout_shimmer_num, 4)
                placeholderId = getResourceIdOrThrow(R.styleable.EasyShimmerLayout_shimmer_placeholder)
                type = getInteger(R.styleable.EasyShimmerLayout_shimmer_type, 0)
                layoutOrientation = getInteger(R.styleable.EasyShimmerLayout_shimmer_orientation, 1)
                colNum = getInteger(R.styleable.EasyShimmerLayout_shimmer_colNum, 2)
            } catch (ex: IllegalArgumentException) {
                throw RuntimeException("Attribute shimmer_placeholder is missing.")
            } finally {
                recycle()
            }
        }
    }

    private fun setLinearLayout(context: Context) =
        addView(LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
            )
            clipToPadding = false
            orientation = layoutOrientation
            for (i in 1..num) {
                addView(inflater.inflate(placeholderId, this, false))
            }
        })


    private fun setGridLayout(context: Context) =
        addView(LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
            )
            clipToPadding = false
            orientation = LinearLayout.VERTICAL

            val rowNum: Int = ceil(num/colNum.toDouble()).toInt()

            var k = num
            for (i in 1..rowNum) {
                addView(LinearLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    )
                    weightSum = colNum.toFloat()
                    orientation = LinearLayout.HORIZONTAL

                    for (j in 1..colNum) {
                        addView(inflater.inflate(placeholderId, this, false).apply {
                            (layoutParams as LinearLayout.LayoutParams).weight = 1f
                        })

                        k--
                        if(k <= 0) break
                    }
                })
            }
        })
}