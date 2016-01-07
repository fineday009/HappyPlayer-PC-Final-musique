package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.KscLyricsLineInfo;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.util.KscLyricsParserUtil;

public class FloatLyricsView extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否有ksc歌词
	 */
	private boolean hasKsc = false;

	/**
	 * 默认高亮未读画笔
	 */
	private GradientPaint paintHLDEF;
	/**
	 * 高亮已读画笔
	 */
	private GradientPaint paintHLED;

	/**
	 * 显示放大缩小的歌词文字的大小值
	 */
	private float SCALEIZEWORDDEF = 0;

	/**
	 * 歌词每行的间隔
	 */
	private float INTERVAL = 0;

	/**
	 * 歌词解析
	 */
	private KscLyricsParserUtil kscLyricsParser;

	/**
	 * 歌词列表
	 */
	private TreeMap<Integer, KscLyricsLineInfo> lyricsLineTreeMap;

	/**
	 * 当前歌词的所在行数
	 */
	private int lyricsLineNum = -1;

	/**
	 * 当前歌词的第几个字
	 */
	private int lyricsWordIndex = -1;

	/**
	 * 当前歌词第几个字 已经播放的时间
	 */
	private int lyricsWordHLEDTime = 0;

	/**
	 * 当前歌词第几个字 已经播放的长度
	 */
	private float lineLyricsHLWidth = 0;

	/** 高亮歌词当前的其实x轴绘制坐标 **/
	private float highLightLrcMoveX;

	/***
	 * 播放进度
	 */
	private int progress = 0;

	/**
	 * 字体大小缩放比例
	 */
	private int fontSizeScale = 0;
	/**
	 * 字体大小缩放比例
	 */
	private int oldFontSizeScale = 0;

	private int width = 0;
	private int height = 0;

	private MouseListener mouseListener = new MouseListener();
	/**
	 * 判断是否进入
	 */
	private boolean isEnter = false;
	/**
	 * 是否显示
	 */
	private boolean isShow = false;
	/**
	 * 桌面窗口事件
	 */
	private MouseInputListener desLrcDialogMouseListener;

	// 统一字体
	String fontFilePath = Constants.PATH_FONTS + File.separator
			+ "weiruanyahei14M.ttf";

	public FloatLyricsView(int width, int height,
			MouseInputListener desLrcDialogMouseListener) {
		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.width = width;
		this.height = height;
		initSizeWord();
		ObserverManage.getObserver().addObserver(this);
		this.setOpaque(false);

		initLockEvent();
	}

	public boolean getHasKsc() {
		return hasKsc;
	}

	public void setHasKsc(boolean hasKsc) {
		this.hasKsc = hasKsc;
		repaint();
	}

	/**
	 * 初始化默认字体的渐变颜色
	 * 
	 */
	private void initPaintHLDEFColor(float x, float y, int height) {
		paintHLDEF = new GradientPaint(x, y, new Color(0, 52, 138), x, y
				+ height, new Color(0, 128, 192), true);
	}

	/**
	 * 初始化高亮字体的渐变颜色
	 * 
	 */
	private void initPaintHLEDColor(float x, float y, int height) {
		// Point2D start = new Point2D.Float(x, y);
		// Point2D end = new Point2D.Float(x + width, y + height);
		// float[] dist = { 0.0f, 0.2f, 1.0f };
		// Color[] colors = { new Color(130, 247, 253), new Color(255, 255,
		// 255),
		// new Color(3, 233, 252) };
		// paintHLED = new LinearGradientPaint(start, end, dist, colors,
		// CycleMethod.NO_CYCLE);

		paintHLED = new GradientPaint(x, y, new Color(130, 247, 253), x, y
				+ height, new Color(255, 255, 255), true);
	}

	/**
	 * 初始化字体大小
	 */
	private void initSizeWord() {
		fontSizeScale = Constants.desktopLrcFontSize;
		float maxSizeWord = (float) (height - 8 * 3) / 2;
		SCALEIZEWORDDEF = fontSizeScale * maxSizeWord
				/ Constants.desktopLrcFontMaxSize;

		INTERVAL = (float) (height - SCALEIZEWORDDEF * 2) / 3;
	}

	// 绘制组件
	public void paint(Graphics g) {

		// long begin = System.currentTimeMillis();
		Graphics2D g2d = (Graphics2D) g;
		// 以达到边缘平滑的效果

		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(renderHints);
		//
		g2d.setFont(new Font("宋体", Font.BOLD, (int) SCALEIZEWORDDEF));

		if (isEnter || isShow) {
			g2d.setPaint(new Color(0, 0, 0, 100));
			g2d.fillRect(0, 0, width, height);
		}

		if (!hasKsc) {
			drawDefText(g2d);
		} else {
			drawLrcText(g2d);
		}
		// long time = System.currentTimeMillis() - begin;
		// System.out.println("桌面耗时" + time);
	}

	/**
	 * 画默认文本
	 * 
	 * @param g2d
	 */
	private void drawDefText(Graphics2D g2d) {
		String tip = Constants.APPTIPTITLE;
		g2d.setPaint(Constants.DESLRCREADEDCOLOR[Constants.desktopLrcIndex]);

		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D rc = fm.getStringBounds(tip, g2d);

		int textWidth = (int) rc.getWidth();
		int textHeight = fm.getHeight();

		int leftX = (width - textWidth) / 2;
		float heightY = (height + textHeight) / 2;

		// 这里不知为何还要减去fm.getDescent() + fm.getLeading() 绘画时才能把全文字绘画完整
		int clipY = (int) (heightY - textHeight + (fm.getDescent() + fm
				.getLeading()));
		drawBackground(g2d, tip, leftX, heightY);

		initPaintHLDEFColor(leftX, heightY, textHeight);
		g2d.setPaint(paintHLDEF);
		g2d.drawString(tip, leftX, heightY);

		g2d.setClip(leftX, clipY, textWidth / 2, textHeight);

		drawBackground(g2d, tip, leftX, heightY);

		initPaintHLEDColor(leftX, heightY, textHeight);
		g2d.setPaint(paintHLED);
		g2d.drawString(tip, leftX, heightY);
	}

	/**
	 * 描绘轮廓
	 * 
	 * @param canvas
	 * @param string
	 * @param x
	 * @param y
	 */
	private void drawBackground(Graphics2D g2d, String string, float x, float y) {
		g2d.setPaint(new Color(0, 0, 0, 100));
		g2d.drawString(string, x - 2, y);
		g2d.drawString(string, x + 2, y);
		g2d.drawString(string, x, y + 2);
		g2d.drawString(string, x, y - 2);
	}

	/**
	 * 画歌词
	 * 
	 * @param g2d
	 */
	private void drawLrcText(Graphics2D g2d) {

		// 画之前的歌词
		if (lyricsLineNum == -1) {
			String lyricsLeft = lyricsLineTreeMap.get(0).getLineLyrics();
			FontMetrics fm2 = g2d.getFontMetrics();
			int textHeight2 = fm2.getHeight();
			drawBackground(g2d, lyricsLeft, 10, SCALEIZEWORDDEF + INTERVAL);
			initPaintHLDEFColor(10, SCALEIZEWORDDEF + INTERVAL, textHeight2);
			g2d.setPaint(paintHLDEF);
			g2d.drawString(lyricsLeft, 10, SCALEIZEWORDDEF + INTERVAL);
			if (lyricsLineNum + 2 < lyricsLineTreeMap.size()) {
				String lyricsRight = lyricsLineTreeMap.get(lyricsLineNum + 2)
						.getLineLyrics();

				FontMetrics fm = g2d.getFontMetrics();
				Rectangle2D rc = fm.getStringBounds(lyricsRight, g2d);
				int textHeight = fm.getHeight();
				int lyricsRightWidth = (int) rc.getWidth();
				float textRightX = width - lyricsRightWidth - 10;
				// 如果计算出的textX为负数，将textX置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
				textRightX = Math.max(textRightX, 10);
				drawBackground(g2d, lyricsRight, textRightX,
						(SCALEIZEWORDDEF + INTERVAL) * 2);

				initPaintHLDEFColor(textRightX,
						(SCALEIZEWORDDEF + INTERVAL) * 2, textHeight);
				g2d.setPaint(paintHLDEF);
				g2d.drawString(lyricsRight, textRightX,
						(SCALEIZEWORDDEF + INTERVAL) * 2);
			}
		} else {

			// 先设置当前歌词，之后再根据索引判断是否放在左边还是右边

			KscLyricsLineInfo kscLyricsLineInfo = lyricsLineTreeMap
					.get(lyricsLineNum);
			// 当行歌词
			String currentLyrics = kscLyricsLineInfo.getLineLyrics();

			FontMetrics fm = g2d.getFontMetrics();
			Rectangle2D rc = fm.getStringBounds(currentLyrics, g2d);

			int currentTextWidth = (int) rc.getWidth();
			int currentTextHeight = fm.getHeight();

			if (lyricsWordIndex != -1) {

				String lyricsWords[] = kscLyricsLineInfo.getLyricsWords();
				int wordsDisInterval[] = kscLyricsLineInfo
						.getWordsDisInterval();
				// 当前歌词之前的歌词
				String lyricsBeforeWord = "";
				for (int i = 0; i < lyricsWordIndex; i++) {
					lyricsBeforeWord += lyricsWords[i];
				}
				// 当前歌词
				String lyricsNowWord = lyricsWords[lyricsWordIndex].trim();// 去掉空格

				Rectangle2D rc2 = fm.getStringBounds(lyricsBeforeWord, g2d);
				// 当前歌词之前的歌词长度
				int lyricsBeforeWordWidth = (int) rc2.getWidth();

				Rectangle2D rc3 = fm.getStringBounds(lyricsNowWord, g2d);
				// 当前歌词长度
				float lyricsNowWordWidth = (int) rc3.getWidth();

				float len = lyricsNowWordWidth
						/ wordsDisInterval[lyricsWordIndex]
						* lyricsWordHLEDTime;
				lineLyricsHLWidth = lyricsBeforeWordWidth + len;
			} else {
				// 整行歌词
				lineLyricsHLWidth = currentTextWidth;
			}
			// 当前歌词行的x坐标
			float textX = 0;

			// 当前歌词行的y坐标
			float textY = 0;
			if (lyricsLineNum % 2 == 0) {

				if (currentTextWidth > width) {
					if (lineLyricsHLWidth >= width / 2) {
						if ((currentTextWidth - lineLyricsHLWidth) >= width / 2) {
							highLightLrcMoveX = (width / 2 - lineLyricsHLWidth);
						} else {
							highLightLrcMoveX = width - currentTextWidth - 10;
						}
					} else {
						highLightLrcMoveX = 10;
					}
					// 如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
					textX = highLightLrcMoveX;
				} else {
					// 如果歌词宽度小于view的宽
					textX = 10;
				}

				// 画下一句的歌词
				if (lyricsLineNum + 1 < lyricsLineTreeMap.size()) {
					String lyricsRight = lyricsLineTreeMap.get(
							lyricsLineNum + 1).getLineLyrics();

					Rectangle2D rc4 = fm.getStringBounds(lyricsRight, g2d);

					int lyricsRightWidth = (int) rc4.getWidth();
					float textRightX = width - lyricsRightWidth - 10;
					// 如果计算出的textX为负数，将textX置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
					textRightX = Math.max(textRightX, 10);
					drawBackground(g2d, lyricsRight, textRightX,
							(SCALEIZEWORDDEF + INTERVAL) * 2);

					int textHeight = fm.getHeight();
					initPaintHLDEFColor(textRightX,
							(SCALEIZEWORDDEF + INTERVAL) * 2, textHeight);
					g2d.setPaint(paintHLDEF);
					g2d.drawString(lyricsRight, textRightX,
							(SCALEIZEWORDDEF + INTERVAL) * 2);
				}

				textY = (SCALEIZEWORDDEF + INTERVAL);

			} else {

				if (currentTextWidth > width) {
					if (lineLyricsHLWidth >= width / 2) {
						if ((currentTextWidth - lineLyricsHLWidth) >= width / 2) {
							highLightLrcMoveX = (width / 2 - lineLyricsHLWidth);
						} else {
							highLightLrcMoveX = width - currentTextWidth - 10;
						}
					} else {
						highLightLrcMoveX = 10;
					}
					// 如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
					textX = highLightLrcMoveX;
				} else {
					// 如果歌词宽度小于view的宽
					textX = width - currentTextWidth - 10;
				}

				// 画下一句的歌词
				if (lyricsLineNum + 1 != lyricsLineTreeMap.size()) {
					String lyricsLeft = lyricsLineTreeMap
							.get(lyricsLineNum + 1).getLineLyrics();

					drawBackground(g2d, lyricsLeft, 10, SCALEIZEWORDDEF
							+ INTERVAL);

					int textHeight = fm.getHeight();
					initPaintHLDEFColor(10, (SCALEIZEWORDDEF + INTERVAL),
							textHeight);
					g2d.setPaint(paintHLDEF);
					g2d.drawString(lyricsLeft, 10, SCALEIZEWORDDEF + INTERVAL);
				}

				textY = (SCALEIZEWORDDEF + INTERVAL) * 2;

			}

			// System.out.println(width);

			drawBackground(g2d, currentLyrics, textX, textY);

			int textHeight = fm.getHeight();
			initPaintHLDEFColor(textX, textY, textHeight);

			g2d.setPaint(paintHLDEF);
			// 画当前歌词
			g2d.drawString(currentLyrics, textX, textY);

			// 这里不知为何还要减去fm.getDescent() + fm.getLeading() 绘画时才能把全文字绘画完整
			int clipY = (int) (textY - currentTextHeight + (fm.getDescent() + fm
					.getLeading()));

			g2d.setClip((int) textX, clipY, (int) lineLyricsHLWidth,
					currentTextHeight);

			initPaintHLEDColor(textX, textY, textHeight);
			g2d.setPaint(paintHLED);
			g2d.drawString(currentLyrics, textX, textY);

		}
	}

	public boolean getShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
		repaint();
	}

	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
		repaint();
	}

	public boolean getEnter() {
		return isEnter;
	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			desLrcDialogMouseListener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			desLrcDialogMouseListener.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			desLrcDialogMouseListener.mouseReleased(e);
			// setCursor(null);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			isEnter = true;
			repaint();

			desLrcDialogMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			isEnter = false;
			repaint();
			desLrcDialogMouseListener.mouseExited(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// /* 鼠标左键托动事件 */
			// if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
			// setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			// }
			desLrcDialogMouseListener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			desLrcDialogMouseListener.mouseMoved(e);
		}

	}

	public void init(KscLyricsParserUtil kscLyricsParser) {

		this.kscLyricsParser = kscLyricsParser;
		lyricsLineTreeMap = kscLyricsParser.getLyricsLineTreeMap();
		lyricsLineNum = -1;
		lyricsWordIndex = -1;
		lineLyricsHLWidth = 0;
		lyricsWordHLEDTime = 0;
		highLightLrcMoveX = 0;

		repaint();
	}

	/**
	 * 
	 * @param playProgress
	 */
	public void showLrc(int playProgress) {
		this.progress = playProgress;
		if (kscLyricsParser == null)
			return;
		int newLyricsLineNum = kscLyricsParser
				.getLineNumberFromCurPlayingTime(playProgress);
		if (newLyricsLineNum != lyricsLineNum) {
			lyricsLineNum = newLyricsLineNum;
			highLightLrcMoveX = 0;
		}
		lyricsWordIndex = kscLyricsParser.getDisWordsIndexFromCurPlayingTime(
				lyricsLineNum, playProgress);

		lyricsWordHLEDTime = kscLyricsParser.getLenFromCurPlayingTime(
				lyricsLineNum, playProgress);

		if (oldFontSizeScale == fontSizeScale) {
			// 字体在切换时，不进行刷新，免得会出现闪屏的问题
			repaint();
		}
		if (oldFontSizeScale != fontSizeScale) {
			oldFontSizeScale = fontSizeScale;
		}
	}

	@Override
	public void update(Observable arg0, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.DESKSCMANYLINELRCCOLOR)) {
						repaint();
					} else if (messageIntent.getAction().equals(
							MessageIntent.DESKSCMANYLINEFONTSIZE)) {
						initSizeWord();
						showLrc(progress);
						repaint();
					} else if (messageIntent.getAction().equals(
							MessageIntent.LOCKDESLRC)) {
						initLockEvent();
					}
				}
			}
		});
	}

	protected void initLockEvent() {
		if (!Constants.desLrcIsLock) {
			this.addMouseListener(mouseListener);
			this.addMouseMotionListener(mouseListener);
		} else {
			this.removeMouseListener(mouseListener);
			this.removeMouseMotionListener(mouseListener);
		}
	}
}
