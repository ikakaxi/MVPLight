package com.liuhc.library.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.liuhc.library.R

/**
 * 描述:扩展TabLayout,增加选中tab字体变大的功能
 * 作者:liuhaichao
 * 创建日期：2020/9/28 on 10:28 AM
 */
class ExtTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TabLayout(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtTabLayout)
        val selectedTextColor = typedArray.getColor(R.styleable.ExtTabLayout_extTabSelectedTextColor, Color.BLACK)
        val selectedTextSize = typedArray.getDimension(R.styleable.ExtTabLayout_extTabSelectedTextSize, -1f)
        typedArray.recycle()
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.common_tab_textview)
                }
                val textView: TextView = tab.customView!!.findViewById(android.R.id.text1)
                if (selectedTextColor != Color.BLACK) {
                    textView.setTextColor(selectedTextColor)
                }
                if (selectedTextSize != -1f) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize)
                }
//                textView.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.xml.text_animator_selector)
            }

            override fun onTabUnselected(tab: Tab) {
                tab.customView = null
            }

            override fun onTabReselected(tab: Tab) {}
        })
    }
}