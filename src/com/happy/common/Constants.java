package com.happy.common;

import java.awt.Color;
import java.io.File;

public class Constants {
	/**
	 * ==================================
	 * <p>
	 * 目录数据
	 * </p>
	 * ==================================
	 */

	/**
	 * 临时目录
	 */
	public final static String PATH_TEMP = "haplayer";

	/**
	 * Logcat日志目录
	 */
	public final static String PATH_LOGCAT = PATH_TEMP + File.separator
			+ "logcat";

	/**
	 * 歌曲目录
	 */
	public final static String PATH_MP3 = PATH_TEMP + File.separator + "mp3";

	/**
	 * 歌词目录
	 */
	public final static String PATH_KSC = PATH_TEMP + File.separator + "ksc";
	/**
	 * 歌手写真目录
	 */
	public final static String PATH_ARTIST = PATH_TEMP + File.separator
			+ "artist";
	/**
	 * 专辑图
	 */
	public final static String PATH_ALBUM = PATH_TEMP + File.separator
			+ "album";

	/**
	 * 皮肤
	 */
	public final static String PATH_SKIN = PATH_TEMP + File.separator + "skin";

	/**
	 * 播放列表数据和基本数据保存路径
	 */
	public final static String PATH_DATA = PATH_TEMP + File.separator + "data";

	/**
	 * 播放歌曲列表数据
	 */
	public final static String PATH_PLAYLISTDATA = PATH_DATA + File.separator
			+ "playdata";

	/**
	 * 窗口背景图片
	 */
	public final static String PATH_BACKGROUND = PATH_TEMP + File.separator
			+ "background";
	/**
	 * 图标
	 */
	public final static String PATH_ICON = PATH_TEMP + File.separator + "icon";

	/**
	 * 字体
	 */
	public final static String PATH_FONTS = PATH_TEMP + File.separator
			+ "fonts";

	/**
	 * 皮肤
	 */
	public final static String PATH_SPLASH = PATH_TEMP + File.separator
			+ "splash";
	/**
	 * ==================================
	 * <p>
	 * 基本数据
	 * </p>
	 * ==================================
	 */
	/**
	 * app应用名
	 */
	public final static String APPNAME = "HappyPlayer-PC";
	/**
	 * app标题
	 */
	public final static String APPTITLE = "乐乐音乐";
	/**
	 * app提示语
	 */
	public final static String APPTIPTITLE = "乐乐2015,传播好的音乐";
	/**
	 * app字体大小
	 */
	public final static int APPFONTSIZE = 15;

	/**
	 * 歌曲播放模式
	 */
	public static int playModel = 0; // 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
	/**
	 * 歌曲播放模式key
	 */
	public static String playModel_KEY = "playModel_KEY";

	/**
	 * 是否显示桌面歌词
	 */
	public static boolean showDesktopLyrics = false;
	/**
	 * 是否显示桌面歌词key
	 */
	public static String showDesktopLyrics_KEY = "showDesktopLyrics_KEY";

	/**
	 * 桌面背景图片
	 */
	public static String backGroundName = "01.jpg";
	/**
	 * 桌面背景图片key
	 */
	public static String backGroundName_KEY = "backGroundName_KEY";

	/**
	 * 标题图标
	 */
	public static String iconName = "ic_launcher.png";
	/**
	 * 标题图标key
	 */
	public static String iconName_KEY = "iconName_KEY";
	/**
	 * 主要窗口的宽度
	 */
	public static int mainFrameWidth = 0;
	/**
	 * 主要窗口的宽度key
	 */
	public static String mainFrameWidth_KEY = "mainFrameWidth_KEY";
	/**
	 * 声音大小
	 */
	public static int volumeSize = 25;
	/**
	 * 声音大小key
	 */
	public static String volumeSize_KEY = "volumeSize_KEY";
	/**
	 * 主要窗口的高度
	 */
	public static int mainFrameHeight = 0;

	/**
	 * 主要窗口的高度key
	 */
	public static String mainFrameHeight_KEY = "mainFrameHeight_KEY";
	/**
	 * 窗口x轴
	 */
	public static int mainFramelocaltionX = 30;
	public static String mainFramelocaltionX_KEY = "mainFramelocaltionX_KEY";

	/**
	 * 窗口y轴
	 */
	public static int mainFramelocaltionY = 30;
	public static String mainFramelocaltionY_KEY = "mainFramelocaltionY_KEY";

	/**
	 * 播放歌曲id
	 */
	public static String playInfoID = "";
	/**
	 * 播放歌曲id key
	 */
	public static String playInfoID_KEY = "playInfoID_KEY";

	/**
	 * 歌词颜色索引
	 */
	public static int lrcColorIndex = 4;
	/**
	 * 歌词颜色索引key
	 */
	public static String lrcColorIndex_KEY = "lrcColorIndex_KEY";

	/**
	 * 播放列表界面透明度
	 */
	public static int listViewAlpha = 100;
	/**
	 * 播放列表界面透明度
	 */
	public static String listViewAlpha_KEY = "listViewAlpha_KEY";

	/**
	 * 歌词颜色集合
	 */
	public static Color[] lrcColorStr = { new Color(250, 218, 131),
			new Color(93, 177, 240), new Color(225, 125, 179),
			new Color(157, 196, 0), new Color(138, 1, 226) };

	/**
	 * 歌词最小大小
	 */
	public static int lrcFontMinSize = 0;
	/**
	 * 歌词最大大小
	 */
	public static int lrcFontMaxSize = 0;
	/**
	 * 歌词大小
	 */
	public static int lrcFontSize = lrcFontMinSize;

	/**
	 * 桌面歌词最小大小
	 */
	public static int desktopLrcFontMinSize = 150;
	/**
	 * 桌面歌词最大大小
	 */
	public static int desktopLrcFontMaxSize = 150;
	/**
	 * 桌面歌词大小
	 */
	public static int desktopLrcFontSize = desktopLrcFontMinSize;

	/**
	 * 桌面歌词字体大小key
	 */
	public static String desktopLrcFontSize_KEY = "desktopLrcFontSize_KEY";

	/**
	 * 未读歌词颜色
	 */
	public static Color DESLRCNOREADCOLOR[] = { new Color(150, 209, 254),
			new Color(214, 142, 237), new Color(214, 215, 214),
			new Color(233, 74, 69) };
	/**
	 * 已读歌词颜色
	 */
	public static Color DESLRCREADEDCOLOR[] = { new Color(229, 146, 230),
			new Color(248, 246, 151), new Color(133, 208, 255),
			new Color(255, 193, 120) };

	/***
	 * 桌面歌词颜色
	 */
	public static String desktopLrcIndex_KEY = "DEF_DES_COLOR_INDEX_KEY";
	public static int desktopLrcIndex = 0;

	/**
	 * 主面板宽度
	 */
	public static int mainPanelWidth = 0;
	/**
	 * 主面板高度
	 */
	public static int mainPanelHeight = 0;

	/**
	 * 歌曲列表单击索引
	 */
	public static int sSingleClickIndex = -1;
	/**
	 * 歌曲列表双击索引
	 */
	public static int sDoubleClickIndex = -1;
	/**
	 * 播放列表展示索引
	 */
	public static int pShowIndex = -1;
	/**
	 * 单击列表的索引
	 */
	public static int pSingleClickIndex = -1;
	/**
	 * 双击列表的索引
	 */
	public static int pDoubleClickIndex = -1;
	/**
	 * 桌面歌词是否锁住
	 */
	public static boolean desLrcIsLock = false;

}
