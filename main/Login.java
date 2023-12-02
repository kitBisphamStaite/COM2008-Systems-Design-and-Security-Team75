import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Login {
	public static String userType;
    public static void main(String[] args) {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
            createLoginFrame(connection);
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }
    }

    private static void createLoginFrame(Connection connection) {
        JFrame loginFrame = new JFrame("Login Page");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(410, 150);

        JPanel panel = new JPanel();
        loginFrame.add(panel);
        placeComponents(loginFrame, panel, connection);

        loginFrame.setVisible(true);
    }

    private static void placeComponents(JFrame loginFrame, JPanel panel, Connection connection) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Email:");
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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
        
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 80, 90, 25);
        panel.add(registerButton);
        
        JLabel unsuccessfulLoginLabel = new JLabel();
        unsuccessfulLoginLabel.setBounds(200, 80, 200, 25);
        panel.add(unsuccessfulLoginLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                char[] password = passwordText.getPassword();
                Boolean verify = verifyLogin(username, password, connection);
                
                if (verify) {
                	setUserType(username, connection);
                	System.out.println(userType);
                	panel.removeAll();
                }else {
                	unsuccessfulLoginLabel.setText("Incorrect email or password.");
                }
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		loginFrame.dispose();
                Register.main(null);
                
            }
        });
        
    }
    
    private static Boolean verifyLogin(String username, char[] password, Connection connection) {
    	
    	try {
	    	//Query SQL database to fetch information on user
	    	String sql = "SELECT email, password_hash FROM Accounts WHERE email = '" + username + "'";
	    	PreparedStatement statement = connection.prepareStatement(sql);
	    	ResultSet resultSet = statement.executeQuery(sql);
	    	
	    	if (resultSet.next()) {
	    		String storedPassword = resultSet.getString("password_hash");
	    		
	    		if (verifyPassword(password, storedPassword, username, connection)) {
	    			//ADD OPENING OF HOME PAGE
	    			System.out.println("Login Successful");
	    			return true;
	    		}else {
	    			System.out.println("Login Unsuccessful");
	    			return false;
	    		}
	    		
	    	}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    private static Boolean verifyPassword(char[] enteredPassword, String storedPassword, String username, Connection connection) {
    	try {
    		String hashedEnteredPassword = HashedPasswordGenerator.hashPassword(enteredPassword, username, connection);
            return hashedEnteredPassword.equals(storedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String setUserType(String username, Connection connection) {
    	try {
    		String sql = "SELECT type FROM Accounts WHERE email = '" + username + "'";
    		PreparedStatement statement = connection.prepareStatement(sql);
	    	statement.executeQuery(sql);
	    	ResultSet resultSet = statement.executeQuery(sql);
	    	
	    	if (resultSet.next()) {
	    		userType = resultSet.getString("type");
	    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return "";
    }
    
    public static String getUserType() {
    	return userType;
    }

}
