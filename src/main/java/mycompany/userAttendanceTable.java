package mycompany;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class userAttendanceTable extends JPanel {

	private static final long serialVersionUID = 1L;
		private static JTable table;

	/**
	 * Create the panel.
	 */
	public userAttendanceTable() throws SQLException{

		table = new JTable();
		loadTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane);

	}

	public static void loadTable() throws SQLException {
		int userId = Login.getUserId();
		String currDate = Date.valueOf(Main.datePicker.getDate()).toString();

		StringBuilder sqlStringBuilder = new StringBuilder();
		sqlStringBuilder.append("SELECT a.id , a.person_id , a.time_in , a.time_out , a.EventTimeStamp  FROM Attendance a WHERE person_id = ");
		sqlStringBuilder.append(userId);
		sqlStringBuilder.append(" and strftime('%Y-%m-%d', EventTimeStamp ) = '");
		sqlStringBuilder.append(currDate);
		sqlStringBuilder.append("';");

		Connection conn = Main.makeConnection();

		//sample id

		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(sqlStringBuilder.toString());

		String[] column = {"id", "personId", "inTime", "outTime", "timeStamp"};
		Object[][] data = new Object[50][5];

		for (int i = 0; resultSet.next(); i++) {
			data[i][0] = resultSet.getInt(1);
			data[i][1] = resultSet.getInt(2);
			data[i][2] = resultSet.getString(3);
			data[i][3] = resultSet.getString(4);
			data[i][4] = resultSet.getString(5);
		}

		DefaultTableModel model = new DefaultTableModel(data,column);
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(45);
	}



}
