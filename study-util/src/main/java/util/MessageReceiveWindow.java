package util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class MessageReceiveWindow extends MessageDisplayWindow {

	public static void main(String[] args) {
		MessageReceiveWindow messageReceiveWindow = new MessageReceiveWindow("gg");
		messageReceiveWindow.show();
	}

	private Consumer<String> inputReceiver = (msg) -> {
	};

	public MessageReceiveWindow(String title) {
		super(title);
	}

	@Override
	protected void initial() {
		super.initial();

		JFrame jframe = super.getJframe();
		Container container = jframe.getContentPane();

		JTextField userText = new JTextField(60);
		userText.setFont(new Font("新細明體", Font.PLAIN, 24));
		userText.addActionListener(buildInputListener(userText));

		jframe.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				userText.requestFocus(); // 畫面開始時聚焦在input上
			}
		});

		container.add(BorderLayout.SOUTH, userText);
	}

	@SuppressWarnings("serial")
	public AbstractAction buildInputListener(final JTextField userText) {
		final Consumer<String> receiver = (msg) -> {
			super.append(msg);
			this.inputReceiver.accept(msg);
		};

		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = e.getActionCommand();
				userText.setText("");
				receiver.accept(input);
			}
		};
	}

	public Consumer<String> getInputReceiver() {
		return inputReceiver;
	}

	public void setInputReceiver(Consumer<String> inputReceiver) {
		this.inputReceiver = inputReceiver;
	}

}
