package com.happy.widget.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.util.DataUtil;
import com.happy.widget.button.BaseButton;

/**
 * 主菜单面板-最小按钮、最大按钮
 * 
 * @author zhangliangming
 * 
 */
public class MainMenuPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;

	/**
	 * 左右边距
	 */
	private int rightPad = 10;

	public MainMenuPanel() {
		initComponent();
		this.setOpaque(false);
		// this.setBackground(Color.red);
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		this.setLayout(null);

		int mainMenuPanelWidth = Constants.mainFrameWidth
				- Constants.mainPanelWidth;
		int mainMenuPanelHeight = Constants.mainPanelHeight / 4 / 3;

		// 关闭按钮
		String closeButtonBaseIconPath = iconPath + "close_normal.png";
		String closeButtonOverIconPath = iconPath + "close_hot.png";
		String closeButtonPressedIconPath = iconPath + "close_down.png";

		int buttonSize = mainMenuPanelHeight / 3 * 2;

		int buttonY = (mainMenuPanelHeight - buttonSize) / 2;

		BaseButton closeButton = new BaseButton(closeButtonBaseIconPath,
				closeButtonOverIconPath, closeButtonPressedIconPath,
				buttonSize, buttonSize);
		closeButton.setBounds(mainMenuPanelWidth - buttonSize - rightPad,
				buttonY, buttonSize, buttonSize);
		closeButton.setToolTipText("关闭");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 关闭窗口时，保存数据
				DataUtil.saveData();
				System.exit(0);
			}
		});

		// 最小化按钮
		String minButtonBaseIconPath = iconPath + "min_def.png";
		String minButtonOverIconPath = iconPath + "min_hot.png";
		String minButtonPressedIconPath = iconPath + "min_down.png";

		BaseButton minButton = new BaseButton(minButtonBaseIconPath,
				minButtonOverIconPath, minButtonPressedIconPath, buttonSize,
				buttonSize);
		minButton.setBounds(
				mainMenuPanelWidth - buttonSize - closeButton.getWidth()
						- rightPad, buttonY - 5, buttonSize, buttonSize);
		minButton.setToolTipText("最小化");
		minButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 最小化窗口
				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.FRAME_MIN);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		// 分隔线
		JLabel lineLabel = new JLabel("|");
		lineLabel.setForeground(new Color(255, 255, 255, 150));
		lineLabel.setBounds(minButton.getX() - 15, buttonY, 15, buttonSize);

		// 皮肤
		String skinButtonBaseIconPath = iconPath + "skin_normal.png";
		String skinButtonOverIconPath = iconPath + "skin_hot.png";
		String skinButtonPressedIconPath = iconPath + "skin_down.png";

		BaseButton skinButton = new BaseButton(skinButtonBaseIconPath,
				skinButtonOverIconPath, skinButtonPressedIconPath, buttonSize,
				buttonSize);
		skinButton.setBounds(lineLabel.getX() - lineLabel.getWidth()
				- buttonSize, buttonY, buttonSize, buttonSize);
		skinButton.setToolTipText("皮肤");
		skinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.OPEN_SKINDIALOG);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		MenuBackGroundPanel menuBackGroundPanel = new MenuBackGroundPanel();
		menuBackGroundPanel.setBounds(0, 0, mainMenuPanelWidth,
				mainMenuPanelHeight);

		this.add(minButton);
		this.add(closeButton);
		this.add(lineLabel);
		this.add(skinButton);
		this.add(menuBackGroundPanel);
	}
}
