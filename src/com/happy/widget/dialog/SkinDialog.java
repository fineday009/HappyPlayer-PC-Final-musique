package com.happy.widget.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.happy.common.Constants;
import com.happy.event.PanelMoveDialog;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.widget.button.BaseButton;
import com.happy.widget.panel.ScrollBarUI;
import com.happy.widget.panel.SkinListPanel;
import com.happy.widget.panel.TitleBackGroundPanel;
import com.happy.widget.slider.TranSlider;

public class SkinDialog extends JDialog implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 背景图片
	 */
	private JLabel bgJLabel;

	private int width = 0;

	private int height = 0;

	/**
	 * 左右边距
	 */
	private int rightPad = 10;

	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;

	private SkinEvent skinEvent;

	private JScrollPane scrollPane;

	public SkinDialog() {

		// 设定禁用窗体装饰，这样就取消了默认的窗体结构
		this.setUndecorated(true);

		width = Constants.mainFrameWidth / 5 * 3;
		height = Constants.mainFrameHeight / 4 * 3;

		this.setSize(width, height);
		this.setAlwaysOnTop(true);

		initComponent();
		initSkin();

		ObserverManage.getObserver().addObserver(this);
	}

	private void initComponent() {
		this.getContentPane().setLayout(null);

		int titleHeight = height / 4 / 3;

		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, width, titleHeight);
		new PanelMoveDialog(this, titlePanel);

		// 关闭按钮
		String closeButtonBaseIconPath = iconPath + "close_normal.png";
		String closeButtonOverIconPath = iconPath + "close_hot.png";
		String closeButtonPressedIconPath = iconPath + "close_down.png";

		int buttonSize = titleHeight / 3 * 2;

		int buttonY = (titleHeight - buttonSize) / 2;

		BaseButton closeButton = new BaseButton(closeButtonBaseIconPath,
				closeButtonOverIconPath, closeButtonPressedIconPath,
				buttonSize, buttonSize);
		closeButton.setBounds(width - buttonSize - rightPad, buttonY,
				buttonSize, buttonSize);
		closeButton.setToolTipText("关闭");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				if (skinEvent != null) {
					skinEvent.close();
				}
			}
		});
		titlePanel.setLayout(null);
		TitleBackGroundPanel titleBackGroundPanel = new TitleBackGroundPanel(
				width, titleHeight);
		titleBackGroundPanel.setBounds(0, 0, width, titleHeight);

		JLabel titleJLabel = new JLabel("皮肤窗口");
		titleJLabel.setForeground(Color.white);
		titleJLabel.setBounds(10, 0, width / 3, titleHeight);

		titlePanel.add(titleJLabel);
		titlePanel.add(closeButton);
		titlePanel.add(titleBackGroundPanel);
		titlePanel.setOpaque(false);

		JPanel buttomPanel = new JPanel();
		buttomPanel.setBounds(0, height - titleHeight, width, titleHeight);

		JLabel textJLabel = new JLabel(" 列表透明度: ");
		textJLabel.setForeground(Color.black);

		final JLabel stextJLabel = new JLabel("");
		stextJLabel.setForeground(Color.black);

		final TranSlider tranSlider = new TranSlider();
		tranSlider.setOpaque(false); // slider的背景透明
		tranSlider.setFocusable(false);
		tranSlider.setMaximum(255);
		tranSlider.setMinimum(0);
		tranSlider.setValue(Constants.listViewAlpha);

		String tip = 100 - (int) (Constants.listViewAlpha * 1.00 / 255 * 100)
				+ "%";
		stextJLabel.setText(" " + tip + " ");

		tranSlider.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {

				// // /* 获取鼠标的位置 */
				tranSlider.setValue(tranSlider.getMaximum() * e.getX()
						/ tranSlider.getWidth());

			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});

		tranSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String tip = 100
						- (int) (((JSlider) e.getSource()).getValue() * 1.00 / 255 * 100)
						+ "%";
				stextJLabel.setText(" " + tip + " ");

				Constants.listViewAlpha = ((JSlider) e.getSource()).getValue();

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.UPDATE_LISTVIEW_ALPHA);
				ObserverManage.getObserver().setMessage(messageIntent);

			}
		});

		buttomPanel.setLayout(new BorderLayout());
		buttomPanel.add(textJLabel, BorderLayout.WEST);
		buttomPanel.add(tranSlider, BorderLayout.CENTER);
		buttomPanel.add(stextJLabel, BorderLayout.EAST);

		this.getContentPane().add(titlePanel);
		this.getContentPane().add(buttomPanel);

		SkinListPanel skinListPanel = new SkinListPanel(width);
		scrollPane = new JScrollPane(skinListPanel);
		scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(100));
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		// 不显示水平的滚动条
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setBounds(0, titlePanel.getHeight(), width, height
				- titlePanel.getHeight() - buttomPanel.getHeight());
		this.getContentPane().add(scrollPane);
	}

	/**
	 * 初始化皮肤
	 */
	private void initSkin() {

		bgJLabel = new JLabel(getBackgroundImageIcon());// 把背景图片显示在一个标签里面
		// 把标签的大小位置设置为图片刚好填充整个面板
		bgJLabel.setBounds(1, 1, width - 2, height - 2);
		this.getContentPane().add(bgJLabel);
	}

	/**
	 * 获取背景图片
	 * 
	 * @return
	 */
	private ImageIcon getBackgroundImageIcon() {
		String backgroundPath = Constants.PATH_BACKGROUND + File.separator
				+ Constants.backGroundName;
		ImageIcon background = new ImageIcon(backgroundPath);// 背景图片
		background.setImage(background.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		return background;
	}

	public int getMWidth() {
		return width;
	}

	public int getMHeight() {
		return height;
	}

	public void setSkinEvent(SkinEvent skinEvent) {
		this.skinEvent = skinEvent;
	}

	public interface SkinEvent {
		public void close();
	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.UPDATE_SKIN)) {
						bgJLabel.setIcon(getBackgroundImageIcon());
					}
				}
			}
		});
	}
}
