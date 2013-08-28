package mars.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader
{
	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
	 * 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象
	 * 3.得到InputStram
	 * 4.从InputStream当中读取数据
	 */
	private URL url = null;

	public String downStr(String urlStr)//下载字符流的方法
	{
		/**
		 * String和StringBuffer他们都可以存储和操作字符串，即包含多个字符的字符串数据。
		 * String类是字符串常量，是不可更改的常量。而StringBuffer是字符串变量，它的对象是可以扩充和修改的。 
		 */
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;//BufferedReader类用于从缓冲区中读取内容

		try
		{
			/**
			 * 因为直接使用InputStream不好用，多以嵌套了BufferedReader，这个是读取字符流的固定格式。
			 */
			
			url = new URL(urlStr);// 创建一个URL对象		
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();// 创建一个Http连接
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));// 使用IO流读取数据
			
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				buffer.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 *  -1：代表下载文件出错 
	 *   0：代表下载文件成功 
	 *   1：代表文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName)//下载文件的方法
	{
		InputStream inputStream = null;
		try
		{
			FileUtils fileUtils = new FileUtils();

			if (fileUtils.isFileExist(path + fileName))
			{
				return 1;
			}
			else
			{
				inputStream = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path, fileName,inputStream);
				if (resultFile == null)
				{
					return -1;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException
	{
		url = new URL(urlStr);// 创建一个URL对象	
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();// 创建一个Http连接
		InputStream inputStream = urlConn.getInputStream();//得到输入流
		
		return inputStream;
	}
}
