package util;

import javax.swing.*;
import java.awt.*;

public class MessageDisplayWindow {

	public static void main(String[] args) throws Throwable {
		MessageDisplayWindow messageDisplayWindow = new MessageDisplayWindow("test");
		messageDisplayWindow.show();

		while (true) {
			Thread.sleep(500);
			messageDisplayWindow.append(System.currentTimeMillis());
		}
	}

	private final JFrame jframe;

	private int windowsWidth = 600;

	private int windowsHeight = 300;

	private int locationOffset_x = 0;

	private int locationOffset_y = 0;

	private boolean shutdownSystemWhenWindowsClose = true;

	private boolean tailing = true;

	private JScrollBar verticalScrollBar;

	private JTextArea textArea;

	public MessageDisplayWindow(String title) {
		this.jframe = new JFrame(title);
	}

	public void locationOffset(int x, int y) {
		this.locationOffset_x = x;
		this.locationOffset_y = y;
	}

	public void show() {
		initial();
		this.jframe.setVisible(true);
	}

	public void append(Object msg) {
		this.textArea.append(msg + "\n"); // 會變更verticalScrollBar長度

		if (this.tailing) {
			int maxSize = this.verticalScrollBar.getMaximum();
			this.verticalScrollBar.setValue(maxSize);
		}
	}

	public void clean() {
		this.textArea.setText("");
	}

	public void close() {
		this.jframe.dispose();
	}

	protected void initial() {
		if (this.shutdownSystemWhenWindowsClose) {
			this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		this.jframe.setAlwaysOnTop(true); // 保持上層

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int windos_location_x = centerPoint.x - (this.windowsWidth / 2) + this.locationOffset_x;
		int windos_location_y = centerPoint.y - (this.windowsHeight / 2) + this.locationOffset_y;

		this.jframe.setSize(this.windowsWidth, this.windowsHeight);
		this.jframe.setLocation(windos_location_x, windos_location_y);

		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.textArea.setFont(new Font("新細明體", Font.PLAIN, 24));

		JScrollPane scrollPane = new JScrollPane(this.textArea);
		this.verticalScrollBar = scrollPane.getVerticalScrollBar();

		Container container = this.jframe.getContentPane();
		container.add(BorderLayout.CENTER, scrollPane);
	}

	public int getWindowsWidth() {
		return windowsWidth;
	}

	public void setWindowsWidth(int windowsWidth) {
		this.windowsWidth = windowsWidth;
	}

	public int getWindowsHeight() {
		return windowsHeight;
	}

	public void setWindowsHeight(int windowsHeight) {
		this.windowsHeight = windowsHeight;
	}

	public boolean isShutdownSystemWhenWindowsClose() {
		return shutdownSystemWhenWindowsClose;
	}

	public void setShutdownSystemWhenWindowsClose(boolean shutdownSystemWhenWindowsClose) {
		this.shutdownSystemWhenWindowsClose = shutdownSystemWhenWindowsClose;
	}

	public boolean isTailing() {
		return tailing;
	}

	public void setTailing(boolean tailing) {
		this.tailing = tailing;
	}

	public JFrame getJframe() {
		return jframe;
	}

}
