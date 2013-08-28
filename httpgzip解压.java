package com.ulwx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

import com.ulwx.tool.HttpUtils;

public class Test {

	public static void main(String[] args) throws Exception {
		String url = "http://127.0.0.1:8080/ndindustry/industry?coidentity=test&key=e6d03692eeeda43870fa00a0eccc6b02ce40bd52&protocol=20001&devicesno=123456789&mac=90E6BAE05903&imei=38756234957854&model=F9V1&resolution=1024*768&appname=NDNews_F9V1&appver=1.0.0&ndjh=3.141592653@ndjh.cn&gzip=true";
		byte[] bytes = HttpUtils.getBytes(url);
		System.out.println(new String(bytes, "UTF-8"));
		byte[] s = unGZip(bytes);
		System.out.println(new String(s, "UTF-8"));
	}

	/**
	 * gZip解压方法
	 * */
	public static byte[] unGZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

}
