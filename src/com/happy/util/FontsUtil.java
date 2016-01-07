package com.happy.util;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 字体处理类
 * 
 * @author zhangliangming
 * 
 */
public class FontsUtil {
	/**
	 * 根据字体文件获取字体
	 * 
	 * @param filePath
	 * @param fontSize
	 *            字体大小
	 * @return
	 */
	public static Font getFontByFile(String filePath, int fontSize) {
		Font font = null;
		try {
			InputStream is = new FileInputStream(filePath);
			BufferedInputStream bis = new BufferedInputStream(is);
			// createFont返回一个使用指定字体类型和输入数据的新 Font。<br>
			// 新 Font磅值为 1，样式为 PLAIN,注意 此方法不会关闭 InputStream
			font = Font.createFont(Font.TRUETYPE_FONT, bis);
			font = font.deriveFont(Font.CENTER_BASELINE, fontSize);
			if (null != bis) {
				bis.close();
			}
			if (null != is) {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (font == null) {
			font = new Font("宋体", Font.CENTER_BASELINE, fontSize);
		}

		return font;
	}

	/**
	 * 获取默认字体
	 * 
	 * @param fontSize
	 * @return
	 */
	public static Font getBaseFont(int fontSize) {
		return new Font("宋体", Font.CENTER_BASELINE, fontSize);
	}
}
