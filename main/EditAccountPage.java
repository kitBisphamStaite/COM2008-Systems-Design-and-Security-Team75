import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class EditAccountPage {
	
    public static void main(String[] args) {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
            createEditAccountFrame(connection);
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }
    }

    private static void createEditAccountFrame(Connection connection) {
    	JFrame registerFrame = new JFrame("Edit Account Page");
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setSize(410, 370);

        JPanel panel = new JPanel();
        registerFrame.add(panel);
        placeComponents(registerFrame, panel, connection);

        registerFrame.setVisible(true);
    }
    
    private static ResultSet getAccountDetails(Connection connection) {
    	try {
    		Integer accountId = Login.getUserID();
    		PreparedStatement getAccDetailsStmt = connection.prepareStatement("SELECT * FROM Accounts WHERE account_id = ?");
    		getAccDetailsStmt.setInt(1, accountId);
    		ResultSet AccDetails = getAccDetailsStmt.executeQuery();
    		return AccDetails;
    		
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    private static ResultSet getBankDetails(Connection connection) {
    	try {
    		Integer accountId = Login.getUserID();
    		PreparedStatement getBankDetailsStmt = connection.prepareStatement("SELECT * FROM Bank_Details WHERE customer_id = ?");
    		getBankDetailsStmt.setInt(1, accountId);
    		ResultSet BankDetails = getBankDetailsStmt.executeQuery();
    		return BankDetails;
    		
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    private static void placeComponents(JFrame registerFrame, JPanel panel, Connection connection) {
        panel.setLayout(null);
        try {
	        ResultSet accountDetails = getAccountDetails(connection);
	        ResultSet bankDetails = getBankDetails(connection);
	        
        	JLabel forenameLabel = new JLabel("Forename:");
            forenameLabel.setBounds(10, 20, 80, 25);
            panel.add(forenameLabel);

            JTextField forenameText = new JTextField(20);
            forenameText.setBounds(140, 20, 200, 25);
            panel.add(forenameText);
            
            JLabel surnameLabel = new JLabel("Surname:");
            surnameLabel.setBounds(10, 50, 80, 25);
            panel.add(surnameLabel);

            JTextField surnameText = new JTextField(20);
            surnameText.setBounds(140, 50, 200, 25);
            panel.add(surnameText);

            JLabel userLabel = new JLabel("Email:");
            userLabel.setBounds(10, 80, 80, 25);
            panel.add(userLabel);

            JTextField userText = new JTextField(20);
            userText.setBounds(140, 80, 200, 25);
            panel.add(userText);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setBounds(10, 110, 80, 25);
            panel.add(passwordLabel);

            JPasswordField passwordText = new JPasswordField(20);
            passwordText.setBounds(140, 110, 200, 25);
            panel.add(passwordText);
	        
	        JLabel cardNumberLabel = new JLabel("Card Number:");
            cardNumberLabel.setBounds(10, 140, 120, 25);
            panel.add(cardNumberLabel);
            
            JTextField cardNumberText = new JTextField(20);
            cardNumberText.setBounds(140, 140, 200, 25);
            panel.add(cardNumberText);
            
            JLabel cardHolderLabel = new JLabel("Card Holder Name:");
            cardHolderLabel.setBounds(10, 170, 120, 25);
            panel.add(cardHolderLabel);

            JTextField cardHolderText = new JTextField(20);
            cardHolderText.setBounds(140, 170, 200, 25);
            panel.add(cardHolderText);
            
            JLabel cardTypeLabel = new JLabel("Type of card:");
            cardTypeLabel.setBounds(10, 200, 120, 25);
            panel.add(cardTypeLabel);

            JTextField cardTypeText = new JTextField(20);
            cardTypeText.setBounds(140, 200, 200, 25);
            panel.add(cardTypeText);
            
            JLabel expirationDateLabel = new JLabel("Expiration Date:");
            expirationDateLabel.setBounds(10, 230, 120, 25);
            panel.add(expirationDateLabel);

            JTextField expirationDateText = new JTextField(20);
            expirationDateText.setBounds(140, 230, 200, 25);
            panel.add(expirationDateText);
            
            JLabel securityCodeLabel = new JLabel("Security Code:");
            securityCodeLabel.setBounds(10, 260, 120, 25);
            panel.add(securityCodeLabel);

            JPasswordField securityCodeText = new JPasswordField(20);
            securityCodeText.setBounds(140, 260, 200, 25);
            panel.add(securityCodeText);
            
            JButton editButton = new JButton("Edit Account");
            editButton.setBounds(10, 290, 110, 25);
            panel.add(editButton);
            
            if (accountDetails.next()) {
                forenameText.setText(accountDetails.getString("forename"));
                surnameText.setText(accountDetails.getString("surname"));
                userText.setText(accountDetails.getString("email"));
            }
            if (bankDetails.next()) {
                cardNumberText.setText(bankDetails.getString("card_number"));
                cardHolderText.setText(bankDetails.getString("holder_name"));
                cardTypeText.setText(bankDetails.getString("card_name"));
                expirationDateText.setText(bankDetails.getString("expiry_date"));
                securityCodeText.setText(bankDetails.getString("security_code"));
            }
            
            JLabel unsuccessfulRegisterLabel = new JLabel();
            unsuccessfulRegisterLabel.setBounds(200, 290, 200, 25);
            panel.add(unsuccessfulRegisterLabel);
            
            editButton.addActionListener(new ActionListener() {
            	@Override
                public void actionPerformed(ActionEvent e) {
            		String forename = forenameText.getText();
            		String surname = surnameText.getText();
            		String email = userText.getText();
                    char[] password = passwordText.getPassword();
                    
                    String cardNumber = cardNumberText.getText();
                    String cardHolderName = cardHolderText.getText();
            		String cardType = cardTypeText.getText();
            		String expirationDate = expirationDateText.getText();
                    String securityCode = String.valueOf(securityCodeText.getPassword());
                    
                    if (!(forename.equals("") ||
                    		surname.equals("") ||
                    		email.equals(""))
                    		) {
                    	try {
                    		PreparedStatement updateAccountsStmt = connection.prepareStatement(
                    				"UPDATE Accounts SET forename = ?, surname = ?, email = ? WHERE (account_id = ?)");
                    		updateAccountsStmt.setString(1, forename);
                    		updateAccountsStmt.setString(2, surname);
                    		updateAccountsStmt.setString(3, email);
                    		updateAccountsStmt.setInt(4, Login.getUserID());
                    		updateAccountsStmt.execute();
                    		
                    		
                    		ResultSet bankDetails = getBankDetails(connection);
                    		if (bankDetails.next()) {
                    			PreparedStatement updateBankStmt = connection.prepareStatement(
                        				"UPDATE Bank_Details SET card_number = ?, holder_name = ?, card_name = ?, expiry_date = ?, security_code = ? WHERE (customer_id = ?)");
                        		updateBankStmt.setString(1, (cardNumber.equals("") ? null : cardNumber));
                        		updateBankStmt.setString(2, (cardHolderName.equals("") ? null : cardHolderName));
                        		updateBankStmt.setString(3, (cardType.equals("") ? null : cardType));
                        		updateBankStmt.setString(4, (expirationDate.equals("") ? null : expirationDate));
                        		updateBankStmt.setString(5, (securityCode.equals("") ? null : securityCode));
                        		updateBankStmt.setInt(6, Login.getUserID());
                        		updateBankStmt.execute();
                    		}else {
                    			PreparedStatement insertBankDetailsStmt = connection.prepareStatement(
                    					"INSERT INTO Bank_Details (customer_id, card_number, holder_name, card_name, expiry_date, security_code) VALUES (?, ?, ?, ?, ?, ?)");
                    			insertBankDetailsStmt.setInt(1, Login.getUserID());
                    			insertBankDetailsStmt.setString(2, (cardNumber.equals("") ? null : cardNumber));
                    			insertBankDetailsStmt.setString(3, (cardHolderName.equals("") ? null : cardHolderName));
                    			insertBankDetailsStmt.setString(4, (cardType.equals("") ? null : cardType));
                    			insertBankDetailsStmt.setString(5, (expirationDate.equals("") ? null : expirationDate));
                    			insertBankDetailsStmt.setString(6, (securityCode.equals("") ? null : securityCode));
                    			insertBankDetailsStmt.execute();
                    		}
                    		
                    		
                    		System.out.println("Saved");
                    		
                    	}catch (SQLException ex) {
                    		 ex.printStackTrace();
                        }
                    }else {
                    	System.out.println("Failed");
                    }
            	
                
    	        }
    	    });
        }catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
        
	}
}