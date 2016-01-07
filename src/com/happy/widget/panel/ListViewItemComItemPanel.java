package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.model.EventIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;

/**
 * 歌曲列表item面板
 * 
 * @author zhangliangming
 * 
 */
public class ListViewItemComItemPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	/**
	 * 歌曲信息
	 */
	private SongInfo songInfo;

	/**
	 * 默认高度
	 */
	private int defHeight = 50;
	/**
	 * 点击高度
	 */
	private int selectHeight = 80;
	/**
	 * 高度
	 */
	private int height = defHeight;

	/**
	 * 宽度
	 */
	private int width = 0;

	/**
	 * 播放列表面板
	 */
	private JPanel listViewJPanel;

	/**
	 * 鼠标经过
	 */
	private boolean isEnter = false;

	/**
	 * 双选
	 */
	private boolean isDoubSelect = false;
	/**
	 * 单选
	 */
	private boolean isSingleSelect = false;
	/**
	 * 播放列表索引
	 */
	private int pindex = 0;
	/**
	 * 歌曲列表索引
	 */
	private int sindex = 0;
	/**
	 * 歌曲名称
	 */
	private JLabel songName;

	/**
	 * 歌曲长度
	 */
	private JLabel songSize;
	/**
	 * 歌手图片
	 */
	private JLabel singerIconLabel;

	public ListViewItemComItemPanel(JPanel mlistViewJPanel, int mpindex,
			int msindex, SongInfo msongInfo, int mWidth) {
		this.pindex = mpindex;
		this.sindex = msindex;
		this.listViewJPanel = mlistViewJPanel;
		this.width = mWidth;
		this.songInfo = msongInfo;

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (e.getClickCount() == 1) {

					// 如果当前歌曲正在播放，则对其它进行暂停操作
					if (Constants.sDoubleClickIndex == sindex
							&& Constants.pDoubleClickIndex == pindex) {
						if (songInfo.getSid().equals(Constants.playInfoID)) {

							if (MediaManage.getMediaManage().getPlayStatus() == MediaManage.PLAYING) {
								// 当前正在播放，发送暂停
								SongMessage msg = new SongMessage();
								msg.setSongInfo(songInfo);
								msg.setType(SongMessage.PAUSEMUSIC);
								ObserverManage.getObserver().setMessage(msg);
							} else {

								SongMessage songMessage = new SongMessage();
								songMessage.setType(SongMessage.PLAYMUSIC);
								// 通知
								ObserverManage.getObserver().setMessage(
										songMessage);
							}
						}
					}
					EventIntent eventIntent = new EventIntent();
					eventIntent.setType(EventIntent.SONGLIST);
					eventIntent.setpIndex(pindex);
					eventIntent.setsIndex(sindex);
					eventIntent.setClickCount(EventIntent.SINGLECLICK);

					ObserverManage.getObserver().setMessage(eventIntent);

				}

				if (e.getClickCount() == 2) {
					// 如果当前歌曲正在播放，则对其它进行暂停操作
					if (Constants.sDoubleClickIndex == sindex
							&& Constants.pDoubleClickIndex == pindex) {

						// 双击，播放歌曲
						if (songInfo.getSid().equals(Constants.playInfoID)) {

							return;
						}
					} else {

						EventIntent eventIntent = new EventIntent();
						eventIntent.setType(EventIntent.SONGLIST);
						eventIntent.setpIndex(pindex);
						eventIntent.setsIndex(sindex);
						eventIntent.setClickCount(EventIntent.DOUBLECLICK);

						ObserverManage.getObserver().setMessage(eventIntent);
					}

					Constants.playInfoID = songInfo.getSid();

					if (MediaManage.getMediaManage().getPindex() != pindex) {

						// 设置播放时的索引
						MediaManage.getMediaManage().setPindex(pindex);
						// 更新歌曲列表
						MediaManage.getMediaManage().upDateSongListData(pindex);
					}
					MediaManage.getMediaManage().setSindex(sindex);

					// 发送播放
					SongMessage msg = new SongMessage();
					msg.setSongInfo(songInfo);
					msg.setType(SongMessage.PLAYINFOMUSIC);
					ObserverManage.getObserver().setMessage(msg);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				isEnter = false;
				listViewJPanel.revalidate();
				listViewJPanel.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				isEnter = true;
				listViewJPanel.revalidate();
				listViewJPanel.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		initComponent();
		this.setOpaque(false);
	}

	public void initComponent() {
		this.setLayout(null);

		if (songInfo.getSid().equals(Constants.playInfoID)) {
			isDoubSelect = true;
			initSelectedComponent();
		} else {
			isDoubSelect = false;
			initDefComponent();
		}
	}

	/**
	 * 初始化选中布局
	 */
	public void initSelectedComponent() {
		this.removeAll();
		if (isDoubSelect) {
			height = selectHeight;
		} else {
			height = defHeight;
		}
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));

		songName = new JLabel(songInfo.getDisplayName());
		songSize = new JLabel(songInfo.getDurationStr());

		String singerIconPath = Constants.PATH_ICON + File.separator
				+ "singer_default_image.png";
		ImageIcon singerIcon = new ImageIcon(singerIconPath);
		singerIcon.setImage(singerIcon.getImage().getScaledInstance(
				height * 2 / 3, height * 2 / 3, Image.SCALE_SMOOTH));
		singerIconLabel = new JLabel(singerIcon);

		singerIconLabel.setBounds(10, (height - height * 2 / 3) / 2,
				height * 2 / 3, height * 2 / 3);

		songName.setBounds(
				10 + singerIconLabel.getX() + singerIconLabel.getWidth(), 0,
				width / 2, height);
		songSize.setBounds(width - 60 - 20, 0, 60, height);

		this.add(singerIconLabel);
		this.add(songName);
		this.add(songSize);

		// this.revalidate();
		// this.repaint();
		// listViewJPanel.revalidate();
		// listViewJPanel.repaint();
		listViewJPanel.updateUI();
	}

	/**
	 * 初始化默认布局
	 */
	public void initDefComponent() {
		this.removeAll();
		if (isDoubSelect) {
			height = selectHeight;
		} else {
			height = defHeight;
		}
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));

		songName = new JLabel(songInfo.getDisplayName());
		songSize = new JLabel(songInfo.getDurationStr());
		this.add(songName);
		this.add(songSize);

		songName.setBounds(10, 0, width / 2, height);
		songSize.setBounds(width - 60 - 20, 0, 60, height);

		// this.revalidate();
		// this.repaint();
		// listViewJPanel.revalidate();
		// listViewJPanel.repaint();
		// this.updateUI();
		listViewJPanel.updateUI();
	}

	public void setSingleSelect(boolean isSingleSelect) {
		this.isSingleSelect = isSingleSelect;
		listViewJPanel.revalidate();
		listViewJPanel.repaint();
	}

	public void setDoubSelect(boolean isDoubSelect) {
		this.isDoubSelect = isDoubSelect;
		if (isDoubSelect) {
			isSingleSelect = false;
			initSelectedComponent();
		} else {
			isSingleSelect = false;
			initDefComponent();
		}
	}

	// 绘制组件
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 消除线条锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (isDoubSelect) {
			g2d.setPaint(new Color(0, 0, 0, 80));
		} else if (isSingleSelect) {
			g2d.setPaint(new Color(0, 0, 0, 50));
		} else if (isEnter) {
			g2d.setPaint(new Color(0, 0, 0, 20));
		} else {
			g2d.setPaint(new Color(0, 0, 0, 0));
		}
		g2d.fillRect(0, 0, width, height);
	}

}
