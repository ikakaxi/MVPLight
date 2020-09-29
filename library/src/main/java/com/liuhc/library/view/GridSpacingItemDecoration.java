package com.liuhc.library.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * GridLayoutManager（网格布局）设置上下item的间隔
 * <p>
 * 作者： 周旭 on 2017年7月20日 0020.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

	private int spanCount; //列数
	private int spacing; //间隔
	private boolean includeEdge; //是否包含边缘
	private boolean includeItemTB;//是否包含item的上下间距

	public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
		this(spanCount, spacing, includeEdge, true);
	}

	public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, boolean includeItemTB) {
		this.spanCount = spanCount;
		this.spacing = spacing;
		this.includeEdge = includeEdge;
		this.includeItemTB = includeItemTB;
	}

	@Override
	public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {

		//这里是关键，需要根据你有几列来判断
		int position = parent.getChildAdapterPosition(view); // item position
		int column = position % spanCount; // item column

		if (includeEdge) {
			outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
			outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

			if (position < spanCount) { // top edge
				outRect.top = spacing;
			}
			outRect.bottom = spacing; // item bottom
		} else {
			outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
			outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
			if (includeItemTB && position >= spanCount) {
				outRect.top = spacing; // item top
			}
		}
	}
}
