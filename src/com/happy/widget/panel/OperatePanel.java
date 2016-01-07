package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.model.MessageIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.util.MediaUtils;
import com.happy.widget.button.BaseButton;
import com.happy.widget.dialog.DesLrcDialog;
import com.happy.widget.slider.BaseSlider;

/**
 * 操作面板进度条、上一首、下一首等
 * 
 * @author zhangliangming
 * 
 */
public class OperatePanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;
	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;

	/**
	 * 歌名Label
	 * 
	 */
	private JLabel songNameLabel;
	/**
	 * 歌曲进度条
	 */
	private BaseSlider songSlider;

	/**
	 * 判断其是否是正在拖动
	 */
	private boolean isStartTrackingTouch = false;

	/**
	 * 歌曲播放进度
	 * 
	 */
	private JLabel songProgressLabel;

	/**
	 * 歌曲总进度
	 * 
	 */
	private JLabel songSizeLabel;
	/**
	 * 显示桌面歌词按钮
	 */
	private BaseButton showDesktopLyricsButton;
	/**
	 * 不显示桌面歌词按钮
	 */
	private BaseButton unShowDesktopLyricsButton;

	/**
	 * 随机播放
	 */
	private BaseButton randomButton;
	/**
	 * 顺序播放
	 */
	private BaseButton sequenceButton;
	/**
	 * 单曲循环
	 */
	private BaseButton singleRepeatButton;

	/**
	 * 列表循环播放
	 */
	private BaseButton listRepeatButton;

	/**
	 * 单曲播放
	 */
	private BaseButton singleButton;

	/**
	 * 播放模式
	 */
	private int playModel = Constants.playModel;
	/**
	 * 播放按钮
	 */
	private BaseButton playButton;
	/**
	 * 暂停按钮
	 */
	private BaseButton pauseButton;
	/**
	 * 上一首
	 */
	private BaseButton preButton;
	/**
	 * 下一首
	 */
	private BaseButton nextButton;

	/**
	 * 顶部距离
	 */
	private int topPadding = 15;
	/**
	 * 左边距离
	 */
	private int leftPadding = 15;
	/**
	 * 音量
	 */
	private BaseButton volumeButton;
	/**
	 * 音量滑块
	 */
	private BaseSlider volumeSlider;

	private DesLrcDialog desLrcDialog;

	public OperatePanel(DesLrcDialog desLrcDialog) {
		this.desLrcDialog = desLrcDialog;
		initComponent();
		ObserverManage.getObserver().addObserver(this);
		this.setOpaque(false);
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		this.setLayout(null);

		int operatePanelWidth = Constants.mainPanelWidth;
		int operatePanelHeightTemp = Constants.mainPanelHeight / 4;
		int operatePanelHeight = operatePanelHeightTemp - 5 * topPadding;

		int bH = operatePanelHeight / 2 / 3;

		songNameLabel = new JLabel();
		songNameLabel.setText(Constants.APPTITLE);
		songNameLabel.setForeground(Color.white);
		songNameLabel.setBounds(leftPadding, topPadding, operatePanelWidth
				- leftPadding * 2, bH + 5);

		songSlider = new BaseSlider();
		songSlider.setOpaque(false); // slider的背景透明
		songSlider.setFocusable(false);
		songSlider.setValue(0);
		songSlider.setMaximum(0);

		songSlider.setBounds(leftPadding,
				songNameLabel.getY() + songNameLabel.getHeight() + topPadding
						- 10, operatePanelWidth - leftPadding * 2, bH + 10);

		songSlider.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isStartTrackingTouch = true;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {

				final int x = e.getX();

				// // /* 获取鼠标的位置 */
				songSlider.setValue(songSlider.getMaximum() * x
						/ songSlider.getWidth());
				new Thread() {

					@Override
					public void run() {
						SongMessage songMessage = new SongMessage();
						songMessage.setType(SongMessage.SEEKTOMUSIC);
						songMessage.setProgress(songSlider.getValue());
						ObserverManage.getObserver().setMessage(songMessage);

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						isStartTrackingTouch = false;

					}

				}.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});

		songSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isStartTrackingTouch) {
					songProgressLabel.setText(MediaUtils
							.formatTime(((JSlider) e.getSource()).getValue()));
				}
			}
		});

		songProgressLabel = new JLabel();
		songProgressLabel.setForeground(Color.white);
		songProgressLabel.setBounds(leftPadding,
				songSlider.getY() + songSlider.getHeight() + topPadding - 10,
				60, bH + 5);
		songProgressLabel.setText(MediaUtils.formatTime(0));

		songSizeLabel = new JLabel();
		songSizeLabel.setForeground(Color.white);
		songSizeLabel.setBounds(operatePanelWidth - 60 - leftPadding / 2,
				songSlider.getY() + songSlider.getHeight() + topPadding - 10,
				60, bH + 5);
		songSizeLabel.setText(MediaUtils.formatTime(0));

		int buttonPanelHeight = operatePanelHeight - bH;
		int buttonPadding = 10;
		int buttonWidth = Constants.mainPanelWidth - 8 * buttonPadding;
		int seek = 10;
		int baseButtonSize = (buttonWidth - 4 * seek) / 7;
		int preOrNetButtonSize = baseButtonSize + seek;
		int playOrPauseButtonSize = preOrNetButtonSize + seek;

		if (playOrPauseButtonSize >= buttonPanelHeight) {
			playOrPauseButtonSize = buttonPanelHeight / 2;
			preOrNetButtonSize = playOrPauseButtonSize - seek;
			baseButtonSize = preOrNetButtonSize - seek;
			buttonWidth = baseButtonSize * 7 + 4 * seek;
			buttonPadding = (Constants.mainPanelWidth - buttonWidth) / 8;
		}

		// 桌面歌词按钮
		String unShowDesktopLyricsButtonBaseIconPath = iconPath
				+ "undeslrc_def.png";
		String unShowDesktopLyricsButtonOverIconPath = iconPath
				+ "undeslrc_hot.png";
		String unShowDesktopLyricsButtonPressedIconPath = iconPath
				+ "undeslrc_down.png";
		unShowDesktopLyricsButton = new BaseButton(
				unShowDesktopLyricsButtonBaseIconPath,
				unShowDesktopLyricsButtonOverIconPath,
				unShowDesktopLyricsButtonPressedIconPath, baseButtonSize,
				baseButtonSize);
		unShowDesktopLyricsButton.setBounds(buttonPadding, songSizeLabel.getY()
				+ songSizeLabel.getHeight()
				+ (buttonPanelHeight - baseButtonSize) / 2 - 2, baseButtonSize,
				baseButtonSize);
		unShowDesktopLyricsButton.setToolTipText("打开桌面歌词");
		unShowDesktopLyricsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Constants.showDesktopLyrics = true;
				initDesktopLrc();

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.OPENORCLOSEDESLRC);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		// 桌面歌词按钮
		String showDesktopLyricsButtonBaseIconPath = iconPath
				+ "deslrc_def.png";
		String showDesktopLyricsButtonOverIconPath = iconPath
				+ "deslrc_hot.png";
		String showDesktopLyricsButtonPressedIconPath = iconPath
				+ "deslrc_down.png";
		showDesktopLyricsButton = new BaseButton(
				showDesktopLyricsButtonBaseIconPath,
				showDesktopLyricsButtonOverIconPath,
				showDesktopLyricsButtonPressedIconPath, baseButtonSize,
				baseButtonSize);
		showDesktopLyricsButton.setBounds(buttonPadding, songSizeLabel.getY()
				+ songSizeLabel.getHeight()
				+ (buttonPanelHeight - baseButtonSize) / 2 - 2, baseButtonSize,
				baseButtonSize);
		showDesktopLyricsButton.setToolTipText("关闭桌面歌词");

		showDesktopLyricsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Constants.showDesktopLyrics = false;
				initDesktopLrc();

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.OPENORCLOSEDESLRC);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		initDesktopLrc();

		// 单曲播放

		String singleButtonBaseIconPath = iconPath + "01_1.png";
		String singleButtonOverIconPath = iconPath + "01_2.png";
		String singleButtonPressedIconPath = iconPath + "01_3.png";
		singleButton = new BaseButton(singleButtonBaseIconPath,
				singleButtonOverIconPath, singleButtonPressedIconPath,
				baseButtonSize, baseButtonSize);

		singleButton.setBounds(showDesktopLyricsButton.getX()
				+ showDesktopLyricsButton.getWidth() + buttonPadding,
				songSizeLabel.getY() + songSizeLabel.getHeight()
						+ (buttonPanelHeight - baseButtonSize) / 2,
				baseButtonSize, baseButtonSize);
		singleButton.setToolTipText("单曲播放");
		singleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
				playModel = 4;
				Constants.playModel = playModel;
				initPlayModel();
			}
		});

		// 单曲循环
		String singleRepeatButtonBaseIconPath = iconPath + "02_1.png";
		String singleRepeatButtonOverIconPath = iconPath + "02_2.png";
		String singleRepeatButtonPressedIconPath = iconPath + "02_3.png";
		singleRepeatButton = new BaseButton(singleRepeatButtonBaseIconPath,
				singleRepeatButtonOverIconPath,
				singleRepeatButtonPressedIconPath, baseButtonSize,
				baseButtonSize);

		singleRepeatButton.setBounds(showDesktopLyricsButton.getX()
				+ showDesktopLyricsButton.getWidth() + buttonPadding,
				songSizeLabel.getY() + songSizeLabel.getHeight()
						+ (buttonPanelHeight - baseButtonSize) / 2,
				baseButtonSize, baseButtonSize);
		singleRepeatButton.setToolTipText("单曲循环");
		singleRepeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
				playModel = 0;
				Constants.playModel = playModel;
				initPlayModel();
			}
		});

		// 顺序播放

		String sequenceButtonBaseIconPath = iconPath + "03_1.png";
		String sequenceButtonOverIconPath = iconPath + "03_2.png";
		String sequenceButtonPressedIconPath = iconPath + "03_3.png";
		sequenceButton = new BaseButton(sequenceButtonBaseIconPath,
				sequenceButtonOverIconPath, sequenceButtonPressedIconPath,
				baseButtonSize, baseButtonSize);

		sequenceButton.setBounds(showDesktopLyricsButton.getX()
				+ showDesktopLyricsButton.getWidth() + buttonPadding,
				songSizeLabel.getY() + songSizeLabel.getHeight()
						+ (buttonPanelHeight - baseButtonSize) / 2,
				baseButtonSize, baseButtonSize);
		sequenceButton.setToolTipText("顺序播放");

		sequenceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
				playModel = 1;
				Constants.playModel = playModel;
				initPlayModel();
			}
		});

		// 列表循环

		String listRepeatButtonBaseIconPath = iconPath + "04_1.png";
		String listRepeatButtonOverIconPath = iconPath + "04_2.png";
		String listRepeatButtonPressedIconPath = iconPath + "04_3.png";
		listRepeatButton = new BaseButton(listRepeatButtonBaseIconPath,
				listRepeatButtonOverIconPath, listRepeatButtonPressedIconPath,
				baseButtonSize, baseButtonSize);

		listRepeatButton.setBounds(showDesktopLyricsButton.getX()
				+ showDesktopLyricsButton.getWidth() + buttonPadding,
				songSizeLabel.getY() + songSizeLabel.getHeight()
						+ (buttonPanelHeight - baseButtonSize) / 2,
				baseButtonSize, baseButtonSize);
		listRepeatButton.setToolTipText("列表循环");
		listRepeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
				playModel = 3;
				Constants.playModel = playModel;
				initPlayModel();
			}
		});

		// 随机播放

		String randomButtonBaseIconPath = iconPath + "05_1.png";
		String randomButtonOverIconPath = iconPath + "05_2.png";
		String randomButtonPressedIconPath = iconPath + "05_3.png";
		randomButton = new BaseButton(randomButtonBaseIconPath,
				randomButtonOverIconPath, randomButtonPressedIconPath,
				baseButtonSize, baseButtonSize);

		randomButton.setBounds(showDesktopLyricsButton.getX()
				+ showDesktopLyricsButton.getWidth() + buttonPadding,
				songSizeLabel.getY() + songSizeLabel.getHeight()
						+ (buttonPanelHeight - baseButtonSize) / 2,
				baseButtonSize, baseButtonSize);
		randomButton.setToolTipText("随机播放");

		randomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
				playModel = 2;
				Constants.playModel = playModel;
				initPlayModel();
			}
		});
		// 初始化播放模式
		initPlayModel();

		// 上一首
		String preButtonBaseIconPath = iconPath + "pre_normal.png";
		String preButtonOverIconPath = iconPath + "pre_hot.png";
		String preButtonPressedIconPath = iconPath + "pre_down.png";

		preButton = new BaseButton(preButtonBaseIconPath,
				preButtonOverIconPath, preButtonPressedIconPath,
				preOrNetButtonSize, preOrNetButtonSize);

		int px = sequenceButton.getX() + sequenceButton.getWidth()
				+ buttonPadding;
		int py = songSizeLabel.getY() + songSizeLabel.getHeight()
				+ (buttonPanelHeight - preOrNetButtonSize) / 2;

		preButton.setBounds(px, py, preOrNetButtonSize, preOrNetButtonSize);
		preButton.setToolTipText("上一首");

		preButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PREMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 播放按钮

		String playButtonBaseIconPath = iconPath + "play_normal.png";
		String playButtonOverIconPath = iconPath + "play_hot.png";
		String playButtonPressedIconPath = iconPath + "play_down.png";

		playButton = new BaseButton(playButtonBaseIconPath,
				playButtonOverIconPath, playButtonPressedIconPath,
				playOrPauseButtonSize, playOrPauseButtonSize);

		int pxx = preButton.getX() + preButton.getWidth() + buttonPadding;
		int y = songSizeLabel.getY() + songSizeLabel.getHeight()
				+ (buttonPanelHeight - playOrPauseButtonSize) / 2;

		playButton.setBounds(pxx, y, playOrPauseButtonSize,
				playOrPauseButtonSize);
		playButton.setToolTipText("播放");

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PLAYMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 暂停按钮
		String pauseButtonBaseIconPath = iconPath + "pause_normal.png";
		String pauseButtonOverIconPath = iconPath + "pause_hot.png";
		String pauseButtonPressedIconPath = iconPath + "pause_down.png";

		pauseButton = new BaseButton(pauseButtonBaseIconPath,
				pauseButtonOverIconPath, pauseButtonPressedIconPath,
				playOrPauseButtonSize, playOrPauseButtonSize);

		pauseButton.setBounds(pxx, y, playOrPauseButtonSize,
				playOrPauseButtonSize);
		pauseButton.setToolTipText("暂停");
		pauseButton.setVisible(false);

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PAUSEMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 下一首

		String nextButtonBaseIconPath = iconPath + "next_normal.png";
		String nextButtonOverIconPath = iconPath + "next_hot.png";
		String nextButtonPressedIconPath = iconPath + "next_down.png";

		nextButton = new BaseButton(nextButtonBaseIconPath,
				nextButtonOverIconPath, nextButtonPressedIconPath,
				preOrNetButtonSize, preOrNetButtonSize);

		int nx = playButton.getX() + playButton.getWidth() + buttonPadding;

		nextButton.setBounds(nx, py, preOrNetButtonSize, preOrNetButtonSize);
		nextButton.setToolTipText("下一首");

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.NEXTMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		//
		int vx = nextButton.getX() + nextButton.getWidth() + buttonPadding / 2;
		int vy = songSizeLabel.getY() + songSizeLabel.getHeight()
				+ (buttonPanelHeight - baseButtonSize) / 2;
		volumeButton = new BaseButton(baseButtonSize, baseButtonSize);
		volumeButton.setBounds(vx, vy, baseButtonSize, baseButtonSize);
		volumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Constants.volumeSize != 0) {
					Constants.volumeSize = 0;
				} else {
					Constants.volumeSize = 10;
				}
				volumeSlider.setValue(Constants.volumeSize);
				initVolumeButtonUI(volumeButton.getWidth() / 3);
			}
		});
		initVolumeButtonUI(baseButtonSize / 3);

		//
		volumeSlider = new BaseSlider();
		volumeSlider.setOpaque(false); // slider的背景透明
		volumeSlider.setFocusable(false);
		volumeSlider.setValue(Constants.volumeSize);
		volumeSlider.setMaximum(100);

		int vsx = volumeButton.getX() + volumeButton.getWidth();
		int vsy = songSizeLabel.getY() + songSizeLabel.getHeight()
				+ (buttonPanelHeight - baseButtonSize + 10) / 2;

		volumeSlider.setBounds(vsx, vsy, baseButtonSize + buttonPadding
				+ buttonPadding / 2, baseButtonSize);

		volumeSlider.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				final int x = e.getX();
				// // /* 获取鼠标的位置 */
				volumeSlider.setValue(volumeSlider.getMaximum() * x
						/ volumeSlider.getWidth());
				//
				// //设置ui
				// Constants.volumeSize = volumeSlider.getValue();
				// initVolumeButtonUI(volumeButton.getWidth() / 3 * 2);

			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});

		volumeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Constants.volumeSize = volumeSlider.getValue();
				initVolumeButtonUI(volumeButton.getWidth() / 3);

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.PLAYERVOLUME);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		this.add(songNameLabel);
		this.add(songSlider);
		this.add(songProgressLabel);
		this.add(songSizeLabel);
		this.add(unShowDesktopLyricsButton);
		this.add(showDesktopLyricsButton);
		this.add(singleButton);
		this.add(singleRepeatButton);
		this.add(sequenceButton);
		this.add(listRepeatButton);
		this.add(randomButton);
		this.add(preButton);
		this.add(playButton);
		this.add(pauseButton);
		this.add(nextButton);
		this.add(volumeButton);
		this.add(volumeSlider);
	}

	/**
	 * 初始化声音按钮的图标
	 * 
	 * @param
	 * @param baseButtonSize
	 */
	private void initVolumeButtonUI(int baseButtonSize) {
		int volumeSize = Constants.volumeSize;
		String sound_normalPath = "Sound_normal_";
		if (volumeSize == 0) {
			sound_normalPath = sound_normalPath + "05.png";

		} else if (0 < volumeSize && volumeSize <= 25) {
			sound_normalPath = sound_normalPath + "01.png";

		} else if (20 < volumeSize && volumeSize <= 50) {
			sound_normalPath = sound_normalPath + "02.png";

		} else if (50 < volumeSize && volumeSize <= 75) {
			sound_normalPath = sound_normalPath + "03.png";

		} else if (75 < volumeSize && volumeSize <= 100) {
			sound_normalPath = sound_normalPath + "04.png";

		}

		ImageIcon icon = new ImageIcon(iconPath + sound_normalPath);
		icon.setImage(icon.getImage().getScaledInstance(baseButtonSize * 2,
				baseButtonSize * 2, Image.SCALE_SMOOTH));
		volumeButton.setIcon(icon);
	}

	/**
	 * 初始化桌面歌词窗口
	 */
	private void initDesktopLrc() {
		if (Constants.showDesktopLyrics) {
			unShowDesktopLyricsButton.setVisible(false);
			showDesktopLyricsButton.setVisible(true);
		} else {
			unShowDesktopLyricsButton.setVisible(true);
			showDesktopLyricsButton.setVisible(false);
		}
	}

	/**
	 * 初始化播放模式
	 */
	private void initPlayModel() {
		// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环
		switch (playModel) {
		case 0:
			sequenceButton.setVisible(true);
			randomButton.setVisible(false);
			listRepeatButton.setVisible(false);
			singleButton.setVisible(false);
			singleRepeatButton.setVisible(false);

			break;

		case 1:

			sequenceButton.setVisible(false);
			randomButton.setVisible(true);
			listRepeatButton.setVisible(false);
			singleButton.setVisible(false);
			singleRepeatButton.setVisible(false);

			break;

		case 2:

			sequenceButton.setVisible(false);
			randomButton.setVisible(false);
			listRepeatButton.setVisible(true);
			singleButton.setVisible(false);
			singleRepeatButton.setVisible(false);

			break;

		case 3:

			sequenceButton.setVisible(false);
			randomButton.setVisible(false);
			listRepeatButton.setVisible(false);
			singleButton.setVisible(true);
			singleRepeatButton.setVisible(false);

			break;
		case 4:

			sequenceButton.setVisible(false);
			randomButton.setVisible(false);
			listRepeatButton.setVisible(false);
			singleButton.setVisible(false);
			singleRepeatButton.setVisible(true);

			break;

		default:
			break;
		}

	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateUI(data);
			}
		});
	}

	/**
	 * 
	 * @param data
	 */
	protected void updateUI(Object data) {
		if (data instanceof SongMessage) {
			SongMessage songMessage = (SongMessage) data;
			if (songMessage.getType() == SongMessage.INITMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC
					|| songMessage.getType() == SongMessage.ERRORMUSIC
					|| songMessage.getType() == SongMessage.SERVICEERRORMUSIC) {
				refreshUI(songMessage);
			}
		} else if (data instanceof MessageIntent) {
			MessageIntent messageIntent = (MessageIntent) data;
			if (messageIntent.getAction().equals(MessageIntent.CLOSEDESLRC)) {
				initDesktopLrc();
			}
		}
	}

	/**
	 * 刷新ui
	 * 
	 * @param songMessage
	 */
	private void refreshUI(SongMessage songMessage) {

		SongInfo mSongInfo = songMessage.getSongInfo();
		if (mSongInfo != null) {
			if (songMessage.getType() == SongMessage.INITMUSIC) {

				if (MediaManage.PLAYING == MediaManage.getMediaManage()
						.getPlayStatus()) {
					playButton.setVisible(false);
					pauseButton.setVisible(true);

					desLrcDialog.getDesOperatePanel().getPlayButton()
							.setVisible(false);
					desLrcDialog.getDesOperatePanel().getPauseButton()
							.setVisible(true);
				} else {
					playButton.setVisible(true);
					pauseButton.setVisible(false);

					desLrcDialog.getDesOperatePanel().getPlayButton()
							.setVisible(true);
					desLrcDialog.getDesOperatePanel().getPauseButton()
							.setVisible(false);
				}

				songNameLabel.setText(mSongInfo.getDisplayName());
				songSlider.setMaximum((int) mSongInfo.getDuration());
				songSlider.setValue(0);
				songProgressLabel.setText(MediaUtils.formatTime(0));
				songSizeLabel.setText(MediaUtils.formatTime((int) mSongInfo
						.getDuration()));

			} else if (songMessage.getType() == SongMessage.SERVICEPLAYMUSIC) {
				playButton.setVisible(false);
				pauseButton.setVisible(true);

				desLrcDialog.getDesOperatePanel().getPlayButton()
						.setVisible(false);
				desLrcDialog.getDesOperatePanel().getPauseButton()
						.setVisible(true);

			} else if (songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC) {

				if (!isStartTrackingTouch) {
					songSlider.setValue((int) mSongInfo.getPlayProgress());
					songProgressLabel.setText(MediaUtils
							.formatTime((int) mSongInfo.getPlayProgress()));
				}

			} else if (songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC) {
				playButton.setVisible(true);
				pauseButton.setVisible(false);

				desLrcDialog.getDesOperatePanel().getPlayButton()
						.setVisible(true);
				desLrcDialog.getDesOperatePanel().getPauseButton()
						.setVisible(false);

				songSlider.setValue((int) mSongInfo.getPlayProgress());
				songProgressLabel.setText(MediaUtils.formatTime((int) mSongInfo
						.getPlayProgress()));

			}
		} else {
			songNameLabel.setText(Constants.APPTITLE);

			songSlider.setValue(0);
			songSlider.setMaximum(0);
			songProgressLabel.setText(MediaUtils.formatTime(0));
			songSizeLabel.setText(MediaUtils.formatTime(0));
			playButton.setVisible(true);
			pauseButton.setVisible(false);

			desLrcDialog.getDesOperatePanel().getPlayButton().setVisible(true);
			desLrcDialog.getDesOperatePanel().getPauseButton()
					.setVisible(false);

		}

	}
}
