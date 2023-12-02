//SQL Packages
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class InventoryUpdate {

    private static InventoryUpdate INSTANCE;
    Connection connection;

    private InventoryUpdate() { 
        //Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }
    }
    
    public static InventoryUpdate getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InventoryUpdate();
        }
        return INSTANCE;
    }

    public void updateProduct(Product updatedProduct){
        Product oldProduct = Inventory.getInstance().getProduct(updatedProduct.getProductCode());
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setManufacturerName(updatedProduct.getManufacturerName());
        oldProduct.setRetailPrice(updatedProduct.getRetailPrice());
        oldProduct.setStock(updatedProduct.getStock());
        oldProduct.setGauge(updatedProduct.getGauge());
        oldProduct.setScale(updatedProduct.getScale());

        try {
            String updateSQL = "UPDATE Products SET product_name=?, manufacturer_name=?, retail_price=?, stock=?, gauge=?, scale=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, updatedProduct.getProductName());
            preparedStatement.setString(2, updatedProduct.getManufacturerName());
            preparedStatement.setInt(3, updatedProduct.getRetailPrice());
            preparedStatement.setInt(4, updatedProduct.getStock());
            preparedStatement.setInt(5, updatedProduct.getGauge().ordinal());
            preparedStatement.setInt(6, updatedProduct.getScale().ordinal());
            preparedStatement.setString(7, updatedProduct.getProductCode());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println(rowsUpdated + " row(s) updated Successfully");
            } else {
                System.out.println("No rows were updated for Product Code: " + updatedProduct.getProductCode());
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        if (updatedProduct.getProductType() == ProductType.CONTROLLER){
            updateController((Controller) oldProduct, (Controller) updatedProduct);
        }
        if (updatedProduct.getProductType() == ProductType.LOCOMOTIVE){
            updateLocomotive((Locomotive) oldProduct, (Locomotive) updatedProduct);
        }
        if (updatedProduct.getProductType() == ProductType.ROLLINGSTOCK){
            updateRollingStock((RollingStock) oldProduct, (RollingStock) updatedProduct);
        }
        if (updatedProduct.getProductType() == ProductType.TRACK){
            updateTrack((Track) oldProduct, (Track) updatedProduct);
        }
        if (updatedProduct.getProductType() == ProductType.TRACKPACK){
            updateTrackPack((TrackPack) oldProduct, (TrackPack) updatedProduct);
        }
        if (updatedProduct.getProductType() == ProductType.TRAINSET){
            updateTrainSet((TrainSet) oldProduct, (TrainSet) updatedProduct);
        }
        //then need to sort out the database connection
        System.out.println("Updated Product");
        updatedProduct = null; //Will be removed by java
    }

    public void updateController(Controller oldProduct, Controller updatedProduct){
        oldProduct.SetChipType(updatedProduct.GetChipType());

        try {
            String updateSQL = "UPDATE Controller SET chip_type=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setInt(1, updatedProduct.GetChipType().ordinal());
            preparedStatement.setString(2, updatedProduct.getProductCode());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println(rowsUpdated + " row(s) updated Successfully");
            } else {
                System.out.println("No rows were updated for Product Code: " + updatedProduct.getProductCode());
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("updateController");
    }
    
    public void updateLocomotive(Locomotive oldProduct, Locomotive updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        oldProduct.setControlType(updatedProduct.getControlType());

        try {
            String updateSQL = "UPDATE Locomotive SET control_type=?, era_code=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setInt(1, updatedProduct.getControlType().ordinal());
            preparedStatement.setString(2, updatedProduct.getEraCode());
            preparedStatement.setString(3, updatedProduct.getProductCode());
            
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println(rowsUpdated + " row(s) updated Successfully");
            } else {
                System.out.println("No rows were updated for Product Code: " + updatedProduct.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        System.out.println("updateLocomotive");
    }
    
    public void updateRollingStock(RollingStock oldProduct, RollingStock updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());

        try {
            String updateSQL = "UPDATE Rolling_Stock SET era_code=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, updatedProduct.getEraCode());
            preparedStatement.setString(2, updatedProduct.getProductCode());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println(rowsUpdated + " row(s) updated Successfully");
            } else {
                System.out.println("No rows were updated for Product Code: " + updatedProduct.getProductCode());
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("updateRollingStock");
    }
    
    public void updateTrack(Track oldProduct, Track updatedProduct){
        oldProduct.setCurveRadius(updatedProduct.getCurveRadius());
        oldProduct.setTrackType(updatedProduct.getTrackType());
        
        try {
            String updateSQL = "UPDATE Track SET track_type=?, curve_radius=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setInt(1, updatedProduct.getTrackType().ordinal());
            preparedStatement.setInt(2, updatedProduct.getCurveRadius().ordinal());
            preparedStatement.setString(3, updatedProduct.getProductCode());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println(rowsUpdated + " row(s) updated Successfully");
            } else {
                System.out.println("No rows were updated for Product Code: " + updatedProduct.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        System.out.println("updateTrack");
    }
    
    public void updateTrackPack(TrackPack oldProduct, TrackPack updatedProduct){
        oldProduct.setTracks(updatedProduct.getTracks());

        InventoryDelete.getInstance().deleteTrackList(oldProduct);
        InventoryInsert.getInstance().insertTrackList(updatedProduct.getTracks(), updatedProduct);
        System.out.println("updateTrackPack");
    }

    public void updateTrainSet(TrainSet oldProduct, TrainSet updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        oldProduct.setController(updatedProduct.getController());
        oldProduct.setLocomotives(updatedProduct.getLocomotives());
        oldProduct.setRollingStocks(updatedProduct.getRollingStocks());
        oldProduct.setTrackPacks(updatedProduct.getTrackPacks());

        try {
            String updateSQL = "UPDATE Train_Set SET controller_product_code=?, era_code=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1, updatedProduct.getController().getProductCode());
            preparedStatement.setString(2, updatedProduct.getEraCode());
            preparedStatement.setString(3, updatedProduct.getProductCode());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println(rowsUpdated + " row(s) updated Successfully");
            } else {
                System.out.println("No rows were updated for Product Code: " + updatedProduct.getProductCode());
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        InventoryDelete.getInstance().deleteLocomotiveList(oldProduct);
        InventoryDelete.getInstance().deleteRollingStockList(oldProduct);
        InventoryDelete.getInstance().deleteTrackPackList(oldProduct);
        InventoryInsert.getInstance().insertLocomotiveList(updatedProduct.getLocomotives(), updatedProduct);
        InventoryInsert.getInstance().insertRollingStockList(updatedProduct.getRollingStocks(), updatedProduct);
        InventoryInsert.getInstance().insertTrackPackList(updatedProduct.getTrackPacks(), updatedProduct);

        System.out.println("updateTrainSet");
    }
}
