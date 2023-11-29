import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class HashedPasswordGenerator {
    private static final String SALT = "MyStaticSalt"; // Replace with your own static salt

    public static String hashPassword(char[] password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Concatenate the salt and password bytes
            byte[] saltedPasswordBytes = concatenateBytes(SALT.getBytes(), new String(password).getBytes());

            // Update the digest with the salted password bytes
            md.update(saltedPasswordBytes);

            // Get the hashed password bytes
            byte[] hashedPasswordBytes = md.digest();

            // Convert the hashed password bytes to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception, e.g., log it or throw a custom exception
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concatenateBytes(byte[] arr1, byte[] arr2) {
        byte[] combined = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
        return combined;
    }
    
    public static Boolean getSalt(String username) {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
            
            String sql = "SELECT password_salt FROM Accounts WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
	    	ResultSet resultSet = statement.executeQuery(sql);
	    	
	    	if (resultSet.next()) {
	    		return true;
	    	}else {
	    		return false;
	    	}
            
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }finally {
        	return false;
        }
    }

    public static void main(String[] args) {
  
        char[] password = "asd@456".toCharArray();
        String hashedPassword = hashPassword(password);

        System.out.println("Original Password: " + String.valueOf(password));
        System.out.println("Hashed Password: " + hashedPassword);
    }
}