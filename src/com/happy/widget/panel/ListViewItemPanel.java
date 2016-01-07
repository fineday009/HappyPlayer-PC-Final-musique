package com.happy.widget.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * 播放列表item面板
 * 
 * @author zhangliangming
 * 
 */
public class ListViewItemPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	public ListViewItemPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
	}
}
