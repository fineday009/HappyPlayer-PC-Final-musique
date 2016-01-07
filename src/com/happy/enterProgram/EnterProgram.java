package com.happy.enterProgram;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.service.MediaPlayerService;
import com.happy.ui.MainFrame;
import com.happy.ui.SplashFrame;
import com.happy.util.DataUtil;
import com.happy.util.FontsUtil;

public class EnterProgram {
	/**
	 * 应用启动窗口
	 */
	private static SplashFrame splashFrame;
	/**
	 * 主窗口
	 */
	private static MainFrame mainFrame;

	/**
	 * 程序入口
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * 
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				splashFrame = new SplashFrame();
				splashFrame.setVisible(true);

				init();
			}
		});

	}

	protected static void init() {

		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {

				initGlobalFont(FontsUtil.getBaseFont(Constants.APPFONTSIZE));
				// 先初始化数据
				DataUtil.init();
				// 初始化播放列表数据
				MediaManage.getMediaManage().initPlayListData();
				// 初始化播放器服务
				MediaPlayerService.getMediaPlayerService().init();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						mainFrame = new MainFrame();
						splashFrame.setVisible(false);
						mainFrame.setVisible(true);
					}
				});

				return null;

			}

			@Override
			protected void done() {
			}
		}.execute();

	}

	/**
	 * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
	 */
	private static void initGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys
				.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
}
