package com.happy.widget.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.model.Category;
import com.happy.model.EventIntent;
import com.happy.model.MessageIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;

/**
 * 播放列表面板
 * 
 * @author zhangliangming
 * 
 */
public class PlayListPanel extends JPanel implements Observer {

	/**
	 * 滚动面板
	 */
	private JScrollPane jScrollPane;
	/**
	 * listview面板
	 */
	private JPanel listViewPanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	/**
	 * 播放列表
	 */
	private List<Category> mCategorys;

	private int width = 0;

	public PlayListPanel(int width) {
		this.width = width;
		initComponent();
		this.setOpaque(false);
		ObserverManage.getObserver().addObserver(this);
	}

	private void initComponent() {
		listViewPanel = new JPanel();
		listViewPanel.setOpaque(false);

		jScrollPane = new JScrollPane(listViewPanel);
		jScrollPane.setBorder(null);

		jScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(100));
		jScrollPane.getVerticalScrollBar().setUnitIncrement(30);
		// 不显示水平的滚动条
		jScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setOpaque(false);
		// jScrollPane.getViewport().setOpaque(false);
		jScrollPane.getViewport().setBackground(
				new Color(255, 255, 255, Constants.listViewAlpha));

		this.setLayout(new BorderLayout());
		this.add(jScrollPane, "Center");

		//
		listViewPanel.setLayout(new BoxLayout(listViewPanel, BoxLayout.Y_AXIS));

