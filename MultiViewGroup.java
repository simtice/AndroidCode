package com.qin.scrollerview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

//自定义ViewGroup ， 包含了三个LinearLayout控件，存放在不同的布局位置，通过scrollBy或者scrollTo方法切换
public class MultiViewGroup extends ViewGroup {

	private Context mContext;

	private static String TAG = "MultiViewGroup";
	private int curScreen = 0; // 当前屏

	private Scroller mScroller = null;

	public MultiViewGroup(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MultiViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	// startScroll 滑屏
	public void startMove() {
		curScreen++;
		Log.i(TAG, "----startMove---- curScreen " + curScreen);

		Log.i(TAG, "----width  " + getWidth());
		// 采用Scroller类控制滑动过程
		if(curScreen<=getChildCount()-1){
			mScroller.startScroll((curScreen - 1) * getWidth(), 0, getWidth(), 0,
					3000);
			// 暴力点直接到目标出
			// scrollTo(curScreen * getWidth(), 0);
			// 其实在点击按钮的时候，就回触发View绘制流程，这儿我们在强制绘制下View
			invalidate();
		}
	}

	// 停止滑屏
	public void stopMove() {

		Log.v(TAG, "----stopMove ----");

		if (mScroller != null) {
			// 如果动画还没结束，我们就按下了结束的按钮，那我们就结束该动画，即马上滑动指定位置
			if (!mScroller.isFinished()) {

				int scrollCurX = mScroller.getCurrX();
				// 判断是否达到下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
				// int moveX = scrollCurX - mScroller.getStartX() ;
				// Log.i(TAG, "----mScroller.is not finished ---- shouldNext" +
				// shouldNext);
				// boolean shouldNext = moveX >= getWidth() / 2 ;
				int descScreen = (scrollCurX + getWidth() / 2) / getWidth();

				Log.i(TAG, "----mScroller.is not finished ---- shouldNext"
						+ descScreen);

				Log.i(TAG, "----mScroller.is not finished ---- scrollCurX "
						+ scrollCurX);
				mScroller.abortAnimation();

				// 停止了动画，我们马上滑倒目标位置
				scrollTo(descScreen * getWidth(), 0);
				mScroller.forceFinished(true);

				curScreen = descScreen;
			}
		} else
			Log.i(TAG, "----OK mScroller.is  finished ---- ");
	}

	// 只有当前LAYOUT中的某个CHILD导致SCROLL发生滚动，才会致使自己的COMPUTESCROLL被调用
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		Log.e(TAG, "computeScroll");
		// 如果返回true，表示动画还没有结束
		// 因为前面startScroll，所以只有在startScroll完成时 才会为false
		if (mScroller.computeScrollOffset()) {
			Log.e(TAG, mScroller.getCurrX() + "======" + mScroller.getCurrY());
			// 产生了动画效果 每次滚动一点
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

			Log.e(TAG, "### getleft is " + getLeft() + " ### getRight is "
					+ getRight());

			// 刷新View 否则效果可能有误差
			postInvalidate();
		} else
			Log.i(TAG, "have done the scoller -----");
	}

	// ///以上可以演示Scroller类的使用
	// // --------------------------------
	// ///--------------------------------

	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState = TOUCH_STATE_REST;
	// --------------------------
	// 处理触摸事件 ~
	public static int SNAP_VELOCITY = 600;
	private int mTouchSlop = 0;
	private float mLastionMotionX = 0;
	private float mLastMotionY = 0;
	// 处理触摸的速率
	private VelocityTracker mVelocityTracker = null;

	// 这个感觉没什么作用 不管true还是false 都是会执行onTouchEvent的 因为子view里面onTouchEvent返回false了
	// touch 事件是否继续从父控件向子空间传递   true不传递
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

