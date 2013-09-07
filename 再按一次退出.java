long waitTime = 2000;
	long touchTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				CommonUtil.showToask(getApplicationContext(), "再按一次退出");
				touchTime = currentTime;
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}