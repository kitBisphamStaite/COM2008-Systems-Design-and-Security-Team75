import java.util.ArrayList;
//SQL Packages
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class InventoryInsert {
    private static InventoryInsert INSTANCE;
    Connection connection;

    private InventoryInsert() { 
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
    
    public static InventoryInsert getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InventoryInsert();
        }
        return INSTANCE;
    }

    public void insertProduct(Product product) throws SQLException{
        try{
            String insertSQL = "INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, product.getProductCode());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getManufacturerName());
            preparedStatement.setInt(4, product.getRetailPrice());
            preparedStatement.setInt(5, product.getStock());
            preparedStatement.setInt(6, product.getGauge().ordinal());
            preparedStatement.setInt(7, product.getScale().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + product.getProductCode());

            if(product.getProductType() == ProductType.CONTROLLER){
                INSTANCE.insertController((Controller) product);
            }
            if(product.getProductType() == ProductType.LOCOMOTIVE){
                INSTANCE.insertLocomotive((Locomotive) product);

            }
            if(product.getProductType() == ProductType.ROLLINGSTOCK){
                INSTANCE.insertRollingStock((RollingStock) product);
            }
            if(product.getProductType() == ProductType.TRACK){
                INSTANCE.insertTrack((Track) product);

            }
            if(product.getProductType() == ProductType.TRACKPACK){
                INSTANCE.insertTrackPack((TrackPack) product);
            }
            if(product.getProductType() == ProductType.TRAINSET){
                INSTANCE.insertTrainSet((TrainSet) product);
            }
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    public void insertController(Controller controller){
        try{
            String insertSQL = "INSERT INTO Controller (product_code, chip_type " + 
            ") VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, controller.getProductCode());
            preparedStatement.setInt(2, controller.GetChipType().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + controller.getProductCode());
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    
    public void insertLocomotive(Locomotive locomotive){
        try{
            String insertSQL = "INSERT INTO Locomotive (product_code, control_type, era_code) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, locomotive.getProductCode());
            preparedStatement.setInt(2, locomotive.getControlType().ordinal());
            preparedStatement.setString(3, locomotive.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + locomotive.getProductCode());
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }
    

    public void insertRollingStock(RollingStock rollingStock){
        try{
            String insertSQL = "INSERT INTO Rolling_Stock (product_code, era_code) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, rollingStock.getProductCode());
            preparedStatement.setString(2, rollingStock.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + rollingStock.getProductCode());
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    } 
    public void insertTrack(Track track){
        try{
            String insertSQL = "INSERT INTO Track (product_code, track_type, curve_radius) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, track.getProductCode());
            preparedStatement.setInt(2, track.getTrackType().ordinal());
            preparedStatement.setInt(3, track.getCurveRadius().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + track.getProductCode());
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }
    public void insertTrackPack(TrackPack trackPack){
        try{
            String insertSQL = "INSERT INTO Track_Pack (product_code) VALUES (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, trackPack.getProductCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + trackPack.getProductCode());
            INSTANCE.insertTrackList(trackPack.getTracks(), trackPack);
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }
    public void insertTrainSet(TrainSet trainSet){
        try{
            String insertSQL = "INSERT INTO Train_Set (product_code, controller_product_code, era_code) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, trainSet.getProductCode());
            preparedStatement.setString(2, trainSet.getController().getProductCode());
            preparedStatement.setString(3, trainSet.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully - " + trainSet.getProductCode());

            INSTANCE.insertLocomotiveList(trainSet.getLocomotives(), trainSet);
            INSTANCE.insertRollingStockList(trainSet.getRollingStocks(), trainSet);
            INSTANCE.insertTrackPackList(trainSet.getTrackPacks(), trainSet);
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }
    public void insertTrackList(ArrayList<ProductPair> tracks, Product product){
        for(ProductPair productPair: tracks){
            try{
                String insertSQL = "INSERT INTO Track_Linker (track_product_code, track_pack_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, productPair.getProduct().getProductCode());
                preparedStatement.setString(2, product.getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully - " + product.getProductCode() + ", " + productPair.getProduct().getProductCode());
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    } public void insertLocomotiveList(ArrayList<ProductPair> locomotives, Product product){
        for(ProductPair productPair: locomotives){
            try{
                String insertSQL = "INSERT INTO Locomotive_Linker (train_set_product_code, locomotive_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, product.getProductCode());
                preparedStatement.setString(2, productPair.getProduct().getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully - " + product.getProductCode() + ", " + productPair.getProduct().getProductCode());
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }    public void insertRollingStockList(ArrayList<ProductPair> rollingStocks, Product product){
        for(ProductPair productPair: rollingStocks){
            try{
                String insertSQL = "INSERT INTO Rolling_Stock_Linker (train_set_product_code, rolling_stock_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, product.getProductCode());
                preparedStatement.setString(2, productPair.getProduct().getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully - " + product.getProductCode() + ", " + productPair.getProduct().getProductCode());
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }
    public void insertTrackPackList(ArrayList<ProductPair> trackPacks, Product product){
        for(ProductPair productPair: trackPacks){
            try{
                String insertSQL = "INSERT INTO Track_Pack_Linker (train_set_product_code, track_pack_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, product.getProductCode());
                preparedStatement.setString(2, productPair.getProduct().getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully - " + product.getProductCode() + ", " + productPair.getProduct().getProductCode());
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }
}
