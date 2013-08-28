public class Download extends Activity implements OnClickListener
{
	/** Called when the activity is first created. */
	private Button downloadTxtButton;
	private Button downloadMp3Button;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findView();
	}

	public void findView()
	{
		downloadTxtButton = (Button) findViewById(R.id.downloadTxt);
		downloadTxtButton.setOnClickListener(this);

		downloadMp3Button = (Button) findViewById(R.id.downloadMp3);
		downloadMp3Button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		int viewId = v.getId();
		switch (viewId)
		{
		
		/**
		 * 这个方法只可以下载文本文件，它是逐行读取字符窜的。
		 */
		case R.id.downloadTxt:
		{
			HttpDownloader httpDownloader = new HttpDownloader();
			String lrc = httpDownloader.downStr("http://61.184.100.229/");//这个地方的url可以自己定义
			Log.e("@@@@", "downloadTxt: " + lrc);
		}
		
		/**
		 * 这个方法可以下载任何文件。
		 */
		case R.id.downloadMp3:
		{
			HttpDownloader httpDownloader = new HttpDownloader();
			int result = httpDownloader.downFile(
					"http://192.168.1.107:8080/voa1500/a1.mp3", "voa/",
					"a1.mp3");//这个地方的url同样可以自己定义
			Log.e("@@@@", "downloadMp3");
		}
		default:
			break;
		}
	}
}