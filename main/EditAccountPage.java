import javax.swing.*;
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

    private static void placeComponents(JFrame registerFrame, JPanel panel, Connection connection) {
        panel.setLayout(null);

        JLabel forenameLabel = new JLabel("Forename:");
        forenameLabel.setBounds(10, 20, 80, 25);
        panel.add(forenameLabel);

        JTextField forenameText = new JTextField(20);
        forenameText.setBounds(140, 20, 165, 25);
        panel.add(forenameText);
        
        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setBounds(10, 50, 80, 25);
        panel.add(surnameLabel);

        JTextField surnameText = new JTextField(20);
        surnameText.setBounds(140, 50, 165, 25);
        panel.add(surnameText);

        JLabel userLabel = new JLabel("Email:");
        userLabel.setBounds(10, 80, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(140, 80, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 110, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(140, 110, 165, 25);
        panel.add(passwordText);
        
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setBounds(10, 140, 120, 25);
        panel.add(cardNumberLabel);

        JTextField cardNumberText = new JTextField(20);
        cardNumberText.setBounds(140, 140, 165, 25);
        panel.add(cardNumberText);
        
        JLabel cardHolderLabel = new JLabel("Card Holder Name:");
        cardHolderLabel.setBounds(10, 170, 120, 25);
        panel.add(cardHolderLabel);

        JTextField cardHolderText = new JTextField(20);
        cardHolderText.setBounds(140, 170, 165, 25);
        panel.add(cardHolderText);
        
        JLabel cardTypeLabel = new JLabel("Type of card:");
        cardTypeLabel.setBounds(10, 200, 120, 25);
        panel.add(cardTypeLabel);

        JTextField cardTypeText = new JTextField(20);
        cardTypeText.setBounds(140, 200, 165, 25);
        panel.add(cardTypeText);
        
        JLabel expirationDateLabel = new JLabel("Expiration Date:");
        expirationDateLabel.setBounds(10, 230, 120, 25);
        panel.add(expirationDateLabel);

        JTextField expirationDateText = new JTextField(20);
        expirationDateText.setBounds(140, 230, 165, 25);
        panel.add(expirationDateText);
        
        JLabel securityCodeLabel = new JLabel("Security Code:");
        securityCodeLabel.setBounds(10, 260, 120, 25);
        panel.add(securityCodeLabel);

        JPasswordField securityCodeText = new JPasswordField(20);
        securityCodeText.setBounds(140, 260, 165, 25);
        panel.add(securityCodeText);
        
        JButton registerButton = new JButton("Edit Account");
        registerButton.setBounds(10, 290, 110, 25);
        panel.add(registerButton);
        
        JLabel unsuccessfulRegisterLabel = new JLabel();
        unsuccessfulRegisterLabel.setBounds(200, 290, 200, 25);
        panel.add(unsuccessfulRegisterLabel);
        
        registerButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		String forename = forenameText.getText();
        		String surname = surnameText.getText();
        		String username = userText.getText();
                char[] password = passwordText.getPassword();
                
                String cardNumber = cardNumberText.getText();
                String cardHolderName = cardHolderText.getText();
        		String cardType = cardTypeText.getText();
        		String expirationDate = expirationDateText.getText();
                String securityCode = String.valueOf(securityCodeText.getPassword());
                
        	
            
	        }
	    });
	}
}