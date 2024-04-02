package mycompany;

import com.github.lgooddatepicker.components.DatePicker;
import org.sqlite.SQLiteConfig;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	private static JFrame frame;
    public static DatePicker datePicker;
	private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                setVisible(true);
            } catch (Exception e) {
				logger.error("some exception message", e);
            }
        });
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 521, 299);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		datePicker = new DatePicker();
		datePicker.setDate(LocalDate.now());
		datePicker.addDateChangeListener(event -> {
            try {
				userAttendanceTable.loadTable();
			}catch (SQLException e) {
				logger.error("and exception occurred", e);
			}
        });
		JLabel lblId = new JLabel("ID:101011");

		JPanel panel = new JPanel();
		panel.add(datePicker);
		panel.add(lblId);
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JPanel tableJPanel;
		try {
			tableJPanel = new userAttendanceTable();
			frame.getContentPane().add(tableJPanel, BorderLayout.CENTER);
		} catch (SQLException e) {
			logger.error("and exception occurred", e);
		}

        JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new GridLayout(0, 1, 0, 5));

		JButton btnNewButton_1 = inButton();
		panel_1.add(btnNewButton_1);

		JButton btnNewButton = outButton();
		panel_1.add(btnNewButton);

        JButton button = new JButton("🔙");
		button.setFont(new Font("Dialog", Font.BOLD, 22));
		button.addActionListener(e -> {
            frame.dispose();
            new Login().setVisible(true);
            System.out.println("EXIT");
        });
		panel_1.add(button);

	}

	private static JButton outButton() {
		JButton btnNewButton = new JButton("OUT");
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton.addActionListener(arg0 -> {
            String personId = String.valueOf(Login.getUserId());

            try (Connection connection = makeConnection()) {

                checkOut(connection, Integer.parseInt(personId));
                userAttendanceTable.loadTable();
            } catch (Exception es) {
				logger.error("and exception occurred", es);
            }
        });
		return btnNewButton;
	}

	private static JButton inButton() {
		JButton btnNewButton_1 = new JButton("IN");
		btnNewButton_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton_1.addActionListener(e -> {
            String personId = String.valueOf(Login.getUserId());
            System.out.println(personId);

            try {
				Connection connection = makeConnection();
					checkIn(connection, Integer.parseInt(personId));
                userAttendanceTable.loadTable();
            } catch (SQLException es) {
				logger.error("and exception occurred", es);
            }
        });
		return btnNewButton_1;
	}

	public static void checkOut(Connection connection, int id) {

		String sqlString = "INSERT INTO Attendance (person_id, time_out, EventTimeStamp) VALUES (?, datetime(), date());";

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (Exception e) {
			logger.error("and exception occurred", e);
		}

		
	}
	
	public static void checkIn(Connection connection, int id) {

		String sqlString = "INSERT INTO Attendance (person_id, time_in, EventTimeStamp) VALUES (?, datetime(), date());";

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			System.out.println("success");
		} catch (Exception e) {
			logger.error("and exception occurred", e);
		}

	}


	public static Connection makeConnection() {

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("Attendance_schema.db");
            assert url != null;
            String URLStringBuilder = "jdbc:sqlite:" +
                    url.getPath();

			SQLiteConfig sqLiteConfig = new SQLiteConfig();
			sqLiteConfig.enforceForeignKeys(true);

            return DriverManager.getConnection(URLStringBuilder,
					sqLiteConfig.toProperties());

		} catch (SQLException e) {
			logger.error("and exception occurred", e);
		}

		return null;
	}

	public static void setVisible(boolean val) {
		try {
			DriverManager.registerDriver(new org.sqlite.JDBC());
		} catch (SQLException e) {
			logger.error("and exception occurred", e);
		}
		new Main();
        frame.setVisible(val);
	}

}
