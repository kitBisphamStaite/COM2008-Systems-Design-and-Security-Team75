import java.security.*;
import java.sql.*;

public class HashedPasswordGenerator {

    public static String hashPassword(char[] password, String username, Connection connection) {
        try {
            //Create a MessageDigest for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            String salt = getSalt(username, connection);

            //Join the salt and password
            byte[] saltedPasswordBytes = concatenateBytes(salt.getBytes(), new String(password).getBytes());

            //Update the digest with the salted password
            md.update(saltedPasswordBytes);

            //Get the hashed password
            byte[] hashedPasswordBytes = md.digest();

            //Convert the hashed password into hexadecimal
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String hashPassword(char[] password, String username, String salt, Connection connection) {
    	try {
            //Create a MessageDigest for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //Join the salt and password
            byte[] saltedPasswordBytes = concatenateBytes(salt.getBytes(), new String(password).getBytes());

            //Update the digest with the salted password
            md.update(saltedPasswordBytes);

            //Get the hashed password
            byte[] hashedPasswordBytes = md.digest();

            //Convert the hashed password into hexadecimal
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] concatenateBytes(byte[] arr1, byte[] arr2) {
        byte[] combined = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
        return combined;
    }
    

	private static String getSalt(String username, Connection connection) {
        try {
        	//Create SQL statement and query the SQL Database
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
    	//Generate random salt
    	SecureRandom RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return String.valueOf(salt);
      }
}