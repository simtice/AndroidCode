//ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
private void setLvHeight(ListView lvReview) {
		ListAdapter adapter = lvReview.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View itemView = adapter.getView(i, null, lvReview);
			itemView.measure(0, 0);
			totalHeight += itemView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams layoutParams = lvReview.getLayoutParams();
		layoutParams.height = totalHeight
				+ (lvReview.getDividerHeight() * (adapter.getCount() - 1));// 总行高+每行的间距
		lvReview.setLayoutParams(layoutParams);
	}