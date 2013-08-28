new Handler().postDelayed(new Runnable(){  
	// 为了减少代码使用匿名Handler创建一个延时的调用
	            public void run() {  
	                Intent i = new Intent(SplashScreen.this, Main.class);   
	                //通过Intent打开最终真正的主界面Main这个Activity
	                SplashScreen.this.startActivity(i);    //启动Main界面
	                SplashScreen.this.finish();    //关闭自己这个开场屏
	            }  
	        }, 5000);   //5秒，够用了吧