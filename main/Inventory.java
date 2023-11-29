import java.util.ArrayList;
//SQL Packages
import java.sql.Statement;
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

        //What SQL should do
        //  Get a list of all products in the db and should make an ArrayList of product objects
        //  Add Products into the db and into the ArrayList
        //  Update products in the db and the ArrayList

        //Remember the search query
    }
    
    public static Inventory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Inventory();
        }
        INSTANCE.createDefaultProductList();
        INSTANCE.getAllProducts();

        return INSTANCE;
    }

    // Track codes start with R, controller codes with C, locomotives with L, rolling stock with S, train sets with M, and track packs with P
    public void createDefaultProductList(){
        ArrayList<ProductPair> locomotives = new ArrayList<ProductPair>();
        ArrayList<ProductPair> trackPacks = new ArrayList<ProductPair>();
        ArrayList<ProductPair> rollingStocks = new ArrayList<ProductPair>();

        ArrayList<ProductPair> tracks = new ArrayList<ProductPair>();

        products.clear();
        products.add(new Controller("C001", "Controller 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, ChipType.ANALOGUE));
        products.add(new Controller("C002", "Controller 2", "John", 10, 1, Gauge.N, Scale.SEVENTYSIXTH, ChipType.ANALOGUE));
        products.add(new Track("R001", "Track 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, CurveRadius.FIRST, TrackType.SINGLE_CURVE));
        products.add(new Track("R002", "Track 2", "John", 10, 1, Gauge.N, Scale.SEVENTYSIXTH, CurveRadius.FIRST, TrackType.SINGLE_CURVE));
        products.add(new Locomotive("L001", "Locomotive 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, "Era 4", ControlType.ANALOGUE));
        products.add(new Locomotive("L002", "Locomotive 2", "John", 10, 1, Gauge.N, Scale.SEVENTYSIXTH, "Era 4", ControlType.ANALOGUE));
        products.add(new RollingStock("S001", "Rolling 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, "Era 4"));
        products.add(new RollingStock("S002", "Rolling 2", "John", 10, 1, Gauge.N, Scale.SEVENTYSIXTH, "Era 4"));
        products.add(new TrainSet("M001", "Train Set 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, "Era4", new Controller("C001", "Controller 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, ChipType.ANALOGUE), locomotives, rollingStocks, trackPacks));
        products.add(new TrainSet("M002", "Train Set 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, "Era4", new Controller("C001", "Controller 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, ChipType.ANALOGUE), locomotives, rollingStocks, trackPacks));
        products.add(new TrackPack("P001", "Track Pack 1", "John", 5, 1, Gauge.N, Scale.SEVENTYSIXTH, tracks));
        products.add(new TrackPack("P002", "Track Pack 2", "John", 10, 1, Gauge.N, Scale.SEVENTYSIXTH, tracks));
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        products.add(product);
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
        //find the product in the list
        Product oldProduct = getProduct(updatedProduct.getProductCode());
        //replace all the data - but Product Code remains the same
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setManufacturerName(updatedProduct.getManufacturerName());
        oldProduct.setRetailPrice(updatedProduct.getRetailPrice());
        oldProduct.setStock(updatedProduct.getStock());
        oldProduct.setGauge(updatedProduct.getGauge());
        oldProduct.setScale(updatedProduct.getScale());


        //Controller
        if (updatedProduct.getProductType() == ProductType.CONTROLLER){
            updateController((Controller) oldProduct, (Controller) updatedProduct);
        }
        //Locomotive
        if (updatedProduct.getProductType() == ProductType.LOCOMOTIVE){
            updateLocomotive((Locomotive) oldProduct, (Locomotive) updatedProduct);
        }
        //Rolling Stock
        if (updatedProduct.getProductType() == ProductType.ROLLINGSTOCK){
            updateRollingStock((RollingStock) oldProduct, (RollingStock) updatedProduct);
        }
        //Track
        if (updatedProduct.getProductType() == ProductType.TRACK){
            updateTrack((Track) oldProduct, (Track) updatedProduct);
        }
        //Track Pack
        if (updatedProduct.getProductType() == ProductType.TRACKPACK){
            updateTrackPack((TrackPack) oldProduct, (TrackPack) updatedProduct);
        }
        //Train Set
        if (updatedProduct.getProductType() == ProductType.TRAINSET){
            updateTrainSet((TrainSet) oldProduct, (TrainSet) updatedProduct);
        }
        //then need to sort out the database connection
        System.out.println("Updated Product");
        updatedProduct = null; //Will be removed by java
    }

    public void updateController(Controller oldProduct, Controller updatedProduct){
        oldProduct.SetChipType(updatedProduct.GetChipType());
        System.out.println("updateController");
    }
    
    public void updateLocomotive(Locomotive oldProduct, Locomotive updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        oldProduct.setControlType(updatedProduct.getControlType());
        System.out.println("updateLocomotive");
    }
    
    public void updateRollingStock(RollingStock oldProduct, RollingStock updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        System.out.println("updateRollingStock");
    }
    
    public void updateTrack(Track oldProduct, Track updatedProduct){
        oldProduct.setCurveRadius(updatedProduct.getCurveRadius());
        oldProduct.setTrackType(updatedProduct.getTrackType());
        System.out.println("updateTrack");
    }
    
    public void updateTrackPack(TrackPack oldProduct, TrackPack updatedProduct){
        oldProduct.setTracks(updatedProduct.getTracks());
        System.out.println("updateTrackPack");
    }

    public void updateTrainSet(TrainSet oldProduct, TrainSet updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        oldProduct.setController(updatedProduct.getController());
        oldProduct.setLocomotives(updatedProduct.getLocomotives());
        oldProduct.setRollingStocks(updatedProduct.getRollingStocks());
        oldProduct.setTrackPacks(updatedProduct.getTrackPacks());
        System.out.println("updateTrainSet");
    }

    public void getAllProducts(){
        try {
            Statement getAllProducts = connection.createStatement();
            ResultSet pendingProductResultSet = getAllProducts.executeQuery("SELECT * FROM Products");
            System.out.println("Query for All Products");
            while (pendingProductResultSet.next()){
                System.out.println(pendingProductResultSet.getString("product_code"));
                //Get the rest of the information and put it into a object for the correct type of product
                //The Enums are stored as a int
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertProduct(Product product) throws SQLException{
        try{
            String insertSQL = "INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, product.getProductCode());
            //Name
            preparedStatement.setString(2, product.getProductName());
            //Name
            preparedStatement.setString(3, product.getManufacturerName());
            //Price
            preparedStatement.setInt(4, product.getRetailPrice());
            //Stock
            preparedStatement.setInt(5, product.getStock());
            //Gauge
            preparedStatement.setInt(6, product.getGauge().ordinal());
            //Scale
            preparedStatement.setInt(7, product.getScale().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");

            //Controller
            if(product.getProductType() == ProductType.CONTROLLER){
                INSTANCE.insertController((Controller) product);
            }
            //Locomotive
            if(product.getProductType() == ProductType.LOCOMOTIVE){
                INSTANCE.insertLocomotive((Locomotive) product);

            }
            //Rolling Stock
            if(product.getProductType() == ProductType.ROLLINGSTOCK){
                INSTANCE.insertRollingStock((RollingStock) product);
            }
            //Track
            if(product.getProductType() == ProductType.TRACK){
                INSTANCE.insertTrack((Track) product);

            }
            //TrackPack
            if(product.getProductType() == ProductType.TRACKPACK){
                INSTANCE.insertTrackPack((TrackPack) product);
            }
            //TrainSet
            if(product.getProductType() == ProductType.TRAINSET){
                INSTANCE.insertTrainSet((TrainSet) product);
            }
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    private void insertController(Controller controller){
        try{
            String insertSQL = "INSERT INTO Controller (product_code, chip_type " + 
            ") VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, controller.getProductCode());
            //Chip Type
            preparedStatement.setInt(2, controller.GetChipType().ordinal());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    private void insertLocomotive(Locomotive locomotive){
        try{
            String insertSQL = "INSERT INTO Locomotive (product_code, control_type, era_code) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, locomotive.getProductCode());
            //Control Type
            preparedStatement.setInt(2, locomotive.getControlType().ordinal());
            //Era Code
            preparedStatement.setString(3, locomotive.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
        }
    }

    private void insertRollingStock(RollingStock rollingStock){
        try{
            String insertSQL = "INSERT INTO Rolling_Stock (product_code, era_code) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, rollingStock.getProductCode());
            //era Code
            preparedStatement.setString(2, rollingStock.getEraCode());

            int rowsAffected  = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows(s) inserted successfully");
        }
        catch (SQLException e1){
            e1.printStackTrace();
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

    private void insertTrainSet(TrainSet trainSet){
        try{
            String insertSQL = "INSERT INTO Train_Set (product_code, controller_product_code, era_code) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //Code
            preparedStatement.setString(1, trainSet.getProductCode());
            //Name
            preparedStatement.setString(2, trainSet.getController().getProductCode());
            //Name
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
}