		loadData();

	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				// 遍历数据，生成界面
				mCategorys = MediaManage.getMediaManage().getmCategorys();
				return null;

			}

			@Override
			protected void done() {
				initComponentData();
			}
		}.execute();
	}

	/**
	 * 初始化组件数据
	 */
	protected void initComponentData() {

		for (int i = 0; i < mCategorys.size(); i++) {
			Category categorys = mCategorys.get(i);
			String titleName = categorys.getmCategoryName();
			int size = categorys.getmCategoryItemCount();

			ListViewItemComPanel listViewItemComPanel = new ListViewItemComPanel();
			listViewItemComPanel.setVisible(false);

			List<SongInfo> mSongInfo = categorys.getmCategoryItem();

			ListViewItemHeadPanel listViewItemHeadPanel = new ListViewItemHeadPanel(
					this, listViewPanel, width, titleName, size,
					listViewItemComPanel, mSongInfo, i);
			for (int j = 0; j < mSongInfo.size(); j++) {
				SongInfo songInfo = mSongInfo.get(j);
				refreshListViewItemComPanelUI(i, j, listViewItemComPanel,
						songInfo);

				if (songInfo.getSid().equals(Constants.playInfoID)) {

					// 设置当前播放歌曲所在的播放列表索引
					Constants.sDoubleClickIndex = j;
					Constants.pDoubleClickIndex = i;
					Constants.pShowIndex = i;

					// 设置当前播放歌曲列表的数据
					MediaManage.getMediaManage().setSongListData(i, songInfo);
					// 设置播放时的索引
					MediaManage.getMediaManage().setPindex(i);
					MediaManage.getMediaManage().setSindex(j);

					// 显示当前播放列表的数据
					listViewItemHeadPanel.setShow(true);

					// 获取当前播放歌曲数据，通知其它界面更新ui

					SongMessage msg = new SongMessage();
					msg.setSongInfo(songInfo);
					msg.setType(SongMessage.INITMUSIC);
					ObserverManage.getObserver().setMessage(msg);

				}
			}

			// listviewitem面板
			ListViewItemPanel itemPanel = new ListViewItemPanel();
			itemPanel.add(listViewItemHeadPanel, 0);
			itemPanel.add(listViewItemComPanel, 1);

			listViewPanel.add(itemPanel);

		}

		// 没有播放的歌曲
		if (Constants.pDoubleClickIndex == -1) {
			SongMessage msg = new SongMessage();
			msg.setSongInfo(null);
			msg.setType(SongMessage.INITMUSIC);
			ObserverManage.getObserver().setMessage(msg);
		}
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @param listViewItemComPanel
	 * @param songInfo
	 */
	private void refreshListViewItemComPanelUI(int pindex, int sindex,
			ListViewItemComPanel listViewItemComPanel, SongInfo songInfo) {
		ListViewItemComItemPanel listViewItemComItemPanel = new ListViewItemComItemPanel(
				this, pindex, sindex, songInfo, width);
		listViewItemComPanel.add(listViewItemComItemPanel);
	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				if (data instanceof EventIntent) {
					EventIntent eventIntent = (EventIntent) data;
					// 播放列表点击
					if (eventIntent.getType() == EventIntent.PLAYLIST) {
						refreshPlayListPanelUI(eventIntent);
					} else if (eventIntent.getType() == EventIntent.SONGLIST) {
						refreshListViewItemComItemPanelUI(eventIntent);
					}
				} else if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.UPDATE_LISTVIEW_ALPHA)) {

						jScrollPane.getViewport().setBackground(
								new Color(255, 255, 255,
										Constants.listViewAlpha));
					}
				}
			}
		});
	}

	/**
	 * 刷新歌曲id
	 * 
	 * @param eventIntent
	 */
	private void refreshListViewItemComItemPanelUI(EventIntent eventIntent) {
		if (eventIntent.getClickCount() == EventIntent.SINGLECLICK) {
			// 单击
			refreshListViewItemComItemSingleClickPanelUI(eventIntent);
		} else {
			// 双击
			refreshListViewItemComItemDoubleClickPanelUI(eventIntent);
		}
	}

	/**
	 * 单击刷新歌曲列表
	 * 
	 * @param eventIntent
	 */
	private void refreshListViewItemComItemSingleClickPanelUI(
			EventIntent eventIntent) {
		// 刷新新的面板ui
		int newPSingleClickIndex = eventIntent.getpIndex();
		if (newPSingleClickIndex == -1
				|| newPSingleClickIndex >= listViewPanel.getComponentCount()) {
			return;
		}
		int newsSingleClickIndex = eventIntent.getsIndex();
		int oldsSingleClickIndex = Constants.sSingleClickIndex;
		int oldPSingleClickIndex = Constants.pSingleClickIndex;

		if (oldsSingleClickIndex == newsSingleClickIndex
				&& oldPSingleClickIndex == newPSingleClickIndex)
			return;

		ListViewItemPanel newItemPanel = (ListViewItemPanel) listViewPanel
				.getComponent(newPSingleClickIndex);

		ListViewItemComPanel newlistViewItemComPanel = (ListViewItemComPanel) newItemPanel
				.getComponent(1);
		if (newsSingleClickIndex != -1
				&& newsSingleClickIndex < newlistViewItemComPanel
						.getComponentCount()) {

			ListViewItemComItemPanel newistViewItemComItemPanel = (ListViewItemComItemPanel) newlistViewItemComPanel
					.getComponent(newsSingleClickIndex);
			newistViewItemComItemPanel.setSingleSelect(true);
		}
		Constants.sSingleClickIndex = newsSingleClickIndex;
		Constants.pSingleClickIndex = newPSingleClickIndex;

		// 刷新旧的面板
		if (oldPSingleClickIndex == -1
				|| oldPSingleClickIndex >= listViewPanel.getComponentCount()) {
			return;
		}

		ListViewItemPanel oldItemPanel = (ListViewItemPanel) listViewPanel
				.getComponent(oldPSingleClickIndex);

		ListViewItemComPanel oldlistViewItemComPanel = (ListViewItemComPanel) oldItemPanel
				.getComponent(1);
		if (oldsSingleClickIndex != -1
				&& oldsSingleClickIndex < oldlistViewItemComPanel
						.getComponentCount()) {

			ListViewItemComItemPanel oldistViewItemComItemPanel = (ListViewItemComItemPanel) oldlistViewItemComPanel
					.getComponent(oldsSingleClickIndex);
			oldistViewItemComItemPanel.setSingleSelect(false);
		}

	}

	/**
	 * 双击刷新歌曲列表
	 * 
	 * @param eventIntent
	 */
	private void refreshListViewItemComItemDoubleClickPanelUI(
			EventIntent eventIntent) {
		// 刷新新的面板ui
		int newPDoubleClickIndex = eventIntent.getpIndex();
		if (newPDoubleClickIndex == -1
				|| newPDoubleClickIndex >= listViewPanel.getComponentCount()) {
			return;
		}
		int newsDoubleClickIndex = eventIntent.getsIndex();
		int oldsDoubleClickIndex = Constants.sDoubleClickIndex;
		int oldPDoubleClickIndex = Constants.pDoubleClickIndex;

		if (oldsDoubleClickIndex == newsDoubleClickIndex
				&& oldPDoubleClickIndex == newPDoubleClickIndex) {
			return;
		}

		ListViewItemPanel newItemPanel = (ListViewItemPanel) listViewPanel
				.getComponent(newPDoubleClickIndex);

		ListViewItemComPanel newlistViewItemComPanel = (ListViewItemComPanel) newItemPanel
				.getComponent(1);
		if (newsDoubleClickIndex != -1
				&& newsDoubleClickIndex < newlistViewItemComPanel
						.getComponentCount()) {

			ListViewItemComItemPanel newistViewItemComItemPanel = (ListViewItemComItemPanel) newlistViewItemComPanel
					.getComponent(newsDoubleClickIndex);
			newistViewItemComItemPanel.setDoubSelect(true);

		}
		Constants.sDoubleClickIndex = newsDoubleClickIndex;
		Constants.pDoubleClickIndex = newPDoubleClickIndex;

		// 刷新旧的面板
		if (oldPDoubleClickIndex == -1
				|| oldPDoubleClickIndex >= listViewPanel.getComponentCount()) {
			return;
		}

		ListViewItemPanel oldItemPanel = (ListViewItemPanel) listViewPanel
				.getComponent(oldPDoubleClickIndex);

		ListViewItemComPanel oldlistViewItemComPanel = (ListViewItemComPanel) oldItemPanel
				.getComponent(1);
		if (oldsDoubleClickIndex != -1
				&& oldsDoubleClickIndex < oldlistViewItemComPanel
						.getComponentCount()) {

			ListViewItemComItemPanel oldistViewItemComItemPanel = (ListViewItemComItemPanel) oldlistViewItemComPanel
					.getComponent(oldsDoubleClickIndex);
			oldistViewItemComItemPanel.setDoubSelect(false);
		}
	}

	/**
	 * 更新列表ui，根据点击的状态，判断是否打开展开列表
	 * 
	 * @param eventIntent
	 */
	private void refreshPlayListPanelUI(EventIntent eventIntent) {

		// 新的展开播放列表索引
		int newShowPIndex = eventIntent.getpShowIndex();

		if (newShowPIndex != -1
				&& newShowPIndex < listViewPanel.getComponentCount()) {
			//
			ListViewItemPanel newItemPanel = (ListViewItemPanel) listViewPanel
					.getComponent(newShowPIndex);
			ListViewItemHeadPanel newListViewItemHeadPanel = (ListViewItemHeadPanel) newItemPanel
					.getComponent(0);
			boolean isShow = eventIntent.getShow();
			if (isShow) {
				newListViewItemHeadPanel.setShow(false);
			} else {
				newListViewItemHeadPanel.setShow(true);
			}
		}

		// 旧的展开播放列表索引
		int oldShowPIndex = Constants.pShowIndex;
		if (oldShowPIndex != -1
				&& oldShowPIndex < listViewPanel.getComponentCount()
				&& oldShowPIndex != newShowPIndex) {
			//
			ListViewItemPanel oldItemPanel = (ListViewItemPanel) listViewPanel
					.getComponent(oldShowPIndex);
			ListViewItemHeadPanel oldListViewItemHeadPanel = (ListViewItemHeadPanel) oldItemPanel
					.getComponent(0);
			oldListViewItemHeadPanel.setShow(false);
		}
		Constants.pShowIndex = newShowPIndex;
	}
}
