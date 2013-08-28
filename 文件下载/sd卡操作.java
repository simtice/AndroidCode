package mars.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils
{
	private String SDPATH;

	public String getSDPATH()
	{
		return SDPATH;
	}

	public FileUtils()
	{
		//得到当前SDCARD存储设备的目录 /SDCARD, Environment.getExternalStorageDirectory()这个方法比较通用
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 */
	public File creatSDFile(String fileName) throws IOException
	{
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 */
	public File creatSDDir(String dirName)
	{
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName)
	{
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input)
	{
		File file = null;
		OutputStream output = null;
		try//InputStream里面的数据写入到SD卡中的固定方法
		{
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1)
			{
				output.write(buffer);
			}
			output.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				output.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}
}