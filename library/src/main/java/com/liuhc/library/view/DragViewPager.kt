package com.liuhc.library.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.liuhc.library.R
import com.liuhc.library.event.DataListener
import com.liuhc.library.event.msg.RequestOriginViewData
import com.liuhc.library.event.msg.ResponseOriginViewData
import com.liuhc.library.utils.NetworkUtils
import com.liuhc.library.utils.ScreenUtils
import com.liuhc.library.utils.ViewHelper
import kotlinx.android.parcel.Parcelize
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/// author:liuhaichao
/// description:仿微信照片和视频拖动可关闭的页面
/// create date：2020-08-28 on 5:01 PM

class DragViewPager : ViewPager {

    private lateinit var mediaFileList: ArrayList<FilesBean>
    private var currentCustomVideoView: CustomVideoView? = null
    private val customVideoViewMap = hashMapOf<Int, CustomVideoView>()
    private val customPhotoViewMap = hashMapOf<Int, CustomPhotoView>()

    //当前显示的View的状态(正常浏览状态 / 滑动状态 / 返回中状态)
    private var currentViewStatus = STATUS_NORMAL

    //ViewPager滑动状态
    private var currentViewPagerStatus = 0
    private var mDownX = 0f
    private var mDownY = 0f
    private var mScreenHeight = 0f

    //要缩放的View
    private var mCurrentView: View? = null
    private var mCurrentViewOriginX: Int = 0
    private var mCurrentViewOriginY: Int = 0

    private var mOriginLeft = 0f
    private var mOriginTop = 0f
    private var mOriginHeight = 0f
    private var mOriginWidth = 0f
    private var mOriginCenterX = 0f
    private var mOriginCenterY = 0f
    private var mAlphaPercent = 1f

    //动画时长
    private val mDuration = 300L

    //滑动速度检测类
    private var mVelocityTracker: VelocityTracker? = null

    //首次显示时,要有一个从上一页的ItemView移动到当前View的动画
    private var mFirstShow = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun initParams(mediaFileList: ArrayList<FilesBean>, curPosition: Int) {
        this.mediaFileList = mediaFileList
        this.mScreenHeight = ScreenUtils.getScreenHeight(context).toFloat()
        //设置背景为透明,防止刚进入的时候黑色背景闪屏
        setBackgroundColor(Color.TRANSPARENT)
        adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return mediaFileList.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                var view: View
                when {
                    mediaFileList[position].isVideo -> {
                        view = getVideoView(position)
                        container.addView(view)
                    }
                    else -> {
                        view = CustomPhotoView(context)
                        view.initParams(ImageView.ScaleType.FIT_CENTER)
                        view = getImageView(position, view)
                        container.addView(view)
                    }
                }
                return view
            }

            private fun getVideoView(position: Int): View {
                val viewGroup: ViewGroup = LayoutInflater.from(context).inflate(R.layout.common_item_custom_video, null) as ViewGroup
                val customVideoView: CustomVideoView = viewGroup.getChildAt(0) as CustomVideoView
                customVideoView.initVideoPlayer(mediaFileList[position].originalImgUrl)
                customVideoViewMap[position] = customVideoView
                (context as LifecycleOwner).lifecycle.addObserver(customVideoView)
                return viewGroup
            }

            private fun getImageView(position: Int, viewCustom: CustomPhotoView): View {
                customPhotoViewMap[position] = viewCustom
                viewCustom.load(mediaFileList[position].originalImgUrl)
                (context as LifecycleOwner).lifecycle.addObserver(viewCustom)
                return viewCustom
            }

