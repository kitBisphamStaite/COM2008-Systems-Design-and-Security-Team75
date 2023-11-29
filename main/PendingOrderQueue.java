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
import javax.swing.DefaultListModel;


public class PendingOrderQueue extends JFrame {	
	String selectedOrder;
	ResultSet pendingOrdersResultSet;
	Connection connection;
	Statement getAllPendingOrderstmt;
	Vector<String> pendingOrdersVector = new Vector<String>();
	JList<String> pendingOrderList = new JList<String>();
	String selectedOrderNumber;
	String selectedOrderCost;
	String selectedOrderDate;
	String selectedOrderCustomerID;
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
	
    public PendingOrderQueue() {
        //GET LIST OF PENDING ORDERS
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllPendingOrderstmt = connection.createStatement();
            //Get All Orders of status PENDING
            pendingOrdersResultSet = getAllPendingOrderstmt.executeQuery("SELECT * FROM Orders WHERE status='PENDING' ORDER BY date_ordered");
            while (pendingOrdersResultSet.next()) {
                String row = pendingOrdersResultSet.getString("order_number") + ", £" + pendingOrdersResultSet.getString("cost") + ", " + pendingOrdersResultSet.getString("date_ordered") + ", " + pendingOrdersResultSet.getString("customer_id");
                pendingOrdersVector.add(row);
            }

            //Set Up Frame
            setTitle("Pending Order Queue");
            setSize(1024, 768);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            
            //Create Panels
            JPanel headerPanel = new JPanel(new BorderLayout());
            JPanel pendingOrderPanel = new JPanel(new BorderLayout());
            JPanel fulfillDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel orderPanel = new JPanel(new GridLayout(7,0));
            JPanel orderDetailsPanel = new JPanel(new GridLayout(5,0,0,2));
            //Add Panels To Main Frame
            add(headerPanel, BorderLayout.NORTH);
            add(pendingOrderPanel, BorderLayout.CENTER);
            //Add sub panels to pendingOrderPanel
            pendingOrderPanel.add(orderPanel, BorderLayout.CENTER);
            pendingOrderPanel.add(fulfillDeletePanel, BorderLayout.SOUTH);
            orderPanel.add(orderDetailsPanel);
            
            


            
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
                        
            
            JList<String> pendingOrderList = new JList<String>(pendingOrdersVector);
            pendingOrderList.setSelectedIndex(0);
            selectedOrder = pendingOrderList.getSelectedValue();
            if (selectedOrder != null) {
                int firstCommaIndex = selectedOrder.indexOf(",");
                selectedOrderNumber = selectedOrder.substring(0, firstCommaIndex);
                int secondCommaIndex = selectedOrder.indexOf(",", firstCommaIndex + 1);
                selectedOrderCost = selectedOrder.substring(firstCommaIndex + 3, secondCommaIndex);
                int thirdCommaIndex = selectedOrder.indexOf(",", secondCommaIndex + 1);
                selectedOrderDate = selectedOrder.substring(secondCommaIndex + 2, thirdCommaIndex);
                selectedOrderCustomerID = selectedOrder.substring(thirdCommaIndex + 2, selectedOrder.length());
            }

            
            

            
            
            
            
            
            
            JScrollPane pendingOrderScrollableList = new JScrollPane(pendingOrderList);
            //DISPLAYING ORDER DETAILS
            JLabel orderInfo = new JLabel("Current Order Info:");
            orderInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            orderDetailsPanel.add(orderInfo);
            JLabel orderNumber = new JLabel("Order Number: " + selectedOrderNumber);
            orderNumber.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderNumber);
            JLabel orderCost = new JLabel("Cost: £" + selectedOrderCost);
            orderCost.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCost);
            JLabel orderDate = new JLabel("Date: " + selectedOrderDate);
            orderDate.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderDate);
            JLabel orderCustomerID = new JLabel("CustomerID: " + selectedOrderCustomerID);
            orderCustomerID.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCustomerID);
            

            
            
            
            
            JButton deleteOrderButton = new JButton("Delete Order");
            deleteOrderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
                			if (selectedOrder != null) {
                    			PreparedStatement deleteOrderstmt = connection.prepareStatement("DELETE FROM Orders WHERE order_number='" + selectedOrderNumber + "'");
                    			int rowsUpdated = deleteOrderstmt.executeUpdate();
                    			if (rowsUpdated == 1) {
        			                System.out.println("Order successfully deleted.");
        			                pendingOrdersVector.clear();
        			                pendingOrderList.clearSelection();
        			                getAllPendingOrderstmt = connection.createStatement();
        			                //Get All Orders of status PENDING
        			                pendingOrdersResultSet = getAllPendingOrderstmt.executeQuery("SELECT * FROM Orders WHERE status='PENDING' ORDER BY date_ordered");
        			                while (pendingOrdersResultSet.next()) {
        			                    String row = pendingOrdersResultSet.getString("order_number") + ", £" + pendingOrdersResultSet.getString("cost") + ", " + pendingOrdersResultSet.getString("date_ordered") + ", " + pendingOrdersResultSet.getString("customer_id");
        			                    pendingOrdersVector.add(row);
        			                }
        			                JList<String> pendingOrderList = new JList<String>(pendingOrdersVector);
        			                pendingOrderList.setSelectedIndex(0);
        			                selectedOrder = pendingOrderList.getSelectedValue();
        			                if (selectedOrder != null) {
        				                int firstCommaIndex = selectedOrder.indexOf(",");
        				                selectedOrderNumber = selectedOrder.substring(0, firstCommaIndex);
        				                int secondCommaIndex = selectedOrder.indexOf(",", firstCommaIndex + 1);
        				                selectedOrderCost = selectedOrder.substring(firstCommaIndex + 3, secondCommaIndex);
        				                int thirdCommaIndex = selectedOrder.indexOf(",", secondCommaIndex + 1);
        				                selectedOrderDate = selectedOrder.substring(secondCommaIndex + 2, thirdCommaIndex);
        				                selectedOrderCustomerID = selectedOrder.substring(thirdCommaIndex + 2, selectedOrder.length());
        				                orderNumber.setText("Order Number: " + selectedOrderNumber);
        				                orderCost.setText("Cost: £" + selectedOrderCost);
        				                orderDate.setText("Date: " + selectedOrderDate);
        				                orderCustomerID.setText("CustomerID: " + selectedOrderCustomerID);
        				                
        				                repaint();
        			                } else {
        				                orderNumber.setText("Order Number: ");
        				                orderCost.setText("Cost: ");
        				                orderDate.setText("Date: ");
        				                orderCustomerID.setText("CustomerID: ");
        			                	repaint();
        			                }
        			                JOptionPane.showMessageDialog(null, "Successfully deleted order.");
                    			}
                    			
                			} else {
                				repaint();
                				JOptionPane.showMessageDialog(null, "No orders currently in queue.");
                			}

			            }
					catch (SQLException e1) {
						e1.printStackTrace();
					}
                }
            });
            
            JButton fulfillOrderButton = new JButton("Fulfill Order");
            fulfillOrderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
            			if (selectedOrder != null) {
                			PreparedStatement fulfillOrderstmt = connection.prepareStatement("UPDATE Orders SET status = 'FULFILLED' WHERE order_number ='" + selectedOrderNumber + "'");
                			int rowsUpdated = fulfillOrderstmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Order successfully fulfilled.");
    			                pendingOrdersVector.clear();
    			                pendingOrderList.clearSelection();
    			                getAllPendingOrderstmt = connection.createStatement();
    			                //Get All Orders of status PENDING
    			                pendingOrdersResultSet = getAllPendingOrderstmt.executeQuery("SELECT * FROM Orders WHERE status='PENDING' ORDER BY date_ordered");
    			                while (pendingOrdersResultSet.next()) {
    			                    String row = pendingOrdersResultSet.getString("order_number") + ", £" + pendingOrdersResultSet.getString("cost") + ", " + pendingOrdersResultSet.getString("date_ordered") + ", " + pendingOrdersResultSet.getString("customer_id");
    			                    pendingOrdersVector.add(row);
    			                }
    			                JList<String> pendingOrderList = new JList<String>(pendingOrdersVector);
    			                pendingOrderList.setSelectedIndex(0);
    			                selectedOrder = pendingOrderList.getSelectedValue();
    			                if (selectedOrder != null) {
    				                int firstCommaIndex = selectedOrder.indexOf(",");
    				                selectedOrderNumber = selectedOrder.substring(0, firstCommaIndex);
    				                int secondCommaIndex = selectedOrder.indexOf(",", firstCommaIndex + 1);
    				                selectedOrderCost = selectedOrder.substring(firstCommaIndex + 3, secondCommaIndex);
    				                int thirdCommaIndex = selectedOrder.indexOf(",", secondCommaIndex + 1);
    				                selectedOrderDate = selectedOrder.substring(secondCommaIndex + 2, thirdCommaIndex);
    				                selectedOrderCustomerID = selectedOrder.substring(thirdCommaIndex + 2, selectedOrder.length());
    				                orderNumber.setText("Order Number: " + selectedOrderNumber);
    				                orderCost.setText("Cost: £" + selectedOrderCost);
    				                orderDate.setText("Date: " + selectedOrderDate);
    				                orderCustomerID.setText("CustomerID: " + selectedOrderCustomerID);
    				                repaint();
    			                } else {
    				                orderNumber.setText("Order Number: ");
    				                orderCost.setText("Cost: ");
    				                orderDate.setText("Date: ");
    				                orderCustomerID.setText("CustomerID: ");
    			                	repaint();
    			                }
    			                JOptionPane.showMessageDialog(null, "Successfully fulfilled order.");
                			}
            			} else {
            				repaint();
            				JOptionPane.showMessageDialog(null, "No orders currently in queue.");
            			}
                	}
                	catch (SQLException e2) {
                	e2.printStackTrace();
                	}
                }
            });
            

            
            
            
            //Add buttons to fullfillDelete panel
            fulfillDeletePanel.add(fulfillOrderButton);
            fulfillDeletePanel.add(deleteOrderButton);
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
    