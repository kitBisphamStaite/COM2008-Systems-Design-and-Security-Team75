import java.util.ArrayList;

public class Inventory {
    private static Inventory INSTANCE;
    private ArrayList<Product> products = new ArrayList<Product>();
    private Inventory() {        
    }
    
    public static Inventory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Inventory();
        }
        INSTANCE.createDefaultProductList();
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
        //replace all the data
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setManufacturerName(updatedProduct.getManufacturerName());
        oldProduct.setRetailPrice(updatedProduct.getRetailPrice());
        oldProduct.setStock(updatedProduct.getStock());
        oldProduct.setGauge(updatedProduct.getGauge());
        oldProduct.setScale(updatedProduct.getScale());
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setProductName(updatedProduct.getProductName());

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
    }

    public void updateController(Controller oldProduct, Controller updatedProduct){
        oldProduct.SetChipType(updatedProduct.GetChipType());
    }
    
    public void updateLocomotive(Locomotive oldProduct, Locomotive updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        oldProduct.setControlType(updatedProduct.getControlType());
    }
    
    public void updateRollingStock(RollingStock oldProduct, RollingStock updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
    }
    
    public void updateTrack(Track oldProduct, Track updatedProduct){
        oldProduct.setCurveRadius(updatedProduct.getCurveRadius());
        oldProduct.setTrackType(updatedProduct.getTrackType());
    }
    
    public void updateTrackPack(TrackPack oldProduct, TrackPack updatedProduct){
        oldProduct.setTracks(updatedProduct.getTracks());
    }

    public void updateTrainSet(TrainSet oldProduct, TrainSet updatedProduct){
        oldProduct.setEraCode(updatedProduct.getEraCode());
        oldProduct.setController(updatedProduct.getController());
        oldProduct.setLocomotives(updatedProduct.getLocomotives());
        oldProduct.setRollingStocks(updatedProduct.getRollingStocks());
        oldProduct.setTrackPacks(updatedProduct.getTrackPacks());
    }
}