            override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
                val view = obj as View
                container.removeView(view)
                customVideoViewMap.remove(position)?.destroy()
                customPhotoViewMap.remove(position)?.destroy()
            }

            override fun setPrimaryItem(container: ViewGroup, position: Int, view: Any) {

            }
        }
        val pageChangeListener = object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                currentCustomVideoView?.pause()
                if (customVideoViewMap[position] != null) {
                    currentCustomVideoView = customVideoViewMap[position]
                    mCurrentView = currentCustomVideoView
                    if (NetworkUtils.isWifiState(context)) {
                        currentCustomVideoView!!.start()
                    }
                } else {
                    mCurrentView = customPhotoViewMap[position]
                }
                //为当前view设置tag
                tag = position
                //每次切换了新的View,就获取当前View对应的上一页的ItemView的位置等信息
                DataListener.publish(RequestOriginViewData(position))
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentViewPagerStatus = state
            }
        }
        //设置当前索引,要在设置完adapter以后,否则不能正确切换到指定索引
        currentItem = curPosition
        tag = curPosition
        addOnPageChangeListener(pageChangeListener)
        //解决mViewPager首次初始化后onPageSelected不回调的问题
        post {
            pageChangeListener.onPageSelected(curPosition)
        }
        DataListener.listen(this, ResponseOriginViewData::class.java) { msg ->
            mOriginLeft = msg.x.toFloat()
            mOriginTop = msg.y.toFloat()
            mOriginWidth = msg.width.toFloat()
            mOriginHeight = msg.height.toFloat()
            mOriginCenterX = mOriginLeft + mOriginWidth / 2
            mOriginCenterY = mOriginTop + mOriginHeight / 2
            if (mFirstShow) {
                mFirstShow = false
                performEnterAnimation()
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.rawX
                mDownY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = abs((ev.rawX - mDownX).toInt())
                val deltaY = (ev.rawY - mDownY).toInt()
                if (deltaY > DRAG_GAP) { //往下移动超过临界，拦截滑动事件
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (currentViewStatus == STATUS_RESETTING) return false
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.rawX
                mDownY = ev.rawY
                addIntoVelocity(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                addIntoVelocity(ev)
                val deltaY = (ev.rawY - mDownY).toInt()
                //手指往上滑动
                if (deltaY <= DRAG_GAP && currentViewStatus != STATUS_MOVING) return super.onTouchEvent(ev)
                //viewpager不在切换中，并且手指往下滑动，开始缩放
                if (currentViewPagerStatus != SCROLL_STATE_DRAGGING && deltaY > DRAG_GAP) {
                    moveView(ev.rawX, ev.rawY)
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (currentViewStatus != STATUS_MOVING) return super.onTouchEvent(ev)
                val mUpX = ev.rawX
                val mUpY = ev.rawY
                val vY = computeYVelocity() //松开时必须释放VelocityTracker资源
                if (vY >= 1200 || abs(mUpY - mDownY) > mScreenHeight / 8) {
                    //下滑速度快，或者下滑距离超过屏幕高度的1/8，就关闭
                    onPictureRelease()
                } else {
                    resetReviewState(mUpX, mUpY)
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    //返回浏览状态
    private fun resetReviewState(mUpX: Float, mUpY: Float) {
        currentViewStatus = STATUS_RESETTING
        when {
            mUpY != mDownY -> {
                val valueAnimator = ValueAnimator.ofFloat(mUpY, mDownY)
                valueAnimator.duration = BACK_DURATION
                valueAnimator.addUpdateListener { animation ->
                    val mY = animation.animatedValue as Float
                    val percent = (mY - mDownY) / (mUpY - mDownY)
                    val mX = percent * (mUpX - mDownX) + mDownX
                    moveView(mX, mY)
                    if (mY == mDownY) {
                        mDownY = 0f
                        mDownX = 0f
                        currentViewStatus = STATUS_NORMAL
                    }
                }
                valueAnimator.start()
            }
            mUpX != mDownX -> {
                val valueAnimator = ValueAnimator.ofFloat(mUpX, mDownX)
                valueAnimator.duration = BACK_DURATION
                valueAnimator.addUpdateListener { animation ->
                    val mX = animation.animatedValue as Float
                    val percent = (mX - mDownX) / (mUpX - mDownX)
                    val mY = percent * (mUpY - mDownY) + mDownY
                    moveView(mX, mY)
                    if (mX == mDownX) {
                        mDownY = 0f
                        mDownX = 0f
                        currentViewStatus = STATUS_NORMAL
                    }
                }
                valueAnimator.start()
            }
            else -> onPictureClick(mCurrentView!!)
        }
    }

    //移动View
    private fun moveView(movingX: Float, movingY: Float) {
        if (mCurrentView == null) return
        currentViewStatus = STATUS_MOVING
        val deltaX = movingX - mDownX
        val deltaY = movingY - mDownY
        var scale = 1f
        mAlphaPercent = 1f
        if (deltaY > 0) {
            scale = 1 - abs(deltaY) / mScreenHeight
            mAlphaPercent = max(0f, 1 - abs(deltaY) / (mScreenHeight / 3))
        }
        ViewHelper.setTranslationX(mCurrentView, deltaX)
        ViewHelper.setTranslationY(mCurrentView, deltaY)
        scaleView(scale)
        setBackgroundColor(getBlackAlpha(mAlphaPercent))
    }

    //缩放View
    @Suppress("NAME_SHADOWING")
    private fun scaleView(scale: Float) {
        var scale = scale
        scale = min(max(scale, MIN_SCALE_SIZE), 1f)
        ViewHelper.setScaleX(mCurrentView, scale)
        ViewHelper.setScaleY(mCurrentView, scale)
    }

    @Suppress("NAME_SHADOWING")
    private fun getBlackAlpha(percent: Float): Int {
        var percent = percent
        percent = min(1f, max(0f, percent))
        val intAlpha = (percent * 255).toInt()
        return Color.argb(intAlpha, 0, 0, 0)
    }

    private fun addIntoVelocity(event: MotionEvent) {
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain()
        mVelocityTracker!!.addMovement(event)
    }

    private fun computeYVelocity(): Float {
        var result = 0f
        if (mVelocityTracker != null) {
            mVelocityTracker!!.computeCurrentVelocity(1000)
            result = mVelocityTracker!!.yVelocity
            releaseVelocity()
        }
        return result
    }

    private fun releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    companion object {
        private const val TAG = "DragViewPager"
        private const val STATUS_NORMAL = 0 //正常浏览状态
        private const val STATUS_MOVING = 1 //滑动状态
        private const val STATUS_RESETTING = 2 //返回中状态
        private const val MIN_SCALE_SIZE = 0.3f //最小缩放比例
        private const val BACK_DURATION = 300L //ms
        private const val DRAG_GAP = 80
    }

    private fun onPictureRelease() {
        finishViewPager()
    }

    private fun onPictureClick(view: View) {
        finishViewPager()
    }

    //页面结束的时候调用,要显示一个移动当前控件到上一页对应Item位置的动画
    fun finishViewPager() {
        val location = IntArray(2)
        mCurrentView!!.getLocationOnScreen(location)
        val scaleX = mOriginWidth / mCurrentView!!.width
        val scaleY = mOriginHeight / mCurrentView!!.height
        val translationX = mOriginCenterX - (mCurrentViewOriginX + mCurrentView!!.width / 2)
        val translationY = mOriginCenterY - (mCurrentViewOriginY + mCurrentView!!.height / 2)
        val xTranslationAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "translationX", mCurrentView!!.translationX, translationX)
        val yTranslationAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "translationY", mCurrentView!!.translationY, translationY)
        val xScaleAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "scaleX", mCurrentView!!.scaleX, scaleX)
        val yScaleAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "scaleY", mCurrentView!!.scaleY, scaleY)
        val alphaAnimator = ValueAnimator.ofFloat(mAlphaPercent, 0f).apply {
            addUpdateListener {
                setBackgroundColor(getBlackAlpha(it.animatedValue as Float))
                if (it.animatedValue as Float == 0f) {
                    it.removeAllListeners()
                }
            }
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = mDuration
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                animation.removeAllListeners()
                xTranslationAnimator.removeAllListeners()
                yTranslationAnimator.removeAllListeners()
                (context as Activity).finish()
                (context as Activity).overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationStart(animation: Animator) {

            }

        })
        animatorSet.playTogether(xTranslationAnimator, yTranslationAnimator, xScaleAnimator, yScaleAnimator, alphaAnimator)
        animatorSet.start()
    }

    //页面刚进入的时候调用,要显示一个上一页对应Item移动到当前控件的动画
    private fun performEnterAnimation() {
        val location = IntArray(2)
        mCurrentView!!.getLocationOnScreen(location)
        mCurrentViewOriginX = location[0]
        mCurrentViewOriginY = location[1]
        val targetWidth = mCurrentView!!.width.toFloat()
        val targetHeight = mCurrentView!!.height.toFloat()
        val scaleX = mOriginWidth / targetWidth
        val scaleY = mOriginHeight / targetHeight
        val targetCenterX: Float = location[0] + targetWidth / 2
        val targetCenterY: Float = location[1] + targetHeight / 2
        val translationCenterX = mOriginCenterX - targetCenterX
        val translationCenterY = mOriginCenterY - targetCenterY
        //先把当前View的初始位置和宽高设置为上一页对应Item的位置和宽高
        mCurrentView!!.translationX = translationCenterX
        mCurrentView!!.translationY = translationCenterY
        mCurrentView!!.scaleX = scaleX
        mCurrentView!!.scaleY = scaleY
        //然后当前View移动和缩放到原始位置和宽高
        val xTranslationAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "translationX", mCurrentView!!.translationX, 0f)
        val yTranslationAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "translationY", mCurrentView!!.translationY, 0f)
        val xScaleAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "scaleX", scaleX, 1f)
        val yScaleAnimator = ObjectAnimator.ofFloat(mCurrentView!!, "scaleY", scaleY, 1f)
        val alphaAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                it.removeAllListeners()
                setBackgroundColor(getBlackAlpha(it.animatedValue as Float))
            }
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = mDuration
        animatorSet.playTogether(xTranslationAnimator, yTranslationAnimator, xScaleAnimator, yScaleAnimator, alphaAnimator)
        animatorSet.start()
    }

    fun destroy() {
        removeAllViews()
        DataListener.destroy(this)
        clearOnPageChangeListeners()
        customVideoViewMap.forEach {
            it.value.destroy()
        }
        customVideoViewMap.clear()
        customPhotoViewMap.forEach {
            it.value.destroy()
        }
        customPhotoViewMap.clear()
    }

    @Parcelize
    data class FilesBean(val originalImgUrl: String, val isVideo: Boolean = false) : Parcelable
}