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
	private Connection connection;
	private String orderNumber;
	private DefaultListModel<OrderLine> listModel;
	private Integer costOfBasket;
	private String accountId;
	private String stockMessage;
	private String purchaseMessage;
	
	
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
    	purchaseMessage = "";
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
            	dispose();
            	Home.main(null);
            }
        });
        
        //Create Footer buttons
        purchaseButton = new JButton("Purchase");
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		stockMessage = validateStock();
            		if (stockMessage == "" && validateBankDetails() == "") {
            			if (listModel.size() != 0) {
            				PreparedStatement updateOrder = connection.prepareStatement("UPDATE Orders SET status='PENDING' WHERE (order_number = ?)");
    	            		updateOrder.setString(1, orderNumber);
    	            		updateOrder.execute();
    	            		updateOrder.close();
    	            		updateProductsStock();
    	            		purchaseMessage = "Purchase Reseved Thank you so much for your new order";
    	            		resetViewBasketAndFooterPanels();
                		}
            		}
            		if (validateBankDetails() != "") {
                    	EditBank.main(null);
                    	dispose();
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
    	
    	//Display stockMessage
    	stockMessage = validateStock();
    	if (stockMessage!="") {
    		JLabel stockLable = new JLabel("Unable to make purchas because "+stockMessage);
    		stockLable.setForeground(Color.red);
    		viewBasketPanel.add(stockLable, BorderLayout.SOUTH);
    	}
    	
    	//Display purchaseMessage
    	if (purchaseMessage != "") {
    		JLabel purchaseLable = new JLabel(purchaseMessage);
    		purchaseLable.setForeground(Color.green);
    		viewBasketPanel.add(purchaseLable, BorderLayout.SOUTH);
    	}
    	
    	//Display bank details message
    	String bankMessage = validateBankDetails();
    	if (bankMessage != "") {
    		JLabel bankMessageLable = new JLabel(bankMessage);
    		bankMessageLable.setForeground(Color.red);
    		viewBasketPanel.add(bankMessageLable, BorderLayout.SOUTH);
    	}
    	
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
    		PreparedStatement getAllBasketOrderstmt = connection.prepareStatement("SELECT * FROM Orders WHERE status='BASKET' AND customer_id=?");
    		PreparedStatement getAllBasketOrderLinestmt = connection.prepareStatement("SELECT * FROM Order_Lines WHERE order_number=?");
        	costOfBasket=0;
        	
        	getAllBasketOrderstmt.setString(1, accountId);
        	ResultSet basketOrdersResultSet = getAllBasketOrderstmt.executeQuery();
        	if (basketOrdersResultSet.next()) {
        		orderNumber = basketOrdersResultSet.getString("order_number");
        		costOfBasket = basketOrdersResultSet.getInt("cost");
        		
        		getAllBasketOrderLinestmt.setString(1,orderNumber);
        		ResultSet basketOrderLinesResultSet = getAllBasketOrderLinestmt.executeQuery();
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
    
    public String validateBankDetails() {
    	try {
    		PreparedStatement getBankDetails = connection.prepareStatement("SELECT * FROM Bank_Details WHERE customer_id=?");
    		getBankDetails.setString(1, accountId);
    		ResultSet bankDetails = getBankDetails.executeQuery();
    		if (bankDetails.next()) {
    			String cardNumber = bankDetails.getString("card_number");
    			if (bankDetails.wasNull()) {
    				return "Bank Details need updating";
    			}
    			String holderName = bankDetails.getString("holder_name");
    			if (bankDetails.wasNull()) {
    				return "Bank Details need updating";
    			}
    			String cardName = bankDetails.getString("card_name");
    			if (bankDetails.wasNull()) {
    				return "Bank Details need updating";
    			}
    			String expiryDate = bankDetails.getString("expiry_date");
    			if (bankDetails.wasNull()) {
    				return "Bank Details need updating";
    			}
    			String securityCode = bankDetails.getString("security_code");
    			if (bankDetails.wasNull()) {
    				return "Bank Details need updating";
    			}
    		}else{
    			return "No Bank details avablible please add them in account settings";
    		}
    		
    	}catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    	return "";
    }
    
    public String validateStock() {
    	try {
    		for(int i=0; i<listModel.size(); i++) {
    			//Get quantity
        		OrderLine nextOrderLine = listModel.get(i);
        		Integer quantity = nextOrderLine.getQuantity();
        		String productCode =  nextOrderLine.getProduct();
        		
        		//GetStock
        		PreparedStatement getProductDetails = connection.prepareStatement("SELECT product_name, stock FROM Products WHERE product_code=?");
        		getProductDetails.setString(1, productCode);
        		ResultSet productDetails = getProductDetails.executeQuery();
        		if (productDetails.next()) {
        			Integer stock = productDetails.getInt("stock");
        			String prodName = productDetails.getString("product_name");
        			//Validate
        			if (stock < quantity) {
        				return (prodName+" only has "+stock+" items left in stock.");
        			}
        		}
        	}
    	}catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    	
    	return "";
    }
    
    public void updateProductsStock() {
    	try {
    		for(int i=0; i<listModel.size(); i++) {
    			//Get quantity
        		OrderLine nextOrderLine = listModel.get(i);
        		Integer quantity = nextOrderLine.getQuantity();
        		String productCode =  nextOrderLine.getProduct();
        		
        		//Get Stock
        		PreparedStatement getProductDetails = connection.prepareStatement("SELECT stock FROM Products WHERE product_code=?");
        		getProductDetails.setString(1, productCode);
        		ResultSet productDetails = getProductDetails.executeQuery();
        		if (productDetails.next()) {
        			Integer stock = productDetails.getInt("stock");
        			
        			//Update Stock
            		PreparedStatement updateProductStock = connection.prepareStatement("UPDATE Products SET stock=? WHERE (product_code = ?)");
            		updateProductStock.setInt(1, stock-quantity);
            		updateProductStock.setString(2, productCode);
            		updateProductStock.execute();
        		}
        	}
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