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

public class PendingOrderQueue extends JFrame {	
	String selectedValue;
	String inputtedEmail;
    public PendingOrderQueue() {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //GET LIST OF STAFF ACCOUNTS AND CREATE A LIST WITH THEM
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            Statement getAllPendingOrderstmt = connection.createStatement();
            //Get All Accounts of Type STAFF
            ResultSet pendingOrdersResultSet = getAllPendingOrderstmt.executeQuery("SELECT * FROM Orders WHERE status='PENDING'");

            Vector<String> pendingOrdersVector = new Vector<String>();
            while (pendingOrdersResultSet.next()) {
                String row = pendingOrdersResultSet.getString("order_number") + ", £" + pendingOrdersResultSet.getString("cost") + ", " + pendingOrdersResultSet.getString("date_ordered");
                pendingOrdersVector.add(row);
            }
            JList<String> pendingOrderList = new JList<String>(pendingOrdersVector);
            pendingOrderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            pendingOrderList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedValue = pendingOrderList.getSelectedValue();
                        System.out.println(selectedValue);
                    }
                }
            });
            //Add List to Scrollable Pane
            JScrollPane pendingOrderScrollableList = new JScrollPane(pendingOrderList);

            //Set Up Frame
            setTitle("Manager Dashboard");
            setSize(1024, 768);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            // Create Header, Promote User, Demote User Panels
            JPanel headerPanel = new JPanel(new BorderLayout());
            JPanel pendingOrderPanel = new JPanel(new BorderLayout());
            
            
            JPanel promoteUserPanel = new JPanel(new BorderLayout());
            JPanel promotePanel = new JPanel(new BorderLayout());
            // Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            
            
            
            
            add(pendingOrderPanel, BorderLayout.CENTER);
            add(promoteUserPanel, BorderLayout.SOUTH);
            promoteUserPanel.add(promotePanel, BorderLayout.CENTER);
            pendingOrderPanel.add(promotePanel, BorderLayout.CENTER);
            
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
            
            
            
            
            
            
            
            
            
            //Create Pending Orders Items
            JLabel pendingOrderLabel = new JLabel("Pending Orders:");
            //Add Pending Orders Items To Panel
            pendingOrderPanel.add(pendingOrderLabel, BorderLayout.NORTH);
            pendingOrderPanel.add(pendingOrderScrollableList, BorderLayout.WEST);

            
            
            
            
            
            setVisible(true);
        } catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    }
}    
    