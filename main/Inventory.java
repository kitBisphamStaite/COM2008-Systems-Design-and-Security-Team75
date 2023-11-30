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

    public void fillArrayList(ArrayList<ProductPair> arrayList, String productCode){
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
            INSTANCE.initProducts();
        }
        return products;
    }

    public ArrayList<Product> getProducts(ProductType productType) {
        if (products.isEmpty()){
            INSTANCE.initProducts();
        }
        ArrayList<Product> productsFiltered = new ArrayList<Product>();
        for (Product product : products){
            if (product.getProductType() == productType){
                productsFiltered.add(product);
            }
        }

        return productsFiltered;
    }

    public void addProduct(Product product){
        products.add(product);
        try{
            InventoryInsert.getInstance().insertProduct(product);
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

   
}