package com.happy.widget.button;

import java.awt.Cursor;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BaseButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 @param baseIconPath
	 *            默认图片
	 * @param overIconPath
	 *            鼠标经过图片
	 * @param pressedIconPath
	 *            点击图片
	 * @param width
	 * @param height
	 */
	public BaseButton(String baseIconPath, String overIconPath,
			String pressedIconPath, int width, int height) {

		ImageIcon icon = new ImageIcon(baseIconPath);
		icon.setImage(icon.getImage().getScaledInstance(width, height,
				Image.SCALE_SMOOTH));
		this.setIcon(icon);

		ImageIcon rolloverIcon = new ImageIcon(overIconPath);
		rolloverIcon.setImage(rolloverIcon.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		this.setRolloverIcon(rolloverIcon);

		ImageIcon pressedIcon = new ImageIcon(pressedIconPath);
		pressedIcon.setImage(pressedIcon.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		this.setPressedIcon(pressedIcon);

		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setDoubleBuffered(false);
		this.setOpaque(false);
		this.setFocusable(false);
		// 设置鼠标的图标
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public BaseButton(int width, int height) {

		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setDoubleBuffered(false);
		this.setOpaque(false);
		this.setFocusable(false);
		// 设置鼠标的图标
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
}
