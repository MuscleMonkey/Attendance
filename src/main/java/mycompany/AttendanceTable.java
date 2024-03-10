package mycompany;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.sqlite.SQLiteConfig;

import com.github.lgooddatepicker.components.DatePicker;


public class AttendanceTable extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JTable table = new JTable();
	private static DatePicker datePicker = new DatePicker();


	/**
	 * Create the panel.
	 *
	 * @throws SQLException
	 */
	public AttendanceTable() throws SQLException {

		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 5, 5);
		setLayout(flowLayout);
		
		datePicker.setDate(LocalDate.now());

		add(datePicker);

		JButton btnReload = new JButton("Refresh");
		btnReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try(Connection connection =  makeConnection();) {

					refreshTable(connection);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(btnReload);

		try (Connection connection =  makeConnection();) {

			refreshTable(connection);

		}catch (SQLException e) {
		}

		table.setPreferredScrollableViewportSize(new Dimension(450, 300));
		table.setRowSelectionAllowed(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		add(scrollPane);

	}

	/**
	 *
	 * https://stackoverflow.com/questions/21450914/sql-auto-increment-by-datetime - reference for line 95
	 * Make 50 rows statically
	 *
	 */

	public static void refreshTable(Connection connection) throws SQLException {

		try {

			StringBuilder queryStringBuilder = new StringBuilder();
			queryStringBuilder.append("SELECT * FROM Attendance a WHERE STRFTIME('%Y-%m-%d', a.EventTimeStamp) = ");
			queryStringBuilder.append("'");
			queryStringBuilder.append(Date.valueOf(datePicker.getDate()));
			queryStringBuilder.append("';");

			Statement statement = connection.createStatement();	
			ResultSet resultSet = statement.executeQuery(queryStringBuilder.toString());

			String columns[] = { "id", "person_name	", "Time_in", "Time_out" };
			Object data[][] = new Object[50][4];

			for (int i = 0; resultSet.next(); i++) {
				data[i][0] = resultSet.getInt(1);
				data[i][1] = resultSet.getInt(2);
				data[i][2] = resultSet.getTime(3);
				data[i][3] = resultSet.getTime(4);

			}

			DefaultTableModel model = new DefaultTableModel(data, columns);
			table.setModel(new DefaultTableModel(new Object[50][4],
					new String[] { "Attendance Id", "New column", "Person Id", "In Time" }));
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(40);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection makeConnection() {

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("Attendance_schema.db");
			StringBuilder URLStringBuilder = new StringBuilder();
			URLStringBuilder.append("jdbc:sqlite:");
			URLStringBuilder.append(url.getPath());
			
			SQLiteConfig sqLiteConfig = new SQLiteConfig();
			sqLiteConfig.enforceForeignKeys(true);
			
			Connection connection2 = DriverManager.getConnection(URLStringBuilder.toString(), sqLiteConfig.toProperties());
			return connection2;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
