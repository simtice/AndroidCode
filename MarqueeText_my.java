package com.kingter.ynjk.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 注意：在布局文件引用本view时,paddingLeft,paddingRigh都必须为0dp android:ellipsize="marquee"
 * android:singleLine="true" 这两个属性也要加上
 */
public class MarqueeText extends TextView implements Runnable,OnClickListener {
	private int currentScrollX;// 当前滚动的位置
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;
	private int screenWidth;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			startScroll();
		}
		
	};
	public MarqueeText(Context context) {
		super(context);
		init(context);
		
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		this.setOnClickListener(this);
		DisplayMetrics dm = new DisplayMetrics(); 
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		screenWidth = dm.widthPixels;        // 屏幕宽（dip，如：320dip）  
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (!isMeasure) {// 文字宽度只需获取一次就可以了
			getTextWidth();
			isMeasure = true;
		}
		if(getScrollX()==0){
			this.stopScroll();
		}
	}
	
	/**
	 * 获取文字高度
	 */
	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}

	//重写setText 在setText的时候重新计算text的宽度
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		super.setText(text, type);
		this.isMeasure = false;
		startFor0();
	}
	
	@Override
	public void run() {
		if (isStop) {
			return;
		}
		currentScrollX += 2;// 滚动速度
		scrollTo(currentScrollX, 0);
		if (getScrollX() >=textWidth) {
			scrollTo(-screenWidth, 0);
			currentScrollX = -screenWidth;
			// return;
		}
		postDelayed(this, 50);
	}

	// 开始滚动
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	// 停止滚动
	public void stopScroll() {
		isStop = true;
	}

	// 从头开始滚动
	public void startFor0() {
		currentScrollX = 0;
		startScroll();
	}

	@Override
	public void onClick(View v) {
//		stopScroll();
//		handler.removeCallbacks(runnable);
//		handler.postDelayed(runnable, 5000);
		this.startScroll();
	}
}
