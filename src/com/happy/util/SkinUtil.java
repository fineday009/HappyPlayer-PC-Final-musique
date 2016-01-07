package com.happy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.happy.common.Constants;

/**
 * 皮肤处理类
 * 
 * @author zhangliangming
 * 
 */
public class SkinUtil {

	/**
	 * 皮肤数据
	 */
	private static List<String> skinDatas;

	/**
	 * 获取皮肤数据
	 * 
	 * @return
	 */
	public static List<String> getAllSkinData() {
		if (skinDatas == null) {
			skinDatas = new ArrayList<String>();
			File skinFileParent = new File(Constants.PATH_BACKGROUND);
			File[] skinFiles = skinFileParent.listFiles();
			for (int i = 0; i < skinFiles.length; i++) {
				File skinFile = skinFiles[i];
				if (skinFile.getPath().endsWith(".jpg")) {
					skinDatas.add(skinFile.getName());
				}
			}
		}
		return skinDatas;

	}
}
