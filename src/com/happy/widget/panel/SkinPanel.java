package com.happy.widget.panel;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;

/**
 * 皮肤面板
 * 
 * @author zhangliangming
 * 
 */
public class SkinPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	private int width = 0;
	private int height = 0;

	private String filePath;

	private JLabel jstatusLabel;

	/**
	 * 文件名
	 */
	private String fileName;

	private boolean isSelect = false;

	public SkinPanel(int width, int height, String mfileName) {
		if (Constants.backGroundName.equals(mfileName)) {
			isSelect = true;
		}
		this.fileName = mfileName;
		this.filePath = Constants.PATH_BACKGROUND + File.separator + mfileName;
		this.width = width;
		this.height = height;
		// 初始化组件
		initComponent();
		this.setOpaque(false);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				Constants.backGroundName = fileName;

				new Thread() {

					@Override
					public void run() {
						MessageIntent messageIntent = new MessageIntent();
						messageIntent.setAction(MessageIntent.UPDATE_SKIN);
						ObserverManage.getObserver().setMessage(messageIntent);
					}

				}.start();

			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
	}

	private void initComponent() {
		this.setLayout(null);
		JLabel jb = new JLabel(getBackgroundImageIcon(width, height, filePath));
		jb.setBounds(0, 0, width, height);

		int statusSize = width / 3;
		String statusIconFilePath = Constants.PATH_ICON + File.separator
				+ "skin_selected_bg_tip.png";
		jstatusLabel = new JLabel(getBackgroundImageIcon(statusSize,
				statusSize, statusIconFilePath));

		int x = (width - statusSize) / 2;
		int y = (height - statusSize) / 2;

		jstatusLabel.setBounds(x, y, statusSize, statusSize);

		if (isSelect) {
			jstatusLabel.setVisible(true);
		} else {
			jstatusLabel.setVisible(false);
		}
		this.add(jstatusLabel);
		this.add(jb);
	}

	public void setSelect(boolean isSelect) {
		if (isSelect) {
			jstatusLabel.setVisible(true);
		} else {
			jstatusLabel.setVisible(false);
		}
		repaint();
	}

	/**
	 * 获取背景图片
	 * 
	 * @return
	 */
	private ImageIcon getBackgroundImageIcon(int width, int height, String path) {
		ImageIcon background = new ImageIcon(path);// 背景图片
		background.setImage(background.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		return background;
	}

}
