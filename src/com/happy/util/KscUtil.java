package com.happy.util;

import java.io.File;

import javax.swing.SwingWorker;

import com.happy.common.Constants;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;

/**
 * 歌词处理类
 * 
 * @author zhangliangming
 * 
 */
public class KscUtil {

	/**
	 * 加载ksc歌词文件
	 * 
	 * @param context
	 * @param sid
	 * @param title
	 * @param singer
	 * @param kscUrl
	 */
	public static void loadKsc(final String sid, String title, String singer,
			final String displayName, String kscUrl, final int type) {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				// 先判断本地有没有歌词文件
				// 如果有，则加载歌词文件
				// 如果没有，则判断kscUrl是否为空
				// 如果kscUrl为空，则从服务器获取kscUrl的路径，然后下载ksc歌词文件
				// 如果kscUrl不为空，则直接下载ksc歌词文件

				String kscFilePath = Constants.PATH_KSC + File.separator
						+ displayName + ".ksc";

				File kscFile = new File(kscFilePath);
				if (!kscFile.exists()) {
					return null;
				} else {

					SongMessage songMessage = new SongMessage();

					if (type == SongMessage.KSCTYPELRC) {

						songMessage.setType(SongMessage.LRCKSCLOADED);
					} else if (type == SongMessage.KSCTYPEDES) {

						songMessage.setType(SongMessage.DESKSCLOADED);
					} else if (type == SongMessage.KSCTYPELOCK) {

						songMessage.setType(SongMessage.LOCKKSCLOADED);
					}

					songMessage.setKscFilePath(kscFilePath);
					songMessage.setSid(sid);
					// 通知
					ObserverManage.getObserver().setMessage(songMessage);
				}
				return null;

			}

			@Override
			protected void done() {

			}
		}.execute();
	}
}
