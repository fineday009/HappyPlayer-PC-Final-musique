package com.happy.widget.panel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * 标题背景面板
 * 
 * @author zhangliangming
 * 
 */
public class TitleBackGroundPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	private int width = 0;
	private int height = 0;

	public TitleBackGroundPanel(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 消除线条锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		AlphaComposite alphaComposite = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.1f);
		g2d.setComposite(alphaComposite);// 透明度
		g2d.setPaint(new Color(0, 0, 0));
		g2d.fillRect(0, 0, width, height);

	}
}
