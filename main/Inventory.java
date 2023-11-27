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

    public boolean validProductCode(String productCode, ProductType productType){
        if (productCode != null && productCode != "") {
            if (isNotProduct(productCode)) {
                if (productCode.length() > 3 && productCode.length() < 6) {
                    if (productType == ProductType.CONTROLLER && productCode.startsWith("C")) {
                        return true;                        
                    } else if (productType == ProductType.LOCOMOTIVE && productCode.startsWith("L")) {
                        return true;                        
                    } else if (productType == ProductType.ROLLINGSTOCK && productCode.startsWith("S")) {
                        return true;
                    } else if (productType == ProductType.TRACK && productCode.startsWith("R")) {
                        return true;
                    } else if (productType == ProductType.TRACKPACK && productCode.startsWith("P")) {
                        return true;
                    } else if (productType == ProductType.TRAINSET && productCode.startsWith("M")) {
                        return true;
                    }
                }
            }
        }
        System.out.println("Invalid Product Code");
        return false;
    }

    public boolean validProductName(String productname){
        if (productname != null && productname !="") {
            return true;
        }
        System.out.println("Invalid Product Name");
        return false;
    }

    public boolean validManufacturerName(String manufacturerName){
        if (manufacturerName != null && manufacturerName !="") {
            return true;
        }
        System.out.println("Invalid Manufacturer Name");
        return false;    
    }

    public boolean validRetailPrice(String retailPrice){
        if (retailPrice != null && retailPrice !="") {
            if (Integer.parseInt(retailPrice) > 0) {
                return true;
            }
        }
        System.out.println("Invalid Price");
        return false;
    }

    public boolean validStock(String stock){
        if (stock != null && stock !="") {
            if (Integer.parseInt(stock) >= 0) {
                return true;
            }
        }
        System.out.println("Invalid Stock");
        return false;
    }

    public boolean validGauge(Gauge gauge){
        return gauge != Gauge.ALL;
    }

    public boolean validScale(Scale scale){
        return scale != Scale.ALL;
    }

    public boolean validChipType(ChipType chipType){
        return chipType != ChipType.ALL;
    }

    public boolean validEraCode(String eraCode){
        if (eraCode != null && eraCode !="") {
            return true;
        }
        System.out.println("Invalid Era code");
        return false;    
    }

    public boolean validControlType(ControlType controlType){
        return controlType != ControlType.ALL;
    }

    
    public boolean validCurveRadius(CurveRadius curveRadius){
        return curveRadius != CurveRadius.ALL;
    }
    
    public boolean validTrackType(TrackType trackType){
        return trackType != TrackType.ALL;
    }

    public boolean validProduct(String productCode, ProductType productType){
        for (Product product : products) {
            if (product.getProductCode() == productCode && product.getProductType() == productType) {
                return true;
            }
        }
        return false;
    }
    
    public Product getProduct(String productCode){ //Assumes that there is a valid product since it should be called only after validProduct is called
        for (Product product : products) {
            if (product.getProductCode() == productCode) {
                return product;
            }
        }
        System.out.println("Product Not Found");
        return null;
    }

    public boolean validProductList(ArrayList<ProductPair> productList, ProductType productType, int minimumListLength){
        for (ProductPair productPair : productList) {
            if (productPair.getProduct().getProductType() != productType) {
                return false;
            }
        }
        if (productList.size() < minimumListLength) {
            return false;
        }
        return true;
    }

}