		final int action = ev.getAction();
		// 表示已经开始滑动了，不需要走该Action_MOVE方法了(第一次时可能调用)。
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "onInterceptTouchEvent move");
			final int xDiff = (int) Math.abs(mLastionMotionX - x);
			// 超过了最小滑动距离
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "onInterceptTouchEvent down");
			mLastionMotionX = x;
			mLastMotionY = y;
			Log.e(TAG, mScroller.isFinished() + "");
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;

			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "onInterceptTouchEvent up or cancel");
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		Log.e(TAG, mTouchState + "====" + TOUCH_STATE_REST);
		return mTouchState != TOUCH_STATE_REST;
	}

	public boolean onTouchEvent(MotionEvent event) {

		Log.i(TAG, "--- onTouchEvent--> ");

		// TODO Auto-generated method stub
		Log.e(TAG, "onTouchEvent start");
		if (mVelocityTracker == null) {

			Log.e(TAG, "onTouchEvent start-------** VelocityTracker.obtain");

			mVelocityTracker = VelocityTracker.obtain();
		}

		mVelocityTracker.addMovement(event);

		super.onTouchEvent(event);

		// 手指位置地点
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 如果屏幕的动画还没结束，你就按下了，我们就结束该动画
			if (mScroller != null) {
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
			}

			mLastionMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int detaX = (int) (mLastionMotionX - x);
//			if ((curScreen == 0 && detaX > 0)
//					|| (curScreen == getChildCount() - 1 && detaX < 0)
//					|| (curScreen != 0 && curScreen != getChildCount() - 1))// 第一屏和最后一屏不回弹
				scrollBy(detaX, 0);

			// scrollBy(detaX, 0);
			// scrollTo(detaX, 0);

			Log.e(TAG, "--- MotionEvent.ACTION_MOVE--> detaX is " + detaX);
			mLastionMotionX = x;

			break;
		case MotionEvent.ACTION_UP:

			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);

			int velocityX = (int) velocityTracker.getXVelocity();

			Log.e(TAG, "---velocityX---" + velocityX);

			// 滑动速率达到了一个标准(快速向右滑屏，返回上一个屏幕) 马上进行切屏处理
			if (velocityX > SNAP_VELOCITY && curScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(curScreen - 1);
			}
			// 快速向左滑屏，返回下一个屏幕)
			else if (velocityX < -SNAP_VELOCITY
					&& curScreen < (getChildCount() - 1)) {
				Log.e(TAG, "snap right");
				snapToScreen(curScreen + 1);
			}
			// 以上为快速移动的 ，强制切换屏幕
			else {
				// 我们是缓慢移动的，因此先判断是保留在本屏幕还是到下一屏幕
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			mTouchState = TOUCH_STATE_REST;

			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	// //我们是缓慢移动的
	private void snapToDestination() {
		// 当前的偏移位置
		int scrollX = getScrollX();
		int scrollY = getScrollY();

		Log.e(TAG, "### onTouchEvent snapToDestination ### scrollX is "
				+ scrollX);

		// 判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
		// 直接使用这个公式判断是哪一个屏幕 前后或者自己
		// 判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
		// 这样的一个简单公式意思是：假设当前滑屏偏移值即 scrollCurX 加上每个屏幕一半的宽度，除以每个屏幕的宽度就是
		// 我们目标屏所在位置了。 假如每个屏幕宽度为320dip, 我们滑到了500dip处，很显然我们应该到达第二屏
		int destScreen = (getScrollX() + getWidth() / 2) / getWidth();

		Log.e(TAG, "### onTouchEvent  ACTION_UP### dx destScreen " + destScreen);

		snapToScreen(destScreen);
	}

	private void snapToScreen(int whichScreen) {
		// 简单的移到目标屏幕，可能是当前屏或者下一屏幕
		// 直接跳转过去，不太友好
		// scrollTo(mLastScreen * getWidth(), 0);
		// 为了友好性，我们在增加一个动画效果
		// 需要再次滑动的距离 屏或者下一屏幕的继续滑动距离

		curScreen = whichScreen;

		if (curScreen > getChildCount() - 1)
			curScreen = getChildCount() - 1;

		int dx = curScreen * getWidth() - getScrollX();

		Log.e(TAG, "### onTouchEvent  ACTION_UP### dx is " + dx);

		mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 2);

		// 此时需要手动刷新View 否则没效果
		invalidate();

	}

	private void init() {

		mScroller = new Scroller(mContext);

		// 初始化3个 LinearLayout控件
		LinearLayout oneLL = new LinearLayout(mContext);
		oneLL.setBackgroundColor(Color.RED);
		addView(oneLL);

		LinearLayout twoLL = new LinearLayout(mContext);
		twoLL.setBackgroundColor(Color.YELLOW);
		addView(twoLL);

		LinearLayout threeLL = new LinearLayout(mContext);
		threeLL.setBackgroundColor(Color.BLUE);
		addView(threeLL);

		// 初始化一个最小滑动距离
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	// measure过程
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Log.i(TAG, "--- start onMeasure --");

		// 设置该ViewGroup的大小
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		System.out.println("width---"+width);
		System.out.println("height---"+height);
		setMeasuredDimension(width, height);

		int childCount = getChildCount();
		Log.i(TAG, "--- onMeasure childCount is -->" + childCount);
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			// 设置每个子视图的大小 ， 即全屏
			System.out.println("getWidth---"+getWidth());
			System.out.println("getMeasuredWidth---"+getMeasuredWidth());
			child.measure(getWidth(), MultiScreenActivity.scrrenHeight);
		}
	}

	private int curPage = 0;

	// layout过程
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.i(TAG, "--- start onLayout --");
		int startLeft = 0; // 每个子视图的起始布局坐标
		int startTop = 10; // 间距设置为10px 相当于 android：marginTop= "10px"
		int childCount = getChildCount();
		Log.i(TAG, "--- onLayout childCount is -->" + childCount);

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);

			// 即使可见的，才划到屏幕上
			if (child.getVisibility() != View.GONE)
				child.layout(startLeft, startTop, startLeft + getWidth(),
						startTop + MultiScreenActivity.scrrenHeight);

			startLeft = startLeft + getWidth(); // 校准每个子View的起始布局位置
			// 三个子视图的在屏幕中的分布如下 [0 , 320] / [320,640] / [640,960]
		}
	}
}
