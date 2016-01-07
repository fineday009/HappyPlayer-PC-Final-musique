package com.happy.widget.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.widget.button.DesOperateButton;

/**
 * 桌面操作面板
 * 
 * @author zhangliangming
 * 
 */
public class DesOperatePanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int mHeight = 0;

	private int mWidth = 0;
	/**
	 * 
	 */
	private int padding = 10;

	/**
	 * 播放按钮
	 */
	private DesOperateButton playButton;
	/**
	 * 暂停按钮
	 */
	private DesOperateButton pauseButton;
	/**
	 * 上一首
	 */
	private DesOperateButton preButton;
	/**
	 * 下一首
	 */
	private DesOperateButton nextButton;

	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;
	/**
	 * 判断是否进入
	 */
	private boolean isEnter = false;
	/**
	 * 桌面窗口事件
	 */
	private MouseInputListener desLrcDialogMouseListener;

	private MouseListener mouseListener = new MouseListener();

	public DesOperatePanel(int width, int height,
			MouseInputListener desLrcDialogMouseListener) {
		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.mWidth = width;
		this.mHeight = height;
		// 初始化组件
		initComponent();
		initLockEvent();
		ObserverManage.getObserver().addObserver(this);
	}

	private void initLockEvent() {
		if (!Constants.desLrcIsLock) {
			this.addMouseListener(mouseListener);
			this.addMouseMotionListener(mouseListener);
		} else {
			this.removeMouseListener(mouseListener);
			this.removeMouseMotionListener(mouseListener);
		}

	}

	private void initComponent() {
		this.setLayout(null);

		int buttonSize = mHeight;

		String liconPath = iconPath + "ic_launcher.png";
		// ImageIcon icon = new ImageIcon(liconPath);
		// icon.setImage(icon.getImage().getScaledInstance(buttonSize,
		// buttonSize,
		// Image.SCALE_SMOOTH));
		// JLabel iconLabel = new JLabel(icon);
		//
		// iconLabel.setBounds(0, 0, buttonSize, buttonSize);

		DesOperateButton iconButton = new DesOperateButton(liconPath,
				buttonSize, buttonSize, desLrcDialogMouseListener, this);
		iconButton.setBounds(0, 0, buttonSize, buttonSize);

		iconButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.FRAME_NORMAL);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		String lockPath = iconPath + "minilycic_lock.png";
		DesOperateButton lockButton = new DesOperateButton(lockPath,
				buttonSize, buttonSize, desLrcDialogMouseListener, this);
		lockButton.setBounds(iconButton.getX() + iconButton.getWidth()
				+ padding, 0, buttonSize, buttonSize);

		lockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!Constants.desLrcIsLock) {
					Constants.desLrcIsLock = true;
					MessageIntent messageIntent = new MessageIntent();
					messageIntent.setAction(MessageIntent.LOCKDESLRC);
					ObserverManage.getObserver().setMessage(messageIntent);
				}
			}
		});

		// 上一首
		String preButtonBaseIconPath = iconPath + "pre_normal1.png";
		String preButtonOverIconPath = iconPath + "pre_hot1.png";
		String preButtonPressedIconPath = iconPath + "pre_down1.png";

		preButton = new DesOperateButton(preButtonBaseIconPath,
				preButtonOverIconPath, preButtonPressedIconPath, buttonSize,
				buttonSize, desLrcDialogMouseListener, this);

		preButton.setBounds(
				lockButton.getX() + lockButton.getWidth() + padding, 0,
				buttonSize, buttonSize);
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

		String playButtonBaseIconPath = iconPath + "play_normal1.png";
		String playButtonOverIconPath = iconPath + "play_hot1.png";
		String playButtonPressedIconPath = iconPath + "play_down1.png";

		playButton = new DesOperateButton(playButtonBaseIconPath,
				playButtonOverIconPath, playButtonPressedIconPath, buttonSize,
				buttonSize, desLrcDialogMouseListener, this);

		playButton.setBounds(preButton.getX() + preButton.getWidth() + padding,
				0, buttonSize, buttonSize);
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
		String pauseButtonBaseIconPath = iconPath + "pause_normal1.png";
		String pauseButtonOverIconPath = iconPath + "pause_hot1.png";
		String pauseButtonPressedIconPath = iconPath + "pause_down1.png";

		pauseButton = new DesOperateButton(pauseButtonBaseIconPath,
				pauseButtonOverIconPath, pauseButtonPressedIconPath,
				buttonSize, buttonSize, desLrcDialogMouseListener, this);

		pauseButton.setBounds(
				preButton.getX() + preButton.getWidth() + padding, 0,
				buttonSize, buttonSize);
		pauseButton.setToolTipText("暂停");

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PAUSEMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		pauseButton.setVisible(false);

		// 下一首

		String nextButtonBaseIconPath = iconPath + "next_normal1.png";
		String nextButtonOverIconPath = iconPath + "next_hot1.png";
		String nextButtonPressedIconPath = iconPath + "next_down1.png";

		nextButton = new DesOperateButton(nextButtonBaseIconPath,
				nextButtonOverIconPath, nextButtonPressedIconPath, buttonSize,
				buttonSize, desLrcDialogMouseListener, this);

		nextButton.setBounds(pauseButton.getX() + pauseButton.getWidth()
				+ padding, 0, buttonSize, buttonSize);
		nextButton.setToolTipText("下一首");

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.NEXTMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 关闭按钮
		String closeButtonBaseIconPath = iconPath + "close_normal1.png";
		String closeButtonOverIconPath = iconPath + "close_hot1.png";
		String closeButtonPressedIconPath = iconPath + "close_hot1.png";

		DesOperateButton closeButton = new DesOperateButton(
				closeButtonBaseIconPath, closeButtonOverIconPath,
				closeButtonPressedIconPath, buttonSize, buttonSize,
				desLrcDialogMouseListener, this);
		closeButton.setBounds(nextButton.getX() + nextButton.getWidth()
				+ padding, 0, buttonSize, buttonSize);
		closeButton.setToolTipText("关闭");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Constants.showDesktopLyrics = false;

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.CLOSEDESLRC);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});
		this.add(lockButton);
		this.add(iconButton);
		this.add(preButton);
		this.add(playButton);
		this.add(pauseButton);
		this.add(nextButton);
		this.add(closeButton);

	}

	public boolean getEnter() {
		return isEnter;
	}

	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
		repaint();
	}

	public DesOperateButton getPlayButton() {
		return playButton;
	}

	public DesOperateButton getPauseButton() {
		return pauseButton;
	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			desLrcDialogMouseListener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			desLrcDialogMouseListener.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			desLrcDialogMouseListener.mouseReleased(e);
			// setCursor(null);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			isEnter = true;
			// repaint();

			desLrcDialogMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			isEnter = false;
			// repaint();
			desLrcDialogMouseListener.mouseExited(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// /* 鼠标左键托动事件 */
			// if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
			// setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			// }
			desLrcDialogMouseListener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			desLrcDialogMouseListener.mouseMoved(e);
		}

	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.LOCKDESLRC)) {
						initLockEvent();
					}
				}
			}
		});
	}
}
