package mycompany;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CheckInCheckOutPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField id;

	/**
	 * Create the panel.
	 */
	public CheckInCheckOutPanel() {
		setPreferredSize(new Dimension(300, 200));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		id = new JTextField();
		id.setPreferredSize(new Dimension(5, 60));
		id.setFont(new Font("Nimbus Sans", Font.PLAIN, 18));
		add(id);
		id.setColumns(15);

		JButton btnTimeIn = new JButton("TIME IN");
		btnTimeIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String personId = id.getText();

				if (personId.isBlank()) {
					JOptionPane.showMessageDialog(null, "Id field should not be empty");
					return;
				}

				try (Connection connection = AttendanceTable.makeConnection();) {

					checkIn(connection, Integer.parseInt(personId));
					AttendanceTable.refreshTable(connection);
				} catch (Exception es) {
					es.printStackTrace();
				}

			}
		});
		btnTimeIn.setPreferredSize(new Dimension(250, 50));
		add(btnTimeIn);

		JButton btnTimeOut = new JButton("TIME OUT");
		btnTimeOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try (Connection connection = AttendanceTable.makeConnection();) {

					String personId = id.getText();

					if (personId.isBlank()) {
						JOptionPane.showMessageDialog(null, "Id field should not be empty");
						return;
					}

					try {

						checkOut(connection, Integer.parseInt(personId));

					} catch (NullPointerException n) {
						n.printStackTrace();
					}

				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		});
		btnTimeOut.setPreferredSize(new Dimension(250, 50));
		add(btnTimeOut);

	}

	public static void checkOut(Connection connection, int id) {

		String sqlString = "INSERT INTO Attendance (person_id, time_out, EventTimeStamp) VALUES (?, datetime(), date());";

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			AttendanceTable.refreshTable(connection);

			System.out.println(id + "checked out");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void checkIn(Connection connection, int id) {

		String sqlString = "INSERT INTO Attendance (person_id, time_in, EventTimeStamp) VALUES (?, datetime(), date());";

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			AttendanceTable.refreshTable(connection);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
