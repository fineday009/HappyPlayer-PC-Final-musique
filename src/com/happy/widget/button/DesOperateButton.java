package com.happy.widget.button;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.widget.panel.DesOperatePanel;

/**
 * 桌面操作面板按钮
 * 
 * @author Administrator
 * 
 */
public class DesOperateButton extends JButton implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 桌面窗口事件
	 */
	private MouseInputListener desLrcDialogMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private DesOperatePanel desOperatePanel;

	public DesOperateButton(String baseIconPath, String overIconPath,
			String pressedIconPath, int width, int height,
			MouseInputListener desLrcDialogMouseListener,
			DesOperatePanel desOperatePanel) {

		ImageIcon icon = new ImageIcon(baseIconPath);
		icon.setImage(icon.getImage().getScaledInstance(width, height,
				Image.SCALE_SMOOTH));
		this.setIcon(icon);

		ImageIcon rolloverIcon = new ImageIcon(overIconPath);
		rolloverIcon.setImage(rolloverIcon.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		this.setRolloverIcon(rolloverIcon);

		ImageIcon pressedIcon = new ImageIcon(pressedIconPath);
		pressedIcon.setImage(pressedIcon.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		this.setPressedIcon(pressedIcon);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setDoubleBuffered(false);
		this.setOpaque(false);
		this.setFocusable(false);
		// 设置鼠标的图标
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.desOperatePanel = desOperatePanel;

		initLockEvent();
		ObserverManage.getObserver().addObserver(this);
	}

	public DesOperateButton(String baseIconPath, int width, int height,
			MouseInputListener desLrcDialogMouseListener,
			DesOperatePanel desOperatePanel) {

		ImageIcon icon = new ImageIcon(baseIconPath);
		icon.setImage(icon.getImage().getScaledInstance(width, height,
				Image.SCALE_SMOOTH));
		this.setIcon(icon);

		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setDoubleBuffered(false);
		this.setOpaque(false);
		this.setFocusable(false);
		// 设置鼠标的图标
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.desOperatePanel = desOperatePanel;

		initLockEvent();
		ObserverManage.getObserver().addObserver(this);
	}

	private void initLockEvent() {
		if (!Constants.desLrcIsLock) {
			this.addMouseListener(mouseListener);
			this.addMouseMotionListener(mouseListener);
		} else {
			this.removeMouseListener(mouseListener);
			this.removeMouseMotionListener(mouseListener);
		}

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
			desOperatePanel.setEnter(true);
			desLrcDialogMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			desOperatePanel.setEnter(false);
			desLrcDialogMouseListener.mouseExited(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			desLrcDialogMouseListener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			desLrcDialogMouseListener.mouseMoved(e);
		}

	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.LOCKDESLRC)) {
						initLockEvent();
					}
				}
			}
		});
	}
}
