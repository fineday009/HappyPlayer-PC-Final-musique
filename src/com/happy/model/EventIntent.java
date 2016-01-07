package com.happy.model;

/**
 * 上一首，下一首，鼠标点击，单击时使用
 * 
 * @author zhangliangming
 * 
 */
public class EventIntent {
	/**
	 * 播放列表类型
	 */
	public static final int PLAYLIST = 0;
	/**
	 * 歌曲列表类型
	 */
	public static final int SONGLIST = 1;
	/**
	 * 单击
	 */
	public static final int SINGLECLICK = 0;
	/**
	 * 双击
	 */
	public static final int DOUBLECLICK = 1;
	/**
	 * 点击次数
	 */
	private int clickCount;

	/**
	 * 播放列表id
	 */
	private int pIndex;

	/**
	 * 歌曲列表id
	 */
	private int sIndex;
	/**
	 * 播放列表展开列表id
	 */
	private int pShowIndex;

	// 播放列表参数
	private boolean isShow;

	/**
	 * 类型
	 */
	private int type;

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public int getpIndex() {
		return pIndex;
	}

	public void setpIndex(int pIndex) {
		this.pIndex = pIndex;
	}

	public int getsIndex() {
		return sIndex;
	}

	public void setsIndex(int sIndex) {
		this.sIndex = sIndex;
	}

	public int getpShowIndex() {
		return pShowIndex;
	}

	public void setpShowIndex(int pShowIndex) {
		this.pShowIndex = pShowIndex;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean getShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

}
