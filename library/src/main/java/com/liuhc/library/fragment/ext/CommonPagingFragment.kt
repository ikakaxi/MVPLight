package com.liuhc.library.fragment.ext

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kennyc.view.MultiStateView
import com.liuhc.library.R
import com.liuhc.library.fragment.base.BaseMVPFragment
import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.utils.DimenUtils
import com.liuhc.library.view.ExtSmartRefreshLayout
import com.liuhc.library.view.GridSpacingItemDecoration

/**
 * 该类的子类有一个可下拉刷新上拉加载的列表
 * @author liuhc
 * @date 2019/7/25
 */
abstract class CommonPagingFragment<T : BasePresenter, D, A : BaseQuickAdapter<D, BaseViewHolder>> : BaseMVPFragment<T>() {

    private lateinit var mRefreshLayout: ExtSmartRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMultiStateView: MultiStateView
    private var autoLoadOnResume = false
    protected val mIsFirstPage: Boolean
        get() = mCurrentPage == 1

    protected lateinit var mAdapter: A
        private set

    protected var mCurrentPage: Int = 1
        private set

    //每次请求多少条数据
    protected var mRequestPageSize = 20
        private set

    /**
     * 调用此方法后在页面重新显示的时候自动载入数据
     */
    fun autoLoadOnResume() {
        autoLoadOnResume = true
    }

    fun setRequestPageSize(requestPageSize: Int) {
        mRequestPageSize = requestPageSize
    }

