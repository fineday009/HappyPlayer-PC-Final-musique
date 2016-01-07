package com.happy.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.SwingUtilities;

import com.happy.common.Constants;
import com.happy.logger.LoggerManage;
import com.happy.model.Category;
import com.happy.model.EventIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.util.DataUtil;
import com.happy.util.MediaUtils;

/**
 * 播放器管理
 * 
 * @author Administrator
 * 
 */
public class MediaManage implements Observer {

	private static MediaManage _mediaManage;

	/**
	 * 播放列表
	 */
	private List<Category> mCategorys;

	/**
	 * 歌曲列表
	 */
	private List<SongInfo> songlist;
	/**
	 * 当前播放歌曲
	 */
	private SongInfo songInfo;

	/**
	 * 正在播放
	 */
	public static final int PLAYING = 0;
	/**
	 * 暂停
	 */
	public static final int PAUSE = 1;
	/**
	 * 快进
	 */
	public static final int SEEKTO = 2;
	/**
	 * 播放歌曲状态
	 */
	private int playStatus = PAUSE;

	/**
	 * 列表索引
	 */
	private int pindex = -1;
	/**
	 * 歌曲列表索引
	 */
	private int sindex = -1;

	private LoggerManage logger;

	public MediaManage() {
		init();
	}

	public static MediaManage getMediaManage() {
		if (_mediaManage == null) {
			_mediaManage = new MediaManage();
		}
		return _mediaManage;
	}

	private void init() {
		logger = LoggerManage.getZhangLogger();
		ObserverManage.getObserver().addObserver(this);
	}

	/**
	 * 初始化播放列表数据
	 */
	public void initPlayListData() {
		mCategorys = DataUtil.readPlayListData();
		if (mCategorys == null) {
			mCategorys = new ArrayList<Category>();
			Category mCategory = new Category("默认列表");
			songlist = new ArrayList<SongInfo>();
			// 加载默认歌曲
			String defFilePath = Constants.PATH_MP3 + File.separator
					+ "姚贝娜 - 可以不可以.mp3";
			File defFile = new File(defFilePath);
			if (defFile.exists()) {
				SongInfo songInfo = MediaUtils.getSongInfoByFile(defFilePath);
				if (songInfo != null) {
					songlist.add(songInfo);
				}
			}
			mCategory.setmCategoryItem(songlist);
			mCategorys.add(mCategory);
		} else {
			songlist = new ArrayList<SongInfo>();
			// 在生成界面的时候会遍历列表，这里面就不做任何操作了
		}
	}

	/**
	 * 通过播放列表的索引，更新歌曲列表数据
	 * 
	 * @param index
	 */
	public void setSongListData(int index, SongInfo msongInfo) {
		if (mCategorys == null || index >= mCategorys.size()) {
			return;
		}
		Category category = mCategorys.get(index);
		songlist = category.getmCategoryItem();
		this.songInfo = msongInfo;
	}

	/**
	 * 更新歌曲数据
	 * 
	 * @param index
	 *            索引
	 * @param songlist
	 *            更新的歌曲列表
	 */
	public void upDateSongListData(int index, List<SongInfo> songlist) {
		if (mCategorys == null || index >= mCategorys.size())
			return;
		Category category = mCategorys.get(index);
		category.setmCategoryItem(songlist);
		mCategorys.remove(index);
		mCategorys.add(index, category);
	}

	/**
	 * 根据播放列表的索引更新当前的歌曲列表数据
	 * 
	 * @param pIndex
	 */
	public void upDateSongListData(int pIndex) {
		if (mCategorys == null || pIndex >= mCategorys.size())
			return;
		Category category = mCategorys.get(pIndex);
		songlist = category.getmCategoryItem();
	}

	public List<Category> getmCategorys() {
		return mCategorys;
	}

	public void setmCategorys(List<Category> mCategorys) {
		this.mCategorys = mCategorys;
	}

