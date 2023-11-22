public class TrainSet extends Product {
    private String eraCode;
    private Controller controller;
    private Locomotive[] locomotives;
    private RollingStock[] rollingStocks;
    private TrackPack[] trackPacks;

    public TrainSet(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale, String eraCode, Controller controller, Locomotive[] locomotives, RollingStock[] rollingStocks, TrackPack[] trackPacks){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale);
        this.controller = controller;
        this.locomotives = locomotives;
        this.rollingStocks = rollingStocks;
        this.trackPacks = trackPacks;
    }

    //get set
    public String getEraCode() {
        return eraCode;
    }
    public Controller getController() {
        return controller;
    }
    public Locomotive[] getLocomotives() {
        return locomotives;
    }
    public RollingStock[] getRollingStocks() {
        return rollingStocks;
    }
    public TrackPack[] getTrackPacks() {
        return trackPacks;
    }
    public void setEraCode(String eraCode) {
        this.eraCode = eraCode;
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void setLocomotives(Locomotive[] locomotives) {
        this.locomotives = locomotives;
    }
    public void setRollingStocks(RollingStock[] rollingStocks) {
        this.rollingStocks = rollingStocks;
    }
    public void setTrackPacks(TrackPack[] trackPacks) {
        this.trackPacks = trackPacks;
    }
    
}