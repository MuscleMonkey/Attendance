package mycompany;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class digitalClockPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	JLabel digitalClock;

	/**
	 * Create the panel.
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public digitalClockPanel() throws FontFormatException, IOException {

		//embed the downloaded font
//		InputStream iStream = digitalClockPanel.class.getResourceAsStream("/digital_7/digital-7.ttf");
//		Font font = Font.createFont(Font.TRUETYPE_FONT, iStream);
//		font = font.deriveFont(Font.PLAIN,36);
//
//		GraphicsEnvironment gEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		gEnvironment.registerFont(font);

		setBackground(new Color(0, 0, 0));

		digitalClock= new JLabel("New label");
		digitalClock.setFont(new Font("Digital-7", Font.PLAIN, 36));
		digitalClock.setBackground(new Color(0, 0, 0));
		digitalClock.setForeground(new Color(0, 128, 0));
		add(digitalClock);

		startClock();
	}

	public void startClock() {

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				LocalTime localTime = LocalTime.now();
				digitalClock.setText(localTime.format(timeFormatter));
			}
		};
		Timer timer = new Timer(1000, actionListener);
		timer.setInitialDelay(0);
		timer.start();
	}

}
