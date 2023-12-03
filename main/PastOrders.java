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


public class PastOrders extends JFrame {	
	String selectedOrder;
	ResultSet pendingOrdersResultSet;
	ResultSet orderProductsResultSet;
	ResultSet pendingOrderCustomerAddressResultSet;
	ResultSet pendingOrderCustomerDetailsResultSet;
	Connection connection;
	PreparedStatement getAllPendingOrderstmt;
	PreparedStatement getAllCustomerDetailstmt;
	PreparedStatement getCustomerAddressstmt;
	PreparedStatement getOrderProductstmt;
	
	Vector<String> pendingOrderAddressVector = new Vector<String>();
	Vector<String> pendingOrderCustomerDetailVector = new Vector<String>();
	
	Vector<String> orderProductsVector = new Vector<String>();
	Vector<String> pendingOrdersVector = new Vector<String>();
	JList<String> pendingOrderList = new JList<String>();
	JList<String> currentOrderProductList = new JList<String>();
	JList<String> pendingOrderCustomerDetailList = new JList<String>();
	JList<String> pendingOrderCustomerAddressList = new JList<String>();
	
	String selectedOrderNumber;
	String selectedOrderCost;
	String selectedOrderDate;
	String selectedOrderCustomerID;
	String selectedOrderAddress;
	String selectedOrderCustomerDetails;
	String selectedOrderCustomerName;
	String selectedOrderCustomerEmail;
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
	
