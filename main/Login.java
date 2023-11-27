import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import com.sheffield.DatabaseConnectionHandler;
import com.sheffield.DatabaseOperations;


public class Login {
    public static void main(String[] args) {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }
    }

    private static void createLoginFrame() {
        JFrame loginFrame = new JFrame("Login Page");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);

        JPanel panel = new JPanel();
        loginFrame.add(panel);
        placeComponents(panel);

        loginFrame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
            }
        });
    }



    private static void openHomePage(String username) {
        JFrame homeFrame = new JFrame("Home Page");
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setSize(300, 150);

        JPanel panel = new JPanel();
        homeFrame.add(panel);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        panel.add(welcomeLabel);

        homeFrame.setVisible(true);
    }
}
