package com.happy.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.happy.common.Constants;

public class SplashFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 背景图片
	 */
	private JLabel bgJLabel;
	private String title;// 标题

	public SplashFrame() {
		init();
		// 初始化组件
		initComponent();
	}

	private void init() {
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				System.exit(0);
			}
		});

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		title = Constants.APPTITLE;
		this.setTitle(title);
		this.setUndecorated(true);
		// // this.setAlwaysOnTop(true);
		// 读取数据失败，设置基本的窗口数据
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenDimension.width / 9 * 3,
				screenDimension.height / 6 * 3);
		// this.setLocation(Constants.mainFramelocaltionX,
		// Constants.mainFramelocaltionY);
		this.setLocationRelativeTo(null);
		// 状态栏图标
		String iconNamePath = Constants.PATH_ICON + File.separator
				+ Constants.iconName;
		this.setIconImage(new ImageIcon(iconNamePath).getImage());
	}

	private void initComponent() {
		this.getContentPane().setLayout(null);
		bgJLabel = new JLabel(getBackgroundImageIcon());// 把背景图片显示在一个标签里面
		// 把标签的大小位置设置为图片刚好填充整个面板
		bgJLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.getContentPane().add(bgJLabel);
	}

	/**
	 * 获取背景图片
	 * 
	 * @return
	 */
	private ImageIcon getBackgroundImageIcon() {
		String backgroundPath = Constants.PATH_SPLASH + File.separator
				+ "splash_bg.jpg";
		ImageIcon background = new ImageIcon(backgroundPath);// 背景图片
		background.setImage(background.getImage().getScaledInstance(
				this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		return background;
	}
}
