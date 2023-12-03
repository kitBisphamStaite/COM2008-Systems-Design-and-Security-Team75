import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderLine {
	private String orderNumber;
	private String productCode;
	private Integer quantity;
	private Statement getProductstmt;
	private Connection connection;
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
	
	
    public OrderLine(String orderNumber, String productCode, Integer quantity) {
    	this.orderNumber = orderNumber;
    	this.productCode = productCode;
    	this.quantity = quantity;
    }  
    
    public String getOrderNumber() {
    	return orderNumber;
    }
    public String getProduct() {
    	return productCode;
    }
    public Integer getQuantity() {
    	return quantity;
    }
    
    @Override
    public String toString() {
    	try {
    		createConnection();
    		getProductstmt = connection.createStatement();
    		ResultSet productResultSet = getProductstmt.executeQuery("SELECT * FROM Products WHERE product_code='"+productCode+"'");
            if (productResultSet.next()) {
            	return (productResultSet.getString("product_name") + ", "  + productResultSet.getString("manufacturer_name") + ", £" + productResultSet.getString("retail_price") + " x" + quantity);
            }
            getProductstmt.close();
    	}catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    	return "";
    	
    }
    
    void createConnection() {
    	try {
    		connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
    		
    	}catch (SQLException e) {
        	//ERROR IN CONNECTING TO DATABASE
            e.printStackTrace();
        }
    }
}