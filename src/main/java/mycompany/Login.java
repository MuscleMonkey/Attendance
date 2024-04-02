package mycompany;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
    private static JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("login");
		btnNewButton.addActionListener(e -> {
            try (Connection connection = Main.makeConnection()) {
                String queryString = "SELECT COUNT(p.name) from Person p WHERE p.person_id  = ?;";

                int personId = Integer.parseInt(textField.getText());
                assert connection != null;
                PreparedStatement preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setInt(1, personId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    if (resultSet.getInt(1) != 0) {
                        System.out.println("USER EXIST");
                        dispose();
                        Main.setVisible(true);
                    } else {
                        System.out.println("USER DOESN'T EXIST");
                    }
                }

                preparedStatement.close();
                resultSet.close();

            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        });
		contentPane.add(btnNewButton);
	}

	public static int getUserId() {
		return Integer.parseInt(textField.getText());
	}
}
