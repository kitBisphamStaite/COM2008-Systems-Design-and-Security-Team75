import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class EditBank {
	
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
        registerFrame.setSize(410, 450);

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
    		ResultSet bankDetails = getBankDetailsStmt.executeQuery();
    		return bankDetails;
    		
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    private static ResultSet getAddressDetails(Connection connection) {
    	try {
    		Integer accountId = Login.getUserID();
			PreparedStatement getAddressStmt = connection.prepareStatement("SELECT * FROM Addresses WHERE account_id = ?");
    		getAddressStmt.setInt(1, accountId);
    		ResultSet address = getAddressStmt.executeQuery();
    		return address;
    		
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
	        ResultSet address = getAddressDetails(connection);
            
        	JLabel cardNumberLabel = new JLabel("Card Number:");
            cardNumberLabel.setBounds(10, 20, 120, 25);
            panel.add(cardNumberLabel);
            
            JTextField cardNumberText = new JTextField(20);
            cardNumberText.setBounds(140, 20, 200, 25);
            panel.add(cardNumberText);
            
            JLabel cardHolderLabel = new JLabel("Card Holder Name:");
            cardHolderLabel.setBounds(10, 50, 120, 25);
            panel.add(cardHolderLabel);

            JTextField cardHolderText = new JTextField(20);
            cardHolderText.setBounds(140, 50, 200, 25);
            panel.add(cardHolderText);
            
            JLabel cardTypeLabel = new JLabel("Type of card:");
            cardTypeLabel.setBounds(10, 80, 120, 25);
            panel.add(cardTypeLabel);

            JTextField cardTypeText = new JTextField(20);
            cardTypeText.setBounds(140, 80, 200, 25);
            panel.add(cardTypeText);
            
            JLabel expirationDateLabel = new JLabel("Expiration Date:");
            expirationDateLabel.setBounds(10, 140, 120, 25);
            panel.add(expirationDateLabel);

            JTextField expirationDateText = new JTextField(20);
            expirationDateText.setBounds(140, 140, 200, 25);
            panel.add(expirationDateText);
            
            JLabel securityCodeLabel = new JLabel("Security Code:");
            securityCodeLabel.setBounds(10, 170, 120, 25);
            panel.add(securityCodeLabel);

            JPasswordField securityCodeText = new JPasswordField(20);
            securityCodeText.setBounds(140, 170, 200, 25);
            panel.add(securityCodeText);
            
            
            JLabel houseNumberLabel = new JLabel("House Number:");
            houseNumberLabel.setBounds(10, 230, 120, 25);
            panel.add(houseNumberLabel);
            
            JTextField houseNumberText = new JTextField(20);
            houseNumberText.setBounds(140, 230, 200, 25);
            panel.add(houseNumberText);
            
            JLabel roadLabel = new JLabel("Road:");
            roadLabel.setBounds(10, 260, 120, 25);
            panel.add(roadLabel);
            
            JTextField roadText = new JTextField(20);
            roadText.setBounds(140, 260, 200, 25);
            panel.add(roadText);
            
            JLabel cityLabel = new JLabel("City:");
            cityLabel.setBounds(10, 290, 120, 25);
            panel.add(cityLabel);
            
            JTextField cityText = new JTextField(20);
            cityText.setBounds(140, 290, 200, 25);
            panel.add(cityText);
            
            JLabel postCodeLable = new JLabel("Post Code:");
            postCodeLable.setBounds(10, 310, 120, 25);
            panel.add(postCodeLable);
            
            JTextField postCodeText = new JTextField(20);
            postCodeText.setBounds(140, 310, 200, 25);
            panel.add(postCodeText);
            
            
            
            if (bankDetails.next()) {
                cardNumberText.setText(bankDetails.getString("card_number"));
                cardHolderText.setText(bankDetails.getString("holder_name"));
                cardTypeText.setText(bankDetails.getString("card_name"));
                expirationDateText.setText(bankDetails.getString("expiry_date"));
                securityCodeText.setText(bankDetails.getString("security_code"));
            }
            if (address.next()) {
            	houseNumberText.setText(address.getString("house_number"));
            	roadText.setText(address.getString("road"));
            	cityText.setText(address.getString("city"));
            	postCodeText.setText(address.getString("postcode"));
            }
            
            JButton editButton = new JButton("Edit Account");
            editButton.setBounds(10, 340, 110, 25);
            panel.add(editButton);
            
            editButton.addActionListener(new ActionListener() {
            	@Override
                public void actionPerformed(ActionEvent e) {
            		
        			String cardNumber = cardNumberText.getText();
                    String cardHolderName = cardHolderText.getText();
            		String cardType = cardTypeText.getText();
            		String expirationDate = expirationDateText.getText();
                    String securityCode = String.valueOf(securityCodeText.getPassword());
                    
                    String houseNumber = houseNumberText.getText();
                    String road = roadText.getText();
                    String city = cityText.getText();
                    String postcode = postCodeText.getText();
                    
                    
                    if (!(cardNumber.equals("") ||
                    		cardHolderName.equals("") ||
                    		cardType.equals("")||
                    		expirationDate.equals("")||
                    		securityCode.equals(""))
                    		) {
                    	try {
                    		
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
                    		
                    		ResultSet addressDetails = getAddressDetails(connection);
                    		
                    		if (addressDetails.next()) {
                    			PreparedStatement updateAddressStmt = connection.prepareStatement(
                        				"UPDATE Addresses SET house_number = ?, road = ?, city = ?, postcode = ? WHERE (account_id = ?)");
                    			updateAddressStmt.setString(1, (houseNumber.equals("") ? null : houseNumber));
                    			updateAddressStmt.setString(2, (road.equals("") ? null : road));
                    			updateAddressStmt.setString(3, (city.equals("") ? null : city));
                    			updateAddressStmt.setString(4, (postcode.equals("") ? null : postcode));
                    			updateAddressStmt.setInt(5, Login.getUserID());
                    			updateAddressStmt.execute();
                    		}else {
                    			PreparedStatement insertAddressStmt = connection.prepareStatement(
                    					"INSERT INTO Addresses (account_id, house_number, road, city, postcode) VALUES (?, ?, ?, ?, ?)");
                    			insertAddressStmt.setInt(1, Login.getUserID());
                    			insertAddressStmt.setString(2, (houseNumber.equals("") ? null : houseNumber));
                    			insertAddressStmt.setString(3, (road.equals("") ? null : road));
                    			insertAddressStmt.setString(4, (city.equals("") ? null : city));
                    			insertAddressStmt.setString(5, (postcode.equals("") ? null : postcode));
                    			insertAddressStmt.execute();
                    		}
                    		
                    		
                    		System.out.println("Saved");
							registerFrame.dispose();
							ViewBasket.main(null);
                    		
                    	}catch (SQLException ex) {
                    		 ex.printStackTrace();
                        }
                    }else {
                    	System.out.println("Failed");
                    }
            	
                
    	        }
    	    });
            JLabel unsuccessfulRegisterLabel = new JLabel();
            unsuccessfulRegisterLabel.setBounds(200, 290, 200, 25);
            panel.add(unsuccessfulRegisterLabel);
            
            JButton homeButton = new JButton("Back");
            homeButton.setBounds(10, 370, 110, 25);
            panel.add(homeButton);
            
            homeButton.addActionListener(new ActionListener() {
            	@Override
            	 public void actionPerformed(ActionEvent e) {
            		registerFrame.dispose();
            		ViewBasket.main(null);
            	}
            });
            	
            
        }catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
        
	}
}