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

public class ViewBasket extends JFrame {
	private ResultSet basketOrdersResultSet;
	private ResultSet basketOrderLinesResultSet;
	private Connection connection;
	private PreparedStatement getAllBasketOrderstmt;
	private PreparedStatement getAllBasketOrderLinestmt;
	private PreparedStatement updateOrder;
	private String orderNumber;
	private DefaultListModel<OrderLine> listModel;
	private Integer costOfBasket;
	private String accountId;
	
	
	JList orderLinesUI;
	JPanel viewBasketPanel;
	JPanel footerPanel;
	JButton removeItemButton;
	JButton purchaseButton;
	JLabel totalCostFooter;
	
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
	
    public ViewBasket() {
    	//Create connection
    	createConnection();
    	//Get logged in account id
    	accountId = String.valueOf(Login.getUserID());
    	
    	//Set Up Frame
        setTitle("View Basket");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //Create Header and Footer Panels
        JPanel headerPanel = new JPanel(new BorderLayout());
        footerPanel = new JPanel(new GridLayout(0,2));
        viewBasketPanel = new JPanel(new BorderLayout());
        
        //Create Header Panel Items
        //*Buttons
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
            	Home.main(null);
            }
        });
        
        //Create Footer buttons
        purchaseButton = new JButton("Purchase");
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		if (listModel.size() != 0) {
	            		updateOrder = connection.prepareStatement("UPDATE Orders SET status='PENDING' WHERE (order_number = ?)");
	            		updateOrder.setString(1, orderNumber);
	            		updateOrder.execute();
	            		updateOrder.close();
	            		resetViewBasketAndFooterPanels();
            		}
            	}catch (SQLException ex) {
                	//ERROR IN CONNECTING TO DATABASE
                    ex.printStackTrace();
                }
            	
            }
        });
        
        removeItemButton = new JButton("Remove Item");
        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	        	 if (orderLinesUI.getSelectedValue() != null) {
	        		 Integer selectedIndex = orderLinesUI.getSelectedIndex();
	        		 OrderLine selectedOrderLine = listModel.get(selectedIndex);
	        		 String selectedOrderNum = selectedOrderLine.getOrderNumber();
	        		 String selectedProduct = selectedOrderLine.getProduct();
	        		 
	        		 try {
	        			 //Delete selected
	        			 PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM Order_Lines WHERE (order_number = ?) and (product_code = ?)");
	        			 deleteStmt.setString(1, selectedOrderNum);
	        			 deleteStmt.setString(2, selectedProduct);
	        			 deleteStmt.execute();
	        			 deleteStmt.close();
	        			 
	        			 //GetProduct cost
	        			 PreparedStatement getProductCost = connection.prepareStatement("SELECT retail_price FROM Products WHERE product_code = ?");
	        			 getProductCost.setString(1, selectedProduct);
	        			 ResultSet productResultSet = getProductCost.executeQuery();
	        			 Integer productCost = 0;
	        			 if (productResultSet.next()) {
	        				 productCost = productResultSet.getInt("retail_price");
	        			 }
	        			 
	        			 //Calculate new cost
	        			 int newCost = costOfBasket - (productCost * selectedOrderLine.getQuantity());
	        			 
	        			 //Update cost
	        			 PreparedStatement updateCost = connection.prepareStatement("UPDATE Orders SET cost=? WHERE (order_number = ?)");
	        			 updateCost.setInt(1, newCost);
	        			 updateCost.setString(2, selectedOrderNum);
	        			 updateCost.execute();
	        			 
	        			 costOfBasket = newCost;
	        			 resetViewBasketAndFooterPanels();
	        		 }catch (SQLException ex) {
	        	        	//ERROR IN CONNECTING TO DATABASE
	        	            ex.printStackTrace();
	        	        }
	        		 
	             }
            }
        });
        
        //HEADER LABELS
        JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Basket");
        trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
        
        //Add Items To Header Panel
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
        //Create ViewBasket and Footer Panels
        resetViewBasketAndFooterPanels();
        
        //Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
        add(viewBasketPanel, BorderLayout.CENTER);
        
        
        
        setVisible(true);
    }
    
    public void resetViewBasketAndFooterPanels(){
    	viewBasketPanel.removeAll();
    	viewBasketPanel.repaint();
    	viewBasketPanel.revalidate();
    	
    	//Get Basket
    	makeBasketList();
    	
    	//Display Order Lines
    	orderLinesUI = new JList<>(listModel);
    	orderLinesUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	viewBasketPanel.add(orderLinesUI);
    	
    	//Display footer
    	footerPanel.removeAll();
    	totalCostFooter = new JLabel("Total cost: £"+costOfBasket);
    	totalCostFooter.setHorizontalAlignment(JLabel.CENTER);
    	footerPanel.add(totalCostFooter);
        footerPanel.add(purchaseButton);
        footerPanel.add(removeItemButton);
    }
    
    public void makeBasketList() {
    	try {
    		listModel = new DefaultListModel<>();
    		getAllBasketOrderstmt = connection.prepareStatement("SELECT * FROM Orders WHERE status='BASKET' AND customer_id=?");
            getAllBasketOrderLinestmt = connection.prepareStatement("SELECT * FROM Order_Lines WHERE order_number=?");
        	costOfBasket=0;
        	
        	getAllBasketOrderstmt.setString(1, accountId);
        	basketOrdersResultSet = getAllBasketOrderstmt.executeQuery();
        	if (basketOrdersResultSet.next()) {
        		orderNumber = basketOrdersResultSet.getString("order_number");
        		costOfBasket = basketOrdersResultSet.getInt("cost");
        		
        		getAllBasketOrderLinestmt.setString(1,orderNumber);
        		basketOrderLinesResultSet = getAllBasketOrderLinestmt.executeQuery();
        		while (basketOrderLinesResultSet.next()) {
        			Integer quantity = basketOrderLinesResultSet.getInt("quantity");
        			String productCode = basketOrderLinesResultSet.getString("product_code");
    	    		OrderLine row = new OrderLine(orderNumber, productCode, quantity);
    		    	listModel.addElement(row);
    	    		
        		}
        	
        		
        	}
        	getAllBasketOrderstmt.close();
        	getAllBasketOrderLinestmt.close();
    	}catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    	 
    }
    
    void createConnection() {
    	try {
    		connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
    		
    	}catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ViewBasket();
    }   
}