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
	ResultSet orderProductsResultSet;
	Connection connection;
	Statement getAllPendingOrderstmt;
	Statement getOrderProductstmt;
	Vector<String> orderProductsVector = new Vector<String>();
	Vector<String> pendingOrdersVector = new Vector<String>();
	JList<String> pendingOrderList = new JList<String>();
	JList<String> currentOrderProductList = new JList<String>();
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
                    StaffDashboard staffDashboardScreen = new StaffDashboard();
                    staffDashboardScreen.setLocationRelativeTo(null);
                    staffDashboardScreen.setVisible(true);
                    setVisible(false);
                }
            });
            //*Trains Of Sheffield Header
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Pending Order Queue");
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
            
            
            JList<String> pendingOrderList = new JList<String>(pendingOrdersVector);
            pendingOrderList.setSelectedIndex(0);
            selectedOrder = pendingOrderList.getSelectedValue();
            if (selectedOrder != null) {
            	//Get values from selected value in list
                int firstCommaIndex = selectedOrder.indexOf(",");
                selectedOrderNumber = selectedOrder.substring(0, firstCommaIndex);
                int secondCommaIndex = selectedOrder.indexOf(",", firstCommaIndex + 1);
                selectedOrderCost = selectedOrder.substring(firstCommaIndex + 3, secondCommaIndex);
                int thirdCommaIndex = selectedOrder.indexOf(",", secondCommaIndex + 1);
                selectedOrderDate = selectedOrder.substring(secondCommaIndex + 2, thirdCommaIndex);
                selectedOrderCustomerID = selectedOrder.substring(thirdCommaIndex + 2, selectedOrder.length());
                //Set the text to the newly retrieved values
                orderNumber.setText("Order Number: " + selectedOrderNumber);
                orderCost.setText("Cost: £" + selectedOrderCost);
                orderDate.setText("Date: " + selectedOrderDate );
                orderCustomerID.setText("Customer ID: " + selectedOrderCustomerID);
                
                try {
                	//SELECT ALL FROM ORDER LINE WHERE ORDER_NUMBER = selectedOrderNumber
                    getOrderProductstmt = connection.createStatement();
                    //Get All Orders of status PENDING
                    orderProductsResultSet = getOrderProductstmt.executeQuery("SELECT * FROM Order_Lines WHERE order_number='" + selectedOrderNumber + "'");
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

            
            JButton deleteOrderButton = new JButton("Delete Order");
            deleteOrderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
                			if (selectedOrder != null) {
                				PreparedStatement deleteOrderLinestmt = connection.prepareStatement("DELETE FROM Order_Lines WHERE order_number='" + selectedOrderNumber + "'");
                				deleteOrderLinestmt.executeUpdate();
;                    			PreparedStatement deleteOrderstmt = connection.prepareStatement("DELETE FROM Orders WHERE order_number='" + selectedOrderNumber + "'");
                    			int rowsUpdated = deleteOrderstmt.executeUpdate();
                    			if (rowsUpdated == 1) {
        			                System.out.println("Order successfully deleted.");
        			                pendingOrdersVector.clear();
        			                orderProductsVector.clear();
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
        				                try {
        				                	//SELECT ALL FROM ORDER LINE WHERE ORDER_NUMBER = selectedOrderNumber
        				                    getOrderProductstmt = connection.createStatement();
        				                    //Get All Orders of status PENDING
        				                    orderProductsResultSet = getOrderProductstmt.executeQuery("SELECT * FROM Order_Lines WHERE order_number='" + selectedOrderNumber + "'");
        				                    while (orderProductsResultSet.next()) {
        				                        String row = orderProductsResultSet.getString("product_code") + ", " + orderProductsResultSet.getString("quantity");
        				                        orderProductsVector.add(row);
        				                    }
        				                    
        				                	repaint();
        				                } catch(SQLException e1) {
        				                	e1.printStackTrace();
        				                }
        				                
        				                JList currentOrderProductList = new JList<String>(orderProductsVector);
        				                
        				                
        				                
        				                
        				                
        				                
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
            				int selectedIndex = pendingOrderList.getSelectedIndex();
            				currentOrderProductList.setSelectedIndex(selectedIndex);
            				String selectedValue = currentOrderProductList.getSelectedValue();
            				String selectedValueProduct = selectedValue.substring(0, selectedValue.indexOf(","));
            				String selectedValueQuantity = selectedValue.substring(selectedValue.indexOf(",") + 2, selectedValue.length());
            				
                			PreparedStatement fulfillOrderstmt = connection.prepareStatement("UPDATE Orders SET status = 'FULFILLED' WHERE order_number ='" + selectedOrderNumber + "'");
                			PreparedStatement decreaseStockstmt = connection.prepareStatement("UPDATE Products SET stock = stock - '" + selectedValueQuantity + "' WHERE product_code = '" + selectedValueProduct + "'");
                			decreaseStockstmt.executeUpdate();
                			int rowsUpdated = fulfillOrderstmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Order successfully fulfilled.");
    			                //REFRESH LIST WITH UPDATED VALUES
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
    				                try {
    				                	//SELECT ALL FROM ORDER LINE WHERE ORDER_NUMBER = selectedOrderNumber
    				                    getOrderProductstmt = connection.createStatement();
    				                    //Get All Orders of status PENDING
    				                    orderProductsResultSet = getOrderProductstmt.executeQuery("SELECT * FROM Order_Lines WHERE order_number='" + selectedOrderNumber + "'");
    				                    while (orderProductsResultSet.next()) {
    				                        String row = orderProductsResultSet.getString("product_code") + ", " + orderProductsResultSet.getString("quantity");
    				                        orderProductsVector.add(row);
    				                    }
    				                    
    				                	repaint();
    				                } catch(SQLException e1) {
    				                	e1.printStackTrace();
    				                }
    				                
    				                JList currentOrderProductList = new JList<String>(orderProductsVector);
    				                
    				                
    				                
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
            //Add Pending Orders Items To Panel
            JScrollPane pendingOrderScrollableList = new JScrollPane(pendingOrderList);
            JLabel pendingOrderLabel = new JLabel("Pending Orders:");
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
    