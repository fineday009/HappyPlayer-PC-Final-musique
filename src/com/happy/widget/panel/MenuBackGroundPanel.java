package com.happy.widget.panel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

/**
 * 右上角菜单背景面板
 * 
 * @author zhangliangming
 * 
 */
public class MenuBackGroundPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	public MenuBackGroundPanel() {
		// this.setOpaque(false);
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
		RoundRectangle2D rect1 = new RoundRectangle2D.Double(0, -this.getHeight() * 2,
				this.getWidth() + this.getWidth()/3, this.getHeight() + this.getHeight() * 2, this.getWidth()/3, this.getHeight() * 2);// 创建//矩形对象
		g2d.fill(rect1);// 画出矩形

	}
}
