package com.happy.model;

/**
 * 消息传递类
 * 
 * @author zhangliangming
 * 
 */
public class MessageIntent {
	/**
	 * 窗口最小化
	 */
	public static final String FRAME_MIN = "com.happy.frame.min";
	/**
	 * 默认
	 */
	public static final String FRAME_NORMAL = "com.happy.frame.normal";

	/**
	 * 打开皮肤窗口
	 */
	public static final String OPEN_SKINDIALOG = "com.happy.frame.openskindialog";
	/**
	 * 更新列表的透明度
	 */
	public static final String UPDATE_LISTVIEW_ALPHA = "com.happy.frame.update.listviewalpha";

	/**
	 * 更新皮肤
	 */
	public static final String UPDATE_SKIN = "com.happy.frame.update.skin";

	/**
	 * 多行歌词字体大小
	 */
	public static final String KSCMANYLINEFONTSIZE = "com.hp.ksc.fontsize";
	/**
	 * 多行歌词歌词颜色
	 */
	public static final String KSCMANYLINELRCCOLOR = "com.hp.ksc.lrc.color";

	/**
	 * 桌面歌词窗口
	 */
	public static final String OPENORCLOSEDESLRC = "com.happy.frame.openorclosedeslrc";
	/**
	 * 桌面歌词桌面关闭
	 */
	public static final String CLOSEDESLRC = "com.happy.frame.closedeslrc";

	/**
	 * 多行桌面歌词字体大小
	 */
	public static final String DESKSCMANYLINEFONTSIZE = "com.hp.ksc.des.fontsize";
	/**
	 * 多行桌面歌词歌词颜色
	 */
	public static final String DESKSCMANYLINELRCCOLOR = "com.hp.ksc.lrc.des.color";
	/**
	 * 播放器音量
	 */
	public static final String PLAYERVOLUME = "com.hp.player.Volume";

	/**
	 * 桌面歌词锁
	 */
	public static final String LOCKDESLRC = "com.happy.frame.locklrc";
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