    public PastOrders() {
        //GET LIST OF past ORDERS
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllPendingOrderstmt = connection.prepareStatement("SELECT * FROM Orders WHERE (status='PENDING' OR status='FULFILLED' OR status='CONFIRMED') AND customer_id = ? ORDER BY date_ordered");
            getAllPendingOrderstmt.setInt(1, Login.getUserID());
            ResultSet pastOrders= getAllPendingOrderstmt.executeQuery();
            //Get All Orders of status PENDING
            pastOrders = getAllPendingOrderstmt.executeQuery();
            while (pastOrders.next()) {
                String row = pastOrders.getString("order_number") + ", £" + pastOrders.getString("cost") + ", " + pastOrders.getString("date_ordered") + ", " + pastOrders.getString("customer_id");
                pendingOrdersVector.add(row);
            }

            //Set Up Frame
            setTitle("Past Order Queue");
            setSize(1024, 768);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            
            //Create Panels
            JPanel headerPanel = new JPanel(new BorderLayout());
            JPanel pendingOrderPanel = new JPanel(new BorderLayout());
            JPanel fulfillDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel orderPanel = new JPanel(new BorderLayout());
            JPanel orderDetailsPanel = new JPanel(new GridLayout(6,0,0,0));
            //Add Panels To Main Frame
            add(headerPanel, BorderLayout.NORTH);
            add(pendingOrderPanel, BorderLayout.CENTER);
            //Add sub panels to pendingOrderPanel
            pendingOrderPanel.add(orderPanel, BorderLayout.CENTER);
            pendingOrderPanel.add(fulfillDeletePanel, BorderLayout.SOUTH);
            orderPanel.add(orderDetailsPanel, BorderLayout.NORTH);
            
            


            
            // Create Header Panel Items
            //*Back Button
            JButton backButton = new JButton("Back"); //Take user back to Staff Dashboard Screen
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	dispose();
                    Home.main(null);
                }
            });
            //*Trains Of Sheffield Header
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Past Order Queue");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            // Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            //DISPLAYING ORDER DETAILS
            JLabel orderInfo = new JLabel("Current Order Info:");
            orderInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            orderDetailsPanel.add(orderInfo);
            JLabel orderNumber = new JLabel("Order Number: ");
            orderNumber.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderNumber);
            JLabel orderCost = new JLabel("Cost: ");
            orderCost.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCost);
            JLabel orderDate = new JLabel("Date: ");
            orderDate.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderDate);
            JLabel orderCustomerID = new JLabel("Customer ID: ");
            orderCustomerID.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCustomerID);
            
            
            JLabel orderCustomerAddress = new JLabel("Customer Address: ");
            orderCustomerAddress.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCustomerAddress);
            JLabel orderCustomerName = new JLabel("Customer Name: ");
            orderCustomerName.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCustomerName);
            JLabel orderCustomerEmail = new JLabel("Customer Email: ");
            orderCustomerEmail.setFont(new Font("Dialog", Font.BOLD, 14));
            orderDetailsPanel.add(orderCustomerEmail);
            
            JList<String> pendingOrderList = new JList<String>(pendingOrdersVector);
            pendingOrderList.setSelectedIndex(0);
            selectedOrder = pendingOrderList.getSelectedValue();
            if (selectedOrder != null) {
            	//Get selected order number
                int firstCommaIndex = selectedOrder.indexOf(",");
                selectedOrderNumber = selectedOrder.substring(0, firstCommaIndex);
            	//Get values from selected value in list
                int secondCommaIndex = selectedOrder.indexOf(",", firstCommaIndex + 1);
                selectedOrderCost = selectedOrder.substring(firstCommaIndex + 3, secondCommaIndex);
                int thirdCommaIndex = selectedOrder.indexOf(",", secondCommaIndex + 1);
                selectedOrderDate = selectedOrder.substring(secondCommaIndex + 2, thirdCommaIndex);
                selectedOrderCustomerID = selectedOrder.substring(thirdCommaIndex + 2, selectedOrder.length());
            	

            	try {
            		getCustomerAddressstmt = connection.prepareStatement("SELECT * FROM Addresses WHERE address_id='" + selectedOrderCustomerID + "'");
            		getAllCustomerDetailstmt = connection.prepareStatement("SELECT * FROM Accounts WHERE account_id='" + selectedOrderCustomerID + "'");
            		pendingOrderCustomerAddressResultSet = getCustomerAddressstmt.executeQuery();
            		pendingOrderCustomerDetailsResultSet = getAllCustomerDetailstmt.executeQuery();
            		while (pendingOrderCustomerDetailsResultSet.next() && pendingOrderCustomerAddressResultSet.next()) {
	                    String row = pendingOrderCustomerDetailsResultSet.getString("forename") + pendingOrderCustomerDetailsResultSet.getString("surname") + ", " + pendingOrderCustomerDetailsResultSet.getString("email");
	                    String row2 = pendingOrderCustomerAddressResultSet.getString("house_number") + " " + pendingOrderCustomerAddressResultSet.getString("road") + ", " + pendingOrderCustomerAddressResultSet.getString("city") + ", " + pendingOrderCustomerAddressResultSet.getString("postcode");
	                    pendingOrderCustomerDetailVector.add(row);
	                    pendingOrderAddressVector.add(row2);
            		}
            		pendingOrderCustomerDetailList = new JList<String>(pendingOrderCustomerDetailVector);
            		pendingOrderCustomerAddressList = new JList<String>(pendingOrderAddressVector);
            		pendingOrderCustomerDetailList.setSelectedIndex(0);
            		pendingOrderCustomerAddressList.setSelectedIndex(0);
            		selectedOrderAddress = pendingOrderCustomerAddressList.getSelectedValue();
            		selectedOrderCustomerDetails = pendingOrderCustomerDetailList.getSelectedValue();
            	} catch(SQLException e3) {
            		e3.printStackTrace();
            	} 
            	//Parse customer details
            	if (selectedOrderCustomerDetails != null) {
                	int firstCommaIndexCustomerDetails = selectedOrderCustomerDetails.indexOf(",");
                	selectedOrderCustomerName = selectedOrderCustomerDetails.substring(0, firstCommaIndexCustomerDetails);
                	selectedOrderCustomerEmail = selectedOrderCustomerDetails.substring(firstCommaIndexCustomerDetails + 1, selectedOrderCustomerDetails.length());
            	}

            	
            	
            	
            	
            	

                //Set the text to the newly retrieved values
                orderNumber.setText("Order Number: " + selectedOrderNumber);
                orderCost.setText("Cost: £" + selectedOrderCost);
                orderDate.setText("Date: " + selectedOrderDate );
                orderCustomerID.setText("Customer ID: " + selectedOrderCustomerID);
                orderCustomerAddress.setText("Customer Address: " + selectedOrderAddress);
                orderCustomerName.setText("Customer Name: " + selectedOrderCustomerName);
                orderCustomerEmail.setText("Customer Email: " + selectedOrderCustomerEmail);
                
                try {
                	//SELECT ALL FROM ORDER LINE WHERE ORDER_NUMBER = selectedOrderNumber
                    getOrderProductstmt = connection.prepareStatement("SELECT * FROM Order_Lines WHERE order_number='" + selectedOrderNumber + "'");
                    //Get All Orders of status PENDING
                    orderProductsResultSet = getOrderProductstmt.executeQuery();
                    while (orderProductsResultSet.next()) {
                        String row = orderProductsResultSet.getString("product_code") + ", " + orderProductsResultSet.getString("quantity");
                        orderProductsVector.add(row);
                    }
                    currentOrderProductList = new JList<String>(orderProductsVector);
                	repaint();
                } catch(SQLException e1) {
                	e1.printStackTrace();
                }
            }
            //Add Pending Orders Items To Panel
            JScrollPane pendingOrderScrollableList = new JScrollPane(pendingOrderList);
            JLabel pendingOrderLabel = new JLabel("Past Orders:");
            pendingOrderPanel.add(pendingOrderLabel, BorderLayout.NORTH);
            pendingOrderPanel.add(pendingOrderScrollableList, BorderLayout.WEST);
            orderPanel.add(currentOrderProductList, BorderLayout.CENTER);

            setVisible(true);
        } catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    }
}    
    