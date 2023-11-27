import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;

public class ManagerDashboard extends JFrame {	
	String selectedValue;
	String selectedValueEmail;
	String inputtedEmail;
    public ManagerDashboard() {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //GET LIST OF STAFF ACCOUNTS AND CREATE A LIST WITH THEM
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            Statement getAllStaffstmt = connection.createStatement();
            //Get All Accounts of Type STAFF
            ResultSet staffListResultSet = getAllStaffstmt.executeQuery("SELECT * FROM Accounts WHERE type='STAFF'");

            Vector<String> staffVector = new Vector<String>();
            while (staffListResultSet.next()) {
                String row = staffListResultSet.getString("email") + ", " + staffListResultSet.getString("forename") + ", " + staffListResultSet.getString("surname");
                staffVector.add(row);
            }
            JList<String> staffList = new JList<String>(staffVector);
            staffList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            staffList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedValue = staffList.getSelectedValue();
                        selectedValueEmail = selectedValue.substring(0, selectedValue.indexOf(","));
                        System.out.println(selectedValue);
                        System.out.println(selectedValueEmail);
                    }
                }
            });
            //Add List to Scrollable Pane
            JScrollPane staffScrollableList = new JScrollPane(staffList);

            //Set Up Frame
            setTitle("Manager Dashboard");
            setSize(1024, 768);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            // Create Header, Promote User, Demote User Panels
            JPanel headerPanel = new JPanel(new BorderLayout());
            JPanel demoteUserPanel = new JPanel(new BorderLayout());
            JPanel demotePanel = new JPanel(new BorderLayout());
            JPanel promoteUserPanel = new JPanel(new BorderLayout());
            JPanel promotePanel = new JPanel(new BorderLayout());
            // Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            add(demoteUserPanel, BorderLayout.CENTER);
            add(promoteUserPanel, BorderLayout.SOUTH);
            promoteUserPanel.add(promotePanel, BorderLayout.CENTER);
            demoteUserPanel.add(demotePanel, BorderLayout.CENTER);
            
            // Create Header Panel Items
            //*Back Button
            JButton backButton = new JButton("Back"); //Take user back to Staff Dashboard Screen
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StaffDashboard staffDashboardScreen = new StaffDashboard();
                    staffDashboardScreen.setLocationRelativeTo(null);
                    staffDashboardScreen.setVisible(true);
                    setVisible(false);
                }
            });
            //*Trains Of Sheffield Header
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Manager Dashboard");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            // Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            
            
            //Create Demote User Panel Items
            JLabel customerListLabel = new JLabel("Staff List:");
            JButton demoteSelectedUserButton = new JButton("Demote User");
            demoteSelectedUserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
						PreparedStatement removeUserFromStaffstmt = connection.prepareStatement("UPDATE Accounts SET type = 'CUSTOMER' WHERE email ='" + selectedValueEmail + "'");
			            int rowsUpdated = removeUserFromStaffstmt.executeUpdate();
			            if (rowsUpdated > 0) {
			                System.out.println("An existing user was updated successfully!");
			            }
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
                }
            });
            //Add Items to Demote User Panel 
            demoteUserPanel.add(customerListLabel, BorderLayout.NORTH);
            demoteUserPanel.add(staffScrollableList, BorderLayout.WEST);
            demotePanel.add(demoteSelectedUserButton, BorderLayout.CENTER);

            
            
            
            
            
            
            
            
            //Create Promote User Panel Items
            JTextField promoteCustomerInputBox = new JTextField(36);
            JLabel promoteCustomerListLabel = new JLabel("Enter Email of Customer to Promote:");
            JButton promoteCustomerButton = new JButton("Promote User");
            promoteCustomerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
                		System.out.println(promoteCustomerInputBox.getText());
						PreparedStatement promoteUsertmt = connection.prepareStatement("UPDATE Accounts SET type = 'STAFF' WHERE email ='" + promoteCustomerInputBox.getText() + "' AND type='CUSTOMER'");
			            int rowsUpdated = promoteUsertmt.executeUpdate();
			            if (rowsUpdated > 0) {
			                System.out.println("An existing user was updated successfully!");
			            }
			            else {
			            	System.out.println("Failed");
			            }
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
                }
            });
            
            
            
            
            
            
            
            //Add Items to Promote User Panel
            promoteUserPanel.add(promoteCustomerInputBox, BorderLayout.WEST);
            promoteUserPanel.add(promoteCustomerListLabel, BorderLayout.NORTH);
            promoteUserPanel.add(promoteCustomerButton, BorderLayout.SOUTH);

            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
    