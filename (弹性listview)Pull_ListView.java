package com.linghu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class Pull_ListView extends ListView implements Runnable{
	private float mLastDownY = 0f;
	private int mDistance = 0;
	private int mStep = 10;
	private boolean mPositive = false;

	public Pull_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Pull_ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Pull_ListView(Context context) {
		super(context);
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mLastDownY == 0f && mDistance == 0) {
				mLastDownY = event.getY();
				return true;
			}
			break;

		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			if (mDistance != 0) {
				mStep = 1;
				mPositive = (mDistance >= 0);
				this.post(this);
				return true;
			}
			mLastDownY = 0f;
			mDistance = 0;
			break;

		case MotionEvent.ACTION_MOVE:
			if (mLastDownY != 0f) {
				mDistance = (int) (mLastDownY - event.getY());
				if ((mDistance < 0 && getFirstVisiblePosition() == 0 && getChildAt(
						0).getTop() == 0)
						|| (mDistance > 0 && getLastVisiblePosition() == getCount() - 1)) {
					setVerticalScrollBarEnabled(false);// 隐藏滚动条
					mDistance /= 3;
					scrollTo(0, mDistance);
					return true;
				}else
					setVerticalScrollBarEnabled(true);// 显示滚动条
			}
			mDistance = 0;
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void run() {
		System.out.println("mStep--"+mStep);
		//mDistance>0：向下拖动了,则每次往上滚动一个位置
		mDistance += mDistance > 0 ? -mStep : mStep; 
		scrollTo(0, mDistance);
		//mPositive = ture：向下拖动了,mDistance<= 0 ：自动向上滚动到原始位置了
		//mPositive = flase：向上拖动了,mDistance>= 0 ：自动向下滚动到原始位置了
		if ((mPositive && mDistance <= 0) || (!mPositive && mDistance >= 0)) {
			scrollTo(0, 0);
			mDistance = 0;
			mLastDownY = 0f;
			return;
		}
		mStep += 1;
		this.postDelayed(this, 5);
	}
}
