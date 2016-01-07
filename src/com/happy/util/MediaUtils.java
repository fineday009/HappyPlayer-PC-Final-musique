package com.happy.util;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import com.happy.model.SongInfo;

public class MediaUtils {
	/**
	 * 通过文件获取mp3的相关数据信息
	 * 
	 * @param filePath
	 * @return
	 */

	public static SongInfo getSongInfoByFile(String filePath) {
		File sourceFile = new File(filePath);
		if (!sourceFile.exists())
			return null;
		SongInfo songInfo = null;
		try {
			Encoder encoder = new Encoder();
			MultimediaInfo m = encoder.getInfo(sourceFile);
			long duration = m.getDuration();

			String durationStr = formatTime((int) duration);

			songInfo = new SongInfo();
			// 文件名
			String displayName = sourceFile.getName();
			if (displayName.contains(".mp3")) {
				String[] displayNameArr = displayName.split(".mp3");
				displayName = displayNameArr[0].trim();
			} else if (displayName.contains(".wav")) {
				String[] displayNameArr = displayName.split(".wav");
				displayName = displayNameArr[0].trim();
			}
			String artist = "";
			String title = "";
			if (displayName.contains("-")) {
				String[] titleArr = displayName.split("-");
				artist = titleArr[0].trim();
				title = titleArr[1].trim();
			} else {
				title = displayName;
			}

			if (sourceFile.length() < 1024 * 1024) {
				return null;
			}

			songInfo.setSid(IDGenerate.getId());
			songInfo.setDisplayName(displayName);
			songInfo.setSinger(artist);
			songInfo.setTitle(title);
			songInfo.setDuration(duration);
			songInfo.setDurationStr(durationStr);
			songInfo.setSize(sourceFile.length());
			songInfo.setSizeStr(getFileSize(sourceFile.length()));
			songInfo.setFilePath(filePath);
			songInfo.setType(SongInfo.LOCALSONG);
			// songInfo.setIslike(SongInfo.UNLIKE);
			// songInfo.setDownloadStatus(SongInfo.DOWNLOADED);
			songInfo.setCreateTime(DateUtil.dateToString(new Date()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return songInfo;

	}

	/**
	 * 时间格式转换
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(int time) {
		time /= 1000;
		int minute = time / 60;
		// int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	/**
	 * 计算文件的大小，返回相关的m字符串
	 * 
	 * @param fileS
	 * @return
	 */
	public static String getFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
