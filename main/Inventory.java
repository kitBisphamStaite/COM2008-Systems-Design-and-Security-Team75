import java.util.ArrayList;
//SQL Packages
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Inventory {
    private static Inventory INSTANCE;
    private ArrayList<Product> products = new ArrayList<Product>();
    Connection connection;
    private Inventory() { 
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
    
    public static Inventory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Inventory();
        }
        return INSTANCE;
    }

    // Track codes start with R, controller codes with C, locomotives with L, rolling stock with S, train sets with M, and track packs with P

    public void initProducts(){
        try {
            String selectSQL = "SELECT * FROM Products";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String productCode = resultSet.getString("product_code");
                String productName = resultSet.getString("product_name");
                String manufacturerName = resultSet.getString("manufacturer_name");
                int retailPrice = resultSet.getInt("retail_price");
                int stock = resultSet.getInt("stock");
                Gauge gauge =  Gauge.values()[resultSet.getInt("gauge")];
                Scale scale = Scale.values()[resultSet.getInt("scale")];
                if(resultSet.getString("product_code").startsWith("C")){
                    String controllerSQL = "SELECT * FROM Controller WHERE product_code=?";
                    PreparedStatement preparedStatementController = connection.prepareStatement(controllerSQL);
                    preparedStatementController.setString(1, resultSet.getString("product_code"));
                    
                    ResultSet resultSetController = preparedStatementController.executeQuery();

                    resultSetController.next();

                    ChipType chipType = ChipType.values()[resultSetController.getInt("chip_type")];
                    products.add(new Controller(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, chipType));
                }
                if(resultSet.getString("product_code").startsWith("L")){
                    String locomotiveSQL = "SELECT * FROM Locomotive WHERE product_code=?";
                    PreparedStatement preparedStatementLocomotive = connection.prepareStatement(locomotiveSQL);
                    preparedStatementLocomotive.setString(1, resultSet.getString("product_code"));
                    
                    ResultSet resultSetLocomotive = preparedStatementLocomotive.executeQuery();
                    resultSetLocomotive.next();
                    String eraCode = resultSetLocomotive.getString("era_code");
                    ControlType controlType = ControlType.values()[resultSetLocomotive.getInt("control_type")];
                    products.add(new Locomotive(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, eraCode, controlType));
                }
                if(resultSet.getString("product_code").startsWith("P")){
                    String trackPackSQL = "SELECT * FROM Track_Pack WHERE product_code=?";
                    PreparedStatement preparedStatementTrackPack = connection.prepareStatement(trackPackSQL);
                    preparedStatementTrackPack.setString(1, resultSet.getString("product_code"));
                    
                    ResultSet resultSetTrackPack = preparedStatementTrackPack.executeQuery();
                    resultSetTrackPack.next();
                    ArrayList<ProductPair> tracks = new ArrayList<ProductPair>();
                    fillArrayList(tracks, productCode);
                    products.add(new TrackPack(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, tracks));
                }
                if(resultSet.getString("product_code").startsWith("M")){
                    String trainSetSQL = "SELECT * FROM Train_Set WHERE product_code=?";
                    PreparedStatement preparedStatementTrainSet = connection.prepareStatement(trainSetSQL);
                    preparedStatementTrainSet.setString(1, resultSet.getString("product_code"));
                    
                    ResultSet resultSetTrainSet = preparedStatementTrainSet.executeQuery();
                    resultSetTrainSet.next();
                    String eraCode = resultSetTrainSet.getString("era_code");
                    Controller controller = (Controller) getProduct(resultSetTrainSet.getString("controller_product_code"));
                    ArrayList<ProductPair> locomotives = new ArrayList<ProductPair>();
                    fillArrayList(locomotives, productCode);
                    ArrayList<ProductPair> rollingStocks = new ArrayList<ProductPair>();
                    fillArrayList(rollingStocks, productCode);
                    ArrayList<ProductPair> trackPacks = new ArrayList<ProductPair>();
                    fillArrayList(trackPacks, productCode);
                    products.add(new TrainSet(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, eraCode, controller, locomotives, rollingStocks, trackPacks));
                }
                if(resultSet.getString("product_code").startsWith("S")){
                    String rollingStockSQL = "SELECT * FROM Rolling_Stock WHERE product_code=?";
                    PreparedStatement preparedStatementRollingStock = connection.prepareStatement(rollingStockSQL);
                    preparedStatementRollingStock.setString(1, resultSet.getString("product_code"));

                    ResultSet resultSetRollingStock = preparedStatementRollingStock.executeQuery();
                    resultSetRollingStock.next();
                    String eraCode = resultSetRollingStock.getString("era_code");
                    products.add(new RollingStock(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, eraCode));
                }
                if(resultSet.getString("product_code").startsWith("R")){
                    String trackSQL = "SELECT * FROM Track WHERE product_code=?";
                    PreparedStatement preparedStatementTrack = connection.prepareStatement(trackSQL);
                    preparedStatementTrack.setString(1, resultSet.getString("product_code"));
                    ResultSet resultSetTrack = preparedStatementTrack.executeQuery();
                    resultSetTrack.next();
                    TrackType trackType = TrackType.values()[resultSetTrack.getInt("track_type")];
                    CurveRadius curveRadius = CurveRadius.values()[resultSetTrack.getInt("curve_radius")];
                    products.add(new Track(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, curveRadius, trackType));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void fillArrayList(ArrayList<ProductPair> arrayList, String productCode){
        if (productCode.startsWith("R")){
            try {
                String selectSQL = "SELECT * FROM Track_Pack_Linker WHERE track_pack_product_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, productCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    arrayList.add(new ProductPair(getProduct(resultSet.getString("track_product_code")), resultSet.getInt("quantity")));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }    
        }
        if (productCode.startsWith("S")){
            try {
                String selectSQL = "SELECT * FROM Rolling_Stock_Linker WHERE train_set_product_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, productCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    arrayList.add(new ProductPair(getProduct(resultSet.getString("rolling_stock_linker")), resultSet.getInt("quantity")));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }        }
        if (productCode.startsWith("P")){
            try {
                String selectSQL = "SELECT * FROM Track_Pack_Linker WHERE train_set_product_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, productCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    arrayList.add(new ProductPair(getProduct(resultSet.getString("track_pack_product_code")), resultSet.getInt("quantity")));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }        }
        if (productCode.startsWith("L")){
            try {
                String selectSQL = "SELECT * FROM Locomotive_Linker WHERE train_set_product_code=?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, productCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    arrayList.add(new ProductPair(getProduct(resultSet.getString("locomotive_product_code")), resultSet.getInt("quantity")));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Product> getProducts() {
        if (products.isEmpty()){
            System.out.println("Inital Products");
            //INSTANCE.createDefaultProductList();
            INSTANCE.initProducts();
        }
        return products;
    }

    public void addProduct(Product product){
        products.add(product);
        try{
            insertProduct(product);
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Added Product");
    }

    public boolean isNotProduct(String productCode){
        for (Product product : products) {
            if (product.getProductCode().equals(productCode)) {
                return false;
            }
        }
        return true;
    }
    
    public Product getProduct(String productCode){ //Assumes that there is a valid product since it should be called only after validProduct is called
        for (Product product : products) {
            if (product.getProductCode().equals(productCode)) {
                System.out.println("Product Found");
                return product;
            }
        }
        System.out.println("Product Not Found");
        return null;
    }

    public void updateProduct(Product updatedProduct){
        Product oldProduct = getProduct(updatedProduct.getProductCode());
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setManufacturerName(updatedProduct.getManufacturerName());
        oldProduct.setRetailPrice(updatedProduct.getRetailPrice());
        oldProduct.setStock(updatedProduct.getStock());
        oldProduct.setGauge(updatedProduct.getGauge());
        oldProduct.setScale(updatedProduct.getScale());

        try {
            String updateSQL = "UPDATE Products SET product_name=?, manufacturer_name=?, retail_price=?, stock=?, gauge=?, scale-? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, updatedProduct.getProductName());
            preparedStatement.setString(2, updatedProduct.getManufacturerName());
            preparedStatement.setInt(3, updatedProduct.getRetailPrice());
            preparedStatement.setInt(4, updatedProduct.getStock());
            preparedStatement.setInt(5, updatedProduct.getGauge().ordinal());
            preparedStatement.setInt(6, updatedProduct.getScale().ordinal());
            preparedStatement.setString(7, updatedProduct.getProductCode());
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
            String updateSQL = "UPDATE Controller chip_type=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setInt(1, updatedProduct.GetChipType().ordinal());
            preparedStatement.setString(2, updatedProduct.getProductCode());
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
        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("updateRollingStock");
    }
    
    public void updateTrack(Track oldProduct, Track updatedProduct){
        oldProduct.setCurveRadius(updatedProduct.getCurveRadius());
        oldProduct.setTrackType(updatedProduct.getTrackType());
        
        try {
            String updateSQL = "UPDATE Track SET track_type=?, curve_radius-? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setInt(1, updatedProduct.getTrackType().ordinal());
            preparedStatement.setInt(2, updatedProduct.getCurveRadius().ordinal());
            preparedStatement.setString(3, updatedProduct.getProductCode());
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        System.out.println("updateTrack");
    }
    
    public void updateTrackPack(TrackPack oldProduct, TrackPack updatedProduct){
        oldProduct.setTracks(updatedProduct.getTracks());

        INSTANCE.deleteTrackList(oldProduct);
        INSTANCE.insertTrackList(updatedProduct.getTracks(), updatedProduct);
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
        } catch (SQLException e){
            e.printStackTrace();
        }
        INSTANCE.deleteLocomotiveList(oldProduct);
        INSTANCE.deleteRollingStockList(oldProduct);
        INSTANCE.deleteTrackPackList(oldProduct);
        INSTANCE.insertLocomotiveList(updatedProduct.getLocomotives(), updatedProduct);
        INSTANCE.insertRollingStockList(updatedProduct.getRollingStocks(), updatedProduct);
        INSTANCE.insertTrackPackList(updatedProduct.getTrackPacks(), updatedProduct);

        System.out.println("updateTrainSet");
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
            System.out.println(rowsAffected + " rows(s) inserted successfully");

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

    public void deleteProduct(Product product){
        try {
            String deleteSQL = "DELETE FROM Products WHERE product_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, product.getProductCode());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println(rowsAffected + " row(s) deleted successfully");
            } else {
                System.out.println("No rows were deleted for product_code: " + product.getProductCode());
            }

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
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    

    private void insertController(Controller controller){
        try{
            String insertSQL = "INSERT INTO Controller (product_code, chip_type " + 
            ") VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, controller.getProductCode());
            preparedStatement.setInt(2, controller.GetChipType().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    private void deleteController(Controller controller){
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

    private void insertLocomotive(Locomotive locomotive){
        try{
            String insertSQL = "INSERT INTO Locomotive (product_code, control_type, era_code) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, locomotive.getProductCode());
            preparedStatement.setInt(2, locomotive.getControlType().ordinal());
            preparedStatement.setString(3, locomotive.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    
    private void deleteLocomotive(Locomotive locomotive){
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
    

    private void insertRollingStock(RollingStock rollingStock){
        try{
            String insertSQL = "INSERT INTO Rolling_Stock (product_code, era_code) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, rollingStock.getProductCode());
            preparedStatement.setString(2, rollingStock.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }
    
    private void deleteRollingStock(RollingStock rollingStock){
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

    private void insertTrack(Track track){
        try{
            String insertSQL = "INSERT INTO Track (product_code, track_type, curve_radius) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, track.getProductCode());
            preparedStatement.setInt(2, track.getTrackType().ordinal());
            preparedStatement.setInt(3, track.getCurveRadius().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }
    
    private void deleteTrack(Track track){
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

    private void insertTrackPack(TrackPack trackPack){
        try{
            String insertSQL = "INSERT INTO Track_Pack (product_code) VALUES (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, trackPack.getProductCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
            INSTANCE.insertTrackList(trackPack.getTracks(), trackPack);
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    private void deleteTrackPack(TrackPack trackPack){
        try {
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

    private void insertTrainSet(TrainSet trainSet){
        try{
            String insertSQL = "INSERT INTO Train_Set (product_code, controller_product_code, era_code) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, trainSet.getProductCode());
            preparedStatement.setString(2, trainSet.getController().getProductCode());
            preparedStatement.setString(3, trainSet.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");

            INSTANCE.insertLocomotiveList(trainSet.getLocomotives(), trainSet);
            INSTANCE.insertRollingStockList(trainSet.getRollingStocks(), trainSet);
            INSTANCE.insertTrackPackList(trainSet.getTrackPacks(), trainSet);
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    private void deleteTrainSet(TrainSet trainSet){
        try {
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

    private void insertTrackList(ArrayList<ProductPair> tracks, Product product){
        for(ProductPair productPair: tracks){
            try{
                String insertSQL = "INSERT INTO Track_Linker (track_product_code, track_pack_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, productPair.getProduct().getProductCode());
                preparedStatement.setString(2, product.getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " rows(s) inserted successfully");
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }

    private void deleteTrackList(Product product){
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
    
    private void insertLocomotiveList(ArrayList<ProductPair> locomotives, Product product){
        for(ProductPair productPair: locomotives){
            try{
                String insertSQL = "INSERT INTO Locomotive_Linker (train_set_product_code, locomotive_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, product.getProductCode());
                preparedStatement.setString(2, productPair.getProduct().getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " rows(s) inserted successfully");
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }
    
    private void deleteLocomotiveList(Product product){
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
    
    private void insertRollingStockList(ArrayList<ProductPair> rollingStocks, Product product){
        for(ProductPair productPair: rollingStocks){
            try{
                String insertSQL = "INSERT INTO Rolling_Stock_Linker (train_set_product_code, rolling_stock_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, product.getProductCode());
                preparedStatement.setString(2, productPair.getProduct().getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " rows(s) inserted successfully");
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }
    

    private void deleteRollingStockList(Product product){
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

    private void insertTrackPackList(ArrayList<ProductPair> trackPacks, Product product){
        for(ProductPair productPair: trackPacks){
            try{
                String insertSQL = "INSERT INTO Track_Pack_Linker (train_set_product_code, track_pack_product_code, quantity) VALUES (?, ?, ?)";
    
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, product.getProductCode());
                preparedStatement.setString(2, productPair.getProduct().getProductCode());
                preparedStatement.setInt(3, productPair.getQuantity());
                int rowsAffected  = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " rows(s) inserted successfully");
            }
            catch (SQLException e1){
                e1.printStackTrace();
            } 
        }
    }

    private void deleteTrackPackList(Product product){
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