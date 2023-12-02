//SQL Packages
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class InventoryDelete {
    
    private static InventoryDelete INSTANCE;
    Connection connection;

    private InventoryDelete() { 
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
    
    public static InventoryDelete getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InventoryDelete();
        }
        return INSTANCE;
    }

    public void deleteProduct(Product product){
        try {
            if(product.getProductType() == ProductType.CONTROLLER){
                INSTANCE.deleteController((Controller) product);
            }
            if(product.getProductType() == ProductType.LOCOMOTIVE){
                INSTANCE.deleteLocomotive((Locomotive) product);

            }
            if(product.getProductType() == ProductType.ROLLINGSTOCK){
                INSTANCE.deleteRollingStock((RollingStock) product);
            }
            if(product.getProductType() == ProductType.TRACK){
                INSTANCE.deleteTrack((Track) product);

            }
            if(product.getProductType() == ProductType.TRACKPACK){
                INSTANCE.deleteTrackPack((TrackPack) product);
            }
            if(product.getProductType() == ProductType.TRAINSET){
                INSTANCE.deleteTrainSet((TrainSet) product);
            }

            String deleteSQL = "DELETE FROM Products WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, product.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + product.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void deleteController(Controller controller){
        try {
            String deleteSQL = "DELETE FROM Controller WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, controller.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + controller.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteLocomotive(Locomotive locomotive){
        try {
            String deleteSQL = "DELETE FROM Locomotive WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, locomotive.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + locomotive.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteRollingStock(RollingStock rollingStock){
        try {
            String deleteSQL = "DELETE FROM Rolling_Stock WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, rollingStock.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + rollingStock.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteTrack(Track track){
        try {
            String deleteSQL = "DELETE FROM Track WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, track.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + track.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteTrackPack(TrackPack trackPack){
        try {
            deleteTrackList(trackPack);
            String deleteSQL = "DELETE FROM Track_Pack WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, trackPack.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + trackPack.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteTrainSet(TrainSet trainSet){
        try {
            deleteLocomotiveList(trainSet);
            deleteTrackPackList(trainSet);
            deleteRollingStockList(trainSet);
            String deleteSQL = "DELETE FROM Train_Set WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, trainSet.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + trainSet.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteTrackList(Product product){
        try {
            String deleteSQL = "DELETE FROM Track_Linker WHERE track_pack_product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, product.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for track_pack_product_code: " + product.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteLocomotiveList(Product product){
        try {
            String deleteSQL = "DELETE FROM Locomotive_Linker WHERE train_set_product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, product.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for train_set_product_code: " + product.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void deleteRollingStockList(Product product){
        try {
            String deleteSQL = "DELETE FROM Rolling_Stock_Linker WHERE train_set_product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, product.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for train_set_product_code: " + product.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteTrackPackList(Product product){
        try {
            String deleteSQL = "DELETE FROM Track_Pack_Linker WHERE train_set_product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, product.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for Track_Pack_Linker: " + product.getProductCode());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
}
