package com.happy.widget.panel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.util.SkinUtil;

/**
 * 皮肤列表面板
 * 
 * @author zhangliangming
 * 
 */
public class SkinListPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	private int width = 0;

	/**
	 * 皮肤数据
	 */
	private List<String> skinDatas;

	private int padding = 3;

	/**
	 * 皮肤面板
	 */
	private SkinPanel[] skinPanel;

	private int skinIndex = -1;

	public SkinListPanel(int width) {
		this.width = width;
		this.setLayout(new GridLayout(4, 3, padding, padding));
		loadData();
		this.setOpaque(false);
		ObserverManage.getObserver().addObserver(this);
	}

	private void loadData() {
		int skinWidth = (width - 2 * padding) / 3;
		int skinHeight = skinWidth + 5;
		skinDatas = SkinUtil.getAllSkinData();
		skinPanel = new SkinPanel[skinDatas.size()];
		for (int i = 0; i < skinDatas.size(); i++) {
			String fileName = skinDatas.get(i);
			if (fileName.equals(Constants.backGroundName)) {
				skinIndex = i;
			}
			skinPanel[i] = new SkinPanel(skinWidth, skinHeight, fileName);
			skinPanel[i].setPreferredSize(new Dimension(skinWidth, skinHeight));
			this.add(skinPanel[i]);
		}
	}

	@Override
	public void update(Observable o, Object data) {
		if (data instanceof MessageIntent) {
			MessageIntent messageIntent = (MessageIntent) data;
			if (messageIntent.getAction().equals(MessageIntent.UPDATE_SKIN)) {
				int newSkinIndex = getSkinIndex();
				if (newSkinIndex == -1)
					return;
				if (skinIndex != -1 && newSkinIndex != skinIndex) {
					skinPanel[skinIndex].setSelect(false);
				}
				skinPanel[newSkinIndex].setSelect(true);
				skinIndex = newSkinIndex;
			}
		}
	}

	private int getSkinIndex() {
		for (int i = 0; i < skinDatas.size(); i++) {
			String fileName = skinDatas.get(i);
			if (fileName.equals(Constants.backGroundName)) {
				return i;
			}
		}
		return -1;
	}
}
