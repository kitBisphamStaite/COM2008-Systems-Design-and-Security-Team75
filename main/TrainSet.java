import java.util.ArrayList;

public class TrainSet extends Product {
    private String eraCode;
    private Controller controller;
    private ArrayList<ProductPair> locomotives;
    private ArrayList<ProductPair> rollingStocks;
    private ArrayList<ProductPair> trackPacks;

    public TrainSet(String productCode, String productName, String manufacturerName,int retailPrice, int stock, 
                    Gauge gauge, Scale scale, String eraCode, Controller controller, ArrayList<ProductPair> locomotives, 
                    ArrayList<ProductPair> rollingStocks, ArrayList<ProductPair> trackPacks){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, ProductType.TRAINSET);
        this.controller = controller;
        this.locomotives = locomotives;
        this.rollingStocks = rollingStocks;
        this.trackPacks = trackPacks;
    }

    public String getEraCode() {
        return eraCode;
    }
    public Controller getController() {
        return controller;
    }
    public ArrayList<ProductPair> getLocomotives() {
        return locomotives;
    }
    public ArrayList<ProductPair> getRollingStocks() {
        return rollingStocks;
    }
    public ArrayList<ProductPair> getTrackPacks() {
        return trackPacks;
    }
    public void setEraCode(String eraCode) {
        this.eraCode = eraCode;
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void setLocomotives(ArrayList<ProductPair> locomotives) {
        this.locomotives = locomotives;
    }
    public void setRollingStocks(ArrayList<ProductPair> rollingStocks) {
        this.rollingStocks = rollingStocks;
    }
    public void setTrackPacks(ArrayList<ProductPair> trackPacks) {
        this.trackPacks = trackPacks;
    }

    public void addToLocomotives(Locomotive track, int quantity){
        boolean inList = false;
        for (ProductPair productPair : locomotives) {
            if (productPair.getProduct() == track) {
                inList = true;
                productPair.addQuantity(quantity);
            }
        }
        if (!inList) {
            locomotives.add(new ProductPair(track, quantity));
        }
    }

    public void addToRollingStock(RollingStock rollingStock, int quantity){
        boolean inList = false;
        for (ProductPair productPair : rollingStocks) {
            if (productPair.getProduct() == rollingStock) {
                inList = true;
                productPair.addQuantity(quantity);
            }
        }
        if (!inList) {
            rollingStocks.add(new ProductPair(rollingStock, quantity));
        }
    }

    public void addToTrackPacks(TrackPack trackPack, int quantity){
        boolean inList = false;
        for (ProductPair productPair : trackPacks) {
            if (productPair.getProduct() == trackPack) {
                inList = true;
                productPair.addQuantity(quantity);
            }
        }
        if (!inList) {
            trackPacks.add(new ProductPair(trackPack, quantity));
        }
    }
    
    @Override
    public String getProductDetails(){
        return (getProductCode() + ", " + controller.getProductCode() + ", " + eraCode);
    }

}