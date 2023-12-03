import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Register {
    public static void main(String[] args) {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
            createRegisterFrame(connection);
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }
    }

    private static void createRegisterFrame(Connection connection) {
    	//Set up frame
        JFrame registerFrame = new JFrame("Register Page");
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setSize(400, 400);
        
        //Set up panel
        JPanel panel = new JPanel();
        registerFrame.add(panel);
        placeComponents(registerFrame, panel, connection);

        registerFrame.setVisible(true);
    }

    private static void placeComponents(JFrame registerFrame, JPanel panel, Connection connection) {
    	//Add all components to panel
        panel.setLayout(null);

        JLabel forenameLabel = new JLabel("*Forename:");
        forenameLabel.setBounds(10, 20, 80, 25);
        panel.add(forenameLabel);

        JTextField forenameText = new JTextField(20);
        forenameText.setBounds(140, 20, 165, 25);
        panel.add(forenameText);
        
        JLabel surnameLabel = new JLabel("*Surname:");
        surnameLabel.setBounds(10, 50, 80, 25);
        panel.add(surnameLabel);

        JTextField surnameText = new JTextField(20);
        surnameText.setBounds(140, 50, 165, 25);
        panel.add(surnameText);

        JLabel userLabel = new JLabel("*Email:");
        userLabel.setBounds(10, 80, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(140, 80, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("*Password:");
        passwordLabel.setBounds(10, 110, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(140, 110, 165, 25);
        panel.add(passwordText);
        
        JLabel bankAccountOptionalLabel = new JLabel("Input bank details below (optional):");
        bankAccountOptionalLabel.setBounds(10, 140, 220, 25);
        panel.add(bankAccountOptionalLabel);
        
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setBounds(10, 170, 120, 25);
        panel.add(cardNumberLabel);

        JTextField cardNumberText = new JTextField(20);
        cardNumberText.setBounds(140, 170, 165, 25);
        panel.add(cardNumberText);
        
        JLabel cardHolderLabel = new JLabel("Card Holder Name:");
        cardHolderLabel.setBounds(10, 200, 120, 25);
        panel.add(cardHolderLabel);

        JTextField cardHolderText = new JTextField(20);
        cardHolderText.setBounds(140, 200, 165, 25);
        panel.add(cardHolderText);
        
        JLabel cardTypeLabel = new JLabel("Type of card:");
        cardTypeLabel.setBounds(10, 230, 120, 25);
        panel.add(cardTypeLabel);

        JTextField cardTypeText = new JTextField(20);
        cardTypeText.setBounds(140, 230, 165, 25);
        panel.add(cardTypeText);
        
        JLabel expirationDateLabel = new JLabel("Expiration Date:");
        expirationDateLabel.setBounds(10, 260, 120, 25);
        panel.add(expirationDateLabel);

        JTextField expirationDateText = new JTextField(20);
        expirationDateText.setBounds(140, 260, 165, 25);
        panel.add(expirationDateText);
        
        JLabel securityCodeLabel = new JLabel("Security Code:");
        securityCodeLabel.setBounds(10, 290, 120, 25);
        panel.add(securityCodeLabel);

        JPasswordField securityCodeText = new JPasswordField(20);
        securityCodeText.setBounds(140, 290, 165, 25);
        panel.add(securityCodeText);
        
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(10, 320, 90, 25);
        panel.add(registerButton);
        
        JLabel unsuccessfulRegisterLabel = new JLabel();
        unsuccessfulRegisterLabel.setBounds(140, 320, 250, 25);
        panel.add(unsuccessfulRegisterLabel);
        
        registerButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		String forename = forenameText.getText();
        		String surname = surnameText.getText();
        		String username = userText.getText();
                char[] password = passwordText.getPassword();
                
                //Presence check for all required fields
                if (forename.equals("") || surname.equals("") || username.equals("") || String.valueOf(password).equals("")) {
                	unsuccessfulRegisterLabel.setText("Please enter all required fields (*)");
                } else {
                	String cardNumber = cardNumberText.getText();
                    String cardHolderName = cardHolderText.getText();
            		String cardType = cardTypeText.getText();
            		String expirationDate = expirationDateText.getText();
                    String securityCode = String.valueOf(securityCodeText.getPassword());
                    
                    //Check if email is already used for another account
            		if(checkUsername(username, connection)) {
            			//Creates new row in Account and BankDetails using the same user ID
            			createNewAccount(forename, surname, username, password, connection);
            			createNewBankAccount(cardNumber, cardHolderName, cardType, expirationDate, securityCode, connection);
            			System.out.println("Account successfully added.");
            			
            			//Redirect to Login page
            			registerFrame.dispose();
    	                Login.main(null);
            		} else {
            			unsuccessfulRegisterLabel.setText("Account already exists with this email.");
            		}
                }
                
            }
        });
    }
    
    private static Boolean checkUsername(String username, Connection connection) {
    	try {
    		//Create SQL statement and query the SQL Database
	    	String sql = "SELECT email FROM Accounts WHERE email = '" + username + "'";
	    	PreparedStatement statement = connection.prepareStatement(sql);
	    	ResultSet resultSet = statement.executeQuery(sql);
	    	
	    	if (resultSet.next()) {
	    			return false;
    		} else {
    			return true;
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    private static void createNewAccount(String forename, String surname, String username, char[] password, Connection connection) {
    	try {
    		int accountID = getLength(connection) + 1;
    		
    		//Create unique, random salt
    		String passwordSalt = HashedPasswordGenerator.getNewSalt();
    		String hashedPassword = HashedPasswordGenerator.hashPassword(password, username, passwordSalt, connection);
    		
    		//Create SQL statement and insert new account into database
    		String sql = "INSERT INTO Accounts (account_id, forename, surname, email, password_hash, password_salt, type) VALUES ('" + accountID	 + "', '" + forename + "', '" + surname + "', '" + username + "', '" + hashedPassword + "', '" + passwordSalt + "', 'CUSTOMER');";
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sql);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    private static void createNewBankAccount(String cardNumber, String cardName, String cardType, String date, String securityCode, Connection connection) {
    	try {
    		int accountID = getLength(connection);
    		
    		//Create SQL statement and insert new bank details into database
    		PreparedStatement insertBankDetailsStmt = connection.prepareStatement(
					"INSERT INTO Bank_Details (customer_id, card_number, holder_name, card_name, expiry_date, security_code) VALUES (?, ?, ?, ?, ?, ?)");
			insertBankDetailsStmt.setInt(1, accountID);
			insertBankDetailsStmt.setString(2, (cardNumber.equals("") ? null : cardNumber));
			insertBankDetailsStmt.setString(3, (cardName.equals("") ? null : cardName));
			insertBankDetailsStmt.setString(4, (cardType.equals("") ? null : cardType));
			insertBankDetailsStmt.setString(5, (date.equals("") ? null : date));
			insertBankDetailsStmt.setString(6, (securityCode.equals("") ? null : securityCode));
			insertBankDetailsStmt.execute();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    private static int getLength(Connection connection) {
    	int count = 0;
    	try {
    		//Create SQL statement and query database
    		String sql = "SELECT * FROM Accounts;";
    		PreparedStatement statement = connection.prepareStatement(sql);
    		statement.executeQuery(sql);
	    	ResultSet resultSet = statement.executeQuery(sql);
	    	
	    	while (resultSet.next()) {
	    		//Count number of accounts in database 
	    		count ++;
	    	}
	    	return count;
    	} catch (SQLException e) {
    		e.printStackTrace();;
    	}
    	return count;
    }
}