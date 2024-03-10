package mycompany;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Frame {

	private JFrame frmAttendance;
	private digitalClockPanel digitalClock;
	private CheckInCheckOutPanel checkInCheckOutJPanel;
	private JPanel attendanceJPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Frame window = new Frame();
					window.frmAttendance.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public Frame() throws SQLException, FontFormatException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException
	 * @throws IOException
	 * @throws FontFormatException
	 */
	private void initialize() throws SQLException, FontFormatException, IOException {
		frmAttendance = new JFrame();
		frmAttendance.setTitle("Attendance");
		frmAttendance.setLocationByPlatform(true);
		frmAttendance.setBounds(100, 100, 800, 445);
		frmAttendance.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		digitalClock = new digitalClockPanel();
		frmAttendance.getContentPane().add(digitalClock, BorderLayout.NORTH);

		attendanceJPanel = new AttendanceTable();
		frmAttendance.getContentPane().add(attendanceJPanel, BorderLayout.EAST);
		attendanceJPanel.setPreferredSize(new Dimension(500, 0));
		attendanceJPanel.getSize();

		checkInCheckOutJPanel = new CheckInCheckOutPanel();
		frmAttendance.getContentPane().add(checkInCheckOutJPanel, BorderLayout.WEST);
	}

}
