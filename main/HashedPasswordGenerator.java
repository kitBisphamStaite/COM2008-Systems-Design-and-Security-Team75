import java.security.*;
import java.sql.*;

public class HashedPasswordGenerator {

    public static String hashPassword(char[] password, String username, Connection connection) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            String salt = getSalt(username, connection);
            System.out.println(salt);

            // Concatenate the salt and password bytes
            byte[] saltedPasswordBytes = concatenateBytes(salt.getBytes(), new String(password).getBytes());

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
    

	public static String getSalt(String username, Connection connection) {
        try {
            String sql = "SELECT password_salt FROM Accounts WHERE email = '" + username + "'";
            PreparedStatement statement = connection.prepareStatement(sql);
	    	ResultSet resultSet = statement.executeQuery(sql);
	    	
	    	if (resultSet.next()) {
	    		String salt = resultSet.getString("password_salt");
	    		return salt;
	    	}else {
	    		return "";
	    	}
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static String getNewSalt() {
    	SecureRandom RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return String.valueOf(salt);
      }
}