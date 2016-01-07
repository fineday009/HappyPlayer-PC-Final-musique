package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.happy.common.Constants;
import com.happy.widget.button.BaseButton;

public class ScrollBarUI extends BasicScrollBarUI {
	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;

	private BaseButton decreaseButton;
	private BaseButton increaseButton;

	private int alpha = 255;

	public ScrollBarUI(int alpha) {
		this.alpha = alpha;
		init();
		String decreaseButtonBaseIconPath = iconPath
				+ "vscrollbtn1normalimage1.png";
		String decreaseButtonOverIconPath = iconPath
				+ "vscrollbtn1hotimage1.png";
		String decreaseButtonPressedIconPath = iconPath
				+ "vscrollbtn1pushedimage1.png";

		decreaseButton = new BaseButton(decreaseButtonBaseIconPath,
				decreaseButtonOverIconPath, decreaseButtonPressedIconPath, 12,
				6);
		decreaseButton.setPreferredSize(new Dimension(12, 16));

		String increaseButtonBaseIconPath = iconPath
				+ "vscrollbtn2normalimage1.png";
		String increaseButtonOverIconPath = iconPath
				+ "vscrollbtn2hotimage1.png";
		String increaseButtonPressedIconPath = iconPath
				+ "vscrollbtn2pushedimage1.png";

		increaseButton = new BaseButton(increaseButtonBaseIconPath,
				increaseButtonOverIconPath, increaseButtonPressedIconPath, 12,
				6);
		increaseButton.setPreferredSize(new Dimension(12, 16));
	}

	private void init() {
		// 滚动条默认颜色
		this.thumbColor = new Color(151, 148, 146, alpha);
		this.thumbDarkShadowColor = new Color(62, 62, 62, alpha);
		this.thumbLightShadowColor = new Color(240, 240, 240, alpha);
		this.thumbHighlightColor = new Color(240, 240, 240, alpha);
		this.trackColor = new Color(240, 240, 240, alpha);
		this.trackHighlightColor = new Color(240, 240, 240, alpha);
	}

	public Dimension getMaximumSize(JComponent c) {
		return new Dimension(12, 12);
	}

	public Dimension getPreferredSize(JComponent c) {
		return new Dimension(12, 12);
	}

	protected JButton createDecreaseButton(int orientation) {

		return decreaseButton;
	}

	protected JButton createIncreaseButton(int orientation) {
		return increaseButton;
	}

	protected Dimension getMaximumThumbSize() {
		Dimension di = super.getMaximumThumbSize();
		return new Dimension(di.width, di.height);
	}

	protected Dimension getMinimumThumbSize() {
		Dimension di = super.getMinimumThumbSize();
		return new Dimension(di.width, di.height);
	}

	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		init();
		Graphics2D g2d = (Graphics2D) g;
		int arc = (int) (thumbBounds.getWidth() < thumbBounds.getHeight() ? thumbBounds
				.getWidth() / 3 : thumbBounds.getHeight() / 3);
		if (this.isThumbRollover())
			g2d.setColor(thumbDarkShadowColor);
		else
			g2d.setColor(thumbColor);
		g2d.translate(thumbBounds.x, thumbBounds.y);
		g2d.fillRoundRect(0, 0, (int) (thumbBounds.getWidth() - 1),
				(int) (thumbBounds.getHeight() - 1), arc, arc);
	}

	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		init();
		// super.paintTrack(g, c, trackBounds);
		Graphics2D g2d = (Graphics2D) g;

		if (this.isThumbRollover())
			g2d.setColor(trackHighlightColor);
		else
			g2d.setColor(trackColor);
		// g2d.translate(trackBounds.x, trackBounds.y);
		g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width - 1,
				trackBounds.height);
	}

	public void paint(Graphics g, JComponent c) {
		paintTrack(g, c, getTrackBounds());
		Rectangle thumbBounds = getThumbBounds();
		if (thumbBounds.intersects(g.getClipBounds())) {
			paintThumb(g, c, thumbBounds);
		}
		// super.paint(g, c);
	}

	protected void paintDecreaseHighlight(Graphics g) {
		super.paintDecreaseHighlight(g);
	}

	protected void paintIncreaseHighlight(Graphics g) {
		super.paintIncreaseHighlight(g);
	}
}