	public int getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(int playStatus) {
		this.playStatus = playStatus;
	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				if (data instanceof SongMessage) {
					SongMessage songMessage = (SongMessage) data;
					if (songMessage.getType() == SongMessage.PLAYMUSIC) {
						playMusic();
					} else if (songMessage.getType() == SongMessage.SEEKTOMUSIC) {
						int progress = songMessage.getProgress();
						seekTo(progress);
					} else if (songMessage.getType() == SongMessage.PLAYINFOMUSIC) {
						pauseMusic();
						playInfoMusic(songMessage.getSongInfo(), true);
					} else if (songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC) {
						if (songInfo != null
								&& songMessage.getSongInfo() != null) {
							// 相同，则更新进度
							if (songInfo.getSid().equals(
									songMessage.getSongInfo().getSid())) {
								songInfo = songMessage.getSongInfo();
							}
						}
					} else if (songMessage.getType() == SongMessage.PAUSEMUSIC) {
						pauseMusic();
					} else if (songMessage.getType() == SongMessage.NEXTMUSIC) {
						if (songInfo == null) {
							return;
						}
						stopToPlay();
						nextMusic(Constants.playModel);
					} else if (songMessage.getType() == SongMessage.PREMUSIC) {
						if (songInfo == null) {
							return;
						}
						stopToPlay();
						preMusic(Constants.playModel);
					}
				}
			}
		});
	}

	/**
	 * 播放歌曲
	 */
	private void playMusic() {
		if (songInfo != null) {
			playInfoMusic(songInfo, false);
		} else {
			SongMessage msg = new SongMessage();
			msg.setType(SongMessage.ERRORMUSIC);
			msg.setErrorMessage(SongMessage.ERRORMESSAGESONGNULL);
			ObserverManage.getObserver().setMessage(msg);
		}
	}

	/**
	 * 播放歌曲
	 * 
	 * @param songInfo
	 */
	private void playInfoMusic(SongInfo mSongInfo, boolean isInit) {

		this.songInfo = mSongInfo;

		if (isInit) {

			songInfo.setPlayProgress(0);
			SongMessage msg = new SongMessage();
			msg.setSongInfo(songInfo);
			msg.setType(SongMessage.INITMUSIC);
			ObserverManage.getObserver().setMessage(msg);
		}

		new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				playStatus = PLAYING;

				SongMessage msg2 = new SongMessage();
				msg2.setType(SongMessage.SERVICEPLAYMUSIC);
				msg2.setSongInfo(songInfo);
				ObserverManage.getObserver().setMessage(msg2);
			}

		}.start();

	}

	/**
	 * 快进歌曲
	 * 
	 * @param progress
	 */
	private void seekTo(int progress) {
		if (songInfo == null) {
			return;
		}

		// 快进的话，要修改当前的播放状态
		if (playStatus == PLAYING) {
			playStatus = SEEKTO;
			//
			SongMessage songMessage = new SongMessage();
			songMessage.setType(SongMessage.SERVICESEEKTOMUSIC);
			songMessage.setProgress(progress);
			ObserverManage.getObserver().setMessage(songMessage);
		} else {
			songInfo.setPlayProgress(progress);
			SongMessage songMessage = new SongMessage();
			songMessage.setType(SongMessage.SERVICEPAUSEEDMUSIC);
			songMessage.setSongInfo(songInfo);
			ObserverManage.getObserver().setMessage(songMessage);
		}
	}

	/**
	 * 暂停歌曲
	 */
	private void pauseMusic() {
		if (songInfo != null) {

			playStatus = PAUSE;

			SongMessage msg = new SongMessage();
			msg.setSongInfo(songInfo);
			msg.setType(SongMessage.SERVICEPAUSEMUSIC);
			ObserverManage.getObserver().setMessage(msg);

		} else {
			SongMessage msg = new SongMessage();
			msg.setType(SongMessage.ERRORMUSIC);
			msg.setErrorMessage(SongMessage.ERRORMESSAGESONGNULL);
			ObserverManage.getObserver().setMessage(msg);
		}
	}

	/**
	 * 播放下一首歌曲
	 * 
	 * @param playModel
	 *            播放模式
	 */
	private void nextMusic(int playModel) {
		// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环

		// 先判断当前的播放模式，再根据播放模式来获取下一首歌曲的索引

		boolean isInit = true;

		// 顺序播放
		int playIndex = sindex;
		switch (playModel) {
		case 0:

			playIndex++;
			if (playIndex >= songlist.size()) {
				stopToPlay();
				return;
			}
			if (songlist.size() != 0) {
				songInfo = songlist.get(playIndex);
			} else {
				stopToPlay();
				return;
			}

			break;
		case 1:
			// 随机播放

			playIndex = new Random().nextInt(songlist.size());
			if (songlist.size() != 0) {
				songInfo = songlist.get(playIndex);
			} else {
				stopToPlay();
				return;
			}
			break;
		case 2:
			// 循环播放

			playIndex++;
			if (playIndex >= songlist.size()) {
				playIndex = 0;
			}

			if (songlist.size() != 0) {
				songInfo = songlist.get(playIndex);
			} else {
				stopToPlay();
				return;
			}

			break;
		case 3:
			// 单曲播放
			stopToPlay();
			return;
		case 4:
			// 单曲循环播放
			break;
		}
		// playIndex == -1说明在单曲播放状态下歌曲被移除了
		if (songInfo == null || songInfo.getSid() == null || playIndex == -1) {
			stopToPlay();
			return;
		}

		// 保存歌曲索引
		Constants.playInfoID = songInfo.getSid();

		EventIntent eventIntent = new EventIntent();
		eventIntent.setType(EventIntent.SONGLIST);
		eventIntent.setpIndex(pindex);
		eventIntent.setsIndex(playIndex);
		eventIntent.setClickCount(EventIntent.DOUBLECLICK);

		ObserverManage.getObserver().setMessage(eventIntent);
		sindex = playIndex;

		playInfoMusic(songInfo, isInit);
	}

	/**
	 * 停止当前播放
	 */
	private void stopToPlay() {
		playStatus = PAUSE;
		// 结束播放保存歌曲索引

		Constants.playInfoID = "";

		EventIntent eventIntent = new EventIntent();
		eventIntent.setType(EventIntent.SONGLIST);
		eventIntent.setpIndex(pindex);
		eventIntent.setsIndex(-1);
		eventIntent.setClickCount(EventIntent.DOUBLECLICK);
		ObserverManage.getObserver().setMessage(eventIntent);

		songInfo = null;

		// 结束播放

		SongMessage msg = new SongMessage();
		msg.setSongInfo(null);
		msg.setType(SongMessage.INITMUSIC);
		ObserverManage.getObserver().setMessage(msg);
	}

	/**
	 * 上一首
	 * 
	 * @param playModel
	 */
	private void preMusic(int playModel) {
		// 0是 顺序播放 1是随机播放 2是循环播放 3是单曲播放 4单曲循环

		// 先判断当前的播放模式，再根据播放模式来获取上一首歌曲的索引

		boolean isInit = true;

		int playIndex = sindex;
		switch (playModel) {
		case 0:
			// 顺序播放
			playIndex--;
			if (playIndex < 0) {
				stopToPlay();
				return;
			}
			if (songlist.size() != 0) {
				songInfo = songlist.get(playIndex);
			} else {
				stopToPlay();
				return;
			}

			break;
		case 1:
			// 随机播放

			playIndex = new Random().nextInt(songlist.size());

			if (songlist.size() != 0) {
				songInfo = songlist.get(playIndex);
			} else {
				stopToPlay();
				return;
			}
		case 2:
			// 循环播放

			playIndex--;
			if (playIndex < 0) {
				playIndex = 0;
			}

			if (songlist.size() != 0) {
				songInfo = songlist.get(playIndex);
			} else {
				stopToPlay();
				return;
			}
		case 3:
			// 单曲播放
			stopToPlay();
			return;
		case 4:
			// 单曲循环播放

			break;
		}

		// playIndex == -1说明在单曲播放状态下歌曲被移除了
		if (songInfo == null || songInfo.getSid() == null || playIndex == -1) {
			stopToPlay();
			return;
		}
		// 保存歌曲索引

		Constants.playInfoID = songInfo.getSid();

		EventIntent eventIntent = new EventIntent();
		eventIntent.setType(EventIntent.SONGLIST);
		eventIntent.setpIndex(pindex);
		eventIntent.setsIndex(playIndex);
		eventIntent.setClickCount(EventIntent.DOUBLECLICK);

		ObserverManage.getObserver().setMessage(eventIntent);
		sindex = playIndex;

		playInfoMusic(songInfo, isInit);
	}

	public int getPindex() {
		return pindex;
	}

	public void setPindex(int pindex) {
		this.pindex = pindex;
	}

	public int getSindex() {
		return sindex;
	}

	public void setSindex(int sindex) {
		this.sindex = sindex;
	}

	public SongInfo getSongInfo() {
		return songInfo;
	}

}
