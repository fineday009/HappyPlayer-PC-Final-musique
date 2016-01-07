package com.happy.widget.panel;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.happy.manage.KscLyricsManage;
import com.happy.model.KscLyricsLineInfo;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.util.KscLyricsParserUtil;
import com.happy.util.KscUtil;
import com.happy.widget.dialog.DesLrcDialog;

/**
 * 歌词内容面板
 * 
 * @author zhangliangming
 * 
 */
public class MainLrcPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;
	/**
	 * 歌词面板
	 */
	private KscManyLineLyricsView kscManyLineLyricsView;

	/**
	 * 歌词解析
	 */
	private KscLyricsParserUtil kscLyricsParser;

	/**
	 * 歌词列表
	 */
	private TreeMap<Integer, KscLyricsLineInfo> lyricsLineTreeMap;

	/**
	 * 当前播放歌曲
	 */
	private SongInfo mSongInfo;
	/**
	 * 主菜单面板
	 */
	private JPanel mainPanel;

	private DesLrcDialog desktopLrcDialog;

	public MainLrcPanel(int width, int height, JPanel mainPanel,
			DesLrcDialog desktopLrcDialog) {
		this.desktopLrcDialog = desktopLrcDialog;
		this.mainPanel = mainPanel;
		this.setLayout(new BorderLayout());
		ObserverManage.getObserver().addObserver(this);
		kscManyLineLyricsView = new KscManyLineLyricsView(width, height);
		this.add(kscManyLineLyricsView, BorderLayout.CENTER);
		this.setOpaque(false);
	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateUI(data);
			}
		});
	}

	/**
	 * 刷新ui
	 * 
	 * @param data
	 */
	protected void updateUI(Object data) {

		if (data instanceof SongMessage) {
			SongMessage songMessage = (SongMessage) data;
			if (songMessage.getType() == SongMessage.INITMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC
					|| songMessage.getType() == SongMessage.ERRORMUSIC
					|| songMessage.getType() == SongMessage.SERVICEERRORMUSIC) {
				refreshUI(songMessage);
			} else if (songMessage.getType() == SongMessage.LRCKSCLOADED) {
				if (mSongInfo == null)
					return;
				if (!mSongInfo.getSid().equals(songMessage.getSid())) {
					return;
				}
				String kscFilePath = songMessage.getKscFilePath();
				String sid = songMessage.getSid();

				initKscLrc(sid, kscFilePath, mSongInfo.getDuration(), true);
			} else if (songMessage.getType() == SongMessage.LRCKSCDOWNLOADED) {
				if (mSongInfo == null)
					return;
				if (!mSongInfo.getSid().equals(songMessage.getSid())) {
					return;
				}
				String sid = songMessage.getSid();

				initKscLrc(sid, null, mSongInfo.getDuration(), false);

			}
		}
	}

	/**
	 * 刷新ui
	 * 
	 * @param mSongInfo
	 */
	protected void refreshUI(SongMessage songMessage) {
		SongInfo songInfo = songMessage.getSongInfo();
		if (songInfo != null) {
			if (songMessage.getType() == SongMessage.INITMUSIC) {

				this.mSongInfo = songInfo;

				KscUtil.loadKsc(mSongInfo.getSid(), mSongInfo.getTitle(),
						mSongInfo.getSinger(), mSongInfo.getDisplayName(),
						mSongInfo.getKscUrl(), SongMessage.KSCTYPELRC);

				kscManyLineLyricsView.setHasKsc(false);

				desktopLrcDialog.getFloatLyricsView().setHasKsc(false);

			} else if (songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC) {

				if (kscManyLineLyricsView.getHasKsc()
						&& !kscManyLineLyricsView.getBlScroll()) {

					kscManyLineLyricsView.showLrc((int) mSongInfo
							.getPlayProgress());
					// 歌词刷新时，刷新一下主面板，防止，歌词画出界
					mainPanel.repaint();
				}

				if (desktopLrcDialog.getFloatLyricsView().getHasKsc()) {
					desktopLrcDialog.getFloatLyricsView().showLrc(
							(int) mSongInfo.getPlayProgress());
				}

			} else if (songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC) {

				if (kscManyLineLyricsView.getHasKsc()
						&& !kscManyLineLyricsView.getBlScroll()) {

					kscManyLineLyricsView.showLrc((int) mSongInfo
							.getPlayProgress());
					// 歌词刷新时，刷新一下主面板，防止，歌词画出界
					mainPanel.repaint();
				}

				if (desktopLrcDialog.getFloatLyricsView().getHasKsc()) {
					desktopLrcDialog.getFloatLyricsView().showLrc(
							(int) mSongInfo.getPlayProgress());
				}

			}
		} else {
			if (kscManyLineLyricsView != null)
				kscManyLineLyricsView.setHasKsc(false);

			if (desktopLrcDialog.getFloatLyricsView() != null) {
				desktopLrcDialog.getFloatLyricsView().setHasKsc(false);
			}
		}
	}

	/**
	 * 
	 * @param sid
	 * @param kscFilePath
	 * @param duration
	 * @param isFile
	 */
	private void initKscLrc(final String sid, final String kscFilePath,
			final long duration, final boolean isFile) {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				if (isFile)
					kscLyricsParser = KscLyricsManage.getKscLyricsParser(sid,
							kscFilePath);
				else
					kscLyricsParser = KscLyricsManage
							.getKscLyricsParserByKscInputStream(sid);

				return null;
			}

			@Override
			protected void done() {
				lyricsLineTreeMap = kscLyricsParser.getLyricsLineTreeMap();
				if (lyricsLineTreeMap != null && lyricsLineTreeMap.size() != 0) {
					kscManyLineLyricsView.init((int) duration, kscLyricsParser);
					kscManyLineLyricsView.setHasKsc(true);

					if (mSongInfo != null) {
						kscManyLineLyricsView.showLrc((int) mSongInfo
								.getPlayProgress());
						// 歌词刷新时，刷新一下主面板，防止，歌词画出界
						mainPanel.repaint();
					}

					desktopLrcDialog.getFloatLyricsView().init(kscLyricsParser);
					desktopLrcDialog.getFloatLyricsView().setHasKsc(true);

					if (mSongInfo != null) {
						desktopLrcDialog.getFloatLyricsView().showLrc(
								(int) mSongInfo.getPlayProgress());
					}

				}
			}
		}.execute();
	}
}
