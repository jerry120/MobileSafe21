package com.itheima.mobilesafe21.utils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
	// 压缩
	public static void zip(File sourceFile, File targetFile) {
		// 用gzipoutputstream进行压缩
		FileInputStream fis = null;
		GZIPOutputStream gos = null;
		try {
			fis = new FileInputStream(sourceFile);
			gos = new GZIPOutputStream(new FileOutputStream(targetFile));
			int len = -1;
			byte[] buffer = new byte[1024];

			while ((len = fis.read(buffer)) != -1) {
				gos.write(buffer, 0, len);
			}

			gos.flush();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeIos(fis, gos);
		}

	}

	// 解压
	public static void unzip(File sourceFile, File targetFile) {
		// 使用gzipinputstream进行解压
		GZIPInputStream gis = null;
		FileOutputStream fos = null;
		try {
			gis = new GZIPInputStream(new FileInputStream(sourceFile));
			fos = new FileOutputStream(targetFile);

			int len = -1;
			byte[] buffer = new byte[1024];

			while ((len = gis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

			fos.flush();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeIos(gis, fos);
		}

	}
	
	// 解压
		public static void unzip(InputStream inputstream, File targetFile) {
			// 使用gzipinputstream进行解压
			GZIPInputStream gis = null;
			FileOutputStream fos = null;
			try {
				gis = new GZIPInputStream(inputstream);
				fos = new FileOutputStream(targetFile);

				int len = -1;
				byte[] buffer = new byte[1024];

				while ((len = gis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}

				fos.flush();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeIos(gis, fos);
			}

		}

	public static void closeIos(Closeable... io) {
		if (io != null) {
			for (int i = 0; i < io.length; i++) {
				Closeable closeable = io[i];
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
	}

}
