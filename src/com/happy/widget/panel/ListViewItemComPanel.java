package com.happy.widget.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * 分类内容面板
 * 
 * @author zhangliangming
 * 
 */
public class ListViewItemComPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	public ListViewItemComPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
	}
}