    @CallSuper
    override fun initViews(view: View, savedInstanceState: Bundle?) {
        mRefreshLayout = view.findViewById(R.id.mRefreshLayout)
        mMultiStateView = view.findViewById(R.id.mMultiStateView)
        mRecyclerView = view.findViewById(R.id.mRecyclerView)
        val rootView = view.findViewById<View>(R.id.mCommonLayout)
        rootView.setBackgroundResource(doGetRootViewBg())
        mRecyclerView.setBackgroundResource(doGetRecyclerViewBg())
        doGetRecyclerMargin()?.let {
            //mRecyclerView添加margin
            val marginParams = mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams
            marginParams.setMargins(
                DimenUtils.dp2px(requireContext(), it[0]),
                DimenUtils.dp2px(requireContext(), it[1]),
                DimenUtils.dp2px(requireContext(), it[2]),
                DimenUtils.dp2px(requireContext(), it[3])
            )
        }
        doGetRecyclerPadding()?.let {
            //mRecyclerView添加padding
            mRecyclerView.setPadding(
                DimenUtils.dp2px(requireContext(), it[0]),
                DimenUtils.dp2px(requireContext(), it[1]),
                DimenUtils.dp2px(requireContext(), it[2]),
                DimenUtils.dp2px(requireContext(), it[3])
            )
        }
        if (doGetGridSpacing() != null) {
            //mRecyclerView添加gridItem间隔
            doGetGridSpacing()?.let {
                val spanCount = it[0] as Int
                val spacing = it[1] as Int
                val includeEdge = it[2] as Boolean
                mRecyclerView.addItemDecoration(
                    GridSpacingItemDecoration(
                        spanCount,
                        DimenUtils.dp2px(requireContext(), spacing.toFloat()),
                        includeEdge
                    )
                )
                mRecyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
            }
        } else {
            //mRecyclerView添加item间隔
            doGetItemOffsets()?.let {
                mRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.set(
                            DimenUtils.dp2px(requireContext(), it[0]),
                            DimenUtils.dp2px(requireContext(), it[1]),
                            DimenUtils.dp2px(requireContext(), it[2]),
                            DimenUtils.dp2px(requireContext(), it[3])
                        )
                    }
                })
            }
        }
        if (!doIsMatchParentHeight()) {
            val marginParams = mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams
            marginParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            mRecyclerView.layoutParams = marginParams
        }
        mAdapter = onGetAdapter()
        mRecyclerView.adapter = mAdapter
        initRefresh()
    }

    override fun onResume() {
        //如果在页面重新显示的时候自动载入数据,那么需要重置当前页面为第一页,否则如果加载过第二页,会只加载第二页
        if (autoLoadOnResume) {
            mCurrentPage = 1
        }
        super.onResume()
    }

    /*
     * 错误信息提示，默认实现
     */
    override fun onError(text: String) {
        super.onError(text)
        setList(listOf())
    }

    fun addRecyclerViewScrollListener(listener: RecyclerView.OnScrollListener) {
        mRecyclerView.addOnScrollListener(listener)
    }

    private fun setList(list: List<D>, hasNextPage: () -> Boolean) {
        if (list.isNotEmpty()) {//获取到的数据不为空
            //重新设置为可以上拉加载更多
            mRefreshLayout.resetNoMoreData()
            if (mIsFirstPage) {//下拉刷新
                mRefreshLayout.finishRefresh()
                mAdapter.setList(list)
            } else {//上拉加载
                mAdapter.addData(list)
            }
            if (hasNextPage()) {
                mCurrentPage++
                //完成加载并标记还可以获取更多数据
                mRefreshLayout.finishLoadMore()
            } else {
                //完成加载并标记没有更多数据
                mRefreshLayout.finishLoadMoreWithNoMoreData()
            }
            mMultiStateView.viewState = MultiStateView.ViewState.CONTENT
        } else {//获取到的数据为空
            if (mIsFirstPage) {//下拉刷新
                mRefreshLayout.finishRefresh()
                mAdapter.setList(arrayListOf())
            } else {//上拉加载
                //完成加载并标记没有更多数据
                mRefreshLayout.finishLoadMoreWithNoMoreData()
            }
            if (mAdapter.itemCount == 0) {//本次没有获取到数据并且之前也没有数据
                mMultiStateView.viewState = MultiStateView.ViewState.EMPTY
            }
        }
    }

    /**
     * 如果有分页数据,建议调此方法
     * 该方法可以在获取到数据后就可以判断是否有下一页
     */
    fun setPageList(page: Page<D>) {
        setList(page.pageData ?: listOf()) {
            //如果列表里的数据小于分页最大数据才page+1
            mAdapter.itemCount < page.totalData
        }
    }

    /**
     * 如果数据不分页或者服务器返回数据分页但是没有分页数据,就调此方法
     * 该方法在获取到数据后无法判断是否有下一页,只有下一页数据为空才知道数据是否已全部获取到
     */
    fun setList(list: List<D>) {
        setList(list) {
            //如果可以上拉加载才page+1
            mRefreshLayout.enableLoadMore()
        }
    }

    /**
     * 是否启用下拉刷新（默认启用）
     * @param enabled 是否启用
     * @return SmartRefreshLayout
     */
    fun enableRefresh(enabled: Boolean) {
        mRefreshLayout.setEnableRefresh(enabled)
    }

    /**
     * Set whether to enable pull-up loading more (enabled by default).
     * 设置是否启用上拉加载更多（默认启用）
     * @param enabled 是否启用
     * @return RefreshLayout
     */
    fun enableLoadMore(enabled: Boolean) {
        mRefreshLayout.setEnableLoadMore(enabled)
    }

    //重置为获取第一页数据
    //当父页面取代了当前页面的下拉刷新功能后,在父页面下拉刷新后需要手动调用该方法
    fun reloadFirstPage() {
        mCurrentPage = 1
        loadData()
    }

    private fun initRefresh() {
        mRefreshLayout.setOnRefreshListener {
            reloadFirstPage()
        }
        mRefreshLayout.setOnLoadMoreListener {
            loadData()
        }
    }

    abstract fun onGetAdapter(): A

    //mRecyclerView添加margin
    open fun doGetRecyclerMargin(): Array<Float>? {
        return null
    }

    //mRecyclerView添加padding
    open fun doGetRecyclerPadding(): Array<Float>? {
        return null
    }

    //mRecyclerView添加item间隔
    open fun doGetItemOffsets(): Array<Float>? {
        return null
    }

    //mRecyclerView添加gridItem间隔
    open fun doGetGridSpacing(): Array<Any>? {
        return null
    }

    @DrawableRes
    open fun doGetRootViewBg(): Int {
        return android.R.color.transparent
    }

    @DrawableRes
    open fun doGetRecyclerViewBg(): Int {
        return android.R.color.transparent
    }

    open fun doIsMatchParentHeight(): Boolean = true

    data class Page<out T>(
        val page: Int,//当前第几页
        val totalData: Int,//服务器一共多少数据
        val pageData: List<T>?//当前页的数据
    )
}